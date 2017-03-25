package com.mcdull.cert.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import com.google.gson.Gson;
import com.mcdull.cert.Bean.CETBean;
import com.mcdull.cert.Bean.ExamTimeBean;
import com.mcdull.cert.Bean.ReExamBean;
import com.mcdull.cert.Bean.ScoreBean;
import com.mcdull.cert.Bean.WeatherBean;
import com.mcdull.cert.Bean.eCardBean;
import com.mcdull.cert.Bean.eCardOwnerBean;
import com.mcdull.cert.R;
import com.mcdull.cert.activity.CallStudentInClassActivity;
import com.mcdull.cert.activity.CetSearchActivity;
import com.mcdull.cert.activity.ECardActivity;
import com.mcdull.cert.activity.ExamActivity;
import com.mcdull.cert.activity.MapActivity;
import com.mcdull.cert.activity.ReExamActivity;
import com.mcdull.cert.activity.RepairActivity;
import com.mcdull.cert.activity.RepairSucActivity;
import com.mcdull.cert.activity.ScoreActivity;
import com.mcdull.cert.activity.TripActivity;
import com.mcdull.cert.activity.WeatherActivity;
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
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;
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

    //饭卡的两个TextView
    private TextView tv_eCardConsume;
    private TextView tv_eCardBalance;

    //天气的TextView
    private ImageView tv_nowTemperature;
    //用于绘制实时天气
    private Bitmap imgMarker;
    private int width,height;   //图片的高度和宽度
    private Bitmap imgTemp;  //临时标记图
    //CET对话框临时保存的数据
    private String cetName = "";
    private String crtNum = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_new_student, container, false);
        initView();
        waitWin = new ShowWaitPopupWindow(getActivity());
        searchWeather(false);
        user = AVUser.getCurrentUser();
        //获取主界面的一卡通信息
        findECard(true);
        return view;
    }

    private void initView() {

        view.findViewById(R.id.bt_map).setOnClickListener(this);
        view.findViewById(R.id.bt_weather).setOnClickListener(this);
        view.findViewById(R.id.bt_ecard).setOnClickListener(this);
        view.findViewById(R.id.bt_EcardBalance).setOnClickListener(this);
        view.findViewById(R.id.bt_checkEcardPaylist).setOnClickListener(this);
        view.findViewById(R.id.eCardStatus).setBackgroundColor(getActivity().getSharedPreferences("setting", MODE_PRIVATE).getInt("theme", 0xff009688));

        view.findViewById(R.id.bt_examScore).setOnClickListener(this);
        view.findViewById(R.id.bt_reExam).setOnClickListener(this);
        view.findViewById(R.id.bt_examTime).setOnClickListener(this);
        view.findViewById(R.id.bt_pcRepair).setOnClickListener(this);
        view.findViewById(R.id.bt_cetSearch).setOnClickListener(this);
        view.findViewById(R.id.bt_backgroundRepair).setOnClickListener(this);
        view.findViewById(R.id.bt_callInClass).setOnClickListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View v) {
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
                SearchforReExam_Exam_Score(2);
                break;
            case R.id.bt_examTime:
                SearchforReExam_Exam_Score(1);
                break;
            case R.id.bt_reExam:
                SearchforReExam_Exam_Score(0);
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
                StartCallingPeople();
                break;
            case R.id.bt_weather:
                searchWeather(true);
                break;
            case R.id.bt_ecard:
                findECard(false);
                break;
            case R.id.bt_EcardBalance:
                findECard(false);
                break;
            case R.id.bt_checkEcardPaylist:
                findECard(false);
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
                    if (isWeatherButton){
                        new InternetUtil(weatherUIHandler, "http://wthrcdn.etouch.cn/weather_mini", map,true,getActivity());//传入参数
                    }else {
                        new InternetUtil(weatherMainUIHandler, "http://wthrcdn.etouch.cn/weather_mini", map,true,getActivity());//传入参数
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
                //WeatherBean bean = new Gson().fromJson(json, WeatherBean.class);

                Intent intent = new Intent(getActivity(), WeatherActivity.class);
                intent.putExtra("weatherJson", json);
                intent.putExtra("Title","天气情况");
                startActivity(intent);
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
                    Toast.makeText(getActivity(), "查询失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                //用Gson解析天气Json数据
                WeatherBean bean = new Gson().fromJson(json, WeatherBean.class);
                createNowTemperatureDrawable(String.valueOf(bean.data.wendu));
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
                    waitWin.dismissWait();
                }
            }
        });
    }

    Handler CallingPeopleHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                waitWin.dismissWait();
                Toast.makeText(getActivity(), "暂时无法获取，请重试", Toast.LENGTH_SHORT).show();
            } else {
                waitWin.dismissWait();

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
    private void findECard(final boolean isMainMenu) {
        final String studentId = user.getString("StudentId");
        final String eCardPassword = user.getString("EcardPwd");
        if (TextUtils.isEmpty(studentId) || studentId.equals("null")) {
            Toast.makeText(getActivity(), "若需查询一卡通信息，请先在个人信息页补全信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if ((TextUtils.isEmpty(eCardPassword) || eCardPassword.equals("null"))&&studentId.length() == 14) {
            Toast.makeText(getActivity(), "若需查询一卡通信息，请先在个人信息页补全信息", Toast.LENGTH_SHORT).show();
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
                    map.put("passwd", "888888");//设置get参数

                    if (isMainMenu){
                        new InternetUtil(eCardMainMenuHandler,basicURL + "ecard_daytrade", map,true,getActivity());//传入参数
                        new InternetUtil(eCardOwnerMainMenuHandler,basicURL + "ecard_account", map,true,getActivity());//传入参数
                    }else{
                        new InternetUtil(eCardOwnerHandler,basicURL + "ecard_account", map,true,getActivity());//传入参数
                        new InternetUtil(eCardHandler,basicURL + "ecard_daytrade", map,true,getActivity());//传入参数
                        Toast.makeText(getActivity(), "正在查询…", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getActivity(), "查询失败，请检查网络是否顺畅", Toast.LENGTH_SHORT).show();
                    waitWin.dismissWait();
                }
            }
        });
    }



    Handler eCardHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                waitWin.dismissWait();
                Toast.makeText(getActivity(), "一卡通消费数据暂时无法获取", Toast.LENGTH_SHORT).show();
            } else {
                waitWin.dismissWait();

                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");

                if (Util.replace(json).equals("false")) {
                    Toast.makeText(getActivity(), "一卡通消费数据暂时无法获取", Toast.LENGTH_SHORT).show();
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
                            tv_eCardConsume.setText(String.valueOf(dayConsume)+"元");
                        }else {
                            tv_eCardConsume.setText("——");
                            Toast.makeText(getActivity(), "一卡通消费数据暂时无法获取", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                Intent intent = new Intent(getActivity(), ECardActivity.class);
                if (tv_eCardBalance != null){
                    intent.putExtra("eCardBalance",tv_eCardBalance.getText());
                }else {
                    intent.putExtra("eCardBalance","——元");
                }
                intent.putExtra("eCardJson", json);
                startActivity(intent);

            }
        }
    };

    Handler eCardMainMenuHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                waitWin.dismissWait();
                Toast.makeText(getActivity(), "一卡通消费数据暂时无法获取", Toast.LENGTH_SHORT).show();
            } else {
                waitWin.dismissWait();

                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");

                if (Util.replace(json).equals("false")) {
                    Toast.makeText(getActivity(), "一卡通消费数据暂时无法获取", Toast.LENGTH_SHORT).show();
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
                            tv_eCardConsume.setText(String.valueOf(dayConsume)+"元");
                        }else {
                            tv_eCardConsume.setText("——");
                            Toast.makeText(getActivity(), "一卡通消费数据暂时无法获取", Toast.LENGTH_SHORT).show();
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
                waitWin.dismissWait();
            } else {
                waitWin.dismissWait();

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
                            String [] cutData = eCardOwnerData.data.balance.split("（");
                            tv_eCardBalance.setText(cutData[0]);
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
                waitWin.dismissWait();
            } else {
                waitWin.dismissWait();

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
            }
        }
    };


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


    //补考信息/成绩查询/考试安排查询
    private void SearchforReExam_Exam_Score(final int searchId) {
        //该方法查询三项，0为补考信息，1为考试安排查询，2为考试成绩查询
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
                    //map.put("term","2016.1" );//学期，默认为当前
                    if (searchId == 0){
                        new InternetUtil(reExamHandler, basicURL+"bexam", map);//传入参数
                    }else if (searchId == 1){
                        new InternetUtil(ExamTimeHandler, basicURL+"exam", map);//传入参数
                    }else if (searchId == 2){
                        new InternetUtil(ScoreHandler, basicURL+"score", map);//传入参数
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
                    if (bean.data == null){
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
                    if (bean.data == null){
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
                    if (bean.data == null){
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



}
