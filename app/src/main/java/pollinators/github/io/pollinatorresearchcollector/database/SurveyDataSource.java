package pollinators.github.io.pollinatorresearchcollector.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pollinators.github.io.pollinatorresearchcollector.database.models.SurveyResponseModel;

/**
 * Created by ted on 10/18/14.
 */
public class SurveyDataSource {

    private SQLiteDatabase database;
    private SurveySQLiteHelper dbHelper;

    public SurveyDataSource(Context context) {
        dbHelper = new SurveySQLiteHelper(context);
    }

    public void open() throws SQLException {
        dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //**********************************************************************************************
    // CRUD
    //**********************************************************************************************

    /**
     * Create
     */
    public SurveyResponseModel createResponse(String table, List<Object> answers) {
        ContentValues values = new ContentValues();

        values.put(SurveySQLiteHelper.COLUMN_START_DATETIME, System.currentTimeMillis());

        // Put all the answers
        for (int i = 0; i < answers.size(); i++) {
            String answer  = answers.get(i).toString();
            values.put(String.format("answer_%d", i), answer);
        }

        long insertId = database.insert(table, null, values);
        Cursor cursor = database.query(table, null, SurveySQLiteHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null);

        SurveyResponseModel model = null;
        if (cursor.moveToFirst()) {
            model  = cursorToModel(cursor);
            cursor.close();
        }

        return model;
    }

    /**
     * Retreive
     */
    public SurveyResponseModel getResponse() {
        return null;
    }

    /**
     * Update
     */
    public void updateResponse(SurveyResponseModel model) {

    }

    /**
     * Delete
     */
    public void deleteResponse(SurveyResponseModel model) {

    }

    public SurveyResponseModel cursorToModel(Cursor cursor) {
        return null;
    }
}
