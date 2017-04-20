package com.mcdull.cert.bean;

import java.io.Serializable;

/**
 * Created by BeginLu on 2017/4/8.
 */

public class CourseBean implements Serializable {
    public String kcmc;
    public String js;
    public String skdd;

    public int skzc_s;
    public int skzc_e;
    public int skkssj;
    public int sksc;
    public int skxq;//课程所在星期
    public int dsz;//单双周 0为不分单双周 1为单周 2为双周

    @Override
    public String toString() {
        return "CourseBean{" +
                "kcmc='" + kcmc + '\'' +
                ", js='" + js + '\'' +
                ", skdd='" + skdd + '\'' +
                ", skzc_s=" + skzc_s +
                ", skzc_e=" + skzc_e +
                ", sksj=" + skkssj +
                ", sksc=" + sksc +
                ", skxq=" + skxq +
                ", dsz=" + dsz +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseBean that = (CourseBean) o;

        if (skzc_s != that.skzc_s) return false;
        if (skzc_e != that.skzc_e) return false;
        if (skkssj != that.skkssj) return false;
        if (sksc != that.sksc) return false;
        if (skxq != that.skxq) return false;
        if (dsz != that.dsz) return false;
        if (!kcmc.equals(that.kcmc)) return false;
        if (!js.equals(that.js)) return false;
        return skdd.equals(that.skdd);

    }

    @Override
    public int hashCode() {
        int result = kcmc.hashCode();
        result = 31 * result + js.hashCode();
        result = 31 * result + skdd.hashCode();
        result = 31 * result + skzc_s;
        result = 31 * result + skzc_e;
        result = 31 * result + skkssj;
        result = 31 * result + sksc;
        result = 31 * result + skxq;
        result = 31 * result + dsz;
        return result;
    }
}
