package com.kmwlyy.patient.module.sign_introduce;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.patient.R;

import butterknife.BindView;

/**
 * Created by Gab on 2017/8/14 0014.
 * 签约后可享服务
 */

public class SignIntroduceActivity extends BaseActivity {

    @BindView(R.id.tv_title_center)
    TextView tv_title_center;

    @BindView(R.id.iv_tools_left)
    Button iv_tools_left;

    @Override
    protected int getLayoutId() {
        return R.layout.sign_introduce_activity;
    }

    @Override
    protected void afterBindView() {
        tv_title_center.setText("签约后可享服务");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
