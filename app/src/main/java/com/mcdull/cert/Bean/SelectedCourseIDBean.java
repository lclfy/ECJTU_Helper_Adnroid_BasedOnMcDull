package com.mcdull.cert.bean;

import java.util.ArrayList;

/**
 * Created by 75800 on 2017/3/27.
 */

public class SelectedCourseIDBean {
    public int code;
    public String msg;
    public ChildSelectedCourseIDBean data;

    public class ChildSelectedCourseIDBean {
        public String term;
        public ArrayList<GrandChildSelectedCourseIDBean> number;
        public class GrandChildSelectedCourseIDBean{
            public String xxlx;//选修类型
            public String jxbmc;//？？
            public String kcmc;//*课程名称
            public String kcyq;//课程要求
            public String khfs;//考核方式
            public String xs;//学时
            public String xf;//学分
            public String sksj;//上课时间
            public String rkjs;
            public String xklx;
            public String xbmc;
            public String xbxh;//*小班编号
        }

    }
}
