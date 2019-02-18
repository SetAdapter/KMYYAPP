package com.kmwlyy.doctor.Activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.event.HttpUser;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.EventAccountApi;
import com.kmwlyy.doctor.model.httpEvent.AccountEvent;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by xcj on 2016/12/9.
 */

public class SetPaymentCodeActivity extends BaseActivity implements View.OnClickListener{

    public static final String IS_SET_PAY_PASSWORD = "IS_SET_PAY_PASSWORD";
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_center)
    TextView tv_title;
    @ViewInject(R.id.tv_phone_number)
    TextView mPhoneNumber;
    @ViewInject(R.id.et_verification_code)
    EditText mVerficationCodeEdit;
    @ViewInject(R.id.btn_verification_code)
    Button mVerficationCodeBtn;
    @ViewInject(R.id.confirm)
    Button mConfirm;
    @ViewInject(R.id.edit_input_pay_password)
    EditText mInputPayPassword;
    @ViewInject(R.id.edit_confirm_pay_password)
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
        ViewUtils.inject(this);
        btn_left.setVisibility(View.VISIBLE);
        tv_title.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        isSetPayPassword = getIntent().getBooleanExtra(IS_SET_PAY_PASSWORD, false);
        if (isSetPayPassword){
            tv_title.setText("重置支付密码");
            mConfirm.setText("重置密码");
        }else{
            tv_title.setText("设置支付密码");
            mConfirm.setText("设置密码");
        }
        mobile = BaseApplication.getInstance().getUserData().mMobile;
        mPhoneNumber.setText(mobile);
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
                ToastUtils.showShort(SetPaymentCodeActivity.this,"验证码获取成功");
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
            ToastUtils.showShort(SetPaymentCodeActivity.this,"请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(inputPayPassword)){
            ToastUtils.showShort(SetPaymentCodeActivity.this,"请输入密码");
            return;
        }
        if (TextUtils.isEmpty(confirmPayPassword)){
            ToastUtils.showShort(SetPaymentCodeActivity.this,"请输入确认密码");
            return;
        }
        showLoadDialog(R.string.set_pay_password_info);
        AccountEvent.SetPayPassword setPayPassword = new AccountEvent.SetPayPassword(code, inputPayPassword, confirmPayPassword, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                ToastUtils.showShort(SetPaymentCodeActivity.this,msg);
            }

            @Override
            public void onSuccess(Object o) {
                dismissLoadDialog();
                ToastUtils.showShort(SetPaymentCodeActivity.this,"支付密码设置成功");
                EventBus.getDefault().post(new EventAccountApi.SetPayPasswordSuc());
                finish();
            }
        });

        HttpClient getOrderClient = NetService.createClient(this, setPayPassword);
        getOrderClient.start();
    }
}
