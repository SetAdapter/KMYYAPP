package com.kmwlyy.patient.kdoctor.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;

import com.alibaba.fastjson.JSONObject;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UserMember;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseFragment;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.event.CorporeityIdentifyEvent;
import com.kmwlyy.patient.helper.net.event.CorporeityIdentifyIsTestEvent;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xcj on 2017/8/21.
 */

public class TcmConstitutionFragment extends BaseFragment {

    private ViewGroup mRoot;
    private String mName;
    private String mIdCardNumber;
    private String mPhoneNumber;
    private String mSex;
    private String mUrl;
    @BindView(R.id.wv_protocols)
    WebView mWebView;
    @BindView(R.id.fl_root)
    FrameLayout frameLayout;
    @BindView(R.id.btn_test)
    Button btn_test;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = (ViewGroup) inflater.inflate(R.layout.fragment_tcm_constitution, container, false);
        ButterKnife.bind(this, mRoot);
        UserMember userMember = (UserMember) getArguments().getSerializable("userMember");
        mName = userMember.mMemberName;
        mIdCardNumber = userMember.mIDNumber;
        mPhoneNumber = userMember.mMobile;
        mSex = userMember.mGender+"";
        getInfo();
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUrl("");
            }
        });
        return mRoot;

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

    private void getUrl(final String suffix){
        frameLayout.setVisibility(View.GONE);
        CorporeityIdentifyEvent event = new CorporeityIdentifyEvent(mName, mIdCardNumber, mPhoneNumber, mSex, new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(getActivity(), "获取数据失败");
            }

            @Override
            public void onSuccess(String s) {
                JSONObject jsStr = JSONObject.parseObject(s);
                String content = jsStr.getString("content");
                int index = content.indexOf("http://");
                mUrl = content.substring(index, index + 89)+suffix;
                WebData();
            }
        });
        NetService.createClient(getActivity(), "http://test-healthrecord.kmhealthcloud.cn:8091/tcq", event).start();
    }

    private void getInfo(){
        String appKey = "kmjtys20170821";

        String timeStamp;
        try {
            timeStamp = CommonUtils.mm2time(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
            timeStamp = "";
        }

        String appSecret = "kmjtys20170821";
        String[] toBeSign = {appKey, timeStamp, appSecret};
        Arrays.sort(toBeSign);
        String preSignString = "";
        for (int i = 0; i < toBeSign.length; i++) {
            preSignString += toBeSign[i];
        }
        String sign = CommonUtils.toMD5(preSignString);
        //        appkey，timestamp，秘钥，用md5加密后生成sign
        String appid = "00130";

        CorporeityIdentifyIsTestEvent event = new CorporeityIdentifyIsTestEvent(mIdCardNumber, appid, timeStamp, timeStamp, sign, new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(getActivity(),"失败");
            }

            @Override
            public void onSuccess(String s) {
                JSONObject jsStr = JSONObject.parseObject(s);
                String content = jsStr.getString("Data");
                if (TextUtils.isEmpty(content)){
                    frameLayout.setVisibility(View.VISIBLE);
                }else{
                    frameLayout.setVisibility(View.GONE);
                    getUrl("&query=1");
                }
            }
        });
        NetService.createClient(getActivity(), "http://test-healthrecord.kmhealthcloud.cn:8160", event).start();
    }
}
