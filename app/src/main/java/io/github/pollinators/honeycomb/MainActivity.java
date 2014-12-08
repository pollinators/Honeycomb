package io.github.pollinators.honeycomb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import io.github.pollinators.honeycomb.data.DataMappingSource;
import io.github.pollinators.honeycomb.data.ImageDataSource;
import io.github.pollinators.honeycomb.data.ResponseDataSource;
import io.github.pollinators.honeycomb.data.models.Answer;
import io.github.pollinators.honeycomb.data.models.Image;
import io.github.pollinators.honeycomb.data.models.MappingModel;
import io.github.pollinators.honeycomb.data.models.ResponseData;
import io.github.pollinators.honeycomb.fragment.NavigationDrawerFragment;
import io.github.pollinators.honeycomb.fragment.QuestionFragment;
import io.github.pollinators.honeycomb.fragment.QuestionFragmentBuilder;
import io.github.pollinators.honeycomb.module.DatabaseModule;
import io.github.pollinators.honeycomb.module.MediaModule;
import io.github.pollinators.honeycomb.module.QuestionModule;
import io.github.pollinators.honeycomb.survey.MedoraSurvey;
import io.github.pollinators.honeycomb.survey.Survey;
import io.github.pollinators.honeycomb.util.Events;
import io.github.pollinators.honeycomb.util.MediaFileStore;
import timber.log.Timber;


public class MainActivity extends PollinatorsBaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
    //**********************************************************************************************
    // STATIC DATA MEMBERS
    //**********************************************************************************************

    private static final String KEY_IMAGE_URI = "IMAGE_URI";
    private static final String KEY_CURRENT_RESPONSE_ID = "CURRENT_RESPONSE_ID";

    //**********************************************************************************************
    // NON-STATIC DATA MEMBERS
    //**********************************************************************************************

    @Inject Survey survey;
    @Inject SharedPreferences sharedPrefs;
    @Inject LocationClient mLocationClient;
    @Inject SQLiteOpenHelper dbHelper;
    @Inject MediaFileStore mediaFileStore;

    @Inject ResponseDataSource responseDataSource;
    @Inject ImageDataSource imageDataSource;

    private Uri imageUri;

    private QuestionFragment questionFragment;

    private int currentPosition;

    private Location lastLocation;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private ResponseData responseData;
    private MappingModel<ResponseData, Image> responseDataImage;


    private DataMappingSource<ResponseData, Image> responseDataImageDataMappingSource;

    private long currentSurveyResponseId;

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            toaster.toast("Location has changed");
            lastLocation = location;

        }
    };

    //**********************************************************************************************
    // CONSTRUCTORS
    //**********************************************************************************************

    public MainActivity() {
        super();
        getModules().add(new QuestionModule());
        getModules().add(new MediaModule());
        getModules().add(new GooglePlayServicesClientModule());
        getModules().add(new DatabaseModule());
    }

    //**********************************************************************************************
    // OVERRIDDEN METHODS
    //**********************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        openDataSources();

        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable(KEY_IMAGE_URI);
            currentSurveyResponseId = savedInstanceState.getLong(KEY_CURRENT_RESPONSE_ID, -1);
        }

        if (currentSurveyResponseId > 0) {
            responseData = responseDataSource.get(currentSurveyResponseId);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_IMAGE_URI, imageUri);
        outState.putLong(KEY_CURRENT_RESPONSE_ID, currentSurveyResponseId);
    }

    @Override
    protected void onStart() {
        super.onStart();
        openDataSources();

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

        sharedPrefs.edit().putLong(KEY_SURVEY_RESPONSE_ID, responseData.getId()).commit();

        try {
            responseDataSource.close();
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
        currentSurveyResponseId = sharedPrefs.getLong(KEY_SURVEY_RESPONSE_ID, -1);

        if (currentSurveyResponseId < 1) {
            // If surveyId is invalid, create a new responseData
            responseData = responseDataSource.create();
            sharedPrefs.edit().putLong(KEY_SURVEY_RESPONSE_ID, responseData.getId()).commit();
        } else {
            responseData = responseDataSource.get(currentSurveyResponseId);
        }

        // TODO: Take this out of production code
        if (responseData == null) {
            Timber.w("The survey response was null. Not good");
            throw new NullPointerException("The survey response was null. Not good");
        }

        mLocationClient.connect();
    }

    @Override
    protected void onPause() {
        mLocationClient.disconnect();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                if (toaster != null) {
                    toaster.toast("Image saved to:\n" + imageUri.toString());
                }

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(imageUri);
                sendBroadcast(mediaScanIntent);


                // Open the data sources here because they were closed on stop when the camera
                // intent started and not reopened yet.
                openDataSources();

                Image image = imageDataSource.create(imageUri);

                // TODO: Maybe null check image in production
                if (lastLocation != null) {
                    //TODO: Change casting
                    image.setLatitude((float) lastLocation.getLatitude());
                    image.setLongitude((float) lastLocation.getLongitude());
                }
                image.setPhotographerUserId(12345l);
                image.setTitle("This is a picture of my dog");

//                mNavigationDrawerFragment.setImageView(imageUri);
                mNavigationDrawerFragment.show();

                responseDataImage = responseDataImageDataMappingSource.create(responseData, image);

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        selectQuestion(position);
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

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

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
        } else if (id == R.id.action_camera) {
            requestCamera(null);
        }

        return super.onOptionsItemSelected(item);
    }

    //**********************************************************************************************
    // NON-STATIC METHODS
    //**********************************************************************************************

    private void openDataSources() {

        responseDataImageDataMappingSource = new DataMappingSource<ResponseData, Image>(dbHelper,
                responseDataSource, imageDataSource);

        try {
            responseDataSource.open();
            imageDataSource.open();
            responseDataImageDataMappingSource.open();
        } catch (SQLException e) {
            Timber.e(e, "Unexpected error when opening database");
            responseDataSource = null;
            imageDataSource = null;
            responseDataImageDataMappingSource = null;
        }
    }

    /**
     * update the main content by replacing fragments
     * @param questionPosition
     */
    private void selectQuestion(int questionPosition) {

        if (questionFragment != null) {
            saveQuestionData(null);

            if ((questionPosition < 0) || (questionPosition >= survey.getQuestionCount())) {
                currentPosition = 0;
                mNavigationDrawerFragment.show();
            } else {
                currentPosition = questionPosition;
            }

            questionFragment.setQuestion(currentPosition);
            retreiveQuestionData(null);
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


    @Subscribe
    public void saveQuestionData(@Nullable Events.SaveQuestionDataEvent event) {
        Object data = questionFragment.getCurrentData();

        Survey.SurveyQuestion<String> sq = survey.getQuestion(currentPosition);

        Answer answer = responseDataSource.getAnswer(responseData, currentPosition);

        if (data == null) {
            answer.clearData();
        }

        try {
            switch (sq.getType()) {
                case Survey.TYPE_YN:
                    answer.setBoolAnswer((Boolean) data);
                    break;
                case Survey.TYPE_NUMERIC:
                case Survey.TYPE_TEMPERATURE_PICKER:
                    answer.setRealValue((Double) data);
                    break;
                case Survey.TYPE_RADIO_MULTI_CHOICE:
                case Survey.TYPE_TEXT:
                    answer.setTextValue(String.valueOf(data));
                    break;
                case Survey.TYPE_NUMBER_PICKER:
                    answer.setIntValue((Integer) data);
                    break;
            }

            responseDataSource.saveAnswer(answer);

        } catch(ClassCastException e) {
            Timber.e(e, "Something happened");
        }

    }

    @Subscribe
    public void retreiveQuestionData(@Nullable Events.RetreiveQuestionDataEvent event) {
        Survey.SurveyQuestion<String> sq = survey.getQuestion(currentPosition);
        Answer answer = responseDataSource.getAnswer(responseData, currentPosition);
        // TODO: Null check answer in production

        try {
            Object data;

            switch (sq.getType()) {
               case Survey.TYPE_YN:
                    data = answer.getBoolValue();
                    break;
                case Survey.TYPE_NUMERIC:
                case Survey.TYPE_TEMPERATURE_PICKER:
                    data = answer.getRealValue();
                    break;
                case Survey.TYPE_RADIO_MULTI_CHOICE:
                case Survey.TYPE_TEXT:
                    data = answer.getTextValue();
                    break;
                case Survey.TYPE_NUMBER_PICKER:
                    data = answer.getIntValue();
                    break;
                default:
                    data = null;
            }

            questionFragment.setCurrentData(data);

        } catch (ClassCastException e) {
            Timber.e(e, "Something happened");
        }
    }

    private void submitSurvey() {
        saveQuestionData(null);

        responseData.setSurveyEnded(true);

        if (lastLocation != null) {
            //TODO: Change casting
            responseData.setLatitude((float) lastLocation.getLatitude());
            responseData.setLongitude((float) lastLocation.getLongitude());
        }

        responseDataSource.save(responseData);
        toaster.toast("Submission successful");

        // Create a new survey to take it's place
        responseData = responseDataSource.create();

        currentSurveyResponseId = responseData.getId();

        // Reset the current fragment shown
        questionFragment.setCurrentData(null);
    }

    @Subscribe
    public void requestCamera(Events.CameraRequestedEvent event) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = mediaFileStore.getImageFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
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
