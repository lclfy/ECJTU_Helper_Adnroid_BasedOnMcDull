package com.mcdull.cert.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.mcdull.cert.ActivityMode.MyTitleActivity;
import com.mcdull.cert.R;
import com.mcdull.cert.utils.CourseUtil;
import com.mcdull.cert.utils.ShowSureDialog;
import com.mcdull.cert.utils.Util;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

public class SettingActivity extends MyTitleActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private SharedPreferences SP;
    private ShowSureDialog sureDialog;
    private Intent intent;
    // user your appid the key.
    public static final String APP_ID = "2882303761517368912";
    // user your appid the key.
    public static final String APP_KEY = "5961736867912";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        SP = getSharedPreferences("setting", MODE_PRIVATE);
        ((Switch) findViewById(R.id.st_push)).setChecked(SP.getBoolean("push", true));
        ((Switch) findViewById(R.id.st_push)).setOnCheckedChangeListener(this);
        setSwitch((Switch) findViewById(R.id.st_push), SP.getBoolean("push", true));
        ((Switch) findViewById(R.id.st_map_type)).setChecked(SP.getBoolean("mapType", true));
        ((Switch) findViewById(R.id.st_map_type)).setOnCheckedChangeListener(this);
        setSwitch((Switch) findViewById(R.id.st_map_type), SP.getBoolean("mapType", true));
//        ((Switch) findViewById(R.id.st_home)).setChecked(SP.getBoolean("homeType", false));
//        ((Switch) findViewById(R.id.st_home)).setOnCheckedChangeListener(this);
//        setSwitch((Switch) findViewById(R.id.st_home), SP.getBoolean("homeType", false));

        ((TextView) findViewById(R.id.tv_title)).setText("个人设置");
        findViewById(R.id.bt_about).setOnClickListener(this);
        findViewById(R.id.bt_logout).setOnClickListener(this);
        findViewById(R.id.bt_theme).setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setSwitch((Switch) buttonView, isChecked);
        SharedPreferences.Editor edit = SP.edit();
        switch (buttonView.getId()) {
            case R.id.st_push:
                edit.putBoolean("push", isChecked);
                if (shouldInit()) {
                    if (isChecked) {
                        MiPushClient.registerPush(this, APP_ID, APP_KEY);
                    } else {
                        MiPushClient.unregisterPush(this);
                    }
                }
                break;
            case R.id.st_map_type:
                edit.putBoolean("mapType", isChecked);
                break;
//            case R.id.st_home:
//                edit.putBoolean("homeType", isChecked);
//                break;
        }
        edit.apply();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_about:
                intent = new Intent(SettingActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_logout:
                showSureDialog();
                break;
            case R.id.bt_theme:
                intent = new Intent(SettingActivity.this, ThemeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sureDialog != null)
            sureDialog.dismiss();
    }

    private void setSwitch(Switch view, Boolean isChecked) {
        if (Build.VERSION.SDK_INT > 15) {
            if (isChecked) {
                view.setThumbResource(R.drawable.switch_on);
                view.setTrackResource(R.drawable.switch_bg_on);
            } else {
                view.setThumbResource(R.drawable.switch_off);
                view.setTrackResource(R.drawable.switch_bg_off);
            }
        } else {
            view.setChecked(isChecked);
        }
    }

    private void showSureDialog() {
        sureDialog = new ShowSureDialog(this, "退出登录", new ShowSureDialog.CallBack() {
            @Override
            public void CallBack() {

                sureDialog.dismiss();
                Intent intent = new Intent(SettingActivity.this, LoginRegisterActivity.class);
                intent.putExtra("back", true);
                intent.putExtra("userName", AVUser.getCurrentUser().getEmail());
                AVUser.getCurrentUser().logOut();
                SharedPreferences SP = getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor edit = SP.edit();
                edit.remove("CourseJson");
                edit.remove("time");
                edit.remove("mapFirst");
                edit.remove("Life");
                edit.remove("Play");
                edit.remove("Other");
                edit.remove("Study");
                edit.remove("Bei");
                edit.remove("Stay");
                edit.remove("Nan");
                edit.remove("Icon");
                edit.remove("isCourse");
                edit.apply();
                Util.removeIcon(SettingActivity.this);
                CourseUtil.removeAll(SettingActivity.this);
                startActivity(intent);
                finish();

                intent = new Intent();
                intent.setAction("com.mcdull.cert.Home");
                intent.putExtra("type", 2);
                SettingActivity.this.sendBroadcast(intent);

            }
        });
        sureDialog.showDialog();
    }

    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
