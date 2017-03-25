package com.mcdull.cert.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import com.mcdull.cert.R;
import com.mcdull.cert.adapter.MapMenuAdapter;
import com.mcdull.cert.domain.Location;
import com.mcdull.cert.domain.SchoolLoc;
import com.mcdull.cert.fragment.MapFragment;

import java.util.LinkedList;

public class MapActivity extends FragmentActivity implements CompoundButton.OnCheckedChangeListener, OnItemClickListener, DrawerLayout.DrawerListener, View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mLeftMenu;
    private LinkedList<Location> selectList;
    private CheckBox mCbBei;
    private CheckBox mCbNan;
    private CheckBox mCbStay;
    private CheckBox mCbStudy;
    private CheckBox mCbLife;
    private CheckBox mCbPlay;
    private CheckBox mCbOther;
    private MapMenuAdapter adapter;
    private ListView mList;
    private MyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftMenu = (RelativeLayout) findViewById(R.id.left_drawer);
        mLeftMenu.setOnClickListener(this);

        FragmentManager fragmentManager = getSupportFragmentManager();

        MapFragment mapFragment = new MapFragment();
        fragmentManager.beginTransaction().replace(R.id.framelayout, mapFragment).commit();

        selectList = null;
        selectList = initList();
        initView();
        initCheckBox();

        mList.setOnItemClickListener(this);

        mDrawerLayout.setDrawerListener(this);

        //注册广播接收器
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.mcdull.cert.MapMenu");
        registerReceiver(receiver, filter);

        SharedPreferences SP = getSharedPreferences("config", MODE_PRIVATE);
        if (SP.getBoolean("mapFirst", true)) {
            mDrawerLayout.openDrawer(mLeftMenu);
            SharedPreferences.Editor edit = SP.edit();
            edit.putBoolean("mapFirst", false);
            edit.apply();
            edit.clear();
        }

    }

    private LinkedList<Location> initList() {
        LinkedList<Location> list = new LinkedList<>();
        SharedPreferences SP = getSharedPreferences("config", MODE_PRIVATE);
        if (SP.getBoolean("Nan", true)) {
            if (SP.getBoolean("Stay", true)) {
                list.addAll(SchoolLoc.getnStayList());
            }
            if (SP.getBoolean("Study", false)) {
                list.addAll(SchoolLoc.getnStudyList());
            }
            if (SP.getBoolean("Life", false)) {
                list.addAll(SchoolLoc.getnLiefList());
            }
            if (SP.getBoolean("Play", false)) {
                list.addAll(SchoolLoc.getnPlayList());
            }
            if (SP.getBoolean("Other", false)) {
                list.addAll(SchoolLoc.getnOtherList());
            }
        }
        if (SP.getBoolean("Bei", false)) {
            if (SP.getBoolean("Stay", false)) {
                list.addAll(SchoolLoc.getbStayList());
            }
            if (SP.getBoolean("Study", false)) {
                list.addAll(SchoolLoc.getbStudyList());
            }
            if (SP.getBoolean("Life", false)) {
                list.addAll(SchoolLoc.getbLiefList());
            }
            if (SP.getBoolean("Play", false)) {
                list.addAll(SchoolLoc.getbPlayList());
            }
            if (SP.getBoolean("Other", false)) {
                list.addAll(SchoolLoc.getbOtherList());
            }
        }

        return list;
    }

    private void initView() {
        findViewById(R.id.tv_nan).setOnClickListener(new CheckBoxOnClick());
        findViewById(R.id.tv_bei).setOnClickListener(new CheckBoxOnClick());
        findViewById(R.id.tv_stay).setOnClickListener(new CheckBoxOnClick());
        findViewById(R.id.tv_study).setOnClickListener(new CheckBoxOnClick());
        findViewById(R.id.tv_lief).setOnClickListener(new CheckBoxOnClick());
        findViewById(R.id.tv_play).setOnClickListener(new CheckBoxOnClick());
        findViewById(R.id.tv_other).setOnClickListener(new CheckBoxOnClick());
        mCbNan = (CheckBox) findViewById(R.id.cb_nan);
        mCbBei = (CheckBox) findViewById(R.id.cb_bei);
        mCbStay = (CheckBox) findViewById(R.id.cb_stay);
        mCbStudy = (CheckBox) findViewById(R.id.cb_study);
        mCbLife = (CheckBox) findViewById(R.id.cb_lief);
        mCbPlay = (CheckBox) findViewById(R.id.cb_play);
        mCbOther = (CheckBox) findViewById(R.id.cb_other);
        mCbNan.setOnCheckedChangeListener(this);
        mCbBei.setOnCheckedChangeListener(this);
        mCbStay.setOnCheckedChangeListener(this);
        mCbStudy.setOnCheckedChangeListener(this);
        mCbLife.setOnCheckedChangeListener(this);
        mCbPlay.setOnCheckedChangeListener(this);
        mCbOther.setOnCheckedChangeListener(this);
        mCbNan.setOnClickListener(new CheckBoxOnClick());
        mCbBei.setOnClickListener(new CheckBoxOnClick());
        mCbStay.setOnClickListener(new CheckBoxOnClick());
        mCbStudy.setOnClickListener(new CheckBoxOnClick());
        mCbLife.setOnClickListener(new CheckBoxOnClick());
        mCbPlay.setOnClickListener(new CheckBoxOnClick());
        mCbOther.setOnClickListener(new CheckBoxOnClick());

        mList = (ListView) findViewById(R.id.list);
        adapter = new MapMenuAdapter(this, selectList);
        mList.setAdapter(adapter);
    }

    private void initCheckBox() {
        SharedPreferences SP = getSharedPreferences("config", MODE_PRIVATE);
        mCbNan.setChecked(SP.getBoolean("Nan", true));
        mCbBei.setChecked(SP.getBoolean("Bei", false));
        mCbStay.setChecked(SP.getBoolean("Stay", true));
        mCbStudy.setChecked(SP.getBoolean("Study", false));
        mCbLife.setChecked(SP.getBoolean("Life", false));
        mCbPlay.setChecked(SP.getBoolean("Play", false));
        mCbOther.setChecked(SP.getBoolean("Other", false));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        selectList.removeAll(selectList);
        if (mCbNan.isChecked()) {
            if (mCbStay.isChecked()) {
                selectList.addAll(SchoolLoc.getnStayList());
            }
            if (mCbStudy.isChecked()) {
                selectList.addAll(SchoolLoc.getnStudyList());
            }
            if (mCbLife.isChecked()) {
                selectList.addAll(SchoolLoc.getnLiefList());
            }
            if (mCbPlay.isChecked()) {
                selectList.addAll(SchoolLoc.getnPlayList());
            }
            if (mCbOther.isChecked()) {
                selectList.addAll(SchoolLoc.getnOtherList());
            }
        }
        if (mCbBei.isChecked()) {
            if (mCbStay.isChecked()) {
                selectList.addAll(SchoolLoc.getbStayList());
            }
            if (mCbStudy.isChecked()) {
                selectList.addAll(SchoolLoc.getbStudyList());
            }
            if (mCbLife.isChecked()) {
                selectList.addAll(SchoolLoc.getbLiefList());
            }
            if (mCbPlay.isChecked()) {
                selectList.addAll(SchoolLoc.getbPlayList());
            }
            if (mCbOther.isChecked()) {
                selectList.addAll(SchoolLoc.getbOtherList());
            }
        }
        adapter.upDateList(selectList);
    }

    private void saveType() {
        SharedPreferences SP = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor edit = SP.edit();
        edit.putBoolean("Nan", mCbNan.isChecked());
        edit.putBoolean("Bei", mCbBei.isChecked());
        edit.putBoolean("Stay", mCbStay.isChecked());
        edit.putBoolean("Study", mCbStudy.isChecked());
        edit.putBoolean("Life", mCbLife.isChecked());
        edit.putBoolean("Play", mCbPlay.isChecked());
        edit.putBoolean("Other", mCbOther.isChecked());
        edit.apply();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Location Location = selectList.get(position);
        int LocationId = Location.getId();
        String LocationName = Location.getName();
        double latitude = Location.getLatLng().latitude;
        double longitude = Location.getLatLng().longitude;

        Bundle bundle = new Bundle();
        bundle.putInt("locaBeenId", LocationId);
        bundle.putString("locaBeenName", LocationName);
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);

        Intent intent = new Intent();
        intent.setAction("com.mcdull.cert.Map");
        intent.putExtra("bundle", bundle);
        sendBroadcast(intent);
        mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mDrawerLayout.isDrawerOpen(mLeftMenu)) {
            mDrawerLayout.closeDrawers();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (!mDrawerLayout.isDrawerOpen(mLeftMenu)) {
                mDrawerLayout.openDrawer(mLeftMenu);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {
        if (newState == DrawerLayout.STATE_IDLE) {
            saveType();
        }
    }

    @Override
    public void onClick(View v) {

    }

    private class CheckBoxOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cb_nan:
                case R.id.tv_nan:
                    mCbBei.setChecked(false);
                    mCbNan.setChecked(true);
                    break;
                case R.id.cb_bei:
                case R.id.tv_bei:
                    mCbNan.setChecked(false);
                    mCbBei.setChecked(true);
                    break;
                case R.id.cb_stay:
                case R.id.tv_stay:
                    mCbStay.setChecked(true);
                    mCbStudy.setChecked(false);
                    mCbLife.setChecked(false);
                    mCbPlay.setChecked(false);
                    mCbOther.setChecked(false);
                    break;
                case R.id.cb_study:
                case R.id.tv_study:
                    mCbStay.setChecked(false);
                    mCbStudy.setChecked(true);
                    mCbLife.setChecked(false);
                    mCbPlay.setChecked(false);
                    mCbOther.setChecked(false);
                    break;
                case R.id.cb_lief:
                case R.id.tv_lief:
                    mCbStay.setChecked(false);
                    mCbStudy.setChecked(false);
                    mCbLife.setChecked(true);
                    mCbPlay.setChecked(false);
                    mCbOther.setChecked(false);
                    break;
                case R.id.cb_play:
                case R.id.tv_play:
                    mCbStay.setChecked(false);
                    mCbStudy.setChecked(false);
                    mCbLife.setChecked(false);
                    mCbPlay.setChecked(true);
                    mCbOther.setChecked(false);
                    break;
                case R.id.cb_other:
                case R.id.tv_other:
                    mCbStay.setChecked(false);
                    mCbStudy.setChecked(false);
                    mCbLife.setChecked(false);
                    mCbPlay.setChecked(false);
                    mCbOther.setChecked(true);
                    break;
            }
        }
    }

    class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mDrawerLayout.openDrawer(mLeftMenu);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
