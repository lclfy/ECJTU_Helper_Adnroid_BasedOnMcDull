package com.mcdull.cert.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Begin on 15/10/18.
 */
public class MyViewPager extends ViewPager {

    private boolean isEvent = true;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isEvent){
            return super.onTouchEvent(ev);
        }else {
            return true;
        }
    }

    public void setIsEvent(boolean isEvent) {
        this.isEvent = isEvent;
    }

    public boolean getIsEvent(){
        return isEvent;
    }
}
