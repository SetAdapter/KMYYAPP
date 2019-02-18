package com.kmwlyy.patient.module.myorder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.patient.R;

import butterknife.BindView;

/**
 * Created by Gab on 2017/7/30 0030.
 * 消费明细
 */

public class ConsumeParticularsActivity extends BaseActivity {

    @BindView(R.id.tv_title_center)
    TextView tv_title_center;
    @BindView(R.id.iv_tools_left)
    Button iv_tools_left;

    @Override
    public int getLayoutId() {
        return R.layout.activity_consume_particulars;
    }

    @Override
    protected void afterBindView() {
        tv_title_center.setText("消费明细");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
