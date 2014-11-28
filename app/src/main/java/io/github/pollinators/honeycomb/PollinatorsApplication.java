package io.github.pollinators.honeycomb;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import dagger.ObjectGraph;
import io.github.pollinators.honeycomb.module.AndroidModule;
import timber.log.Timber;

/**
 * Created by ted on 11/1/14.
 */
public class PollinatorsApplication extends Application {

    private ObjectGraph applicationGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationGraph = ObjectGraph.create(getModules().toArray());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            ButterKnife.setDebug(BuildConfig.DEBUG);
        } else {
//            Timber.plant(new CrashReportingTree());
//            Crashlytics.start(this);
        }

    }

    private List<Object> getModules() {
        return Arrays.<Object>asList(new AndroidModule(this));
    }

    public ObjectGraph getApplicationGraph() {
        return applicationGraph;
    }
}
