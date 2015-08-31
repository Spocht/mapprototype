package dev.spocht.spocht.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import dev.spocht.spocht.R;
import dev.spocht.spocht.fragment.DetailFragment;
import dev.spocht.spocht.mock.location.Lorrainepark;
import dev.spocht.spocht.mock.location.Lorrainestrasse;
import dev.spocht.spocht.mock.location.Spitalacker;
import dev.spocht.spocht.mock.location.Steckweg;
import dev.spocht.spocht.mock.location.Stub;

public class MapsActivity extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        GoogleMap.OnMarkerClickListener {

    private static android.content.Context context;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private LocationRequest locationRequest;
    private ArrayList<Stub> locationList;

    DetailFragment detailFragment;

    public static android.content.Context getAppContext() {
        return MapsActivity.context;
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    //this one is needed... unfortunately it is not mentioned in the tutorial
    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("activity/MapsActivityOnCreate");

        detailFragment = new DetailFragment();

        super.onCreate(savedInstanceState);

        buildGoogleApiClient();
        setContentView(R.layout.activity_maps);
        MapsActivity.context = getApplicationContext();

        setUpMapIfNeeded();
        setUpActionBar();
        loadLocations();
    }

    private void setUpActionBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // ActionBar was first introduced in HoneyComb
            ActionBar a = getSupportActionBar();
            if (a != null) {
                a.setTitle(R.string.app_name);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_stats:
                return false;
            case R.id.menu_settings:
                return false;
            case R.id.menu_logout:
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the foo@Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        createLocationRequest();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        System.out.println("NotConnected");
        System.out.println(connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println(location.getLatitude() + " " + location.getLongitude());

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
    }

    public void loadMarkers(View view) {
        for (Stub loc: locationList) {
            mMap.addMarker(new MarkerOptions()
                            .position(loc.getLatLng())
                            .title(loc.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.spocht_tabletennis_grey))
                            .anchor(0, 1)
            );
            System.out.println(loc.getClass());
        }
    }

    private void loadLocations() {
        locationList = new ArrayList<Stub>();
        locationList.add(new Lorrainepark());
        locationList.add(new Steckweg());
        locationList.add(new Spitalacker());
        locationList.add(new Lorrainestrasse());
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        System.out.println(marker.getTitle());

        animateFragment();

        return true;
    }

    private void animateFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_fragment_in, R.animator.slide_fragment_out);
        ft.add(R.id.move_to_back_container, detailFragment);
        ft.addToBackStack(null);
        ft.commit();
    }
}
