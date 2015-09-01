package dev.spocht.spocht.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import dev.spocht.spocht.R;
import dev.spocht.spocht.fragment.DetailFragment;
import dev.spocht.spocht.listener.OnDetailsFragmentListener;
import dev.spocht.spocht.callbacks.LocationCallback;
import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.DatenSchleuder;
import dev.spocht.spocht.data.Facility;
import dev.spocht.spocht.data.GeoPoint;
import dev.spocht.spocht.data.InfoRetriever;
import dev.spocht.spocht.data.Sport;
import dev.spocht.spocht.listener.MyLocationListener;
import dev.spocht.spocht.mock.location.Lorrainepark;
import dev.spocht.spocht.mock.location.Lorrainestrasse;
import dev.spocht.spocht.mock.location.Spitalacker;
import dev.spocht.spocht.mock.location.Steckweg;
import dev.spocht.spocht.mock.location.Stub;

public class MapsActivity extends AppCompatActivity
        implements
        GoogleMap.OnMarkerClickListener,
        FragmentManager.OnBackStackChangedListener,
        OnDetailsFragmentListener {


    MyLocationListener myLocationListener;
    LocationCallback<Void, Location> locationCallback = new LocationCallback<Void, Location>() {
        @Override
        public Void operate(Location location) {

            LatLng latLng = new LatLng(
                    location.getLatitude(),
                    location.getLongitude());
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

            updateMarkers(new GeoPoint(location));

            return null;
        }

    };

    DetailFragment detailFragment;
    boolean isDetailFragmentVisible = false;
    boolean isAnimating = false;

    private HashMap<Marker,Facility> mapFacility=new HashMap<>(20);
    private HashSet<String>          setFacilities=new HashSet<>(20);
    private static android.content.Context context;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private ArrayList<Stub> locationList;


    public static android.content.Context getAppContext() {
        return MapsActivity.context;
    }


    //this one is needed... unfortunately it is not mentioned in the tutorial
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("activity/MapsActivityOnCreate");

        detailFragment = new DetailFragment();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        MapsActivity.context = getApplicationContext();
        myLocationListener = new MyLocationListener(context, locationCallback, true);

        //SPOCHT-13:
        //setup() had a method to DataManager.getInstance that
        //was called there. that leaded to unfortunate
        //Parse.enableLocalDatastore-called-twice-Exceptions.
        //now the context is given as a param.
        DatenSchleuder.getInstance().setup(DataManager.getInstance().getContext());

        getFragmentManager().addOnBackStackChangedListener(this);

        setUpMapIfNeeded();
        setUpActionBar();
        loadLocations();
    }

    View.OnClickListener mapClickListener = new View.OnClickListener () {
        @Override
        public void onClick(View view) {
            animateFragment(false);
        }
    };

    private void setUpActionBar() {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            // ActionBar was first introduced in HoneyComb
//            ActionBar a = getActionBar();
//            if (a != null) {
//                a.setTitle(R.string.app_name);
//            }
//        }
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
                DataManager.getInstance().logout();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
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
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
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

    public void loadMarkers(View view) {
        updateMarkers(myLocationListener.getLastLocationGP());
    }

    private void updateMarkers(final GeoPoint location)
    {
        DataManager.getInstance().findFacilities(location, 1.5, new InfoRetriever<List<Facility>>() {
            @Override
            public void operate(List<Facility> facilities) {
                for(Facility f:facilities)
                {
                    Log.d("spocht.maps","Got facility: "+f.name());
                    if(!setFacilities.contains(f.getObjectId())) {
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(f.location().toLatLng())
                                        .title(f.name())
//                                        .icon(BitmapDescriptorFactory.fromResource(Resources.getSystem().getIdentifier("spocht_" + f.sport().name() + "_" + "grey", "drawable", "android")))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.spocht_tabletennis_grey))
                                        .anchor(0, 1)
                        );
                        mapFacility.put(marker, f);
                        setFacilities.add(f.getObjectId());
                        Log.d("spocht.maps","stored facility: "+f.name());
                    }
                }
            }
        });
    }

    //todo: remove
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

        animateFragment(true);


        return true;
    }

    private void animateFragment(boolean visible) {
        if (isAnimating) {
            return;
        }
        isAnimating = true;

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if (visible) {
            isDetailFragmentVisible = true;
            ft.setCustomAnimations(R.animator.slide_fragment_in, 0, 0, R.animator.slide_fragment_out);

            Log.d("animateFragment", "slide up");
            ft.add(R.id.main_content, detailFragment);
            ft.addToBackStack(null);
            ft.commit();

        } else {
            Log.d("animanteFragment", "slide down");
            getFragmentManager().popBackStack();
        }

    }

    @Override
    public void onBackStackChanged() {
        if (isDetailFragmentVisible) {
            isDetailFragmentVisible = false;
            animateFragment(false);
        }
    }

    @Override
    public void onAnimationEnd() {
        isAnimating = false;
    }
}
