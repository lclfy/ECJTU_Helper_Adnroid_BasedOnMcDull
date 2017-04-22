package com.mcdull.cert.activity;

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
import com.mcdull.cert.R;
import com.mcdull.cert.activity.base.BaseActivity;
import com.mcdull.cert.utils.IconHelper;
import com.mcdull.cert.utils.InternetUtil;
import com.mcdull.cert.utils.Util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MyDataActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    //    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private TextInputLayout mEtEcardPwd;
    private TextInputLayout mEtEMail;
    private CheckBox mCbMan;
    private CheckBox mCbWoman;
    private TextView mTvName;
    private TextView mTvStudentID;
    private ImageView mIvIcon;
    private int MAN = 1;
    private int WOMAN = -1;
    private AlertDialog alertDialog;
    private Bitmap bmp;
    private Button bt_back;
    private ImageView tv_save;

    public String jwcPwd = "";
    public String eCardPwd= "";
    public String studentId= "";
    public String name= "";
    public int sex = 0;

    //更改一卡通密码后，提示主界面更新一下.
    private SharedPreferences SP;
    private SharedPreferences.Editor edit;
    private String originalECardPwd = "";

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

        initView();


    }

    private void initView() {
        //返回键+保存键
        findViewById(R.id.bt_back).setOnClickListener(this);
        findViewById(R.id.tv_save).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_title)).setText("个人信息");
        this.mIvIcon = (ImageView) findViewById(R.id.iv_icon);
        this.mTvName = (TextView) findViewById(R.id.tv_name);
        this.mTvStudentID = (TextView)findViewById(R.id.tv_student_id);
        this.mCbMan = (CheckBox) findViewById(R.id.cb_man);
        this.mCbWoman = (CheckBox) findViewById(R.id.cb_woman);
        this.mEtEcardPwd = (TextInputLayout) findViewById(R.id.et_ecard_pwd);
        this.mEtEMail = (TextInputLayout) findViewById(R.id.et_email);
        mEtEcardPwd.setHint("一卡通密码");
        mEtEMail.setHint("电子邮箱");
        mEtEMail.setErrorEnabled(false);

        mIvIcon.setOnClickListener(this);
        mCbMan.setOnCheckedChangeListener(this);
        mCbWoman.setOnCheckedChangeListener(this);

        String name = AVUser.getCurrentUser().getString("Name");
        mTvName.setText(name);
        String studentID = AVUser.getCurrentUser().getString("StudentId");
        mTvStudentID.setText(studentID);


        IconHelper.getIcon(MyDataActivity.this, new IconHelper.GetIconCallBack() {
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

        String Email = AVUser.getCurrentUser().getEmail();
        if (!TextUtils.isEmpty(Email)) {
            mEtEMail.getEditText().setText(Email);
        }
        //判断一卡通密码改了没
        SP = getSharedPreferences("config", MODE_PRIVATE);
        edit = SP.edit();
        String eCardPwd = AVUser.getCurrentUser().getString("EcardPwd");
        originalECardPwd = eCardPwd;
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
            case R.id.bt_back:
                finish();
                break;
            case R.id.tv_save:
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

        if (!TextUtils.isEmpty(mEtEcardPwd.getEditText().getText().toString())) {
            eCardPwd = mEtEcardPwd.getEditText().getText().toString();
            if (!mEtEcardPwd.getEditText().getText().toString().equals(originalECardPwd)){
                edit.putBoolean("eCardPwdChanged",true);
                edit.commit();
            }
        }
        Save();

    }
    //验证通过后方可储存
    private void Save(){
        AVUser user = AVUser.getCurrentUser();
        user.put("Sex", sex);
        user.put("EcardPwd", eCardPwd);
        user.setEmail(mEtEMail.getEditText().getText().toString());
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
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String error = "错误"+ e.toString().split("error")[1].replaceAll("\"","").replaceAll("\\}","");
                    Toast.makeText(MyDataActivity.this,error, Toast.LENGTH_SHORT).show();
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
        DialogView.findViewById(R.id.bt_no).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        DialogView.findViewById(R.id.bt_yes).setOnClickListener(new View.OnClickListener() {

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
        }

}
