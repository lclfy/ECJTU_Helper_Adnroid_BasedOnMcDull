package com.mcdull.cert;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mcdull.cert.utils.services.HJXYTClient;

import java.net.URLConnection;
import java.util.Stack;

/**
 * Created by BeginLu on 2017/3/29.
 */

public class HJXYT {
    private static volatile HJXYT instance;
    private final Context context;
    private Stack<Activity> activityStack = new Stack<>();

    //    singleTop、singleTask、singleInstance
    public static final int ACTIVITY_TYPE_STANDARD = 0;
    public static final int ACTIVITY_TYPE_SINGLE_TOP = 1;
    public static final int ACTIVITY_TYPE_SINGLE_TASK = 2;
    public static final int ACTIVITY_TYPE_SINGLE_INSTANCE = 3;


    private HJXYT(Context context) {
        this.context = context;
    }

    public static void createInstance(Context context) {
        if (instance == null)
            instance = new HJXYT(context);
    }

    public static HJXYT getInstance() {
        return instance;
    }

    public HJXYTClient getAppClient() {
        return HJXYTClient.getInstance();
    }
}
