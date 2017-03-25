package com.mcdull.cert.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.RetryPolicy;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.google.gson.Gson;
import com.mcdull.cert.Bean.CourseBean;
import com.mcdull.cert.R;
import com.mcdull.cert.activity.ImportCourseActivity;
import com.mcdull.cert.activity.ModifyActivity;
import com.mcdull.cert.adapter.*;
import com.mcdull.cert.utils.CourseUtil;
import com.mcdull.cert.utils.ShowSureDialog;
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.InternetUtil;
import com.mcdull.cert.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.widget.Toast;

public class CourseFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private SharedPreferences SP;
    private View view;
    private GridView mGvCourse;
    private CourseAdapter adapter;
    private ShowWaitPopupWindow waitWin;
    private View mBackGround;
    private ShowSureDialog sureDialog;
    private String basicURL = "http://api1.ecjtu.org/v1/";

    Handler CourseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Toast.makeText(getActivity(), "课表数据暂无法获取，请重试", Toast.LENGTH_SHORT).show();
            } else {

                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");
                System.out.print(json);
                try{
                    CourseBean CourseData = new Gson().fromJson(json, CourseBean.class);
                    adapter.setCourseList(CourseData);
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    try {
                        CourseBean CourseData = new Gson().fromJson(json, CourseBean.class);
                        adapter.setCourseList(CourseData);
                        adapter.notifyDataSetChanged();
                    }catch (Exception e1){
                        Toast.makeText(getActivity(), "课表数据暂无法获取，请重试", Toast.LENGTH_SHORT).show();
                    }
                }


            }

        }

    };

    @Override
    public void onResume() {
        super.onResume();
        final String studentId = AVUser.getCurrentUser().getString("StudentId");
        if (TextUtils.isEmpty(studentId)) {
            return;
        }
        Boolean isCourse = SP.getBoolean("isCourse", false);
        if (!isCourse && studentId.length() == 16) {
            //获取新生课表
            String password = AVUser.getCurrentUser().getString("JwcPwd");
            if (!TextUtils.isEmpty(password)) {
                Map<String, String> map = new ArrayMap<>();
                map.put("stuid", studentId);//设置get参数
                map.put("passwd", password);//设置get参数
                map.put("term", findCurrentTerm());//设置get参数
                new InternetUtil(CourseHandler,basicURL + "schedule", map,true,getActivity());//传入参数
            }
            return;

        }
//        List<List<String>> courseList = CourseUtil.getCourse(getActivity());
//        mBackGround.setVisibility(View.VISIBLE);

//        for (int i = 0; i < courseList.size(); i++)
//            if (!TextUtils.isEmpty(courseList.get(i).get(0)))
//                mBackGround.setVisibility(View.GONE);
//        adapter.setCourseList(courseList);
//        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_course, container, false);

        waitWin = new ShowWaitPopupWindow(getActivity());
        this.mGvCourse = (GridView) view.findViewById(R.id.gv_course);
        CourseBean courseList = new CourseBean();
        adapter = new CourseAdapter(getActivity(), courseList, mGvCourse);

//        mBackGround = view.findViewById(R.id.back);
//        mBackGround.setVisibility(View.VISIBLE);
//        mBackGround.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.setVisibility(View.GONE);
//            }
//        });

        mGvCourse.setAdapter(adapter);
        mGvCourse.setOnItemClickListener(this);
        mGvCourse.setOnItemLongClickListener(this);

        this.SP = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        Boolean isCourse = SP.getBoolean("isCourse", false);
        final String studentId = AVUser.getCurrentUser().getString("StudentId");

        if (TextUtils.isEmpty(studentId)) {
            return view;
        }
        int[] time = Util.getSystemTime();
        final String timeString = time[0] + "." + time[1];
        if (!timeString.equals(SP.getString("time", null)) || !isCourse) {
        if (studentId.length() == 16) {
                //获取课表
                String password = AVUser.getCurrentUser().getString("JwcPwd");
                if (!TextUtils.isEmpty(password)) {
                    Map<String, String> map = new ArrayMap<>();
                    map.put("stuid", studentId);//设置get参数
                    map.put("passwd", password);//设置get参数
                    map.put("term", findCurrentTerm());//设置get参数
                    new InternetUtil(CourseHandler,basicURL + "schedule", map,true,getActivity());//传入参数
                } else {

                    return view;
                }
            } else {
            Toast.makeText(getActivity(),"抱歉，暂不支持15级以前同学的课程表查询",Toast.LENGTH_SHORT);
                return view;
            }
        } else {
//            courseList = CourseUtil.getCourse(getActivity());
//            for (int i = 0; i < courseList.size(); i++)
//                if (!TextUtils.isEmpty(courseList.get(i).get(0)))
//                    mBackGround.setVisibility(View.GONE);
//            adapter.setCourseList(courseList);
//            adapter.notifyDataSetChanged();
            return view;
        }
        return view;
    }

    private String findCurrentTerm(){
        //拼字符串-找学期
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
        //把年月按-分开，然后8-12月是当年第一学期，1月是去年第一学期，2-7月是去年第二学期
        String[] nowTime = df.format(new Date()).split("-");// new Date()为获取当前系统时间
        String nowTerm = "";
        //当年第一学期
        if (Integer.parseInt(nowTime[1]) > 7 && Integer.parseInt(nowTime[1]) <= 12)
        {
            nowTerm = nowTime[0]+".1";
        }else if(Integer.parseInt(nowTime[1]) == 1){//去年第一学期
            nowTerm = String.valueOf(Integer.parseInt(nowTime[0])-1) + ".1";
        }else if(Integer.parseInt(nowTime[1]) > 1){//去年第二学期
            nowTerm = String.valueOf(Integer.parseInt(nowTime[0])-1) + ".2";
        }
        return nowTerm;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (waitWin != null) {
            waitWin.dismissWait();
            waitWin = null;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //修改课表
        if (position < 8 || position % 8 == 0) {
            return;
        }
        int i, j, l;
        int k = position + 1;
        if (k % 8 == 0) {
            l = ((k / 8) - 1) * 7;
        } else {
            l = ((k % 8) - 1) + (((k / 8) - 1) * 7);
        }
        if (l % 7 == 0) {
            i = 6;
            j = (l / 7) - 1;
        } else {
            i = (l % 7) - 1;
            j = l / 7;
        }
        Intent intent = new Intent();
        intent.setClass(getActivity(), ModifyActivity.class);
        intent.putExtra("type", new int[]{i, j});
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //删除课表
        if (position < 8 || position % 8 == 0) {
            return false;
        }
        final int i, j, l;
        int k = position + 1;
        if (k % 8 == 0) {
            l = ((k / 8) - 1) * 7;
        } else {
            l = ((k % 8) - 1) + (((k / 8) - 1) * 7);
        }
        if (l % 7 == 0) {
            i = 6;
            j = (l / 7) - 1;
        } else {
            i = (l % 7) - 1;
            j = l / 7;
        }

        List<String> list = CourseUtil.getCourse(i, j, getActivity());
        if (TextUtils.isEmpty(list.get(0)))
            return false;

        sureDialog = new ShowSureDialog(getActivity(), "是否确认删除？", new ShowSureDialog.CallBack() {
            @Override
            public void CallBack() {
//                CourseUtil.removeCourse(getActivity(), i, j);
//                adapter.setCourseList(CourseUtil.getCourse(getActivity()));
//                adapter.notifyDataSetChanged();
//                sureDialog.dismiss();
            }
        });
        sureDialog.showDialog();
        return true;
    }
}
