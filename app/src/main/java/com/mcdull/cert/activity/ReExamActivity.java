package com.mcdull.cert.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.google.gson.Gson;
import com.mcdull.cert.HJXYT;
import com.mcdull.cert.activity.base.BaseThemeActivity;
import com.mcdull.cert.bean.ReExamBean;
import com.mcdull.cert.R;
import com.mcdull.cert.adapter.ReExamAdapter;
import com.mcdull.cert.utils.ShowWaitPopupWindow;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReExamActivity extends BaseThemeActivity {

    private TextView tvTerm;
    private TextView tvQueryTitle;
    private ListView lvReExam;
    private ReExamBean mReExamData;
    private boolean loading = false;
    private ShowWaitPopupWindow mWaitPopupWindow;

    @Override
    protected void onTheme(Bundle savedInstanceState) {
        setContentView(R.layout.activity_re_exam);

        initView();

        mWaitPopupWindow = new ShowWaitPopupWindow(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mReExamData == null) {
            AVUser avUser = AVUser.getCurrentUser();
            loading = true;
            HJXYT.getInstance().getAppClient().getJWXTService().getBexamBean(avUser.getString("StudentId"), avUser.getString("JwcPwd")).enqueue(new Callback<ReExamBean>() {
                @Override
                public void onResponse(Call<ReExamBean> call, Response<ReExamBean> response) {
                    mReExamData = response.body();
                    init();
                }

                @Override
                public void onFailure(Call<ReExamBean> call, Throwable t) {
                    loading = false;
                    mWaitPopupWindow.dismissWait();
                }
            });
        } else {
            init();
        }
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
        if (mReExamData.data == null) {
            return;
        }
//        String reExamJson = getIntent().getStringExtra("reExamJson");
//        ReExamBean mReExamData = new Gson().fromJson(reExamJson, ReExamBean.class);
        tvTerm.setText("当前学期:" + mReExamData.data.term);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (mReExamData.data.bexam != null) {
            //有两个data，第一个data内包含学期和其他数据，第二个data包含全部信息（json就这么写的）
            for (int item = 0; item < mReExamData.data.bexam.size(); item++) {
                //获取每个补考安排
                ReExamBean.ChildReExamBean.GrandChildReExamBean ReExamDetails = mReExamData.data.bexam.get(item);
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


    private void initView() {
        tvQueryTitle = (TextView) findViewById(R.id.tv_title);
        tvTerm = (TextView) findViewById(R.id.tv_term);
        lvReExam = (ListView) findViewById(R.id.lv_re_exam);
        tvQueryTitle.setText("补考安排");
    }


}
