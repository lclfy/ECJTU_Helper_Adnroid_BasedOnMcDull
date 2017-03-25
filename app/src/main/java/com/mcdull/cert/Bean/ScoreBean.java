package com.mcdull.cert.Bean;

import java.util.ArrayList;

/**
 * Created by 75800 on 2017/3/24.
 */

public class ScoreBean {
    public int code;
    public String msg;
    public ChildScoreBean data;

    public class ChildScoreBean {
        public String term;
        public ArrayList<GrandChildScoreBean> score;
        public class GrandChildScoreBean{
            public String kcmc;//课程名称
            public String kcyq;
            public String khfs;//考核方式
            public String kcxf;//课程学分
            public String kscj;//考试成绩
            public String ckcj;
            public String cxcj;
        }

    }
}
