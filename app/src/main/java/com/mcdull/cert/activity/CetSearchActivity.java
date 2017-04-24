package com.mcdull.cert.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.google.gson.Gson;
import com.mcdull.cert.HJXYT;
import com.mcdull.cert.bean.CETBean;
import com.mcdull.cert.R;
import com.mcdull.cert.activity.base.BaseActivity;
import com.mcdull.cert.bean.ExamTimeBean;
import com.mingle.widget.ShapeLoadingDialog;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CetSearchActivity extends BaseActivity {

    private TextView mTvCetName;
    private TextView mTvCetSchool;
    private TextView mTvCetTotalScore;
    private TextView mTvCetListeningScore;
    private TextView mTvCetReadingScore;
    private TextView mTvCetWritingScore;
    private TextView mTvCetSpeakLevel;
    private TextView mTvType;

    private String CETName = "";
    private String CETNum = "";
    private CETBean mCETData;

    private ShapeLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cet_search);
        super.onCreate(savedInstanceState);
        //判断SDK版本，设置沉浸状态栏
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            findViewById(R.id.status_bar).setVisibility(View.VISIBLE);
        }
        //返回键
        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLoadingDialog = new ShapeLoadingDialog(this);
        CETSetNameAndID();
        initView();
    }

    private void init(CETBean CETData) {
        if (CETData != null){
            if (CETData.data != null) {
                mTvCetName.setText(CETData.data.name);
                mTvCetSchool.setText(CETData.data.school);
                mTvType.setText("全国大学"+CETData.data.type+"考试");

                mTvCetTotalScore.setText(CETData.data.w_test.total);
                mTvCetListeningScore.setText(CETData.data.w_test.listen);
                mTvCetReadingScore.setText(CETData.data.w_test.reading);
                mTvCetWritingScore.setText(CETData.data.w_test.writing);

                mTvCetSpeakLevel.setText(CETData.data.s_test.level);
        }else {
                CETSetNameAndID();
                Toast.makeText(CetSearchActivity.this, "查询失败，请重试", Toast.LENGTH_SHORT).show();
            }
        }else {
            CETSetNameAndID();
            Toast.makeText(CetSearchActivity.this, "查询失败，请重试", Toast.LENGTH_SHORT).show();
        }
        mLoadingDialog.dismiss();

    }


    private void initView() {
        mTvCetName = (TextView) findViewById(R.id.tv_cetName);
        mTvCetSchool = (TextView) findViewById(R.id.tv_cetSchool);
        mTvType = (TextView) findViewById(R.id.tv_type);
        mTvCetTotalScore = (TextView) findViewById(R.id.tv_cetTotalScore);
        mTvCetListeningScore = (TextView) findViewById(R.id.tv_cetListeningScore);
        mTvCetReadingScore = (TextView) findViewById(R.id.tv_cetReadingScore);
        mTvCetWritingScore = (TextView) findViewById(R.id.tv_cetWritingScore);
        mTvCetSpeakLevel = (TextView) findViewById(R.id.tv_cetSpeakLevel);

    }

    //弹一个输入框给输入一下准考证号和姓名
    private void CETSetNameAndID(){
        LayoutInflater factory = LayoutInflater.from(CetSearchActivity.this);
        final View textEntryView = factory.inflate(R.layout.alert_cet, null);
        final EditText editTextName = (EditText) textEntryView.findViewById(R.id.editTextName);
        final EditText editTextNumEditText = (EditText)textEntryView.findViewById(R.id.editTextNum);
        if (CETName.length()!= 0){
            editTextName.setText(CETName);
        }if (CETNum.length()!= 0){
            editTextNumEditText.setText(CETNum);
        }
        AlertDialog.Builder ad1 = new AlertDialog.Builder(CetSearchActivity.this);
        ad1.setTitle("请输入姓名与准考证号");
        ad1.setIcon(R.drawable.ic_img_cet);
        ad1.setView(textEntryView);
        ad1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                //临时保存一下，避免对话框消失了数据也没了
                CETName = editTextName.getText().toString();
                CETNum = editTextNumEditText.getText().toString();
                if (editTextName.getText().toString().length()==0 ||
                        editTextNumEditText.getText().toString().length() == 0){
                    Toast.makeText(CetSearchActivity.this, "请填写信息", Toast.LENGTH_SHORT).show();
                    CETSetNameAndID();
                }else {
                    //判断姓名中是否有英文数字
                    Pattern p = Pattern.compile("[0-9]*");
                    Matcher m = p.matcher(editTextName.getText().toString());
                    if(m.matches() ){
                        Toast.makeText(CetSearchActivity.this, "请填写正确的姓名", Toast.LENGTH_SHORT).show();
                        CETSetNameAndID();
                        return;
                    }
                    p=Pattern.compile("[a-zA-Z]");
                    m=p.matcher(editTextName.getText().toString());
                    if(m.matches()){
                        Toast.makeText(CetSearchActivity.this, "请填写正确的姓名", Toast.LENGTH_SHORT).show();
                        CETSetNameAndID();
                        return;
                    }
                    if (editTextNumEditText.getText().toString().length() > 15){
                        Toast.makeText(CetSearchActivity.this, "请输入正确的准考证号", Toast.LENGTH_SHORT).show();
                        CETSetNameAndID();
                        return;
                    }
//                    Map<String, String> map = new ArrayMap<String, String>();
//                    测试用数据
//                    map.put("name","丁睿玄");
//                    map.put("crtNum","360040162206803");
                    searchCETData(editTextName.getText().toString(),editTextNumEditText.getText().toString());
                }
            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                finish();
            }
        });
        ad1.show();// 显示对话框
    }

    private void searchCETData(String CETName,String CETNum){
        mLoadingDialog.show();
        HJXYT.getInstance().getAppClient().getJWXTService().getCETBean(CETName, CETNum).enqueue(new Callback<CETBean>() {
            @Override
            public void onResponse(Call<CETBean> call, Response<CETBean> response) {
                mCETData = response.body();
                init(mCETData);
            }

            @Override
            public void onFailure(Call<CETBean> call, Throwable t) {
                mLoadingDialog.dismiss();
                Toast.makeText(CetSearchActivity.this, "查询失败，请重试", Toast.LENGTH_SHORT).show();
                CETSetNameAndID();
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
