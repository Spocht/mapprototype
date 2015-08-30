package dev.spocht.spocht.monitor;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseGeoPoint;

import dev.spocht.spocht.callbacks.LocationCallback;
import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.Facility;
import dev.spocht.spocht.listener.MyLocationListener;

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
        myLocationListener = new MyLocationListener(
                ctx,
                new LocationCallback<Void, Location>() {
                    @Override
                    public Void operate(Location location) {
                        newParseGeoPoint.setLatitude(location.getLatitude());
                        newParseGeoPoint.setLongitude(location.getLongitude());
                        if (newParseGeoPoint.distanceInKilometersTo(eventParseGeoPoint) >=
                                maxDistanceToEventInKilometers) {
                            System.out.println("Location too far away. Checking out.");
                        } else {
                            System.out.println("Location still in range. Noop.");
                        }

                        return null;
                    }
                },
                false
         );
    }





}
