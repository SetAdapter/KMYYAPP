package com.kmwlyy.patient.myservice;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.LogisticInfo;
import com.kmwlyy.patient.helper.net.event.HttpLogistic;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.winson.ui.widget.EmptyViewUtils;

import butterknife.BindView;

/**
 * Created by xcj on 2016/8/22.
 */
public class DeliveryActivity extends BaseActivity {

    private static final String TAG = DeliveryActivity.class.getSimpleName();

    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.tv_patient_name)
    TextView mPatientName;
    @BindView(R.id.tv_patient_sex)
    TextView mPatientSex;
    @BindView(R.id.tv_patient_age)
    TextView mPatientAge;
    @BindView(R.id.tv_patient_phone)
    TextView mPatientPhone;
    @BindView(R.id.tv_phone_title)
    TextView mPhoneTitle;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_info)
    TextView tv_detail;
    @BindView(R.id.tv_drug_number)
    TextView tv_drug_number;
    @BindView(R.id.tv_drug_amount)
    TextView tv_drug_amount;
    @BindView(R.id.tv_doctor_info)
    TextView mTradeInfo;
    @BindView(R.id.delivery_state)
    TextView delivery_state;
    @BindView(R.id.tv_delivery_detail)
    TextView tv_delivery_detail;
    @BindView(R.id.tv_pay_type)
    TextView tv_pay_type;
    @BindView(R.id.tv_logisticState)
    TextView tv_logisticState;

    private String patientName = null;
    private int age = 0;
    private int sex = 0;
    private String logisticNo = null;
    private HttpClient mHttpClient;
    private View root = null;
    private int logisticState = -1;
    //-2=待发货,-1=待审核,0=已审核,1=已备货,2=已发货,3=配送中,4=已送达

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        butterknife.ButterKnife.bind(this);
        root = ((View)this.findViewById(R.id.ll_root));
        EmptyViewUtils.showLoadingView((ViewGroup) root);
        tv_center.setText(R.string.logistic_info);
        patientName = getIntent().getStringExtra("patientName");
        age = getIntent().getIntExtra("patientAge",0);
        sex = getIntent().getIntExtra("patientSex",0);
        logisticState = getIntent().getIntExtra("logisticState",-1);
        String phoneNumber = getIntent().getStringExtra("patientPhone");
        mPatientName.setText(patientName);
        mPatientAge.setText(age+"");
        if (sex == 0){
            mPatientSex.setText(R.string.man);
        }else {
            mPatientSex.setText(R.string.women);
        }
        logisticNo = getIntent().getStringExtra("logisticNo");
       // logisticNo = "TD16082400013";
        if (TextUtils.isEmpty(phoneNumber)){
            mPhoneTitle.setVisibility(View.GONE);
            mPatientPhone.setText("");
        }else{
            mPhoneTitle.setVisibility(View.VISIBLE);
            mPatientPhone.setText(phoneNumber);
        }
        double totalFee = getIntent().getDoubleExtra("totalFee",0);
        String info = getIntent().getStringExtra("info");
        int num =  getIntent().getIntExtra("num",0);
        String time = getIntent().getStringExtra("time");
        tv_time.setText(time);
        tv_detail.setText(info);
        tv_drug_number.setText(String.valueOf(num));
        tv_drug_amount.setText("￥"+String.valueOf(totalFee));
        int payType = getIntent().getIntExtra("payType",0);
        String tradeTime = getIntent().getStringExtra("tradeTime");
        //-1=免支付,0=康美支付,1=微信支付,2=支付宝,3=中国银联,4=MasterCard,5=PayPal,6=VISA 7=his,8=余额
        String pay = null;
        switch (payType){
            case -1:
                pay = getResources().getString(R.string.pay_free);
                break;
            case 0:
                pay = getResources().getString(R.string.pay_for_km);
                break;
            case 1:
                pay = getResources().getString(R.string.pay_for_wx);
                break;
            case 2:
                pay = getResources().getString(R.string.pay_for_ali);
                break;
            case 3:
                pay = getResources().getString(R.string.pay_for_bank_card);
                break;
            case 4:
                pay = getResources().getString(R.string.pay_for_master_card);
                break;
            case 5:
                pay = getResources().getString(R.string.pay_for_pay_pal);
                break;
            case 6:
                pay = getResources().getString(R.string.pay_for_visa);
                break;
            case 7:
                pay = getResources().getString(R.string.pay_for_his);
                break;
            case 8:
                pay = getResources().getString(R.string.pay_for_account);
                break;
        }
        switch(logisticState){
            case -2:
                tv_logisticState.setText(getResources().getString(R.string.wait_send_out));
                break;
            case -1:
                tv_logisticState.setText(getResources().getString(R.string.wait_audit));
                break;
            case 0:
                tv_logisticState.setText(getResources().getString(R.string.audited));
                break;
            case 1:
                tv_logisticState.setText(getResources().getString(R.string.stock_up));
                break;
            case 2:
                tv_logisticState.setText(getResources().getString(R.string.delivered));
                break;
            case 3:
                tv_logisticState.setText(getResources().getString(R.string.delivery_ing));
                break;
            case 4:
                tv_logisticState.setText(getResources().getString(R.string.arrived));
                break;
        }
        if (!TextUtils.isEmpty(tradeTime)) {
            mTradeInfo.setText(tradeTime.substring(0,10)+" "+tradeTime.substring(11,16));
        }else{
            mTradeInfo.setText(tradeTime);
        }
        tv_pay_type.setText(pay);
        getDetails();
    }
    private void getDetails(){
        if (TextUtils.isEmpty(logisticNo)){
            delivery_state.setText(getResources().getString(R.string.carrier_source) + "" + "\n" + getResources().getString(R.string.waybill_no) + "");
            tv_delivery_detail.setText("" + "\n" + "");
            EmptyViewUtils.removeAllView((ViewGroup) root);
            return;
        }
        HttpLogistic.GetDetail getDetail = new HttpLogistic.GetDetail(logisticNo, new HttpListener<LogisticInfo>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getRecipeOrdersList", code, msg));
                }
                EmptyViewUtils.showErrorView((ViewGroup) root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDetails();
                    }
                });
            }

            @Override
            public void onSuccess(LogisticInfo logisticInfo) {
                Log.d(TAG, DebugUtils.successFormat("logisticInfo", PUtils.toJson(logisticInfo)));
                if(logisticInfo.mCompany == null || logisticInfo.mCompany.mLogisCompany == null){
                    delivery_state.setText(getResources().getString(R.string.carrier_source) + "" + "\n" + getResources().getString(R.string.waybill_no) + "");
                }else {
                    delivery_state.setText(getResources().getString(R.string.carrier_source) + logisticInfo.mCompany.mLogisCompany + "\n" + getResources().getString(R.string.waybill_no) + logisticInfo.mCompany.mOrderId);
                }
                if (logisticInfo.mLogisticList == null || logisticInfo.mLogisticList.size() == 0){
                    tv_delivery_detail.setText("" + "\n" + "");
                }else {
                    int i = logisticInfo.mLogisticList.size() - 1;
                    tv_delivery_detail.setText(logisticInfo.mLogisticList.get(i).mAddrInfo + "\n" + logisticInfo.mLogisticList.get(i).mTime);
                }
                EmptyViewUtils.removeAllView((ViewGroup) root);
            }
        });

        mHttpClient = NetService.createClient(this, getDetail);
        mHttpClient.start();
    }
}
