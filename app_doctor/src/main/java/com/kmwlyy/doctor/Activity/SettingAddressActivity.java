package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.w3c.dom.Text;

/**
 * Created by KM on 2017/6/27.
 */

public class SettingAddressActivity extends BaseActivity {

    @ViewInject(R.id.tv_left)
    TextView tv_left;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.iv_left)
    ImageView iv_left;




    @ViewInject(R.id.et_traffic_address)
    EditText et_traffic_address;
    @ViewInject(R.id.et_clinic_adderss)
    EditText et_clinic_address;
    private String mTrafficAddress;
    private String mClinicAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settting_address);
        ViewUtils.inject(this);
        getIntentInfo();
        initView();
        initListener();
    }

    private void getIntentInfo(){

        mTrafficAddress = getIntent().getStringExtra("MapAddress");
        mClinicAddress = getIntent().getStringExtra("ClicnicAddress");



    }




    private void initView() {
        tv_center.setText(R.string.offline_address);
        tv_left.setVisibility(View.GONE);
        tv_right.setVisibility(View.VISIBLE);
        iv_left.setVisibility(View.VISIBLE);
        tv_right.setText(R.string.save);

        if(mTrafficAddress == null || mTrafficAddress.length()==0){
            et_traffic_address.setText("");
        }else {
            et_traffic_address.setText(mTrafficAddress);
        }

        if(mClinicAddress == null || mClinicAddress.length() == 0){
            et_clinic_address.setText("");
        }else{
            et_clinic_address.setText(mClinicAddress);
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
                check_save();

                break;

        }
    }

    private void back() {
        Intent intentReturn = new Intent();
        intentReturn.putExtra("Back",0);
        setResult(RESULT_OK,intentReturn);
        finish();
    }

    private void check_save() {
        if(TextUtils.isEmpty(et_traffic_address.getText().toString().trim())){
            ToastUtils.showShort(mContext,"请输入交通地址");
            return;
        }else{
            mTrafficAddress = et_traffic_address.getText().toString().trim();
        }

        if(TextUtils.isEmpty(et_clinic_address.getText().toString().trim())){
            ToastUtils.showShort(mContext,"诊室地址为空");
            return;
        }else{
            mClinicAddress = et_clinic_address.getText().toString().trim();
        }

        ToastUtils.showShort(mContext,"保存成功");
        Intent intent = new Intent();
        intent.putExtra("isSetting",true);
        intent.putExtra("MapAddress",mTrafficAddress);
        intent.putExtra("ClicnicAddress",mClinicAddress);
        intent.putExtra("Back",1);
        setResult(RESULT_OK,intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        back();
    }
}
