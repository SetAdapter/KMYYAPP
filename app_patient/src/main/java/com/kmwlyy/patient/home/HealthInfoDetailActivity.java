package com.kmwlyy.patient.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;

import butterknife.BindView;

public class HealthInfoDetailActivity extends  BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = HealthInfoDetailActivity.class.getSimpleName();

    public static final String KEY_HEALTH_INFO_URL = "HEALTH_INFO_URL";


    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthinfo_detail);
        butterknife.ButterKnife.bind(this);
        tv_center.setText(R.string.health_information);

        try {
            url = getIntent().getStringExtra(KEY_HEALTH_INFO_URL);
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
//        webView.getSettings().setSupportZoom(true);
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webView.getSettings().setLoadWithOverviewMode(true);


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
        if (DebugUtils.debug) {
            Log.d(TAG, "health info url = "+url);
        }
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
