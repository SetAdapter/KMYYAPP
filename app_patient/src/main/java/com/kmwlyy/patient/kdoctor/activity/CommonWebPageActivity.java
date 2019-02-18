package com.kmwlyy.patient.kdoctor.activity;

import android.net.http.SslError;
import android.util.DisplayMetrics;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.kdoctor.SendFeedbackWithStringEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description描述:
 * @Author作者: hx
 * @Date日期: 2017/7/31
 */

public class CommonWebPageActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.wv_protocols)
    WebView webview;
    private static final String CONSENSUS_LABLE = "HealthBAT";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void afterBindView() {
        toolbarTitle.setText(getIntent().getStringExtra("title"));
        initWebview();
        webview.loadUrl(getIntent().getStringExtra("url"));
    }

    private void initWebview() {
        WebSettings webSettings = webview.getSettings();
        //百分比大小
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;

        //酷派Y60C1 240
        if (mDensity == 240) {
            //百分比大小
            webview.setInitialScale(150);
        } else if (mDensity == 160) {
            //百分比大小
            webview.setInitialScale(200);
        } else if (mDensity == 120) {
            //百分比大小
            webview.setInitialScale(100);
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
        webview.addJavascriptInterface(this, CONSENSUS_LABLE);
        webview.setWebViewClient(new WebViewClient() {
            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // 接受所有网站的证书，忽略SSL错误，执行访问网页
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }

        });
    }

    /*选择图片*/
    @JavascriptInterface
    public void goBackDrKang(String str) {
        EventBus.getDefault().post(new SendFeedbackWithStringEvent(str));
        finish();
    }

    @OnClick(R.id.navigation_btn)
    public void onViewClicked() {
        finish();
    }
}
