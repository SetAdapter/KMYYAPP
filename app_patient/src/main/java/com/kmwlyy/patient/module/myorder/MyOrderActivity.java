package com.kmwlyy.patient.module.myorder;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.module.myorder.Fragment.FileInformationFragment;
import com.kmwlyy.patient.module.myservice.Adapter.HealthRecordsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Gab on 2017/7/30 0030.
 * 我的订单
 */

public class MyOrderActivity extends BaseActivity {

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
    public int getLayoutId() {
        return R.layout.activity_bill;
    }

    @Override
    protected void afterBindView() {
        initViewPager();
        tv_title_center.setText("我的订单");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_tools_right.setVisibility(View.VISIBLE);
        iv_tools_right.setText("消费明细");
        iv_tools_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOrderActivity.this,ConsumeParticularsActivity.class));
            }
        });
    }

    private void initViewPager() {
        String[] titles = {"待处理", "服务中", "评价"};

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FileInformationFragment());
        fragments.add(new FileInformationFragment());
        fragments.add(new FileInformationFragment());
        vpRemind.setAdapter(new HealthRecordsAdapter((AppCompatActivity) mContext, fragments, titles));
        tabLremind.setupWithViewPager(vpRemind);

    }

}
