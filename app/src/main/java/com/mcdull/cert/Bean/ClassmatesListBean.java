package com.mcdull.cert.bean;

import java.util.ArrayList;

/**
 * Created by 75800 on 2017/3/24.
 */

public class ClassmatesListBean {
    public int code;
    public String msg;
    public ArrayList<ChildClassmatesBean> data;

    public class ChildClassmatesBean {
        public String sn;
        public String xm;//name
        public String xb;//sex
        public String zbbh;//在班编号
        public String xh;//学号
        public String xjzt;//学籍状态


    }
}
