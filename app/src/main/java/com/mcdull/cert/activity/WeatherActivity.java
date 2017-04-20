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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mcdull.cert.HJXYT;
import com.mcdull.cert.bean.Weather7DayBean;
import com.mcdull.cert.bean.WeatherBean;
import com.mcdull.cert.R;

import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends Activity {
    //用于绘制时间戳
    ImageView bgImg;
    private Bitmap imgMarker;
    private int width, height;   //图片的高度和宽度
    private Bitmap imgTemp;  //临时标记图

    private TextView tv_cityName;
    private TextView tv_nowTemperture;
    private TextView tvTemperture_day1;
    private ImageView weatherIcon_day1;
    private ImageView weatherIcon2_day1;
    private TextView tvTemperture_day2;
    private ImageView weatherIcon_day2;
    private ImageView weatherIcon2_day2;
    private TextView tvTemperture_day3;
    private ImageView weatherIcon_day3;
    private ImageView weatherIcon2_day3;
    private TextView tv_day4;
    private TextView tvTemperture_day4;
    private ImageView weatherIcon_day4;
    private ImageView weatherIcon2_day4;
    private TextView tv_day5;
    private TextView tvTemperture_day5;
    private ImageView weatherIcon_day5;
    private ImageView weatherIcon2_day5;
    private TextView tv_day6;
    private TextView tvTemperture_day6;
    private ImageView weatherIcon_day6;
    private ImageView weatherIcon2_day6;
    private TextView tv_day7;
    private TextView tvTemperture_day7;
    private ImageView weatherIcon_day7;
    private ImageView weatherIcon2_day7;
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
//        String weatherJson = getIntent().getStringExtra("weather7DJson");
//        String nowTemp = getIntent().getStringExtra("nowTemp");
        getViewDetails();
    }

    private void getViewDetails() {
        //进入页面时候传入Json，此处调用

//        Weather7DayBean weatherData = new Gson().fromJson(weatherJson,Weather7DayBean.class);
        HJXYT.getInstance().getAppClient().getJWXTService().getWeatherBean("南昌").enqueue(new Callback<WeatherBean>() {
            @Override
            public void onResponse(Call<WeatherBean> call, Response<WeatherBean> response) {
                tv_nowTemperture = (TextView) findViewById(R.id.tv_nowTemperture);
                tv_nowTemperture.setText(response.body().data.temp + "℃");
            }

            @Override
            public void onFailure(Call<WeatherBean> call, Throwable t) {

            }
        });
        HJXYT.getInstance().getAppClient().getJWXTService().getWeather7DayBean("南昌").enqueue(new Callback<Weather7DayBean>() {
            @Override
            public void onResponse(Call<Weather7DayBean> call, Response<Weather7DayBean> response) {
                Log.d("weather", "weather");
                initView(response.body());
            }

            @Override
            public void onFailure(Call<Weather7DayBean> call, Throwable t) {

            }
        });
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
        SimpleDateFormat sDateFormat = new SimpleDateFormat("MM/dd HH:mm");
        String updateTime = "更新时间：" + sDateFormat.format(new java.util.Date());
        canvas.drawText(String.valueOf(updateTime), width / 2 - 5, height / 2 + 5,
                textPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        //添加到图片
        bgImg.setBackgroundDrawable(new BitmapDrawable(getResources(), imgTemp));

    }

    private void initView(Weather7DayBean weatherData) {
        //返回键
        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //搜索键
        findViewById(R.id.bt_search).setOnClickListener(new View.OnClickListener() {
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
        tv_cityName = (TextView) findViewById(R.id.tv_cityName);
        //直辖市的时候比较脑残…让人很崩溃
        if (weatherData.data.city.contains("城区")) {
            tv_cityName.setText(weatherData.data.province);
        } else {
            tv_cityName.setText(weatherData.data.city);
        }


        for (int day = 0; day < weatherData.data.weather.size(); day++) {
            //获取每天的天气
            Weather7DayBean.GrandChild7DWeatherBean dayForecast = weatherData.data.weather.get(day);
            String HighTemp = dayForecast.h_temp;
            String LowTemp = dayForecast.l_temp;
            //找天气种类
            String weatherDetails = dayForecast.weather;
            //如果有两个天气的话。。
            String[] splitWeatherString = {""};
            if (weatherDetails.contains("转")) {
                splitWeatherString = weatherDetails.split("转");
            } else {
                splitWeatherString[0] = weatherDetails;
            }
            int weatherDetailsDrawable = 0;
            int weatherDetails2Drawable = 0;
            for (int i = 0; i < splitWeatherString.length; i++) {
                if (i == 0) {
                    if (splitWeatherString[0].contains("晴")) {
                        weatherDetailsDrawable = R.drawable.weather_sunny;
                    }
                    if (splitWeatherString[0].contains("云")) {
                        weatherDetailsDrawable = R.drawable.weather_cloudy;
                    }
                    if (splitWeatherString[0].contains("阴")) {
                        weatherDetailsDrawable = R.drawable.weather_shadow;
                    }
                    if (splitWeatherString[0].contains("雾") || splitWeatherString[0].contains("霾") || splitWeatherString[0].contains("沙")) {
                        weatherDetailsDrawable = R.drawable.weather_fog;
                    }
                    if (splitWeatherString[0].contains("雨") && splitWeatherString[0].contains("雷")) {
                        weatherDetailsDrawable = R.drawable.weather_thunder;
                    }
                    if (splitWeatherString[0].contains("雨") && splitWeatherString[0].contains("雪")) {
                        weatherDetailsDrawable = R.drawable.weather_rain_snow;
                    }
                    if (splitWeatherString[0].contains("雪")) {
                        weatherDetailsDrawable = R.drawable.weather_snow;
                    }
                    if (splitWeatherString[0].contains("雨") && !splitWeatherString[0].contains("雪") && !splitWeatherString[0].contains("雷")) {
                        weatherDetailsDrawable = R.drawable.weather_rain;
                    }
                } else {
                    if (splitWeatherString[1].contains("晴")) {
                        weatherDetails2Drawable = R.drawable.weather_sunny;
                    }
                    if (splitWeatherString[1].contains("云")) {
                        weatherDetails2Drawable = R.drawable.weather_cloudy;
                    }
                    if (splitWeatherString[1].contains("阴")) {
                        weatherDetails2Drawable = R.drawable.weather_shadow;
                    }
                    if (splitWeatherString[1].contains("雾") || splitWeatherString[1].contains("霾") || splitWeatherString[1].contains("沙")) {
                        weatherDetails2Drawable = R.drawable.weather_fog;
                    }
                    if (splitWeatherString[1].contains("雨") && splitWeatherString[1].contains("雷")) {
                        weatherDetails2Drawable = R.drawable.weather_thunder;
                    }
                    if (splitWeatherString[1].contains("雨") && splitWeatherString[1].contains("雪")) {
                        weatherDetails2Drawable = R.drawable.weather_rain_snow;
                    }
                    if (splitWeatherString[1].contains("雪")) {
                        weatherDetails2Drawable = R.drawable.weather_snow;
                    }
                    if (splitWeatherString[1].contains("雨") && !splitWeatherString[1].contains("雪") && !splitWeatherString[1].contains("雷")) {
                        weatherDetails2Drawable = R.drawable.weather_rain;
                    }
                }


            }


            switch (day) {
                case 0:
                    //获取第一天的风力风向
                    tv_windStrength = (TextView) findViewById(R.id.tv_windStrength);
                    tv_windStrength.setText(dayForecast.wind);

                    tvTemperture_day1 = (TextView) findViewById(R.id.tvTemperture_day1);
                    tvTemperture_day1.setText(LowTemp + "~" + HighTemp + "℃");

                    weatherIcon_day1 = (ImageView) findViewById(R.id.weatherIcon_day1);
                    weatherIcon_day1.setBackgroundResource(weatherDetailsDrawable);
                    if (weatherDetails.contains("转")) {
                        weatherIcon2_day1 = (ImageView) findViewById(R.id.weatherIcon2_day1);
                        weatherIcon2_day1.setVisibility(View.VISIBLE);
                        weatherIcon2_day1.setBackgroundResource(weatherDetails2Drawable);
                    }
                    break;
                case 1:
                    tvTemperture_day2 = (TextView) findViewById(R.id.tvTemperture_day2);
                    tvTemperture_day2.setText(LowTemp + "~" + HighTemp + "℃");

                    weatherIcon_day2 = (ImageView) findViewById(R.id.weatherIcon_day2);
                    weatherIcon_day2.setBackgroundResource(weatherDetailsDrawable);

                    if (weatherDetails.contains("转")) {
                        weatherIcon2_day2 = (ImageView) findViewById(R.id.weatherIcon2_day2);
                        weatherIcon2_day2.setVisibility(View.VISIBLE);
                        weatherIcon2_day2.setBackgroundResource(weatherDetails2Drawable);
                    }
                    break;
                case 2:
                    tvTemperture_day3 = (TextView) findViewById(R.id.tvTemperture_day3);
                    tvTemperture_day3.setText(LowTemp + "~" + HighTemp + "℃");

                    weatherIcon_day3 = (ImageView) findViewById(R.id.weatherIcon_day3);
                    weatherIcon_day3.setBackgroundResource(weatherDetailsDrawable);

                    if (weatherDetails.contains("转")) {
                        weatherIcon2_day3 = (ImageView) findViewById(R.id.weatherIcon2_day3);
                        weatherIcon2_day3.setVisibility(View.VISIBLE);
                        weatherIcon2_day3.setBackgroundResource(weatherDetails2Drawable);
                    }
                    break;
                case 3:
                    tvTemperture_day4 = (TextView) findViewById(R.id.tvTemperture_day4);
                    tvTemperture_day4.setText(LowTemp + "~" + HighTemp + "℃");

                    weatherIcon_day4 = (ImageView) findViewById(R.id.weatherIcon_day4);
                    weatherIcon_day4.setBackgroundResource(weatherDetailsDrawable);

                    if (weatherDetails.contains("转")) {
                        weatherIcon2_day4 = (ImageView) findViewById(R.id.weatherIcon2_day4);
                        weatherIcon2_day4.setVisibility(View.VISIBLE);
                        weatherIcon2_day4.setBackgroundResource(weatherDetails2Drawable);
                    }

                    tv_day4 = (TextView) findViewById(R.id.tv_day4);
                    //截取XX日星期X的后半部分
                    tv_day4.setText(dayForecast.date.split("（")[0]);
                    break;
                case 4:
                    tvTemperture_day5 = (TextView) findViewById(R.id.tvTemperture_day5);
                    tvTemperture_day5.setText(LowTemp + "~" + HighTemp + "℃");

                    weatherIcon_day5 = (ImageView) findViewById(R.id.weatherIcon_day5);
                    weatherIcon_day5.setBackgroundResource(weatherDetailsDrawable);

                    if (weatherDetails.contains("转")) {
                        weatherIcon2_day5 = (ImageView) findViewById(R.id.weatherIcon2_day5);
                        weatherIcon2_day5.setVisibility(View.VISIBLE);
                        weatherIcon2_day5.setBackgroundResource(weatherDetails2Drawable);
                    }

                    tv_day5 = (TextView) findViewById(R.id.tv_day5);
                    //截取XX日星期X的后半部分
                    tv_day5.setText(dayForecast.date.split("（")[0]);
                    break;
                case 5:
                    weatherIcon2_day6 = (ImageView) findViewById(R.id.weatherIcon2_day6);
                    weatherIcon2_day6.setVisibility(View.VISIBLE);
                    tvTemperture_day6 = (TextView) findViewById(R.id.tvTemperture_day6);
                    tvTemperture_day6.setText(LowTemp + "~" + HighTemp + "℃");

                    weatherIcon_day6 = (ImageView) findViewById(R.id.weatherIcon_day6);
                    weatherIcon_day6.setBackgroundResource(weatherDetailsDrawable);

                    if (weatherDetails.contains("转")) {
                        weatherIcon2_day6 = (ImageView) findViewById(R.id.weatherIcon2_day6);
                        weatherIcon2_day6.setVisibility(View.VISIBLE);
                        weatherIcon2_day6.setBackgroundResource(weatherDetails2Drawable);
                    }

                    tv_day6 = (TextView) findViewById(R.id.tv_day6);
                    //截取XX日星期X的后半部分
                    tv_day6.setText(dayForecast.date.split("（")[0]);
                    break;
                case 6:
                    tvTemperture_day7 = (TextView) findViewById(R.id.tvTemperture_day7);
                    tvTemperture_day7.setText(LowTemp + "~" + HighTemp + "℃");

                    if (weatherDetails.contains("转")) {
                        weatherIcon2_day7 = (ImageView) findViewById(R.id.weatherIcon2_day7);
                        weatherIcon2_day7.setVisibility(View.VISIBLE);
                        weatherIcon2_day7.setBackgroundResource(weatherDetails2Drawable);
                    }

                    tv_day7 = (TextView) findViewById(R.id.tv_day7);
                    //截取XX日星期X的后半部分
                    tv_day7.setText(dayForecast.date.split("（")[0]);
                    break;
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
//            getViewDetails(bundle.getString("weather7DJson"), bundle.getString("nowTemp"));
        }

    }

}
