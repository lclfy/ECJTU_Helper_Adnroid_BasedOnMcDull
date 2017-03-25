package com.mcdull.cert.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mcdull.cert.ActivityMode.MyTitleActivity;
import com.mcdull.cert.R;
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.InternetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ShowEnrollActivity extends MyTitleActivity {

    private ShowWaitPopupWindow showWaitPopupWindow;
    private boolean isGetImg = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_show_enroll);
        super.onCreate(savedInstanceState);

        showWaitPopupWindow = new ShowWaitPopupWindow(this);
        String json = getIntent().getStringExtra("json");
        try {
            JSONObject jsonObject = new JSONObject(json);
            String ksh = jsonObject.getString("ksh");
            String name = jsonObject.getString("name");
            String major = jsonObject.getString("major");
            String ems = jsonObject.getString("ems");
            ((TextView) findViewById(R.id.tv_name)).setText("姓名："+name);
            ((TextView) findViewById(R.id.tv_zkz)).setText("准考证号："+ksh);
            ((TextView) findViewById(R.id.tv_ems)).setText("EMS单号："+ems);
            ((TextView) findViewById(R.id.tv_major)).setText("专业："+major);
            getEmsJson(ems);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isGetImg) {
            showWaitPopupWindow.showWait();
        }
    }


    public void getEmsJson(String ems) {
        Map<String, String> map = new ArrayMap<>();
        map.put("num", ems);//设置get参数
        String url = "http://api.ecjtu.org/func/weixin/ems_api.php";//设置url
        new InternetUtil(handler, url, map);//传入参数
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isGetImg = false;
            showWaitPopupWindow.dismissWait();
            if (msg.what == 1) {
                Bundle bundle = (Bundle) msg.obj;
                try {
                    JSONArray jsonArray = new JSONArray(bundle.getString("Json"));
                    if (jsonArray.length()==0){
                        ((TextView) findViewById(R.id.tv_wuliu)).setText("暂无物流信息。请耐心等候。");
                        return;
                    }
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String context = jsonObject.getString("context");
                    ((TextView) findViewById(R.id.tv_wuliu)).setText(context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
