package com.mcdull.cert.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcdull.cert.ActivityMode.MyTitleActivity;
import com.mcdull.cert.R;
import com.mcdull.cert.anim.ShakeAnim;
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.InternetUtil;


/*
已废弃
 */


public class LibraryScheduleActivity extends MyTitleActivity implements
        OnClickListener {

    private TextView tvQueryTitle;
    private RelativeLayout btSure;
    private EditText etStudentId;
    private EditText etLibraryPwd;
    private ShowWaitPopupWindow waitWin;

    Handler LibraryHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                waitWin.dismissWait();
                Toast.makeText(LibraryScheduleActivity.this, "查询失败，请检查网络链接", Toast.LENGTH_SHORT).show();
                ShakeAnim skAnim = new ShakeAnim();
                skAnim.setDuration(1000);
                btSure.startAnimation(skAnim);
                return;
            }else {
                waitWin.dismissWait();
                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");
                if (json.equals("false\n")){
                    Toast.makeText(LibraryScheduleActivity.this, "查询失败", Toast.LENGTH_SHORT).show();
                    ShakeAnim skAnim = new ShakeAnim();
                    skAnim.setDuration(1000);
                    btSure.startAnimation(skAnim);
                    return;
                }

//                Editor edit = SP.edit();
//                edit.putString("libraryJson", json);
//                edit.commit();

                Intent intent = new Intent(LibraryScheduleActivity.this	, LibraryActivity.class);
                intent.putExtra("libraryJson",json);
                startActivity(intent);
                finish();
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_library_schedule);
        super.onCreate(savedInstanceState);

        initView();

        init();
    }

    private void init() {
//        SP = getSharedPreferences("date", MODE_PRIVATE);
        tvQueryTitle.setText("图书馆查询");
        btSure.setOnClickListener(this);

    }

    private void initView() {
        tvQueryTitle = (TextView) findViewById(R.id.tv_title);
        btSure = (RelativeLayout) findViewById(R.id.bt_sure);
        etStudentId = (EditText) findViewById(R.id.et_student_id);
        etLibraryPwd = (EditText) findViewById(R.id.et_library_pwd);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sure:
                enterLibrary();
                break;
        }
    }

    private void enterLibrary() {
        String studentId = etStudentId.getText().toString();
        String libraryPwd = etLibraryPwd.getText().toString();
        if (TextUtils.isEmpty(studentId)) {
            Toast.makeText(this, "请选输入学号", Toast.LENGTH_SHORT).show();
            ShakeAnim skAnim = new ShakeAnim();
            skAnim.setDuration(1000);
            btSure.startAnimation(skAnim);
            return;
        }
        if (studentId.length() != 14) {
            Toast.makeText(this, "请检查学号是否正确", Toast.LENGTH_SHORT).show();
            ShakeAnim skAnim = new ShakeAnim();
            skAnim.setDuration(1000);
            btSure.startAnimation(skAnim);
            return;
        }

        waitWin = new ShowWaitPopupWindow(LibraryScheduleActivity.this);

        waitWin.showWait();

        Map<String, String> map = new HashMap<>();
        map.put("uid", etStudentId.getText().toString());//设置get参数
        map.put("pwd", etLibraryPwd.getText().toString());//设置get参数
        String url = "http://api.ecjtu.org/func/jwc/library.php";//设置url
        InternetUtil internetUtil = new InternetUtil(LibraryHandler, url, map);//传入参数

    }

}
