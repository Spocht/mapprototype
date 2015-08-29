package dev.spocht.spocht.data;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by edm on 11.08.15.
 */
@ParseClassName("Facility")
public class Facility extends ParseData {
    public Facility()
    {//default constructor for Parse.com
        ;
    }
    public void setName(final String name)
    {
        put("name", name);
    }
    public String name()
    {
        String name = getString("name");
        if(null == name)
        {
            name = "unknown";
        }
        return (name);
    }
    public void setLocation(final GeoPoint location)
    {
        put("location",location);
    }
    public GeoPoint location()
    {
        GeoPoint location = (GeoPoint)get("location");
        if(null == location)
        {
            location = new GeoPoint(33.0,44.0);
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


//    ParseGeoPoint location;
//    public void generateTestData() {
//        location = new ParseGeoPoint(33.0,44.0);
//        put("location", location);
//        Event event = new Event();
//        try {
//            event.save();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        event.put("associatedFacility", this);
//        event.saveInBackground();
//
//    }
}
