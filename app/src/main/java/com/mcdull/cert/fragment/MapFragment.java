package com.mcdull.cert.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.mcdull.cert.R;
import com.mcdull.cert.activity.DetailsActivity;
import com.mcdull.cert.domain.Location;

import java.util.ArrayList;

/**
 * Created by mcdull on 15/8/13.
 */
public class MapFragment extends Fragment implements View.OnClickListener, LocationSource, AMap.OnInfoWindowClickListener, AMapLocationListener {
    private View view;
    private MapView mapView;
    private AMap aMap;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private Marker marker;
    private Location Location;
    private MyReceiver receiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_map, container, false);

        initView();

        mapView.onCreate(savedInstanceState);

        initMap();

        //注册广播接收器
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.mcdull.cert.Map");
        getActivity().registerReceiver(receiver, filter);

        return view;
    }

    private void initView() {
        mapView = (MapView) view.findViewById(R.id.map);
        view.findViewById(R.id.bt_back).setOnClickListener(this);
        view.findViewById(R.id.bt_find).setOnClickListener(this);
    }

    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getBundleExtra("bundle");

            int locaBeenId = bundle.getInt("locaBeenId");
            String locaBeenName = bundle.getString("locaBeenName");
            double latitude = bundle.getDouble("latitude");
            double longitude = bundle.getDouble("longitude");

            Location = new Location(locaBeenId,locaBeenName,new LatLng(latitude,longitude));

            addMarker(Location.getName(), Location.getLatLng());

        }
    }

    //添加标记点的方法。。。
    private void addMarker(String title, LatLng latLng) {

        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        giflist.add(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        if (marker != null)
            marker.remove();

        MarkerOptions markerOption = new MarkerOptions().anchor(0.5f, 0.5f).position(latLng).title(title)
                .snippet("").icons(giflist).draggable(true).period(50);
        marker = aMap.addMarker(markerOption);
        marker.showInfoWindow();

        changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, aMap.getCameraPosition().zoom, 0, 0)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_back:
                getActivity().finish();
                break;
            case R.id.bt_find:
                Intent intent = new Intent();
                intent.setAction("com.mcdull.cert.MapMenu");
                getActivity().sendBroadcast(intent);
                break;
        }
    }

    //没事别动。。。
    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            SharedPreferences SP = getActivity().getSharedPreferences("setting",Context.MODE_PRIVATE);
            if (SP.getBoolean("mapType",true)){
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
            }else {
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);// 标准地图模式
            }

            //学校坐标 28.743319 115.868472
            //
            // 自定义系统定位小蓝点
            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                    .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
            myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
            myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
            // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
            myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.setMyLocationRotateAngle(180);
            aMap.setLocationSource(this);// 设置定位监听
            aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            //设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));

            aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器

            changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(28.743319, 115.868472), aMap.getCameraPosition().zoom, 0, 0)));

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
            mapView = null;
        }
        getActivity().unregisterReceiver(receiver);
        aMap = null;
        mListener = null;
        mAMapLocationManager = null;
        marker = null;
        Location = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView!=null){
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView!=null){
            mapView.onPause();
        }
    }

    //定位监听（显示蓝点）
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

        mListener = onLocationChangedListener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
            /*
             * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
            mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, 2000, 10, this);
        }
    }

    @Override
    public void deactivate() {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
//        String title = marker.getTitle();
//        Intent intent = new Intent();
//        intent.setClass(getActivity(), DetailsActivity.class);
//        intent.putExtra("id", Location.getId());
//        intent.putExtra("type", Location.getName());
//        startActivity(intent);
    }

    private void changeCamera(CameraUpdate update) {
        aMap.moveCamera(update);
    }

    //标识是否第一次进入地图页面
    private Boolean isFirst = true;

    //位置变化监听
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (mListener != null && aMapLocation != null) {
            mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
//            marker.setPosition(new LatLng(aMapLocation.getLatitude(), aMapLocation
//                    .getLongitude()));// 定位雷达小图标
            float bearing = aMap.getCameraPosition().bearing;
            aMap.setMyLocationRotateAngle(bearing);// 设置小蓝点旋转角度

            if (isFirst) {
                changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(28.743319, 115.868472), aMap.getCameraPosition().zoom, 0, 0)));
                isFirst = false;
            }

        }
    }

    @Override
    public void onLocationChanged(android.location.Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
