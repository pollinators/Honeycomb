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
    public static final String TABLE_USERS = "users";

    // Id column suffix
    public static final String COLUMN_ID = "_id";

    // Survey data columns
    public static final String COLUMN_START_DATETIME = "start_datetime";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_USER_ID = "user_id";

    // Survey answers columns
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_INTEGER = "integer";
    public static final String COLUMN_REAL = "real";
    public static final String COLUMN_BLOB = "blob";
    public static final String COLUMN_SURVEY_DATA_ID = TABLE_SURVEY_DATA + "_id";

    // Users columns
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_FIRSTNAME = "first_name";
    public static final String COLUMN_LASTNAME = "last_name";
    public static final String COLUMN_ORGANIZATION = "organization_id";


    public static final String TABLE_DEF_SURVEY_DATA = "CREATE TABLE " + TABLE_SURVEY_DATA + "("  +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_START_DATETIME + " DATETIME, " +
            COLUMN_DURATION + " INTEGER, " +
            COLUMN_USER_ID + " TEXT, " +
            COLUMN_LATITUDE + " REAL, " +
            COLUMN_LONGITUDE + " REAL);";

    public static final String TABLE_DEF_SURVEY_ANSWERS = "CREATE TABLE " + TABLE_SURVEY_ANSWERS + "("  +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_TEXT + " TEXT, " +
            COLUMN_INTEGER + " INTEGER, " +
            COLUMN_REAL + " REAL, " +
            COLUMN_BLOB + " BLOB, " +
            COLUMN_SURVEY_DATA_ID + " INTEGER, " +
            "FOREIGN KEY (" + COLUMN_SURVEY_DATA_ID + ") REFERENCES " + TABLE_SURVEY_DATA + " (" + COLUMN_ID + "));";

    public static final String TABLE_DEF_USERS = "CREATE TABLE " + TABLE_USERS + "("  +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_USERNAME + " TEXT NOT NULL, " +
            COLUMN_FIRSTNAME + " TEXT, " +
            COLUMN_LASTNAME + " TEXT, " +
            COLUMN_ORGANIZATION + " INTEGER);";

    public SurveySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Timber.d(TABLE_DEF_SURVEY_DATA);
        db.execSQL(TABLE_DEF_SURVEY_DATA);
        Timber.d(TABLE_DEF_SURVEY_ANSWERS);
        db.execSQL(TABLE_DEF_SURVEY_ANSWERS);
        Timber.d(TABLE_DEF_USERS);
        db.execSQL(TABLE_DEF_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SURVEY_ANSWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEF_USERS);
        onCreate(db);
    }
}
