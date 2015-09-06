package dev.spocht.spocht.data;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.parse.ParseGeoPoint;

import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.GeoPoint;
import dev.spocht.spocht.geoFence.GeoFence;
import dev.spocht.spocht.geoFence.GeoFenceCallback;
import dev.spocht.spocht.location.LocationCallback;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.data.Facility;
import dev.spocht.spocht.location.MyLocationListener;

/**
 * Created by edm on 26.08.15.
 */
public class EventMonitor {
    private Event       mEvent=null;
    private GeoFence    mFence=null;
    private Context     mCtx;
    private GeoFenceCallback cbIn=new GeoFenceCallback() {
        @Override
        public void action() {
            Log.d("spocht.eventMonitor","coming in danger zone");
            //do nothing when entering the fence;
        }
    };
    private GeoFenceCallback cbOut = new GeoFenceCallback() {
        @Override
        public void action() {
            Log.d("spocht.eventMonitor","leaving danger zone");
            if(null != event())
            {
                setEvent(null);
            }
        }
    };

    public EventMonitor(Context ctx) {
        mCtx=ctx;
    }

    public Event event() {
        return mEvent;
    }

    public void setEvent(Event event) {
        if (null != mEvent) {
            mEvent.checkOut(DataManager.getInstance().currentUser());
        }
        if(null != event)
        {
            mFence = new GeoFence(event.facility().location().toLocation(),0.2,cbIn,cbOut,mCtx);
        }
        else
        {
            mFence = null;
        }
        mEvent = event;
    }



}