package com.mcdull.cert.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mcdull.cert.activity.base.BaseThemeActivity;
import com.mcdull.cert.R;
import com.mcdull.cert.utils.db.CourseDao;
import com.mcdull.cert.view.CourseView;

public class CourseActivity extends BaseThemeActivity {

    private CourseView mCourseView;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onTheme(Bundle savedInstanceState) {
        setContentView(R.layout.course_activity_new);
        //标题
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("全部课程");
        this.mCourseView = (CourseView) findViewById(R.id.course_view);
        CourseDao courseDao = new CourseDao(this);
        mCourseView.setCourse(courseDao.findAll());
    }

}
