package io.github.pollinators.honeycomb.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timber.log.Timber;

/**
 * Created by ted on 10/18/14.
 */
public class SurveySQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "surveys.db";
    public static final int DATABASE_VERSION = 3;

    /**
     * Many-to-many Table format <br>
     * i.e. lefttable_righttable
     */
    public static final String TABLE_FORMAT = "%s_%s";

    public static final String TABLE_RESPONSE_DATA = "response_data";
    public static final String TABLE_ANSWERS = "answers";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_IMAGES = "images";
    public static final String TABLE_RESPONSE_DATA_IMAGES = String.format(TABLE_FORMAT, TABLE_RESPONSE_DATA, TABLE_IMAGES);

    // Id column suffix
    public static final String COLUMN_ID = "_id";

    // Other columns
    public static final String COLUMN_START_DATETIME = "start_datetime";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_USER_ID = TABLE_USERS + COLUMN_ID;
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_INTEGER = "integer";
    public static final String COLUMN_REAL = "real";
    public static final String COLUMN_BLOB = "blob";
    public static final String COLUMN_RESPONSE_DATA_ID = TABLE_RESPONSE_DATA + COLUMN_ID;
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_FIRSTNAME = "first_name";
    public static final String COLUMN_LASTNAME = "last_name";
    public static final String COLUMN_ORGANIZATION = "organization_id";
    public static final String COLUMN_URI = "uri";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_PHOTOGRAPHER_USER_ID = "photographer_" + TABLE_USERS + COLUMN_ID;
    public static final String COLUMN_TITLE = "title";

    // SurveyData-Images columns (for the many-to-many relationship)
    public static final String COLUMN_IMAGE_ID = TABLE_IMAGES + COLUMN_ID;

    public static final String TABLE_DEF_RESPONSE_DATA = "CREATE TABLE " + TABLE_RESPONSE_DATA + "("  +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_START_DATETIME + " DATETIME, " +
            COLUMN_DURATION + " INTEGER, " +
            COLUMN_USER_ID + " TEXT, " +
            COLUMN_LATITUDE + " REAL DEFAULT NULL, " +
            COLUMN_LONGITUDE + " REAL DEFAULT NULL);";

    public static final String TABLE_DEF_SURVEY_ANSWERS = "CREATE TABLE " + TABLE_ANSWERS + "("  +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_TEXT + " TEXT, " +
            COLUMN_INTEGER + " INTEGER DEFAULT NULL, " +
            COLUMN_REAL + " REAL DEFAULT NULL, " +
            COLUMN_BLOB + " BLOB, " +
            COLUMN_RESPONSE_DATA_ID + " INTEGER, " +
            "FOREIGN KEY (" + COLUMN_RESPONSE_DATA_ID + ") REFERENCES " + TABLE_RESPONSE_DATA + " (" + COLUMN_ID + "));";

    public static final String TABLE_DEF_USERS = "CREATE TABLE " + TABLE_USERS + "("  +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_USERNAME + " TEXT, " +
            COLUMN_FIRSTNAME + " TEXT, " +
            COLUMN_LASTNAME + " TEXT, " +
            COLUMN_ORGANIZATION + " INTEGER);";

    public static final String TABLE_DEF_IMAGES = "CREATE TABLE " + TABLE_IMAGES + "("  +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_URI + " TEXT, " +
            COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
            COLUMN_PHOTOGRAPHER_USER_ID + " INTEGER, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_LONGITUDE + " REAL DEFAULT NULL, " +
            COLUMN_LATITUDE + " REAL DEFAULT NULL, " +
            "FOREIGN KEY (" + COLUMN_PHOTOGRAPHER_USER_ID + ") REFERENCES " + TABLE_USERS + " (" + COLUMN_ID + "));";

    public static final String TABLE_DEF_SURVEY_DATA_IMAGES = "CREATE TABLE " + TABLE_RESPONSE_DATA_IMAGES + "("  +
            COLUMN_RESPONSE_DATA_ID + " INTEGER, " +
            COLUMN_IMAGE_ID + " INTEGER, " +
            "FOREIGN KEY (" + COLUMN_RESPONSE_DATA_ID + ") REFERENCES " + TABLE_RESPONSE_DATA + " (" + COLUMN_ID + ") ON DELETE CASCADE," +
            "FOREIGN KEY (" + COLUMN_IMAGE_ID + ") REFERENCES " + TABLE_IMAGES + " (" + COLUMN_ID + ") ON DELETE CASCADE," +
            "PRIMARY KEY (" + COLUMN_RESPONSE_DATA_ID + ", " + COLUMN_IMAGE_ID + "));";

    public SurveySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Timber.d(TABLE_DEF_RESPONSE_DATA);
        db.execSQL(TABLE_DEF_RESPONSE_DATA);
        Timber.d(TABLE_DEF_SURVEY_ANSWERS);
        db.execSQL(TABLE_DEF_SURVEY_ANSWERS);
        Timber.d(TABLE_DEF_USERS);
        db.execSQL(TABLE_DEF_USERS);
        Timber.d(TABLE_DEF_IMAGES);
        db.execSQL(TABLE_DEF_IMAGES);
        Timber.d(TABLE_DEF_SURVEY_DATA_IMAGES);
        db.execSQL(TABLE_DEF_SURVEY_DATA_IMAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPONSE_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESPONSE_DATA_IMAGES);
        onCreate(db);
    }
}
