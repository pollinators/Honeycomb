package io.github.pollinators.honeycomb;

import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.SQLException;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import io.github.pollinators.honeycomb.data.SurveyDataSource;
import io.github.pollinators.honeycomb.data.models.SurveyResponseModel;
import io.github.pollinators.honeycomb.module.QuestionModule;
import io.github.pollinators.honeycomb.survey.Survey;

public class MapsActivity extends PollinatorsBaseActivity {

    //**********************************************************************************************
    // NON-STATIC DATA MEMBERS
    //**********************************************************************************************

    @Inject LocationClient mLocationClient;
    @Inject Survey survey;
    @Inject SQLiteOpenHelper dbHelper;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    LocationListener listener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            addMarkersInLine(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(
                    new LatLng(location.getLatitude(), location.getLongitude())));
        }
    };

    //**********************************************************************************************
    // CONSTRUCTOR
    //**********************************************************************************************

    public MapsActivity() {
        super();
        getModules().add(new QuestionModule());
        getModules().add(new GooglePlayServicesClientModule());
    }

    //**********************************************************************************************
    // OVERRIDDEN METHODS
    //**********************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mLocationClient.connect();

        SurveyDataSource data = new SurveyDataSource(dbHelper, survey.getQuestionCount());
        try {
            data.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        SurveyResponseModel model = data.get(31);
//        LatLng loc = new LatLng(model.getLatitude(), model.getLongitude());
//        mMap.addMarker(new MarkerOptions()
//                .position(loc)
//                .draggable(true)
//                .title("Me :)"));
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }

    /*
     * Called when the Activity is no longer visible at all.
     * Stop updates and disconnect.
     */
    @Override
    protected void onStop() {
        // If the client is connected
        if (mLocationClient.isConnected()) {
            /*
             * Remove location updates for a listener.
             * The current Activity is the listener, so
             * the argument is "this".
             */
//            removeLocationUpdates(this);
        }
        /*
         * After disconnect() is called, the client is
         * considered "dead".
         */
        mLocationClient.disconnect();
        super.onStop();
    }

    //**********************************************************************************************
    // NON-STATIC METHODS
    //**********************************************************************************************

    private void addMarkersInLine(double lat, double lng) {

        for (int i = 0; i < 10; i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat + i * 4, lng + i * 4))
                    .draggable(true)
                    .rotation(i * 10)
                    .title("Me :)"));
        }
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
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-37.813, 144.962))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }

    //**********************************************************************************************
    // Modules
    //**********************************************************************************************

    @Module(library = true)
    public class GooglePlayServicesClientModule {

        @Provides GooglePlayServicesClient.ConnectionCallbacks provideConnectionCallbacks() {
            return new GooglePlayServicesClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    LocationRequest lr = LocationRequest.create();
                    lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    lr.setInterval(5000);
                    lr.setFastestInterval(1000);

                    mLocationClient.requestLocationUpdates(lr, listener);
                }

                @Override
                public void onDisconnected() {

                }
            };
        }

        @Provides GooglePlayServicesClient.OnConnectionFailedListener providedConnectionFailedListener() {
            return new GooglePlayServicesClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {

                }
            };
        }
    }
}
