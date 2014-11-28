package io.github.pollinators.honeycomb.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.sql.SQLException;

import io.github.pollinators.honeycomb.data.models.AbstractModel;
import io.github.pollinators.honeycomb.data.models.SurveyAnswer;
import io.github.pollinators.honeycomb.data.models.SurveyResponseModel;

/**
 * Created by ted on 10/18/14.
 */
public abstract class AbstractDataSource<MODEL extends AbstractModel> {

    protected SQLiteDatabase database;
    protected SQLiteOpenHelper dbHelper;

    public AbstractDataSource(SQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public abstract MODEL create();

    public abstract MODEL get(long id);

    public void save(MODEL model) {
        database.update(getTableName(), modelToContentValues(model),
                SurveySQLiteHelper.COLUMN_ID + " = " + model.getId(), null);
    }

    public void delete(MODEL model) {
        database.delete(getTableName(),
                SurveySQLiteHelper.COLUMN_ID + " = " + model.getId(), null);
    }

    public abstract String getTableName();

    public abstract MODEL cursorToModel(Cursor cursor);

    public abstract ContentValues modelToContentValues(MODEL model);
}
