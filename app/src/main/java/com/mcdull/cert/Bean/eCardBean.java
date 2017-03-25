package com.mcdull.cert.Bean;

import java.util.ArrayList;

/**
 * Created by 75800 on 2017/3/20.
 */

public class eCardBean {
    public int code;
    public String msg;
    public ArrayList<ChildECardBean> data;

    public class ChildECardBean {
        public String name;
        public String type;
        public String address;
        public String consume;//消费
        public String balance;//余额
        public String time;
        public String swipetimes;
        public String status;
        public String comment;



    }
}
