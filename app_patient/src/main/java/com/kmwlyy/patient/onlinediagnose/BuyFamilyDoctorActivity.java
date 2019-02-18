package com.kmwlyy.patient.onlinediagnose;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.patient.PApplication;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.pay.PayActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.Doctor;
import com.kmwlyy.patient.helper.net.bean.UserFamilyDoctor;
import com.kmwlyy.patient.helper.net.event.HttpUserFamilyDoctor;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.winson.ui.widget.RateLayout;
import com.winson.ui.widget.TagBaseAdapter;
import com.winson.ui.widget.TagCloudLayout;
import com.winson.ui.widget.ToastMananger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Winson on 2016/8/19.
 */
public class BuyFamilyDoctorActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = BuyFamilyDoctorActivity.class.getSimpleName();

    public static final String DOCTOR = "DOCTOR";
    private HttpClient mAddFamilyDoctorClient;
    private HttpClient mGetDoctorServiceInfoClient;
    private ProgressDialog mGetServiceInfoDialog;


    public static void startBuyHomeDoctorActivity(Context context, Doctor.Detail listItem) {
        Intent intent = new Intent(context, BuyFamilyDoctorActivity.class);

        intent.putExtra(DOCTOR, listItem);

        context.startActivity(intent);
    }


    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.service_period)
    TextView mServicePeriod;
    @BindView(R.id.service_price)
    TextView mServicePrice;
    @BindView(R.id.pay_now)
    Button mPayNow;
    @BindView(R.id.service_content)
    TextView mServiceContent;
    @BindView(R.id.tv_service_money)
    TextView mServiceMoneyTxt;
    @BindView(R.id.tcl_container)
    TagCloudLayout tcl_container;

    private Doctor.Detail mDoctor;
    private String mCurrentDate;
    private String mEndDate;
    private float mFamilyDoctorServicePrice;
    private UserFamilyDoctor.ServiceInfo mDoctorServiceInfo;
    private TagBaseAdapter tagAdapter;
    private List<String> mTagList;
    private String mMonth = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_home_doctor);
        butterknife.ButterKnife.bind(this);
        setBarTitle(R.string.buy_home_doctor);

        mDoctor = (Doctor.Detail) getIntent().getSerializableExtra(DOCTOR);
        mPayNow.setOnClickListener(this);
        mTagList = new ArrayList<>();
        tagAdapter = new TagBaseAdapter(this,mTagList,TagBaseAdapter.WHITE);
        tcl_container.setAdapter(tagAdapter);

        tcl_container.setItemClickListener(new TagCloudLayout.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                tagAdapter.setHighLight(mTagList.get(position));
                updateDoctorServiceInfo(position);
            }
        });
        getDoctorServiceInfo();
        updateDoctorServiceInfo(0);
    }

    private void updateDoctorServiceInfo(int position) {

        if (mDoctorServiceInfo != null) {
            mMonth =  mDoctorServiceInfo.mDetails.get(position).mMonth;
            mCurrentDate = mDoctorServiceInfo.mDetails.get(position).mStartDate.substring(0, 10);
            mEndDate = mDoctorServiceInfo.mDetails.get(position).mEndDate.substring(0, 10);
            mServiceContent.setText(mDoctorServiceInfo.mServiceContent);
            mServicePrice.setText(mTagList.get(position));
            mServicePeriod.setText(String.format(getResources().getString(R.string.service_period_content), mCurrentDate, mEndDate));
            mServiceMoneyTxt.setText(getString(R.string.service_account)+mDoctorServiceInfo.mDetails.get(position).mServicePrice);

        } else {
            mCurrentDate = null;
            mEndDate = null;

            mServiceContent.setText(null);
            mServicePrice.setText(getString(R.string.service_time_none));
            mServicePeriod.setText(String.format(getResources().getString(R.string.service_period_content), "--", "--"));
            mServiceMoneyTxt.setText(getString(R.string.service_account)+0);
        }

    }

    private void getDoctorServiceInfo() {
        showGetServiceInfoDialog();
        HttpUserFamilyDoctor.GetDoctorServiceInfo getDoctorServiceInfo = new HttpUserFamilyDoctor.GetDoctorServiceInfo(
                mDoctor.mDoctorID, new HttpListener<UserFamilyDoctor.ServiceInfo>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getDoctorServiceInfo", code, msg));
                }
                ToastMananger.showToast(BuyFamilyDoctorActivity.this, R.string.get_doctor_service_info_failed, Toast.LENGTH_SHORT);
                updateDoctorServiceInfo(0);
                dismissServiceInfoDialog();
            }

            @Override
            public void onSuccess(UserFamilyDoctor.ServiceInfo serviceInfo) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("getDoctorServiceInfo", DebugUtils.toJson(serviceInfo)));
                }

                mDoctorServiceInfo = serviceInfo;
                mTagList.clear();
                for (UserFamilyDoctor.Detail detail: mDoctorServiceInfo.mDetails){
                    mTagList.add(detail.mMonth+getString(R.string.service_time_unit));
                }
                tagAdapter.notifyDataSetChanged();
                updateDoctorServiceInfo(0);
                dismissServiceInfoDialog();

            }
        }
        );

        mGetDoctorServiceInfoClient = NetService.createClient(this, getDoctorServiceInfo);
        mGetDoctorServiceInfoClient.start();

    }

    private void showGetServiceInfoDialog() {
        mGetServiceInfoDialog = new ProgressDialog(this);
        mGetServiceInfoDialog.setMessage(getResources().getString(R.string.on_get_service_info));
        mGetServiceInfoDialog.setCancelable(false);
        mGetServiceInfoDialog.show();
    }

    private void dismissServiceInfoDialog() {
        if (mGetServiceInfoDialog != null) {
            mGetServiceInfoDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetService.closeClient(mAddFamilyDoctorClient);
        NetService.closeClient(mGetDoctorServiceInfoClient);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.pay_now:
                createFamilyDoctorOrder();
                break;
        }
    }

    private void createFamilyDoctorOrder() {
        if (mDoctorServiceInfo == null) {
            ToastMananger.showToast(this, R.string.doctor_service_is_null, Toast.LENGTH_SHORT);
            return;
        }
        HttpUserFamilyDoctor.Add addFamilyDoctor = new HttpUserFamilyDoctor.Add(
                BaseApplication.getInstance().getUserData().mUserId,
                mDoctor.mDoctorID,
                mMonth,
                new HttpListener<UserFamilyDoctor.AddResp>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("addFamilyDoctor", code, msg));
                        }
//                        ToastMananger.showToast(BuyFamilyDoctorActivity.this, R.string.buy_family_doctor_service_failed, Toast.LENGTH_SHORT);
                        ToastMananger.showToast(BuyFamilyDoctorActivity.this, msg, Toast.LENGTH_SHORT);

                    }

                    @Override
                    public void onSuccess(UserFamilyDoctor.AddResp addResp) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("addFamilyDoctor", DebugUtils.toJson(addResp)));
                        }
                        ToastMananger.showToast(BuyFamilyDoctorActivity.this, R.string.buy_family_doctor_service_success, Toast.LENGTH_SHORT);
//                        CommonUtils.startActivity(BuyFamilyDoctorActivity.this, PayActivity.class);

                        PayActivity.startPayActivity(BuyFamilyDoctorActivity.this, addResp.mOrderNo,"0", PayActivity.BUY_FAMILY_DOCTOR, true);
                        finish();

                    }
                }
        );

        mAddFamilyDoctorClient = NetService.createClient(this, addFamilyDoctor);
        mAddFamilyDoctorClient.start();

    }
}
