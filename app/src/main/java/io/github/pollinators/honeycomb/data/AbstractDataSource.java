package io.github.pollinators.honeycomb.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;


import io.github.pollinators.honeycomb.data.models.AbstractModel;

/**
 * Created by ted on 10/18/14.
 */
public abstract class AbstractDataSource<MODEL extends AbstractModel> extends DatabaseSource implements CRUD<MODEL> {

    public AbstractDataSource(SQLiteOpenHelper dbHelper) {
        super(dbHelper);
    }

    public MODEL create() {
        return create(getInitializerContentValues());
    }

    /**
     * Method children may use to add additional create methods beyond default no-arg
     * @param initialContentValues
     * @return
     */
    public MODEL create(ContentValues initialContentValues) {
        return get(database.insert(getTableName(), null, initialContentValues));
    }

    public MODEL get(long id) {
        Cursor cursor = database.query(getTableName(), null,
                SurveySQLiteHelper.COLUMN_ID + " = " + id,
                null, null, null, null);

        MODEL model = null;
        if (cursor.moveToFirst()) {
            model  = cursorToModel(cursor);
            cursor.close();
        }

        return model;
    }

    public void save(MODEL model) {
        database.update(getTableName(), modelToContentValues(model),
                SurveySQLiteHelper.COLUMN_ID + " = " + model.getId(), null);
    }

    public void delete(MODEL model) {
        database.delete(getTableName(),
                SurveySQLiteHelper.COLUMN_ID + " = " + model.getId(), null);
    }

    public abstract ContentValues getInitializerContentValues();

    public abstract String getTableName();

    public abstract MODEL cursorToModel(Cursor cursor);

    public abstract ContentValues modelToContentValues(MODEL model);
}
