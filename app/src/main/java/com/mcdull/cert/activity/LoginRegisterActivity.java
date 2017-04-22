package com.mcdull.cert.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RefreshCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SignUpCallback;
import com.bumptech.glide.Glide;
import com.mcdull.cert.HJXYT;
import com.mcdull.cert.R;
import com.mcdull.cert.bean.UserInfoBean;
import com.mcdull.cert.service.CourseIntentService;
import com.mcdull.cert.view.SlideView;
import com.mcdull.cert.activity.base.BaseActivity;
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.Util;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginRegisterActivity extends BaseActivity implements View.OnClickListener {
    private Intent intent = new Intent();
    private AlertDialog alertDialog;
    private EditText mLoginEMain;
    private EditText mLoginPwd;
    private ShowWaitPopupWindow waitWin;
    private Handler handler;
    private Runnable startHome = new Runnable() {
        @Override
        public void run() {
            nextActivity(HomeActivity.class);
        }
    };
    private Tencent mTencent;
    private IUiListener mIUiListener;

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
        if (AVUser.getCurrentUser() != null) {
            if (AVUser.getCurrentUser().getEmail() == null) {
                nextActivity(SignInJWCActivity.class);
            } else {
                SharedPreferences SP = getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor edit = SP.edit();
                edit.putBoolean("first", false);
                edit.apply();

                final long start = System.currentTimeMillis();
                handler = new Handler(Looper.getMainLooper());

                AVUser.getCurrentUser().refreshInBackground(new RefreshCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        long now = System.currentTimeMillis();
                        Log.d("TAG", now + "time");
                        if (now - start < 1500) {
                            handler.postDelayed(startHome, now - start);
                        } else {
                            nextActivity(HomeActivity.class);
                        }
                    }
                });
            }
        } else {
            initView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        freeMemory();
    }

    private void nextActivity(Class clazz) {
        intent.setClass(LoginRegisterActivity.this, clazz);
        startActivity(intent);
        finish();
    }

    private void initView() {
        waitWin = new ShowWaitPopupWindow(LoginRegisterActivity.this);

        SlideView slideView = (SlideView) findViewById(R.id.slide_view);
        slideView.setAdapter(new MyAdapter(this));
        slideView.setOnPageSelectedListener(new SlideView.OnPageSelectedListener() {
            @Override
            public void onPageSelectedListener(int position) {
                if (position == 4) {
                    findViewById(R.id.input).setVisibility(View.VISIBLE);
                    findViewById(R.id.input).startAnimation(AnimationUtils.loadAnimation(LoginRegisterActivity.this, R.anim.alpha_in));
                } else {
                    findViewById(R.id.input).setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.bt_sure).setOnClickListener(this);
        findViewById(R.id.bt_find_pwd).setOnClickListener(this);
        findViewById(R.id.bt_qq_sign).setOnClickListener(this);

        mLoginEMain = (EditText) findViewById(R.id.et_email_d);
        mLoginPwd = (EditText) findViewById(R.id.et_password_d);

        if (getIntent().getBooleanExtra("back", false)) {
            if (!TextUtils.isEmpty(getIntent().getStringExtra("userName"))) {
                mLoginEMain.setText(getIntent().getStringExtra("userName"));
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_find_pwd:
                findPwd();
                break;
            case R.id.bt_sure:
                sure();
                break;
            case R.id.bt_qq_sign:
                qqLogin();
                break;
        }
    }

    private void qqLogin() {
        mTencent = Tencent.createInstance("1104722411", getApplicationContext());
        mIUiListener = new LoginListener();
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", mIUiListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
    }

    private void sure() {
        toLogin();
    }

    private void toLogin() {
        final String email = mLoginEMain.getText().toString();
        final String pwd = mLoginPwd.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginRegisterActivity.this, "请输入邮箱地址", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginRegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        waitWin.showWait();

        AVUser.logInInBackground(email, pwd, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e != null) {
                    if (e.getCode() == 211)
                        loginForJwc(email, pwd, 0);//未注册 转入自动注册及信息补全流程
                    else if (e.getCode() == 210)
                        loginForJwc(email, pwd, 1);//密码错误 转入密码修改流程
                    else {
                        String error = "错误" + e.toString().split("error")[1].replaceAll("\"", "").replaceAll("\\}", "");
                        Toast.makeText(LoginRegisterActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    nextActivity(HomeActivity.class);
                }
            }
        });
    }

    private void loginForJwc(String userId, final String pwd, final int type) {
        HJXYT.getInstance().getAppClient().getJWXTService().getUserInfoBean(userId, pwd).enqueue(new Callback<UserInfoBean>() {
            @Override
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                UserInfoBean bean = response.body();
                if (bean == null || bean.getCode() != 1 ||!bean.getMsg().equals("success")) {
                    //失败
                    waitWin.dismissWait();
                    //未能成功登录教务系统
                    Toast.makeText(LoginRegisterActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                } else {
                    switch (type) {
                        case 0:
                            sign(bean, pwd);
                            break;
                        case 1:
                            changePwd(bean, pwd);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<UserInfoBean> call, Throwable t) {
                waitWin.dismissWait();
                Toast.makeText(LoginRegisterActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sign(UserInfoBean bean, String pwd) {
        AVUser avUser = new AVUser();
        avUser.setUsername(bean.getData().getXh());
        avUser.setPassword(pwd);
        switch (bean.getData().getXb()) {
            case "男":
                avUser.put("Sex", 1);
                break;
            case "女":
                avUser.put("Sex", -1);
                break;
        }
        avUser.put("JwcPwd", pwd);
        avUser.put("Name", bean.getData().getXm());
        avUser.put("Class", bean.getData().getBj());
        avUser.put("StudentId", bean.getData().getXh());
        avUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null)
                    nextActivity(SignInJWCActivity.class);
                else
                    e.printStackTrace();
                waitWin.dismissWait();
            }
        });
    }

    private void changePwd(UserInfoBean bean, String pwd) {
        //此处说明教务系统密码与LeanCloud密码不相同,需要修改LeanCloud的账号密码.
        waitWin.dismissWait();
    }

    private void findPwd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginRegisterActivity.this);
        View view = View.inflate(LoginRegisterActivity.this, R.layout.edit_dialog, null);

        final EditText etTextView = (EditText) view.findViewById(R.id.et_tooltip);
        TextView tvTooltipTitle = (TextView) view.findViewById(R.id.tv_tooltip_title);

        tvTooltipTitle.setText("找回密码");
        etTextView.setHint("请输入注册邮箱");

        view.findViewById(R.id.bt_yes).setOnClickListener(new View.OnClickListener() {

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

        view.findViewById(R.id.bt_no).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.setView(view, 0, 0, 0, 0);
        alertDialog.show();
    }

    private class MyAdapter extends PagerAdapter {
        ArrayList<ImageView> ivs;

        MyAdapter(Context context) {
            ivs = new ArrayList<>();
            for (int i = 0; i < 5; i++)
                ivs.add(new ImageView(context));
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return ivs.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = ivs.get(position);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(getApplicationContext()).load("").placeholder(getResources().getIdentifier("qd" + (position + 1), "drawable", getApplicationInfo().packageName)).crossFade().into(iv);
            container.addView(iv, 0);// 添加页卡
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView iv = ivs.get(position);
            iv.setDrawingCacheEnabled(true);
            if (iv.getDrawingCache() != null) {
                iv.getDrawingCache().recycle();
            }
            iv.setDrawingCacheEnabled(false);
            container.removeView(ivs.get(position));// 删除页卡
        }
    }

    private void freeMemory() {
    }

    class LoginListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            Log.d("QQ", o.toString());
            JSONObject jsonObject = JSON.parseObject(o.toString());
            String openid = jsonObject.getString("openid");
            loginForQQ(openid);
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }

    private void loginForQQ(final String openId) {
        AVQuery<AVUser> query = new AVQuery<>("_User");
        query.whereEqualTo("OpenId", openId);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null) {
                    if (list == null || list.size() == 0) {
                        //账号系统中不存在该QQ号所绑定的账号，转入Sign进入注册流程
                        Intent intent = new Intent();
                        intent.setClass(LoginRegisterActivity.this, SignInJWCActivity.class);
                        intent.putExtra("openId", openId);
                        intent.putExtra("QQ", true);
                        startActivity(intent);
                        finish();
                    } else {
                        //账号系统中存在账号，进行登录。
                        AVUser avUser = list.get(0);
                        AVUser.logInInBackground(avUser.getUsername(), avUser.getString("JwcPwd"), new LogInCallback<AVUser>() {
                            @Override
                            public void done(AVUser avUser, AVException e) {
                                if (e == null) {
                                    nextActivity(HomeActivity.class);
                                }else {
                                    String error = "错误"+ e.toString().split("error")[1].replaceAll("\"","").replaceAll("\\}","");
                                    Toast.makeText(LoginRegisterActivity.this,error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    String error = "错误"+ e.toString().split("error")[1].replaceAll("\"","").replaceAll("\\}","");
                    Toast.makeText(LoginRegisterActivity.this,error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
