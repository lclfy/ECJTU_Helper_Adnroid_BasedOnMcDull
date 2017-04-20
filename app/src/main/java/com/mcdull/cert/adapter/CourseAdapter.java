package com.mcdull.cert.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.mcdull.cert.bean.OldCourseBean;
import com.mcdull.cert.R;

/**
 * Created by mcdull on 15/7/10.
 */

public class CourseAdapter extends BaseAdapter {

    private Activity activity;
    private OldCourseBean courseList;
    private String week[] = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private GridView gridView;
    TextView tv;



    public void setCourseList(OldCourseBean courseList) {
        this.courseList = courseList;
    }

    public CourseAdapter(Activity activity, OldCourseBean courseList, GridView gridView) {
        this.activity = activity;
        this.courseList = courseList;
        this.gridView = gridView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (courseList == null) {
            return 0;
        }
        return 48;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        //如果这时段没课，背景换颜色
        boolean isEmpty = false;
        view = View.inflate(activity, R.layout.item_week, null);
        if (courseList.data == null) {
            view.setVisibility(View.GONE);
            return view;
        }
        //x是0-15的集合
        int x = position % 15;
        if (position < 8) {
            //位置0-7，第一行显示周数
            view = View.inflate(activity, R.layout.item_week, null);
            tv = (TextView) view.findViewById(R.id.ItemText);
        } else {
            //第二行开始，显示课程
            view = View.inflate(activity, R.layout.item_course, null);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    //把表格的高度/4.7来得到每行高度
                    (int)(gridView.getHeight() / 4.7));
            view.setLayoutParams(param);
            tv = (TextView) view.findViewById(R.id.ItemText);
            //字体大小
            tv.setTextSize(9);
        }
        if (position < 8) {
            //设置周
            tv.setText(week[position]);
        } else if (position == 8) {
            //设置第x节课，以此类推
            tv.setText("\n一\n\n\n二\n");
        } else if (position == 16) {
            tv.setText("\n三\n\n\n四\n");
        } else if (position == 24) {
            tv.setText("\n五\n\n\n六\n");
        } else if (position == 32) {
            tv.setText("\n七\n\n\n八\n");
        } else if (position == 40) {
            tv.setText("\n九\n\n\n十\n");
        } else {
//            posion与课程的对比关系
//            9  10 11 12 13 14 15
//            17 18 19 20 21 22 23
//            25 26 27 28 29 30 31
//            33 34 35 36 37 38 39
//            41 42 43 44 45 46 47
//            k的排列为posion+1
//            i的排列
//            1  2  3  4  5  6  7
//            8  9  10 11 12 13 14
//            15 16 17 18 19 20 21
//            22 23 24 25 26 27 28
//            29 30 31 32 33 34 35
//            j的排列
//            1 6  11 16 21 26 31
//            2 7  12 17 22 27 32
//            3 8  13 18 23 28 33
//            4 9  14 19 24 29 34
//            5 10 15 20 25 30 35
            int i;
            int k = position + 1;
            if (k % 8 == 0) {
                //posion为每行最后一列时
                i = ((k % 8)) + (((k / 8) - 1) * 7);
            } else {
                i = ((k % 8) - 1) + (((k / 8) - 1) * 7);
            }
            int j;
            if (i % 7 == 0) {
                j = (6 * 5) + (i / 7);
            } else {
                j = (((i % 7) - 1) * 5) + ((i / 7) + 1);
            }
            //填课表
            switch (j){
                //根据位置拼字符串
                //周一
                case 1:
                    tv.setText(courseList.data.Mon.fir_sec.kcmc+"\n"
                            +courseList.data.Mon.fir_sec.js+"\n"
                            +courseList.data.Mon.fir_sec.skdd+"\n"
                            +courseList.data.Mon.fir_sec.skzc+"\n"
                            +courseList.data.Mon.fir_sec.sksj);
                    if (courseList.data.Mon.fir_sec.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 2:
                    tv.setText(courseList.data.Mon.thi_four.kcmc+"\n"
                            +courseList.data.Mon.thi_four.js+"\n"
                            +courseList.data.Mon.thi_four.skdd+"\n"
                            +courseList.data.Mon.thi_four.skzc+"\n"
                            +courseList.data.Mon.thi_four.sksj);
                    if (courseList.data.Mon.thi_four.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 3:
                    tv.setText(courseList.data.Mon.fif_six.kcmc+"\n"
                            +courseList.data.Mon.fif_six.js+"\n"
                            +courseList.data.Mon.fif_six.skdd+"\n"
                            +courseList.data.Mon.fif_six.skzc+"\n"
                            +courseList.data.Mon.fif_six.sksj);
                    if (courseList.data.Mon.fif_six.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 4:
                    tv.setText(courseList.data.Mon.sev_eight.kcmc+"\n"
                            +courseList.data.Mon.sev_eight.js+"\n"
                            +courseList.data.Mon.sev_eight.skdd+"\n"
                            +courseList.data.Mon.sev_eight.skzc+"\n"
                            +courseList.data.Mon.sev_eight.sksj);
                    if (courseList.data.Mon.sev_eight.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 5:
                    tv.setText(courseList.data.Mon.nin_ten.kcmc+"\n"
                            +courseList.data.Mon.nin_ten.js+"\n"
                            +courseList.data.Mon.nin_ten.skdd+"\n"
                            +courseList.data.Mon.nin_ten.skzc+"\n"
                            +courseList.data.Mon.nin_ten.sksj);
                    if (courseList.data.Mon.nin_ten.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                //周二
                case 6:
                    tv.setText(courseList.data.Tues.fir_sec.kcmc+"\n"
                            +courseList.data.Tues.fir_sec.js+"\n"
                            +courseList.data.Tues.fir_sec.skdd+"\n"
                            +courseList.data.Tues.fir_sec.skzc+"\n"
                            +courseList.data.Tues.fir_sec.sksj);
                    if (courseList.data.Tues.fir_sec.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 7:
                    tv.setText(courseList.data.Tues.thi_four.kcmc+"\n"
                            +courseList.data.Tues.thi_four.js+"\n"
                            +courseList.data.Tues.thi_four.skdd+"\n"
                            +courseList.data.Tues.thi_four.skzc+"\n"
                            +courseList.data.Tues.thi_four.sksj);
                    if (courseList.data.Tues.thi_four.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 8:
                    tv.setText(courseList.data.Tues.fif_six.kcmc+"\n"
                            +courseList.data.Tues.fif_six.js+"\n"
                            +courseList.data.Tues.fif_six.skdd+"\n"
                            +courseList.data.Tues.fif_six.skzc+"\n"
                            +courseList.data.Tues.fif_six.sksj);
                    if (courseList.data.Tues.fif_six.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 9:
                    tv.setText(courseList.data.Tues.sev_eight.kcmc+"\n"
                            +courseList.data.Tues.sev_eight.js+"\n"
                            +courseList.data.Tues.sev_eight.skdd+"\n"
                            +courseList.data.Tues.sev_eight.skzc+"\n"
                            +courseList.data.Tues.sev_eight.sksj);
                    if (courseList.data.Tues.sev_eight.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 10:
                    tv.setText(courseList.data.Tues.nin_ten.kcmc+"\n"
                            +courseList.data.Tues.nin_ten.js+"\n"
                            +courseList.data.Tues.nin_ten.skdd+"\n"
                            +courseList.data.Tues.nin_ten.skzc+"\n"
                            +courseList.data.Tues.nin_ten.sksj);
                    if (courseList.data.Tues.nin_ten.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                    //周三
                case 11:
                    tv.setText(courseList.data.Wed.fir_sec.kcmc+"\n"
                            +courseList.data.Wed.fir_sec.js+"\n"
                            +courseList.data.Wed.fir_sec.skdd+"\n"
                            +courseList.data.Wed.fir_sec.skzc+"\n"
                            +courseList.data.Wed.fir_sec.sksj);
                    if (courseList.data.Wed.fir_sec.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 12:
                    tv.setText(courseList.data.Wed.thi_four.kcmc+"\n"
                            +courseList.data.Wed.thi_four.js+"\n"
                            +courseList.data.Wed.thi_four.skdd+"\n"
                            +courseList.data.Wed.thi_four.skzc+"\n"
                            +courseList.data.Wed.thi_four.sksj);
                    if (courseList.data.Wed.thi_four.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 13:
                    tv.setText(courseList.data.Wed.fif_six.kcmc+"\n"
                            +courseList.data.Wed.fif_six.js+"\n"
                            +courseList.data.Wed.fif_six.skdd+"\n"
                            +courseList.data.Wed.fif_six.skzc+"\n"
                            +courseList.data.Wed.fif_six.sksj);
                    if (courseList.data.Wed.fif_six.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 14:
                    tv.setText(courseList.data.Wed.sev_eight.kcmc+"\n"
                            +courseList.data.Wed.sev_eight.js+"\n"
                            +courseList.data.Wed.sev_eight.skdd+"\n"
                            +courseList.data.Wed.sev_eight.skzc+"\n"
                            +courseList.data.Wed.sev_eight.sksj);
                    if (courseList.data.Wed.sev_eight.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 15:
                    tv.setText(courseList.data.Wed.nin_ten.kcmc+"\n"
                            +courseList.data.Wed.nin_ten.js+"\n"
                            +courseList.data.Wed.nin_ten.skdd+"\n"
                            +courseList.data.Wed.nin_ten.skzc+"\n"
                            +courseList.data.Wed.nin_ten.sksj);
                    if (courseList.data.Wed.nin_ten.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                    //周四
                case 16:
                    tv.setText(courseList.data.Thur.fir_sec.kcmc+"\n"
                            +courseList.data.Thur.fir_sec.js+"\n"
                            +courseList.data.Thur.fir_sec.skdd+"\n"
                            +courseList.data.Thur.fir_sec.skzc+"\n"
                            +courseList.data.Thur.fir_sec.sksj);
                    if (courseList.data.Thur.fir_sec.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 17:
                    tv.setText(courseList.data.Thur.thi_four.kcmc+"\n"
                            +courseList.data.Thur.thi_four.js+"\n"
                            +courseList.data.Thur.thi_four.skdd+"\n"
                            +courseList.data.Thur.thi_four.skzc+"\n"
                            +courseList.data.Thur.thi_four.sksj);
                    if (courseList.data.Thur.thi_four.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 18:
                    tv.setText(courseList.data.Thur.fif_six.kcmc+"\n"
                            +courseList.data.Thur.fif_six.js+"\n"
                            +courseList.data.Thur.fif_six.skdd+"\n"
                            +courseList.data.Thur.fif_six.skzc+"\n"
                            +courseList.data.Thur.fif_six.sksj);
                    if (courseList.data.Thur.fif_six.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 19:
                    tv.setText(courseList.data.Thur.sev_eight.kcmc+"\n"
                            +courseList.data.Thur.sev_eight.js+"\n"
                            +courseList.data.Thur.sev_eight.skdd+"\n"
                            +courseList.data.Thur.sev_eight.skzc+"\n"
                            +courseList.data.Thur.sev_eight.sksj);
                    if (courseList.data.Thur.sev_eight.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 20:
                    tv.setText(courseList.data.Thur.nin_ten.kcmc+"\n"
                            +courseList.data.Thur.nin_ten.js+"\n"
                            +courseList.data.Thur.nin_ten.skdd+"\n"
                            +courseList.data.Thur.nin_ten.skzc+"\n"
                            +courseList.data.Thur.nin_ten.sksj);
                    if (courseList.data.Thur.nin_ten.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                    //周五
                case 21:
                    tv.setText(courseList.data.Fri.fir_sec.kcmc+"\n"
                            +courseList.data.Fri.fir_sec.js+"\n"
                            +courseList.data.Fri.fir_sec.skdd+"\n"
                            +courseList.data.Fri.fir_sec.skzc+"\n"
                            +courseList.data.Fri.fir_sec.sksj);
                    if (courseList.data.Fri.fir_sec.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 22:
                    tv.setText(courseList.data.Fri.thi_four.kcmc+"\n"
                            +courseList.data.Fri.thi_four.js+"\n"
                            +courseList.data.Fri.thi_four.skdd+"\n"
                            +courseList.data.Fri.thi_four.skzc+"\n"
                            +courseList.data.Fri.thi_four.sksj);
                    if (courseList.data.Fri.thi_four.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 23:
                    tv.setText(courseList.data.Fri.fif_six.kcmc+"\n"
                            +courseList.data.Fri.fif_six.js+"\n"
                            +courseList.data.Fri.fif_six.skdd+"\n"
                            +courseList.data.Fri.fif_six.skzc+"\n"
                            +courseList.data.Fri.fif_six.sksj);
                    if (courseList.data.Fri.fif_six.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 24:
                    tv.setText(courseList.data.Fri.sev_eight.kcmc+"\n"
                            +courseList.data.Fri.sev_eight.js+"\n"
                            +courseList.data.Fri.sev_eight.skdd+"\n"
                            +courseList.data.Fri.sev_eight.skzc+"\n"
                            +courseList.data.Fri.sev_eight.sksj);
                    if (courseList.data.Fri.sev_eight.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 25:
                    tv.setText(courseList.data.Fri.nin_ten.kcmc+"\n"
                            +courseList.data.Fri.nin_ten.js+"\n"
                            +courseList.data.Fri.nin_ten.skdd+"\n"
                            +courseList.data.Fri.nin_ten.skzc+"\n"
                            +courseList.data.Fri.nin_ten.sksj);
                    if (courseList.data.Fri.nin_ten.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                    //周六
                case 26:
                    tv.setText(courseList.data.Sat.fir_sec.kcmc+"\n"
                            +courseList.data.Sat.fir_sec.js+"\n"
                            +courseList.data.Sat.fir_sec.skdd+"\n"
                            +courseList.data.Sat.fir_sec.skzc+"\n"
                            +courseList.data.Sat.fir_sec.sksj);
                    if (courseList.data.Sat.fir_sec.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 27:
                    tv.setText(courseList.data.Sat.thi_four.kcmc+"\n"
                            +courseList.data.Sat.thi_four.js+"\n"
                            +courseList.data.Sat.thi_four.skdd+"\n"
                            +courseList.data.Sat.thi_four.skzc+"\n"
                            +courseList.data.Sat.thi_four.sksj);
                    if (courseList.data.Sat.thi_four.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 28:
                    tv.setText(courseList.data.Sat.fif_six.kcmc+"\n"
                            +courseList.data.Sat.fif_six.js+"\n"
                            +courseList.data.Sat.fif_six.skdd+"\n"
                            +courseList.data.Sat.fif_six.skzc+"\n"
                            +courseList.data.Sat.fif_six.sksj);
                    if (courseList.data.Sat.fif_six.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 29:
                    tv.setText(courseList.data.Sat.sev_eight.kcmc+"\n"
                            +courseList.data.Sat.sev_eight.js+"\n"
                            +courseList.data.Sat.sev_eight.skdd+"\n"
                            +courseList.data.Sat.sev_eight.skzc+"\n"
                            +courseList.data.Sat.sev_eight.sksj);
                    if (courseList.data.Sat.sev_eight.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 30:
                    tv.setText(courseList.data.Sat.nin_ten.kcmc+"\n"
                            +courseList.data.Sat.nin_ten.js+"\n"
                            +courseList.data.Sat.nin_ten.skdd+"\n"
                            +courseList.data.Sat.nin_ten.skzc+"\n"
                            +courseList.data.Sat.nin_ten.sksj+"\n");
                    if (courseList.data.Sat.nin_ten.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                    //周日
                case 31:
                    tv.setText(courseList.data.Sun.fir_sec.kcmc+"\n"
                            +courseList.data.Sun.fir_sec.js+"\n"
                            +courseList.data.Sun.fir_sec.skdd+"\n"
                            +courseList.data.Sun.fir_sec.skzc+"\n"
                            +courseList.data.Sun.fir_sec.sksj+"\n");
                    if (courseList.data.Sun.fir_sec.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 32:
                    tv.setText(courseList.data.Sun.thi_four.kcmc+"\n"
                            +courseList.data.Sun.thi_four.js+"\n"
                            +courseList.data.Sun.thi_four.skdd+"\n"
                            +courseList.data.Sun.thi_four.skzc+"\n"
                            +courseList.data.Sun.thi_four.sksj+"\n");
                    if (courseList.data.Sun.thi_four.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 33:
                    tv.setText(courseList.data.Sun.fif_six.kcmc+"\n"
                            +courseList.data.Sun.fif_six.js+"\n"
                            +courseList.data.Sun.fif_six.skdd+"\n"
                            +courseList.data.Sun.fif_six.skzc+"\n"
                            +courseList.data.Sun.fif_six.sksj+"\n");
                    if (courseList.data.Sun.fif_six.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 34:
                    tv.setText(courseList.data.Sun.sev_eight.kcmc+"\n"
                            +courseList.data.Sun.sev_eight.js+"\n"
                            +courseList.data.Sun.sev_eight.skdd+"\n"
                            +courseList.data.Sun.sev_eight.skzc+"\n"
                            +courseList.data.Sun.sev_eight.sksj+"\n");
                    if (courseList.data.Sun.sev_eight.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;
                case 35:
                    tv.setText(courseList.data.Sun.nin_ten.kcmc+"\n"
                            +courseList.data.Sun.nin_ten.js+"\n"
                            +courseList.data.Sun.nin_ten.skdd+"\n"
                            +courseList.data.Sun.nin_ten.skzc+"\n"
                            +courseList.data.Sun.nin_ten.sksj+"\n");
                    if (courseList.data.Sun.nin_ten.kcmc.length()==0){
                        isEmpty = true;
                    }
                    break;

            }
            x = j % 15;
            if (isEmpty){
                view.setBackgroundResource(R.color.white);
                x=16;
                return view;
            }
        }
        switch (x) {
            case 0:
                view.setBackgroundResource(R.drawable.ic_course_bg_0);
                break;
            case 1:
                view.setBackgroundResource(R.drawable.ic_course_bg_1);
                break;
            case 2:
                view.setBackgroundResource(R.drawable.ic_course_bg_2);
                break;
            case 3:
                view.setBackgroundResource(R.drawable.ic_course_bg_3);
                break;
            case 4:
                view.setBackgroundResource(R.drawable.ic_course_bg_4);
                break;
            case 5:
                view.setBackgroundResource(R.drawable.ic_course_bg_5);
                break;
            case 6:
                view.setBackgroundResource(R.drawable.ic_course_bg_6);
                break;
            case 7:
                view.setBackgroundResource(R.drawable.ic_course_bg_7);
                break;
            case 8:
                view.setBackgroundResource(R.drawable.ic_course_bg_8);
                break;
            case 9:
                view.setBackgroundResource(R.drawable.ic_course_bg_9);
                break;
            case 10:
//                view.setBackgroundResource(R.drawable.ic_course_bg_10);
                break;
            case 11:
                view.setBackgroundResource(R.drawable.ic_course_bg_11);
                break;
            case 12:
                view.setBackgroundResource(R.drawable.ic_course_bg_12);
                break;
            case 13:
                view.setBackgroundResource(R.drawable.ic_course_bg_13);
                break;
            case 14:
                view.setBackgroundResource(R.drawable.ic_course_bg_10);
                break;
        }
        return view;
    }

}