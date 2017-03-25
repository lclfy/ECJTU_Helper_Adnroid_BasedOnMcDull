package com.mcdull.cert.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.content.Context;
import android.app.Application;

import com.alibaba.fastjson.JSON;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mcdull.cert.Bean.WeatherBean;
import com.mcdull.cert.Models.Weather;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by mcdull on 15/7/23.
 */
public class InternetUtil {

    private Map<String, String> map;
    private String url;
    private Handler handler;
    private String Json = null;
    RequestQueue sQueue = null;


    public InternetUtil(Handler handler, String url) {
        this.handler = handler;
        this.url = url;
        start(false);
    }

    public InternetUtil(Handler handler, String url, Map<String, String> map) {
        this.handler = handler;
        this.url = url;
        this.map = map;
        start(false);
    }

    public InternetUtil(Handler handler, String url, Map<String, String> map, boolean ifUseVolley,Context context) {
        //自定义HTTPOK方法无法正确获取JSON时使用
        this.handler = handler;
        this.url = url;
        this.map = map;
        sQueue = Volley.newRequestQueue(context);
        start(ifUseVolley);
    }

    /**
     * 判断是否连接网络
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());

    }

    private void start(final boolean ifUseVolley) {
        if (handler == null || url == null || url.equals("")) {
            return;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                String json = null;
                if(ifUseVolley){
                    getJsonWithVolley();
                    return;
                }else{
                    json = getJson();
                }
                Message msg = new Message();
                if (TextUtils.isEmpty(json)) {
                    msg.what = 0;
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("Json", json);
                    msg.obj = bundle;
                    msg.what = 1;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    private String getJson() {
        HttpClient client = null;
        try {
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
            HttpConnectionParams.setSoTimeout(httpParams, 5000);
            client = new DefaultHttpClient(httpParams);

            String URL = url;

            if (map != null && map.size() != 0) {
                URL = URL + "?";
                //拼URL
                for (String key : map.keySet()) {
                    String v = URLEncoder.encode(map.get(key), "UTF-8");
                    URL = URL + key + "=" + v + "&";
                }
            }
            //查询URL内容 转为字符串传回去
            HttpGet get = new HttpGet(URL);
            HttpResponse response = client.execute(get);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                InputStream is = response.getEntity().getContent();
                return Util.convertStreamToString(is);
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (client != null) {
                client.getConnectionManager().shutdown();
            }
        }
        return null;
    }

    private void getJsonWithVolley(){
        String URL = url;
        try{
            if (map != null && map.size() != 0) {
                URL = URL + "?";
                //拼URL
                for (String key : map.keySet()) {

                    String v = URLEncoder.encode(map.get(key), "UTF-8");
                    URL = URL + key + "=" + v + "&";
                }
            }
        }catch (UnsupportedEncodingException e){

        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());
                        Json = response.toString();
                        Message msg = new Message();
                        if (TextUtils.isEmpty(Json)) {
                            msg.what = 0;
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("Json", Json);
                            msg.obj = bundle;
                            msg.what = 1;
                        }
                        handler.sendMessage(msg);
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("Error", "错误");
                msg.obj = bundle;
                msg.what = 0;
                handler.sendMessage(msg);
            }
        });
        sQueue.add(jsonObjectRequest);
    }



}
