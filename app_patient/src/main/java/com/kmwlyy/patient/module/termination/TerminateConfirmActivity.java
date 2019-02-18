package com.kmwlyy.patient.module.termination;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.patient.R;

import butterknife.BindView;

/**
 * Created by Gab on 2017/8/14 0014.
 * 解约确认
 */

public class TerminateConfirmActivity extends BaseActivity {

    @BindView(R.id.tv_title_center)
    TextView tv_title_center;
    @BindView(R.id.tv_uploading)
    TextView tv_uploading;

    @BindView(R.id.iv_tools_left)
    Button iv_tools_left;

    @Override
    protected int getLayoutId() {
        return R.layout.termination_confirm_activity;
    }

    @Override
    protected void afterBindView() {
        tv_title_center.setText("解约确认");
        iv_tools_left.setOnClickListener(this);
        tv_uploading.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.iv_tools_left:
                finish();
                break;
            case R.id.tv_uploading:
                new MaterialDialog.Builder(this)
                        .content("您的解约申请已接收，等待家庭医生确认，10个工作日后，将给出结果，请耐心等待。解约期间，您将继续享有该家庭医生团队为您提供的服务")
                        .positiveText("确定")
                        .buttonsGravity(GravityEnum.CENTER)
                        .positiveColor(ContextCompat.getColor(mContext,R.color.color_main_green))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                                finish();
                            }
                        })
                        .show();
                break;
        }
    }
}
