package com.mcdull.cert.activity;

import android.app.Activity;
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

import java.util.ArrayList;

public class CERTActivity extends Activity {

    private ViewPager viewPagerView;
    private View view1;
    private View view2;
    private View view3;
    private View view4;
    private View view5;
    private ArrayList<ImageView> ivs;
    private View btJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cert);
        //判断SDK版本，设置沉浸状态栏
        if (Build.VERSION.SDK_INT>=19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            findViewById(R.id.status_bar).setVisibility(View.VISIBLE);
        }
        initView();

    }

    private void initView() {
        viewPagerView = (ViewPager) findViewById(R.id.viewpager);

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

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);

        ImageView iv1 = new ImageView(this);
        ImageView iv2 = new ImageView(this);
        ImageView iv3 = new ImageView(this);
        ImageView iv4 = new ImageView(this);
        ImageView iv5 = new ImageView(this);
        ivs = new ArrayList<>();
        ivs.add(iv1);
        ivs.add(iv2);
        ivs.add(iv3);
        ivs.add(iv4);
        ivs.add(iv5);

        MyAdapter adapter = new MyAdapter();
        viewPagerView.setOffscreenPageLimit(1);
        viewPagerView.setAdapter(adapter);
        viewPagerView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                btJoin.setVisibility(View.GONE);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.VISIBLE);
                view4.setVisibility(View.VISIBLE);
                view5.setVisibility(View.VISIBLE);
                view1.setBackgroundResource(R.drawable.circletm);
                view2.setBackgroundResource(R.drawable.circletm);
                view3.setBackgroundResource(R.drawable.circletm);
                view4.setBackgroundResource(R.drawable.circletm);
                view5.setBackgroundResource(R.drawable.circletm);
                switch (arg0) {
                    case 0:
                        view1.setBackgroundResource(R.drawable.circle);
                        break;
                    case 1:
                        view2.setBackgroundResource(R.drawable.circle);
                        break;
                    case 2:
                        view3.setBackgroundResource(R.drawable.circle);
                        break;
                    case 3:
                        view4.setBackgroundResource(R.drawable.circle);
                        break;
                    case 4:
                        btJoin.setVisibility(View.VISIBLE);
                        view5.setBackgroundResource(R.drawable.circle);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        freeMemory();
    }

    private void freeMemory() {
        if (view1 != null) {
            view1.setDrawingCacheEnabled(true);
            if (view1.getDrawingCache() != null) {
                view1.getDrawingCache().recycle();
            }
            view1.setDrawingCacheEnabled(false);
            view1 = null;
        }
        if (view2 != null) {
            view2.setDrawingCacheEnabled(true);
            if (view2.getDrawingCache() != null) {
                view2.getDrawingCache().recycle();
            }
            view2.setDrawingCacheEnabled(false);
            view2 = null;
        }
        if (view3 != null) {
            view3.setDrawingCacheEnabled(true);
            if (view3.getDrawingCache() != null) {
                view3.getDrawingCache().recycle();
            }
            view3.setDrawingCacheEnabled(false);
            view3 = null;
        }
        if (view4 != null) {
            view4.setDrawingCacheEnabled(true);
            if (view4.getDrawingCache() != null) {
                view4.getDrawingCache().recycle();
            }
            view4.setDrawingCacheEnabled(false);
            view4 = null;
        }
        if (view5 != null) {
            view5.setDrawingCacheEnabled(true);
            if (view5.getDrawingCache() != null) {
                view5.getDrawingCache().recycle();
            }
            view5.setDrawingCacheEnabled(false);
            view5 = null;
        }
        if (ivs != null) {
            for (int i = 0; i < ivs.size(); i++) {
                ImageView iv = ivs.get(i);
                iv.setDrawingCacheEnabled(true);
                if (iv.getDrawingCache() != null) {
                    iv.getDrawingCache().recycle();
                }
                iv.setDrawingCacheEnabled(false);
            }
            ivs = null;
        }
        if (viewPagerView != null) {
            for (int i = 0; i < viewPagerView.getCurrentItem(); i++) {
                ImageView iv = (ImageView) viewPagerView.getChildAt(i);
                if (iv != null) {
                    iv.setDrawingCacheEnabled(true);
                    if (iv.getDrawingCache() != null) {
                        iv.getDrawingCache().recycle();
                    }
                    iv.setDrawingCacheEnabled(false);
                }
            }
            viewPagerView = null;
        }
        btJoin = null;
    }

    class MyAdapter extends PagerAdapter {

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
            switch (position){
                case 0:
                    Glide.with(getApplicationContext()).load("").placeholder(R.drawable.cert_01).crossFade().into(ivs.get(position));
                    break;
                case 1:
                    Glide.with(getApplicationContext()).load("").placeholder(R.drawable.cert_02).crossFade().into(ivs.get(position));
                    break;
                case 2:
                    Glide.with(getApplicationContext()).load("").placeholder(R.drawable.cert_03).crossFade().into(ivs.get(position));
                    break;
                case 3:
                    Glide.with(getApplicationContext()).load("").placeholder(R.drawable.cert_04).crossFade().into(ivs.get(position));
                    break;
                case 4:
                    Glide.with(getApplicationContext()).load("").placeholder(R.drawable.cert_05).crossFade().into(ivs.get(position));
                    break;
            }
            ivs.get(position).setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(ivs.get(position), 0);// 添加页卡
            return ivs.get(position);
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
