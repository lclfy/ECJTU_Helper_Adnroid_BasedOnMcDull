package com.mcdull.cert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mcdull.cert.ActivityMode.MyTitleActivity;
import com.mcdull.cert.R;
import com.mcdull.cert.utils.Util;
import com.umeng.update.UmengUpdateAgent;

public class AboutActivity extends MyTitleActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about);

        super.onCreate(savedInstanceState);

        initView();

    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("关于");
        findViewById(R.id.bt_bug).setOnClickListener(this);
        findViewById(R.id.bt_updata).setOnClickListener(this);
        findViewById(R.id.bt_cert).setOnClickListener(this);
        ((TextView) findViewById(R.id.text)).setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_bug:
                Intent intent = new Intent(AboutActivity.this, BugFeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_updata:
                UmengUpdateAgent.forceUpdate(AboutActivity.this);
                if (!UmengUpdateAgent.getUpdateFromPushMessage()) {
                    Toast.makeText(this, "已是最新版", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_cert:
                intent = new Intent(AboutActivity.this, CERTActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
