package com.mcdull.cert;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Process;

import com.avos.avoscloud.AVOSCloud;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

public class Application extends android.app.Application {
    // user your appid the key.
    public static final String APP_ID = "2882303761517368912";
    // user your appid the key.
    public static final String APP_KEY = "5961736867912";

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        SharedPreferences SP = getSharedPreferences("config", MODE_PRIVATE);
        if (shouldInit()) {
            if (SP.getBoolean("push", true)) {
                MiPushClient.registerPush(this, APP_ID, APP_KEY);
            } else {
                MiPushClient.unregisterPush(this);
            }
        }

    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
