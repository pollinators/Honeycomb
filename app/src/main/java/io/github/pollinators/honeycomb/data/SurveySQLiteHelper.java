package io.github.pollinators.honeycomb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import io.github.pollinators.honeycomb.survey.Survey;
import timber.log.Timber;

/**
 * Created by ted on 10/18/14.
 */
public class SurveySQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "surveys.db";
    public static final int DATABASE_VERSION = 2;

    public static final String TABLE_SURVEY_DATA = "survey_data";
    public static final String TABLE_SURVEY_ANSWERS = "survey_answers";

    // Id column suffix
    public static final String COLUMN_ID = "_id";

    // Survey data columns
    public static final String COLUMN_START_DATETIME = "start_datetime";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    // Survey answers columns
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_INTEGER = "integer";
    public static final String COLUMN_REAL = "real";
    public static final String COLUMN_BLOB = "blob";
    public static final String COLUMN_SURVEY_DATA_ID = TABLE_SURVEY_DATA + "_id";

    public static final String TABLE_DEF_SURVEY_DATA = "CREATE TABLE " + TABLE_SURVEY_DATA + "("  +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_START_DATETIME + " DATETIME, " +
            COLUMN_DURATION + " INTEGER, " +
            COLUMN_LATITUDE + " REAL, " +
            COLUMN_LONGITUDE + " REAL);";

    public static final String TABLE_DEF_SURVEY_ANSWERS = "CREATE TABLE " + TABLE_SURVEY_ANSWERS + "("  +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TEXT + " TEXT, " +
            COLUMN_INTEGER + " INTEGER, " +
            COLUMN_REAL + " REAL, " +
            COLUMN_BLOB + " BLOB, " +
            COLUMN_SURVEY_DATA_ID + " INTEGER, " +
            "FOREIGN KEY (" + COLUMN_SURVEY_DATA_ID + ") REFERENCES " + TABLE_SURVEY_DATA + " (" + COLUMN_ID + "));";

    public List<Survey> surveys;
    public List<String> tableDefs;

    public SurveySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Timber.d(TABLE_DEF_SURVEY_DATA);
        db.execSQL(TABLE_DEF_SURVEY_DATA);
        Timber.d(TABLE_DEF_SURVEY_ANSWERS);
        db.execSQL(TABLE_DEF_SURVEY_ANSWERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY_ANSWERS);
        onCreate(db);
    }
}
