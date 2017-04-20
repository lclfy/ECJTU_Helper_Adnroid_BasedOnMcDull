package com.mcdull.cert.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by BeginLu on 2017/4/8.
 */

public class CourseDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "hjxyt.db";
    public static final String TABLE_NAME = "course";
    private static final int DB_VERSION = 1;

    public CourseDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table course(\n" +
                "       id integer not null primary key autoincrement,\n" +
                "       kcmc CHAR(255) NOT NULL,\n" +
                "       js CHAR(255) NOT NULL,\n" +
                "       skdd CHAR(255) NOT NULL,\n" +
                "       skzc_s INT NOT NULL,\n" +
                "       skzc_e INT NOT NULL,\n" +
                "       skkssj INT NOT NULL,\n" +
                "       sksc INT NOT NULL,\n" +
                "       skxq INT NOT NULL,\n" +
                "       dsz INT NOT NULL\n" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
