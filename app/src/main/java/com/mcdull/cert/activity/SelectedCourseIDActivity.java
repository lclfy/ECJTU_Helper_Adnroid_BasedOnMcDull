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
import com.mcdull.cert.bean.SelectedCourseIDBean;
import com.mcdull.cert.R;
import com.mcdull.cert.adapter.SelectedCourseIDAdapter;
import com.mcdull.cert.utils.ShowWaitPopupWindow;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedCourseIDActivity extends BaseThemeActivity {

    private TextView tvTerm;
    private TextView tvQueryTitle;
    private ListView lvExam;
    private SelectedCourseIDBean mExamData;
    private boolean loading;
    private ShowWaitPopupWindow mWaitPopupWindow;

    @Override
    protected void onTheme(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exam);

        initView();

        mWaitPopupWindow = new ShowWaitPopupWindow(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mExamData == null) {
            this.loading = true;
            AVUser avUser = AVUser.getCurrentUser();
            HJXYT.getInstance().getAppClient().getJWXTService().getSelectedCourseIDBean(avUser.getString("StudentId"), avUser.getString("JwcPwd")).enqueue(new Callback<SelectedCourseIDBean>() {
                @Override
                public void onResponse(Call<SelectedCourseIDBean> call, Response<SelectedCourseIDBean> response) {
                    mExamData = response.body();
                    init();
                }

                @Override
                public void onFailure(Call<SelectedCourseIDBean> call, Throwable t) {
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
        this.loading = false;
        mWaitPopupWindow.dismissWait();
        if (mExamData.data == null) {

            return;
        }
        tvTerm.setText("当前学期:" + mExamData.data.term);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        if (mExamData.data.number != null) {
            //有两个data，第一个data内包含学期和其他数据，第二个data包含全部信息（json就这么写的）
            for (int item = 0; item < mExamData.data.number.size(); item++) {
                //获取每个补考安排
                SelectedCourseIDBean.ChildSelectedCourseIDBean.GrandChildSelectedCourseIDBean Details = mExamData.data.number.get(item);
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


    private void initView() {
        tvQueryTitle = (TextView) findViewById(R.id.tv_title);
        tvTerm = (TextView) findViewById(R.id.tv_term);
        lvExam = (ListView) findViewById(R.id.lv_searchListView);
        tvQueryTitle.setText("选修小班信息");
    }


}

