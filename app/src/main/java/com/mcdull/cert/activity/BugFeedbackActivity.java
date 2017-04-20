package com.mcdull.cert.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.mcdull.cert.activity.base.BaseThemeActivity;
import com.mcdull.cert.R;

public class BugFeedbackActivity extends BaseThemeActivity implements View.OnClickListener {

    private EditText mEditText;

    @Override
    protected void onTheme(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bug_feedback);

        ((TextView) findViewById(R.id.tv_title)).setText("意见建议");
        findViewById(R.id.bt_feed).setOnClickListener(this);
        mEditText = (EditText) findViewById(R.id.editText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_feed:
                if (TextUtils.isEmpty(mEditText.getText().toString())) {
                    Toast.makeText(this, "内容不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                AVObject object = new AVObject("BugFeedBack");
                object.put("email", AVUser.getCurrentUser().getEmail());
                if (!TextUtils.isEmpty(AVUser.getCurrentUser().getString("StudentId")))
                    object.put("studentId", AVUser.getCurrentUser().getString("StudentId"));
                object.put("bug", mEditText.getText().toString());
                object.put("type", "Android");
                object.put("name", AVUser.getCurrentUser().getString("Name"));
                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            mEditText.setText("");
                            Toast.makeText(BugFeedbackActivity.this, "反馈成功。感谢您的支持！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BugFeedbackActivity.this, "反馈失败。请检查你的网络状况", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }
}
