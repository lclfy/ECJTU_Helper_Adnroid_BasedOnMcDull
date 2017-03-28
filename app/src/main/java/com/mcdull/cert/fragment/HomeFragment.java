package com.mcdull.cert.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.bumptech.glide.load.engine.Resource;
import com.mcdull.cert.Bean.CourseBean;
import com.mcdull.cert.R;
//import com.mcdull.cert.activity.ImportCourseActivity;
import com.mcdull.cert.activity.MyDataActivity;
import com.mcdull.cert.utils.GetIcon;
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.InternetUtil;
import com.mcdull.cert.utils.Util;
import com.umeng.update.UmengUpdateAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mcdull on 15/8/10.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private View view;
    private ViewPager vpMain;//是界面tab选择器
    private List<Fragment> fragments;
    private String studentId;
    private PopupWindow popupWindow;
    //课表刷新按钮
    private ShowWaitPopupWindow waitWin;
    private ImageView mIvTX;
    private TabLayout mTabLayout;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.activity_query, container, false);

        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(getActivity());

        waitWin = new ShowWaitPopupWindow(getActivity());
        String studentId = AVUser.getCurrentUser().getString("StudentId");

        initView();

        init();

        initIv();
//        if (studentId == null) {
//            vpMain.setCurrentItem(0);
//        } else {
//            if (getActivity().getSharedPreferences("setting", MODE_PRIVATE).getBoolean("homeType", false)) {
//                vpMain.setCurrentItem(1);
//            } else {
//                vpMain.setCurrentItem(0);
//            }
//
//        }
        vpMain.setCurrentItem(0);
        Drawable drawable = getResources().getDrawable(R.drawable.icon);
        mIvTX.setImageBitmap(Util.toRoundBitmap(Util.drawableToBitmap(drawable)));
        drawable = null;

        new GetIcon(getActivity(), new GetIcon.GetIconCallBack() {
            @Override
            public void done(Bitmap bitmap) {
                if (bitmap != null) {
                    mIvTX.setImageBitmap(Util.toRoundBitmap(bitmap));
                }
            }
        });

        if (studentId != null) {
            SharedPreferences SP = getActivity().getSharedPreferences("myInfo", MODE_PRIVATE);
            if (SP.getString("name", null) == null) {

                Map<String, String> map = new ArrayMap<>();
                map.put("studentId", studentId);//设置get参数
                String url = "http://luapi.sinaapp.com/getStudentInfo.php";//设置url
                new InternetUtil(infoHandler, url, map);//传入参数
            }
        }

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        //换主题
        SharedPreferences SP = getActivity().getSharedPreferences("config", MODE_PRIVATE);
        getActivity().findViewById(R.id.view_home_title).setBackgroundColor(getActivity().getSharedPreferences("setting", MODE_PRIVATE).getInt("theme", 0xff009688));
        getActivity().findViewById(R.id.tab).setBackgroundColor(getActivity().getSharedPreferences("setting", MODE_PRIVATE).getInt("theme", 0xff009688));


        if (SP.getBoolean("Icon", true)) {
            new GetIcon(getActivity(), new GetIcon.GetIconCallBack() {
                @Override
                public void done(Bitmap bitmap) {
                    if (bitmap != null) {
                        mIvTX.setImageBitmap(Util.toRoundBitmap(bitmap));
                    }
                }
            });
        }
        //mark
        int num = vpMain.getCurrentItem();
        this.studentId = AVUser.getCurrentUser().getString("StudentId");
        if (num == 1 || num == 2) {
            if (TextUtils.isEmpty(studentId)) {
                //提示新生输入学号、
                showEditIdWin();
            }
        }
    }

    private void init() {

        this.studentId = AVUser.getCurrentUser().getString("StudentId");

//        CourseFragment course = new CourseFragment();
       // QueryFragment query = new QueryFragment();
        NewStudentFragment newStudent = new NewStudentFragment();

        fragments = new ArrayList<>();
        fragments.add(newStudent);
        //fragments.add(query);
//        fragments.add(course);

        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getActivity().getSupportFragmentManager()) {
            String[] titles = {""};

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        };

        vpMain.setOffscreenPageLimit(5);
        vpMain.setAdapter(adapter);
        vpMain.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        break;
//                    case 1:
//                        initIv();
//                        if (TextUtils.isEmpty(studentId)) {
//                            //提示新生输入学号、
//                            showEditIdWin();
//                        }
//                        break;
//                    case 1:
//                        initIv();
//                        view.findViewById(R.id.bt_add_course).setVisibility(View.VISIBLE);
//                        if (TextUtils.isEmpty(studentId)) {
//                            //提示新生输入学号、
//                            showEditIdWin();
//                        }
//                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabLayout.setTabsFromPagerAdapter(adapter);
        mTabLayout.setupWithViewPager(vpMain);
    }

    private void showEditIdWin() {
        //未填写学号时展示的窗口
        Rect outRect = new Rect();
        getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        View view = View.inflate(getActivity(), R.layout.win_prompt_id, null);
        view.findViewById(R.id.other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpMain.setCurrentItem(0);
                popupWindow.dismiss();
            }
        });
        view.findViewById(R.id.bt_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入个人信息页
                popupWindow.dismiss();
                Intent intent = new Intent(getActivity(), MyDataActivity.class);
                startActivity(intent);
            }
        });
        popupWindow = new PopupWindow(view, outRect.width(), outRect.height());
        popupWindow.showAsDropDown(View.inflate(getActivity(), R.layout.activity_query, null), 0, outRect.top);
    }

    private void initIv() {
//        view.findViewById(R.id.bt_add_course).setVisibility(View.GONE);
    }


    private void initView() {
//        view.findViewById(R.id.bt_add_course).setOnClickListener(this);
        view.findViewById(R.id.bt_me).setOnClickListener(this);
        vpMain = (ViewPager) view.findViewById(R.id.vp_main);
        mIvTX = (ImageView) view.findViewById(R.id.iv_tx);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.bt_add_course:
//                //如果要添加页面此处需要修改
//                CourseFragment contentFragment = (CourseFragment)fragments.get(1);
//                contentFragment.refreshCourse();
//
//                break;
            case R.id.bt_me:
                openLeftWin();
                break;
        }
    }

    private void openLeftWin() {
        Intent intent = new Intent();
        intent.setAction("com.mcdull.cert.Home");
        intent.putExtra("type",1);
        getActivity().sendBroadcast(intent);
    }

    Handler infoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                Bundle bundle = (Bundle) msg.obj;
                String json = bundle.getString("Json", null);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String departId = jsonObject.getString("departId");
                    if (TextUtils.isEmpty(departId) || departId.equals("null")) {
                        return;
                    }
                    String depart = jsonObject.getString("depart");
                    String year = jsonObject.getString("year");
                    String classId = jsonObject.getString("classId");
                    String className = jsonObject.getString("className");
                    String name = jsonObject.getString("name");
                    String studentId = jsonObject.getString("studentId");
                    SharedPreferences SP = getActivity().getSharedPreferences("myInfo", MODE_PRIVATE);
                    SharedPreferences.Editor edit = SP.edit();
                    edit.putString("departId", departId);
                    edit.putString("depart", depart);
                    edit.putString("year", year);
                    edit.putString("classId", classId);
                    edit.putString("className", className);
                    edit.putString("name", name);
                    edit.putString("studentId", studentId);
                    edit.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (waitWin != null) {
            waitWin.dismissWait();
            waitWin = null;
        }
    }
}
