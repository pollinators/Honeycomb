package pollinators.github.io.pollinatorresearchcollector.database;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pollinators.github.io.pollinatorresearchcollector.R;
import pollinators.github.io.pollinatorresearchcollector.survey.PollinatorSurvey;
import pollinators.github.io.pollinatorresearchcollector.survey.Survey;

/**
 * Created by ted on 10/18/14.
 */
public class SurveySQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "surveys.db";
    public static final int DATABASE_VERSION = 1;

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_START_DATETIME = "start_datetime";
    public static final String COLUMN_END_DATETIME = "end_datetime";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    public static final String SQL_TABLE_CREATION_TEMPLATE = "CREATE TABLE %s " +
            "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_START_DATETIME + " DATETIME, " +
            COLUMN_END_DATETIME + " DATETIME, " +
            COLUMN_LATITUDE + " REAL, " +
            COLUMN_LONGITUDE + " REAL, %s);";

    public List<Survey> surveys;
    public List<String> tableDefs;

    public SurveySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        Resources resources = context.getResources();
        tableDefs = new ArrayList<String>(13);
        tableDefs.add(resources.getString(R.string.organizations_table_def));
        tableDefs.add(resources.getString(R.string.answers_table_def));
        tableDefs.add(resources.getString(R.string.questions_table_def));
        tableDefs.add(resources.getString(R.string.question_options_table_def));
        tableDefs.add(resources.getString(R.string.input_types_table_def));
        tableDefs.add(resources.getString(R.string.option_choices_table_def));
        tableDefs.add(resources.getString(R.string.option_groups_table_def));
        tableDefs.add(resources.getString(R.string.survey_comments_table_def));
        tableDefs.add(resources.getString(R.string.survey_headers_table_def));
        tableDefs.add(resources.getString(R.string.survey_sections_table_def));
        tableDefs.add(resources.getString(R.string.user_survey_sections_table_def));
        tableDefs.add(resources.getString(R.string.users_table_def));
        tableDefs.add(resources.getString(R.string.unit_of_measures_table_def));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String def : tableDefs) {
            Log.d("TABLE", def);
            db.execSQL(def);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
