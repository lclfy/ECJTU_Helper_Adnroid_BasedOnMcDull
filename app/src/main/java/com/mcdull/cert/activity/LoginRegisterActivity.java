package com.mcdull.cert.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.mcdull.cert.R;
import com.mcdull.cert.View.MyViewPager;
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.Util;
import com.umeng.update.UmengUpdateAgent;

import java.util.List;


public class LoginRegisterActivity extends Activity implements View.OnClickListener {


    private Intent intent = new Intent();
    private MyViewPager viewPagerView;
    private List<ImageView> ivs;
    private ImageView view6;
    private LinearLayout sign;
    private LinearLayout login;
    private boolean loginOrRegister = true;
    private AlertDialog alertDialog;
    private EditText mLoginEMain;
    private EditText mSignPwd2;
    private EditText mLoginPwd;
    private EditText mSignEMail;
    private EditText mSignPwd;
    private ShowWaitPopupWindow waitWin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AVAnalytics.trackAppOpened(getIntent());
        //判断SDK版本，设置沉浸状态栏
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);
        view6 = (ImageView) findViewById(R.id.img);
        view6.setImageResource(R.drawable.qd);
        view6.setScaleType(ImageView.ScaleType.FIT_XY);


        if (AVUser.getCurrentUser() != null) {

//            findViewById(R.id.view1).setVisibility(View.GONE);
//            findViewById(R.id.view2).setVisibility(View.GONE);
//            findViewById(R.id.view3).setVisibility(View.GONE);
//            findViewById(R.id.view4).setVisibility(View.GONE);
//          findViewById(R.id.view5).setVisibility(View.GONE);


            SharedPreferences SP = getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor edit = SP.edit();
            edit.putBoolean("first", false);
            edit.apply();

            final long time = System.currentTimeMillis();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        AVUser.getCurrentUser().refresh();
                        if (System.currentTimeMillis() - time < 1500) {
                            Thread.sleep(1500);
                        }
                    } catch (InterruptedException | AVException e) {
                        e.printStackTrace();
                    }
                    intent.setClass(LoginRegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).start();

        } else {

            UmengUpdateAgent.setUpdateOnlyWifi(false);
            UmengUpdateAgent.update(this);
            initView();
        }
    }

    private void initView() {



        viewPagerView = (MyViewPager) findViewById(R.id.viewpager);

        viewPagerView.setIsEvent(false);
        findViewById(R.id.input).setVisibility(View.VISIBLE);
        findViewById(R.id.input).startAnimation(AnimationUtils.loadAnimation(LoginRegisterActivity.this, R.anim.alpha_in));
        waitWin = new ShowWaitPopupWindow(LoginRegisterActivity.this);

//        view1 = findViewById(R.id.view1);
//        view2 = findViewById(R.id.view2);
//        view3 = findViewById(R.id.view3);
//        view4 = findViewById(R.id.view4);
//        view5 = findViewById(R.id.view5);

        sign = (LinearLayout) findViewById(R.id.sign);
        login = (LinearLayout) findViewById(R.id.login);

        findViewById(R.id.bt_sure).setOnClickListener(this);
        findViewById(R.id.bt_type).setOnClickListener(this);
        findViewById(R.id.bt_find_pwd).setOnClickListener(this);

        mLoginEMain = (EditText) findViewById(R.id.et_email_d);
        mLoginPwd = (EditText) findViewById(R.id.et_password_d);

        mSignEMail = (EditText) findViewById(R.id.et_email);
        mSignPwd = (EditText) findViewById(R.id.et_password);
        mSignPwd2 = (EditText) findViewById(R.id.et_password2);
//
//        ImageView iv1 = new ImageView(LoginRegisterActivity.this);
//        ImageView iv2 = new ImageView(LoginRegisterActivity.this);
//        ImageView iv3 = new ImageView(LoginRegisterActivity.this);
//        ImageView iv4 = new ImageView(LoginRegisterActivity.this);
//        ImageView iv5 = new ImageView(LoginRegisterActivity.this);
//        ivs = new ArrayList<>();
//        ivs.add(iv1);
//        ivs.add(iv2);
//        ivs.add(iv3);
//        ivs.add(iv4);
//        ivs.add(iv5);

//        MyAdapter adapter = new MyAdapter();
//        viewPagerView.setAdapter(adapter);

        viewPagerView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {

//                view1.setVisibility(View.VISIBLE);
//                view2.setVisibility(View.VISIBLE);
//                view3.setVisibility(View.VISIBLE);
//                view4.setVisibility(View.VISIBLE);
//                view5.setVisibility(View.VISIBLE);
//                view1.setBackgroundResource(R.drawable.circletm);
//                view2.setBackgroundResource(R.drawable.circletm);
//                view3.setBackgroundResource(R.drawable.circletm);
//                view4.setBackgroundResource(R.drawable.circletm);
//                view5.setBackgroundResource(R.drawable.circletm);
//                switch (arg0) {
//                    case 0:
//                        view1.setBackgroundResource(R.drawable.circle);
//                        break;
//                    case 1:
//                        view2.setBackgroundResource(R.drawable.circle);
//                        break;
//                    case 2:
//                        view3.setBackgroundResource(R.drawable.circle);
//                        break;
//                    case 3:
//                        view4.setBackgroundResource(R.drawable.circle);
//                        break;
//                    case 4:
//                        view1.setVisibility(View.GONE);
//                        view2.setVisibility(View.GONE);
//                        view3.setVisibility(View.GONE);
//                        view4.setVisibility(View.GONE);
//                        view5.setVisibility(View.GONE);
//
//
//                        break;
//                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });


        if (getIntent().getBooleanExtra("back", false)) {
            viewPagerView.setCurrentItem(4);
            if(!TextUtils.isEmpty(getIntent().getStringExtra("userName"))){
                mLoginEMain.setText(getIntent().getStringExtra("userName"));
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_type:
                switchType();
                break;
            case R.id.bt_find_pwd:
                findPwd();
                break;
            case R.id.bt_sure:
                sure();
                break;
        }
    }

    private void sure() {
        if (loginOrRegister) {
            toLogin();
        } else {
            toSign();
        }
    }

    private void toSign() {
        final String email = mSignEMail.getText().toString();
        final String pwd = mSignPwd.getText().toString();
        String rePwd = mSignPwd2.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginRegisterActivity.this, "请输入邮箱地址", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginRegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(rePwd)) {
            Toast.makeText(LoginRegisterActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Util.matcherEmali(email)) {
            Toast.makeText(LoginRegisterActivity.this, "邮箱格式有误", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!rePwd.equals(pwd)) {
            Toast.makeText(LoginRegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

        waitWin.showWait();

        AVQuery<AVUser> query = AVUser.getQuery();
        query.whereEqualTo("email", email);
        query.findInBackground(new FindCallback<AVUser>() {
            public void done(List<AVUser> objects, AVException e) {
                waitWin.dismissWait();
                if (e != null || objects.size() == 0) {
                    Intent intent = new Intent(LoginRegisterActivity.this, SignInJWCActivity.class);
                    intent.putExtra("email", email);
                    intent.putExtra("pwd", pwd);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginRegisterActivity.this, "该邮箱已注册", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void toLogin() {
//
        String email = mLoginEMain.getText().toString();
        String pwd = mLoginPwd.getText().toString();
//        String email = "758003984@qq.com";
//        String pwd = "1081884068lsc";
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginRegisterActivity.this, "请输入邮箱地址", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginRegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Util.matcherEmali(email)) {
            Toast.makeText(LoginRegisterActivity.this, "邮箱格式有误", Toast.LENGTH_SHORT).show();
            return;
        }
        waitWin.showWait();


        AVUser.logInInBackground(email, pwd, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                waitWin.dismissWait();
                if (avUser != null) {
                    Toast.makeText(LoginRegisterActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    intent.setClass(LoginRegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if ("The username and password mismatch.".equals(e.getMessage())) {
                        Toast.makeText(LoginRegisterActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                    } else if ("Could not find user".equals(e.getMessage())) {
                        Toast.makeText(LoginRegisterActivity.this, "该邮箱账号未注册", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginRegisterActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void findPwd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginRegisterActivity.this);
        View view = View.inflate(LoginRegisterActivity.this, R.layout.edit_dialog, null);

        final EditText etTextView = (EditText) view.findViewById(R.id.et_tooltip);
        Button btYes = (Button) view.findViewById(R.id.bt_yes);
        Button btNo = (Button) view.findViewById(R.id.bt_no);
        TextView tvTooltipTitle = (TextView) view.findViewById(R.id.tv_tooltip_title);

        tvTooltipTitle.setText("找回密码");
        etTextView.setHint("请输入注册邮箱");

        btYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Util.matcherEmali(etTextView.getText().toString())) {
                    AVUser.requestPasswordResetInBackground(etTextView.getText().toString(), new RequestPasswordResetCallback() {
                        public void done(AVException e) {
                            if (e == null) {
                                Toast.makeText(LoginRegisterActivity.this, "已发送重置密码邮件", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginRegisterActivity.this, "该邮箱未注册帐号", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    alertDialog.dismiss();
                } else {
                    Toast.makeText(LoginRegisterActivity.this, "邮箱格式错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        freeMemory();

    }

    public void switchType() {
        //判断登录还是注册
        if (loginOrRegister) {
            Animation loginAnimation = new TranslateAnimation(0, -LoginRegisterActivity.this.getWindowManager().getDefaultDisplay().getWidth(), 0, 0);
            loginAnimation.setDuration(700);
            login.startAnimation(loginAnimation);

            login.setVisibility(View.GONE);

            sign.setVisibility(View.VISIBLE);

            Animation signAnimation = new TranslateAnimation(LoginRegisterActivity.this.getWindowManager().getDefaultDisplay().getWidth(), 0, 0, 0);
            signAnimation.setDuration(700);
            sign.startAnimation(signAnimation);

            ((Button) findViewById(R.id.bt_sure)).setText("注册");
            ((TextView) findViewById(R.id.bt_type)).setText("从现有账户登录");
            findViewById(R.id.bt_find_pwd).setVisibility(View.INVISIBLE);
        } else {
            Animation signAnimation = new TranslateAnimation(0, LoginRegisterActivity.this.getWindowManager().getDefaultDisplay().getWidth(), 0, 0);
            signAnimation.setDuration(700);
            sign.startAnimation(signAnimation);

            sign.setVisibility(View.GONE);

            login.setVisibility(View.VISIBLE);

            Animation loginAnimation = new TranslateAnimation(-LoginRegisterActivity.this.getWindowManager().getDefaultDisplay().getWidth(), 0, 0, 0);
            loginAnimation.setDuration(700);
            login.startAnimation(loginAnimation);

            ((Button) findViewById(R.id.bt_sure)).setText("登入");
            ((TextView) findViewById(R.id.bt_type)).setText("注册新用户");
            findViewById(R.id.bt_find_pwd).setVisibility(View.VISIBLE);
        }
        loginOrRegister = !loginOrRegister;
    }


//    class MyAdapter extends PagerAdapter {
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return ivs.size();
//        }
//
//        @Override
//        public boolean isViewFromObject(View arg0, Object arg1) {
//            // TODO Auto-generated method stub
//            return arg0 == arg1;
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            ImageView iv = ivs.get(position);
//            iv.setScaleType(ImageView.ScaleType.FIT_XY);
//            switch (position) {
//                case 0:
//                    Glide.with(getApplicationContext()).load("").placeholder(R.drawable.qd1).crossFade().into(iv);
//                    break;
//                case 1:
//                    Glide.with(getApplicationContext()).load("").placeholder(R.drawable.qd2).crossFade().into(iv);
//                    break;
//                case 2:
//                    Glide.with(getApplicationContext()).load("").placeholder(R.drawable.qd3).crossFade().into(iv);
//                    break;
//                case 3:
//                    Glide.with(getApplicationContext()).load("").placeholder(R.drawable.qd4).crossFade().into(iv);
//                    break;
//                case 4:
//                    Glide.with(getApplicationContext()).load("").placeholder(R.drawable.qd5).crossFade().into(iv);
//                    break;
//            }
//
//            container.addView(iv, 0);// 添加页卡
//            return iv;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            ImageView iv = ivs.get(position);
//            iv.setDrawingCacheEnabled(true);
//            if (iv.getDrawingCache() != null) {
//                iv.getDrawingCache().recycle();
//            }
//            iv.setDrawingCacheEnabled(false);
//            container.removeView(ivs.get(position));// 删除页卡
//        }
//    }

    private void freeMemory() {
//        if (view1 != null) {
//            view1.setDrawingCacheEnabled(true);
//            if (view1.getDrawingCache() != null) {
//                view1.getDrawingCache().recycle();
//            }
//            view1.setDrawingCacheEnabled(false);
//            view1 = null;
//        }
//        if (view2 != null) {
//            view2.setDrawingCacheEnabled(true);
//            if (view2.getDrawingCache() != null) {
//                view2.getDrawingCache().recycle();
//            }
//            view2.setDrawingCacheEnabled(false);
//            view2 = null;
//        }
//        if (view3 != null) {
//            view3.setDrawingCacheEnabled(true);
//            if (view3.getDrawingCache() != null) {
//                view3.getDrawingCache().recycle();
//            }
//            view3.setDrawingCacheEnabled(false);
//            view3 = null;
//        }
//        if (view4 != null) {
//            view4.setDrawingCacheEnabled(true);
//            if (view4.getDrawingCache() != null) {
//                view4.getDrawingCache().recycle();
//            }
//            view4.setDrawingCacheEnabled(false);
//            view4 = null;
//        }
//        if (view5 != null) {
//            view5.setDrawingCacheEnabled(true);
//            if (view5.getDrawingCache() != null) {
//                view5.getDrawingCache().recycle();
//            }
//            view5.setDrawingCacheEnabled(false);
//            view5 = null;
//        }
        if (view6 != null) {
            view6.setDrawingCacheEnabled(true);
            if (view6.getDrawingCache() != null) {
                view6.getDrawingCache().recycle();
            }
            view6.setDrawingCacheEnabled(false);
            view6 = null;
        }
        if (ivs != null) {
            for (int i = 0; i < ivs.size(); i++) {
                ImageView iv = ivs.get(i);
                iv.setDrawingCacheEnabled(true);
                if (iv.getDrawingCache() != null) {
                    iv.getDrawingCache().recycle();
                }
                iv.setDrawingCacheEnabled(false);
                iv = null;
            }
            ivs = null;
        }
        if (viewPagerView != null) {
            for (int i = 0; i < viewPagerView.getCurrentItem(); i++) {
                ImageView iv = (ImageView) viewPagerView.getChildAt(i);
                if (iv != null) {
                    iv.setDrawingCacheEnabled(true);
                    if (iv.getDrawingCache() != null) {
                        iv.getDrawingCache().recycle();
                    }
                    iv.setDrawingCacheEnabled(false);
                    iv = null;
                }
            }
            viewPagerView = null;
        }
    }
}
