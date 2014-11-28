package io.github.pollinators.honeycomb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.squareup.otto.Subscribe;

import java.sql.SQLException;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import io.github.pollinators.honeycomb.data.SurveyDataSource;
import io.github.pollinators.honeycomb.data.models.SurveyAnswer;
import io.github.pollinators.honeycomb.data.models.SurveyResponseModel;
import io.github.pollinators.honeycomb.fragment.NavigationDrawerFragment;
import io.github.pollinators.honeycomb.fragment.QuestionFragment;
import io.github.pollinators.honeycomb.fragment.QuestionFragmentBuilder;
import io.github.pollinators.honeycomb.module.QuestionModule;
import io.github.pollinators.honeycomb.survey.MedoraSurvey;
import io.github.pollinators.honeycomb.survey.Survey;
import io.github.pollinators.honeycomb.util.Events;
import timber.log.Timber;


public class MainActivity extends PollinatorsBaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
    @Inject Survey survey;
    @Inject SharedPreferences sharedPrefs;
    @Inject LocationClient mLocationClient;
    @Inject SQLiteOpenHelper dbHelper;

    QuestionFragment questionFragment;

    int currentPosition;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private SurveyResponseModel surveyResponse;

    SurveyDataSource surveyDataSource;

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            toaster.toast("Location has changed");
            if (surveyResponse != null) {
                //TODO: Change casting
                surveyResponse.setLatitude((float) location.getLatitude());
                surveyResponse.setLongitude((float) location.getLongitude());
            }
        }
    };

    public MainActivity() {
        super();
        getModules().add(new QuestionModule());
        getModules().add(new GooglePlayServicesClientModule());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Open the database
        surveyDataSource = new SurveyDataSource(dbHelper, survey.getQuestionCount());
        try {
            surveyDataSource.open();
        } catch (SQLException e) {
            Timber.e(e, "Unexpected error when opening database");
            surveyDataSource = null;
        }

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        FragmentManager fragmentManager = getSupportFragmentManager();
        questionFragment = new QuestionFragmentBuilder(0).build();
        fragmentManager.beginTransaction()
                .replace(R.id.container, questionFragment)
                .commit();
    }

    @Override
    protected void onStop() {

        sharedPrefs.edit().putLong(KEY_SURVEY_RESPONSE_ID, surveyResponse.getId()).commit();

        try {
            surveyDataSource.close();
        } catch (NullPointerException e) {
            Timber.e(e, "The data source was null for some reason. It was probably not opened correctly");
        }

        super.onStop();
    }

    public static final String KEY_SURVEY_RESPONSE_ID = "CURRENT_SURVEY_RESPONSE_ID";

    @Override
    protected void onResume() {
        super.onResume();

        // Get the current survey response from the database if it exists
        long currentSurveyResponseId = sharedPrefs.getLong(KEY_SURVEY_RESPONSE_ID, -1);

        if (currentSurveyResponseId < 1) {
            // If surveyId is invalid, create a new surveyResponse
            surveyResponse = surveyDataSource.create();
            sharedPrefs.edit().putLong(KEY_SURVEY_RESPONSE_ID, surveyResponse.getId()).commit();
        } else {
            surveyResponse = surveyDataSource.get(currentSurveyResponseId);
        }

        // TODO: Take this out of production code
        if (surveyResponse == null) {
            Timber.w("The survey response was null. Not good");
            throw new NullPointerException("The survey response was null. Not good");
        }

        mLocationClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments

        if (questionFragment != null) {
            saveQuestionData(null);

            if (position < 0) {
                currentPosition = 0;
            } else if (position >= survey.getQuestionCount()) {
                currentPosition = survey.getQuestionCount() - 1;
            } else {
                currentPosition = position;
            }
            questionFragment.setQuestion(currentPosition);
            retreiveQuestionData(null);
        }
    }

    @Subscribe
    public void saveQuestionData(Events.SaveQuestionDataEvent event) {
        Object data = questionFragment.getCurrentData();
        if (data == null) {
            return;
        }

        Survey.SurveyQuestion<String> s = survey.getQuestion(currentPosition);

        SurveyAnswer answer = surveyDataSource.getAnswer(surveyResponse, currentPosition);

        try {
            switch (s.type) {
                case MedoraSurvey.TYPE_YN:
                    answer.setBoolAnswer((Boolean) data);
                    break;
                case MedoraSurvey.TYPE_NUMERIC:
                    answer.setRealAnswer((Double) data);
                    break;
                case MedoraSurvey.TYPE_RADIO_MULTI_CHOICE:
                    answer.setTextAnswer(String.valueOf(data));
                    break;
                case MedoraSurvey.TYPE_NUMBER_PICKER:
                    answer.setIntAnswer((Integer) data);
                    break;
            }

            surveyDataSource.saveAnswer(answer);

        } catch(ClassCastException e) {
            Timber.e(e, "Something happened");
        }

    }

    @Subscribe
    public void retreiveQuestionData(Events.RetreiveQuestionDataEvent event) {
        Survey.SurveyQuestion<String> s = survey.getQuestion(currentPosition);
        SurveyAnswer answer = surveyDataSource.getAnswer(surveyResponse, currentPosition);

        try {
            Object data;

            switch (s.type) {
                case MedoraSurvey.TYPE_YN:
                    data = answer.getBoolAnswer();
                    break;
                case MedoraSurvey.TYPE_NUMERIC:
                    data = answer.getRealAnswer();
                    break;
                case MedoraSurvey.TYPE_RADIO_MULTI_CHOICE:
                    data = answer.getTextAnswer();
                    break;
                case MedoraSurvey.TYPE_NUMBER_PICKER:
                    data = answer.getIntAnswer();
                    break;
                default:
                    data = null;
            }

            questionFragment.setCurrentData(data);

        } catch (ClassCastException e) {
            Timber.e(e, "Something happened");
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(false);
//        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_map) {
            Intent mapsActivity = new Intent(this, MapsActivity.class);
            startActivity(mapsActivity);
        } else if (id == R.id.action_next) {
            onNavigationDrawerItemSelected(currentPosition + 1);
        } else if (id == R.id.action_previous) {
           onNavigationDrawerItemSelected(currentPosition - 1);
        } else if (id == R.id.action_submit) {
            submitSurvey();
        }

        return super.onOptionsItemSelected(item);
    }

    private void submitSurvey() {
        surveyResponse.setSurveyEnded(true);
        surveyDataSource.save(surveyResponse);
        toaster.toast("Submission successful");

        // Create a new survey to take it's place
        surveyResponse = surveyDataSource.create();
        questionFragment.setCurrentData(null);
    }

    //**********************************************************************************************
    // Modules
    //**********************************************************************************************

    @Module(library = true)
    public class GooglePlayServicesClientModule {

        @Provides
        GooglePlayServicesClient.ConnectionCallbacks provideConnectionCallbacks() {
            return new GooglePlayServicesClient.ConnectionCallbacks() {
                @Override
                public void onConnected(Bundle bundle) {
                    LocationRequest lr = LocationRequest.create();
                    lr.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    lr.setInterval(5000);
                    lr.setFastestInterval(1000);

                    mLocationClient.requestLocationUpdates(lr, locationListener);
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
