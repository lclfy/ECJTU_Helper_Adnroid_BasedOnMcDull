package com.mcdull.cert.fragment;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import com.google.gson.Gson;
import com.mcdull.cert.Bean.CETBean;
import com.mcdull.cert.Bean.CalenderBean;
import com.mcdull.cert.Bean.ExamTimeBean;
import com.mcdull.cert.Bean.ReExamBean;
import com.mcdull.cert.Bean.ScoreBean;
import com.mcdull.cert.Bean.SelectedCourseIDBean;
import com.mcdull.cert.Bean.Weather7DayBean;
import com.mcdull.cert.Bean.WeatherBean;
import com.mcdull.cert.Bean.eCardBean;
import com.mcdull.cert.Bean.eCardOwnerBean;
import com.mcdull.cert.R;
import com.mcdull.cert.activity.CallStudentInClassActivity;
import com.mcdull.cert.activity.CetSearchActivity;
import com.mcdull.cert.activity.CourseActivity;
import com.mcdull.cert.activity.ECardActivity;
import com.mcdull.cert.activity.ExamActivity;
import com.mcdull.cert.activity.MapActivity;
import com.mcdull.cert.activity.ReExamActivity;
import com.mcdull.cert.activity.RepairActivity;
import com.mcdull.cert.activity.RepairSucActivity;
import com.mcdull.cert.activity.ScoreActivity;
import com.mcdull.cert.activity.SelectedCourseIDActivity;
import com.mcdull.cert.activity.TripActivity;
import com.mcdull.cert.activity.WeatherActivity;
import com.mcdull.cert.adapter.CalenderAdapter;
import com.mcdull.cert.utils.GetIcon;
import com.mcdull.cert.utils.InternetUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.mcdull.cert.utils.ShowSureDialog;
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.Util;
import com.umeng.message.proguard.B;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.track;
import static android.R.attr.width;
import static android.content.Context.MODE_PRIVATE;

public class NewStudentFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ShowWaitPopupWindow waitWin;
    private Intent intent;
    private AVUser user;
    private Activity activity;


    //新的URL
    private String basicURL = "http://api1.ecjtu.org/v1/";

    //饭卡的四个TextView
    private TextView tv_eCardConsume;
    private TextView tv_eCardBalance;
    private TextView tv_eCardConsumeTitle;
    private TextView tv_eCardBalanceTitle;
    //当前温度
    private String nowTemp = "";

    //天气的TextView
    private ImageView tv_nowTemperature;
    //日历的TextView
    private TextView tv_calenderTitle;
    //日历的listview
    private ListView lvCalender;

    //刷新日历的标记，第一次启动的时候不需要修改重试按钮状态
    private boolean isFirstTime = true;
    //判断是否需要查询第二天的日历
    boolean searchNextDays = false;
    //用于绘制实时天气
    private Bitmap imgMarker;
    private int width,height;   //图片的高度和宽度
    private Bitmap imgTemp;  //临时标记图
    //CET对话框临时保存的数据
    private String cetName = "";
    private String crtNum = "";
    //保存一卡通的对象
    private String eCardJson = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_new_student, container, false);
        initView();
        waitWin = new ShowWaitPopupWindow(getActivity());
        searchWeather(false);
        user = AVUser.getCurrentUser();
        //获取主界面的一卡通信息
        findECard(true,false);
        //获取主界面的日历信息
        findCalender(user.getString("StudentId").length() ==16);
        return view;
    }

    protected void nextActivity(Class cls, Bundle bundle, ActivityOptions options) {
        Intent intent = new Intent(getActivity(), cls);
        if (bundle != null)
            intent.putExtra("bundle", bundle);
        if (options != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            startActivity(intent, options.toBundle());
        else
            startActivity(intent);
    }

    private void initView() {
        view.findViewById(R.id.bt_map).setOnClickListener(this);
        view.findViewById(R.id.bt_weather).setOnClickListener(this);
        view.findViewById(R.id.bt_ecard).setOnClickListener(this);
        view.findViewById(R.id.bt_EcardBalance).setOnClickListener(this);
        view.findViewById(R.id.bt_checkEcardPaylist).setOnClickListener(this);
        view.findViewById(R.id.eCardStatus).setBackgroundColor(getActivity().getSharedPreferences("setting", MODE_PRIVATE).getInt("theme", 0xff009688));
        TextView tvCalenderTitle = (TextView)view.findViewById(R.id.calenderArea_title);
        tvCalenderTitle.setTextColor(getActivity().getSharedPreferences("setting", MODE_PRIVATE).getInt("theme", 0xff009688));
        TextView tvAllCourse = (TextView)view.findViewById(R.id.tv_allCourseBtn);
        tvAllCourse.setTextColor(getActivity().getSharedPreferences("setting", MODE_PRIVATE).getInt("theme", 0xff009688));
        TextView retryBtn = (TextView)view.findViewById(R.id.tv_reTryBtn);
        retryBtn.setTextColor(getActivity().getSharedPreferences("setting", MODE_PRIVATE).getInt("theme", 0xff009688));

        tv_eCardConsumeTitle = (TextView) view.findViewById(R.id.tv_consumeTitle);
        tv_eCardBalanceTitle = (TextView) view.findViewById(R.id.tv_balanceTitle);
        //设置跟随主题变换颜色的图标的颜色
        switch (getActivity().getSharedPreferences("setting", MODE_PRIVATE).getInt("themeInt", 2)){
            case 0:
                view.findViewById(R.id.calenderIcon).setBackgroundResource(R.drawable.ic_calendericon_deep_purple);
                break;
            case 1:
                view.findViewById(R.id.calenderIcon).setBackgroundResource(R.drawable.ic_calendericon_pink);
                break;
            case 2:
                view.findViewById(R.id.calenderIcon).setBackgroundResource(R.drawable.ic_calendericon_deep_teal);
                break;
            case 3:
                view.findViewById(R.id.calenderIcon).setBackgroundResource(R.drawable.ic_calendericon_deep_blue);
                break;
            case 4:
                view.findViewById(R.id.calenderIcon).setBackgroundResource(R.drawable.ic_calendericon_deep_amber);
                break;
            case 5:
                view.findViewById(R.id.calenderIcon).setBackgroundResource(R.drawable.ic_calendericon_deep_red);
                break;
        }
        view.findViewById(R.id.bt_examScore).setOnClickListener(this);
        view.findViewById(R.id.bt_reExam).setOnClickListener(this);
        view.findViewById(R.id.bt_examTime).setOnClickListener(this);
        view.findViewById(R.id.bt_pcRepair).setOnClickListener(this);
        view.findViewById(R.id.bt_cetSearch).setOnClickListener(this);
        view.findViewById(R.id.bt_backgroundRepair).setOnClickListener(this);
        view.findViewById(R.id.bt_callInClass).setOnClickListener(this);

        view.findViewById(R.id.bt_selectedcourse_id).setOnClickListener(this);
        view.findViewById(R.id.tv_reTryBtn).setOnClickListener(this);
        view.findViewById(R.id.tv_allCourseBtn).setOnClickListener(this);


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences SP = getActivity().getSharedPreferences("config", MODE_PRIVATE);
        int stuChanged = SP.getInt("stuIDChanged",0);
        int eCardChanged = SP.getInt("eCardPwdChanged",0);
        String stuID = AVUser.getCurrentUser().getString("StudentId");
        //如果一卡通密码改了刷一下一卡通
        if (eCardChanged == 1){
            SharedPreferences.Editor edit = SP.edit();
            findECard(true,true);
            edit.putInt("eCardPwdChanged",0);
        }
        if (stuChanged==1){
            //如果学号改了让它刷新一下界面
            findECard(true,true);
            findCalender(stuID.length()==16);
            SharedPreferences.Editor edit = SP.edit();
            edit.putInt("stuIDChanged",0);
            edit.commit();
            //如果是13,14级学生给个框提醒一下不能查，并且把主页的课表隐藏了
            if (stuID.length()==14){
                view.findViewById(R.id.lv_calenderListView).setVisibility(View.GONE);
                view.findViewById(R.id.scroll_status_bar).setVisibility(View.GONE);
                        /* @setIcon 设置对话框图标
                         * @setTitle 设置对话框标题
                         * @setMessage 设置对话框消息提示
                         * setXXX方法返回Dialog对象，因此可以链式设置属性
                         */
                final AlertDialog.Builder normalDialog =
                        new AlertDialog.Builder(getActivity());
                normalDialog.setTitle("查询不可用提醒");
                normalDialog.setMessage("非常抱歉，由于教务系统的更新，新版本教务系统与旧版已无法通用，受此影响，13/14级同学将仅能使用：\n一卡通信息\n地图信息\n天气信息\n百科信息\n电脑维修\n花椒维权\n四六级查询\n对此造成的不便，我们深表遗憾。");
                normalDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                normalDialog.show();
            }else {
                view.findViewById(R.id.lv_calenderListView).setVisibility(View.VISIBLE);
//                view.findViewById(R.id.scroll_status_bar).setVisibility(View.VISIBLE);
            }

        }

    }


    @Override
    public void onClick(View v) {
        //判断学号
        boolean isEnabled = true;
        if (user.getString("StudentId").length() ==14){
            isEnabled = false;
        }
        if (!InternetUtil.isConnected(getActivity())) {
            Toast.makeText(getActivity(), "请检查网络设置", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (v.getId()){
            case R.id.bt_map:
                intent = new Intent(getActivity(),MapActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_examScore:
                if (isEnabled){
                    universalSearch(2);
                }
                break;
            case R.id.bt_examTime:
                if (isEnabled){
                    universalSearch(1);
                }
                break;
            case R.id.bt_reExam:
                if (isEnabled){
                    universalSearch(0);
                }
                break;
            case R.id.bt_pcRepair:
                getOrderState();
                break;
            case R.id.bt_cetSearch:
                CETSetNameAndID();
                break;
            case R.id.bt_backgroundRepair:
                intent = new Intent(getActivity(), TripActivity.class);
                intent.putExtra("Title", "花椒维权");
                startActivity(intent);
                break;
            case R.id.bt_callInClass:
            //新做的点名器
                if (isEnabled){
                    StartCallingPeople();
                }
                break;
            case R.id.bt_weather:
                searchWeather(true);
                break;
            case R.id.bt_ecard:
            case R.id.bt_EcardBalance:
            case R.id.bt_checkEcardPaylist:
                findECard(false,false);
                break;
            case R.id.bt_selectedcourse_id:
                if (isEnabled){
                    universalSearch(3);
                }
                break;
            case R.id.tv_reTryBtn:
                //重试搜索日历
                if (isEnabled){
                    findCalender(true);
                }

                break;
            case R.id.tv_allCourseBtn:
                Intent intent = new Intent(getActivity(), CourseActivity.class);
                startActivity(intent);
                break;
        }

    }


    //天气查询
    private void searchWeather(final boolean isWeatherButton){
        if (isWeatherButton){
            waitWin.showWait();
        }
        AVQuery<AVObject> query = new AVQuery<>("API");
        query.whereEqualTo("title", "weather");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Map<String, String> map = new ArrayMap<>();
                    map.put("city", "南昌");//设置get参数
                    new InternetUtil(weatherMainUIHandler, basicURL + "weather", map,true,getActivity());//传入参数
                    if (isWeatherButton) {
                        new InternetUtil(weatherUIHandler, basicURL + "weather7d", map, true, getActivity());//传入参数
                    }

                } else {
                    Toast.makeText(getActivity(), "查询失败，请检查网络是否顺畅", Toast.LENGTH_SHORT).show();
                    if (isWeatherButton){
                        waitWin.dismissWait();
                    }
                }
            }
        });


    }



//点击按钮后查询的天气
    Handler weatherUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                waitWin.dismissWait();
                Toast.makeText(getActivity(), "查询失败，请稍后再试", Toast.LENGTH_SHORT).show();
            } else {
                waitWin.dismissWait();

                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");
                if (Util.replace(json).equals("false")) {
                    Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                //用Gson解析天气Json数据
                Weather7DayBean bean = new Weather7DayBean();
                try{
                    bean = new Gson().fromJson(json, Weather7DayBean.class);
                    Intent intent = new Intent(getActivity(), WeatherActivity.class);
                    intent.putExtra("weather7DJson", json);
                    intent.putExtra("nowTemp",nowTemp);
                    intent.putExtra("Title","天气情况");
                    startActivity(intent);
                }catch (Exception e ){
                    try{
                        bean = new Gson().fromJson(json, Weather7DayBean.class);
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather7DJson", json);
                        intent.putExtra("nowTemp",nowTemp);
                        intent.putExtra("Title","天气情况");
                        startActivity(intent);
                    }catch (Exception e1){
                        Toast.makeText(getActivity(), "天气刷新失败", Toast.LENGTH_SHORT).show();
                    }

                }



            }
        }
    };
//在主页面用来更新实时的天气
    Handler weatherMainUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

            } else {

                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");
                if (Util.replace(json).equals("false")) {
                    return;
                }
                //用Gson解析天气Json数据
                WeatherBean bean = new WeatherBean();
                try{
                    bean = new Gson().fromJson(json, WeatherBean.class);
                    nowTemp = bean.data.temp;
                    createNowTemperatureDrawable(String.valueOf(bean.data.temp));
                }catch (Exception e ){
                    try{
                        bean = new Gson().fromJson(json, WeatherBean.class);
                        nowTemp = bean.data.temp;
                        createNowTemperatureDrawable(String.valueOf(bean.data.temp));
                    }catch (Exception e1){
                        return;
                    }

                }

            }
        }
    };

    //用画笔在主界面天气图标上写温度
    private void createNowTemperatureDrawable(String Temperature) {
        //导入图片
        tv_nowTemperature = (ImageView) getActivity().findViewById(R.id.img_weather);
        //tv_nowTemperature.setBackgroundResource(R.drawable.ic_img_weather_nowtime);
        imgMarker = BitmapFactory.decodeResource(getResources(), R.drawable.ic_img_weather_nowtime);
        width = imgMarker.getWidth();
        height = imgMarker.getHeight();
        imgTemp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(imgTemp);
        Paint paint = new Paint(); // 建立画笔
        paint.setDither(true);
        paint.setFilterBitmap(true);
        Rect src = new Rect(0, 0, width, height);
        Rect dst = new Rect(0, 0, width, height);
        canvas.drawBitmap(imgMarker, src, dst, paint);

        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.DEV_KERN_TEXT_FLAG);
        textPaint.setTextSize(150f);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD); // 采用默认的宽度
        textPaint.setColor(Color.WHITE);

        canvas.drawText(Temperature, width/2-90, height/2 + 60,
                textPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        //添加到图片
        tv_nowTemperature.setBackgroundDrawable(new BitmapDrawable(getResources(), imgTemp));

    }

    //点名器
    private void StartCallingPeople() {
        //开启点名器
        final String studentId = user.getString("StudentId");
        final String JwcPwd = user.getString("JwcPwd");
        if (TextUtils.isEmpty(studentId) || studentId.equals("null")) {
            Toast.makeText(getActivity(), "若需点名，请先在个人信息页补全信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(JwcPwd) || JwcPwd.equals("null")) {
            Toast.makeText(getActivity(), "若需点名，请先在个人信息页补全信息", Toast.LENGTH_SHORT).show();
            return;
        }


        AVQuery<AVObject> query = new AVQuery<>("API");
        query.whereEqualTo("title", "ClassList");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Map<String, String> map = new ArrayMap<>();
                    map.put("stuid", studentId);//设置get参数
                    map.put("passwd", JwcPwd);//设置get参数

                    new InternetUtil(CallingPeopleHandler,basicURL + "mates", map,true,getActivity());//传入参数
                    Toast.makeText(getActivity(), "正在搜索学号所属班级…", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "查询失败，请检查网络是否顺畅", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    Handler CallingPeopleHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                Toast.makeText(getActivity(), "暂时无法获取，请重试", Toast.LENGTH_SHORT).show();
            } else {


                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");

                if (Util.replace(json).equals("false")) {
                    Toast.makeText(getActivity(), "暂时无法获取，请重试", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(getActivity(), CallStudentInClassActivity.class);
                intent.putExtra("CallingJson", json);
                startActivity(intent);
            }
        }
    };
    //一卡通查询
    private void findECard(final boolean isMainMenu,boolean isRefresh) {
        if (eCardJson.length()!=0 &&!isRefresh){
            findECardWithLocalData(eCardJson);
            return;
        }
        final String studentId = user.getString("StudentId");
        final String eCardPassword = user.getString("EcardPwd");
        if (TextUtils.isEmpty(studentId) || studentId.equals("null")) {
            Toast.makeText(getActivity(), "若需查询，请先在个人信息页补全信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if ((TextUtils.isEmpty(eCardPassword) || eCardPassword.equals("null"))&&studentId.length() == 14) {
            Toast.makeText(getActivity(), "若需查询，请先在个人信息页补全信息", Toast.LENGTH_SHORT).show();
            return;
        }
        

        AVQuery<AVObject> query = new AVQuery<>("API");
        query.whereEqualTo("title", "eCard");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Map<String, String> map = new ArrayMap<>();
                    map.put("user", studentId);//设置get参数
                    //map.put("user", "73083");//设置get参数
                    //map.put("passwd", eCardPassword);//设置get参数
                    map.put("passwd", eCardPassword);//设置get参数

                    if (isMainMenu){
                        new InternetUtil(eCardMainMenuHandler,basicURL + "ecard_daytrade", map,true,getActivity());//传入参数
                        new InternetUtil(eCardOwnerMainMenuHandler,basicURL + "ecard_account", map,true,getActivity());//传入参数
                    }else{
                        if (eCardJson!=null &&eCardJson.length()!=0){

                            if (tv_eCardBalance != null){
                                intent.putExtra("eCardBalance",tv_eCardBalance.getText());
                            }else {
                                new InternetUtil(eCardOwnerHandler,basicURL + "ecard_account", map,true,getActivity());//传入参数
                                intent.putExtra("eCardBalance",tv_eCardBalance.getText());
                                //intent.putExtra("eCardBalance","——元");
                            }
                        }
                        new InternetUtil(eCardOwnerHandler,basicURL + "ecard_account", map,true,getActivity());//传入参数
                        new InternetUtil(eCardHandler,basicURL + "ecard_daytrade", map,true,getActivity());//传入参数
                        Toast.makeText(getActivity(), "正在查询…", Toast.LENGTH_SHORT).show();
                    }

                } else {
//                    Toast.makeText(getActivity(), "查询失败，请检查网络是否顺畅", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void findECardWithLocalData(String json){
        eCardJson = json;
        Bundle b = new Bundle();
        if (tv_eCardBalance != null){
            b.putSerializable("eCardBalance", tv_eCardBalance.getText().toString());
        }else {
            b.putSerializable("eCardBalance","——元");
        }
        tv_eCardBalanceTitle = (TextView)view.findViewById(R.id.tv_balanceTitle);
        tv_eCardConsumeTitle = (TextView)view.findViewById(R.id.tv_consumeTitle);
        tv_eCardBalance = (TextView)view.findViewById(R.id.tv_eCardBalance);
        tv_eCardConsume = (TextView)view.findViewById(R.id.tv_eCardConsume);
        b.putSerializable("eCardJson", json);
        ActivityOptions options = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                    Pair.create((View) tv_eCardBalanceTitle, "balanceTitle"),
                    Pair.create((View) tv_eCardConsumeTitle, "consumeTitle"),
                    Pair.create((View) tv_eCardBalance, "balance"),
                    Pair.create((View) tv_eCardConsume, "consume"));
        }
        nextActivity(ECardActivity.class, b, options);
    }


    Handler eCardHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                Toast.makeText(getActivity(), "获取一卡通数据失败\n点击消费详情以重试\n如多次失败，请校验一卡通密码", Toast.LENGTH_SHORT).show();
            } else {


                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");

                if (Util.replace(json).equals("false")) {
                    Toast.makeText(getActivity(), "获取一卡通数据失败\n点击消费详情以重试\n如多次失败，请校验一卡通密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //更新主页面的信息
                eCardBean eCardDataIn = new eCardBean();
                try {
                    eCardDataIn = new Gson().fromJson(json, eCardBean.class);
                }catch (Exception e){

                }
                if (eCardDataIn!=null) {
                    if(eCardDataIn.msg.contains("不匹配")){
                        Toast.makeText(getActivity(), "查询一卡通数据失败：一卡通密码错误", Toast.LENGTH_SHORT).show();
                    }else {
                        tv_eCardConsume = (TextView)view.findViewById(R.id.tv_eCardConsume);
                        float dayConsume = 0;
                        if (eCardDataIn.data != null){
                            for (int item = 0;item<eCardDataIn.data.size();item++) {
                                //获取当日每次消费金额以获取总消费
                                eCardBean.ChildECardBean consumeLog = eCardDataIn.data.get(item);
                                //去除充值金额后的消费金额的绝对值累计
                                if (Float.parseFloat(consumeLog.consume) < 0){
                                    dayConsume += Math.abs(Float.parseFloat(consumeLog.consume));
                                }
                            }
                            tv_eCardConsume.setText(String.valueOf(dayConsume)+"元");
                        }else {
                            tv_eCardConsume.setText("——");
                            Toast.makeText(getActivity(), "获取一卡通数据失败\n点击消费详情以重试\n如多次失败，请校验一卡通密码", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                findECardWithLocalData(json);

            }
        }
    };

    Handler eCardMainMenuHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                Toast.makeText(getActivity(), "获取一卡通数据失败\n点击消费详情以重试\n如多次失败，请校验一卡通密码", Toast.LENGTH_SHORT).show();
            } else {


                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");

                if (Util.replace(json).equals("false")) {
                    Toast.makeText(getActivity(), "获取一卡通数据失败\n点击消费详情以重试\n如多次失败，请校验一卡通密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                //更新主页面的信息
                eCardBean eCardData = new eCardBean();
                try {
                    eCardData = new Gson().fromJson(json, eCardBean.class);
                }catch (Exception e){

                }

                if (eCardData!=null) {
                    if(eCardData.msg.contains("不匹配")){
                        Toast.makeText(getActivity(), "查询一卡通数据失败：一卡通密码错误", Toast.LENGTH_SHORT).show();
                    }else {
                        tv_eCardConsume = (TextView)view.findViewById(R.id.tv_eCardConsume);
                        float dayConsume = 0;
                        if (eCardData.data != null){
                            for (int item = 0;item<eCardData.data.size();item++) {
                                //获取当日每次消费金额以获取总消费
                                eCardBean.ChildECardBean consumeLog = eCardData.data.get(item);
                                //去除充值金额后的消费金额的绝对值累计
                                if (Float.parseFloat(consumeLog.consume) < 0){
                                    dayConsume += Math.abs(Float.parseFloat(consumeLog.consume));
                                }
                            }
                            eCardJson = json;
                            tv_eCardConsume.setText(String.valueOf(dayConsume)+"元");
                        }else {
                            tv_eCardConsume.setText("——");
                            Toast.makeText(getActivity(), "获取一卡通数据失败\n点击消费详情以重试\n如多次失败，请校验一卡通密码", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    };



    Handler eCardOwnerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

            } else {


                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");

                if (Util.replace(json).equals("false")) {
                    return;
                }
                //更新主页面的信息
                eCardOwnerBean eCardOwnerData = new eCardOwnerBean();
                try {
                    eCardOwnerData = new Gson().fromJson(json, eCardOwnerBean.class);
                }catch (Exception e){

                }
                if (eCardOwnerData!=null) {
                    if(eCardOwnerData.msg.contains("不匹配")){

                    }else {
                        tv_eCardBalance = (TextView)view.findViewById(R.id.tv_eCardBalance);
                        if (eCardOwnerData.data != null){
                            try{
                                String [] cutData = eCardOwnerData.data.balance.split("（");
                                tv_eCardBalance.setText(cutData[0]);
                            }catch (Exception e){
                                tv_eCardBalance.setText("——");
                            }
                        }else {
                            tv_eCardBalance.setText("——");
                        }
                    }
                }
            }
        }
    };

    Handler eCardOwnerMainMenuHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

            } else {


                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");

                if (Util.replace(json).equals("false")) {
                    return;
                }
                //更新主页面的信息
                eCardOwnerBean eCardOwnerData = new eCardOwnerBean();
                try {
                    eCardOwnerData = new Gson().fromJson(json, eCardOwnerBean.class);
                }catch (Exception e){

                }
                if (eCardOwnerData!=null) {
                    if(eCardOwnerData.msg.contains("不匹配")){

                    }else {
                        tv_eCardBalance = (TextView)view.findViewById(R.id.tv_eCardBalance);
                        if (eCardOwnerData.data != null){
                            try {
                                String [] cutData = eCardOwnerData.data.balance.split("（");
                                tv_eCardBalance.setText(cutData[0]);

                            }catch (Exception e){

                            }

                        }else {
                            tv_eCardBalance.setText("——");
                        }
                    }
                }
                //借地方更新一下13,14级学生的当日课表标题…
                if (AVUser.getCurrentUser().getString("StudentId").length() ==14){
                    TextView tv_calenderTitle = (TextView)view.findViewById(R.id.calenderArea_title);
                    tv_calenderTitle.setText("你好~"+AVUser.getCurrentUser().getString("Name")+"。");
                    return;
                }
            }
        }
    };

    //今日课表-校园日历
    private void findCalender(boolean isEnabled) {
        //13,14级不开放
        if (!isEnabled){
            return;
        }
        if (!isFirstTime){
            //设置主界面日历的标题
            tv_calenderTitle = (TextView)view.findViewById(R.id.calenderArea_title);

            tv_calenderTitle.setText("加载当日课表…");

            //把重试按钮隐藏
            view.findViewById(R.id.tv_reTryBtn).setVisibility(View.GONE);
        }else {
            //第一次进来的时候判断要不要查明天的
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            //把日期和时间区分开
            String[] date_Time = formatter.format(curDate).split(" ");
            if (Integer.parseInt(date_Time[1].split(":")[0]) >= 20) {
                //8点以后查明天的
                searchNextDays = !searchNextDays;
            }
            isFirstTime = !isFirstTime;

        }

        final String studentId = user.getString("StudentId");
        final String JwcPwd = user.getString("JwcPwd");
        if (TextUtils.isEmpty(studentId) || studentId.equals("null")) {
            return;
        }
        if (TextUtils.isEmpty(JwcPwd) || JwcPwd.equals("null")) {
            return;
        }


        AVQuery<AVObject> query = new AVQuery<>("API");
        query.whereEqualTo("title", "Calender");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Map<String, String> map = new ArrayMap<>();
                    //判断要不要查询下一天的内容,如果需要将会返回第二天的日期
                    String shouldSearchNextDay = shouldSearchNextDay_Calender();
                    if (shouldSearchNextDay.length()!=0){
                        map.put("date",shouldSearchNextDay);
                    }
                    map.put("stuid", studentId);//设置get参数
                    map.put("passwd", JwcPwd);//设置get参数
                    //判断查今天的还是明天的
                    new InternetUtil(CalenderHandler,basicURL + "today", map,true,getActivity());//传入参数

                } else {
                    //Toast.makeText(getActivity(), "查询失败，请检查网络是否顺畅", Toast.LENGTH_SHORT).show();
                    tv_calenderTitle.setText("加载失败");
                    //显示重试按钮和全部课表按钮
                    view.findViewById(R.id.tv_reTryBtn).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.tv_allCourseBtn).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    Handler CalenderHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //设置主界面日历的标题
            tv_calenderTitle = (TextView)view.findViewById(R.id.calenderArea_title);
            if (msg.what == 0) {
                //Toast.makeText(getActivity(), "查询失败，请检查网络是否顺畅", Toast.LENGTH_SHORT).show();
                tv_calenderTitle.setText("加载失败");
                //显示重试按钮
                view.findViewById(R.id.tv_allCourseBtn).setVisibility(View.VISIBLE);
                view.findViewById(R.id.tv_reTryBtn).setVisibility(View.VISIBLE);
            } else {

                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");


                if (Util.replace(json).equals("false")) {
                    //Toast.makeText(getActivity(), "查询失败，请检查网络是否顺畅", Toast.LENGTH_SHORT).show();
                    tv_calenderTitle.setText("加载失败");
                    //显示重试按钮
                    view.findViewById(R.id.tv_reTryBtn).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.tv_allCourseBtn).setVisibility(View.VISIBLE);
                    return;
                }
                //更新主页面的信息
                CalenderBean CalenderData = new CalenderBean();
                try {
                    CalenderData = new Gson().fromJson(json, CalenderBean.class);
                }catch (Exception e){
                    try {
                        CalenderData = new Gson().fromJson(json, CalenderBean.class);
                    }catch (Exception e1){
                        tv_calenderTitle.setText("加载失败");
                        //显示重试按钮
                        view.findViewById(R.id.tv_reTryBtn).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.tv_allCourseBtn).setVisibility(View.VISIBLE);
                    }
                }
                if (CalenderData!=null) {
                    if (CalenderData.data!=null){
                        //判断星期
                        String weekday = "";
                        switch (CalenderData.data.weekday){
                            case "1":
                                weekday = "一";
                                break;
                            case "2":
                                weekday = "二";
                                break;
                            case "3":
                                weekday = "三";
                                break;
                            case "4":
                                weekday = "四";
                                break;
                            case "5":
                                weekday = "五";
                                break;
                            case "6":
                                weekday = "六";
                                break;
                            case "7":
                                weekday = "日";
                                break;
                        }
                        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
                        //字幕君
                        String day = "";
                        if (searchNextDays){
                            day = "明";
                        }else {
                            day = "今";
                        }

                        tv_calenderTitle.setText(day+"天是第"+CalenderData.data.week+"周，星期"+weekday);
                        if (CalenderData.data.daylist.size()!=0){
                            for (int item = 0;item<CalenderData.data.daylist.size();item++) {
                                //获取每个项目
                                CalenderBean.ChildCalenderBean.GrandChildCalenderBean CalenderDetails = CalenderData.data.daylist.get(item);
                                //把arraylist填充成list
                                //暂时不知道pktype的意思
                                Map<String, String> CalenderMap = new ArrayMap<>();
                                CalenderMap.put("course", CalenderDetails.course);
                                CalenderMap.put("classRoom", CalenderDetails.classRoom);
                                CalenderMap.put("classString", CalenderDetails.classString);
                                list.add(CalenderMap);
                            }
                            //用一个透明栏来…有偏移
                            //每行课需要35，一行时候不需要
                            ViewGroup.LayoutParams para = view.findViewById(R.id.scroll_status_bar).getLayoutParams();//获取偏移量顶置部件的布局
                            final float scale = view.getResources().getDisplayMetrics().density;
                            //dp换算成px
                            para.height=(CalenderData.data.daylist.size()-2)*(int) (40 * scale + 0.5f);//修改高度

                            view.findViewById(R.id.scroll_status_bar).setVisibility(View.VISIBLE);
                            lvCalender = (ListView)view.findViewById(R.id.lv_calenderListView);
                            CalenderAdapter Adapter = new CalenderAdapter(getActivity(), list);
                            lvCalender.setEnabled(false);
                            lvCalender.setAdapter(Adapter);
                            //不管怎么样都要显示这个按钮
                            view.findViewById(R.id.tv_allCourseBtn).setVisibility(View.VISIBLE);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (Util.dip2px(getActivity(), 56) + 1) * list.size());
                            lvCalender.setLayoutParams(layoutParams);
                        }else {
                            tv_calenderTitle.setText("加载失败");
                            //显示重试按钮
                            view.findViewById(R.id.tv_reTryBtn).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.tv_allCourseBtn).setVisibility(View.VISIBLE);
                        }
                    }


                }
            }
        }
    };

    //查明天的课表
    private String shouldSearchNextDay_Calender() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        //把日期和时间区分开
        String[] date_Time = formatter.format(curDate).split(" ");
        //获取小时
        if (searchNextDays){
            String[] splitday = date_Time[0].split("-");
            String yyyy = splitday[0];
            String mm = splitday[1];
            String dd = splitday[2];
            if (Integer.parseInt(mm) == 2 && Integer.parseInt(dd)==28){
                //2月特殊情况
                if (Integer.parseInt(yyyy)%4 == 0){
                    dd = "29";
                }else {
                    mm = "3";
                    dd = "1";
                }
                return yyyy + "-" + mm + "-"+dd;
            }else if (Integer.parseInt(dd)==31){
                if (Integer.parseInt(mm) == 12){
                    //年底特殊情况（不过年底应该都没有课了吧。。）
                    yyyy = String.valueOf(Integer.parseInt(yyyy)+1);
                    mm = "1";
                    dd = "1";
                }
                //31号的下一天一定是下个月了。。
                mm = String.valueOf(Integer.parseInt(mm)+1);
                dd = "1";
                return yyyy + "-" + mm + "-"+dd;
            }else if (Integer.parseInt(dd)==30){
                if (Integer.parseInt(mm) == 4 ||
                        Integer.parseInt(mm) == 6||
                        Integer.parseInt(mm) == 9||
                        Integer.parseInt(mm) == 11){
                    //30号的下一天是下个月的情况
                    mm = String.valueOf(Integer.parseInt(mm)+1);
                    dd = "1";
                    return yyyy + "-" + mm + "-"+dd;
                }else {
                    //正常情况下查第二天的
                    return yyyy + "-" + mm + "-" + String.valueOf(Integer.parseInt(dd) + 1);
                }
            }else {
                //正常情况下查第二天的
                return yyyy + "-" + mm + "-" + String.valueOf(Integer.parseInt(dd) + 1);
            }
        }
        return "";

    }


    //下方功能按钮类
    //电脑维修
    private void getOrderState() {
        waitWin.showWait();
        new InternetUtil(orderStateHandle, "http://cms.ecjtu.org/index.php?s=/addon/FaultRepair/FaultRepair/api/act/getOrderInfo/student_id/" + AVUser.getCurrentUser().getString("StudentId"));
    }

    Handler orderStateHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                waitWin.dismissWait();
                Toast.makeText(getActivity(), "链接网络失败，请稍后再试", Toast.LENGTH_SHORT).show();
            } else {
                waitWin.dismissWait();
                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");
                if (Util.replace(json).length() == 2) {
                    getOrderTime();
                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        String time = jsonObject.getString("dateTime");
                        intent = new Intent(getActivity(), RepairSucActivity.class);
                        intent.putExtra("time", time);
                        startActivity(intent);
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(),"欸,服务器开小差了...",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }
        }
    };

    private void getOrderTime() {
        waitWin.showWait();
        AVQuery<AVObject> query = new AVQuery<>("API");
        query.whereEqualTo("title", "orderTime");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    new InternetUtil(orderTimeHandle, list.get(0).getString("url"));//传入参数
                } else {
                    Toast.makeText(getActivity(), "查询失败，请检查网络是否顺畅", Toast.LENGTH_SHORT).show();
                    waitWin.dismissWait();
                }
            }
        });
    }


    Handler orderTimeHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                waitWin.dismissWait();
                Toast.makeText(getActivity(), "链接网络失败，请稍后再试", Toast.LENGTH_SHORT).show();
            } else {
                waitWin.dismissWait();
                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");

                String string = null;
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    string = jsonObject.getString("json");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(string) || Util.replace(string).equals("null")) {
                    Toast.makeText(getActivity(), "近期暂不支持报修服务.", Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(getActivity(), RepairActivity.class);
                    intent.putExtra("TimeJson", json);
                    startActivity(intent);
                }
            }
        }
    };

    //弹一个输入框给输入一下准考证号和姓名
    private void CETSetNameAndID(){
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View textEntryView = factory.inflate(R.layout.alert_cet, null);
        final EditText editTextName = (EditText) textEntryView.findViewById(R.id.editTextName);
        final EditText editTextNumEditText = (EditText)textEntryView.findViewById(R.id.editTextNum);
        if (cetName.length()!= 0){
            editTextName.setText(cetName);
        }if (crtNum.length()!= 0){
            editTextNumEditText.setText(crtNum);
        }
        AlertDialog.Builder ad1 = new AlertDialog.Builder(getActivity());
        ad1.setTitle("请输入姓名与准考证号");
        ad1.setIcon(R.drawable.ic_img_cet);
        ad1.setView(textEntryView);
        ad1.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                //临时保存一下，避免对话框消失了数据也没了
                cetName = editTextName.getText().toString();
                crtNum = editTextNumEditText.getText().toString();
                if (editTextName.getText().toString().length()==0 ||
                        editTextNumEditText.getText().toString().length() == 0){
                    Toast.makeText(getActivity(), "请填写信息", Toast.LENGTH_SHORT).show();
                }else {
                    //判断姓名中是否有英文数字
                    Pattern p = Pattern.compile("[0-9]*");
                    Matcher m = p.matcher(editTextName.getText().toString());
                    if(m.matches() ){
                        Toast.makeText(getActivity(), "请填写正确的姓名", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    p=Pattern.compile("[a-zA-Z]");
                    m=p.matcher(editTextName.getText().toString());
                    if(m.matches()){
                        Toast.makeText(getActivity(), "请填写正确的姓名", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Map<String, String> map = new ArrayMap<String, String>();
//                    测试用数据
//                    map.put("name","丁睿玄");
//                    map.put("crtNum","360040162206803");
                    map.put("name",editTextName.getText().toString());
                    map.put("crtNum",editTextNumEditText.getText().toString());
                    SearchforCET(map);
                }
            }
        });
        ad1.setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框
    }

    //四六级查询
    private void SearchforCET(final Map<String, String> map) {

        waitWin.showWait();
        AVQuery<AVObject> query = new AVQuery<>("API");
        query.whereEqualTo("title", "CET");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    new InternetUtil(CETSearchHandler, basicURL+"cetquery", map);//传入参数


                } else {
                    Toast.makeText(getActivity(), "查询失败，请检查网络是否顺畅", Toast.LENGTH_SHORT).show();
                    waitWin.dismissWait();
                }
            }
        });
    }

    //补考
    Handler CETSearchHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                waitWin.dismissWait();
                Toast.makeText(getActivity(), "查询失败，请稍后重试", Toast.LENGTH_SHORT).show();
            } else {
                waitWin.dismissWait();

                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");
                CETBean bean = new CETBean();
                try {
                    bean = new Gson().fromJson(json, CETBean.class);
                }catch (Exception e){
                    Toast.makeText(getActivity(), "查询失败，请重试", Toast.LENGTH_SHORT).show();
                    waitWin.dismissWait();
                    return;
                }
                if (bean.msg.contains("success")){
                    if (bean.data == null){
                        Toast.makeText(getActivity(), bean.msg, Toast.LENGTH_SHORT).show();
                        waitWin.dismissWait();
                    }else {
                        Intent intent = new Intent(getActivity(), CetSearchActivity.class);
                        intent.putExtra("CETJson", json);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(getActivity(), "请检查准考证号与姓名后重试", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };


    //补考信息/成绩查询/考试安排查询//小班序号查询
    private void universalSearch(final int searchId) {
        //该方法查询三项，0为补考信息，1为考试安排查询，2为考试成绩查询,3为小班序号查询
        final String studentId = user.getString("StudentId");
        final String JwcPassword = user.getString("JwcPwd");
        if ( TextUtils.isEmpty(studentId) || studentId.equals("null")) {
            Toast.makeText(getActivity(), "请先在个人信息页补全信息", Toast.LENGTH_SHORT).show();
            return;
        }

        waitWin.showWait();
        AVQuery<AVObject> query = new AVQuery<>("API");
        query.whereEqualTo("title", "reExam");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Map<String, String> map = new ArrayMap<>();
                    map.put("stuid", studentId);//设置get参数
                    map.put("passwd",JwcPassword );//设置get参数
//                    map.put("term","2016.1" );//学期，默认为当前
                    if (searchId == 0){
                        new InternetUtil(reExamHandler, basicURL+"bexam", map);//传入参数
                    }else if (searchId == 1){
                        new InternetUtil(ExamTimeHandler, basicURL+"exam", map);//传入参数
                    }else if (searchId == 2){
                        new InternetUtil(ScoreHandler, basicURL+"score", map);//传入参数
                    }else if (searchId == 3){
                        new InternetUtil(selectedCourseIDHandler, basicURL+"selectnumber", map);//传入参数
                    }

                } else {
                    Toast.makeText(getActivity(), "查询失败，请检查网络是否顺畅", Toast.LENGTH_SHORT).show();
                    waitWin.dismissWait();
                }
            }
        });
    }
    //补考
    Handler reExamHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                waitWin.dismissWait();
                Toast.makeText(getActivity(), "查询失败，请稍后重试", Toast.LENGTH_SHORT).show();
            } else {
                waitWin.dismissWait();

                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");
                ReExamBean bean = new ReExamBean();
                try {
                    bean = new Gson().fromJson(json, ReExamBean.class);
                }catch (Exception e){
                    Toast.makeText(getActivity(), "查询失败，请重试", Toast.LENGTH_SHORT).show();
                    waitWin.dismissWait();
                    return;
                }
                if (bean.msg.contains("success")){
                    if (bean.data == null ||bean.data.data == null){
                        Toast.makeText(getActivity(), "无记录：当前无补考信息或无挂科", Toast.LENGTH_SHORT).show();
                        waitWin.dismissWait();
                    }else {
                        Intent intent = new Intent(getActivity(), ReExamActivity.class);
                        intent.putExtra("reExamJson", json);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(getActivity(), "请检查学号密码是否正确后重试", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };
    //考试安排
    Handler ExamTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                waitWin.dismissWait();
                Toast.makeText(getActivity(), "查询失败，可能当前无考试安排", Toast.LENGTH_SHORT).show();
            } else {
                waitWin.dismissWait();

                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");
                ExamTimeBean bean = new ExamTimeBean();
                try {
                    bean = new Gson().fromJson(json, ExamTimeBean.class);
                }catch (Exception e){
                    Toast.makeText(getActivity(), "查询失败，可能当前无考试安排", Toast.LENGTH_SHORT).show();
                    waitWin.dismissWait();
                    return;
                }

                if (bean.msg.contains("success")){
                    if (bean.data == null||bean.data.data == null){
                        Toast.makeText(getActivity(), "无记录：当前无考试安排", Toast.LENGTH_SHORT).show();
                        waitWin.dismissWait();
                    }else {
                        Intent intent = new Intent(getActivity(), ExamActivity.class);
                        intent.putExtra("ExamJson", json);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(getActivity(), "请检查学号密码是否正确后重试", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };

    //成绩查询
    Handler ScoreHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                waitWin.dismissWait();
                Toast.makeText(getActivity(), "查询失败，请稍后重试", Toast.LENGTH_SHORT).show();
            } else {
                waitWin.dismissWait();

                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");
                ScoreBean bean = new ScoreBean();
                try {
                    bean = new Gson().fromJson(json, ScoreBean.class);
                }catch (Exception e){
                    Toast.makeText(getActivity(), "查询失败，当前学期可能未公布成绩", Toast.LENGTH_SHORT).show();
                    waitWin.dismissWait();
                    return;
                }

                if (bean.msg.contains("success")){
                    if (bean.data == null || bean.data.score == null){
                        Toast.makeText(getActivity(), "无记录：网络或服务器错误", Toast.LENGTH_SHORT).show();
                        waitWin.dismissWait();
                    }else {
                        Intent intent = new Intent(getActivity(), ScoreActivity.class);
                        intent.putExtra("ScoreJson", json);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(getActivity(), "请检查学号密码是否正确后重试", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };
    //小班序号
    Handler selectedCourseIDHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                waitWin.dismissWait();
                Toast.makeText(getActivity(), "查询失败，请稍后重试", Toast.LENGTH_SHORT).show();
            } else {
                waitWin.dismissWait();

                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");
                SelectedCourseIDBean bean = new SelectedCourseIDBean();
                try {
                    bean = new Gson().fromJson(json, SelectedCourseIDBean.class);
                }catch (Exception e){
                    Toast.makeText(getActivity(), "查询失败，请重试", Toast.LENGTH_SHORT).show();
                    waitWin.dismissWait();
                    return;
                }
                if (bean.msg.contains("success")){
                    if (bean.data == null ||bean.data.number == null){
                        Toast.makeText(getActivity(), "无记录：请重试", Toast.LENGTH_SHORT).show();
                        waitWin.dismissWait();
                    }else {
                        Intent intent = new Intent(getActivity(), SelectedCourseIDActivity.class);
                        intent.putExtra("sCIDJson", json);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(getActivity(), "请检查学号密码是否正确后重试", Toast.LENGTH_SHORT).show();
                }

            }
        }
    };



}
