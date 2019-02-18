package com.kmwlyy.patient.module.myservice;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.NewHttpClient;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.module.myservice.Adapter.HealthRecordsAdapter;
import com.kmwlyy.patient.module.myservice.Bean.BaseService;
import com.kmwlyy.patient.module.myservice.Bean.Http_serviceListById;
import com.kmwlyy.patient.module.myservice.Fragment.ServiceContextFragment;
import com.kmwlyy.patient.module.myservice.Fragment.ServiceExplainFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Gab on 2017/8/9 0009.
 * 上门服务-基础服务
 */

public class BaseServiceActivity extends BaseActivity {

    @BindView(R.id.tv_title_center)
    TextView tv_title_center;
    @BindView(R.id.iv_tools_left)
    Button iv_tools_left;
    @BindView(R.id.iv_tools_right)
    Button iv_tools_right;
    @BindView(R.id.tabLremind)
    TabLayout tabLremind;
    @BindView(R.id.vpRemind)
    ViewPager vpRemind;
    @BindView(R.id.tv_service)
    TextView tv_service;
    @BindView(R.id.text_buy)
    TextView text_buy;
    @BindView(R.id.serviceintro)
    TextView serviceintro;
    @BindView(R.id.Price)
    TextView Price;
    @BindView(R.id.text_crowddesc)
    TextView text_crowddesc;
    String packageID;
    int BuyUserNum;
    Bundle bundle = new Bundle();

    @Override
    protected int getLayoutId() {
        return R.layout.base_service_activity;
    }

    @Override
    protected void afterBindView() {
        tv_title_center.setText("上门服务-基础服务");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_tools_right.setVisibility(View.GONE);
        initViewPager();
        bundle = this.getIntent().getExtras();
        packageID = getIntent().getStringExtra("packageID");
        BuyUserNum = bundle.getInt("BuyUserNum",-1);
        text_buy.setText(BuyUserNum + "人已购");
        getBaseService();
    }


    private void initViewPager() {
        String[] titles = {"服务内容", "服务说明"};
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ServiceContextFragment());
        fragments.add(new ServiceExplainFragment());
        vpRemind.setAdapter(new HealthRecordsAdapter((AppCompatActivity) mContext, fragments, titles));
        tabLremind.setupWithViewPager(vpRemind);

    }

    private void getBaseService() {
        Http_serviceListById event = new Http_serviceListById(packageID, new HttpListener <BaseService.DataBean>() {
            @Override
            public void onError(int code, String msg) {
                Log.i("packageID", msg + code);
                ToastUtils.showShort(BaseServiceActivity.this, msg + code);
            }

            @Override
            public void onSuccess(BaseService.DataBean dataBeen) {
                if (dataBeen != null) {
                    tv_service.setText(dataBeen.getPackageName());
                    serviceintro.setText(dataBeen.getUserRangeName());
                    Price.setText(dataBeen.getPrice() + "元");
                    text_crowddesc.setText(dataBeen.getUserRangeName());
                }
            }
        });

        NetService.createClient(mContext, HttpClient.FAMILY_URL, event).start();
    }


    public void Buy_Service(View view) {
        startActivity(new Intent(BaseServiceActivity.this, PaymentActivity.class));
    }
}
