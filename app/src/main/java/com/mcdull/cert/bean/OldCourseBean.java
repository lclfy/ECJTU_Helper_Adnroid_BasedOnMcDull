package com.mcdull.cert.bean;

import java.io.Serializable;

/**
 * Created by 75800 on 2017/3/23.
 */

public class OldCourseBean implements Serializable {
    public int code;
    public String msg;
    public SecondCourseBean data;

    public class SecondCourseBean implements Serializable{
        public ThirdCourseBean Mon;
        public ThirdCourseBean Tues;
        public ThirdCourseBean Wed;
        public ThirdCourseBean Thur;
        public ThirdCourseBean Fri;
        public ThirdCourseBean Sat;
        public ThirdCourseBean Sun;

        public class ThirdCourseBean implements Serializable{
            public ForthCourseBean fir_sec;//1-2节课，后面以此类推
            public ForthCourseBean thi_four;
            public ForthCourseBean fif_six;
            public ForthCourseBean sev_eight;
            public ForthCourseBean nin_ten;
            public ForthCourseBean elev_twe;
            public class ForthCourseBean implements Serializable{
                public String kcmc;
                public String js;
                public String skdd;
                public String skzc;
                public String sksj;
            }
        }

    }
}
