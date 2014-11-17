package io.github.pollinators.honeycomb;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.ObjectGraph;
import io.github.pollinators.honeycomb.module.ActivityModule;
import io.github.pollinators.honeycomb.util.Utils;

/**
 * Created by ted on 11/1/14.
 */
public abstract class PollinatorsBaseActivity extends ActionBarActivity {

    private ObjectGraph activityGraph;
    private List<Object> modules = new ArrayList<Object>();

    @Inject Bus bus;
    @Inject Utils.Toaster toaster;

    public PollinatorsBaseActivity() {
        super();
        modules.addAll(getBaseModules());
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the activity graph by .plus-ing modules onto the application graph
        PollinatorsApplication application = (PollinatorsApplication) getApplication();
        activityGraph = application.getApplicationGraph().plus(getModules().toArray());

        activityGraph.inject(this);
    }

    @Override
    protected void onDestroy() {
        activityGraph = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bus.register(this);
    }

    /**
     * A list of modules to use for the individual activity graph. Subclasses can override this
     * method to provide additional modules provided they call and include the modules returned by
     * calling {@code super.getModules()}.
     */
    protected List<Object> getBaseModules() {
        return Arrays.<Object>asList(new ActivityModule(this));
    }

    protected List<Object> getModules() {
        return modules;
    }

    /** Inject the supplied {@code object} using the activity-specific graph. */
    public void inject(Object object) {
        activityGraph.inject(object);
    }
}
