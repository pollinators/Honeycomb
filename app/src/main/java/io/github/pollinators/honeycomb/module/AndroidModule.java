package io.github.pollinators.honeycomb.module;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.pollinators.honeycomb.PollinatorsApplication;

/**
 * Created by ted on 11/1/14.
 */
@Module(library = true)
public class AndroidModule {

    private final PollinatorsApplication application;

    public AndroidModule(PollinatorsApplication application) {
        this.application = application;
    }

    @Provides @Singleton @ForApplication Context provideApplicationContext() {
        return application;
    }

    @Provides @Singleton
    SharedPreferences provideSharedPreferences() {
        return application.getSharedPreferences("Pollinator Research Collector", Context.MODE_PRIVATE);
    }

    @Provides @Singleton LocationManager provideLocationManager() {
        return (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
    }
}
