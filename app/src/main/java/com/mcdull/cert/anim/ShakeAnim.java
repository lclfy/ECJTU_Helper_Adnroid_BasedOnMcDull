package com.mcdull.cert.anim;

/**
 * Created by mcdull on 15/7/10.
 */

import android.view.animation.Animation;
import android.view.animation.Transformation;

public class ShakeAnim extends Animation {
    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {

        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        t.getMatrix().setTranslate((float) (Math.sin(interpolatedTime * 30) * 20), 0);
        super.applyTransformation(interpolatedTime, t);
    }
}
