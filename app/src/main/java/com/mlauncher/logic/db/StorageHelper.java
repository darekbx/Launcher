package com.mlauncher.logic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by INFOR PL on 2015-01-27.
 */
public class StorageHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;

    public StorageHelper(Context context) {
        super(context, "data.db", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS apps (\n" +
                "        _id integer primary key autoincrement,\n" +
                "        package_name TEXT,\n" +
                "        name TEXT,\n" +
                "        label TEXT,\n" +
                "        clicks INTEGER DEFAULT 0,\n" +
                "        UNIQUE(package_name))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}