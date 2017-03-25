package com.mcdull.cert.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetDataCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mcdull on 15/8/24.
 */
public class GetIcon {
    private Context context;
    private GetIconCallBack callBack;

    /**
     * 获取头像的方法
     *
     * @param context 传入一个上下文
     * @return 如果获取到了头像则返回一个Bitmap 如果没有则返回null
     */
    public GetIcon(Context context, GetIconCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        Bitmap bitmap = null;
        try {
            FileInputStream fis = context.openFileInput("Icon.png");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buf = new byte[1024];
            int len = 0;

            //将读取后的数据放置在内存中---ByteArrayOutputStream
            while ((len = fis.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            fis.close();
            baos.close();

            //返回内存中存储的数据
            bitmap = Util.Bytes2Bitmap(baos.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            callBack.done(null);
        }

        if (bitmap == null) {
            if (AVUser.getCurrentUser() == null)
                this.callBack.done(null);
            if (AVUser.getCurrentUser() != null) {
                AVFile file = AVUser.getCurrentUser().getAVFile("Icon");
                if (file != null) {
                    file.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, AVException e) {
                            if (bytes != null) {
                                Bitmap bitmap = Util.Bytes2Bitmap(bytes);
                                try {
                                    FileOutputStream fos = GetIcon.this.context.openFileOutput("Icon.png", Context.MODE_PRIVATE);
                                    fos.write(bytes);
                                    fos.close();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                GetIcon.this.callBack.done(bitmap);
                            } else {
                                GetIcon.this.callBack.done(null);
                            }
                        }
                    });
                } else {
                    this.callBack.done(null);
                }
            }else {
                this.callBack.done(null);
            }
        } else {
            this.callBack.done(bitmap);
        }
    }

    public interface GetIconCallBack {
        public void done(Bitmap bitmap);
    }
}
