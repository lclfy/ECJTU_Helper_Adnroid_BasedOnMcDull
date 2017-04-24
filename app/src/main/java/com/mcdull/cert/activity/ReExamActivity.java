package com.mcdull.cert.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.ImageView;
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
import com.mingle.widget.LoadingView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReExamActivity extends BaseThemeActivity {

    private TextView mTvTerm;
    private TextView mTvQueryTitle;
    private ListView mLvReExam;
    private ReExamBean mReExamData;
    private boolean loading = false;
    private LoadingView mLoadingView;
    private boolean isError = false;

    @Override
    protected void onTheme(Bundle savedInstanceState) {
        setContentView(R.layout.activity_re_exam);

        initView();

        this.mLoadingView = (LoadingView) findViewById(R.id.mLoadingView);

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
                    mLoadingView.setVisibility(View.GONE);
                    ImageView errorImg = (ImageView)findViewById(R.id.img_nodata);
                    errorImg.setBackgroundResource(R.drawable.pic_error);
                    errorImg.setVisibility(View.VISIBLE);
                    isError = true;
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
            mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        loading = false;
        mLoadingView.setVisibility(View.GONE);
        if (mReExamData.data == null) {
            findViewById(R.id.img_nodata).setVisibility(View.VISIBLE);
            return;
        }
//        String reExamJson = getIntent().getStringExtra("reExamJson");
//        ReExamBean mReExamData = new Gson().fromJson(reExamJson, ReExamBean.class);
        mTvTerm.setText("当前学期:" + mReExamData.data.term);
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
            mLvReExam.setAdapter(reExamAdapter);
        }
    }


    private void initView() {
        mTvQueryTitle = (TextView) findViewById(R.id.tv_title);
        mTvTerm = (TextView) findViewById(R.id.tv_term);
        mLvReExam = (ListView) findViewById(R.id.lv_re_exam);
        mTvQueryTitle.setText("补考安排");
        findViewById(R.id.img_nodata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isError){
                    reLoadData();
                    isError = false;
                }
            }
        });
    }

    private void reLoadData(){
        ImageView mIvNodata = (ImageView)findViewById(R.id.img_nodata);
        mIvNodata.setBackgroundResource(R.drawable.pic_nodata);
        mIvNodata.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
        onResume();

    }


}
