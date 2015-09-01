package dev.spocht.spocht.data;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
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
    public GeoPoint(final ParseGeoPoint other)
    {
        super(other.getLatitude(),other.getLongitude());
    }
    public GeoPoint(double latitude, double longitude)
    {
        super(latitude,longitude);
    }
    public GeoPoint(Location location)
    {
        super(location.getLatitude(),location.getLongitude());
    }
    public GeoPoint(LatLng location)
    {
        super(location.latitude,location.longitude);
    }
    public Location toLocation(){
        Location l= new Location("");
        l.setLatitude(getLatitude());
        l.setLongitude(getLongitude());
        return l;
    }
    public LatLng toLatLng()
    {
        return new LatLng(getLatitude(),getLongitude());

    }
}
