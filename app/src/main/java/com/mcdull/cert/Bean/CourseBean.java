package com.mcdull.cert.Bean;

import java.util.ArrayList;

/**
 * Created by 75800 on 2017/3/23.
 */

public class CourseBean {
    public int code;
    public String msg;
    public SecondCourseBean data;

    public class SecondCourseBean {
        public ThirdCourseBean Mon;
        public ThirdCourseBean Tues;
        public ThirdCourseBean Wed;
        public ThirdCourseBean Thur;
        public ThirdCourseBean Fri;
        public ThirdCourseBean Sat;
        public ThirdCourseBean Sun;

        public class ThirdCourseBean {
            public ForthCourseBean fir_sec;//1-2节课，后面以此类推
            public ForthCourseBean thi_four;
            public ForthCourseBean fif_six;
            public ForthCourseBean sev_eight;
            public ForthCourseBean nin_ten;
            public ForthCourseBean elev_twe;
            public class ForthCourseBean {
                public String kcmc;
                public String js;
                public String skdd;
                public String skzc;
                public String sksj;
            }
        }

    }
}
