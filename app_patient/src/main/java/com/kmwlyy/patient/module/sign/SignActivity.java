package com.kmwlyy.patient.module.sign;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.module.InhabitantStart.InhabitanStartActivity;
import com.kmwlyy.patient.module.signcomfirm.FinishEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by Gab on 2017/8/18 0018.
 * 签约
 */

public class SignActivity extends BaseActivity {

    @BindView(R.id.tv_title_center)
    TextView tv_title_center;

    @BindView(R.id.iv_tools_left)
    Button iv_tools_left;
    @BindView(R.id.bt_sign)
    Button bt_sign;
    //    @BindView(R.id.scroll_sign)
//    ScrollView scroll_sign;
    @BindView(R.id.check_read)
    CheckBox check_read;

    @BindView(R.id.webView)
    WebView mWebView;
    String Url = HttpClient.FAMILY_URL + "/SignatureAgreement.html";

    @Override
    protected int getLayoutId() {
        return R.layout.sign_activity;
    }

    @Override
    protected void afterBindView() {
        tv_title_center.setText("签约");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WebData();
//        scroll_sign.setOnTouchListener(new TouchListenerImpl());
        EventBus.getDefault().register(this);
    }

    private void WebData() {
        // 设置WebView属性
        WebSettings settings = mWebView.getSettings();
        //支持js
        settings.setJavaScriptEnabled(true);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
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
        mWebView.loadUrl(Url);
    }

//    private class TouchListenerImpl implements View.OnTouchListener {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            switch (motionEvent.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    int scrollY = view.getScrollY();
//                    int height = view.getHeight();
//                    int scrollViewMeasuredHeight = scroll_sign.getChildAt(0).getMeasuredHeight();
//                    if (scrollY == 0) {
//                        Log.i("getScrollY","滑动到了顶端 view.getScrollY()=" + scrollY);
//                    }
//                    if ((scrollY + height) == scrollViewMeasuredHeight) {
//                        bt_sign.setEnabled(true);
//                        check_read.setEnabled(true);
//                    }
//                    break;
//                default:
//                    break;
//            }
//            return false;
//        }
//    }

    public void onNext(View view) {
        if (!check_read.isChecked()) {
            Toast.makeText(mContext, "请同意服务条款", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(mContext, InhabitanStartActivity.class));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(FinishEvent event){
        finish();
    }
}
