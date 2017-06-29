package com.idear.move.Service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.idear.move.POJO.User;
import com.idear.move.util.Logger;

import java.util.ArrayList;

/**
 * 作者:geminiyang on 2017/6/28.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class ImgRecordsDB {
    public static final String KEY_ID = "id";
    public static final String KEY_PIC_URL = "picUrl";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    public static final String TABLE_NAME = "ImgRecords";
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

    public ImgRecordsDB(Context ctx) {
        this.context= ctx;
    }

    public ImgRecordsDB open() throws SQLException {
        mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    //下面开始封装一些具体的自定义的数据库操作
    /**
     * 创建一个保存用户id和对应图像url的
     * @param id
     * @param picUrl
     * @return
     */
    public long createUser(String id,String picUrl) {
        long createResult = 0;
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, id);
        initialValues.put(KEY_PIC_URL, picUrl);
        try {
            createResult = mDb.insert(TABLE_NAME, null, initialValues);
            Logger.d("the new row is " + createResult);
        } catch (Exception e) {
            //在这里添加一些自定义的异常处理
            Logger.d("delete all data failed!");
            e.printStackTrace();
        }
        return createResult;
    }

    /**
     * 删除表中全部数据
     * @return
     */
    public boolean deleteAllUsers() {
        int deleteResult = 0;
        try {
            deleteResult = mDb.delete(TABLE_NAME, null, null);
            Logger.d(deleteResult+"row has been deleted!");
        } catch (Exception e) {
            //在这里添加一些自定义的异常处理
            e.printStackTrace();
        }
        return deleteResult > 0;
    }

    /**
     * 根据名称删除表中的数据
     * @param id
     * @return
     */
    public boolean deleteUserByID(String id) {
        int isDelete;
        String[] tName = new String[] {id};
        isDelete = mDb.delete(TABLE_NAME, KEY_ID + "=?", tName);
        Logger.d("isDelete:" + isDelete + "---" + "UserID = " + id);
        return isDelete > 0;
    }

    /**
     * 获取表中的所有字段
     * @return
     */
    public ArrayList<User> queryAll() {

        ArrayList<User> allUsersList = new ArrayList<>();
        Cursor mCursor = null;
        mCursor = mDb.query(TABLE_NAME, new String[] {KEY_ID,KEY_PIC_URL}, null, null, null, null, null);
        if (mCursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_ID)));
                user.setPicUrl(mCursor.getString(mCursor.getColumnIndexOrThrow(KEY_PIC_URL)));
                allUsersList.add(user);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
        return allUsersList;
    }

}
