package com.mcdull.cert.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.mcdull.cert.R;
import com.mcdull.cert.activity.CetSearchScheduleActivity;
import com.mcdull.cert.activity.ECardActivity;
import com.mcdull.cert.activity.ExamActivity;
import com.mcdull.cert.activity.LibraryActivity;
import com.mcdull.cert.activity.ReExamActivity;
import com.mcdull.cert.activity.RepairActivity;
import com.mcdull.cert.activity.RepairSucActivity;
import com.mcdull.cert.activity.ScoreActivity;
import com.mcdull.cert.activity.TripActivity;
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.InternetUtil;
import com.mcdull.cert.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class QueryFragment extends Fragment implements View.OnClickListener {
    private View view;
    private Intent intent;
    private ShowWaitPopupWindow waitWin;
    private AVUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_query, container, false);
        user = AVUser.getCurrentUser();
        return view;
    }

    public void onClick(View v) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (waitWin != null) {
            waitWin.dismissWait();
            waitWin = null;
        }
    }

}
