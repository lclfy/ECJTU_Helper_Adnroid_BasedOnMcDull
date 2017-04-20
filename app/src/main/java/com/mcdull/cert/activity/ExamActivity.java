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
import com.mcdull.cert.bean.ExamTimeBean;
import com.mcdull.cert.R;
import com.mcdull.cert.adapter.ExamAdapter;
import com.mcdull.cert.bean.ScoreBean;
import com.mcdull.cert.utils.ShowWaitPopupWindow;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamActivity extends BaseThemeActivity {

    private TextView tvTerm;
    private TextView tvQueryTitle;
    private ListView lvExam;
    private ExamTimeBean mExamData;
    private ShowWaitPopupWindow mWaitPopupWindow;
    private boolean loading = false;

    @Override
    protected void onResume() {
        super.onResume();
        if (mExamData == null) {
            this.loading = true;
            AVUser avUser = AVUser.getCurrentUser();
            HJXYT.getInstance().getAppClient().getJWXTService().getExamTimeBean(avUser.getString("StudentId"), avUser.getString("JwcPwd")).enqueue(new Callback<ExamTimeBean>() {
                @Override
                public void onResponse(Call<ExamTimeBean> call, Response<ExamTimeBean> response) {
                    mExamData = response.body();
                    init();
                }

                @Override
                public void onFailure(Call<ExamTimeBean> call, Throwable t) {
                    loading = false;
                    mWaitPopupWindow.dismissWait();
                }
            });
        } else {
            init();
        }
    }

    @Override
    protected void onTheme(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exam);

        initView();

        this.mWaitPopupWindow = new ShowWaitPopupWindow(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (loading)
            mWaitPopupWindow.showWait();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWaitPopupWindow != null)
            mWaitPopupWindow.dismissWait();
    }

    private void init() {
        loading = false;
        mWaitPopupWindow.dismissWait();
        if (mExamData.data == null) {
            Toast.makeText(this, "当前学期考试安排暂未公布", Toast.LENGTH_SHORT).show();
            return;
        }
        tvTerm.setText("当前学期:" + mExamData.data.term);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (mExamData != null) {
            if (mExamData.data.exam != null) {
                //有两个data，第一个data内包含学期和其他数据，第二个data包含全部信息（json就这么写的）
                for (int item = 0; item < mExamData.data.exam.size(); item++) {
                    //获取每个补考安排
                    ExamTimeBean.ChildExamTimeBean.GrandChildExamTimeBean ExamDetails = mExamData.data.exam.get(item);
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
        tvTerm = (TextView) findViewById(R.id.tv_term);
        lvExam = (ListView) findViewById(R.id.lv_searchListView);
        tvQueryTitle.setText("考试安排");
    }


}
