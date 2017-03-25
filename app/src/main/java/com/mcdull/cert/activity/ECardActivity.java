package com.mcdull.cert.activity;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mcdull.cert.Bean.WeatherBean;
import com.mcdull.cert.Bean.eCardBean;
import com.mcdull.cert.R;
import com.mcdull.cert.adapter.ECardAdapter;

public class ECardActivity extends Activity {

    private TextView tv_eCardConsume;
    private TextView tv_eCardBalance;
    private TextView tvECardId;
    private TextView tvBalance;
    private TextView tvTotal;
    private ListView lvECard;
    private TextView tvQueryTitle;
    private boolean downloadCounter = false;
    //从主界面传进来的余额数据
    String eCardBalance;
    private ImageView noDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ecard);
        super.onCreate(savedInstanceState);
        String eCardJson = getIntent().getStringExtra("eCardJson");
        this.eCardBalance = getIntent().getStringExtra("eCardBalance");
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(1);
        //判断SDK版本，设置沉浸状态栏
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            findViewById(R.id.status_bar).setVisibility(View.VISIBLE);
        }
        initView(eCardJson);
        //返回键
        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init(eCardJson);
        //充值按钮
        findViewById(R.id.bt_Ecardrecharge).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                eCardRecharge(downloadCounter);
                if (downloadCounter == false){
                    downloadCounter = !downloadCounter;
                }

            }
        });
    }

    private void init(String eCardJson) {
        //进入页面时候传入Json，此处调用
        //无消费记录的图
        noDetails = (ImageView)findViewById(R.id.noCardDetails);
        noDetails.setVisibility(View.GONE);
        lvECard = (ListView) findViewById(R.id.lv_ecard);
        try{
            eCardBean eCardData = new Gson().fromJson(eCardJson, eCardBean.class);

            List<Map<String, String>> list = new ArrayList<>();
            if (eCardData!=null) {
                tv_eCardConsume = (TextView)findViewById(R.id.tv_eCardConsume);
                tv_eCardBalance = (TextView)findViewById(R.id.tv_eCardBalance);
                float dayConsume = 0;
                float nowBalance = 0;
                if (eCardData.data != null){
                    for (int item = 0;item<eCardData.data.size();item++) {
                        eCardBean.ChildECardBean consumeLog = eCardData.data.get(item);
                        if(item == 0){
                            nowBalance = Float.parseFloat(consumeLog.balance);
                        }
                        //去除充值金额后的消费金额的绝对值累计
                        if (Float.parseFloat(consumeLog.consume) < 0){
                            dayConsume += Math.abs(Float.parseFloat(consumeLog.consume));
                        }
                        //把arraylist填充成list
                        Map<String, String> eCardMap = new ArrayMap<>();
                        eCardMap.put("consumeCount", consumeLog.consume);
                        eCardMap.put("consumeTime", consumeLog.time);
                        eCardMap.put("consumeAddress", consumeLog.address);
                        list.add(eCardMap);


                    }
                }
                if (eCardData.data.size() == 0){
                    noDetails.setVisibility(View.VISIBLE);
                }
                tv_eCardConsume.setText(String.valueOf(dayConsume)+"元");
                tv_eCardBalance.setText(this.eCardBalance);
                //填充listview
                ECardAdapter adapter = new ECardAdapter(this,list);
                lvECard.setAdapter(adapter);
            }else {

                tv_eCardConsume.setText("0"+"元");
                tv_eCardBalance.setText(this.eCardBalance);

                Toast.makeText(ECardActivity.this, "一卡通信息暂时无法获取", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(ECardActivity.this, "发生错误，请重试", Toast.LENGTH_SHORT).show();
        }




    }

    private void initView(String eCardJson) {

    }


    //一卡通充值按钮
    private void eCardRecharge(boolean isDownloading){
        //直接跳到建行App
        try{
            ComponentName localComponentName = new ComponentName(
                    "com.chinamworld.main",
                    "com.ccb.mbs.main.StartActivity");
            Intent localIntent = new Intent();
            localIntent.setComponent(localComponentName);
            startActivity(localIntent);
        }catch (Exception e){
            if (isDownloading)
            {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://download.ccb.com/cn/html1/office/ebank/dzb/subject/12/docs/security/CCBClientV3.5.6.apk"));
                //设置通知栏标题
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setTitle("开始下载:");
                request.setDescription("正在下载建行手机银行…");
                request.setAllowedOverRoaming(false);
                //设置文件存放目录
                //request.setDestinationInExternalFilesDir(activity, Environment.DIRECTORY_DOWNLOADS, "建设银行");
                DownloadManager downManager = (DownloadManager)this.getSystemService(this.DOWNLOAD_SERVICE);
                long id= downManager.enqueue(request);
            }else {
                // 没有安装要跳转的app应用，提醒一下
                Toast.makeText(this, "尚未安装建行手机银行App，再次点击充值按钮即可开始下载", Toast.LENGTH_LONG).show();
            }

        }

    }


}
