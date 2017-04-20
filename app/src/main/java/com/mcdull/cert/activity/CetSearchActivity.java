package com.mcdull.cert.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mcdull.cert.bean.CETBean;
import com.mcdull.cert.R;
import com.mcdull.cert.activity.base.BaseActivity;

public class CetSearchActivity extends BaseActivity {

    private TextView tv_cetName;
    private TextView tv_cetSchool;
    private TextView tv_cetTotalScore;
    private TextView tv_cetListeningScore;
    private TextView tv_cetReadingScore;
    private TextView tv_cetWritingScore;
    private TextView tv_cetSpeakLevel;
    private TextView tv_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cet_search);
        super.onCreate(savedInstanceState);
        //判断SDK版本，设置沉浸状态栏
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            findViewById(R.id.status_bar).setVisibility(View.VISIBLE);
        }
        //返回键
        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();

        init();
    }

    private void init() {
        String CETJson = getIntent().getStringExtra("CETJson");
        CETBean CETData = new Gson().fromJson(CETJson, CETBean.class);
        if (CETData != null) {
            tv_cetName.setText(CETData.data.name);
            tv_cetSchool.setText(CETData.data.school);
            tv_type.setText("全国大学"+CETData.data.type+"考试");

            tv_cetTotalScore.setText(CETData.data.w_test.total);
            tv_cetListeningScore.setText(CETData.data.w_test.listen);
            tv_cetReadingScore.setText(CETData.data.w_test.reading);
            tv_cetWritingScore.setText(CETData.data.w_test.writing);

            tv_cetSpeakLevel.setText(CETData.data.s_test.level);
        }

    }


    private void initView() {
        tv_cetName = (TextView) findViewById(R.id.tv_cetName);
        tv_cetSchool = (TextView) findViewById(R.id.tv_cetSchool);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_cetTotalScore = (TextView) findViewById(R.id.tv_cetTotalScore);
        tv_cetListeningScore = (TextView) findViewById(R.id.tv_cetListeningScore);
        tv_cetReadingScore = (TextView) findViewById(R.id.tv_cetReadingScore);
        tv_cetWritingScore = (TextView) findViewById(R.id.tv_cetWritingScore);
        tv_cetSpeakLevel = (TextView) findViewById(R.id.tv_cetSpeakLevel);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
