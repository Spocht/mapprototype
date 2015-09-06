package dev.spocht.spocht.location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import dev.spocht.spocht.data.GeoPoint;

/**
 * Created by edm on 17.08.15.
 */
public class MyLocationListener extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static MyLocationListener instance = new MyLocationListener();

    private Map<Object,CallbacksAndTheirUiBehaviour> locationCallbacks = new HashMap<>(10);
    private Location lastLocation;
    private Context ctx;
    private GoogleApiClient googleApiClient = null;
    private LocationRequest locationRequest;


    private MyLocationListener()
    {
        lastLocation = new Location("");

    }
    public void register(LocationCallback<Void, Location> cb, boolean um, final Object requester)
    {
        CallbacksAndTheirUiBehaviour callback = new CallbacksAndTheirUiBehaviour(cb, um);
        locationCallbacks.put(requester,callback);
    }
    public void unregister(final Object requester)
    {
        locationCallbacks.remove(requester);
    }

    public static MyLocationListener getInstance()
    {
        return instance;
    }

    public static synchronized void create(Context ctx)
    {
        if(null == getInstance().ctx) {
            getInstance().ctx = ctx;
        }
        getInstance().buildGoogleApiClient();
    }

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
    public GeoPoint getLastLocationGP(){
        Log.d("LocationListener","lastLocation: "+lastLocation);
        return new GeoPoint(lastLocation);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;

        final Location loc = location;

        for (final CallbacksAndTheirUiBehaviour cbb: locationCallbacks.values()) {
            if (!cbb.uiManipulating) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("spocht.locationListener", "Not ui manipulating");
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
                                Log.d("spocht.locationListener","Running manipulation");
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
