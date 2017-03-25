package com.mcdull.cert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SignUpCallback;
import com.google.gson.Gson;
import com.mcdull.cert.ActivityMode.MyTitleActivity;
import com.mcdull.cert.Bean.eCardBean;
import com.mcdull.cert.R;
import com.mcdull.cert.utils.InternetUtil;
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.Util;

import java.util.List;
import java.util.Map;

public class SignInJWCActivity extends MyTitleActivity implements View.OnClickListener {

    private TextInputLayout mEtJwcPwd;
    private TextInputLayout mEtECardPwd;
    private TextInputLayout mEtName;
    private TextInputLayout mEtStudentId;
    private String mEMail;
    private String mPwd;
    private ShowWaitPopupWindow waitWin;
    private String basicURL = "http://api1.ecjtu.org/v1/";
    public String studentId = "";
    public String name = "";
    public String eCardPwd = "";
    public String jwcPwd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_sign);
        super.onCreate(savedInstanceState);

        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInJWCActivity.this, LoginRegisterActivity.class);
                intent.putExtra("back", true);
                startActivity(intent);
                finish();
            }
        });

        init();

    }

    private void init() {
        ((TextView) findViewById(R.id.tv_title)).setText("完善个人信息");

        this.mEtStudentId = (TextInputLayout) findViewById(R.id.et_student_id);
        this.mEtName = (TextInputLayout) findViewById(R.id.et_name);
        this.mEtECardPwd = (TextInputLayout) findViewById(R.id.et_eCard_pwd);
        this.mEtJwcPwd = (TextInputLayout) findViewById(R.id.et_jwc_pwd);
        mEtName.setHint("姓名");
        mEtStudentId.setHint("学号");
        mEtECardPwd.setHint("一卡通密码");
        mEtJwcPwd.setHint("教务管理系统密码(15级及以后必填)");

        findViewById(R.id.bt_sure).setOnClickListener(this);
        findViewById(R.id.tv_no_id).setOnClickListener(this);

        mEMail = getIntent().getStringExtra("email");
        mPwd = getIntent().getStringExtra("pwd");

        waitWin = new ShowWaitPopupWindow(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sure:
                toSign();
                break;
            case R.id.tv_no_id:
                noId();
                break;
        }
    }

    private void noId() {
        String name = mEtName.getEditText().getText().toString();

        mEtName.setErrorEnabled(false);

        if (TextUtils.isEmpty(name)) {
            mEtName.setErrorEnabled(true);
            mEtName.setError("请输入姓名");
            return;
        }

        waitWin.showWait();

        AVUser user = new AVUser();
        user.setEmail(mEMail);
        user.setUsername(mEMail);
        user.setPassword(mPwd);
        user.put("Name", name);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                waitWin.dismissWait();
                if (e==null){
                    Toast.makeText(SignInJWCActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInJWCActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(SignInJWCActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void toSign() {
        String studentId = mEtStudentId.getEditText().getText().toString();
        String name = mEtName.getEditText().getText().toString();
        String eCardPwd = mEtECardPwd.getEditText().getText().toString();
        String jwcPwd = mEtJwcPwd.getEditText().getText().toString();

        mEtECardPwd.setErrorEnabled(false);
        mEtName.setErrorEnabled(false);
        mEtStudentId.setErrorEnabled(false);
        mEtJwcPwd.setErrorEnabled(false);

        if (TextUtils.isEmpty(name)) {
            mEtName.setErrorEnabled(true);
            mEtName.setError("请输入姓名");
            return;
        }
        if (TextUtils.isEmpty(studentId)) {
            mEtStudentId.setErrorEnabled(true);
            mEtStudentId.setError("请输入学号");
            return;
        }
        if (TextUtils.isEmpty(eCardPwd)) {
            mEtECardPwd.setErrorEnabled(true);
            mEtECardPwd.setError("请输入一卡通密码");
            return;
        }


        if (studentId.length() >= 14) {
            int year = Integer.parseInt(studentId.substring(0, 4));
            if (year > 2014) {
                if (studentId.length() == 16) {
                    if (TextUtils.isEmpty(jwcPwd)) {
                        mEtJwcPwd.setErrorEnabled(true);
                        mEtJwcPwd.setError("请填写密码或选择无学号注册");
                        return;

                    } else {
                        //还需验证用户名密码能否通过教务管理系统
                        validateStuID_JwcPwd();
                    }
                } else {
                    mEtStudentId.setErrorEnabled(true);
                    mEtStudentId.setError("学号格式有误");
                }
            } else {
                if (studentId.length() == 14) {
                    if (TextUtils.isEmpty(jwcPwd)) {
                        sign(name, studentId, eCardPwd, "");
                    } else {
                        sign(name, studentId, eCardPwd, jwcPwd);
                    }
                } else {
                    mEtStudentId.setErrorEnabled(true);
                    mEtStudentId.setError("学号格式有误");
                }
            }
        } else {
            mEtStudentId.setErrorEnabled(true);
            mEtStudentId.setError("学号格式有误");
        }

    }

    private void sign(String name, String studentId, String eCardPwd, String jwcPwd) {

        waitWin.showWait();

        AVUser user = new AVUser();
        user.setUsername(mEMail);
        user.setEmail(mEMail);
        user.setPassword(mPwd);
        user.put("Name", name);
        user.put("StudentId", studentId);
        user.put("EcardPwd", eCardPwd);
        user.put("JwcPwd",jwcPwd);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                waitWin.dismissWait();
                if (e==null){
                    Toast.makeText(SignInJWCActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignInJWCActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(SignInJWCActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void validateStuID_JwcPwd(){
        //用于验证用户名密码能否登录进入教务系统
        final boolean Validated = false;
        AVQuery<AVObject> query = new AVQuery<>("API");
        query.whereEqualTo("title", "validate");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Map<String, String> map = new ArrayMap<>();
                    map.put("stuid", name);//设置get参数
                        map.put("passwd", jwcPwd);//设置get参数
                    new InternetUtil(validateHandler,basicURL + "profile", map,true,SignInJWCActivity.this);//传入参数

                } else {
                    Toast.makeText(SignInJWCActivity.this, "登入教务处失败\n请检查网络是否顺畅", Toast.LENGTH_SHORT).show();
                    waitWin.dismissWait();
                }
            }
        });


    }




    Handler validateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                waitWin.dismissWait();
                Bundle bundle = (Bundle) msg.obj;
                String validateString = bundle.getString("Error");
                if (validateString!= null){
                    if (validateString.length()!=0){
                        Toast.makeText(SignInJWCActivity.this, "登入教务处失败\n学号或密码错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(SignInJWCActivity.this, "登入教务处失败\n可能为网络原因", Toast.LENGTH_SHORT).show();
            } else {
                //成功验证
                waitWin.dismissWait();
                sign(name,studentId,eCardPwd,jwcPwd);


            }
        }
    };
}

