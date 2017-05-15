package com.mcdull.cert.activity;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mcdull.cert.R;
import com.mcdull.cert.ActivityMode.MyTitleActivity;

import java.util.ArrayList;

public class ThemeActivity extends MyTitleActivity implements CompoundButton.OnCheckedChangeListener {

    private SharedPreferences SP;
    private SharedPreferences.Editor edit;
    private CheckBox mCbDeepPurple;
    private CheckBox mCbPink;
    private CheckBox mCbTeal;
    private CheckBox mCbBlue;
    private CheckBox mCbAmber;
    private int themeType = 0;
    private ArrayList<CheckBox> checkBoxList;
    private CheckBox mCbRed;
    private ObjectAnimator animator;
    private boolean canStart = false;
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_theme);
        super.onCreate(savedInstanceState);

        SP = getSharedPreferences("setting", MODE_PRIVATE);
        edit = SP.edit();
        color = SP.getInt("theme", 0xffD83A48);

        initView();

        setCheckBox();

    }

    private void setCheckBox() {

        mCbDeepPurple.setChecked(false);
        mCbPink.setChecked(false);
        mCbTeal.setChecked(false);
        mCbBlue.setChecked(false);
        mCbAmber.setChecked(false);

        switch (getSharedPreferences("setting", Context.MODE_PRIVATE).getInt("theme", 0xffD83A48)) {
            case 0xff673AB7:
                themeType = mCbDeepPurple.getId();
                mCbDeepPurple.setChecked(true);
                break;
            case 0xffE91E63:
                themeType = mCbPink.getId();
                mCbPink.setChecked(true);
                break;
            case 0xff009688:
                themeType = mCbTeal.getId();
                mCbTeal.setChecked(true);
                break;
            case 0xff2196F3:
                themeType = mCbBlue.getId();
                mCbBlue.setChecked(true);
                break;
            case 0xffFFC107:
                themeType = mCbAmber.getId();
                mCbAmber.setChecked(true);
                break;
            case 0xffD83A48:
                themeType = mCbRed.getId();
                mCbRed.setChecked(true);
                break;
        }
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("主题颜色");

        this.mCbDeepPurple = (CheckBox) findViewById(R.id.cb_deep_purple);
        this.mCbPink = (CheckBox) findViewById(R.id.cb_pink);
        this.mCbTeal = (CheckBox) findViewById(R.id.cb_teal);
        this.mCbBlue = (CheckBox) findViewById(R.id.cb_blue);
        this.mCbAmber = (CheckBox) findViewById(R.id.cb_amber);
        this.mCbRed = (CheckBox) findViewById(R.id.cb_red);

        TextView mTvpurple = (TextView) findViewById(R.id.tv_purple);
        mTvpurple.setTextColor(0xff673AB7);
        TextView mTvPink = (TextView) findViewById(R.id.tv_pink);
        mTvPink.setTextColor(0xffE91E63);
        TextView mTvGreen = (TextView) findViewById(R.id.tv_green);
        mTvGreen.setTextColor(0xff009688);
        TextView mTvBlue = (TextView) findViewById(R.id.tv_blue);
        mTvBlue.setTextColor(0xff2196F3);
        TextView mTvAmbor = (TextView) findViewById(R.id.tv_ambor);
        mTvAmbor.setTextColor(0xffFFC107);
        TextView mTvRed = (TextView) findViewById(R.id.tv_red);
        mTvRed.setTextColor(0xffD83A48);


        checkBoxList = new ArrayList<>();
        checkBoxList.add(mCbAmber);
        checkBoxList.add(mCbBlue);
        checkBoxList.add(mCbDeepPurple);
        checkBoxList.add(mCbPink);
        checkBoxList.add(mCbTeal);
        checkBoxList.add(mCbRed);

        for (CheckBox c : checkBoxList) {
            c.setOnCheckedChangeListener(this);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_deep_purple:
                if (isChecked) {
                    themeType = mCbDeepPurple.getId();
                    edit.putInt("theme", 0xff673AB7);
                    edit.putInt("themeInt", 0);
                    edit.apply();
                }
                break;
            case R.id.cb_pink:
                if (isChecked) {
                    themeType = mCbPink.getId();
                    edit.putInt("theme", 0xffE91E63);
                    edit.putInt("themeInt", 1);
                    edit.apply();
                }
                break;
            case R.id.cb_teal:
                if (isChecked) {
                    themeType = mCbTeal.getId();
                    edit.putInt("theme", 0xff009688);
                    edit.putInt("themeInt", 2);
                    edit.apply();
                }
                break;
            case R.id.cb_blue:
                if (isChecked) {
                    themeType = mCbBlue.getId();
                    edit.putInt("theme", 0xff2196F3);
                    edit.putInt("themeInt", 3);
                    edit.apply();
                }
                break;
            case R.id.cb_amber:
                if (isChecked) {
                    themeType = mCbAmber.getId();
                    edit.putInt("theme", 0xffFFC107);
                    edit.putInt("themeInt", 4);
                    edit.apply();
                }
                break;
            case R.id.cb_red:
                if (isChecked) {
                    themeType = mCbRed.getId();
                    edit.putInt("theme", 0xffD83A48);
                    edit.putInt("themeInt", 5);
                    edit.apply();
                }
                break;
        }
        for (CheckBox c : checkBoxList)
            c.setChecked(false);
        ((CheckBox) findViewById(themeType)).setChecked(true);
        if(animator != null) {
            animator.pause();
            changeColorAnimator((Integer) animator.getAnimatedValue(), SP.getInt("theme", 0xffD83A48));
        } else if (canStart) {
            changeColorAnimator(color, SP.getInt("theme", 0xffD83A48));
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        canStart = true;
    }

    private void changeColorAnimator(int color1, int color2) {
        animator = ObjectAnimator.ofInt(findViewById(R.id.view_title), "backgroundColor", color1, color2);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setDuration(600);
        animator.start();
    }

}
