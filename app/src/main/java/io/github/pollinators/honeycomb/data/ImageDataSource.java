package io.github.pollinators.honeycomb.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import io.github.pollinators.honeycomb.data.models.Image;

/**
 * Created by ted on 11/28/14.
 */
public class ImageDataSource extends AbstractDataSource<Image> {

    public ImageDataSource(SQLiteOpenHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    public ContentValues getInitializerContentValues() {
        ContentValues values = new ContentValues(1);
        values.put(SurveySQLiteHelper.COLUMN_TIMESTAMP, System.currentTimeMillis());

        return values;
    }

    /**
     * Convenience method to create with a URI
     * @param imageUri
     * @return
     */
    public Image create(Uri imageUri) {
        ContentValues initialValues = new ContentValues(getInitializerContentValues().size() + 1);
        initialValues.putAll(getInitializerContentValues());
        initialValues.put(SurveySQLiteHelper.COLUMN_URI, imageUri.toString());
        return create(initialValues);
    }

    @Override
    public String getTableName() {
        return SurveySQLiteHelper.TABLE_IMAGES;
    }

    @Override
    public Image cursorToModel(Cursor cursor) {
        Image model = new Image();

        model.setId(cursor.getInt(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_ID)));

        String uriString = cursor.getString(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_URI));
        if (uriString == null) {
            model.setUri(null);
        } else {
            model.setUri(Uri.parse(uriString));
        }

        model.setTitle(cursor.getString(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_TITLE)));
        model.setTimestamp(cursor.getLong(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_TIMESTAMP)));
        model.setPhotographerUserId(cursor.getLong(cursor.getColumnIndex(SurveySQLiteHelper.COLUMN_PHOTOGRAPHER_USER_ID)));

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

    @Override
    public ContentValues modelToContentValues(Image model) {
        ContentValues values = new ContentValues();

        if (model.getUri() == null) {
            values.putNull(SurveySQLiteHelper.COLUMN_URI);
        } else {
            values.put(SurveySQLiteHelper.COLUMN_URI, model.getUri().toString());
        }

        if (model.getTitle() == null) {
            values.putNull(SurveySQLiteHelper.COLUMN_TITLE);
        } else {
            values.put(SurveySQLiteHelper.COLUMN_TITLE, model.getTitle());
        }

        if (model.getTimestamp() <= 0) {
            values.putNull(SurveySQLiteHelper.COLUMN_TIMESTAMP);
        } else {
            values.put(SurveySQLiteHelper.COLUMN_TIMESTAMP, model.getTimestamp());
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

        if (model.getPhotographerUserId() <= 0) {
            values.putNull(SurveySQLiteHelper.COLUMN_PHOTOGRAPHER_USER_ID);
        } else {
            values.put(SurveySQLiteHelper.COLUMN_PHOTOGRAPHER_USER_ID, model.getPhotographerUserId());
        }

        return values;
    }
}
