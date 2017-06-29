package com.idear.move.Service;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.idear.move.util.Logger;

/**
 * 作者:geminiyang on 2017/6/28.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class SearchRecordsDB {
    public static final String KEY_ID = "id";
    public static final String KEY_Content = "content";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    public static final String TABLE_NAME = "SearchRecords";
    private final Context context;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, CommDB.DATABASE_NAME, null, CommDB.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Logger.d("Upgrading database from version " + oldVersion + " to " + newVersion +
                    ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public SearchRecordsDB(Context ctx) {
        this.context= ctx;
    }

    public SearchRecordsDB open() throws SQLException {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    //记得在Activity,destroy时关闭数据库
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    //封装一些自定义的数据库操作
}
