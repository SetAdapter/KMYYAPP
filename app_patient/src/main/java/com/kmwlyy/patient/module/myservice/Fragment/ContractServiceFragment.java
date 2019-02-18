package com.kmwlyy.patient.module.myservice.Fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;


import com.kmwlyy.patient.R;
import com.kmwlyy.patient.module.myservice.Adapter.ContractServicePackageFragmentChildAdapter;
import com.kmwlyy.patient.weight.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xcj on 2017/8/10.
 * 签约服务包
 */

public class ContractServiceFragment extends BaseFragment {

    @BindView(R.id.tabLremind)
    TabLayout tabLremind;
    @BindView(R.id.vpRemind)
    ViewPager vpRemind;
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_contract_service;
    }
    @Override
    protected void baseInitView() {
        initViewPager();
    }

    private void initViewPager() {
        String[] titles = {"一般人群服务包", "老年人服务包", "0~6岁服务包", "孕产妇服务包"};
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            EverymanServicePackageFragment one = new EverymanServicePackageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("Type", i + "");
            one.setArguments(bundle);
            fragments.add(one);
        }
        vpRemind.setAdapter(new ContractServicePackageFragmentChildAdapter(getChildFragmentManager(), fragments, titles));
        tabLremind.setupWithViewPager(vpRemind);

    }

}
