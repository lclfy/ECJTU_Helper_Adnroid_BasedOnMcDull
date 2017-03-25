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
import com.mcdull.cert.Bean.ReExamBean;
import com.mcdull.cert.R;
import com.mcdull.cert.adapter.ReExamAdapter;

public class ReExamActivity extends MyTitleActivity {

    private TextView tvTerm;
    private TextView tvQueryTitle;
    private ListView lvReExam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_re_exam);
        super.onCreate(savedInstanceState);

        initView();

        init();

    }

    private void init() {
        String reExamJson = getIntent().getStringExtra("reExamJson");
        ReExamBean ReExamData = new Gson().fromJson(reExamJson, ReExamBean.class);
        tvQueryTitle.setText("补考安排");
        if (ReExamData != null) {
            tvTerm.setText("当前学期:"+ReExamData.data.term);
        }
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        if (ReExamData != null) {
            if (ReExamData.data != null){
                //有两个data，第一个data内包含学期和其他数据，第二个data包含全部信息（json就这么写的）
                for (int item = 0;item<ReExamData.data.data.size();item++) {
                    //获取每个补考安排
                    ReExamBean.ChildReExamBean.GrandChildReExamBean ReExamDetails = ReExamData.data.data.get(item);
                    //把arraylist填充成list
                    Map<String, String> ReExamMap = new ArrayMap<>();
                    ReExamMap.put("kcmc", ReExamDetails.kcmc);
                    //切时间
                    String[] temp1 = ReExamDetails.kssj.split("年");
                    String temp2 = temp1[1];


                    ReExamMap.put("kssj", temp1[1]);
                    ReExamMap.put("ksdd", ReExamDetails.ksdd);
                    list.add(ReExamMap);
                }
                ReExamAdapter reExamAdapter = new ReExamAdapter(this, list);
                lvReExam.setAdapter(reExamAdapter);
            }

        }
    }


    private void initView() {
        tvQueryTitle = (TextView) findViewById(R.id.tv_title);
        tvTerm = (TextView)findViewById(R.id.tv_term);
        lvReExam = (ListView) findViewById(R.id.lv_re_exam);
    }


}
