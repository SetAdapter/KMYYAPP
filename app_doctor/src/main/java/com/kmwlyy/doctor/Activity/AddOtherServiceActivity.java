package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.AddOtherRemarkAdapter;
import com.kmwlyy.doctor.model.HomeSettingBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TFeng on 2017/6/29.
 */

public class AddOtherServiceActivity extends BaseActivity {

    @ViewInject(R.id.tv_left)
    TextView tv_left;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.iv_left)
    ImageView iv_left;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.lv_remark)
    ListView lv_remark;
    private List<HomeSettingBean.DoctorPackageBean> mRemark = new ArrayList<>();
    private AddOtherRemarkAdapter mAddOtherRemarkAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_other_service);
        ViewUtils.inject(this);
        getIntentInfo();
        initView();
        initListener();
    }

    private void getIntentInfo() {
        mRemark = (List<HomeSettingBean.DoctorPackageBean>) getIntent().getSerializableExtra("Remark");
    }


    private void initView() {
        tv_left.setVisibility(View.GONE);
        iv_left.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);

        tv_center.setText(R.string.add_other_service);
        tv_right.setText(R.string.save);


        if(mRemark != null){
            mAddOtherRemarkAdapter = new AddOtherRemarkAdapter(mContext,mRemark);
            lv_remark.setAdapter(mAddOtherRemarkAdapter);

        }else{
            ToastUtils.showShort(mContext,"暂无数据");
        }







    }


    private void initListener() {

        iv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()){
            case R.id.iv_left:
                back();
                break;
            case R.id.tv_right:
                // TODO: 2017/6/29 检查并保存数据
                check_data();
                Intent intent = new Intent();
                intent.putExtra("isSetting",true);
                intent.putExtra("Remark",(Serializable)mRemark);
                intent.putExtra("Back",1);
                setResult(RESULT_OK,intent);
                finish();
                break;

        }
    }

    private void back() {
        Intent intentReturn = new Intent();
        intentReturn.putExtra("Back",0);
        setResult(RESULT_OK,intentReturn);
        finish();
    }

    private void check_data() {
        mRemark = mAddOtherRemarkAdapter.getDataList();
    }

    @Override
    public void onBackPressed() {
        back();
    }
}
