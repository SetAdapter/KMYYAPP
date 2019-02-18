package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by KM on 2017/6/27.
 */

public class SettingPeriodActivity extends BaseActivity {

    @ViewInject(R.id.iv_left)
    ImageView ivLeft;
    @ViewInject(R.id.tv_left)
    TextView tvLeft;
    @ViewInject(R.id.tv_center)
    TextView tvCenter;
    @ViewInject(R.id.tv_right)
    TextView tvRight;

    @ViewInject(R.id.btn_plus)
    Button btnPlus;
    @ViewInject(R.id.et_person_max)
    EditText etPersonMax;
    @ViewInject(R.id.btn_minus)
    Button btnMinus;
    @ViewInject(R.id.lv_price)
    ListView lv_price;
    private int personNum;


    private FamilyDoctorSettingAdapter mFamilyDoctorSettingAdapter;
    private List<HomeSettingBean.DoctorPackageBean> mPrice = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_setting);

        ViewUtils.inject(this);
        getIntentInfo();
        initView();
        initListener();
    }

    private void getIntentInfo(){
        mPrice = (List<HomeSettingBean.DoctorPackageBean>) getIntent().getSerializableExtra("Price");
        personNum = getIntent().getIntExtra("PersonNum",0);

    }

    private void initView() {

//        checkEdit();
        tvCenter.setText(R.string.crycle_price_person_setting);
        tvRight.setVisibility(View.VISIBLE);
        tvLeft.setVisibility(View.GONE);
        ivLeft.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.save);
        etPersonMax.setText(personNum+"");

        if(mPrice != null) {
            if(mPrice.size()>0) {
                mFamilyDoctorSettingAdapter = new FamilyDoctorSettingAdapter(mContext, true, mPrice);
                lv_price.setAdapter(mFamilyDoctorSettingAdapter);
            }
        }else{
         ToastUtils.showShort(mContext,"数据有误");
        }



    }

    private void checkEdit() {
        if(TextUtils.isEmpty(etPersonMax.getText())){
            personNum = 0;

        }else{
            personNum = Integer.parseInt(etPersonMax.getText().toString().trim());
        }
    }

    private void initListener() {
        ivLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch(arg0.getId()){
            case R.id.iv_left:
                back();
                break;
            case R.id.tv_right:
                checkInfomation();

                break;
            case R.id.btn_plus:
                addPersonNum();
                break;
            case R.id.btn_minus:
                minPersonNum();
                break;

        }
    }

    private void back() {
        Intent intentReturn = new Intent();
        boolean isEmpty  = false;
        for (HomeSettingBean.DoctorPackageBean bean : mPrice) {
           if(bean.getServicePrice() != null && !bean.getServicePrice().equals("")){
               if(Integer.parseInt(bean.getServicePrice())>0){
                   isEmpty = true;
                   break;
               }
           }
        }
        if((TextUtils.isEmpty(etPersonMax.getText().toString().trim()) || etPersonMax.getText().toString().trim().equals("0")) && !isEmpty){
            intentReturn.putExtra("isSetting",false);
//            intentReturn.putExtra("PriceList",(Serializable)mPrice);
        }else {
            intentReturn.putExtra("isSetting",true);

        }
        intentReturn.putExtra("Back",0);
        setResult(RESULT_OK,intentReturn);
        finish();
    }

    private void minPersonNum() {
        checkEdit();
        personNum--;
        if(personNum < 0){
            ToastUtils.showShort(mContext,"人数不能为负");
            personNum = 0;
            etPersonMax.setText(personNum+"");

        }else {
            etPersonMax.setText(personNum + "");

        }
    }

    private void addPersonNum() {
        checkEdit();
        personNum++;
        etPersonMax.setText(personNum+"");

    }

    private void checkInfomation() {
        // TODO: 2017/6/29 检查填充的设置信息
        mPrice = mFamilyDoctorSettingAdapter.getDataList();

        for (HomeSettingBean.DoctorPackageBean bean : mPrice) {
            String servicePrice = bean.getServicePrice();
            if(null == servicePrice || servicePrice.equals("")){
                ToastUtils.showShort(mContext,bean.getMonthCountName()+"价格不能为空");
                return;
            }
        }



        if(TextUtils.isEmpty(etPersonMax.getText().toString().trim()) || etPersonMax.getText().toString().trim().equals("0")){

            ToastUtils.showShort(mContext,"服务人数不能为空");
            return;
        }else{
            personNum = Integer.parseInt(etPersonMax.getText().toString().trim());

        }

        Intent intent = new Intent();
        intent.putExtra("PriceList",(Serializable)mPrice);
        intent.putExtra("isSetting",true);
        intent.putExtra("PersonCount",personNum);
        intent.putExtra("Back",1);
        setResult(RESULT_OK,intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        back();
    }
}
