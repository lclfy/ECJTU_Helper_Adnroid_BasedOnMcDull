package com.mcdull.cert.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.google.gson.Gson;
import com.mcdull.cert.HJXYT;
import com.mcdull.cert.activity.base.BaseThemeActivity;
import com.mcdull.cert.bean.ClassmatesListBean;
import com.mcdull.cert.bean.ScoreBean;
import com.mcdull.cert.R;
import com.mcdull.cert.adapter.ScoreAdapter;
import com.mcdull.cert.utils.ShowWaitPopupWindow;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoreActivity extends BaseThemeActivity {

    private TextView tvTerm;
    private TextView tvQueryTitle;
    private ListView lvScore;
    private ScoreBean mScoreData;
    private ShowWaitPopupWindow mWaitPopupWindow;
    private boolean loading = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (mScoreData == null) {
            loading = true;
            AVUser avUser = AVUser.getCurrentUser();
            String term = "2016.1";
            HJXYT.getInstance().getAppClient().getJWXTService().getScoreBean(avUser.getString("StudentId"), avUser.getString("JwcPwd"), term).enqueue(new Callback<ScoreBean>() {
                @Override
                public void onResponse(Call<ScoreBean> call, Response<ScoreBean> response) {
                    mScoreData = response.body();
                    init();
                }

                @Override
                public void onFailure(Call<ScoreBean> call, Throwable t) {
                    loading = false;
                    mWaitPopupWindow.dismissWait();
                }
            });
        } else {
            init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loading)
            mWaitPopupWindow.dismissWait();
    }

    @Override
    protected void onTheme(Bundle savedInstanceState) {
        //这里和成绩查询公用界面了
        setContentView(R.layout.activity_exam);

        initView();

        this.mWaitPopupWindow = new ShowWaitPopupWindow(this);

        Toast.makeText(ScoreActivity.this, "补考成绩仍可在此处查询\n将显示补考或重修", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (mScoreData == null)
            mWaitPopupWindow.showWait();
    }

    private void init() {
        loading = false;
        mWaitPopupWindow.dismissWait();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (mScoreData != null) {
            tvTerm.setText("当前学期:" + mScoreData.data.term);
        }
        if (mScoreData != null) {
            if (mScoreData.data != null) {
                //有两个data，第一个data内包含学期和其他数据，第二个data包含全部信息（json就这么写的）
                for (int item = 0; item < mScoreData.data.score.size(); item++) {

                    ScoreBean.ChildScoreBean.GrandChildScoreBean ScoreDetails = mScoreData.data.score.get(item);
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
        tvTerm = (TextView) findViewById(R.id.tv_term);
        lvScore = (ListView) findViewById(R.id.lv_searchListView);
        tvQueryTitle.setText("成绩查询");
    }


}
