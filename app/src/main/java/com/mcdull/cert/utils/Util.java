package com.mcdull.cert.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mcdull on 15/8/14.
 */


public class Util {
    /**
     * 去掉字符串中的\r和\n
     *
     * @param s 传入一个待操作的字符串
     * @return 返回操作后的字符串
     */
    public static String replace(String s) {
        return s.replaceAll("\r|\n", "");
    }


    /**
     * 去掉Email中的- . _
     *
     * @param s 传入一个Email地址
     * @return 返回处理后的String
     */
    public static String toAlias(String s) {
        String[] a = s.split("@");
        return a[0].replaceAll("_|-|\\.", "");
    }

    /**
     * 流转String
     *
     * @param is 传入一个InputStream
     * @return String 返回一个String
     */
    public static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }




    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getLocalVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return "";
        }
        return packageInfo.versionName;

    }


    /**
     * 获取系统当前的年份
     */
    public static int getSystemYear() {
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy");
        return Integer.parseInt(DateFormat.format(new java.util.Date()));
    }

    /**
     * byte转Bitmap
     *
     * @param b 传入一个byte数组
     * @return 返回一个Bitmap
     */
    public static Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 删除icon
     *
     * @param context 传入一个Context
     */
    public static void removeIcon(Context context) {
        File file = new File(context.getFilesDir(), "Icon.png");
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }


    /**
     * 获取系统当前时间 返回数组是年份和1或2 月份为7-12月为1 1-6月为2
     *
     * @return 返回一个int数组
     */
    public static int[] getSystemTime() {
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy");
        int year = Integer.parseInt(DateFormat.format(new java.util.Date()));
        DateFormat = new SimpleDateFormat("MM");
        int month = Integer.parseInt(DateFormat.format(new java.util.Date()));
        if (month > 06) {
            month = 1;
        } else {
            month = 2;
            year = year - 1;
        }
        return new int[]{year, month};
    }


    /**
     * Bitmap转is
     *
     * @param bm 传入一个BitMap
     * @return 返回一个byte数组
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static boolean matcherEmali(String email) {
        String check = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    /**
     * 将图片转换为圆形
     *
     * @param bitmap
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        //圆形图片宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //正方形的边长
        int r = 0;
        //取最短边做边长
        if (width > height) {
            r = height;
        } else {
            r = width;
        }
        //构建一个bitmap
        Bitmap backgroundBmp = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        //new一个Canvas，在backgroundBmp上画图
        Canvas canvas = new Canvas(backgroundBmp);
        Paint paint = new Paint();
        //设置边缘光滑，去掉锯齿
        paint.setAntiAlias(true);
        //宽高相等，即正方形
        RectF rect = new RectF(0, 0, r, r);
        //通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
        //且都等于r/2时，画出来的圆角矩形就是圆形
        canvas.drawRoundRect(rect, r / 2, r / 2, paint);
        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //canvas将bitmap画在backgroundBmp上
        canvas.drawBitmap(bitmap, null, rect, paint);
        //返回已经绘画好的backgroundBmp
        return backgroundBmp;
    }

    /**
     * Drawable转bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();// 取drawable的长宽
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;// 取drawable的颜色格式
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);// 建立对应bitmap
        Canvas canvas = new Canvas(bitmap);// 建立对应bitmap的画布
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);// 把drawable内容画到画布中
        return bitmap;
    }

    /**
     * Resource转Drawable
     * @param res
     * @param context
     * @returnR
     */
    public static Drawable resourceToDrawable(int res,Context context){
        return context.getResources().getDrawable(res);
    }

    public static int dip2px(Context context, float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}
