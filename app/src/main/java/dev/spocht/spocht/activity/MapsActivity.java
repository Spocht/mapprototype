package dev.spocht.spocht.activity;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import dev.spocht.spocht.Application;
import dev.spocht.spocht.R;
import dev.spocht.spocht.data.Event;
import dev.spocht.spocht.location.LocationCallback;
import dev.spocht.spocht.data.DataManager;
import dev.spocht.spocht.data.DatenSchleuder;
import dev.spocht.spocht.data.Facility;
import dev.spocht.spocht.data.GeoPoint;
import dev.spocht.spocht.data.InfoRetriever;
import dev.spocht.spocht.location.MyLocationListener;

public class MapsActivity extends AppCompatActivity
        implements
        GoogleMap.OnMarkerClickListener {

    private LocationCallback<Void, Location> locationCallback = new LocationCallback<Void, Location>() {
        @Override
        public Void operate(Location location) {

            LatLng latLng = new LatLng(
                    location.getLatitude(),
                    location.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

            return null;
        }

    };
    private BroadcastReceiver recv = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            if(intent.hasExtra("event"))
            {
                //update the announced event
                DataManager.getInstance().update(intent.getStringExtra("event"), Event.class, new InfoRetriever<Event>() {
                    @Override
                    public void operate(Event event) {
                        Toast toastPush = Toast.makeText(
                                context,
                                "Event "+event.name()+" is updated!",
                                Toast.LENGTH_LONG
                        );
                        toastPush.show();
                        //only reload details if the event is selected
                        if(mapFacility.get(mSelectedMarker).getObjectId().equals(event.facility().getObjectId())) {
                            mDetailFragment.refreshContents();
                        }
                    }
                });
            }
            else if(intent.hasExtra("closeEvent"))
            {
                //todo: how to handle closing??
            }
        }
    };

    private DetailFragment mDetailFragment;
    private MapFragment mMapFragment;
    private boolean mIsDetailFragmentVisible = false;
    private boolean mIsAnimating = false;

    private int mScreenHeight;
    private float mNewHeight = -1;

    private Marker mSelectedMarker;

    private HashMap<Marker,Facility> mapFacility   = new HashMap<>(20);
    private HashSet<String>          setFacilities = new HashSet<>(20);
    private static android.content.Context  context;
    private GoogleMap                       mMap; // Might be null if Google Play services APK is not available.

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
        Log.d("spocht.mapsactivity", "activity/MapsActivityOnCreate");

        mDetailFragment = new DetailFragment();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        MapsActivity.context = getApplicationContext();

        context.registerReceiver(recv, new IntentFilter("ParsePusher"));

        MyLocationListener.create(context);
        MyLocationListener.getInstance().register(locationCallback, true,this);
        Toast toastWelcome = Toast.makeText(
                context,
                getString(R.string.welcome)+ " " + DataManager.getInstance().currentUser().getUsername(),
                Toast.LENGTH_LONG
        );
        toastWelcome.show();

        Log.d("spocht.mapsactivity","Loged in "+DataManager.getInstance().currentUser().getUsername());
        //SPOCHT-13:
        //setup() had a method to DataManager.getInstance that
        //was called there. that leaded to unfortunate
        //Parse.enableLocalDatastore-called-twice-Exceptions.
        //now the context is given as a param.
        DatenSchleuder.getInstance().setup(DataManager.getInstance().getContext());

        setUpMapIfNeeded();
        setUpActionBar();
    }

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
            case R.id.menu_clear_local:
                DataManager.getInstance().flushLocalStore();
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
        context.registerReceiver(recv,new IntentFilter("ParsePusher"));
        setUpMapIfNeeded();
    }

    @Override
    protected void onPause() {
        super.onPause();
        context.unregisterReceiver(recv);
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
            mMapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
            mMap = mMapFragment.getMap();
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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d("Map", "Fragment should now slide down");
                animateFragment(false);
            }
        });
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                Log.d("spocht.mapsactivity", "new Location: " + cameraPosition.toString());
                updateMarkers(new GeoPoint(cameraPosition.target));
            }
        });
    }

    /**
     * refreah button click handler
     *
     * @deprecated
     * @param view
     */
    public void loadMarkers(View view) {
        updateMarkers(MyLocationListener.getInstance().getLastLocationGP());
    }

    private void updateMarkers(final GeoPoint location)
    {

        double distance = new GeoPoint(mMap.getProjection().getVisibleRegion().nearLeft).distanceInKilometersTo(location);
        Log.d("spocht.mapsactivity", "Search distance: " + distance);
        if(distance > 5)
        {
            distance = 5;
        }
        DataManager.getInstance().findFacilities(location, distance, new InfoRetriever<List<Facility>>() {
            @Override
            public void operate(List<Facility> facilities) {
                for (Facility f : facilities) {
                    Log.d("spocht.maps", "Got facility: " + f.name() + " type: " + f.sport().name());
                    if (!setFacilities.contains(f.getObjectId())) {
                        //todo: set color according to facility's state
                        String iconDescriptor = "spocht_" + f.sport().name() + "_" + "grey";
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(f.location().toLatLng())
                                        .title(f.name())
                                        .icon(BitmapDescriptorFactory.fromResource(
                                                // this will throw a NotFoundException if the icon is not found
                                                getResources()
                                                        .getIdentifier(
                                                                iconDescriptor,
                                                                "drawable",
                                                                Application.PACKAGE_NAME
                                                        ))).anchor(0, 1)
                        );
                        mapFacility.put(marker, f);
                        setFacilities.add(f.getObjectId());
                        Log.d("spocht.maps", "stored facility: " + f.name());
                    }
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(getClass().getCanonicalName(), marker.getClass().getCanonicalName());
        Log.d(getClass().getCanonicalName(), marker.getTitle());
        System.out.println(mIsDetailFragmentVisible);

        mSelectedMarker = marker;

        // if the fragment is already visible, only refresh contents
        if ( ! mIsDetailFragmentVisible) {
            // otherwise display it. Contents will then be refreshed via onResume()
            animateFragment(true);
        } else {
            mDetailFragment.refreshContents();
        }

        return true;
    }

    public Facility getSelectedFacility() {
        return mapFacility.get(mSelectedMarker);
    }

    private void animateFragment(boolean visible) {
        if (visible) {
            mIsDetailFragmentVisible = true;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.slide_fragment_in, 0, 0, R.animator.slide_fragment_out);

            Log.d("animateFragment", "slide up");
            ft.add(R.id.main_content, mDetailFragment);
            ft.addToBackStack(null);
            ft.commit();

            // need this to resize map fragment back and forth
            // it seems odd to have this here instead within onCreate() and is also kind of inefficient.
            // but doing this in onCreate produces some weird shit. for some reason, doing it here is much more accurate
            // could also have fiddled around with some crazy wild animations, but this is more phun.
            if (-1 == mNewHeight) {
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                mScreenHeight = size.y - getSupportActionBar().getHeight() - getResources().getDimensionPixelSize(resourceId);

                // resize map
                TypedValue rValue = new TypedValue();
                getResources().getValue(R.dimen.slide_up_down_fraction, rValue, true);
                float factor = 1.0f - rValue.getFloat();
                mNewHeight = mScreenHeight * factor;
            }
            ViewGroup.LayoutParams lp = mMapFragment.getView().getLayoutParams();
            lp.height = (int) mNewHeight;
            mMapFragment.getView().setLayoutParams(lp);
        } else {
            Log.d("animanteFragment", "slide down");
            mIsDetailFragmentVisible = false;
            getFragmentManager().popBackStack();

            // resize map back
            ViewGroup.LayoutParams lp = mMapFragment.getView().getLayoutParams();
            lp.height = mScreenHeight;
            mMapFragment.getView().setLayoutParams(lp);
        }
    }

    public void createNew(View view) {
        Log.d("DetailFragment", "Create new event");
    }
}
