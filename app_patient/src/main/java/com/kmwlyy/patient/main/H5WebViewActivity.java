package com.kmwlyy.patient.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpCode;
import com.kmwlyy.patient.R;

import java.net.URLEncoder;

/**
 * Created by xcj on 2017/3/24.
 */

public class H5WebViewActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{
    private WebView webView;
    private SwipeRefreshLayout swipeRefresh;

//    private String url = "http://121.15.153.63:8060/h5/index.html";

      private String url = "";
//    private String url = "http://10.2.20.179:8080/?AppKey=SHY&AppToken=9332b44b12964eddafae67971f0c22c8&UserToken=a9225a2bbaf04c4fab30c03a50bc1095";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_h5);
        webView = (WebView) findViewById(R.id.webView);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        String appKey = URLEncoder.encode(HttpCode.APP_KEY);
        String appToken = BaseApplication.instance.getAppToken();
        String userToken = BaseApplication.instance.getUserData() == null ? null : BaseApplication.instance.getUserData().mUserToken;
        url = "http://121.15.153.63:8060/h5/index.html?AppKey="+appKey+"&AppToken="+appToken+"&UserToken="+userToken;
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                swipeRefresh.setRefreshing(false);
            }
        });

        WebSettings webSettings = webView.getSettings();
        //百分比大小
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;

        if (mDensity == 240) {
            //百分比大小
            webView.setInitialScale(300);
        } else if (mDensity == 160) {
            //百分比大小
            webView.setInitialScale(200);
        } else if (mDensity == 120) {
            //百分比大小
            webView.setInitialScale(100);
        }

        // User settings
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //webSettings.setUseWideViewPort(true);//关键点

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.addJavascriptInterface(new JsInteration(), "robot");
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);


        initSwipeRefreshLayout();
        webView.loadUrl(url);
    }

    @Override
    public void onRefresh() {
        webView.reload();
    }

    private void initSwipeRefreshLayout(){
        swipeRefresh.setColorSchemeResources(
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

     class JsInteration {
        @JavascriptInterface
        public void callAndroidMethod() {
           finish();
        }

    }
}
