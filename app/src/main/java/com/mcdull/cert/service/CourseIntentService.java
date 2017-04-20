package com.mcdull.cert.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.avos.avoscloud.AVUser;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mcdull.cert.HJXYT;
import com.mcdull.cert.bean.CalenderBean;
import com.mcdull.cert.bean.CourseBean;
import com.mcdull.cert.utils.Util;
import com.mcdull.cert.utils.db.CourseDao;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseIntentService extends IntentService {

    public CourseIntentService() {
        super("CourseIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("Service", "onHandleIntent");
        AVUser avUser = AVUser.getCurrentUser();
        String studentId = avUser.getString("StudentId");
        String jwcPwd = avUser.getString("JwcPwd");
        HJXYT.getInstance().getAppClient().getJWXTService().getCourse(studentId, jwcPwd).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String string = response.body().string();
                    JsonObject jsonObject = new Gson().fromJson(string, JsonObject.class);
                    if (jsonObject.get("code").getAsInt() == 1) {
                        JsonObject data = jsonObject.getAsJsonObject("data");
                        HashSet<CourseBean> hashSet = new HashSet<>();
                        int i = 1;
                        for (Map.Entry<String, JsonElement> entry : data.entrySet()) {
                            JsonObject day = entry.getValue().getAsJsonObject();
                            for (Map.Entry<String, JsonElement> elementEntry : day.entrySet()) {
                                JsonObject courseObject = elementEntry.getValue().getAsJsonObject();
                                if (courseObject.get("kcmc").getAsString().equals("")) {
                                    continue;
                                }
                                CourseBean course = new Gson().fromJson(courseObject, CourseBean.class);
                                course.skxq = i;
                                course.dsz = 0;

                                String[] skzc = courseObject.get("skzc").getAsString().split("-");
                                if (skzc[1].length() > 3) {
                                    switch (skzc[1].charAt(skzc[1].length() - 2)) {
                                        case '单':
                                            course.dsz = 1;
                                            break;
                                        case '双':
                                            course.dsz = 2;
                                            break;
                                    }
                                    skzc[1] = skzc[1].split("\\(")[0];
                                }
                                course.skzc_s = Integer.parseInt(skzc[0]);
                                course.skzc_e = Integer.parseInt(skzc[1]);
                                String[] sksj = courseObject.get("sksj").getAsString().split(",");
                                course.skkssj = Integer.parseInt(sksj[0]);
                                course.sksc = sksj.length;
                                hashSet.add(course);
                            }
                            i++;
                        }
                        Log.d("CourseIntentService", "start");
                        CourseDao courseDao = new CourseDao(CourseIntentService.this);
                        courseDao.saveCourse(hashSet);
                        ArrayList<CourseBean> all = courseDao.findAll();
                        for (CourseBean courseBean : all) {
                            Log.d("CourseIntentService", courseBean.toString());
                        }
                        Log.d("CourseIntentService", "end");
                        SharedPreferences sp = CourseIntentService.this.getSharedPreferences("config", MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("term", Util.getTerm());
                        edit.apply();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        HJXYT.getInstance().getAppClient().getJWXTService().getCalenderBean(studentId, jwcPwd).enqueue(new Callback<CalenderBean>() {
            @Override
            public void onResponse(Call<CalenderBean> call, Response<CalenderBean> response) {
                CalenderBean calenderBean = response.body();
                if (calenderBean.code != 1)
                    return;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = dateFormat.parse(calenderBean.data.date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                int week = Integer.parseInt(calenderBean.data.week);
                int weekday = Integer.parseInt(calenderBean.data.weekday);
                int day = (week - 1) * 7 + weekday - 1;
                Calendar calender = new GregorianCalendar();
                calender.setTime(date);
                calender.add(Calendar.DATE, -day);
                date = calender.getTime();
                SharedPreferences sp = CourseIntentService.this.getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putLong("date", date.getTime());
                edit.apply();
            }

            @Override
            public void onFailure(Call<CalenderBean> call, Throwable t) {

            }
        });
    }
}
