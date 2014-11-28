package io.github.pollinators.honeycomb.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;


import io.github.pollinators.honeycomb.data.models.SurveyAnswer;
import io.github.pollinators.honeycomb.data.models.SurveyResponseModel;

/**
 * Created by ted on 10/18/14.
 */
public class SurveyDataSource extends AbstractDataSource<SurveyResponseModel> {

    private int questionCount;

    public SurveyDataSource(SQLiteOpenHelper dbHelper, int questionCount) {
        super(dbHelper);
        this.questionCount = questionCount;
    }

    @Override
    public String getTableName() {
        return SurveySQLiteHelper.TABLE_SURVEY_DATA;
    }

    //**********************************************************************************************
    // Survey Response
    //**********************************************************************************************

    /**
     * Create
     */
    public SurveyResponseModel create() {

        ContentValues values = new ContentValues(1);
        values.put(SurveySQLiteHelper.COLUMN_START_DATETIME, System.currentTimeMillis());
        long surveyResponseId = database.insert(SurveySQLiteHelper.TABLE_SURVEY_DATA, null, values);

        // Create place holders for all the answers
        long[] answerIds = new long[questionCount];
        for (int i = 0; i < questionCount; i++) {
            ContentValues value = new ContentValues(1);
            value.put(SurveySQLiteHelper.COLUMN_SURVEY_DATA_ID, surveyResponseId);
            answerIds[i] = database.insert(SurveySQLiteHelper.TABLE_SURVEY_ANSWERS,
                    null, value);
        }

        return get(surveyResponseId);
    }

    /**
     * Retreive
     */
    public SurveyResponseModel get(long id) {
        Cursor cursor = database.query(SurveySQLiteHelper.TABLE_SURVEY_DATA, null,
                SurveySQLiteHelper.COLUMN_ID + " = " + id,
                null, null, null, null);

        SurveyResponseModel model = null;
        if (cursor.moveToFirst()) {
            model  = cursorToModel(cursor);
            cursor.close();
        }

        if (model != null) {

            cursor = database.query(SurveySQLiteHelper.TABLE_SURVEY_ANSWERS,
                    new String[] {SurveySQLiteHelper.COLUMN_ID},
                    SurveySQLiteHelper.COLUMN_SURVEY_DATA_ID + " = " + id,
                    null, null, null, null);

            long[] answerIds = new long[questionCount];

            int i = 0;
            if (cursor.moveToFirst()) {
                while(!cursor.isAfterLast()) {
                    answerIds[i++] = cursor.getLong(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_ID));
                    cursor.moveToNext();
                }
                cursor.close();
            }

            model.setAnswerIds(answerIds);
        }

        return model;
    }

    /**
     * Delete
     * <p>
     * deletes response, with associated answers
     */
    public void delete(SurveyResponseModel model) {
        super.delete(model);

        for (long answerId : model.getAnswerIds()) {
            database.delete(SurveySQLiteHelper.TABLE_SURVEY_ANSWERS,
                    SurveySQLiteHelper.COLUMN_ID + " = " + answerId, null);
        }
    }

    //**********************************************************************************************
    // Model-to-ContentValues Methods
    //**********************************************************************************************

    /**
     * Helper method to turn our model into content values for the DB
     *
     * @param model
     * @return
     */
    public ContentValues modelToContentValues(SurveyResponseModel model) {
        ContentValues values = new ContentValues();

        if (model.getDuration() <= 0) {
            values.putNull(SurveySQLiteHelper.COLUMN_DURATION);
        } else {
            values.put(SurveySQLiteHelper.COLUMN_DURATION, model.getDuration());
        }

        if (model.getStartTime() <= 0) {
            values.putNull(SurveySQLiteHelper.COLUMN_START_DATETIME);
        } else {
            values.put(SurveySQLiteHelper.COLUMN_START_DATETIME, model.getStartTime());
        }

        if (model.getLatitude() == null) {
            values.putNull(SurveySQLiteHelper.COLUMN_LATITUDE);
        } else {
            values.put(SurveySQLiteHelper.COLUMN_LATITUDE, model.getLatitude());
        }

        if (model.getLatitude() == null) {
            values.putNull(SurveySQLiteHelper.COLUMN_LONGITUDE);
        } else {
            values.put(SurveySQLiteHelper.COLUMN_LONGITUDE, model.getLongitude());
        }

        if (model.getUserId() <= 0) {
            values.putNull(SurveySQLiteHelper.COLUMN_USER_ID);
        } else {
            values.put(SurveySQLiteHelper.COLUMN_USER_ID, model.getUserId());
        }

        return values;
    }

    /**
     * Helper method to turn our answer model into content values for the DB
     *
     * @param model
     * @return
     */
    public ContentValues answerToContentValues(SurveyAnswer model) {
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

        return values;
    }

    //**********************************************************************************************
    // Cursor-to-Model Methods
    //**********************************************************************************************

    public SurveyResponseModel cursorToModel(Cursor cursor) {
        SurveyResponseModel model = new SurveyResponseModel();

        model.setId(cursor.getInt(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_ID)));
        model.setStartTime(cursor.getLong(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_START_DATETIME)));
        model.setDuration(cursor.getInt(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_DURATION)));
        model.setLatitude(cursor.getFloat(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_LATITUDE)));
        model.setLongitude(cursor.getFloat(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_LONGITUDE)));

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

    //**********************************************************************************************
    // Survey Question Answer
    //**********************************************************************************************

    /**
     * Retreive
     *
     * @param surveyResponse
     * @param questionNumber
     * @return
     */
    public SurveyAnswer getAnswer(SurveyResponseModel surveyResponse, int questionNumber) {
        long questionId = surveyResponse.getAnswerIds()[questionNumber];

        Cursor cursor = database.query(SurveySQLiteHelper.TABLE_SURVEY_ANSWERS, null,
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

    /**
     * Update
     */
    public void saveAnswer(SurveyAnswer model) {
        database.update(SurveySQLiteHelper.TABLE_SURVEY_ANSWERS, answerToContentValues(model),
                SurveySQLiteHelper.COLUMN_ID + " = " + model.getId(), null);
    }

}
