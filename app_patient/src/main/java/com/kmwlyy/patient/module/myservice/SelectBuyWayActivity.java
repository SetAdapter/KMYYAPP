package com.kmwlyy.patient.module.myservice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.module.myservice.Bean.EventApi;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

/**
 * Created by xcj on 2017/8/10.
 */

public class SelectBuyWayActivity extends Activity{
    @BindView(R.id.tv_title_center)
    TextView tv_title_center;
    @BindView(R.id.iv_tools_left)
    Button iv_tools_left;
    @BindView(R.id.iv_tools_right)
    Button iv_tools_right;
    @BindView(R.id.btnImmediatelyPay)
    Button btnImmediatelyPay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_buy_way);
        butterknife.ButterKnife.bind(this);
        tv_title_center.setText("支付界面");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_tools_right.setVisibility(View.GONE);
        btnImmediatelyPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转回列表页面
                ToastUtils.showShort(SelectBuyWayActivity.this,"支付成功");
                finish();
                EventBus.getDefault().post(new EventApi.BuySuc());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
