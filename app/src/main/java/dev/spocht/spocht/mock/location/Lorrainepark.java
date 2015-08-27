package dev.spocht.spocht.mock.location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by highway on 16.08.2015.
 */
public class Lorrainepark implements Stub {
    public final static double LATITUDE = 46.955664;
    public final static double LONGITUDE = 7.444901;
    public final static String DESCRIPTION = "Einzelner Tisch im belebten Lorrainepärkli";
    public final static String NAME = "Lorrainepark";

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
