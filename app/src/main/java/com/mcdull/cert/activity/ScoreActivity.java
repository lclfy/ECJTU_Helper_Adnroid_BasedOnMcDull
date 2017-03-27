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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.internal.Streams;
import com.mcdull.cert.ActivityMode.MyTitleActivity;
import com.mcdull.cert.Bean.ExamTimeBean;
import com.mcdull.cert.Bean.ReExamBean;
import com.mcdull.cert.Bean.ScoreBean;
import com.mcdull.cert.R;
import com.mcdull.cert.adapter.ExamAdapter;
import com.mcdull.cert.adapter.ReExamAdapter;
import com.mcdull.cert.adapter.ScoreAdapter;

public class ScoreActivity extends MyTitleActivity {

    private TextView tvTerm;
    private TextView tvQueryTitle;
    private ListView lvScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //这里和成绩查询公用界面了
        setContentView(R.layout.activity_exam);
        super.onCreate(savedInstanceState);

        initView();

        init();
        Toast.makeText(ScoreActivity.this, "补考成绩仍可在此处查询\n将显示补考或重修", Toast.LENGTH_SHORT).show();

    }

    private void init() {
        String ScoreJson = getIntent().getStringExtra("ScoreJson");
        ScoreBean ScoreData = new Gson().fromJson(ScoreJson, ScoreBean.class);
        tvQueryTitle.setText("成绩查询");
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        if (ScoreData != null) {
            tvTerm.setText("当前学期:"+ScoreData.data.term);
        }
        if (ScoreData != null) {
            if (ScoreData.data != null){
                //有两个data，第一个data内包含学期和其他数据，第二个data包含全部信息（json就这么写的）
                for (int item = 0;item<ScoreData.data.score.size();item++) {

                    ScoreBean.ChildScoreBean.GrandChildScoreBean ScoreDetails = ScoreData.data.score.get(item);
                    //把arraylist填充成list
                    Map<String, String> ExamMap = new ArrayMap<>();
                    ExamMap.put("kcmc", ScoreDetails.kcmc);
                    ExamMap.put("khfs", ScoreDetails.khfs);
                    ExamMap.put("kcxf", ScoreDetails.kcxf);
                    ExamMap.put("kscj", ScoreDetails.kscj);
                    ExamMap.put("ckcj", ScoreDetails.ckcj);
                    ExamMap.put("cxcj", ScoreDetails.cxcj);
                    list.add(ExamMap);
                }
                ScoreAdapter ScoreAdapter = new ScoreAdapter(this, list);
                lvScore.setAdapter(ScoreAdapter);
            }

        }
    }


    private void initView() {
        tvQueryTitle = (TextView) findViewById(R.id.tv_title);
        tvTerm = (TextView)findViewById(R.id.tv_term);
        lvScore = (ListView) findViewById(R.id.lv_searchListView);
    }


}
