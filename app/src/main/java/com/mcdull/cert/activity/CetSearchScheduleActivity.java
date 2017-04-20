package com.mcdull.cert.activity;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputLayout;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.mcdull.cert.activity.base.BaseThemeActivity;
import com.mcdull.cert.R;
import com.mcdull.cert.anim.ShakeAnim;
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.InternetUtil;
import com.mcdull.cert.utils.Util;

public class CetSearchScheduleActivity extends BaseThemeActivity implements
        OnClickListener {

    private TextView tvQueryTitle;
    private Button btSure;
    private TextInputLayout etCetName;
    private TextInputLayout etCetZkzh;
    private ShowWaitPopupWindow waitWin;

    Handler cetSearchHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                waitWin.dismissWait();
                ShakeAnim skAnim = new ShakeAnim();
                skAnim.setDuration(1000);
                btSure.startAnimation(skAnim);
                Toast.makeText(CetSearchScheduleActivity.this, "查询失败，请检查网络设置",
                        Toast.LENGTH_SHORT).show();
            } else {
                waitWin.dismissWait();

                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json");

                if (Util.replace(json).equals("false")) {
                    Toast.makeText(CetSearchScheduleActivity.this, "查询失败", Toast.LENGTH_SHORT)
                            .show();
                    ShakeAnim skAnim = new ShakeAnim();
                    skAnim.setDuration(1000);
                    btSure.startAnimation(skAnim);
                    return;
                }

                Intent intent = new Intent(CetSearchScheduleActivity.this,CetSearchActivity.class);
                intent.putExtra("cetSearchJson",json);
                startActivity(intent);
                finish();

            }
        }
    };

    @Override
    protected void onTheme(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cetsearch_schedule);

        initView();

        init();
    }

    private void init() {
        tvQueryTitle.setText("四六级查询");
        btSure.setOnClickListener(this);
    }

    private void initView() {
        tvQueryTitle = (TextView) findViewById(R.id.tv_title);
        btSure = (Button) findViewById(R.id.bt_sure);
        etCetName = (TextInputLayout) findViewById(R.id.et_cet_name);
        etCetZkzh = (TextInputLayout) findViewById(R.id.et_cet_zkzh);
        etCetName.setHint("姓名");
        etCetZkzh.setHint("准考证号");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_sure:
                enterCetSearch();
                break;
        }
    }

    private void enterCetSearch() {
        String cetName = etCetName.getEditText().getText().toString();
        String CetZkzh = etCetZkzh.getEditText().getText().toString();
        boolean isError = false;
        etCetZkzh.setErrorEnabled(false);
        etCetName.setErrorEnabled(false);
        if (TextUtils.isEmpty(CetZkzh)) {
            isError = true;
            etCetZkzh.setError("请输入准考证号");
            etCetZkzh.setErrorEnabled(true);
        }
        if (TextUtils.isEmpty(cetName)) {
            isError = true;
            etCetName.setError("请输入姓名");
            etCetName.setErrorEnabled(true);
        }

        if (isError)
            return;

        waitWin = new ShowWaitPopupWindow(this);
        waitWin.showWait();
        AVQuery<AVObject> query = new AVQuery<>("API");
        query.whereEqualTo("title", "cet");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Map<String, String> map = new ArrayMap<>();
                    map.put("zkzh", etCetZkzh.getEditText().getText().toString());//设置get参数
                    map.put("name", etCetName.getEditText().getText().toString());//设置get参数
                    new InternetUtil(cetSearchHandler, list.get(0).getString("url"), map);//传入参数
                } else {
                    Toast.makeText(CetSearchScheduleActivity.this, "查询失败，请检查网络是否顺畅", Toast.LENGTH_SHORT).show();
                    waitWin.dismissWait();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (waitWin!=null){
            waitWin.dismissWait();
            waitWin = null;
        }
        tvQueryTitle = null;
        btSure = null;
        etCetName = null;
        etCetZkzh = null;
    }

}
