package com.kmwlyy.patient.kdoctor.activity;


import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.kdoctor.net.BaseConstants;
import com.kmwlyy.patient.kdoctor.HHEmptyView;
import com.winson.ui.widget.RateLayout;

import java.util.Arrays;

import butterknife.BindView;

public class HealthReportActivity extends BaseActivity {
    @BindView(R.id.topBar)
    LinearLayout topBar;
    @BindView(R.id.wv_protocols)
    WebView mWebView;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.navigation_btn)
    RateLayout navigation_btn;
    @BindView(R.id.hh_empty_view)
    HHEmptyView hh_empty_view;

    private String mUrl;
    private static final String CONSENSUS_LABLE = "HealthBAT";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_report;
    }

    @Override
    protected void afterBindView() {
        topBar.setVisibility(View.GONE);
        init();
        toolbar_title.setText("健康报告");
        navigation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        WebSettings webSettings = mWebView.getSettings();
        //百分比大小
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(false); // 支持缩放
        webSettings.setLoadWithOverviewMode(true);
        mWebView.addJavascriptInterface(this, CONSENSUS_LABLE);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                hh_empty_view.empty();
                super.onReceivedError(view, request, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }


        });
        //appkey=e38ad4f48133c76ad8e6165ccc427211
        //timestamp=时间戳，格式为yyyy-MM-dd HH:mm:ss，时区为GMT+8
        //秘钥=dbf2dcc52133c76ad8e61600eeafa583

        String appKey = "f82d8903067c4348925dd529e353cf5e";

        String timeStamp;
        try {
            timeStamp = CommonUtils.mm2time(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
            timeStamp = "";
        }

        String appSecret = "e2e5d7ef6b4e4d129d80e30faf33fa89";
        String[] toBeSign = {appKey, timeStamp, appSecret};
        Arrays.sort(toBeSign);
        String preSignString = "";
        for (int i = 0; i < toBeSign.length; i++) {
            preSignString += toBeSign[i];
        }
        String sign = CommonUtils.toMD5(preSignString);
        //        appkey，timestamp，秘钥，用md5加密后生成sign

        String idnumber = getIntent().getStringExtra("IDNUMBER");
        String phone;
        if (!TextUtils.isEmpty(idnumber)){
            phone = idnumber;
        }else {
            phone = "430121198902027311";
        }
        mUrl = BaseConstants.URL360 + "?appkey=" + appKey + "&timestamp=" + timeStamp + "&sign=" + sign + "&phone=" + phone + "&src=3";
        mWebView.loadUrl(mUrl);
        hh_empty_view.loading();
        hh_empty_view.setOnBtnClickListener(new HHEmptyView.OnBtnClickListener() {
            @Override
            public void onBtnClick() {
                mWebView.reload();
            }
        });

    }

    @JavascriptInterface
    public void goToBatHome() {
        Log.e("sb","fuck");
        finish();
    }

    @JavascriptInterface
    public void removeAnimation(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("sb","cnm2");
                hh_empty_view.success();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.onPause();
        mWebView.destroy();
        mWebView= null;
        System.gc();
    }
}
