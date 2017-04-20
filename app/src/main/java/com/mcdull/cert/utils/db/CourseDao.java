package com.mcdull.cert.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mcdull.cert.bean.CourseBean;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by BeginLu on 2017/4/8.
 */

public class CourseDao {
    private final Context context;
    private final CourseDBHelper courseDBHelper;

    public CourseDao(Context context) {
        this.context = context;
        this.courseDBHelper = new CourseDBHelper(context);
    }

    public void saveCourse(HashSet<CourseBean> hashSet) {
        SQLiteDatabase database = courseDBHelper.getWritableDatabase();
        database.beginTransaction();
        for (CourseBean course : hashSet) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("kcmc", course.kcmc);
            contentValues.put("js", course.js);
            contentValues.put("skdd", course.skdd);
            contentValues.put("skzc_s", course.skzc_s);
            contentValues.put("skzc_e", course.skzc_e);
            contentValues.put("skkssj", course.skkssj);
            contentValues.put("sksc", course.sksc);
            contentValues.put("skxq", course.skxq);
            contentValues.put("dsz", course.dsz);
            database.insert(CourseDBHelper.TABLE_NAME, null, contentValues);
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    public ArrayList<CourseBean> findAll() {
        SQLiteDatabase database = courseDBHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from " + CourseDBHelper.TABLE_NAME, null);
        ArrayList<CourseBean> arrayList = new ArrayList<>();
        while (cursor.moveToNext()){
            CourseBean courseBean = new CourseBean();
            courseBean.kcmc =  cursor.getString(1);
            courseBean.js =  cursor.getString(2);
            courseBean.skdd =  cursor.getString(3);
            courseBean.skzc_s =  cursor.getInt(4);
            courseBean.skzc_e =  cursor.getInt(5);
            courseBean.skkssj =  cursor.getInt(6);
            courseBean.sksc =  cursor.getInt(7);
            courseBean.skxq =  cursor.getInt(8);
            courseBean.dsz =  cursor.getInt(9);
            arrayList.add(courseBean);
        }
        return arrayList;
    }
}
