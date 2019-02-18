package com.kmwlyy.patient.symptomGuide;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.patient.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Description描述:
 * @Author作者: hx
 * @Date日期: 2017/7/31
 */

public class AiBodyActivity extends BaseActivity {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.wv_protocols)
    WebView webView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void afterBindView() {
        toolbarTitle.setText("症状自查");
        initData();
    }

    private void initData() {
        String mUrl = "http://www.jkbat.com/" + "weixin/webApp/home/SelfExam?src=batapp";
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setBlockNetworkImage(true);
        webView.addJavascriptInterface(this, "HealthBAT");
        webView.setWebViewClient(new WebViewClient() {
                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                         view.loadUrl(url);
                                         return true;
                                     }
                                 }
        );
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (!TextUtils.isEmpty(title)) {
                    if (title.trim().toLowerCase().contains("error") || title.contains("找不到网页")) {
                        //自定义错误页面
                        //用javascript隐藏系统定义的404页面信息
                        String data = " ";
                        view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
                    }
                }
                view.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        if (!TextUtils.isEmpty(title)) {
                            if (title.trim().toLowerCase().contains("error") || title.contains("找不到网页")) {
                                //自定义错误页面
                                //用javascript隐藏系统定义的404页面信息
                                String data = " ";
                                view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
                            }
                        }
                    }
                });
            }
        });
        webView.loadUrl(mUrl);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                webView.getSettings().setBlockNetworkImage(false);
            }
        }, 1000);
    }

    @JavascriptInterface
    public void chooseSymptom(String id) {
        Log.i("lxg", id);
        Intent intent = new Intent(mContext, IllnessDetailActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @OnClick(R.id.navigation_btn)
    public void onViewClicked() {
        finish();
    }
}
