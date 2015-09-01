package dev.spocht.spocht.data;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Facility")
public class Facility extends ParseData {
    static private GeoPoint defaultPoint = new GeoPoint(33.0,44.0);
    public Facility()
    {//default constructor for Parse.com
        ;
    }
    public Facility(final String name)
    {
        this(name, defaultPoint, 0);
    }
    public Facility(final String name, final GeoPoint location, final int numberOfFields)
    {
        this(name, location, numberOfFields, new Sport("unknown", 0));
    }
    public Facility(final String name, final GeoPoint location, final int numberOfFields, final Sport sport)
    {
        setName(name);
        setLocation(location);
        setNumberOfFields(numberOfFields);
        setSport(sport);
    }
    public void setName(final String name)
    {
        put("name", name);
        setUpdated();
    }
    public String name()
    {
        String name = getString("name");
        if(null == name)
        {
            name = new String("unknown");
        }
        return (name);
    }
    public void setLocation(final GeoPoint location)
    {
        put("location",location);
        setUpdated();
    }
    public GeoPoint location()
    {
        GeoPoint location = new GeoPoint(getParseGeoPoint("location"));
        if(null == location)
        {
            location = defaultPoint;
        }
        return(location);
    }
    public boolean isInRange(final double range, final GeoPoint locationClient)
    {
        if(locationClient == null)
        {
            return(false);
        }
        double distance = locationClient.distanceInKilometersTo(location());
        if(distance < range)
        {
            return (true);
        }
        else
        {
            return(false);
        }
    }
    public void setNumberOfFields(final int num)
    {
        put("numberOfFields",num);
        setUpdated();
    }
    public int numberOfFields()
    {
        int num = getInt("numberOfFields");
        if(num <1)
        {
            num = 1;
        }
        return(num);
    }
    public void setComment(final String comment)
    {//todo: maybe an "add" would be better, so multiple comments would be available
        put("comment",comment);
        setUpdated();
    }
    public String comment()
    {
        String comment = getString("comment");
        if(null == comment)
        {
            comment = "Zone X-Ray";
        }
        return(comment);
    }
    public void setRating(final double rating)
    {
        double old = rating();
        put("rating",(old+rating)/2); //todo: review rating stuff
        setUpdated();
    }
    public double rating()
    {
        double val = (double)get("rating");
        if(val <0)
        {
            val=0;
        }
        return (val);
    }
    public void setImage(final Image pic)
    {
        if(null == pic.getObjectId())
        {
            pic.saveEventually(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        put("image", ParseObject.createWithoutData(Image.class, pic.getObjectId()));
                        setUpdated();
                        if(pic.clearUpdated())
                        {
                            pic.persist();
                        }
                    } else {
                        System.out.println("Error while saving image object");
                    }
                }
            });
        }
        else {
            put("image", ParseObject.createWithoutData(Image.class, pic.getObjectId()));
            setUpdated();
        }
    }
    public Image image()
    {
        Image pic = (Image)get("image");
        if(null == pic)
        {
            if(this.has("image")) {
                try {
                    pic = this.getParseObject("image").fetchIfNeeded();
                } catch (com.parse.ParseException e) {
                    //todo log?!
                    pic = new Image();
                }
            }
            else
            {
                pic = new Image();
            }
        }
        return(pic);
    }
    public void setSport(final Sport sport)
    {
        if(null == sport.getObjectId())
        {
            sport.saveEventually(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        put("sport", ParseObject.createWithoutData(Sport.class, sport.getObjectId()));
                        setUpdated();
                        if(sport.clearUpdated())
                        {
                            sport.persist();
                        }
                    } else {
                        System.out.println("Error while saving sport object");
                    }
                }
            });
        }
        else {
            put("sport", ParseObject.createWithoutData(Sport.class, sport.getObjectId()));
            setUpdated();
        }
    }
    public Sport sport()
    {
        Sport sport = (Sport)get("sport");
        if(null == sport)
        {
            if(this.has("sport")) {
                try {
                    sport = this.getParseObject("sport").fetchIfNeeded();
                } catch (com.parse.ParseException e) {
                    //todo log?!
                    sport = new Sport("unknown", 0);
                }
            }
            else
            {
                sport = new Sport("unknown", 0);
            }
        }
        return(sport);
    }
    public void setEvent(final Event event)
    {
        if(numberOfFields() > events().size()) {
            if (null == event.getObjectId()) {
                event.saveEventually(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            addUnique("events", ParseObject.createWithoutData(Event.class, event.getObjectId()));
                            setUpdated();
                            if(event.clearUpdated())
                            {
                                event.persist();
                            }
                        } else {
                            System.out.println("Error while saving event object");
                        }
                    }
                });
            } else {
                addUnique("events", ParseObject.createWithoutData(Event.class, event.getObjectId()));
                setUpdated();
            }
        }
    }
    public List<Event> events()
    {
        List<Event> events = getList("events");
        if(null == events)
        {
            if(this.has("events")) {
                try {
                    events = this.getParseObject("event").fetchIfNeeded();
                } catch (com.parse.ParseException e) {
                    //todo log?!
                    events = new ArrayList<Event>();
                }
            }
            else
            {
                events = new ArrayList<Event>();
            }
        }
        return(events);
    }
    public Event addEvent()
    {
        return addEvent("Kesselrun"); //todo: change default event name
    }
    public Event addEvent(final String name)
    {
        Event event = new Event(name);
        setEvent(event);
        if((null == event.facility().getObjectId())||
           (!event.facility().getObjectId().equals(this.getObjectId())))
        {
            event.setFacility(this);
        }
        return event;
    }

}
