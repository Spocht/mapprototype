package dev.spocht.spocht.geoFence;

import android.location.Location;
import android.util.Log;

/**
 * Created by mueller8 on 05.09.2015.
 */
public class GeoFenceStateIn extends GeoFenceState {
    public GeoFenceStateIn(GeoFence ctx) {
        super(ctx);
    }

    @Override
    public void execute(Location location) {
        super.execute(location);
        if((mCtx.location().distanceTo(location)/1000) >= (mCtx.distance()+0.01))
        {
           mCtx.setState(new GeoFenceStateOut(mCtx));
        }
    }
}
