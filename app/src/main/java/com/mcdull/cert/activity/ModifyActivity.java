package com.mcdull.cert.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mcdull.cert.activity.base.BaseThemeActivity;
import com.mcdull.cert.R;
import com.mcdull.cert.utils.CourseUtil;

import java.util.LinkedList;
import java.util.List;

public class ModifyActivity extends BaseThemeActivity implements View.OnClickListener {

    private TextInputLayout mEtName;
    private TextInputLayout mEtTeacher;
    private TextInputLayout mEtSite;
    private TextInputLayout mEtWeek;
    private int[] type;

    @Override
    protected void onTheme(Bundle savedInstanceState) {
        setContentView(R.layout.activity_modify);

        type = getIntent().getIntArrayExtra("type");
        List<String> course = CourseUtil.getCourse(type[0], type[1], this);

        initView();

        if (course != null && !TextUtils.isEmpty(course.get(0))) {
            mEtName.getEditText().setText(course.get(0));
            mEtTeacher.getEditText().setText(course.get(1));
            mEtSite.getEditText().setText(course.get(2));
            mEtWeek.getEditText().setText(course.get(3));
        }
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("修改课表");
        findViewById(R.id.bt_save).setOnClickListener(this);
        mEtName = (TextInputLayout) findViewById(R.id.et_name);
        mEtTeacher = (TextInputLayout) findViewById(R.id.et_teacher);
        mEtSite = (TextInputLayout) findViewById(R.id.et_site);
        mEtWeek = (TextInputLayout) findViewById(R.id.et_week);
        mEtName.setHint("课程名称");
        mEtTeacher.setHint("授课教师");
        mEtSite.setHint("上课地点");
        mEtWeek.setHint("上课周次");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                modify();
                break;
        }
    }

    private void modify() {
        mEtName.setErrorEnabled(false);
        String name = mEtName.getEditText().getText().toString();
        String teacher = mEtTeacher.getEditText().getText().toString();
        String site = mEtSite.getEditText().getText().toString();
        String week = mEtWeek.getEditText().getText().toString();
        List<String> list = new LinkedList<>();
        if (!TextUtils.isEmpty(name)) {
            list.add(name);
        } else {
            mEtName.setError("课程名称不能为空");
            mEtName.setErrorEnabled(true);
            return;
        }
        if (!TextUtils.isEmpty(teacher)) {
            list.add(teacher);
        } else {
            list.add("");
        }
        if (!TextUtils.isEmpty(site)) {
            list.add(site);
        } else {
            list.add("");
        }
        if (!TextUtils.isEmpty(week)) {
            list.add(week);
        } else {
            list.add("");
        }
        CourseUtil.modifyCourse(type[0], type[1], list, this);
        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        finish();
    }
}
