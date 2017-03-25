package com.mcdull.cert.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mcdull.cert.Bean.WeatherBean;
import com.mcdull.cert.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class WeatherActivity extends Activity {
    //用于绘制时间戳
    ImageView bgImg;
    private Bitmap imgMarker;
    private int width,height;   //图片的高度和宽度
    private Bitmap imgTemp;  //临时标记图

    private TextView tv_cityName;
    private TextView tv_nowTemperture;
    private TextView tvTemperture_day1;
    private ImageView weatherIcon_day1;
    private TextView tvTemperture_day2;
    private ImageView weatherIcon_day2;
    private TextView tvTemperture_day3;
    private ImageView weatherIcon_day3;
    private TextView tv_day4;
    private TextView tvTemperture_day4;
    private ImageView weatherIcon_day4;
    private TextView tv_day5;
    private TextView tvTemperture_day5;
    private ImageView weatherIcon_day5;
//    private TextView tv_day6;
//    private TextView tvTemperture_day6;
//    private ImageView weatherIcon_day6;
//    private TextView tv_day7;
//    private TextView tvTemperture_day7;
//    private ImageView weatherIcon_day7;
    private TextView tv_windDirection;
    private TextView tv_windStrength;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_weather);
        super.onCreate(savedInstanceState);
        //判断SDK版本，设置沉浸状态栏
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            findViewById(R.id.status_bar).setVisibility(View.VISIBLE);
        }
        //刷新页面
        String weatherJson = getIntent().getStringExtra("weatherJson");
        getViewDetails(weatherJson);
    }

    private void getViewDetails(String weatherJson){
        //进入页面时候传入Json，此处调用

        WeatherBean weatherData = new Gson().fromJson(weatherJson,WeatherBean.class);
        initView(weatherData);
        createTimeTagDrawable();
    }

    //用画笔在下方图片内添加更新时间
    private void createTimeTagDrawable() {
        //导入图片
        bgImg = (ImageView) findViewById(R.id.bg_weather);
        imgMarker = BitmapFactory.decodeResource(getResources(), R.drawable.bg_weather2);
        width = imgMarker.getWidth();
        height = imgMarker.getHeight();
        imgTemp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(imgTemp);
        Paint paint = new Paint(); // 建立画笔
        paint.setDither(true);
        paint.setFilterBitmap(true);
        Rect src = new Rect(40, 30, width, height);
        Rect dst = new Rect(0, 0, width, height);
        canvas.drawBitmap(imgMarker, src, dst, paint);

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);
        textPaint.setTextSize(40.0f);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD); // 采用默认的宽度
        textPaint.setColor(Color.WHITE);
        //获取时间
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM/dd hh:mm");
        String updateTime = "更新时间：" + sDateFormat.format(new java.util.Date());
        canvas.drawText(String.valueOf(updateTime), width /2-5, height/2+5,
                textPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        //添加到图片
        bgImg.setBackgroundDrawable(new BitmapDrawable(getResources(), imgTemp));

    }

    private void initView(WeatherBean weatherData){
        //返回键
        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //搜索键
        findViewById(R.id.bt_search).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(WeatherActivity.this, WeatherSearchActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);//将Bundle添加到Intent,也可以在Bundle中添加相应数据传递给下个页面,例如：bundle.putString("abc", "bbb");
                startActivityForResult(intent, 0);// 跳转并要求返回值，0代表请求值(可以随便写)
            }
        });
        //设置Text
        tv_cityName = (TextView)findViewById(R.id.tv_cityName);
        tv_cityName.setText(weatherData.data.city);

        tv_nowTemperture = (TextView)findViewById(R.id.tv_nowTemperture);
        tv_nowTemperture.setText(weatherData.data.wendu+"℃");

        for (int day = 0;day<weatherData.data.forecast.size();day++){
            //获取每天的天气
            WeatherBean.ChildWeatherBean.GrandChildsWeatherBean dayForecast = weatherData.data.forecast.get(day);
            String HighTemp[] = dayForecast.high.split(" ");
            String LowTemp[] = dayForecast.low.split(" ");
            //找天气种类
            String weatherDetails = dayForecast.type;
            int weatherDetailsDrawable = 0;
            if(weatherDetails.contains("晴")){
                weatherDetailsDrawable = R.drawable.weather_sunny;
            }
            if(weatherDetails.contains("云")){
                weatherDetailsDrawable = R.drawable.weather_cloudy;
            }
            if(weatherDetails.contains("阴")){
                weatherDetailsDrawable = R.drawable.weather_shadow;
            }
            if(weatherDetails.contains("雾") || weatherDetails.contains("霾") || weatherDetails.contains("沙")){
                weatherDetailsDrawable = R.drawable.weather_fog;
            }
            if(weatherDetails.contains("雨") && weatherDetails.contains("雷")){
                weatherDetailsDrawable = R.drawable.weather_thunder;
            }
            if(weatherDetails.contains("雨") && weatherDetails.contains("雪")){
                weatherDetailsDrawable = R.drawable.weather_rain_snow;
            }
            if(weatherDetails.contains("雪")){
                weatherDetailsDrawable = R.drawable.weather_snow;
            }
            if(weatherDetails.contains("雨") && !weatherDetails.contains("雪") && !weatherDetails.contains("雷")){
                weatherDetailsDrawable = R.drawable.weather_rain;
            }


            switch (day) {
                case 0:
                    //获取第一天的风力风向
                    tv_windDirection = (TextView)findViewById(R.id.tv_windDirection);
                    tv_windDirection.setText(dayForecast.fengxiang);

                    tv_windStrength = (TextView)findViewById(R.id.tv_windStrength);
                    tv_windStrength.setText(dayForecast.fengli);

                    tvTemperture_day1 = (TextView)findViewById(R.id.tvTemperture_day1);
                    tvTemperture_day1.setText(LowTemp[1]+"~"+HighTemp[1]);

                    weatherIcon_day1 = (ImageView)findViewById(R.id.weatherIcon_day1);
                    weatherIcon_day1.setBackgroundResource(weatherDetailsDrawable);
                    break;
                case 1:
                    tvTemperture_day2 = (TextView)findViewById(R.id.tvTemperture_day2);
                    tvTemperture_day2.setText(LowTemp[1]+"~"+HighTemp[1]);

                    weatherIcon_day2 = (ImageView)findViewById(R.id.weatherIcon_day2);
                    weatherIcon_day2.setBackgroundResource(weatherDetailsDrawable);
                    break;
                case 2:
                    tvTemperture_day3 = (TextView)findViewById(R.id.tvTemperture_day3);
                    tvTemperture_day3.setText(LowTemp[1]+"~"+HighTemp[1]);

                    weatherIcon_day3 = (ImageView)findViewById(R.id.weatherIcon_day3);
                    weatherIcon_day3.setBackgroundResource(weatherDetailsDrawable);
                    break;
                case 3:
                    tvTemperture_day4 = (TextView)findViewById(R.id.tvTemperture_day4);
                    tvTemperture_day4.setText(LowTemp[1]+"~"+HighTemp[1]);

                    weatherIcon_day4 = (ImageView)findViewById(R.id.weatherIcon_day4);
                    weatherIcon_day4.setBackgroundResource(weatherDetailsDrawable);

                    tv_day4 = (TextView)findViewById(R.id.tv_day4);
                    //截取XX日星期X的后半部分
                    tv_day4.setText(dayForecast.date.split("日")[1]);
                    break;
                case 4:
                    tvTemperture_day5 = (TextView)findViewById(R.id.tvTemperture_day5);
                    tvTemperture_day5.setText(LowTemp[1]+"~"+HighTemp[1]);

                    weatherIcon_day5 = (ImageView)findViewById(R.id.weatherIcon_day5);
                    weatherIcon_day5.setBackgroundResource(weatherDetailsDrawable);

                    tv_day5 = (TextView)findViewById(R.id.tv_day5);
                    //截取XX日星期X的后半部分
                    tv_day5.setText(dayForecast.date.split("日")[1]);
                    break;
//                case 5:
//                    tvTemperture_day6 = (TextView)findViewById(R.id.tvTemperture_day6);
//                    tvTemperture_day6.setText(LowTemp[1]+"-"+HighTemp[1]);
//
//                    tv_day6 = (TextView)findViewById(R.id.tv_day6);
//                    //截取XX日星期X的后半部分
//                    tv_day6.setText(dayForecast.date.split("日")[1]);
//                    break;
//                case 6:
//                    tvTemperture_day7 = (TextView)findViewById(R.id.tvTemperture_day7);
//                    tvTemperture_day7.setText(LowTemp[1]+"-"+HighTemp[1]);
//
//                    tv_day7 = (TextView)findViewById(R.id.tv_day7);
//                    //截取XX日星期X的后半部分
//                    tv_day7.setText(dayForecast.date.split("日")[1]);
//                    break;
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //接受搜索页面传回的数据
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            //刷新页面
            getViewDetails(bundle.getString("weatherJson"));
        }

    }

}
