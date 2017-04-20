package com.mcdull.cert.bean;

/**
 * Created by 75800 on 2017/3/24.
 */

public class CalledPersonBean {
    public String sn;
    public String xm;
    public Boolean hasCome = false;//点名未到标记
    public Boolean isDone = false;//完成标记

    public void ChangeComeStatus(boolean hascome){
        this.hasCome = !hascome;
    }

    public void ChangeisDoneStatus(boolean isDone){
        this.isDone = !isDone;
    }
}
