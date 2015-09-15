package dev.spocht.spocht.geoFence;

import android.content.Context;
import android.location.Location;

import java.util.HashMap;
import java.util.Map;

import dev.spocht.spocht.location.LocationCallback;
import dev.spocht.spocht.location.MyLocationListener;

/**
 * Created by mueller8 on 05.09.2015.
 */
public class GeoFence {
    private GeoFenceState         mState =null;
    private Location        mLocation=new Location("");
    private double          mDistance=0;
    private Map<String,GeoFenceCallback> mpCb=new HashMap<>(10);

    public GeoFence(final Location location, final double distance, final GeoFenceCallback actionIn, final GeoFenceCallback actionOut, final Context ctx)
    {
        mLocation=location;
        mDistance=distance;

        mpCb.put(GeoFenceStateIn.class.getName(), actionIn);
        mpCb.put(GeoFenceStateOut.class.getName(), actionOut);
        mState = new GeoFenceStateOut(this);

        MyLocationListener.create(ctx);
        MyLocationListener.getInstance().register(new LocationCallback<Void, Location>() {
            @Override
            public Void operate(Location location) {
                execute(location);
                return null;
            }
        },false,this);
    }

    @Override
    protected void finalize() throws Throwable {
        MyLocationListener.getInstance().unregister(this);
        super.finalize();
    }

    GeoFenceCallback cb(String key)
    {
        return mpCb.get(key);
    }
    void setState(final GeoFenceState next)
    {
        mState.exit();
        mState=next;
        mState.entry();
    }
    public Location location()
    {
        return mLocation;
    }
    public double distance()
    {
        if(mDistance < 0)
        {
            mDistance=0;
        }
        return mDistance;
    }
    public void execute(final Location location)
    {
        if(null != mState)
        {
            mState.execute(location);
        }
    }

}
