package dev.spocht.spocht.geoFence;

import android.location.Location;

/**
 * Created by mueller8 on 05.09.2015.
 */
public class GeoFenceStateOut extends GeoFenceState {
    public GeoFenceStateOut(GeoFence ctx) {
        super(ctx);
    }
    @Override
    public void execute(Location location) {
        super.execute(location);
        if((mCtx.location().distanceTo(location)/1000) <= (mCtx.distance()-0.01))
        {
            mCtx.setState(new GeoFenceStateIn(mCtx));
        }
    }
}
