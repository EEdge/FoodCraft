package sfsu.csc413.foodcraft;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationCallback;
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

/**
 * The MapsActivity is where FoodCraft displays nearby grocery stores. It makes an API call to
 * Google Maps, creates a map in the view, then makes an API call to Yelp to find grocery stores
 * in the area and draws those on the map.
 *
 * @author: Evan Edge
 * @version: 1.0
 */

public class MapsActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String TAG = MapsActivity.class.getSimpleName();

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationCallback locationCallback;
    SharedPreferences sharedPreferences;
    int locationCount = 0;
    double mLatitude;
    double mLongitude;
    String mLatitudeText = "";
    String mLongitudeText = "";

    YelpAPIRequest yelpRequest;
    private ArrayList<Place> yelpPlacesArray = new ArrayList<>();

    @TargetApi(Build.VERSION_CODES.M)
    @Override

    /**
     * The onCreate method establishes a connection with Google Maps and sets up on connection
     * listeners to make other API calls.
     *
     * @param savedInstanceState the bundle of saved information from last time the activity was used
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
        Location mLastLocation;

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)        // in milliseconds
                .setFastestInterval(1000);

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }

    }

    /**
     * overridden TaskCallback method to draw Yelp Places on the map when app gets API response
     */

    TaskCallback taskCallback = new TaskCallback() {
        public void onTaskCompleted(ArrayList<Place> result) {
            if (result.size() == 0) {

            } else {
                yelpPlacesArray.addAll(result);
                drawYelp(yelpPlacesArray, mMap);
            }

        }
        public void onTaskCompleted(String holder){

        }
    };

    /**
     * This method takes all the Place objects parsed from the Yelp response and draws them on the Google Map
     *
     * @param yelpPlacesArray the array of Place objects
     * @param mMap The GoogleMap
     */

    private static void drawYelp(ArrayList<Place> yelpPlacesArray, GoogleMap mMap) {

        if (yelpPlacesArray.size() == 0) {
            Place tj = new Place();
            tj.lng = -122.476590;
            tj.lat = 37.726611;
            tj.name = "Trader Joe's";
            tj.address = "3251 20th Ave";
            yelpPlacesArray.add(tj);
            Place safeWay = new Place();
            safeWay.lng = -122.474153;
            safeWay.lat = 37.743416;
            safeWay.name = "Safeway";
            safeWay.address = "730 Taraval St";
            yelpPlacesArray.add(safeWay);

        }
        for (int i = 0; i < yelpPlacesArray.size(); i++) {
            Place place = yelpPlacesArray.get(i);
            LatLng latLng = new LatLng(place.lat, place.lng);
            Marker placeMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(place.name)
                    .snippet(place.address.substring(2, place.address.length() - 2))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            placeMarker.showInfoWindow();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * This method makes sure the GoogleMap is instantiated
     */

    private void setUpMapIfNeeded() {
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

    private void setUpMap() {
        //my initial test for adding a marker.
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Test Marker"));
    }

    /**
     * This method is called if the original location of the user changes
     *
     * @param location Location object with latitude and longitude coordinates
     */

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        //mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here @ " + currentLatitude + " " + currentLongitude);
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }

    /**
     * This method is called once the app has a response from Google Maps.
     * With that response comes Latitude and Longitute coordinates and the
     * ability to call the Yelp API using those coordinates.
     *
     * @param bundle This is the bundle containing the information returned from Google Maps API
     */

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        mLatitudeText = String.valueOf(mLatitude);
        mLongitudeText = String.valueOf(mLongitude);
        String latLngString = mLatitudeText + "," + mLongitudeText;
        yelpRequest = new YelpAPIRequest("groceries", latLngString, getApplicationContext(), taskCallback);

        try {
            yelpRequest.makeRequest();
            Log.i("yelp response.:", "madeRequest in maps");
        } catch (Exception e) {
            Log.i("MapsActivity Yelp call", "Error");
            Toast toast = Toast.makeText(this.getApplicationContext(), "Grocery stores not found- please try again", Toast.LENGTH_SHORT);
            toast.show();
        }
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /**
     * This method is called if the Google Maps connections fails.
     *
     * @param connectionResult ConnectionResult object containing details of connection failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }
}