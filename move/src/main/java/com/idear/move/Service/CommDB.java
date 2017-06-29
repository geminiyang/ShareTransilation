package com.idear.move.Service;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者:geminiyang on 2017/6/28.
 * 邮箱:15118871363@163.com
 * github地址：https://github.com/geminiyang/ShareTransilation
 */

public class CommDB {
    public static final String DATABASE_NAME = "MoveDatabase.db"; //数据库名称
    public static final int DATABASE_VERSION = 1;
    //创建图像url对应id的表
    private static final String CREATE_TABLE_ImgRecords = "create table if not exists" +
            ImgRecordsDB.TABLE_NAME + "(" + ImgRecordsDB.KEY_ID +"integer," +
            ImgRecordsDB.KEY_PIC_URL +"varchar(200))";
    //创建搜索记录表
    private static final String CREATE_TABLE_SearchRecords =
            "create table if not exists" + SearchRecordsDB.TABLE_NAME + "(" + SearchRecordsDB.KEY_ID +
                    "integer primary key autoincrement," + SearchRecordsDB.KEY_Content +"varchar(200))";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public CommDB(Context ctx) {
        this.context = ctx;
        this.DBHelper = new DatabaseHelper(this.context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
           super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
                 db.execSQL(CREATE_TABLE_ImgRecords);//创建图像记录表
                 //db.execSQL(CREATE_TABLE_SearchRecords);//创建搜索记录表
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //当提供的新版本号比旧版号大的时候，将会回调该方法
        }
    }

   public CommDB open() throws SQLException {
       this.db = this.DBHelper.getWritableDatabase();
       return this;
   }


    public void close() {
        this.DBHelper.close();
    }
}

