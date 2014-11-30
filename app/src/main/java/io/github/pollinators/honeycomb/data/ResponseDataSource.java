package io.github.pollinators.honeycomb.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;


import io.github.pollinators.honeycomb.data.models.Answer;
import io.github.pollinators.honeycomb.data.models.ResponseData;

/**
 * Created by ted on 10/18/14.
 */
public class ResponseDataSource extends AbstractDataSource<ResponseData> {

    private int questionCount;

    public ResponseDataSource(SQLiteOpenHelper dbHelper, int questionCount) {
        super(dbHelper);
        this.questionCount = questionCount;
    }

    @Override
    public String getTableName() {
        return SurveySQLiteHelper.TABLE_RESPONSE_DATA;
    }

    //**********************************************************************************************
    // Survey Response
    //**********************************************************************************************

    /**
     * Create
     */
    public ResponseData create() {

        // Returns a model without answer IDs set since none exist yet
        ResponseData model = super.create();

        // Create place holders for all the answers
        long[] answerIds = new long[questionCount];
        for (int i = 0; i < questionCount; i++) {
            ContentValues value = new ContentValues(1);
            value.put(SurveySQLiteHelper.COLUMN_RESPONSE_DATA_ID, model.getId());
            answerIds[i] = database.insert(SurveySQLiteHelper.TABLE_ANSWERS,
                    null, value);
        }

        // Get the model again, this time with the answer IDs
        return get(model.getId());
    }

    /**
     * Retreive
     */
    public ResponseData get(long id) {
        ResponseData model = super.get(id);

        if (model != null) {
            Cursor cursor = database.query(SurveySQLiteHelper.TABLE_ANSWERS,
                    new String[] {SurveySQLiteHelper.COLUMN_ID},
                    SurveySQLiteHelper.COLUMN_RESPONSE_DATA_ID + " = " + id,
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

    @Override
    public ContentValues getInitializerContentValues() {
        ContentValues values = new ContentValues(1);
        values.put(SurveySQLiteHelper.COLUMN_START_DATETIME, System.currentTimeMillis());

        return values;
    }

    /**
     * Delete
     * <p>
     * deletes response, with associated answers
     */
    public void delete(ResponseData model) {
        super.delete(model);

        for (long answerId : model.getAnswerIds()) {
            database.delete(SurveySQLiteHelper.TABLE_ANSWERS,
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
    public ContentValues modelToContentValues(ResponseData model) {
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

        if (model.getLongitude() == null) {
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
    public ContentValues answerToContentValues(Answer model) {
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

    public ResponseData cursorToModel(Cursor cursor) {
        ResponseData model = new ResponseData();

        model.setId(cursor.getInt(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_ID)));
        model.setStartTime(cursor.getLong(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_START_DATETIME)));
        model.setDuration(cursor.getInt(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_DURATION)));

        String latVal = cursor.getString(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_LATITUDE));
        if (latVal == null) {
            model.setLatitude(null);
        } else {
            model.setLatitude(Float.parseFloat(latVal));
        }

        String lngVal = cursor.getString(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_LONGITUDE));
        if (lngVal == null) {
            model.setLongitude(null);
        } else {
            model.setLongitude(Float.parseFloat(lngVal));
        }

        return model;
    }

    public Answer cursorToAnswer(Cursor cursor) {
        Answer model = new Answer();

        // TODO: Figure out null checking
        model.setId(cursor.getInt(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_ID)));

        // First get the value as a String to check if it is Null, because we can have Null ints
        String intValue = cursor.getString(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_INTEGER));
        if (intValue == null) {
            model.setIntAnswer(null);
        } else {
            model.setIntAnswer(Integer.parseInt(intValue));
        }

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
    public Answer getAnswer(ResponseData surveyResponse, int questionNumber) {
        long questionId = surveyResponse.getAnswerIds()[questionNumber];

        Cursor cursor = database.query(SurveySQLiteHelper.TABLE_ANSWERS, null,
                SurveySQLiteHelper.COLUMN_ID + " = " + questionId +
                        " AND " + SurveySQLiteHelper.COLUMN_RESPONSE_DATA_ID + " = ?",
                new String[]{ String.valueOf(surveyResponse.getId()) }, null, null, null);

        Answer model = null;
        if (cursor.moveToFirst()) {
            model = cursorToAnswer(cursor);
            cursor.close();
        }

        return model;
    }

    /**
     * Update
     */
    public void saveAnswer(Answer model) {
        database.update(SurveySQLiteHelper.TABLE_ANSWERS, answerToContentValues(model),
                SurveySQLiteHelper.COLUMN_ID + " = " + model.getId(), null);
    }

}
