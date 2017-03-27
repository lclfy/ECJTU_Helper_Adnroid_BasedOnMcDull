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
import com.mcdull.cert.R;
import com.mcdull.cert.adapter.ExamAdapter;
import com.mcdull.cert.adapter.ReExamAdapter;

public class ExamActivity extends MyTitleActivity {

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
        String ExamJson = getIntent().getStringExtra("ExamJson");
        ExamTimeBean ExamData = new Gson().fromJson(ExamJson, ExamTimeBean.class);
        tvQueryTitle.setText("考试安排");
        if (ExamData != null) {
            tvTerm.setText("当前学期:"+ExamData.data.term);
        }
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        if (ExamData != null) {
            if (ExamData.data.data != null){
                //有两个data，第一个data内包含学期和其他数据，第二个data包含全部信息（json就这么写的）
                for (int item = 0;item<ExamData.data.data.size();item++) {
                    //获取每个补考安排
                    ExamTimeBean.ChildExamTimeBean.GrandChildExamTimeBean ExamDetails = ExamData.data.data.get(item);
                    //把arraylist填充成list
                    Map<String, String> ExamMap = new ArrayMap<>();
                    ExamMap.put("kcmc", ExamDetails.kcmc);
                    ExamMap.put("kcxz", ExamDetails.kcxz);
                    //切时间
                    String[] temp1 = ExamDetails.kssj.split("年");
                    ExamMap.put("kssj", temp1[1]);
                    ExamMap.put("ksdd", ExamDetails.ksdd);
                    list.add(ExamMap);
                }
                ExamAdapter ExamAdapter = new ExamAdapter(this, list);
                lvExam.setAdapter(ExamAdapter);
            }

        }
    }


    private void initView() {
        tvQueryTitle = (TextView) findViewById(R.id.tv_title);
        tvTerm = (TextView)findViewById(R.id.tv_term);
        lvExam = (ListView) findViewById(R.id.lv_searchListView);
    }


}
