package com.mcdull.cert.activity;

import android.app.Fragment;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.mcdull.cert.R;
import com.mcdull.cert.fragment.HomeFragment;
import com.mcdull.cert.fragment.NewStudentFragment;
import com.mcdull.cert.utils.GetIcon;
import com.mcdull.cert.utils.Util;
import com.xiaomi.mipush.sdk.MiPushClient;

public class HomeActivity extends FragmentActivity implements View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mLeftMenu;
    private ImageView mIvIcon;
    private Intent intent;
    private long exitTime;
    private MyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            //结束你的activity
            finish();
            return;
        }
        setContentView(R.layout.activity_home);

        //判断SDK版本，设置沉浸状态栏
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            findViewById(R.id.status_bar).setVisibility(View.VISIBLE);
        }

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftMenu = (RelativeLayout) findViewById(R.id.left_drawer);
        mLeftMenu.setOnClickListener(this);
        //创建已用Fragment设定好内容的ActionBar-Layout
        FragmentManager fragmentManager = getSupportFragmentManager();
//        Fragment ft = (Fragment) getSupportFragmentManager().findFragmentById(R.id.);
        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().replace(R.id.framelayout, homeFragment).commit();

        //注册广播接收器
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.mcdull.cert.Home");
        HomeActivity.this.registerReceiver(receiver, filter);

        init();

        if (AVUser.getCurrentUser() != null) {
            MiPushClient.setAlias(HomeActivity.this, Util.toAlias(AVUser.getCurrentUser().getEmail()), null);
        } else {
            Toast.makeText(HomeActivity.this,"未登录...",Toast.LENGTH_SHORT).show();
            intent = new Intent(HomeActivity.this,LoginRegisterActivity.class);
            intent.putExtra("back",true);
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences SP = getSharedPreferences("config", MODE_PRIVATE);
        findViewById(R.id.status_bar).setBackgroundColor(getSharedPreferences("setting", MODE_PRIVATE).getInt("theme", 0xff009688));
        try{
            TextView tvAllCourse = (TextView)findViewById(R.id.tv_allCourseBtn);
            tvAllCourse.setTextColor(HomeActivity.this.getSharedPreferences("setting", MODE_PRIVATE).getInt("theme", 0xff009688));
            TextView retryBtn = (TextView)findViewById(R.id.tv_reTryBtn);
            retryBtn.setTextColor(HomeActivity.this.getSharedPreferences("setting", MODE_PRIVATE).getInt("theme", 0xff009688));
            findViewById(R.id.eCardStatus).setBackgroundColor(getSharedPreferences("setting", MODE_PRIVATE).getInt("theme", 0xff009688));
            TextView tvCalenderTitle = (TextView)findViewById(R.id.calenderArea_title);
            tvCalenderTitle.setTextColor(getSharedPreferences("setting", MODE_PRIVATE).getInt("theme", 0xff009688));
            //设置跟随主题变换颜色的图标的颜色
            switch (getSharedPreferences("setting", MODE_PRIVATE).getInt("themeInt", 2)){
                case 0:
                    findViewById(R.id.calenderIcon).setBackgroundResource(R.drawable.ic_calendericon_deep_purple);
                    break;
                case 1:
                    findViewById(R.id.calenderIcon).setBackgroundResource(R.drawable.ic_calendericon_pink);
                    break;
                case 2:
                    findViewById(R.id.calenderIcon).setBackgroundResource(R.drawable.ic_calendericon_deep_teal);
                    break;
                case 3:
                    findViewById(R.id.calenderIcon).setBackgroundResource(R.drawable.ic_calendericon_deep_blue);
                    break;
                case 4:
                    findViewById(R.id.calenderIcon).setBackgroundResource(R.drawable.ic_calendericon_deep_amber);
                    break;
                case 5:
                    findViewById(R.id.calenderIcon).setBackgroundResource(R.drawable.ic_calendericon_deep_red);
                    break;
            }
        }catch (Exception e){

        }
        findViewById(R.id.view_home_menu).setBackgroundColor(getSharedPreferences("setting", MODE_PRIVATE).getInt("theme", 0xff009688));
        if (SP.getBoolean("Icon", true)) {
            new GetIcon(HomeActivity.this, new GetIcon.GetIconCallBack() {
                @Override
                public void done(Bitmap bitmap) {
                    if (bitmap != null) {
                        mIvIcon.setImageBitmap(Util.toRoundBitmap(bitmap));
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    //左菜单
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_drawer:
                break;
            case R.id.bt_bug:
                mDrawerLayout.closeDrawers();
                intent = new Intent(HomeActivity.this, BugFeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_mydata:
                mDrawerLayout.closeDrawers();
                intent = new Intent(HomeActivity.this, MyDataActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_join:
                mDrawerLayout.closeDrawers();
                intent = new Intent(HomeActivity.this, CERTActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_setting:
                mDrawerLayout.closeDrawers();
                intent = new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 获取广播数据
     *
     * @author jiqinlin
     */
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra("type", 0);
            switch (type) {
                case 1:
                    mDrawerLayout.openDrawer(mLeftMenu);
                    break;
                case 2:
                    finish();
                    break;
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    private void init() {
        mIvIcon = (ImageView) findViewById(R.id.iv_icon);
        mIvIcon.setImageBitmap(Util.toRoundBitmap(Util.drawableToBitmap(getResources().getDrawable(R.drawable.ic_account_circle_color2_48dp))));
        new GetIcon(HomeActivity.this, new GetIcon.GetIconCallBack() {
            @Override
            public void done(Bitmap bitmap) {
                if (bitmap != null) {
                    mIvIcon.setImageBitmap(Util.toRoundBitmap(bitmap));
                }
            }
        });
        ((TextView) findViewById(R.id.tv_name)).setText(AVUser.getCurrentUser().getString("Name"));
        ((TextView) findViewById(R.id.tv_email)).setText(AVUser.getCurrentUser().getEmail());
        findViewById(R.id.bt_mydata).setOnClickListener(this);
        findViewById(R.id.bt_bug).setOnClickListener(this);
        findViewById(R.id.bt_join).setOnClickListener(this);
        findViewById(R.id.bt_setting).setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //设置按键频率与关闭应用的关系
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 300 && (System.currentTimeMillis() - exitTime) < 2000) {
                //返回桌面
//                Intent home = new Intent(Intent.ACTION_MAIN);
//                home.addCategory(Intent.CATEGORY_HOME);
//                startActivity(home);
                finish();
            } else if ((System.currentTimeMillis() - exitTime) < 300) {
                exitTime = System.currentTimeMillis();
            } else {
                exitTime = System.currentTimeMillis();
                Toast.makeText(HomeActivity.this, "再按一次返回键退出", Toast.LENGTH_SHORT).show();
            }
            if (mDrawerLayout.isDrawerOpen(mLeftMenu)) {
                mDrawerLayout.closeDrawers();
            }
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



}
