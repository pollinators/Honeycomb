package io.github.pollinators.honeycomb.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.pollinators.honeycomb.MainActivity;
import io.github.pollinators.honeycomb.util.PollinatorCoridorsAPI;
import retrofit.RestAdapter;

/**
 * Created by ted on 12/9/14.
 */
@Module(
        complete = false,
        library = true,
        injects = {
                MainActivity.class,
        }
)
public class APIsModule {

    //**********************************************************************************************
    // STATIC DATA MEMBERS
    //**********************************************************************************************

    private static final String POLLINATOR_API_ENDPOINT = "http://pollinatorcorridors.psu.edu";

    @Provides @Singleton
    RestAdapter provideRestAdapter() {
        return new RestAdapter.Builder()
                .setEndpoint(POLLINATOR_API_ENDPOINT)
                .build();

    }

    @Provides
    PollinatorCoridorsAPI provideAPI(RestAdapter restAdapter) {
        return restAdapter.create(PollinatorCoridorsAPI.class);
    }
}
