package com.mcdull.cert.bean;

import java.util.ArrayList;

/**
 * Created by 75800 on 2017/3/15.
 */

public class Weather7DayBean {
    public Child7DWeatherBean data;
    public int code;
    public String msg;

    public class Child7DWeatherBean {
        public String province;
        public String city;
        public String county;
        public ArrayList<GrandChild7DWeatherBean> weather;
    }
        public class GrandChild7DWeatherBean {
            public String date;
            public String weather;
            public String h_temp;
            public String l_temp;
            public String wind;
            public GrandGrandChild7DWeatherBean index;

        }
        public class GrandGrandChild7DWeatherBean {
            public LastChild7DWeatherBean zwxzs;
            public LastChild7DWeatherBean gmzs;
            public LastChild7DWeatherBean cyzs;
            public LastChild7DWeatherBean xczs;
            public LastChild7DWeatherBean ydzs;
            public LastChild7DWeatherBean kqwrzs;
        }
        public class LastChild7DWeatherBean {
        public String index;
        public String suggest;



    }


}
