package io.github.pollinators.honeycomb.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

import io.github.pollinators.honeycomb.data.models.SurveyAnswer;
import io.github.pollinators.honeycomb.data.models.SurveyResponseModel;

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
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
        database = null;
    }

    //**********************************************************************************************
    // Survey Question Answer
    //**********************************************************************************************

    public SurveyAnswer getAnswer(SurveyResponseModel surveyResponse, int questionNumber) {
        long questionId = surveyResponse.getAnswerIds()[questionNumber];
        Cursor cursor = database.query(SurveySQLiteHelper.TABLE_DEF_SURVEY_ANSWERS, null,
                SurveySQLiteHelper.COLUMN_ID + " = " + questionId +
                        " AND " + SurveySQLiteHelper.COLUMN_SURVEY_DATA_ID + " = ?",
                new String[]{ String.valueOf(surveyResponse.getId()) }, null, null, null);

        SurveyAnswer model = null;
        if (cursor.moveToFirst()) {
            model = cursorToAnswer(cursor);
            cursor.close();
        }

        return model;
    }

    //**********************************************************************************************
    // Survey Response
    //**********************************************************************************************

    /**
     * Create
     * @param answerCount
     * @return
     */
    public SurveyResponseModel createResponse(int answerCount) {
        ContentValues values = new ContentValues();

        values.put(SurveySQLiteHelper.COLUMN_START_DATETIME, System.currentTimeMillis());

        // Create place holders for all the answers
        long[] answerIds = new long[answerCount];
        for (int i = 0; i < answerCount; i++) {
            // This is strange. I have to use the "null column hack" provided by this API.
            // But I should probably just do something different. IDK!
            answerIds[i] = database.insert(SurveySQLiteHelper.TABLE_SURVEY_ANSWERS,
                    SurveySQLiteHelper.COLUMN_INTEGER, null);
        }

        long insertId = database.insert(SurveySQLiteHelper.TABLE_SURVEY_DATA, null, null);

        Cursor cursor = database.query(SurveySQLiteHelper.TABLE_SURVEY_DATA, null,
                SurveySQLiteHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null);

        SurveyResponseModel model = null;
        if (cursor.moveToFirst()) {
            model  = cursorToSurvey(cursor);
            cursor.close();

            model.setAnswerIds(answerIds);
        }

        return model;
    }

    /**
     * Retreive
     */
    public SurveyResponseModel getResponse(long id) {
        return null;
    }

    /**
     * Update
     */
    public void saveResponse(SurveyResponseModel model) {
        ContentValues values = new ContentValues();
        values.put(SurveySQLiteHelper.COLUMN_START_DATETIME, model.getStartTime());
        values.put(SurveySQLiteHelper.COLUMN_DURATION, model.getDuration());
        values.put(SurveySQLiteHelper.COLUMN_LATITUDE, model.getLatitude());
        values.put(SurveySQLiteHelper.COLUMN_LONGITUDE, model.getLongitude());

        database.update(SurveySQLiteHelper.TABLE_SURVEY_DATA, values,
                SurveySQLiteHelper.COLUMN_ID + " = " + model.getId(), null);
    }

    /**
     * Update
     */
    public void saveAnswer(SurveyAnswer model) {
        ContentValues values = new ContentValues();

        if (model.getIntAnswer() == null) {
            values.putNull(SurveySQLiteHelper.COLUMN_INTEGER);
        } else {
            values.put(SurveySQLiteHelper.COLUMN_INTEGER, model.getIntAnswer());
        }

        if (model.getTextAnswer() == null) {
            values.putNull(SurveySQLiteHelper.COLUMN_TEXT);
        } else {
            values.put(SurveySQLiteHelper.COLUMN_TEXT, model.getTextAnswer());
        }

        if (model.getRealAnswer() == null) {
            values.putNull(SurveySQLiteHelper.COLUMN_REAL);
        } else {
            values.put(SurveySQLiteHelper.COLUMN_REAL, model.getRealAnswer());
        }

        if (model.getBlobAnswer() == null) {
            values.putNull(SurveySQLiteHelper.COLUMN_BLOB);
        } else {
            values.put(SurveySQLiteHelper.COLUMN_BLOB, model.getBlobAnswer());
        }

        database.update(SurveySQLiteHelper.TABLE_SURVEY_DATA, values,
                SurveySQLiteHelper.COLUMN_ID + " = " + model.getId(), null);
    }


    /**
     * Delete
     * <p>
     * deletes response, with associated answers
     */
    public void deleteResponse(SurveyResponseModel model) {
        database.delete(SurveySQLiteHelper.TABLE_SURVEY_DATA,
                SurveySQLiteHelper.COLUMN_ID + " = " + model.getId(), null);

        for (long answerId : model.getAnswerIds()) {
            database.delete(SurveySQLiteHelper.TABLE_SURVEY_ANSWERS,
                    SurveySQLiteHelper.COLUMN_ID + " = " + answerId, null);
        }
    }

    //**********************************************************************************************
    // Cursor-to-Model Methods
    //**********************************************************************************************

    public SurveyResponseModel cursorToSurvey(Cursor cursor) {
        SurveyResponseModel model = new SurveyResponseModel();

        model.setId(cursor.getInt(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_ID)));
        model.setStartTime(cursor.getInt(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_START_DATETIME)));
        model.setDuration(cursor.getInt(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_DURATION)));
        model.setLatitude(cursor.getInt(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_LATITUDE)));
        model.setLongitude(cursor.getInt(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_LONGITUDE)));

        return model;
    }

    public SurveyAnswer cursorToAnswer(Cursor cursor) {
        SurveyAnswer model = new SurveyAnswer();

        // TODO: Figure out null checking
        model.setId(cursor.getInt(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_ID)));
        model.setIntAnswer(cursor.getInt(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_INTEGER)));
        model.setTextAnswer(cursor.getString(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_TEXT)));
        model.setRealAnswer(cursor.getDouble(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_REAL)));
        model.setBlobAnswer(cursor.getBlob(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_BLOB)));

        return model;
    }
}
