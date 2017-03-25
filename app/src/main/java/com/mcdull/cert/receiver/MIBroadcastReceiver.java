package com.mcdull.cert.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.mcdull.cert.R;
import com.mcdull.cert.activity.ECardActivity;
import com.mcdull.cert.activity.TripActivity;
import com.mcdull.cert.utils.InternetUtil;
import com.mcdull.cert.utils.Util;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Begin on 15/11/7.
 */
public class MIBroadcastReceiver extends PushMessageReceiver {

    private Context context;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
//        super.onReceivePassThroughMessage(context, miPushMessage);
        this.context = context;
        String content = miPushMessage.getContent();
        try {
            JSONObject jsonObject = new JSONObject(content);
            String type = jsonObject.getString("type");
            if (!TextUtils.isEmpty(type)) {
                Log.d("push", type);
                switch (type) {
                    case "eCard":
                        pushECard();
                        break;
                    case "web":
                        String url = jsonObject.getString("url");
                        String title = jsonObject.getString("title");
                        if (!TextUtils.isEmpty(url)) {
                            pushWeb(title, url);
                        }
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceiveMessage(Context context, MiPushMessage miPushMessage) {
        this.context = context;
        super.onReceiveMessage(context, miPushMessage);
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        this.context = context;
        super.onNotificationMessageClicked(context, miPushMessage);
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        this.context = context;
        super.onNotificationMessageArrived(context, miPushMessage);
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        this.context = context;
        super.onCommandResult(context, miPushCommandMessage);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        this.context = context;
        super.onReceiveRegisterResult(context, miPushCommandMessage);
    }

    private void pushWeb(String title, String url) {
        List<String> list = new LinkedList<>();
        list.add(title);
        list.add("点击查看详情>>>");
        showNotification(context, list, "web", url);
    }

    private void pushECard() {

        if (AVUser.getCurrentUser() == null)
            return;
        final String studentId = AVUser.getCurrentUser().getString("StudentId");
        final String eCardPassword = AVUser.getCurrentUser().getString("EcardPwd");
        if (studentId == null || TextUtils.isEmpty(studentId) || studentId.equals("null")) {
            return;
        }
        if (eCardPassword == null || TextUtils.isEmpty(eCardPassword) || eCardPassword.equals("null")) {
            return;
        }
        AVQuery<AVObject> query = new AVQuery<>("API");
        query.whereEqualTo("title", "eCard");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Map<String, String> map = new ArrayMap<>();
                    map.put("student_id", studentId);//设置get参数
                    map.put("%20e_password", eCardPassword);//设置get参数
                    new InternetUtil(eCardHandler, list.get(0).getString("url"), map);//传入参数
                }
            }
        });
    }

    private Handler eCardHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != 0) {
                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");
                if (Util.replace(json).equals("false")) {
                    return;
                }
                List<String> list = new LinkedList<>();
                JSONArray jsonArray = null;
                try {
                    double num = 0;
                    jsonArray = new JSONObject(json).getJSONObject("listdata").getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        double a = Double.parseDouble(item.getString("account"));
                        if (a < 0) {
                            num = num - a;
                        }
                    }
                    if (num != 0) {
                        list.add("你今日的一卡通消费为" + num + "元");
                        list.add("点击查看详情>>>");
                        showNotification(context, list, "eCard", json);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 显示通知
     */
    private void showNotification(Context context, List<String> list, String type, String value) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
        notification.setSmallIcon(R.mipmap.ic_launcher);
        notification.setContentTitle(list.get(0));
        notification.setContentText(list.get(1));
        notification.setDefaults(Notification.DEFAULT_SOUND);
        Intent intent = new Intent();
        int id = 0;
        switch (type) {
            case "eCard":
                id = 1;
                intent.setClass(context, ECardActivity.class);
                intent.putExtra("eCardJson", value);
                break;
            case "web":
                id = 5;
                intent.setClass(context, TripActivity.class);
                intent.putExtra("Title", list.get(0));
                intent.putExtra("url", value);
                break;
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pendingIntent);//添加点击事件
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, notification.build());
    }


}
