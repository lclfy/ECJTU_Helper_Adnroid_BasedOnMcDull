package com.mcdull.cert.activity;

import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;
import com.mcdull.cert.HJXYT;
import com.mcdull.cert.activity.base.BaseThemeActivity;
import com.mcdull.cert.R;
import com.mcdull.cert.bean.UserInfoBean;
import com.mcdull.cert.utils.ShowWaitPopupWindow;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInJWCActivity extends BaseThemeActivity implements View.OnClickListener {

    private TextInputLayout mEtEmail;
    private TextInputLayout mEtECardPwd;
    private TextInputLayout mEtStudentId;
    private TextInputLayout mEtJwcPwd;
    private ShowWaitPopupWindow waitWin;
    private boolean isQQLogin = false;
    private AVUser avUser;

    @Override
    protected void onTheme(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sign);

        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInJWCActivity.this, LoginRegisterActivity.class);
                intent.putExtra("back", true);
                startActivity(intent);
                finish();
            }
        });

        isQQLogin = getIntent().getBooleanExtra("QQ", false);
        init();


    }

    private void init() {
        ((TextView) findViewById(R.id.tv_title)).setText("完善个人信息");

        this.mEtEmail = (TextInputLayout) findViewById(R.id.et_email);
        this.mEtECardPwd = (TextInputLayout) findViewById(R.id.et_eCard_pwd);
        this.mEtStudentId = (TextInputLayout) findViewById(R.id.et_student_id);
        this.mEtJwcPwd = (TextInputLayout) findViewById(R.id.et_jwc_pwd);

        findViewById(R.id.bt_sure).setOnClickListener(this);

        waitWin = new ShowWaitPopupWindow(this);

        findViewById(R.id.bt_sure).setVisibility(View.VISIBLE);
        findViewById(R.id.bt_sure).setOnClickListener(this);
        if (!isQQLogin) {
            avUser = AVUser.getCurrentUser();
            mEtStudentId.getEditText().setText(avUser.getUsername());
            mEtJwcPwd.getEditText().setText(avUser.getString("JwcPwd"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sure:
                toSign();
                break;
        }
    }

    private void toSign() {
        String eCardPwd = mEtECardPwd.getEditText().getText().toString();
        String email = mEtEmail.getEditText().getText().toString();
        String username = mEtStudentId.getEditText().getText().toString();
        String jwcPwd = mEtJwcPwd.getEditText().getText().toString();

        mEtECardPwd.setErrorEnabled(false);
        mEtEmail.setErrorEnabled(false);

        if (TextUtils.isEmpty(email)) {
            mEtEmail.setErrorEnabled(true);
            mEtEmail.setError("请输入邮箱地址");
            return;
        }
        if (TextUtils.isEmpty(eCardPwd)) {
            mEtECardPwd.setErrorEnabled(true);
            mEtECardPwd.setError("请输入一卡通密码");
            return;
        }
        if (TextUtils.isEmpty(username)) {
            mEtECardPwd.setErrorEnabled(true);
            mEtECardPwd.setError("请输入一卡通密码");
            return;
        }
        if (TextUtils.isEmpty(jwcPwd)) {
            mEtECardPwd.setErrorEnabled(true);
            mEtECardPwd.setError("请输入一卡通密码");
            return;
        }
        waitWin.showWait();
        if (isQQLogin)
            setQQData(username, jwcPwd, email, eCardPwd);
        else
            setData(email, eCardPwd);
    }

    private void setQQData(final String username, final String jwcPwd, final String email, final String eCardPwd) {
        HJXYT.getInstance().getAppClient().getJWXTService().getUserInfoBean(username, jwcPwd).enqueue(new Callback<UserInfoBean>() {
            @Override
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                final UserInfoBean bean = response.body();
                avUser = new AVUser();
                avUser.setUsername(bean.getData().getXh());
                avUser.setPassword(jwcPwd);
                switch (bean.getData().getXb()) {
                    case "男":
                        avUser.put("Sex", 1);
                        break;
                    case "女":
                        avUser.put("Sex", -1);
                        break;
                }
                avUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            avUser = AVUser.getCurrentUser();
                            avUser.put("JwcPwd", jwcPwd);
                            avUser.put("Name", bean.getData().getXm());
                            avUser.put("Class", bean.getData().getBj());
                            avUser.put("StudentId", bean.getData().getXh());
                            avUser.put("OpenId", getIntent().getStringExtra("openId"));
                            setData(email, eCardPwd);
                        } else {
                            if (e.getCode() == 202) {
                                bindQQ(bean, jwcPwd, email, eCardPwd);
                            } else {
                                Toast.makeText(SignInJWCActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                                waitWin.dismissWait();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<UserInfoBean> call, Throwable t) {
                waitWin.dismissWait();
                Toast.makeText(SignInJWCActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindQQ(final UserInfoBean bean, final String jwcPwd, final String email, final String eCardPwd) {
        AVQuery<AVUser> query = new AVQuery<>("_User");
        query.whereEqualTo("username", bean.getData().getXh());
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null && list != null && list.size() != 0) {
                    AVUser.logInInBackground(bean.getData().getXh(), jwcPwd, new LogInCallback<AVUser>() {
                        @Override
                        public void done(AVUser avUser, AVException e) {
                            if (e == null) {
                                avUser.put("JwcPwd", jwcPwd);
                                avUser.put("Name", bean.getData().getXm());
                                avUser.put("Class", bean.getData().getBj());
                                avUser.put("StudentId", bean.getData().getXh());
                                avUser.put("OpenId", getIntent().getStringExtra("openId"));
                                setData(email, eCardPwd);
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignInJWCActivity.this, "网络异常", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setData(String email, String eCardPwd) {
        avUser = AVUser.getCurrentUser();
        avUser.setEmail(email);
        avUser.put("EcardPwd", eCardPwd);
        avUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                waitWin.dismissWait();
                if (e==null) {
                    Intent intent = new Intent();
                    intent.setClass(SignInJWCActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    String error = "错误"+ e.toString().split("error")[1].replaceAll("\"","").replaceAll("\\}","");
                    Toast.makeText(SignInJWCActivity.this,error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

