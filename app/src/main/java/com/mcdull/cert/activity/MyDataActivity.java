package com.mcdull.cert.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.mcdull.cert.ActivityMode.MyTitleActivity;
import com.mcdull.cert.R;
import com.mcdull.cert.utils.GetIcon;
import com.mcdull.cert.utils.InternetUtil;
import com.mcdull.cert.utils.ShowWaitPopupWindow;
import com.mcdull.cert.utils.Util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MyDataActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    //    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private TextInputLayout mEtJwcPwd;
    private TextInputLayout mEtStudentId;
    private TextInputLayout mEtEcardPwd;
    private CheckBox mCbMan;
    private CheckBox mCbWoman;
    private TextView mTvName;
    private ImageView mIvIcon;
    private int MAN = 1;
    private int WOMAN = -1;
    private AlertDialog alertDialog;
    private Bitmap bmp;
    private ShowWaitPopupWindow waitWin;
    private Button bt_back;
    private ImageView tv_save;

    public String jwcPwd = "";
    public String eCardPwd= "";
    public String studentId= "";
    public String name= "";
    public int sex = 0;

    public String basicURL = "http://api1.ecjtu.org/v1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_data);
        super.onCreate(savedInstanceState);
        //判断SDK版本，设置沉浸状态栏
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            findViewById(R.id.status_bar).setVisibility(View.VISIBLE);
        }
        waitWin = new ShowWaitPopupWindow(this);

        initView();


    }

    private void initView() {
        //返回键
        bt_back = (Button)findViewById(R.id.bt_back);
        tv_save = (ImageView) findViewById(R.id.tv_save);
        ((TextView) findViewById(R.id.tv_title)).setText("个人信息");
        findViewById(R.id.bt_name).setOnClickListener(this);
        this.mIvIcon = (ImageView) findViewById(R.id.iv_icon);
        this.mTvName = (TextView) findViewById(R.id.tv_name);
        this.mCbMan = (CheckBox) findViewById(R.id.cb_man);
        this.mCbWoman = (CheckBox) findViewById(R.id.cb_woman);

        this.mEtStudentId = (TextInputLayout) findViewById(R.id.et_student_id);
        this.mEtJwcPwd = (TextInputLayout) findViewById(R.id.et_jwc_pwd);
        this.mEtEcardPwd = (TextInputLayout) findViewById(R.id.et_ecard_pwd);
        mEtStudentId.setHint("学号");
        mEtJwcPwd.setHint("学分制教务系统密码");
        mEtEcardPwd.setHint("一卡通密码");
        mEtStudentId.setErrorEnabled(false);

        tv_save.setOnClickListener(this);
        mIvIcon.setOnClickListener(this);
        mCbMan.setOnCheckedChangeListener(this);
        mCbWoman.setOnCheckedChangeListener(this);
        bt_back.setOnClickListener(this);

        String name = AVUser.getCurrentUser().getString("Name");
        mTvName.setText(name);

        new GetIcon(MyDataActivity.this, new GetIcon.GetIconCallBack() {
            @Override
            public void done(Bitmap bitmap) {
                if (bitmap != null) {
                    MyDataActivity.this.bmp = bitmap;
                    mIvIcon.setImageBitmap(Util.toRoundBitmap(bitmap));
                }else {

                }
            }
        });

        int sex = AVUser.getCurrentUser().getInt("Sex");
        if (sex == WOMAN) {
            mCbMan.setChecked(false);
            mCbWoman.setChecked(true);
        } else if (sex == MAN) {
            mCbMan.setChecked(true);
            mCbWoman.setChecked(false);
        }

        String studentId = AVUser.getCurrentUser().getString("StudentId");
        if (!TextUtils.isEmpty(studentId)) {
            mEtStudentId.getEditText().setText(studentId);
        }

        String jwcPwd = AVUser.getCurrentUser().getString("JwcPwd");
        if (!TextUtils.isEmpty(jwcPwd)) {
            mEtJwcPwd.getEditText().setText(jwcPwd);
        }


        String eCardPwd = AVUser.getCurrentUser().getString("EcardPwd");
        if (!TextUtils.isEmpty(eCardPwd)) {
            mEtEcardPwd.getEditText().setText(eCardPwd);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_icon:
                selectIcon();
                break;
            case R.id.bt_name:
                showEditName();
                break;
            case R.id.bt_back:
                finish();
                break;
            case R.id.tv_save:
                mEtStudentId.setErrorEnabled(false);
                saveButton();
                break;
        }
    }

    private void selectIcon() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_REQUEST_GALLERY:
                if (data != null) {
                    startPhotoZoom(data.getData(), 150);
                }
                break;

            case PHOTO_REQUEST_CUT:
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle == null) {
            Toast.makeText(this, "Sorry，部分三星手机暂不支持自定义头像，后期我们将会解决这个问题。", Toast.LENGTH_SHORT).show();
        } else {
            bmp = bundle.getParcelable("data");

            mIvIcon.setImageBitmap(Util.toRoundBitmap(bmp));
        }
    }

    private void saveButton() {
         name = mTvName.getText().toString();
        if (mCbMan.isChecked()) {
            sex = MAN;
        }
        if (mCbWoman.isChecked()) {
            sex = WOMAN;
        }
        if (!TextUtils.isEmpty(mEtStudentId.getEditText().getText().toString())) {
            studentId = mEtStudentId.getEditText().getText().toString();
            if (studentId.length() != 14 && studentId.length() != 16) {
                mEtStudentId.setError("学号格式有误");
                mEtStudentId.setErrorEnabled(true);
//                Toast.makeText(getApplicationContext(), "学号格式有误", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!TextUtils.isEmpty(mEtJwcPwd.getEditText().getText().toString())) {
            jwcPwd = mEtJwcPwd.getEditText().getText().toString();
        }

        if (!TextUtils.isEmpty(mEtEcardPwd.getEditText().getText().toString())) {
            eCardPwd = mEtEcardPwd.getEditText().getText().toString();
        }
        waitWin.showWait();

        if (studentId.length() == 16){
            //需要验证教务处用户密码
            validateStuID_JwcPwd();
        }else {
            //13,14级直接保存
            validatedSave();
        }

    }
    public void validateStuID_JwcPwd(){
        //用于验证用户名密码能否登录进入教务系统
        AVQuery<AVObject> query = new AVQuery<>("API");
        query.whereEqualTo("title", "validate");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    Map<String, String> map = new ArrayMap<>();
                    map.put("stuid", studentId);//设置get参数
                    map.put("passwd", jwcPwd);//设置get参数
                    new InternetUtil(validateHandler,basicURL + "profile", map,true,MyDataActivity.this);//传入参数

                } else {
                    Toast.makeText(MyDataActivity.this, "登入教务处失败\n请检查网络是否顺畅", Toast.LENGTH_SHORT).show();
                    waitWin.dismissWait();
                }
            }
        });


    }

    Handler validateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                waitWin.dismissWait();
                Bundle bundle = (Bundle) msg.obj;
                String validateString = bundle.getString("Error");
                if (validateString!= null){
                    if (validateString.length()!=0){
                        Toast.makeText(MyDataActivity.this, "登入教务处失败\n学号或密码错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(MyDataActivity.this, "登入教务处失败\n可能为网络原因", Toast.LENGTH_SHORT).show();
            } else {
                //成功验证
                waitWin.dismissWait();
                validatedSave();

            }
        }
    };
    //验证通过后方可储存
    private void validatedSave(){
        AVUser user = AVUser.getCurrentUser();
        user.put("Name", name);
        user.put("Sex", sex);
        user.put("StudentId", studentId);
        user.put("JwcPwd", jwcPwd);
        user.put("EcardPwd", eCardPwd);
        if (bmp != null) {

            AVFile icon = user.getAVFile("Icon");
            if (icon != null) {
                icon.deleteInBackground();
            }

            AVFile file = new AVFile(AVUser.getCurrentUser().getUsername(), Util.Bitmap2Bytes(bmp));
            user.put("Icon", file);

            try {
                FileOutputStream fos = openFileOutput("Icon.png", MODE_PRIVATE);
                fos.write(Util.Bitmap2Bytes(bmp));
                fos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            SharedPreferences SP = getSharedPreferences("config", MODE_PRIVATE);
            SharedPreferences.Editor edit = SP.edit();
            edit.putBoolean("Icon", true);
            edit.apply();
        }
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                waitWin.dismissWait();
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "保存失败，请检查网络设置或重试", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_man:
                Log.d("Check", isChecked + "男");
                if (isChecked) {
                    mCbWoman.setChecked(false);
                }
                break;
            case R.id.cb_woman:
                Log.d("Check", isChecked + "女");
                if (isChecked) {
                    mCbMan.setChecked(false);
                }
                break;
        }
    }

    private void showEditName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyDataActivity.this);
        View DialogView = View.inflate(MyDataActivity.this, R.layout.edit_dialog, null);
        final EditText editText = (EditText) DialogView.findViewById(R.id.et_tooltip);
        Button btSure = (Button) DialogView.findViewById(R.id.bt_yes);
        Button btCanCel = (Button) DialogView.findViewById(R.id.bt_no);
        btCanCel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btSure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                mTvName.setText(name);
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.setView(DialogView, 0, 0, 0, 0);
        alertDialog.show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (waitWin != null) {
            waitWin.dismissWait();
            waitWin = null;
        }
    }
}
