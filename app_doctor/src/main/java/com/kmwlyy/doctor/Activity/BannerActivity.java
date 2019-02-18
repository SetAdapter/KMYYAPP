package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


import com.kmwlyy.doctor.R;

/**
 * Created by Winson on 2017/7/3.
 */

public class BannerActivity extends BaseActivity {

    public static final String TAG = BannerActivity.class.getSimpleName();

    static final String NAME = "_NAME";
    static final String URL = "_URL";

    public static void startSelf(Context context, String name, String url) {
        Intent intent = new Intent(context, BannerActivity.class);
        intent.putExtra(NAME, name);
        intent.putExtra(URL, url);
        context.startActivity(intent);
    }

    WebView mWebView;
    String mBannerName, mTargetUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        mBannerName = getIntent().getStringExtra(NAME);
        mTargetUrl = getIntent().getStringExtra(URL);

        ((TextView) findViewById(R.id.tv_center)).setText(mBannerName);

        mLeftBtn = (TextView) findViewById(R.id.tv_left);
        mLeftBtn.setOnClickListener(this);

        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.loadUrl(mTargetUrl);

    }

    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        switch (id) {
            case R.id.tv_left:
                onBackPressed();
                break;
        }
    }

}
