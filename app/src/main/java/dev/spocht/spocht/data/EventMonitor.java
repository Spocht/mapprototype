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
    private enum EventMonitorState{
        IN_RANGE (0.21),
        OUT_RANGE (0.19);

        private final double maxDistanceToEventInKilometers;
        private EventMonitorState(final double distance)
        {
            maxDistanceToEventInKilometers = distance;
        }
        public EventMonitorState execute(final Event event, final Location location)
        {
            EventMonitorState res=this;
            switch(this)
            {
                case IN_RANGE:
                    if(null != event) {//only run if there is a valid object available
                        if (new GeoPoint(location).distanceInKilometersTo(event.facility().location()) >=
                                maxDistanceToEventInKilometers) {
                            res = OUT_RANGE;
                        } else {
                            Log.d("spocht.eventMonitor", "Location still in range. Noop.");
                        }
                    }
                    break;
                case OUT_RANGE:
                    if(null != event) {//only run if there is a valid object available
                        if (new GeoPoint(location).distanceInKilometersTo(event.facility().location()) <=
                                maxDistanceToEventInKilometers) {
                            res = IN_RANGE;
                        } else {
                            Log.d("spocht.eventMonitor", "Location still in range. Noop.");
                        }
                    }
                    break;
                default:
                    Log.d("spocht.eventMonitor","Reached default of switch case in an enum ...  something went wrong!");
                    break;
            }
            return res;
        }
    }
    private EventMonitorState state;

    public EventMonitor(Context ctx) {
        state = EventMonitorState.OUT_RANGE;
        MyLocationListener.create(ctx);
        MyLocationListener.getInstance().register(
                new LocationCallback<Void, Location>() {
                    @Override
                    public Void operate(Location location) {
                        if(null != event()) {//only run if there is a valid object available
                            EventMonitorState next = state.execute(event(),location);
                            if(next != state)
                            {
                                switch(next)
                                {
                                    case IN_RANGE:
                                        Log.d("spocht.eventMonitor","set foot in danger zone");
                                        break;
                                    case OUT_RANGE:
                                        Log.d("spocht.eventMonitor", "Location too far away. Checking out.");
                                        event().checkOut(DataManager.getInstance().currentUser());
                                        setEvent(null);
                                        break;
                                }
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
