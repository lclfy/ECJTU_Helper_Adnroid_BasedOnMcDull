package com.mcdull.cert.activity;


import java.util.ArrayList;
import java.util.List;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.google.gson.Gson;
import com.mcdull.cert.HJXYT;
import com.mcdull.cert.bean.CalledPersonBean;
import com.mcdull.cert.bean.ClassmatesListBean;
import com.mcdull.cert.R;
import com.mcdull.cert.activity.base.BaseActivity;
import com.mcdull.cert.adapter.CallStudentInClassAdapter;
import com.mcdull.cert.utils.ShowWaitPopupWindow;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallStudentInClassActivity extends BaseActivity {

    private TextView tv_shouldCome;
    private TextView tv_actuallyCome;
    private ListView lvCallPeople;
    private TextView tvQueryTitle;
    //返回按钮计数，点两次才能返回，避免点名数据丢失
    private int countBackBtn = 0;
    //实到人数计数器
    private int comingStudentsCount = 0;
    //列表数据
    final List<CalledPersonBean> list = new ArrayList<>();
    //“完成点名”计数器，用于更改完成点名按钮上的字
    private boolean isDone = false;
    private ListView lv;
    private ShowWaitPopupWindow mWaitPopupWindow;
    private ClassmatesListBean cmlData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_callstudentinclass);
        super.onCreate(savedInstanceState);
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1);
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

        //提示点名方法
        Toast.makeText(CallStudentInClassActivity.this, "点击签到\n再次点击则继续记为未到", Toast.LENGTH_SHORT).show();

        initView();

        mWaitPopupWindow = new ShowWaitPopupWindow(this);

        //完成按钮
        findViewById(R.id.bt_finishCalling).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishCalling();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cmlData == null) {
            AVUser avUser = AVUser.getCurrentUser();
            HJXYT.getInstance().getAppClient().getJWXTService().getClassmatesListBean(avUser.getString("StudentId"), avUser.getString("JwcPwd")).enqueue(new Callback<ClassmatesListBean>() {
                @Override
                public void onResponse(Call<ClassmatesListBean> call, Response<ClassmatesListBean> response) {
                    cmlData = response.body();
                    init();
                }

                @Override
                public void onFailure(Call<ClassmatesListBean> call, Throwable t) {
                }
            });
        } else {
            init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWaitPopupWindow != null)
            mWaitPopupWindow.dismissWait();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (cmlData == null)
            mWaitPopupWindow.showWait();
    }

    private void init() {
        //进入页面时候传入Json，此处调用
        mWaitPopupWindow.dismissWait();
        if (cmlData != null) {
            //总人数
            tv_shouldCome.setText(String.valueOf(cmlData.data.size()) + "人");

            if (cmlData.data != null) {
                for (int item = 0; item < cmlData.data.size(); item++) {
                    //把arraylist填充成list,并且完成对CalledPersonBean（点名标记类）数据的填写
                    ClassmatesListBean.ChildClassmatesBean student = cmlData.data.get(item);
                    //创建点名标记对象，将姓名，班级编号传入。
                    CalledPersonBean called = new CalledPersonBean();
                    called.sn = student.sn;
                    called.xm = student.xm;
                    list.add(called);
                }
            }

            //设置点击事件

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView adapter, View view, int position, long id) {
                    if (isDone) {
                        //完成点名状态下点击是无效的
                        return;
                    } else {
                        TextView changeStatus = (TextView) view.findViewById(R.id.tv_Right);//该句可以得到LISTVIEW中的相应数据
                        if (list.get(position).hasCome) {
                            //来了
                            changeStatus.setText("未到");//改句可以修改相应Item数据的内容（注意:一刷新就没有了）
                            changeStatus.setTextColor(getResources().getColor(R.color.zhuhong));
                            //刷新页面上的实到人数
                            tv_actuallyCome.setText(String.valueOf(--comingStudentsCount) + "人");
                            list.get(position).ChangeComeStatus(list.get(position).hasCome);

                            CallStudentInClassAdapter sAdapter = (CallStudentInClassAdapter) lv.getAdapter();
                            sAdapter.notifyDataSetChanged(list);
                        } else {
                            changeStatus.setText("√已签到");
                            //字体换色
                            changeStatus.setTextColor(getResources().getColor(R.color.caolv));
                            //刷新页面上的实到人数
                            tv_actuallyCome.setText(String.valueOf(++comingStudentsCount) + "人");
                            list.get(position).ChangeComeStatus(list.get(position).hasCome);
                        }
                    }


                }
            });

            //填充listview
            CallStudentInClassAdapter adapter = new CallStudentInClassAdapter(this, list, false);
            lvCallPeople.setAdapter(adapter);
        } else {
            tv_shouldCome.setText("——" + "人");
            tv_actuallyCome.setText("——" + "人");
            Toast.makeText(CallStudentInClassActivity.this, "抱歉，暂无法获取班级信息", Toast.LENGTH_SHORT).show();
        }


    }

    private void initView() {
        tv_shouldCome = (TextView) findViewById(R.id.tv_shouldCome);
        //实到人数,初始状态为0
        tv_actuallyCome = (TextView) findViewById(R.id.tv_actuallyCome);
        tv_actuallyCome.setText("0人");
        lvCallPeople = (ListView) findViewById(R.id.lv_callPeople);
        //点名标记类
        lv = (ListView) findViewById(R.id.lv_callPeople);
    }


    //停止点名，恢复点名
    private void finishCalling() {
        //更改界面上的字
        Button doneBt = (Button) findViewById(R.id.bt_finishCalling);
        this.isDone = !this.isDone;
        if (this.isDone) {
            doneBt.setText("恢复点名");
            //临时存放未到人员的list
            final List<CalledPersonBean> listNotCome = new ArrayList<>();
            for (int item = 0; item < list.size(); item++) {
                //把没到的人创建成列表传进去
                if (!list.get(item).hasCome) {
                    listNotCome.add(list.get(item));
                }
            }
            final ListView lv = (ListView) findViewById(R.id.lv_callPeople);
            CallStudentInClassAdapter sAdapter = new CallStudentInClassAdapter(this, listNotCome, true);
            lv.setAdapter(sAdapter);

        } else {
            doneBt.setText("完成点名");
            final ListView lv = (ListView) findViewById(R.id.lv_callPeople);
            CallStudentInClassAdapter sAdapter = new CallStudentInClassAdapter(this, list, false);
            lv.setAdapter(sAdapter);
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //设置按键与关闭应用的关系
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (countBackBtn == 0) {
                countBackBtn = countBackBtn + 1;
                Toast.makeText(CallStudentInClassActivity.this, "再次点击返回键将返回主界面\n点名数据将丢失", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                finish();
            }

        }
        return true;
    }


}
