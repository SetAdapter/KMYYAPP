package com.kmwlyy.doctor.Activity;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;

import com.kmwlyy.doctor.model.httpEvent.Http_clinicRule_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Description描述: 诊所规则页面
 * @Author作者: zhaoqile
 * @Date日期: 2016/8/24
 */
public class ClinicRuleActivity extends BaseActivity {
    private final String TAG = "ClinicRuleActivity";

    //返回
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_center)
    TextView tv_title;
    @ViewInject(R.id.tv_body)
    TextView tv_body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_rule);
        ViewUtils.inject(this);

        tv_title.setText(getResources().getString(R.string.string_rules));
        btn_left.setVisibility(View.VISIBLE);
        tv_title.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getData();
    }

    private void getData()
    {
        Http_clinicRule_Event  event = new Http_clinicRule_Event(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_apptoken));
                finish();
            }

            @Override
            public void onSuccess(String str) {
                dismissLoadDialog();
                tv_body.setText(Html.fromHtml(str));
            }
        });


        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }
}
