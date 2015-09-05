package dev.spocht.spocht.data;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.parse.ParseGeoPoint;

import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.GeoPoint;
import dev.spocht.spocht.location.LocationCallback;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.Facility;
import dev.spocht.spocht.location.MyLocationListener;

/**
 * Created by edm on 26.08.15.
 */
public class EventMonitor {


    private Event mEvent=null;
    private double maxDistanceToEventInKilometers = 0.2;

    public EventMonitor(Context ctx) {
        MyLocationListener.create(ctx);
        MyLocationListener.getInstance().register(
                new LocationCallback<Void, Location>() {
                    @Override
                    public Void operate(Location location) {
                        if(null != event()) {//only run if there is a valid object available
                            if (new GeoPoint(location).distanceInKilometersTo(event().facility().location()) >=
                                    maxDistanceToEventInKilometers) {
                                Log.d("spocht.eventMonitor", "Location too far away. Checking out.");
                                event().checkOut(DataManager.getInstance().currentUser());
                                setEvent(null);
                            } else {
                                Log.d("spocht.eventMonitor", "Location still in range. Noop.");
                            }
                        }
                        return null;
                    }
                },
                false
         );
    }

    public Event event() {
        return mEvent;
    }

    public void setEvent(Event event) {
        if (null != mEvent) {
            mEvent.checkOut(DataManager.getInstance().currentUser());
        }
        mEvent = event;
    }



}
