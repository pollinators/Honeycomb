package io.github.pollinators.honeycomb.module;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationClient;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.pollinators.honeycomb.MapsActivity;
import io.github.pollinators.honeycomb.data.SurveySQLiteHelper;
import io.github.pollinators.honeycomb.fragment.NavigationDrawerFragment;
import io.github.pollinators.honeycomb.MainActivity;
import io.github.pollinators.honeycomb.PollinatorsBaseActivity;
import io.github.pollinators.honeycomb.ui.ActionBarController;
import io.github.pollinators.honeycomb.util.Utils;

/**
 * Created by ted on 11/1/14.
 */
@Module(
        injects = {
                MainActivity.class,
                MapsActivity.class,
                NavigationDrawerFragment.class
        },
        addsTo = AndroidModule.class,
        complete = false,
        library = true
)
public class ActivityModule {

    PollinatorsBaseActivity activity;

    public ActivityModule(PollinatorsBaseActivity activity) {
        this.activity = activity;
    }

    @Provides @Singleton @ForActivity
    Context provideActivityContext() {
        return activity;
    }

    @Provides @Singleton ActionBarController provideActionBarContoller() {
        return new ActionBarController(activity);
    }

    @Provides Resources provideResources() {
        return activity.getResources();
    }

    @Provides @Singleton Bus provideBus() {
        return new Bus();
    }

    @Provides Utils.Toaster provideToaster() {
        return new Utils.Toaster(activity);
    }

    @Provides LocationClient provideLocationClient(
            GooglePlayServicesClient.ConnectionCallbacks connCBs,
            GooglePlayServicesClient.OnConnectionFailedListener cfl)
    {
        return new LocationClient(activity, connCBs, cfl);
    }

    @Provides
    Picasso providePicasso(@Named("Local") RequestHandler localHandler,
                           @Named("Network") RequestHandler networkHandler) {
        return new Picasso.Builder(activity)
//                .addRequestHandler(networkHandler)
//                .addRequestHandler(localHandler)
                .indicatorsEnabled(true)
                .build();
    }

    @Provides @Named("Local") RequestHandler provideLocalRequestHandler() {
        return new RequestHandler() {
            @Override
            public boolean canHandleRequest(Request data) {
                if (data.resourceId > 1) {
                    return true;
                }
                return false;
            }

            @Override
            public Result load(Request data) throws IOException {
                return null;
            }
        };
    }

    @Provides @Named("Network") RequestHandler provideNetworkRequestHandler() {
        return new RequestHandler() {
            @Override
            public boolean canHandleRequest(Request data) {
                if (data.resourceId > 1) {
                    return true;
                }
                return false;
            }

            @Override
            public Result load(Request data) throws IOException {
                return null;
            }
        };
    }
}
