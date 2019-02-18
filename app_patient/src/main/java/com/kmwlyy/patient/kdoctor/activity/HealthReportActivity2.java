package com.kmwlyy.patient.kdoctor.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.RecyclerAdapter;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.event.CorporeityIdentifyEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Gab on 2017/7/29 0029.
 *  健康报告
 */

public class HealthReportActivity2 extends BaseActivity{

    @BindView(R.id.tv_title_center)
    TextView tv_title_center;
    @BindView(R.id.iv_tools_left)
    Button iv_tools_left;
    @BindView(R.id.text_down)
    TextView text_down;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    private List data;
    private List data_down;
    private String mName;
    private String mIdCardNumber;
    private String mPhoneNumber;
    private String mSex;
    @Override
    protected int getLayoutId() {
        return R.layout.health_report_activity;
    }

    @Override
    protected void afterBindView() {
        init();
        tv_title_center.setText("我的家人");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init(){
//        WebSettings webSettings = mWebView.getSettings();
//        //百分比大小
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        int mDensity = metrics.densityDpi;
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webSettings.setDisplayZoomControls(false);
//        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
//        webSettings.setAllowFileAccess(true); // 允许访问文件
//        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
//        webSettings.setSupportZoom(false); // 支持缩放
//        webSettings.setLoadWithOverviewMode(true);
//        mWebView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//        });
//        mWebView.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//            }
//
//
//        });
//        //appkey=e38ad4f48133c76ad8e6165ccc427211
//        //timestamp=时间戳，格式为yyyy-MM-dd HH:mm:ss，时区为GMT+8
//        //秘钥=dbf2dcc52133c76ad8e61600eeafa583
//
//        String appKey = "e38ad4f48133c76ad8e6165ccc427211";
//
//        String timeStamp;
//        try {
//            timeStamp = CommonUtils.mm2time(System.currentTimeMillis());
//        }catch (Exception e){
//            e.printStackTrace();
//            timeStamp = "";
//        }
//
//        String appSecret = "dbf2dcc52133c76ad8e61600eeafa583";
//        String[] toBeSign = {appKey,timeStamp,appSecret};
//        Arrays.sort(toBeSign);
//        String preSignString = "";
//        for (int i = 0; i < toBeSign.length; i++) {
//            preSignString += toBeSign[i];
//        }
//        String sign = CommonUtil.getMD5(preSignString);
//        //        appkey，timestamp，秘钥，用md5加密后生成sign
//
//        String phone;
//        try {
//            phone = "15814488696";
//        }catch (Exception e){
//            e.printStackTrace();
//            phone = "";
//        }
//        mUrl = BaseConstants.URL360 + "?appkey="+appKey+"&timestamp="+timeStamp+"&sign="+sign+"&phone="+ phone+"&src=2";
//        mWebView.loadUrl(mUrl);
        setRecyclerView();
        getRecyclerData();
        getRecyclerDownData();

        //绑定适配器 传数据
        adapter=new RecyclerAdapter(this,data);
        adapter.setOnItemClickLitener(new RecyclerAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view,  int position) {
                text_down.setText(data_down.get(position)+"");

                adapter.setCheckPosition(position);//选中位置
                adapter.notifyDataSetChanged(); //刷新适配器
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void setRecyclerView() {
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getRecyclerData() {
        data=new ArrayList<>();
        data.add("杨亦帆");
        data.add("王莉");
        data.add("杨洋");
    }

    private void getRecyclerDownData() {
        data_down=new ArrayList();
        data_down.add("老年人");
        data_down.add("中年人");
        data_down.add("儿童");
    }

    @OnClick({R.id.doctor_message,R.id.health_manage,R.id.china_physique,R.id.health_report})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.doctor_message:
//                startActivity(new Intent(this, DoctorMessageActivity.class));
                break;
            case R.id.health_manage:
//                startActivity(new Intent(this, Health_Record_Activity.class));
                break;
            case R.id.china_physique:
                getKey();
//                startActivity(new Intent(this, CorporeityIdentifyActivity.class));
                break;
            case R.id.health_report:
                startActivity(new Intent(this, HealthReportActivity.class));
                break;
        }
    }

    private void getKey(){
        mName = "赖晓燕";
        mIdCardNumber = "360726199310242641";
        mPhoneNumber = "17603028436";
        mSex = "1";
        CorporeityIdentifyEvent event = new CorporeityIdentifyEvent(mName, mIdCardNumber, mPhoneNumber, mSex, new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(HealthReportActivity2.this,"获取数据失败");
            }

            @Override
            public void onSuccess(String s) {
                JSONObject jsStr = JSONObject.parseObject(s);
                String content = jsStr.getString("content");
                int index = content.indexOf("http://");
                String url = content.substring(index, index+89);
                Intent intent = new Intent(HealthReportActivity2.this, CorporeityIdentifyActivity.class);
                intent.putExtra("URL",url);
                startActivity(intent);
            }
        });
        NetService.createClient(HealthReportActivity2.this, "http://test-healthrecord.kmhealthcloud.cn:8091/tcq", event).start();
    }

}
