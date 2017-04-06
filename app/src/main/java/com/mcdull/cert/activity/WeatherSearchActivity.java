package com.mcdull.cert.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.google.gson.Gson;
import com.mcdull.cert.Bean.WeatherBean;
import com.mcdull.cert.R;
import com.mcdull.cert.utils.InternetUtil;
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.Util;

import java.util.List;
import java.util.Map;

/**
 * Created by 75800 on 2017/3/20.
 */

public class WeatherSearchActivity extends AppCompatActivity {

    private String basicURL = "http://api1.ecjtu.org/v1/";
    private String selectedItemInBack = "";

    private String nowTemp = "";

    private ShowWaitPopupWindow waitWin;
    private String[] mStrs = {"北京", "天津","石家庄","唐山","秦皇岛","邯郸",
            "邢台","保定","张家口","承德","沧州","廊坊","衡水","太原","大同",
            "阳泉","长治","晋城","朔州","呼和浩特","包头","乌海","赤峰","通辽",
            "沈阳","大连","鞍山","抚顺","本溪","丹东","锦州","营口","阜新",
            "辽阳","盘锦","铁岭","朝阳","葫芦岛","长春","吉林","四平","辽源",
            "通化","白山","松原","白城","哈尔滨","齐齐哈尔","鸡西","鹤岗",
            "双鸭山","大庆","伊春","佳木斯","七台河","牡丹江","黑河","上海",
            "南京","无锡","徐州","常州","苏州","南通","连云港","淮阴","盐城",
            "扬州","镇江","泰州","宿迁","杭州","宁波","温州","嘉兴","湖州",
            "绍兴","金华","舟山","衢州","台州","合肥","芜湖","蚌埠","淮南",
            "马鞍山","淮北","铜陵","安庆","黄山","滁州","阜阳","宿州","巢湖",
            "六安","福州","三明","厦门","莆田","泉州","漳州","南平","南昌",
            "景德镇","萍乡","九江","新余","鹰潭","赣州","丰城","吉安","高安",
            "上饶","樟树","瑞昌","贵溪","井冈山","济南","青岛","淄博","枣庄",
            "东营","烟台","潍坊","日照","济宁","泰安","威海","莱芜","临沂",
            "德州","聊城","郑州","开封","洛阳","平顶山","安阳","鹤壁","新乡",
            "焦作","濮阳","许昌","漯河","三门峡","南阳","商丘","信阳","武汉",
            "驻马店","黄石","十堰","孝感","襄樊","鄂州","荆门","孝感","荆州",
            "黄冈","咸宁","恩施","麻城","利川","天门","广水","长沙","株洲",
            "湘潭","衡阳","邵阳","岳阳","常德","张家界","益阳","郴州","永州",
            "怀化","娄底","广州","韶关","深圳","珠海","汕头","佛山","江门",
            "湛江","茂名","肇庆","惠州","梅州","汕尾","河源","阳江","清远",
            "东莞","中山","潮州","揭阳","云浮","南宁","柳州","桂林","梧州",
            "北海","防城港","钦州","贵港","玉林","海口","三亚","重庆","成都",
            "自贡","攀枝花","泸州","德阳","绵阳","广元","遂宁","内江","乐山",
            "南充","宜宾","广安","达州","都江堰","峨眉山","西昌","江油","贵阳",
            "六盘水","遵义","安顺","铜仁","昆明","曲靖","玉溪","安宁","昭通",
            "楚雄","大理","西安","铜川","宝鸡","咸阳","渭南","延安","汉中",
            "榆林","兰州","嘉峪关","金昌","白银","天水","酒泉","敦煌","张掖",
            "西宁","格尔木","武威","德令哈","银川","石嘴山","吴忠","乌鲁木齐",
            "克拉玛依","吐鲁番","哈密","库尔勒","塔城","喀什","阿克苏","和田"};
    private SearchView mSearchView;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        waitWin = new ShowWaitPopupWindow(WeatherSearchActivity.this);
        setContentView(R.layout.activity_weathersearch);
        mSearchView = (SearchView) findViewById(R.id.searchView);
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrs));
        mListView.setTextFilterEnabled(true);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //获得选中项的HashMap对象
                String selectedItem =(String) mListView.getItemAtPosition(arg2);
                Toast.makeText(getApplicationContext(),
                        "正在更新"+selectedItem+"天气…",
                        Toast.LENGTH_SHORT).show();
                searchWeather();
                selectedItemInBack = selectedItem;

            }

        });

        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }});

    }

    //根据选中的项目查询天气
    private void searchWeather(){
        waitWin.showWait();
        AVQuery<AVObject> query = new AVQuery<>("API");
        query.whereEqualTo("title", "weather");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Map<String, String> map = new ArrayMap<>();
                    map.put("city", selectedItemInBack);//设置get参数
                    new InternetUtil(nowTempHandler, basicURL+"weather", map,true,WeatherSearchActivity.this);//传入参数

                } else {
                    Toast.makeText(WeatherSearchActivity.this, "查询失败，请检查网络是否顺畅", Toast.LENGTH_SHORT).show();
                    waitWin.dismissWait();
                }
            }
        });


    }


    Handler weatherUIHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                waitWin.dismissWait();
                Toast.makeText(WeatherSearchActivity.this, "查询失败，请稍后再试", Toast.LENGTH_SHORT).show();
            } else {
                waitWin.dismissWait();

                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");
                if (Util.replace(json).equals("false")) {
                    Toast.makeText(WeatherSearchActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                //用Gson解析天气Json数据
                //WeatherBean bean = new Gson().fromJson(json, WeatherBean.class);

                Intent intent = getIntent();
                intent.putExtra("weather7DJson", json);
                intent.putExtra("nowTemp",nowTemp);
                setResult(Activity.RESULT_OK, intent);//返回之前页面
                finish();

            }
        }
    };

    Handler nowTempHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                waitWin.dismissWait();
                Toast.makeText(WeatherSearchActivity.this, "查询失败，请稍后再试", Toast.LENGTH_SHORT).show();
            } else {
                waitWin.dismissWait();

                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");
                if (Util.replace(json).equals("false")) {
                    Toast.makeText(WeatherSearchActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                //用Gson解析天气Json数据
                WeatherBean bean = new Gson().fromJson(json, WeatherBean.class);
                nowTemp = bean.data.temp;
                Map<String, String> map = new ArrayMap<>();
                map.put("city", selectedItemInBack);//设置get参数
                new InternetUtil(weatherUIHandler, basicURL+ "weather7d", map,true,WeatherSearchActivity.this);//传入参数


            }
        }
    };
}