package com.mcdull.cert.bean;

import java.io.Serializable;

/**
 * Created by 75800 on 2017/3/23.
 */

public class ECardOwnerBean implements Serializable{
    public int code;
    public String msg;
    public ChildECardOwnerBean data;

    public class ChildECardOwnerBean implements Serializable{
        public String name;
        public String account;
        public String stuid;
        public String usertype;
        public String balance;//余额
        public String area;//消费地点
        public String unit;

    }
}
