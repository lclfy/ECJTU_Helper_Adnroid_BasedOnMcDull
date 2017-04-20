package com.mcdull.cert.utils.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by BeginLu on 2017/3/29.
 */

public class HJXYTClient {
    private static volatile HJXYTClient instance;
    private final ConcurrentHashMap<Class, Object> services;
    private final Retrofit adapter;
    private static String baseUrl = "http://api1.ecjtu.org/v1/";

    private HJXYTClient() {
        this.services = new ConcurrentHashMap<>();
        final Gson gson = new GsonBuilder().create();
        OkHttpClient okHttpClient = new OkHttpClient();

        adapter = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static HJXYTClient getInstance() {
        if (instance == null)
            instance = new HJXYTClient();
        return instance;
    }

    public JWXTService getJWXTService() {
        return getService(JWXTService.class);
    }

    protected <T> T getService(Class<T> cls) {
        if (!services.contains(cls))
            services.putIfAbsent(cls, adapter.create(cls));
        return (T) services.get(cls);
    }
}
