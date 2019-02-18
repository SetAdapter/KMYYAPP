package com.kmwlyy.patient.module.termination;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.patient.R;

import butterknife.BindView;

/**
 * Created by Gab on 2017/8/14 0014.
 * 解约须知
 */

public class TerminationActivity extends BaseActivity {

    @BindView(R.id.tv_title_center)
    TextView tv_title_center;

    @BindView(R.id.iv_tools_left)
    Button iv_tools_left;

    @Override
    protected int getLayoutId() {
        return R.layout.termination_activity;
    }

    @Override
    protected void afterBindView() {
        tv_title_center.setText("解约须知");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void but_next(View view) {
        startActivity(new Intent(TerminationActivity.this,TerminationApplicationActivity.class));
        finish();
    }
}
