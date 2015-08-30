package dev.spocht.spocht.data;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;

/**
 * Created by mueller8 on 29.08.2015.
 */
public class GeoPoint extends ParseGeoPoint {
    public GeoPoint()
    {
        ;
    }
    public GeoPoint(double latitude, double longitude)
    {
        super(latitude,longitude);
    }
}
