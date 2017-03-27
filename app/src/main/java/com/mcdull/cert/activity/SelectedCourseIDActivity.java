package com.mcdull.cert.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.internal.Streams;
import com.mcdull.cert.ActivityMode.MyTitleActivity;
import com.mcdull.cert.Bean.ExamTimeBean;
import com.mcdull.cert.Bean.ReExamBean;
import com.mcdull.cert.Bean.SelectedCourseIDBean;
import com.mcdull.cert.R;
import com.mcdull.cert.adapter.ExamAdapter;
import com.mcdull.cert.adapter.ReExamAdapter;
import com.mcdull.cert.adapter.SelectedCourseIDAdapter;

public class SelectedCourseIDActivity extends MyTitleActivity {

    private TextView tvTerm;
    private TextView tvQueryTitle;
    private ListView lvExam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exam);
        super.onCreate(savedInstanceState);

        initView();

        init();

    }

    private void init() {
        String ExamJson = getIntent().getStringExtra("sCIDJson");
        SelectedCourseIDBean ExamData = new SelectedCourseIDBean();
        try {
            ExamData = new Gson().fromJson(ExamJson, SelectedCourseIDBean.class);
        }catch (Exception e){
            ExamData = new Gson().fromJson(ExamJson, SelectedCourseIDBean.class);
        }

        tvQueryTitle.setText("选修小班信息");
        if (ExamData != null) {
            tvTerm.setText("当前学期:"+ExamData.data.term);
        }
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        if (ExamData != null) {
            if (ExamData.data.number != null){
                //有两个data，第一个data内包含学期和其他数据，第二个data包含全部信息（json就这么写的）
                for (int item = 0;item<ExamData.data.number.size();item++) {
                    //获取每个补考安排
                    SelectedCourseIDBean.ChildSelectedCourseIDBean.GrandChildSelectedCourseIDBean Details = ExamData.data.number.get(item);
                    //把arraylist填充成list
                    Map<String, String> Map = new ArrayMap<>();
                    Map.put("kcmc", Details.kcmc);
                    Map.put("xbmc", Details.xbmc);
                    Map.put("xbxh", Details.xbxh);
                    list.add(Map);
                }
                SelectedCourseIDAdapter Adapter = new SelectedCourseIDAdapter(this, list);
                lvExam.setAdapter(Adapter);
            }

        }
    }


    private void initView() {
        tvQueryTitle = (TextView) findViewById(R.id.tv_title);
        tvTerm = (TextView)findViewById(R.id.tv_term);
        lvExam = (ListView) findViewById(R.id.lv_searchListView);
    }


}

