package dev.spocht.spocht;

import android.app.Application;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import bolts.Task;
import dev.spocht.spocht.data.DataManager;

public class MapsActivity extends FragmentActivity
    implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{

    private static android.content.Context context;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    String pos_actual = "bla";

    DataManager dataManager = DataManager.getInstance();

    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private LocationRequest locationRequest;

    double latitude = 46.954581;
    double longitude =  7.447266;


    Button button;


    private TextView tv;

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

    protected void createLocationRequest(){
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
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();
        setContentView(R.layout.activity_maps);

        tv = (TextView)findViewById(R.id.textView1);

        button = (Button)findViewById(R.id.button);


        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {


                MyUser user = new MyUser();
                user.store("Adolf", "Ceaucescu");
                System.out.println("Clicked");
                ParseQuery<ParseObject> pq = ParseQuery.getQuery("Sportsite");
                ParseQuery<ParseObject> blutt = ParseQuery.getQuery("Sportsite");


                blutt.findInBackground(new FindCallback<ParseObject>() {
                    @Override

                    public void done(List<ParseObject> list, ParseException e) {
                        for (int i = 0; i < list.size(); i++) {
                            System.out.println(list.get(i).get("name"));
                            System.out.println(list.get(i).get("location"));
                        }
                    }
                });

                pq.whereWithinKilometers("location", new ParseGeoPoint(lastLocation.getLatitude(), lastLocation.getLongitude()), 1.0);
                pq.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, ParseException e) {
                        Iterator<ParseObject> it =list.iterator();

                        while (it.hasNext()) {
                            System.out.println(it.next().get("name")+"name");
                        }
                    }
                });
            }
        });
        setUpMapIfNeeded();





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
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
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
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

    }


    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        createLocationRequest();

        startLocationUpdates();

        LatLng currentPosition = new LatLng(latitude, longitude);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(currentPosition));

        //tv = (TextView)findViewById(R.id.textView1);
        //tv.setText("bla");

        if (lastLocation != null) {
            tv.setText(String.valueOf(lastLocation.getLatitude()));
            //latitude.setText(String.valueOf(lastLocation.getLatitude()));
            //longitude.setText(String.valueOf(lastLocation.getLongitude()));

        }





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
        lastLocation = location;
        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        tv.setText(String.valueOf(lastLocation.getLatitude()));
    }
}
