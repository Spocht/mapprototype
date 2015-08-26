package dev.spocht.spocht.mock.location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by highway on 16.08.2015.
 */
public final class Spitalacker  implements Stub {
    public final static double LATITUDE = 46.955293;
    public final static double LONGITUDE = 7.454206;
    public final static String DESCRIPTION = "Hartplatz, Zugang nicht ganz einfach zu finden.";
    public final static String NAME = "Spitalacker";

    LatLng latLng;

    public void Steckweg() {
        latLng = new LatLng(LATITUDE, LONGITUDE);
    }

    @Override
    public LatLng getLatLng() {
        return new LatLng(LATITUDE, LONGITUDE);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
