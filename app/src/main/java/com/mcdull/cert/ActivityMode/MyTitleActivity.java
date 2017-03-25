package com.mcdull.cert.ActivityMode;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.mcdull.cert.R;

/**
 * Created by mcdull on 15/9/7.
 */
public class MyTitleActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断SDK版本，设置沉浸状态栏
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            findViewById(R.id.status_bar).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences SP = getSharedPreferences("setting",MODE_PRIVATE);
        findViewById(R.id.view_title).setBackgroundColor(SP.getInt("theme",0xff009688));
    }
}
