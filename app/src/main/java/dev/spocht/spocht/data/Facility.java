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

    ParseGeoPoint location;



    public void generateTestData() {
        location = new ParseGeoPoint(33.0,44.0);
        put("location", location);
        Event event = new Event();
        try {
            event.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        event.put("associatedFacility", this);
        event.saveInBackground();

    }
}
