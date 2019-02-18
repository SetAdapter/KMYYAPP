package com.kmwlyy.patient.kdoctor.activity;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.patient.R;
import com.winson.ui.widget.RateLayout;

import butterknife.BindView;

/**
 * Created by Gab on 2017/8/1 0001.
 *  中医体质
 */

public class CorporeityIdentifyActivity extends BaseActivity {

    @BindView(R.id.toolbar_title)
    TextView tv_title_center;
    @BindView(R.id.navigation_btn)
    RateLayout iv_tools_left;
    @BindView(R.id.wv_protocols)
    WebView mWebView;

    private String mUrl;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void afterBindView() {
        tv_title_center.setText("中医体质");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mUrl = getIntent().getStringExtra("URL");
        WebData();
    }

    private void WebData() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @SuppressWarnings("deprecation")
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

        });
        if (!TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        }
    }

}
