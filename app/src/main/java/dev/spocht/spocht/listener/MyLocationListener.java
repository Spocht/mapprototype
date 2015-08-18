package dev.spocht.spocht.listener;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import dev.spocht.spocht.callbacks.LocationCallback;

/**
 * Created by edm on 17.08.15.
 */
public class MyLocationListener implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    LocationCallback cb;

    private Location lastLocation;

    private Context ctx;
    public MyLocationListener(Context ctx, LocationCallback<Void, Location> cb) {
        this.ctx = ctx;
        this.cb = cb;
        buildGoogleApiClient();
    }




    private GoogleApiClient googleApiClient;


    protected synchronized GoogleApiClient buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
        return googleApiClient;

    }

    private LocationRequest locationRequest;

    protected void createLocationRequest(){
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }


    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        createLocationRequest();

        startLocationUpdates();

    }

    public Location getLastLocation(){
        return lastLocation;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
            lastLocation = location;
            System.out.println("Loca changed");
            cb.operate(location);

            //LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            //mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            //tv.setText(String.valueOf(lastLocation.getLatitude()));

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


}
