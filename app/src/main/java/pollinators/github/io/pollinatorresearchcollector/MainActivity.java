package pollinators.github.io.pollinatorresearchcollector;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;

import com.twotoasters.android.support.v7.widget.DefaultItemAnimator;
import com.twotoasters.android.support.v7.widget.LinearLayoutManager;
import com.twotoasters.android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @InjectView(R.id.list) RecyclerView mRecyclerView;

    @InjectView(R.id.btn_add_question) Button mAddQuestionButton;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        ButterKnife.inject(this);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // Set up the list view (using the new Android RecyclerView)
        mRecyclerView.setHasFixedSize(true);
        final SurveyRecycleAdapter surveyAdapter = new SurveyRecycleAdapter(
                createMockList(),
                R.layout.view_survey_question);
        mRecyclerView.setAdapter(surveyAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        surveyAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener<SurveyRecycleAdapter.SurveyQuestionModel>() {
            @Override
            public void onItemClick(View view, SurveyRecycleAdapter.SurveyQuestionModel model) {
                surveyAdapter.remove(model);
            }
        });

        surveyAdapter.setOnSubmitClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surveyAdapter.add(new SurveyRecycleAdapter.SurveyQuestionModel("This is the next question?", "", 0), surveyAdapter.getItemCount());
            }
        });

        mAddQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                surveyAdapter.add(new SurveyRecycleAdapter.SurveyQuestionModel("Added question?", "", 0), 1);
            }
        });
    }

    private List<SurveyRecycleAdapter.SurveyQuestionModel> createMockList() {
        List<SurveyRecycleAdapter.SurveyQuestionModel> items = new ArrayList<SurveyRecycleAdapter.SurveyQuestionModel>();
        for (int i = 0; i < 20; i++) {
            items.add(new SurveyRecycleAdapter.SurveyQuestionModel("Question?", "This is an answer", 0));
        }
        return items;
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        new Utils.Toaster(this).toast("Position was" + String.valueOf(position));
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
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
