package com.kmwlyy.imchat.page;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.kmwlyy.imchat.R;


/**
 * Created by xcj on 2017/3/16.
 */

public class WebViewActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{
    public static final String URL = "URL";
    private TextView tv_center;
    private WebView webView;
    private SwipeRefreshLayout swipeRefresh;
    private TextView tv_left;

    private String url = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imchat_activity_web_view);
        tv_center = (TextView) findViewById(R.id.tv_center);
        webView = (WebView) findViewById(R.id.webView);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_center.setText("详情");
        try {
            url = getIntent().getStringExtra(URL);
        }catch (Exception e){
            url = "";
        }
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

        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放

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
}
