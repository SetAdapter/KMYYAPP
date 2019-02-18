package com.kmwlyy.patient.account;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.event.HttpUser;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.event.AccountEvent;

import butterknife.BindView;

/**
 * Created by xcj on 2017/1/7.
 */

public class BindMobileActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.edit_input_mobile)
    EditText mMobileEdit;
    @BindView(R.id.et_verification_code)
    EditText mVerficationCodeEdit;
    @BindView(R.id.btn_verification_code)
    Button mVerficationCodeBtn;
    @BindView(R.id.confirm)
    Button mConfirm;

    private String mRepeatGetIndentifyCodeStr;
    private CountDownTimer mTimer;
    private boolean mCouldNotRepeatGetIndetifyCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_mobile);
        setBarTitle(getString(R.string.bind_mobile_title));
        butterknife.ButterKnife.bind(this);
        mRepeatGetIndentifyCodeStr = getResources().getString(R.string.get_pay_password_code);
        mVerficationCodeBtn.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verification_code:
                if (mCouldNotRepeatGetIndetifyCode) {
                    return;
                }
                if (TextUtils.isEmpty(mMobileEdit.getText().toString())){
                    ToastUtils.showShort(BindMobileActivity.this, getString(R.string.plz_input_mobile));
                    return;
                }
                getCode(mMobileEdit.getText().toString());
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

    private void getCode(String mobile){
        showLoadDialog(R.string.get_code);
        HttpUser.SendSmsCode sendSmsCode = new HttpUser.SendSmsCode(mobile, 4, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                ToastUtils.showShort(BindMobileActivity.this,msg);
            }

            @Override
            public void onSuccess(Object o) {
                dismissLoadDialog();
                ToastUtils.showShort(BindMobileActivity.this, getString(R.string.get_code_suc));
            }
        });
        HttpClient getOrderClient = NetService.createClient(this, sendSmsCode);
        getOrderClient.start();
    }

    private void confirm(){
        String code = mVerficationCodeEdit.getText().toString();
        String mobile = mMobileEdit.getText().toString();
        if (TextUtils.isEmpty(mobile)){
            ToastUtils.showShort(BindMobileActivity.this, getString(R.string.plz_input_mobile));
            return;
        }
        if (TextUtils.isEmpty(code)){
            ToastUtils.showShort(BindMobileActivity.this, getString(R.string.plz_input_code));
            return;
        }
        showLoadDialog(R.string.bind_mobile_ing);
        AccountEvent.BindMobile bindMobile = new AccountEvent.BindMobile(mobile, code, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                ToastUtils.showShort(BindMobileActivity.this, getString(R.string.bind_mobile_fail));
            }

            @Override
            public void onSuccess(Object o) {
                dismissLoadDialog();
                ToastUtils.showShort(BindMobileActivity.this, getString(R.string.bind_mobile_suc));
            }
        });
        HttpClient getOrderClient = NetService.createClient(this, bindMobile);
        getOrderClient.start();


    }
}
