package dev.spocht.spocht.listener;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dev.spocht.spocht.callbacks.LocationCallback;

/**
 * Created by edm on 17.08.15.
 */
public class MyLocationListener extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static List<CallbacksAndTheirUiBehaviour> locationCallbacks = new ArrayList<>();
    private Location lastLocation;
    private Context ctx;

    public MyLocationListener(Context ctx, LocationCallback<Void, Location> cb, boolean um) {
        this.ctx = ctx;
        CallbacksAndTheirUiBehaviour callback = new CallbacksAndTheirUiBehaviour(cb, um);
        locationCallbacks.add(callback);
        buildGoogleApiClient();
    }

    private static GoogleApiClient googleApiClient = null;


    private synchronized GoogleApiClient buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(ctx)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }
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
        createLocationRequest();
        startLocationUpdates();
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

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

        final Location loc = location;

        for (final CallbacksAndTheirUiBehaviour cbb: locationCallbacks) {
            if (!cbb.uiManipulating) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Not ui manipulating");
                        cbb.lc.operate(loc);
                    }
                }).start();
            } else {
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("Running manipulation");
                                cbb.lc.operate(loc);
                            }
                        });
                    }
                }, new Date());
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private static class CallbacksAndTheirUiBehaviour{
        LocationCallback lc;
        boolean uiManipulating;
        public CallbacksAndTheirUiBehaviour(LocationCallback lc, boolean um){
            this.lc = lc;
            this.uiManipulating = um;
        }
    }





}
