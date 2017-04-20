package com.mcdull.cert.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mcdull.cert.bean.CourseBean;
import com.mcdull.cert.utils.Util;

import java.util.ArrayList;

/**
 * Created by BeginLu on 2017/4/8.
 */

public class CourseView extends RelativeLayout {

    private final Context context;
    private final int height;
    private ArrayList<CourseBean> arrayList;
    private final int width;
    private final String[] weeks = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    private final String[] times = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
    private RelativeLayout contentView;
    private final int padding = 2;


    public CourseView(Context context) {
        this(context, null);
    }

    public CourseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CourseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        DisplayMetrics dm = getResources().getDisplayMetrics();
        width = dm.widthPixels / 15;
        height = (int) (width * 2.5);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void init() {
        for (int i = 0; i < weeks.length; i++) {
            LinearLayout linearLayout = new LinearLayout(context);
            LayoutParams layoutParams = new LayoutParams((width * 2) - (2 * padding), width - (2 * padding));
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setBackgroundResource(getResources().getIdentifier("ic_course_bg_" + i, "drawable", context.getApplicationInfo().packageName));
            int marginsLeft = width + (i * width * 2);
            layoutParams.setMargins(marginsLeft + padding, padding, 0, 0);
            linearLayout.setLayoutParams(layoutParams);
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(weeks[i]);
            linearLayout.addView(textView);
            addView(linearLayout);
        }
        ScrollView scrollView = new ScrollView(context);
        LayoutParams layoutParams1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams1.setMargins(0, width, 0, 0);
        scrollView.setLayoutParams(layoutParams1);
        LinearLayout ly = new LinearLayout(context);
        ly.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        scrollView.addView(ly);
        this.contentView = new RelativeLayout(context);
        contentView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ly.addView(contentView);
        addView(scrollView);

        for (int i = 0; i < times.length; i++) {
            LinearLayout linearLayout = new LinearLayout(context);
            LayoutParams layoutParams = new LayoutParams(width - (2 * padding), height - (2 * padding));
            linearLayout.setBackgroundResource(getResources().getIdentifier("ic_course_bg_" + (13 - (i / 2)), "drawable", context.getApplicationInfo().packageName));
            int marginsTop = (i * height);
            linearLayout.setGravity(Gravity.CENTER);
            layoutParams.setMargins(padding, marginsTop + padding, 0, 0);
            linearLayout.setLayoutParams(layoutParams);
            TextView textView = new TextView(context);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText(times[i]);
            linearLayout.addView(textView);
            contentView.addView(linearLayout);
        }
    }

    public void setCourse(ArrayList<CourseBean> arrayList) {
        this.arrayList = arrayList;
//        removeAllViews();
        refreshView();

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    public void refreshView() {
        if (arrayList == null)
            return;
        for (CourseBean courseBean : arrayList) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            LayoutParams layoutParams = new LayoutParams(width * 2 - 2 * padding, courseBean.sksc * height - 2 * padding);
            layoutParams.setMargins((width) + (courseBean.skxq - 1) * (width * 2) + padding, (courseBean.skkssj - 1) * height + padding, 0, 0);
            int i = courseBean.kcmc.hashCode();
            if (i < 0)
                i = -i;
            linearLayout.setBackgroundResource(getResources().getIdentifier("ic_course_bg_" + (i % 14), "drawable", context.getApplicationInfo().packageName));
            linearLayout.setLayoutParams(layoutParams);

            TextView textView = new TextView(context);
            textView.setText(courseBean.kcmc);
            textView.setMaxLines(2);
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(textView);
            TextView textView1 = new TextView(context);
            textView1.setText(courseBean.skdd + "\n" +
                    courseBean.skzc_s + "-" + courseBean.skzc_e + "\n" +
                    courseBean.js + "\n");
            textView1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(textView1);
            contentView.addView(linearLayout);
        }
    }
}
