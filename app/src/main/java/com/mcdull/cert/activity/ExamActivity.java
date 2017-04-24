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
import com.mingle.widget.LoadingView;
import com.mingle.widget.ShapeLoadingView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExamActivity extends BaseThemeActivity {

    private TextView mTvTerm;
    private TextView mTvQueryTitle;
    private ListView mLvExam;
    private ExamTimeBean mExamData;
    private boolean loading = false;
    private boolean isError = false;

    private LoadingView mLoadingView;

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
    protected void onTheme(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exam);

        initView();
        this.mLoadingView = (LoadingView) findViewById(R.id.mLoadingView);
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
        if (mExamData.data == null) {
            findViewById(R.id.img_nodata).setVisibility(View.VISIBLE);
            isError = false;
            return;
        }
        mTvTerm.setText("当前学期:" + mExamData.data.term);
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
                mLvExam.setAdapter(ExamAdapter);
            }

        }
    }


    private void initView() {
        mTvQueryTitle = (TextView) findViewById(R.id.tv_title);
        mTvTerm = (TextView) findViewById(R.id.tv_term);
        mLvExam = (ListView) findViewById(R.id.lv_searchListView);
        mTvQueryTitle.setText("考试安排");
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
