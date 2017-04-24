package com.mcdull.cert.utils.services;

import com.mcdull.cert.bean.CETBean;
import com.mcdull.cert.bean.CalenderBean;
import com.mcdull.cert.bean.ClassmatesListBean;
import com.mcdull.cert.bean.ECardBean;
import com.mcdull.cert.bean.ECardOwnerBean;
import com.mcdull.cert.bean.ExamTimeBean;
import com.mcdull.cert.bean.ReExamBean;
import com.mcdull.cert.bean.ScoreBean;
import com.mcdull.cert.bean.SelectedCourseIDBean;
import com.mcdull.cert.bean.UserInfoBean;
import com.mcdull.cert.bean.Weather7DayBean;
import com.mcdull.cert.bean.WeatherBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by BeginLu on 2017/3/29.
 */

public interface JWXTService {

    @GET("schedule")
    Call<ResponseBody> getCourse(@Query("stuid") String id, @Query("passwd") String pwd);

    @GET("ecard_daytrade")
    Call<ECardBean> getECard(@Query("user") String id, @Query("passwd") String pwd);

    @GET("ecard_account")
    Call<ECardOwnerBean> getECardOwn(@Query("user") String id, @Query("passwd") String pwd);

    @GET("today")
    Call<CalenderBean> getCalenderBean(@Query("stuid") String id, @Query("passwd") String pwd);

    @GET("bexam")
    Call<ReExamBean> getBexamBean(@Query("stuid") String id, @Query("passwd") String pwd);

    @GET("exam")
    Call<ExamTimeBean> getExamTimeBean(@Query("stuid") String id, @Query("passwd") String pwd);

    @GET("score")
    Call<ScoreBean> getScoreBean(@Query("stuid") String id, @Query("passwd") String pwd, @Query("term") String term);

    @GET("selectnumber")
    Call<SelectedCourseIDBean> getSelectedCourseIDBean(@Query("stuid") String id, @Query("passwd") String pwd);

    @GET("mates")
    Call<ClassmatesListBean> getClassmatesListBean(@Query("stuid") String id, @Query("passwd") String pwd);

    @GET("profile")
    Call<UserInfoBean> getUserInfoBean(@Query("stuid") String id, @Query("passwd") String pwd);

    @GET("weather7d")
    Call<Weather7DayBean> getWeather7DayBean(@Query("city") String city);

    @GET("weather")
    Call<WeatherBean> getWeatherBean(@Query("city") String city);

    @GET("cetquery")
    Call<CETBean> getCETBean(@Query("name") String name, @Query("crtNum") String crtNum);

}
