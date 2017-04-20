package com.mcdull.cert.bean;

import java.util.ArrayList;

/**
 * Created by 75800 on 2017/3/27.
 */

public class CalenderBean {
    public int code;
    public String msg;
    public ChildCalenderBean data;

    public class ChildCalenderBean {
        public String date;
        public String weekday;
        public String week;
        public ArrayList<GrandChildCalenderBean> daylist;
        public class GrandChildCalenderBean{
            public String className;
            public String classRoom;//*教室
            public String classSpan;
            public String classString;//*上课时间
            public String course;//*课程名称
            public String courseRequire;//考核方式
            public String pkType;//*本周是否上课
            public String teacherName;
            public String weekDay;
            public String weekSpan;
        }

    }
}
