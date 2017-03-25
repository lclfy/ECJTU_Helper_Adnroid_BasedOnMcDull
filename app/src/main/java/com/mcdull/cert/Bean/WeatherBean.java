package com.mcdull.cert.Bean;

import java.util.ArrayList;

/**
 * Created by 75800 on 2017/3/15.
 */

public class WeatherBean {
    public ChildWeatherBean data;
    public int status;
    public String desc;

    public class ChildWeatherBean {
        public int wendu;
        public String ganmao;
        public String city;
        public GrandChildWeatherBean yesterday;
        public ArrayList<GrandChildsWeatherBean> forecast;

        public class GrandChildWeatherBean {

            public String high;//高温
            public String fl;//风力
            public String date;//星期几
            public String low;//低温
            public String type;//天气类型
            public String fx;//风向
        }

        public class GrandChildsWeatherBean {
            public String high;
            public String fengli;
            public String fengxiang;
            public String date;
            public String low;
            public String type;
        }
    }
}
