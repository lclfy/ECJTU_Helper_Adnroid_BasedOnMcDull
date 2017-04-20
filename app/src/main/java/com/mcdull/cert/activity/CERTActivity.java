package com.mcdull.cert.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mcdull.cert.R;
import com.mcdull.cert.activity.base.BaseActivity;
import com.mcdull.cert.view.SlideView;

import java.util.ArrayList;

public class CERTActivity extends BaseActivity {

    private View btJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cert);
        //判断SDK版本，设置沉浸状态栏
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            findViewById(R.id.status_bar).setVisibility(View.VISIBLE);
        }
        initView();
    }

    private void initView() {
        btJoin = findViewById(R.id.bt_join);
        btJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CERTActivity.this, JoinCERTActivity.class);
                startActivity(intent);
            }
        });
        btJoin.setVisibility(View.GONE);

        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SlideView slideView = (SlideView) findViewById(R.id.slide_view);
        MyAdapter adapter = new MyAdapter(this);
        slideView.setAdapter(adapter);
        slideView.setOnPageSelectedListener(new SlideView.OnPageSelectedListener() {
            @Override
            public void onPageSelectedListener(int position) {
                if (position == 4)
                    btJoin.setVisibility(View.VISIBLE);
                else
                    btJoin.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        freeMemory();
    }

    private void freeMemory() {
        btJoin = null;
    }

    class MyAdapter extends PagerAdapter {
        ArrayList<ImageView> ivs;

        public MyAdapter(Context context) {
            ivs = new ArrayList<>();
            for (int i = 0; i < 5; i++)
                ivs.add(new ImageView(context));
        }

        @Override
        public int getCount() {
            return ivs.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = ivs.get(position);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(getApplicationContext()).load("").placeholder(getResources().getIdentifier("cert_0" + (position + 1), "drawable", getApplicationInfo().packageName)).crossFade().into(iv);
            container.addView(iv, 0);// 添加页卡
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ivs.get(position).setDrawingCacheEnabled(true);
            if (ivs.get(position).getDrawingCache() != null) {
                ivs.get(position).getDrawingCache().recycle();
            }
            ivs.get(position).setDrawingCacheEnabled(false);
            container.removeView(ivs.get(position));// 删除页卡
        }
    }

}
