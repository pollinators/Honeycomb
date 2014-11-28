package io.github.pollinators.honeycomb.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import io.github.pollinators.honeycomb.data.models.User;

/**
 * Created by ted on 11/28/14.
 */
public class UserDataSource extends AbstractDataSource<User> {

    public UserDataSource(SQLiteOpenHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    public User create() {
        return null;
    }

    @Override
    public User get(long id) {
        return null;
    }

    @Override
    public String getTableName() {
        return SurveySQLiteHelper.TABLE_USERS;
    }

    @Override
    public User cursorToModel(Cursor cursor) {
        return null;
    }

    @Override
    public ContentValues modelToContentValues(User model) {
        return null;
    }
}
