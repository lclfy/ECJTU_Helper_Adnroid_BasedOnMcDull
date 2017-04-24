package com.mcdull.cert.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.mcdull.cert.bean.CalenderBean;
import com.mcdull.cert.bean.ECardBean;
import com.mcdull.cert.bean.ECardOwnerBean;
import com.mcdull.cert.HJXYT;
import com.mcdull.cert.R;
import com.mcdull.cert.activity.CallStudentInClassActivity;
import com.mcdull.cert.activity.CetSearchActivity;
import com.mcdull.cert.activity.CourseActivity;
import com.mcdull.cert.activity.ECardActivity;
import com.mcdull.cert.activity.ExamActivity;
import com.mcdull.cert.activity.HomeActivity;
import com.mcdull.cert.activity.MapActivity;
import com.mcdull.cert.activity.ReExamActivity;
import com.mcdull.cert.activity.RepairSucActivity;
import com.mcdull.cert.activity.ScoreActivity;
import com.mcdull.cert.activity.SelectedCourseIDActivity;
import com.mcdull.cert.activity.TripActivity;
import com.mcdull.cert.activity.WeatherActivity;
import com.mcdull.cert.adapter.CalenderAdapter;
import com.mcdull.cert.utils.IconHelper;
import com.mcdull.cert.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mcdull on 15/8/10.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private View view;
    //课表刷新按钮
    private ImageView mIvTX;
    //饭卡的两个TextView
    private TextView mTvECardConsume;
    private TextView mTvECardBalance;
    private TextView mTvECardBalanceTitle;
    private TextView mTvECardConsumeTitle;
    //日历的TextView
    private TextView mTvCalenderTitle;
    //日历的listview
    private ListView mLvCalender;
    private TextView mTvAllCourse;

    private com.mcdull.cert.bean.ECardBean eCardBean;
    private com.mcdull.cert.bean.ECardOwnerBean eCardOwnerBean;
    public CalenderBean calenderBean;

    private boolean refresh = false;
    private AVUser user;
    private boolean isNextDay = false;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int count = 0;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.activity_query, container, false);

        initView();

        mIvTX.setImageBitmap(Util.toRoundBitmap(Util.drawableToBitmap(Util.resourceToDrawable(R.drawable.ic_account_circle_color2_48dp, getActivity()))));

//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.add(R.id.content_framelayout, new NewStudentFragment());
//        fragmentTransaction.commit();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //初始化界面数据
        if (AVUser.getCurrentUser() != null)
            initData();
        SharedPreferences SP = getActivity().getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor edit = SP.edit();
        boolean eCardPwd_Changed = SP.getBoolean("eCardPwdChanged",false);
        if (eCardPwd_Changed){
            refresh = true;
            initECardData();
            edit.putBoolean("eCardPwdChanged",false);
            edit.commit();
        }
        if (SP.getBoolean("Icon", true)) {
            IconHelper.getIcon(getActivity(), new IconHelper.GetIconCallBack() {
                @Override
                public void done(Bitmap bitmap) {
                    if (bitmap != null) {
                        mIvTX.setImageBitmap(Util.toRoundBitmap(bitmap));
                    }
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initData() {
        this.user = AVUser.getCurrentUser();
        initECardData();
        initCalenderData();
    }

    private void initView() {
        view.findViewById(R.id.bt_me).setOnClickListener(this);
        mIvTX = (ImageView) view.findViewById(R.id.iv_tx);
        mTvECardConsume = (TextView) view.findViewById(R.id.tv_eCardConsume);
        mTvECardBalance = (TextView) view.findViewById(R.id.tv_eCardBalance);
        mTvECardConsumeTitle = (TextView) view.findViewById(R.id.tv_consumeTitle);
        mTvECardBalanceTitle= (TextView) view.findViewById(R.id.tv_balanceTitle);
        mTvCalenderTitle = (TextView) view.findViewById(R.id.calenderArea_title);
        mLvCalender = (ListView) view.findViewById(R.id.lv_calenderListView);
        mTvAllCourse = (TextView) view.findViewById(R.id.tv_allCourseBtn);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);

        view.findViewById(R.id.bt_EcardBalance).setOnClickListener(this);
        view.findViewById(R.id.bt_ecard).setOnClickListener(this);
        view.findViewById(R.id.bt_checkEcardPaylist).setOnClickListener(this);
        view.findViewById(R.id.bt_map).setOnClickListener(this);
        view.findViewById(R.id.bt_examScore).setOnClickListener(this);
        view.findViewById(R.id.bt_reExam).setOnClickListener(this);
        view.findViewById(R.id.bt_examTime).setOnClickListener(this);
        view.findViewById(R.id.bt_pcRepair).setOnClickListener(this);
        view.findViewById(R.id.bt_cetSearch).setOnClickListener(this);
        view.findViewById(R.id.bt_backgroundRepair).setOnClickListener(this);
        view.findViewById(R.id.bt_callInClass).setOnClickListener(this);
        view.findViewById(R.id.bt_selectedcourse_id).setOnClickListener(this);
        view.findViewById(R.id.tv_reTryBtn).setOnClickListener(this);
        view.findViewById(R.id.tv_allCourseBtn).setOnClickListener(this);
        view.findViewById(R.id.bt_weather).setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = true;
                initData();
            }
        });
    }

    protected void nextActivity(Class cls, Bundle bundle, ActivityOptions options) {
        Intent intent = new Intent(getActivity(), cls);
        if (bundle != null)
            intent.putExtra("bundle", bundle);
        if (options != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            startActivity(intent, options.toBundle());
        else
            startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_me:
                openLeftWin();
                break;
            case R.id.bt_map:
                nextActivity(MapActivity.class, null, null);
                break;
            case R.id.bt_reExam:
                nextActivity(ReExamActivity.class, null, null);
                break;
            case R.id.bt_examTime:
                nextActivity(ExamActivity.class, null, null);
                break;
            case R.id.bt_examScore:
                nextActivity(ScoreActivity.class, null, null);
                break;
            case R.id.bt_selectedcourse_id:
                nextActivity(SelectedCourseIDActivity.class, null, null);
                break;
            case R.id.bt_pcRepair:
                nextActivity(RepairSucActivity.class, null, null);
                break;
            case R.id.bt_cetSearch:
                nextActivity(CetSearchActivity.class, null, null);
                break;
            case R.id.bt_callInClass:
                //新做的点名器
                nextActivity(CallStudentInClassActivity.class, null, null);
                break;
            case R.id.bt_weather:
                nextActivity(WeatherActivity.class, null, null);
                break;
            case R.id.bt_ecard:
            case R.id.bt_EcardBalance:
            case R.id.bt_checkEcardPaylist:
                Bundle bundle = new Bundle();
                bundle.putSerializable("eCardBean", eCardBean);
                bundle.putSerializable("eCardOwnerBean", eCardOwnerBean);
                ActivityOptions options = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                            Pair.create((View)mTvECardBalanceTitle,"balanceTitle"),
                            Pair.create((View)mTvECardConsumeTitle,"consumeTitle"),
                            Pair.create((View) mTvECardBalance, "balance"),
                            Pair.create((View) mTvECardConsume, "consume"));
                }
                nextActivity(ECardActivity.class, bundle, options);
                break;
            case R.id.tv_allCourseBtn:
                nextActivity(CourseActivity.class, null, null);
                break;
            case R.id.bt_backgroundRepair:
                intent = new Intent(getActivity(), TripActivity.class);
                intent.putExtra("Title", "花椒维权");
                startActivity(intent);
                break;
        }
    }

    private void openLeftWin() {
        ((HomeActivity) getActivity()).openMenu();
    }

    private void initECardData() {
        getECardBean();
        getECardOwnerBean();
    }

    public void getECardBean() {
        if (eCardBean == null || refresh) {
            final String studentId = user.getString("StudentId");
            final String eCardPassword = user.getString("EcardPwd");
            Log.d("TAG", "start");
            Call<ECardBean> eCardBeanCall = HJXYT.getInstance().getAppClient().getJWXTService().getECard(studentId, eCardPassword);
            eCardBeanCall.enqueue(new Callback<ECardBean>() {
                @Override
                public void onResponse(Call<ECardBean> call, Response<ECardBean> response) {
                    eCardBean = response.body();
                    refreshECardConsume();
                    refreshEnd();
                }

                @Override
                public void onFailure(Call<ECardBean> call, Throwable t) {
                    refreshEnd();
                }
            });
        }
    }

    public void getECardOwnerBean() {
        if (eCardOwnerBean == null || refresh) {
            if (refresh){
                refresh = false;
            }
            final String studentId = user.getString("StudentId");
            final String eCardPassword = user.getString("EcardPwd");
            Call<ECardOwnerBean> eCardOwnerBeanCall = HJXYT.getInstance().getAppClient().getJWXTService().getECardOwn(studentId, eCardPassword);
            eCardOwnerBeanCall.enqueue(new Callback<com.mcdull.cert.bean.ECardOwnerBean>() {
                @Override
                public void onResponse(Call<ECardOwnerBean> call, Response<ECardOwnerBean> response) {
                    eCardOwnerBean = response.body();
                    refreshECardBalance();
                    refreshEnd();
                }

                @Override
                public void onFailure(Call<ECardOwnerBean> call, Throwable t) {
                    refreshEnd();
                }
            });
        }
    }

    private void initCalenderData() {
        SimpleDateFormat format = new SimpleDateFormat("HH", Locale.getDefault());
        int hour = Integer.parseInt(format.format(new Date()));
        this.isNextDay = hour >= 20;
        if (!isNextDay && calenderBean != null && !refresh)
            return;
        final String studentId = user.getString("StudentId");
        final String jwcPwd = user.getString("JwcPwd");
        Call<CalenderBean> calenderBeanCall = HJXYT.getInstance().getAppClient().getJWXTService().getCalenderBean(studentId, jwcPwd);
        calenderBeanCall.enqueue(new Callback<CalenderBean>() {

            @Override
            public void onResponse(Call<CalenderBean> call, Response<CalenderBean> response) {
                calenderBean = response.body();
                refreshCalenderView();
                refreshEnd();
            }

            @Override
            public void onFailure(Call<CalenderBean> call, Throwable t) {
                refreshEnd();
            }
        });
    }

    private void refreshECardConsume() {
        if (eCardBean == null)
            return;
            float dayConsume = 0;
            if ("success".equals(eCardBean.msg)) {
                for (int item = 0; item < eCardBean.data.size(); item++) {
                    //获取当日每次消费金额以获取总消费
                    ECardBean.ChildECardBean consumeLog = eCardBean.data.get(item);
                    //去除充值金额后的消费金额的绝对值累计
                    if (Float.parseFloat(consumeLog.consume) < 0) {
                        dayConsume += Math.abs(Float.parseFloat(consumeLog.consume));
                    }
                }
                String sum = dayConsume + "元";
                mTvECardConsume.setText(sum);
            } else {
                mTvECardConsume.setText("——");
                Toast.makeText(getActivity(), "一卡通查询："+eCardBean.msg, Toast.LENGTH_SHORT).show();
            }
    }

    private void refreshECardBalance() {
        if (eCardOwnerBean == null)
            return;
            if (eCardOwnerBean.data != null) {
                try {
                    String[] cutData = eCardOwnerBean.data.balance.split("（");
                    mTvECardBalance.setText(cutData[0]);
                } catch (Exception e) {
                    mTvECardBalance.setText("——");
                }
            } else {
                mTvECardBalance.setText("——");
            }
    }

    private void refreshCalenderView() {
        if (calenderBean == null)
            return;
        if ("success".equals(calenderBean.msg)) {
            String date = calenderBean.data.date;
            String week = calenderBean.data.week;
            String day = "今";
            if (isNextDay){
                day = "明";
            }
            String calenderTitle = day + "天是"+date + " 第" + week + "周";
            mTvCalenderTitle.setText(calenderTitle);

            List<Map<String, String>> list = new ArrayList<>();
            for (CalenderBean.ChildCalenderBean.GrandChildCalenderBean bean : calenderBean.data.daylist) {
                Map<String, String> calenderMap = new HashMap<>();
                calenderMap.put("course", bean.course);
                calenderMap.put("classRoom", bean.classRoom);
                calenderMap.put("classString", bean.classString);
                list.add(calenderMap);
            }
            CalenderAdapter calenderAdapter = new CalenderAdapter(getActivity(), list);
            mLvCalender.setAdapter(calenderAdapter);
            mLvCalender.setDividerHeight(0);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (Util.dip2px(56) + 1) * list.size());
            mLvCalender.setLayoutParams(layoutParams);
            mTvAllCourse.setVisibility(View.VISIBLE);
        } else {

        }
    }

    private void refreshEnd() {
        if (count++ == 3) {
            refresh = false;
            mSwipeRefreshLayout.setRefreshing(false);
            count = 0;
        }
    }
}
