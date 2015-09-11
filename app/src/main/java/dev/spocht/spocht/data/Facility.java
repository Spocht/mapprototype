package dev.spocht.spocht.data;

import android.util.Log;

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
        String name = null;
        this.fetchIfNeeded();
        name = getString("name");
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
        GeoPoint location = null;
        this.fetchIfNeeded();
        location = new GeoPoint(getParseGeoPoint("location"));
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
        int num = 0;
        this.fetchIfNeeded();
        num = getInt("numberOfFields");
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
        String comment = null;
        this.fetchIfNeeded();
        comment = getString("comment");
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
        double val = 0;
        this.fetchIfNeeded();
        val = (double)get("rating");
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
                        Log.e(this.getClass().getCanonicalName(), "Error saving data.", e);
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
        Image pic = null;
        this.fetchIfNeeded();
        pic = (Image)get("image");
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
                        Log.e(this.getClass().getCanonicalName(), "Error saving data.", e);
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
        Sport sport = null;

        this.fetchIfNeeded();
        sport = (Sport)get("sport");
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
                            Log.e(this.getClass().getCanonicalName(), "Error saving data.", e);
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
        List<Event> events = null;
        this.fetchIfNeeded();
        events = getList("events");
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
    public void updateEvents(final InfoRetriever<Facility> callback)
    {
        Log.d(this.getClass().getCanonicalName(),"Updating "+ this.name());
        ParseQuery<Facility> query = ParseQuery.getQuery(Facility.class);
        query.include("events.participants.user.user");
        query.getInBackground(this.getObjectId(), new GetCallback<Facility>() {
            @Override
            public void done(Facility facility, ParseException e) {
                if(null == e)
                {
                    callback.operate(facility);
                }
                else
                {
                    Log.e(this.getClass().getCanonicalName(),"Fail to update",e);
                }
            }
        });
    }

}
