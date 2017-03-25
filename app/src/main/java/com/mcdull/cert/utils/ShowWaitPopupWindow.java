package com.mcdull.cert.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.PopupWindow;

import com.mcdull.cert.R;


public class ShowWaitPopupWindow {

    private Activity activity;
    private PopupWindow popupWindow;
    private DisplayMetrics outMetrics;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public ShowWaitPopupWindow(Activity activity) {
        this.activity = activity;
        outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getRealMetrics(outMetrics);
        View view = View.inflate(activity, R.layout.wait_dialog, null);
        popupWindow = new PopupWindow(view, (int) (outMetrics.widthPixels),
                outMetrics.widthPixels / 4);

    }

    public void showWait() {
        popupWindow.showAsDropDown(
                View.inflate(activity, R.layout.activity_main, null),
                (outMetrics.widthPixels / 2) - (popupWindow.getWidth() / 2),
                (outMetrics.heightPixels / 2) - (popupWindow.getHeight() / 2));
    }

    public void dismissWait() {
        popupWindow.dismiss();
    }

}
