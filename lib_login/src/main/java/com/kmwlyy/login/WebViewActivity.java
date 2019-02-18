package com.kmwlyy.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.winson.ui.widget.RateLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xcj on 2016/11/2.
 */

public class WebViewActivity extends Activity{

    @BindView(R2.id.navigation_icon)
    ImageView mNavigationIcon;
    @BindView(R2.id.navigation_btn)
    RateLayout mNavigationBtn;
    @BindView(R2.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R2.id.wv_protocols)
    WebView mProtocolsWebView;

    private static final String URL = "URL";

    private String mUrl = null;

    public static void startWebViewActivity(Context context, String  url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(URL, url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        mUrl = getIntent().getStringExtra(URL);
        mToolbarTitle.setText(R.string.protocols_web_view_title);
        mProtocolsWebView.loadUrl(mUrl);
        mProtocolsWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mProtocolsWebView.loadUrl(mUrl);
                return true;
            }
        });
        mNavigationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mProtocolsWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

            }
        });
    }
}
