package com.mcdull.cert.Bean;

import java.util.ArrayList;

/**
 * Created by 75800 on 2017/3/24.
 */

public class ReExamBean {
    public int code;
    public String msg;
    public ChildReExamBean data;

    public class ChildReExamBean {
        public String term;
        public ArrayList<GrandChildReExamBean> data;
        public class GrandChildReExamBean{
            public String kcbh;
            public String kcmc;//课程名称
            public String kcyq;
            public String kszc;
            public String kssj;//考试时间
            public String ksdd;//考试地点
        }

    }
}
