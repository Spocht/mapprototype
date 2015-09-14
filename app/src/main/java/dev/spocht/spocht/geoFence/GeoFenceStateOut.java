package dev.spocht.spocht.geoFence;

import android.location.Location;
import android.util.Log;

import dev.spocht.spocht.data.DataManager;

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

        if((mCtx.location().distanceTo(location)/1000) >= (mCtx.distance()-0.01)) {
            DataManager.getInstance().getEventMonitor().event().checkOut(DataManager.getInstance().currentUser());
            mCtx.setState(new GeoFenceStateIn(mCtx));
        }
    }
}
