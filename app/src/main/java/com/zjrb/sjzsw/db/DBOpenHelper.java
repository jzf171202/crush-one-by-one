package com.zjrb.sjzsw.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jinzifu on 2017/9/26.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    final String CREATE_SQL="CREATE TABLE test(_id INTEGER PRIMARY KEY AUTOINCREMENT,NAME)";

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
