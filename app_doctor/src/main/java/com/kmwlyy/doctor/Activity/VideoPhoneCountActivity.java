package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.FamilyDoctorSettingAdapter;
import com.kmwlyy.doctor.model.HomeSettingBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TFeng on 2017/6/30.
 */

public class VideoPhoneCountActivity extends BaseActivity{
    @ViewInject(R.id.tv_left)
    TextView tv_left;
    @ViewInject(R.id.iv_left)
    ImageView iv_left;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.lv_vidcount)
    ListView lv_vidcount;

    private List<HomeSettingBean.DoctorPackageBean> mDataList = new ArrayList<>();
    private FamilyDoctorSettingAdapter mFamilyDoctorSettingAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_phone_count);
        ViewUtils.inject(this);
        getIntentInfo();
        initView();
        initListener();
    }

    private void getIntentInfo() {
        mDataList = (List<HomeSettingBean.DoctorPackageBean>) getIntent().getSerializableExtra("vidCount");

    }


    private void initView() {
        tv_left.setVisibility(View.GONE);
        iv_left.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_center.setText(R.string.video_count_setting);
        tv_right.setText(R.string.save);

        if(mDataList != null){
            mFamilyDoctorSettingAdapter = new FamilyDoctorSettingAdapter(mContext,false,mDataList);
            lv_vidcount.setAdapter(mFamilyDoctorSettingAdapter);
        }else {
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
                check_data();
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
        mDataList = mFamilyDoctorSettingAdapter.getDataList();
        for (HomeSettingBean.DoctorPackageBean bean:mDataList) {
            if(bean.getVidServiceCount().equals("") || null == bean.getVidServiceCount()){
                ToastUtils.showShort(mContext,bean.getMonthCountName()+"次数不能为空");
                return;
            }

        }
        Intent intent = new Intent();
        intent.putExtra("isSetting",true);
        intent.putExtra("vidCount",(Serializable)mDataList);
        intent.putExtra("Back",1);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }
}
