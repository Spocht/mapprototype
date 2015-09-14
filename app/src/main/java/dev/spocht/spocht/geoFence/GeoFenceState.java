package dev.spocht.spocht.geoFence;

import android.location.Location;
import android.util.Log;

/**
 * Created by mueller8 on 05.09.2015.
 */
public class GeoFenceState {
    final protected GeoFence mCtx;
    public GeoFenceState(final GeoFence ctx)
    {
        mCtx=ctx;
    }
    public void entry()
    {
        //NPE
        //java.lang.NullPointerException: Attempt to invoke interface method 'void dev.spocht.spocht.geoFence.GeoFenceCallback.action()' on a null object reference
        //mCtx.cb(this.toString()).action();
    }
    public void exit()
    {
        ;
    }
    public void execute(final Location location)
    {
        Log.d(this.getClass().getCanonicalName(),"Distance to center: "+ mCtx.location().distanceTo(location)+"m");
    }
}
