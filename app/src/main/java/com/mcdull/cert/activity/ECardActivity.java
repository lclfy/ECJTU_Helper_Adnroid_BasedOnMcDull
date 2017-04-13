package com.mcdull.cert.activity;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RunnableFuture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.support.v4.util.ArrayMap;
import android.util.Log;
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
    //从主界面传进来的余额数据
    String eCardBalance;
    private ImageView noDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ecard);
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        String eCardJson = (String)bundle.getSerializable("eCardJson");
        this.eCardBalance = (String)bundle.getSerializable("eCardBalance");
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    finishAfterTransition();
                else
                    finish();
            }
        });
        init(eCardJson);
        //充值按钮
        findViewById(R.id.bt_Ecardrecharge).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                eCardRecharge();

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
                        eCardMap.put("consumeType",consumeLog.type);
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
    private void eCardRecharge(){
        //直接跳到建行App
        try{
            ComponentName localComponentName = new ComponentName(
                    "com.chinamworld.main",
                    "com.ccb.mbs.main.StartActivity");
            Intent localIntent = new Intent();
            localIntent.setComponent(localComponentName);
            startActivity(localIntent);
        }catch (Exception e){

                // 没有安装要跳转的app应用，提醒一下
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(ECardActivity.this);
                normalDialog.setTitle("尚未下载建行手机银行");
                normalDialog.setMessage("点击“下载”开始下载");
                    normalDialog.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            normalDialog.setPositiveButton("下载",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(downLoadFile).start();
                            Toast.makeText(ECardActivity.this, "已开始在后台下载建行手机银行\n下载完成后将自动打开", Toast.LENGTH_LONG).show();
                        }
                    });
            normalDialog.show();
            }



    }

    //下载apk程序代码
    Runnable downLoadFile = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            final String fileName = "CCBClientV3.5.6.apk";
            File tmpFile = new File("/sdcard/");
            if (!tmpFile.exists()) {
                tmpFile.mkdir();
            }
            final File file = new File("/sdcard/" + fileName);

            try {
                URL url = new URL("http://download.ccb.com/cn/html1/office/ebank/dzb/subject/12/docs/security/CCBClientV3.5.6.apk");
                try {
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    try {
                        InputStream is = conn.getInputStream();
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buf = new byte[256];
                        conn.connect();
                        double count = 0;
                        if (conn.getResponseCode() >= 400) {
                            Toast.makeText(ECardActivity.this, "连接超时", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            while (count <= 100) {
                                if (is != null) {
                                    int numRead = is.read(buf);
                                    if (numRead <= 0) {
                                        break;
                                    } else {
                                        fos.write(buf, 0, numRead);
                                    }

                                } else {
                                    break;
                                }

                            }
                        }

                        conn.disconnect();
                        fos.close();
                        is.close();
                    }catch (NetworkOnMainThreadException e){

                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block

                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block

                e.printStackTrace();
            }

            openFile(file);
        }
    };

    //打开APK程序代码

    private void openFile(File file) {
        // TODO Auto-generated method stub
        Log.e("OpenFile", file.getName());
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }


}
