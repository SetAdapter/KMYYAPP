package com.kmwlyy.patient.module.myservice;


import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.module.myservice.Adapter.ContractServicePackageFragmentAdapter;
import com.kmwlyy.patient.module.myservice.Fragment.ContractServiceFragment;
import com.kmwlyy.patient.module.myservice.Fragment.VisitingServiceFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Gab on 2017/8/17 0017.
 * 我的服务
 */

public class MyServiceActivity extends BaseActivity {

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contract_service_package;
    }

    @Override
    protected void afterBindView() {
        initViewPager();
        tv_title_center.setText("我的服务");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViewPager() {
        String[] titles = {"签约服务", "上门服务"};
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ContractServiceFragment());
        fragments.add(new VisitingServiceFragment());
        vpRemind.setAdapter(new ContractServicePackageFragmentAdapter(this, fragments, titles));
        tabLremind.setupWithViewPager(vpRemind);

    }
}
