package com.mcdull.cert.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.mcdull.cert.ActivityMode.MyTitleActivity;
import com.mcdull.cert.R;
import com.mcdull.cert.utils.ShowWaitPopupWindow;

import java.util.List;

public class TripActivity extends MyTitleActivity {

    private WebView webView;
    private ShowWaitPopupWindow waitWin;
    private boolean isGetImg = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_trip);
        super.onCreate(savedInstanceState);

        //判断SDK版本，设置沉浸状态栏
        if (Build.VERSION.SDK_INT>=19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            findViewById(R.id.status_bar).setVisibility(View.VISIBLE);
        }

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(5);
        waitWin = new ShowWaitPopupWindow(TripActivity.this);

        String title = getIntent().getStringExtra("Title");
        String url = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(url)) {


            AVQuery<AVObject> query = new AVQuery<>("Url");
            query.whereEqualTo("title", title);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {

                        webView = (WebView) findViewById(R.id.webview);
                        //启用支持javascript
                        WebSettings settings = webView.getSettings();
                        settings.setJavaScriptEnabled(true);
                        webView.loadUrl(list.get(0).getString("url"));
                        webView.setWebViewClient(new WebViewClient() {
                            @Override
                            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                // TODO Auto-generated method stub
                                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                                view.loadUrl(url);
                                return true;
                            }

                        });

                        webView.setWebChromeClient(new WebChromeClient() {
                            @Override
                            public void onProgressChanged(WebView view, int newProgress) {
                                if (newProgress == 100) {
                                    isGetImg = false;
                                    waitWin.dismissWait();
                                } else {

                                }
                            }
                        });
                    } else {
                        isGetImg = false;
                        waitWin.dismissWait();
                        Toast.makeText(TripActivity.this, "加载失败，请检测网络设置", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            webView = (WebView) findViewById(R.id.webview);
            //启用支持javascript
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            webView.loadUrl(url);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // TODO Auto-generated method stub
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }

            });

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    if (newProgress == 100) {
                        isGetImg = false;
                        waitWin.dismissWait();
                    }
                }
            });
        }

        ((TextView) findViewById(R.id.tv_title)).setText(title);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isGetImg) {
            waitWin.showWait();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (webView != null) {
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        if (waitWin!=null){
            waitWin.dismissWait();
            waitWin = null;
        }
    }
}
