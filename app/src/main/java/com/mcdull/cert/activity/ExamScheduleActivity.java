package com.mcdull.cert.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.mcdull.cert.ActivityMode.MyTitleActivity;
import com.mcdull.cert.R;
import com.mcdull.cert.adapter.SelectAdapter;
import com.mcdull.cert.anim.ShakeAnim;
import com.mcdull.cert.domain.CollegeIdName;
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.InternetUtil;
import com.mcdull.cert.utils.Util;












/*
已废弃
 */






















public class ExamScheduleActivity extends MyTitleActivity implements OnClickListener {
    private CollegeIdName colleges[] = { new CollegeIdName("01", "土木建筑学院"),
            new CollegeIdName("02", "电气与电子工程学院"),
            new CollegeIdName("03", "机电工程学院"),
            new CollegeIdName("04", "经济管理学院"), new CollegeIdName("05", "体育学院"),
            new CollegeIdName("06", "信息工程学院"),
            new CollegeIdName("07", "人文社会科学学院"),
            new CollegeIdName("08", "基础科学学院"),
            new CollegeIdName("09", "外国语学院"), new CollegeIdName("10", "学工处"),
            new CollegeIdName("11", "艺术学院"), new CollegeIdName("12", "国际学院"),
            new CollegeIdName("13", "轨道交通学院"),
            new CollegeIdName("14", "马克思主义学院"),
            new CollegeIdName("21", "软件学院"), new CollegeIdName("31", "理工学院"),
            new CollegeIdName("40", "现代教育技术中心"),
            new CollegeIdName("51", "职业技术学院"), new CollegeIdName("61", "国防生学院") };
    private TextView tvQueryTitle;
    private RelativeLayout btSure;
    private TextView tvSelectYear;
    private TextView tvSelectCollege;
    private TextView tvSelectClass;
    private AlertDialog alertDialog;
    private int[] years;
    private String year;
    private String collegeId;
    private ShowWaitPopupWindow showWaitPopupWindow;
    private List<Map<String, String>> classList;
    private String classId;

    Handler classHandler = new Handler() {

        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                showWaitPopupWindow.dismissWait();
                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");
                classList = classJsonParse(json);
            } else {
                showWaitPopupWindow.dismissWait();
                Toast.makeText(ExamScheduleActivity.this, "获取班级列表失败", Toast.LENGTH_SHORT).show();
            }
        };
    };

    Handler examHandle = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                showWaitPopupWindow.dismissWait();
                Toast.makeText(ExamScheduleActivity.this, "查询失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                ShakeAnim skAnim = new ShakeAnim();
                skAnim.setDuration(1000);
                btSure.startAnimation(skAnim);
            }else {
                showWaitPopupWindow.dismissWait();

                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");

                String isExam = null;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    isExam = jsonObject.getString("class_name");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (isExam==null||isExam.equals("false")) {
                    Toast.makeText(ExamScheduleActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                    ShakeAnim skAnim = new ShakeAnim();
                    skAnim.setDuration(1000);
                    btSure.startAnimation(skAnim);
                    return;
                }

                Intent intent = new Intent(ExamScheduleActivity.this, ExamActivity.class);
                intent.putExtra("examJson",json);
                startActivity(intent);
                finish();
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exam_schedule);
        super.onCreate(savedInstanceState);

        initView();

        init();

    }

    protected List<Map<String, String>> classJsonParse(String classJson2) {
        List<Map<String, String>> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(classJson2);
            for (int i = 0; i < jsonArray.length(); i++) {
                Map<String, String> itemMap = new ArrayMap<>();
                JSONObject item = jsonArray.getJSONObject(i);
                itemMap.put("class_id", item.getString("class_id"));
                itemMap.put("class_name", item.getString("class_name"));
                list.add(itemMap);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    private void init() {
        int year = Util.getSystemYear();
        years = new int[] { year - 4, year - 3, year - 2, year - 1, year };
        tvQueryTitle.setText("考试安排");
        btSure.setOnClickListener(this);
        tvSelectYear.setOnClickListener(this);
        tvSelectCollege.setOnClickListener(this);
        tvSelectClass.setOnClickListener(this);
    }

    private void initView() {
        tvQueryTitle = (TextView) findViewById(R.id.tv_title);
        btSure = (RelativeLayout) findViewById(R.id.bt_sure);
        tvSelectYear = (TextView) findViewById(R.id.tv_select_year);
        tvSelectCollege = (TextView) findViewById(R.id.tv_select_college);
        tvSelectClass = (TextView) findViewById(R.id.tv_select_class);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sure:
                enterExam();
                break;
            case R.id.tv_select_year:
                showSelectYearDialog();
                break;
            case R.id.tv_select_college:
                showSelectCollegeDialog();
                break;
            case R.id.tv_select_class:
                if (classList != null) {
                    showSelectClassDialog();
                } else {
                    Toast.makeText(this, "未获取到班级列表", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void enterExam() {
        if (TextUtils.isEmpty(collegeId)) {
            Toast.makeText(this, "请选择学院", Toast.LENGTH_SHORT).show();
            ShakeAnim skAnim = new ShakeAnim();
            skAnim.setDuration(1000);
            btSure.startAnimation(skAnim);
            return;
        }
        if (TextUtils.isEmpty(year)) {
            Toast.makeText(this, "请选年级", Toast.LENGTH_SHORT).show();
            ShakeAnim skAnim = new ShakeAnim();
            skAnim.setDuration(1000);
            btSure.startAnimation(skAnim);
            return;
        }
        if (TextUtils.isEmpty(classId)) {
            Toast.makeText(this, "请选择班级", Toast.LENGTH_SHORT).show();
            ShakeAnim skAnim = new ShakeAnim();
            skAnim.setDuration(1000);
            btSure.startAnimation(skAnim);
            return;
        }

        showWaitPopupWindow.showWait();

        Map<String, String> map = new ArrayMap<>();
        map.put("class_id", classId);//设置get参数
        String url = "http://api.ecjtu.org/func/jwc/exam_arrange";//设置url
        new InternetUtil(examHandle, url, map);//传入参数
    }

    private void showSelectYearDialog() {
        List<String> select = new ArrayList<>();
        for (int year1 : years) {
            select.add(year1 + "");
        }

        AlertDialog.Builder builder = new Builder(ExamScheduleActivity.this);
        View view = View.inflate(ExamScheduleActivity.this,
                R.layout.select_dialog, null);
        ListView lvSelect = (ListView) view.findViewById(R.id.lv_select);
        SelectAdapter selectAdapter = new SelectAdapter(this, select);
        lvSelect.setAdapter(selectAdapter);

        lvSelect.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                year = years[position] + "";
                tvSelectYear.setText(years[position] + "");
                alertDialog.dismiss();
                if (!TextUtils.isEmpty(collegeId)) {
                    showWaitPopupWindow = new ShowWaitPopupWindow(
                            ExamScheduleActivity.this);
                    showWaitPopupWindow.showWait();

                    Map<String, String> map = new ArrayMap<>();
                    map.put("college_id", collegeId);//设置get参数
                    map.put("grade", year);//设置get参数
                    String url = "http://api.ecjtu.org/func/jwc/classes";//设置url
                    new InternetUtil(classHandler, url, map);//传入参数
                }
            }
        });

        alertDialog = builder.create();
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.show();

        select = null;

    }

    private void showSelectCollegeDialog() {
        List<String> select = new ArrayList<>();
        for (CollegeIdName college : colleges) {
            select.add(college.getCollege_name());
        }

        AlertDialog.Builder builder = new Builder(ExamScheduleActivity.this);
        View view = View.inflate(ExamScheduleActivity.this,
                R.layout.select_dialog, null);

        ListView lvSelect = (ListView) view.findViewById(R.id.lv_select);
        SelectAdapter selectAdapter = new SelectAdapter(this, select);
        lvSelect.setAdapter(selectAdapter);

        lvSelect.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                collegeId = colleges[position].getCollege_id();
                tvSelectCollege.setText(colleges[position].getCollege_name());
                alertDialog.dismiss();
                if (!TextUtils.isEmpty(year)) {
                    showWaitPopupWindow = new ShowWaitPopupWindow(
                            ExamScheduleActivity.this);
                    showWaitPopupWindow.showWait();

                    Map<String, String> map = new ArrayMap<>();
                    map.put("college_id", collegeId);//设置get参数
                    map.put("grade", year);//设置get参数
                    String url = "http://api.ecjtu.org/func/jwc/classes";//设置url
                    new InternetUtil(classHandler, url, map);//传入参数
                }
            }

        });

        alertDialog = builder.create();
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.show();

        select = null;

    }

    private void showSelectClassDialog() {
        List<String> select = new ArrayList<>();
        for (int i = 0; i < classList.size(); i++) {
            select.add(classList.get(i).get("class_name"));
        }

        AlertDialog.Builder builder = new Builder(ExamScheduleActivity.this);
        View view = View.inflate(ExamScheduleActivity.this,
                R.layout.select_dialog, null);

        ListView lvSelect = (ListView) view.findViewById(R.id.lv_select);
        SelectAdapter selectAdapter = new SelectAdapter(this, select);
        lvSelect.setAdapter(selectAdapter);

        lvSelect.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                tvSelectClass
                        .setText(classList.get(position).get("class_name"));
                classId = classList.get(position).get("class_id");
                alertDialog.dismiss();
            }

        });

        alertDialog = builder.create();
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.show();

        select = null;
    }

}
