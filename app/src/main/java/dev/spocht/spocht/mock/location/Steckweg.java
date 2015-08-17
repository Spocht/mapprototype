package dev.spocht.spocht.mock.location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by highway on 16.08.2015.
 */
public final class Steckweg implements Stub {
    public final static double LATITUDE = 46.959055;
    public final static double LONGITUDE = 7.444084;

    public final static String DESCRIPTION = "Auf dem Schulhausplatz, leicht abschüssiges Gelände.";
    public final static String NAME = "Steckweg";

    LatLng latLng;

    public void Steckweg() {
        // hiermit wird eine Exception geworfen.... :-(
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
