package dev.spocht.spocht.mock.location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by highway on 16.08.2015.
 */
public final class Lorrainestrasse implements Stub {
    public final static double LATITUDE = 46.959411;
    public final static double LONGITUDE = 7.444365;
    public final static String DESCRIPTION = "Rasen und Erde als Untergrund, etwas unbeben. Gemütlich. Tisch hat eine Ecke ab";
    public final static String NAME = "Lorrainestrasse";

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