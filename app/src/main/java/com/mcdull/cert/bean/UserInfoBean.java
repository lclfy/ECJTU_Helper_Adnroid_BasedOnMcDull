package com.mcdull.cert.bean;

/**
 * Created by BeginLu on 2017/4/6.
 */

public class UserInfoBean {
    /**
     * code : 1
     * msg : success
     * data : {"xh":"2015211001000207","zbbh":"2015211001000207","xm":"李杰","bj":"软件工程2015-2","xb":"男","mz":"汉族","csrq":"1998-01-24","zzmm":"共青团员","jg":"江西","pyfabh":"","yyfjjb":"B","xjzt":"正常|有学籍","cfzt":"正常","gkksh":"15360981150099","gkcj":"0.0"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * xh : 2015211001000207
         * zbbh : 2015211001000207
         * xm : 李杰
         * bj : 软件工程2015-2
         * xb : 男
         * mz : 汉族
         * csrq : 1998-01-24
         * zzmm : 共青团员
         * jg : 江西
         * pyfabh :
         * yyfjjb : B
         * xjzt : 正常|有学籍
         * cfzt : 正常
         * gkksh : 15360981150099
         * gkcj : 0.0
         */

        private String xh;
        private String xm;
        private String bj;
        private String xb;
        private String cfzt;

        public String getXh() {
            return xh;
        }

        public void setXh(String xh) {
            this.xh = xh;
        }

        public String getXm() {
            return xm;
        }

        public void setXm(String xm) {
            this.xm = xm;
        }

        public String getBj() {
            return bj;
        }

        public void setBj(String bj) {
            this.bj = bj;
        }

        public String getXb() {
            return xb;
        }

        public void setXb(String xb) {
            this.xb = xb;
        }

        public String getCfzt() {
            return cfzt;
        }

        public void setCfzt(String cfzt) {
            this.cfzt = cfzt;
        }
    }
}
