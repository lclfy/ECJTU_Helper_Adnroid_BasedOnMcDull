package com.mcdull.cert.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mcdull.cert.activity.base.BaseThemeActivity;
import com.mcdull.cert.R;

public class AboutActivity extends BaseThemeActivity implements View.OnClickListener {

    @Override
    protected void onTheme(Bundle savedInstanceState) {
        setContentView(R.layout.activity_about);

        initView();

    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("关于");
        findViewById(R.id.bt_bug).setOnClickListener(this);
        findViewById(R.id.bt_updata).setOnClickListener(this);
        findViewById(R.id.bt_cert).setOnClickListener(this);
        ((TextView) findViewById(R.id.text)).setText("2.0.0 Beta");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_bug:
                Intent intent = new Intent(AboutActivity.this, BugFeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_updata:
//                UmengUpdateAgent.forceUpdate(AboutActivity.this);
//                if (!UmengUpdateAgent.getUpdateFromPushMessage()) {
//                    Toast.makeText(this, "已是最新版", Toast.LENGTH_SHORT).show();
//                }
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
