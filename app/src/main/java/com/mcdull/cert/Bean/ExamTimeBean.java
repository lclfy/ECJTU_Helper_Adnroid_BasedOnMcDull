package com.mcdull.cert.Bean;

import java.util.ArrayList;

/**
 * Created by 75800 on 2017/3/24.
 */

public class ExamTimeBean {
    public int code;
    public String msg;
    public ChildExamTimeBean data;

    public class ChildExamTimeBean {
        public String term;
        public ArrayList<GrandChildExamTimeBean> exam;
        public class GrandChildExamTimeBean{
            public String kcmc;//课程名称
            public String kcxz;//课程种类（选修必修
            public String bjmc;
            public String xsrs;
            public String kszc;
            public String kssj;//考试时间
            public String ksdd;//考试地点（只用了这三个）
            public String xbbh;
        }

    }
}
