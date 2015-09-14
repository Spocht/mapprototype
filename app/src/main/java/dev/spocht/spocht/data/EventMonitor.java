package dev.spocht.spocht.data;

import android.content.Context;
import android.util.Log;

import dev.spocht.spocht.geoFence.GeoFence;
import dev.spocht.spocht.geoFence.GeoFenceCallback;

/**
 * Created by edm on 26.08.15.
 */
public class EventMonitor {
    private Event       mEvent=null;
    private GeoFence    mFence=null;
    private Context     mCtx;
    private float mThreshhold;
    private GeoFenceCallback cbIn=new GeoFenceCallback() {
        @Override
        public void action() {
            Log.d(this.getClass().getCanonicalName(),"coming in danger zone");
            //do nothing when entering the fence;
        }
    };
    private GeoFenceCallback cbOut = new GeoFenceCallback() {
        @Override
        public void action() {
            Log.d(this.getClass().getCanonicalName(), "leaving danger zone");
            setEvent(null);
        }
    };

    public EventMonitor(Context ctx) {
        mCtx=ctx;
        // spits out an exception somehow
        // mThreshhold = DataManager.getContext().getResources().getDimension(R.dimen.geofence_threshold);
    }

    public Event event() {
        return mEvent;
    }

    public void setEvent(Event event) {
        if (null != mEvent) {
            Event tmp=mEvent;
            mEvent = null;
            tmp.checkOut(DataManager.getInstance().currentUser());
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
