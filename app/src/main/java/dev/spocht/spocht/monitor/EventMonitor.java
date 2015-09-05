package dev.spocht.spocht.monitor;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;


import java.util.HashMap;
import java.util.Map;

import dev.spocht.spocht.location.LocationCallback;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.Facility;
import dev.spocht.spocht.location.MyLocationListener;

/**
 * Created by edm on 26.08.15.
 */
public class EventMonitor {

    private Facility facility = new Facility();

    private Event event;

    private double maxDistanceToEventInKilometers = 0.2;


    ParseGeoPoint eventParseGeoPoint = new ParseGeoPoint(33.0 ,44.0);
    //the ParseGeoPoint used in myLocationListener
    ParseGeoPoint newParseGeoPoint = new ParseGeoPoint(33.0, 44.0);

    MyLocationListener myLocationListener;


    public EventMonitor(Context ctx) {
//        facility.generateTestData();
        MyLocationListener.create(ctx);
        MyLocationListener.getInstance().register(
                new LocationCallback<Void, Location>() {
                    @Override
                    public Void operate(Location location) {
                        newParseGeoPoint.setLatitude(location.getLatitude());
                        newParseGeoPoint.setLongitude(location.getLongitude());
                        if (newParseGeoPoint.distanceInKilometersTo(eventParseGeoPoint) >=
                                maxDistanceToEventInKilometers) {
                            Log.d("spocht.eventMonitor", "Location too far away. Checking out.");
                            Map<String, Map<String, String>> cloudParams = new HashMap<>();
                            Map <String, String> event = new HashMap<>();
                            event.put("id", event.get("objectId"));
                            Map <String, String> user = new HashMap<>();
                            user.put("id", ParseUser.getCurrentUser().getObjectId());
                            cloudParams.put("event", event);
                            cloudParams.put("user", user);

                            try {
                                ParseCloud.callFunction("checkout", cloudParams);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.d("spocht.eventMonitor","Location still in range. Noop.");
                        }

                        return null;
                    }
                },
                false
        );
    }





}
