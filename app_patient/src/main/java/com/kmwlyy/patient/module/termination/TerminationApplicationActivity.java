package com.kmwlyy.patient.module.termination;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.patient.R;

import butterknife.BindView;

/**
 * Created by Gab on 2017/8/14 0014.
 * 解约申请书
 */

public class TerminationApplicationActivity extends BaseActivity {

    @BindView(R.id.tv_title_center)
    TextView tv_title_center;
    @BindView(R.id.text_context)
    TextView text_context;

    @BindView(R.id.iv_tools_left)
    Button iv_tools_left;

    @Override
    protected int getLayoutId() {
        return R.layout.termination_application_activity;
    }

    @Override
    protected void afterBindView() {
        tv_title_center.setText("解约申请书");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String str = "    居民姓名陈少群,目前与和平社区服务健康中心存在签约合作关系，因如下原因欲与该社康中心解除家庭医生签约服务关系。";
        int bstart = str.indexOf("陈少群");
        int bend = bstart + "陈少群".length();
        int fstart = str.indexOf("和平社区服务健康中心");
        int fend = fstart + "和平社区服务健康中心".length();
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.color_button_green)), bstart, bend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        style.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mContext,R.color.color_button_green)), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        text_context.setText(style);
    }

    public void onTermination(View view) {
        startActivity(new Intent(TerminationApplicationActivity.this, TerminateConfirmActivity.class));
        finish();
    }
}
