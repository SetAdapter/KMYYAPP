package com.kmwlyy.patient.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.event.HttpUser;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.AccountDetail;
import com.kmwlyy.patient.helper.net.event.AccountEvent;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.winson.ui.widget.AlterDialogView;
import com.winson.ui.widget.RateLayout;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

/**
 * Created by xcj on 2016/12/9.
 */

public class SetPaymentCodeActivity extends BaseActivity implements View.OnClickListener{

    public static final String IS_SET_PAY_PASSWORD = "IS_SET_PAY_PASSWORD";
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.tv_phone_number)
    TextView mPhoneNumber;
    @BindView(R.id.et_verification_code)
    EditText mVerficationCodeEdit;
    @BindView(R.id.btn_verification_code)
    Button mVerficationCodeBtn;
    @BindView(R.id.confirm)
    Button mConfirm;
    @BindView(R.id.edit_input_pay_password)
    EditText mInputPayPassword;
    @BindView(R.id.edit_confirm_pay_password)
    EditText mConfirmPayPassword;

    private boolean isSetPayPassword;
    private  String mobile;
    private String mRepeatGetIndentifyCodeStr;
    private CountDownTimer mTimer;
    private boolean mCouldNotRepeatGetIndetifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_payment_code);
        butterknife.ButterKnife.bind(this);
        isSetPayPassword = getIntent().getBooleanExtra(IS_SET_PAY_PASSWORD, false);
        if (isSetPayPassword){
            tv_center.setText(getString(R.string.reset_pay_password_title));
            mConfirm.setText(getString(R.string.reset_pay_password));
        }else{
            tv_center.setText(getString(R.string.set_pay_password_title));
            mConfirm.setText(getString(R.string.set_pay_password));
        }
        mRepeatGetIndentifyCodeStr = getResources().getString(R.string.get_pay_password_code);
        mVerficationCodeBtn.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        getInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verification_code:
                if (mCouldNotRepeatGetIndetifyCode) {
                    return;
                }
                if (TextUtils.isEmpty(mobile)){
                    showAlterDialog();
                    return;
                }
                getCode();
                mCouldNotRepeatGetIndetifyCode = true;
                mVerficationCodeBtn.setEnabled(false);
                mVerficationCodeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn_bg_gray));
                mVerficationCodeBtn.setText(String.format(mRepeatGetIndentifyCodeStr, 60));
                if (mTimer == null) {

                    mTimer = new CountDownTimer(60000, 300) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            mVerficationCodeBtn.setText(String.format(mRepeatGetIndentifyCodeStr,
                                    millisUntilFinished / 1000));
                        }

                        @Override
                        public void onFinish() {
                            mVerficationCodeBtn.setEnabled(true);
                            mVerficationCodeBtn.setText(com.kmwlyy.login.R.string.get_identify_code);
                            mVerficationCodeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn_bg));
                            mCouldNotRepeatGetIndetifyCode = false;
                        }
                    };
                }
                mTimer.cancel();
                mTimer.start();
                break;
            case R.id.confirm:
                confirm();
                break;
        }
    }

    private void getCode(){
        showLoadDialog(R.string.get_code);
        HttpUser.SendSmsCode sendSmsCode = new HttpUser.SendSmsCode(mobile, 3, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                ToastUtils.showShort(SetPaymentCodeActivity.this,msg);
            }

            @Override
            public void onSuccess(Object o) {
                dismissLoadDialog();
                ToastUtils.showShort(SetPaymentCodeActivity.this, getString(R.string.get_code_suc));
            }
        });
        HttpClient getOrderClient = NetService.createClient(this, sendSmsCode);
        getOrderClient.start();
    }

    private void confirm(){
        String code =  mVerficationCodeEdit.getText().toString().trim();
        String inputPayPassword =  mInputPayPassword.getText().toString().trim();
        String confirmPayPassword =  mConfirmPayPassword.getText().toString().trim();
        if (TextUtils.isEmpty(code)){
            ToastUtils.showShort(SetPaymentCodeActivity.this, getString(R.string.plz_input_code));
            return;
        }
        if (TextUtils.isEmpty(inputPayPassword)){
            ToastUtils.showShort(SetPaymentCodeActivity.this, getString(R.string.plz_input_password));
            return;
        }
        if (TextUtils.isEmpty(confirmPayPassword)){
            ToastUtils.showShort(SetPaymentCodeActivity.this, getString(R.string.plz_input_confirm_password));
            return;
        }
        showLoadDialog(R.string.set_pay_password_info);
        AccountEvent.SetPayPassword setPayPassword = new AccountEvent.SetPayPassword(code, inputPayPassword, confirmPayPassword, new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                ToastUtils.showShort(SetPaymentCodeActivity.this,msg);
            }

            @Override
            public void onSuccess(String o) {
                dismissLoadDialog();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(o);
                    String data = jsonObject.optString("Data");
                    String msg = jsonObject.optString("Msg");
                    //1成功，0失败，-1支付密码为6位字符串，-2两次输入的密码不一致，-3用户没有设置手机号，-4验证码不存在或已经过期
                    if ("1".equals(data)){
                        ToastUtils.showShort(SetPaymentCodeActivity.this, getString(R.string.pay_password_set_suc));
                        EventBus.getDefault().post(new EventApi.SetPayPasswordSuc());
                        finish();
                    }else {
                        ToastUtils.showShort(SetPaymentCodeActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }
        });

        HttpClient getOrderClient = NetService.createClient(this, setPayPassword);
        getOrderClient.start();
    }

    private void getInfo(){
        showLoadDialog(R.string.get_set_pay_code_info);
        AccountEvent.GetAccountDetail detail = new AccountEvent.GetAccountDetail(new HttpListener<AccountDetail>() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                ToastUtils.showShort(SetPaymentCodeActivity.this, getString(R.string.get_person_info_fail));
            }

            @Override
            public void onSuccess(AccountDetail accountDetail) {
                dismissLoadDialog();
                if (TextUtils.isEmpty(accountDetail.mUser.mMobile)){
                    showAlterDialog();
                }else{
                    mobile = accountDetail.mUser.mMobile;
                    mPhoneNumber.setText(mobile);
                }
            }
        });

        HttpClient mHttpClient = NetService.createClient(this, detail);
        mHttpClient.start();
    }

    private void showAlterDialog(){
        AlterDialogView.Builder builder = new AlterDialogView.Builder(this);
        builder.setMessage(getString(R.string.bind_mobile_note));
        builder.setTitle(getString(R.string.not_bind_mobile_title));
        builder.setPositiveButton(getString(R.string.bind_yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        //打开绑定页面
                        Intent intent = new Intent(SetPaymentCodeActivity.this, BindMobileActivity.class);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton(getString(R.string.bind_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create().show();
    }
}
