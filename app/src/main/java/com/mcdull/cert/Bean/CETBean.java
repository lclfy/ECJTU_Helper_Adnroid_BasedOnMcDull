package com.mcdull.cert.bean;

/**
 * Created by 75800 on 2017/3/24.
 */

public class CETBean {
    public int code;
    public String msg;
    public ChildCETBean data;

    public class ChildCETBean {
        public String name;
        public String school;
        public String type;
        public WritingTestBean w_test;
        public SpeakingTestBean s_test;

        public class WritingTestBean{
            public String crtNum;//准考证号
            public String total;//总分，下面是三项分数
            public String listen;
            public String reading;
            public String writing;
        }
        public class SpeakingTestBean{
            public String crtNum;
            public String level;
        }

    }
}
