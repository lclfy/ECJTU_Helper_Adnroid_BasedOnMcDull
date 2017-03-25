package com.mcdull.cert.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.mcdull.cert.ActivityMode.MyTitleActivity;
import com.mcdull.cert.R;
import com.mcdull.cert.adapter.SelectAdapter;
import com.mcdull.cert.anim.ShakeAnim;
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class JoinCERTActivity extends MyTitleActivity implements View.OnClickListener {

    private EditText mEtName;
    private EditText mEtPhone;
    private TextView mTvsector;
    private TextView mTvYear;
    private TextView mTvText;
    private AlertDialog alertDialog;
    private Button btSure;
    private ShowWaitPopupWindow waitWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_join_cert);
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("加入我们");
        mEtName = (EditText) findViewById(R.id.et_name);
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        btSure = (Button) findViewById(R.id.bt_sure);
        findViewById(R.id.bt_sure).setOnClickListener(this);
        mTvYear = (TextView) findViewById(R.id.tv_year);
        mTvYear.setOnClickListener(this);
        mTvsector = (TextView) findViewById(R.id.tv_sector);
        mTvsector.setOnClickListener(this);
        mTvText = (TextView) findViewById(R.id.tv_text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sure:
                join();
                break;
            case R.id.tv_year:
                showSelectYearDialog();
                break;
            case R.id.tv_sector:
                showSelectSectorDialog();
                break;
        }
    }

    private void join() {
        String name = mEtName.getText().toString();
        String phone = mEtPhone.getText().toString();
        String year = mTvYear.getText().toString();
        String sector = mTvsector.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
            ShakeAnim skAnim = new ShakeAnim();
            skAnim.setDuration(1000);
            btSure.startAnimation(skAnim);
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入电话号码", Toast.LENGTH_SHORT).show();
            ShakeAnim skAnim = new ShakeAnim();
            skAnim.setDuration(1000);
            btSure.startAnimation(skAnim);
            return;
        }
        if (TextUtils.isEmpty(year)) {
            Toast.makeText(this, "请选择入学年份", Toast.LENGTH_SHORT).show();
            ShakeAnim skAnim = new ShakeAnim();
            skAnim.setDuration(1000);
            btSure.startAnimation(skAnim);
            return;
        }
        if (TextUtils.isEmpty(sector)) {
            Toast.makeText(this, "请选择想要加入的部门", Toast.LENGTH_SHORT).show();
            ShakeAnim skAnim = new ShakeAnim();
            skAnim.setDuration(1000);
            btSure.startAnimation(skAnim);
            return;
        }

        waitWin = new ShowWaitPopupWindow(this);
        waitWin.showWait();

        AVObject joinObject = new AVObject("Join");
        joinObject.put("name", name);
        joinObject.put("phone", phone);
        joinObject.put("year", year);
        joinObject.put("sector", sector);
        joinObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                waitWin.dismissWait();
                if (e == null) {
                    Toast.makeText(JoinCERTActivity.this, "报名成功，请耐心等候通知", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(JoinCERTActivity.this, "报名失败，请检查网络设置或线下报名", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void showSelectYearDialog() {
        int year = Util.getSystemYear();
        int[] years = new int[]{year - 4, year - 3, year - 2, year - 1, year};
        List<String> select = new ArrayList<>();
        for (int i : years) {
            select.add(i + "");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(JoinCERTActivity.this);
        View view = View.inflate(JoinCERTActivity.this, R.layout.select_dialog, null);
        ListView lvSelect = (ListView) view.findViewById(R.id.lv_select);
        SelectAdapter selectAdapter = new SelectAdapter(this, select);
        lvSelect.setAdapter(selectAdapter);

        lvSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String year = ((TextView) view.findViewById(R.id.tv_select_item)).getText().toString();
                mTvYear.setText(year);
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.show();
    }


    private void showSelectSectorDialog() {
        List<String> select = new ArrayList<>();
        select.add("技术研发");
        select.add("硬件维修");
        select.add("产品运营");
        select.add("创意策划");

        AlertDialog.Builder builder = new AlertDialog.Builder(JoinCERTActivity.this);
        View view = View.inflate(JoinCERTActivity.this, R.layout.select_dialog, null);
        ListView lvSelect = (ListView) view.findViewById(R.id.lv_select);
        SelectAdapter selectAdapter = new SelectAdapter(this, select);
        lvSelect.setAdapter(selectAdapter);

        lvSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sector = ((TextView) view.findViewById(R.id.tv_select_item)).getText().toString();
                mTvsector.setText(sector);
                alertDialog.dismiss();
                switch (sector) {
                    case "技术研发":
                        mTvText.setText("技术研发：\n" +
                                "研究C/C++,C#,JavaScript,PHP,Python,Java或其他现代程序开发语言\n" +
                                "对图像处理，数据挖掘，人工智能等进行学习研究\n" +
                                "负责响应组服务器运维及安全检测，配合学校处理各种安全事故；\n" +
                                "参与响应组各项活动，完成组织布置的任务\n" +
                                "定期举办组内技术分享讨论会。");
                        break;
                    case "硬件维修":
                        mTvText.setText("硬件研究：\n" +
                                "研究计算机原理以及硬件知识\n" +
                                "喜欢钻研数码产品，注重用户体验，喜欢分析各种参数\n" +
                                "向全校师生普及电脑知识\n" +
                                "能快速解决各类电脑常见问题\n" +
                                "更新组内FTP资源站资源");
                        break;
                    case "产品运营":
                        mTvText.setText("产品运营：\n" +
                                "手绘，鼠绘大触，平面设计师，PS玩家，能够绘制基本的插图和元素\n" +
                                "了解前沿的互联网资讯，对互联网有一定理解，关注行业动态\n" +
                                "把握产品方向，通过对用户调研，搜集用户反馈来发掘用户需求\n" +
                                "产品内容策划，后期版本迭代规划\n" +
                                "产品的对外运营推广，组内新媒体平台的运营");
                        break;
                    case "创意策划":
                        mTvText.setText("创意策划：\n" +
                                "组内人员管理\n" +
                                "组内所有活动资料的整理归档\n" +
                                "对外合作项目的运营推广\n" +
                                "组内财务管理");
                        break;
                }
            }
        });

        alertDialog = builder.create();
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (waitWin!=null){
            waitWin.dismissWait();
            waitWin = null;
        }
    }
}
