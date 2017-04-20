package com.mcdull.cert.bean;

import java.util.ArrayList;

/**
 * Created by 75800 on 2017/3/15.
 */

public class WeatherBean {
    public ChildWeatherBean data;
    public int code;
    public String msg;

    public class ChildWeatherBean {
        public String nameen;
        public String city;
        public String cityname;//*
        public String temp;//*
        public String tempf;
        public String WD;//*
        public String wde;
        public String WS;//*
        public String wse;
        public String SD;
        public String time;
        public String weather;
        public String weathere;
        public String weathercode;
        public String qy;
        public String njd;
        public String sd;
        public String rain;
        public String rain24h;
        public String aqi;
        public String limitnumber;
        public String aqi_pm25;
        public String date;
        public GrandChildWeatherBean index;


        public class GrandChildWeatherBean {
            public GrandChildsWeatherBean gmzs;
            public GrandChildsWeatherBean zwxzs;
            public GrandChildsWeatherBean cyzs;
            public GrandChildsWeatherBean xczs;
            public GrandChildsWeatherBean ydzs;
            public GrandChildsWeatherBean kqwrzs;
        }

        public class GrandChildsWeatherBean {
            public String index;
            public String suggest;
        }
    }
}
