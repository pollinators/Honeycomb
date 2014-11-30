package io.github.pollinators.honeycomb.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

import io.github.pollinators.honeycomb.data.models.AbstractModel;

/**
 * Created by ted on 10/18/14.
 */
public class DatabaseSource {

    protected SQLiteDatabase database;
    protected SQLiteOpenHelper dbHelper;

    public DatabaseSource(SQLiteOpenHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        database.execSQL("PRAGMA foreign_keys=ON;");
    }

    public void close() {
        dbHelper.close();
    }
}
