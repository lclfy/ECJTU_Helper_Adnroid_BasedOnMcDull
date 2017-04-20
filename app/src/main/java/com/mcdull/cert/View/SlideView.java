package com.mcdull.cert.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mcdull.cert.R;

/**
 * Created by BeginLu on 2017/3/30.
 */

public class SlideView extends RelativeLayout {
    private final SpotView mSpotView;
    private final ViewPager mViewPager;
    private PagerAdapter mAdapter;
    private OnPageSelectedListener onPageSelectedListener;

    public SlideView(Context context) {
        this(context, null);
    }

    public SlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mSpotView = new SpotView(context);
        mSpotView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mViewPager = new ViewPager(context);
        mViewPager.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.addView(mViewPager);
        this.addView(mSpotView);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mSpotView.setPosition(position);
                if (onPageSelectedListener != null)
                    onPageSelectedListener.onPageSelectedListener(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("size", getWidth() + ".." + getHeight());
        LayoutParams layoutParams = (LayoutParams) mSpotView.getLayoutParams();
        layoutParams.setMargins((getMeasuredWidth() - mSpotView.getMeasuredWidth()) / 2, getMeasuredHeight() - mSpotView.getMeasuredHeight() - 80, (getMeasuredWidth() - mSpotView.getMeasuredWidth()) / 2, 80);
        mSpotView.setLayoutParams(layoutParams);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setAdapter(PagerAdapter adapter) {
        this.mAdapter = adapter;
        mSpotView.drawSpot();
        mViewPager.setAdapter(adapter);
    }

    public void setOnPageSelectedListener(OnPageSelectedListener onPageSelectedListener) {
        this.onPageSelectedListener = onPageSelectedListener;
    }

    private class SpotView extends LinearLayout {

        private final Context mContext;
        private int position = 0;

        public SpotView(Context context) {
            super(context);
            drawSpot();
            this.mContext = context;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }

        private void drawSpot() {
            if (mAdapter != null) {
                removeAllViews();
                for (int i = 0; i < mAdapter.getCount(); i++) {
                    ImageView imageView = new ImageView(mContext);
                    if (position != i)
                        imageView.setImageResource(R.drawable.circletm);
                    else
                        imageView.setImageResource(R.drawable.circle);
                    LinearLayout.LayoutParams layoutParams = new LayoutParams(25, 25);
                    layoutParams.setMargins(5, 0, 5, 0);
                    imageView.setLayoutParams(layoutParams);
                    this.addView(imageView);
                }
            }
        }

        private void setPosition(int position) {
            this.position = position;
            drawSpot();
        }
    }

    public interface OnPageSelectedListener {
        void onPageSelectedListener(int position);
    }

}
