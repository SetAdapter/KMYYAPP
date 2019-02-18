package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by TFeng on 2017/7/4.
 */

public class NewFileActivity extends BaseActivity {
    @ViewInject(R.id.tv_left)
    TextView tv_left;
    @ViewInject(R.id.iv_left)
    ImageView iv_left;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.iv_new_file)
    ImageView iv_new_file;
    private String mFamilyDoctorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_file);
        ViewUtils.inject(this);
        getIntentInfo();
        initView();
        initListener();
    }

    private void getIntentInfo() {
        mFamilyDoctorID = getIntent().getStringExtra("FamilyDoctorID");
    }


    private void initView() {
        tv_left.setVisibility(View.GONE);
        iv_left.setVisibility(View.VISIBLE);
        tv_center.setText(R.string.health_file);

    }
    private void initListener() {
        iv_left.setOnClickListener(this);
        iv_new_file.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()){
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_new_file:
                ToastUtils.showShort(this,"跳转到建档页面");
                Intent intent = new Intent();
                intent.putExtra("FamilyDocterID",mFamilyDoctorID);
                intent.putExtra("BaseInfoAcitvity",1);
                intent.setClass(mContext,BaseInfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
