package com.kmwlyy.doctor.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.doctor.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;

public class PDFActivity extends BaseActivity implements DownloadFile.Listener{
    public static final String TAG = "PDFActivity";
    PDFPagerAdapter adapter;
    RemotePDFViewPager remotePDFViewPager;
    String id,url;
    LinearLayout root;

    //返回和保存
    @ViewInject(R.id.tv_left)
    TextView btn_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        ViewUtils.inject(this); //注入view和事件

        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");
        id = bundle.getString("id") + ".pdf";
        Log.i(TAG,url);

        ((TextView)findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_check_pdf));
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(this);
        btn_left.setText(getResources().getString(R.string.string_back));

        initView();
    }

    public void initView(){
        root = (LinearLayout) findViewById(R.id.root);
        // 请求头部
        String noneStr = CommonUtils.createRandomString();
        String appToken = BaseApplication.instance.getAppToken();
        String userToken = BaseApplication.instance.getUserData() == null ? null : BaseApplication.instance.getUserData().mUserToken;
        String sign = CommonUtils.createSignString(noneStr, appToken, userToken);
        String[] headers = new String[]{sign,noneStr,appToken,userToken};

        remotePDFViewPager = new RemotePDFViewPager(this, url, id, this, headers);
        remotePDFViewPager.setId(R.id.pdfViewPager);
    }
    @Override
    public void onSuccess(String url, String destinationPath) {
        // That's the positive case. PDF Download went fine
        adapter = new PDFPagerAdapter(this, id);
        remotePDFViewPager.setAdapter(adapter);
        updateLayout();
    }

    public void updateLayout() {
        root.removeAllViewsInLayout();
        root.addView(remotePDFViewPager,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onFailure(Exception e) {
        // This will be called if download fails
        Log.i(TAG,"download failed");
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
        // You will get download progress here
        // Always on UI Thread so feel free to update your views here
        Log.i(TAG,progress + "/" + total);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(adapter != null){
            adapter.close();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_left://返回
                finish();
                break;
        }
    }
}
