package io.github.pollinators.honeycomb.module;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.model.LatLng;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.pollinators.honeycomb.BuildConfig;
import io.github.pollinators.honeycomb.MainActivity;
import io.github.pollinators.honeycomb.MapsActivity;
import io.github.pollinators.honeycomb.module.qualifier.ForActivity;
import io.github.pollinators.honeycomb.module.qualifier.Mock;

/**
 * Created by ted on 12/10/14.
 */
@Module(
        complete = false,
        library = true,
        injects = {
                MainActivity.class,
                MapsActivity.class
        }
)
public class LocationModule {

    public static class MockLocation {

        static final String PROVIDER = "flp";
        static final String LAT = "37.377166";
        static final String LNG = "-122.086966";
        static final String ACCURACY = "3.0";

        /*
         * From input arguments, create a single Location with provider set to
         * "flp"
         */
        public Location createLocation(double lat, double lng, float accuracy) {
            // Create a new Location
            Location newLocation = new Location(PROVIDER);
            newLocation.setLatitude(lat);
            newLocation.setLongitude(lng);
            newLocation.setAccuracy(accuracy);
            return newLocation;
        }
    }

    @Provides @Mock
    Location providesMockLocation() {
        return new MockLocation().createLocation(1.2d,40.3d,3.0f);
    }

    @Provides
    @Singleton
    LocationClient provideLocationClient(@Mock Location mockLocation,
                                         @ForActivity Context context,
                                         GooglePlayServicesClient.ConnectionCallbacks connCBs,
                                         GooglePlayServicesClient.OnConnectionFailedListener cfl) {
        LocationClient locationClient;

        if (BuildConfig.DEBUG) {
        }
        locationClient = new LocationClient(context, connCBs, cfl);

        return locationClient;
    }
}
