package com.mcdull.cert.activity;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.mcdull.cert.ActivityMode.MyTitleActivity;
import com.mcdull.cert.Bean.CourseBean;
import com.mcdull.cert.Bean.eCardBean;
import com.mcdull.cert.R;
import com.mcdull.cert.adapter.SelectAdapter;
import com.mcdull.cert.anim.ShakeAnim;
import com.mcdull.cert.domain.CollegeIdName;
import com.mcdull.cert.utils.CourseUtil;
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.InternetUtil;
import com.mcdull.cert.utils.Util;

import static com.mcdull.cert.R.id.tv_eCardConsume;
import static com.mcdull.cert.R.id.view;

public class ImportCourseActivity extends MyTitleActivity implements OnClickListener {


    private String basicURL = "http://api1.ecjtu.org/v1/";

    private AVUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_import_course);
        super.onCreate(savedInstanceState);
        user = AVUser.getCurrentUser();
        initView();

        init();

    }

    private void initView() {
        findViewById(R.id.bt_getCourse).setOnClickListener(this);
    }

    private void init() {
        int year = Util.getSystemYear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_getCourse:
                getCourse();
                break;
        }
    }

    private void getCourse() {
            final String studentId = user.getString("StudentId");
            final String JwcPwd = user.getString("JwcPwd");
            if (TextUtils.isEmpty(studentId) || studentId.equals("null")) {
                Toast.makeText(ImportCourseActivity.this, "若需查询课程表，请先在个人信息页补全信息", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(JwcPwd) || JwcPwd.equals("null")) {
                Toast.makeText(ImportCourseActivity.this, "若需查询课程表，请先在个人信息页补全信息", Toast.LENGTH_SHORT).show();
                return;
            }


            AVQuery<AVObject> query = new AVQuery<>("API");
            query.whereEqualTo("title", "Course");
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        Map<String, String> map = new ArrayMap<>();
                        //map.put("user", studentId);//设置get参数
                        map.put("stuid", studentId);//设置get参数
                        map.put("passwd", JwcPwd);//设置get参数
                        //拼字符串-找学期
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
                        //把年月按-分开，然后8-12月是当年第一学期，1月是去年第一学期，2-7月是去年第二学期
                        String[] nowTime = df.format(new Date()).split("-");// new Date()为获取当前系统时间
                        String nowTerm = "";
                        //当年第一学期
                        if (Integer.parseInt(nowTime[1]) > 7 && Integer.parseInt(nowTime[1]) <= 12)
                        {
                            nowTerm = nowTime[0]+".1";
                        }else if(Integer.parseInt(nowTime[1]) == 1){//去年第一学期
                            nowTerm = String.valueOf(Integer.parseInt(nowTime[0])-1) + ".1";
                        }else if(Integer.parseInt(nowTime[1]) > 1){//去年第二学期
                            nowTerm = String.valueOf(Integer.parseInt(nowTime[0])-1) + ".2";
                        }
                        map.put("term", nowTerm);//设置get参数
                        new InternetUtil(CourseHandler,basicURL + "schedule", map,true,ImportCourseActivity.this);//传入参数

                    } else {
                        Toast.makeText(ImportCourseActivity.this, "查询失败，请检查网络是否顺畅", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }



        Handler CourseHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    Toast.makeText(ImportCourseActivity.this, "课表数据暂无法获取", Toast.LENGTH_SHORT).show();
                } else {

                    Bundle bundle = (Bundle) msg.obj;
                    String json = bundle.getString("Json");
                    System.out.print(json);
                    CourseBean CourseData = new Gson().fromJson(json, CourseBean.class);
                    System.out.print(CourseData);

                    }

                }

        };

    }
