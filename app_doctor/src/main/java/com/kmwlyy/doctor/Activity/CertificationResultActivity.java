package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by xcj on 2016/10/28.
 */
public class CertificationResultActivity extends BaseActivity {

    private static final String IS_APPLING = "IS_APPLING";
    private int mIsAppling = 0;

    @ViewInject(R.id.navigation_icon)
    ImageView mNavigationIcon;
    @ViewInject(R.id.toolbar_title)
    TextView mToolbarTitle;
    @ViewInject(R.id.iv_certification_result)
    ImageView mCertificationResult;
    @ViewInject(R.id.tv_certification_result_title)
    TextView mResultTitle;
    @ViewInject(R.id.tv_certification_result)
    TextView mResultTxt;
    @ViewInject(R.id.tv_reset)
    TextView mResetTxt;


    public static void startCertificationResultActivity(Context context, int type) {
        Intent intent = new Intent(context, CertificationResultActivity.class);
        intent.putExtra(IS_APPLING, type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certification_result);
        ViewUtils.inject(this); //注入view和事件
        mToolbarTitle.setText(getString(R.string.real_name_authentication));
        mIsAppling = getIntent().getIntExtra(IS_APPLING, 0);
        mResetTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoctorCertificationActivity.startDoctorCertificationActivity(CertificationResultActivity.this, true);
            }
        });
        mNavigationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MyApplication.getApplication(v.getContext()).logout();
            }
        });
        if (mIsAppling == 2){
            mCertificationResult.setImageResource(R.mipmap.image_certification_defeated);
            mResultTitle.setText(getResources().getString(R.string.result_certification_fail));
            mResultTitle.setTextColor(Color.RED);
            mResultTxt.setText(getResources().getString(R.string.result_certification_fail_info));
            mResetTxt.setVisibility(View.VISIBLE);
            mResetTxt.setEnabled(true);
        }else if(mIsAppling == 3){
            mCertificationResult.setImageResource(R.mipmap.image_certification_appling);
            mResultTitle.setText(getResources().getString(R.string.result_certification_in_review));
            mResultTitle.setTextColor(getResources().getColor(R.color.color_main_green));
            mResultTxt.setText(getResources().getString(R.string.result_certification_in_review_info));
            mResetTxt.setVisibility(View.GONE);
            mResetTxt.setEnabled(false);
        }else if(mIsAppling == 4){
            mCertificationResult.setImageResource(R.mipmap.image_certification_appling);
            mResultTitle.setText(getResources().getString(R.string.result_certification_wait_review));
            mResultTitle.setTextColor(getResources().getColor(R.color.color_main_green));
            mResultTxt.setText(getResources().getString(R.string.result_certification_in_review_info));
            mResetTxt.setVisibility(View.VISIBLE);
            mResetTxt.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
