package com.kmwlyy.patient.module.myservice;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;

import butterknife.BindView;

/**
 * Created by Gab on 2017/8/10 0010.
 * 支付界面
 */

public class PaymentActivity extends BaseActivity {

    @BindView(R.id.tv_title_center)
    TextView tv_title_center;
    @BindView(R.id.iv_tools_left)
    Button iv_tools_left;
    @BindView(R.id.iv_tools_right)
    Button iv_tools_right;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_payment;
    }

    @Override
    protected void afterBindView() {
        tv_title_center.setText("支付界面");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_tools_right.setVisibility(View.GONE);
    }

    public void btnImmediatelyPay(View view) {
        ToastUtils.showShort(mContext, "支付功能暂无开放");
    }
}
