package com.kmwlyy.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UserData;
import com.kmwlyy.core.net.event.HttpUser;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.winson.ui.widget.RateLayout;
import com.winson.ui.widget.ToastMananger;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Winson on 2016/8/6.
 */
public class RegisterActivity extends NormalActivity implements View.OnClickListener {

    public static final String TAG = RegisterActivity.class.getSimpleName();

    public static final String FORGET_PWD = "FORGET_PWD";

    @BindView(R2.id.user_name)
    EditText mUserName;
    @BindView(R2.id.identify_code)
    EditText mIdentifyCode;
    @BindView(R2.id.get_identify_code)
    Button mGetIdentifyCode;
    @BindView(R2.id.password)
    EditText mPassword;
    @BindView(R2.id.register)
    Button mRegister;
    @BindView(R2.id.navigation_icon)
    ImageView mNavigationIcon;
    @BindView(R2.id.navigation_btn)
    RateLayout mNavigationBtn;
    @BindView(R2.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R2.id.ll_protocols)
    LinearLayout mProtcolsLinearLayout;
    @BindView(R2.id.cb_protocols)
    CheckBox mProtocolsCheckBox;
    @BindView(R2.id.tv_protocols)
    TextView mProtocolsTxt;

    private String mRepeatGetIndentifyCodeStr;
    private CountDownTimer mTimer;
    private boolean mCouldNotRepeatGetIndetifyCode;
    private boolean mForgetPwd;
    private int mAppType;
    private String url = "http://121.15.153.63:8018/LicenseAgreement.html";

    private HttpClient mSendSmsClient;
    private HttpClient mRegisterClient;
    private HttpClient mLoginClient;
    private ProgressDialog mLoginDialog;
    private ProgressDialog mRegisterDialog;
    private ProgressDialog mFindPwdDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ButterKnife.bind(this);
        mSkipFinsih = true;

        mForgetPwd = getIntent().getBooleanExtra(FORGET_PWD, false);
        mAppType = getIntent().getIntExtra(LoginActivity.APP_TYPE, LoginActivity.PATIENT);
        if (!mForgetPwd && mAppType == LoginActivity.DOCTOR) {
            mProtcolsLinearLayout.setVisibility(View.VISIBLE);
            mProtocolsCheckBox.setEnabled(true);
            mProtocolsTxt.setEnabled(true);
            mRegister.setEnabled(false);
        } else {
            mProtcolsLinearLayout.setVisibility(View.GONE);
            mProtocolsCheckBox.setEnabled(false);
            mProtocolsTxt.setEnabled(false);
            mRegister.setEnabled(true);
        }

        mProtocolsTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到H5页面
                WebViewActivity.startWebViewActivity(RegisterActivity.this, url);
            }
        });

        mProtocolsCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //选中才能注册
                setBtnEnable();
            }
        });

        mToolbarTitle.setTextColor(getResources().getColor(R.color.white));

        mToolbarTitle.setText(mForgetPwd ? R.string.forget_password_title : R.string.register);
        mRegister.setText(mForgetPwd ? R.string.confirm : R.string.register);
        mPassword.setHint(mForgetPwd ? R.string.find_password_hint : R.string.register_hint);

        mNavigationBtn.setVisibility(View.VISIBLE);
        mNavigationIcon.setBackgroundResource(R.drawable.back_white);

        mGetIdentifyCode.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mNavigationBtn.setOnClickListener(this);

        mRepeatGetIndentifyCodeStr = getResources().getString(R.string.repeat_get_identify_code);

        findViewById(R.id.navigation_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mForgetPwd) {
                    forgetPwd();
                } else {
                    register();
                }
            }
        });

        findViewById(R.id.get_identify_code).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIdentifyCode();
            }
        });

        //必填项都填了，注册按钮才变enable
        setBtnEnable();
        mUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                setBtnEnable();
            }
        });
        mIdentifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                setBtnEnable();
            }
        });
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                setBtnEnable();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    @Override
    public void onClick(View v) {
//        int id = v.getId();
//        switch (id) {
//            case R2.id.navigation_btn:
//                onBackPressed();
//                break;
//            case R2.id.register:
//                if (mForgetPwd) {
//                    forgetPwd();
//                } else {
//                    register();
//                }
//                break;
//            case R2.id.get_identify_code:
//                getIdentifyCode();
//                break;
//        }
    }

    private void forgetPwd() {
        final String phone = mUserName.getText().toString();
        if (checkPhone(phone)) {
            return;
        }

        String indentifyCode = mIdentifyCode.getText().toString();
        if (TextUtils.isEmpty(indentifyCode)) {
            ToastUtils.show(this, R.string.please_input_indentify_code, Toast.LENGTH_SHORT);
            return;
        }

        final String pwd = mPassword.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show(this, R.string.please_input_pwd, Toast.LENGTH_SHORT);
            return;
        }
        if (!CommonUtils.checkPwd(pwd)) {
            ToastUtils.show(this, R.string.please_input_true_pwd, Toast.LENGTH_SHORT);
            ToastUtils.show(this, R.string.true_pwd_format, Toast.LENGTH_SHORT);
            return;
        }

        showFindPwdDialog();

        HttpUser.FindPassword findPassword = new HttpUser.FindPassword(phone, pwd, indentifyCode,mAppType, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("findPassword", code, msg));
                }
//                ToastMananger.showToast(RegisterActivity.this, R.string.find_pwd_failed, Toast.LENGTH_SHORT);
                ToastMananger.showToast(RegisterActivity.this, msg, Toast.LENGTH_SHORT);
                dismissFindPwdDialog();

            }

            @Override
            public void onSuccess(Object o) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("findPassword", DebugUtils.toJson(o)));
                }

                dismissFindPwdDialog();
                login(phone, pwd);
            }
        });

        HttpClient findPasswordClient = new HttpClient(this, findPassword, BaseApplication.instance.getHttpFilter());
        findPasswordClient.start();

    }

    private void showFindPwdDialog() {
        mFindPwdDialog = new ProgressDialog(this);
        mFindPwdDialog.setMessage(getResources().getString(R.string.on_find_pwd));
        mFindPwdDialog.setCancelable(false);
        mFindPwdDialog.show();
    }

    private void dismissFindPwdDialog() {
        if (mFindPwdDialog != null) {
            mFindPwdDialog.dismiss();
        }
    }

    private void getIdentifyCode() {
        if (mCouldNotRepeatGetIndetifyCode) {
            return;
        }

        String phone = mUserName.getText().toString();
        if (checkPhone(phone)) {
            return;
        }

        requestIndentifyCode(phone);
    }

    private boolean checkPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, R.string.phone_cannot_be_null, Toast.LENGTH_SHORT);
            return true;
        }

        if (!CommonUtils.checkPhone(phone)) {
            ToastUtils.show(this, R.string.please_input_true_phone, Toast.LENGTH_SHORT);
            return true;
        }
        return false;
    }

    private void requestIndentifyCode(String phone) {
        HttpUser.SendSmsCode sendSmsCodeEvent = new HttpUser.SendSmsCode(phone,
                mForgetPwd ? HttpUser.MSG_TYPE_FORGET_PWD : HttpUser.MSG_TYPE_REGISTER, new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, "requestIndentifyCode onError , code : " + code + " , msg : " + msg);
                }
                ToastUtils.show(RegisterActivity.this, R.string.get_identify_code_failed, Toast.LENGTH_SHORT);
                ToastUtils.show(RegisterActivity.this, msg, Toast.LENGTH_SHORT);

            }

            @Override
            public void onSuccess(String s) {
                if (DebugUtils.debug) {
                    Log.d(TAG, "requestIndentifyCode onSuccess!");
                }
                ToastUtils.show(RegisterActivity.this, R.string.get_identify_code_success, Toast.LENGTH_SHORT);
                startTime();
            }
        });

        mSendSmsClient = new HttpClient(this, sendSmsCodeEvent, BaseApplication.instance.getHttpFilter());
        mSendSmsClient.start();
    }

    private void register() {
        final String phone = mUserName.getText().toString();
        if (checkPhone(phone)) {
            return;
        }

        String indentifyCode = mIdentifyCode.getText().toString();
        if (TextUtils.isEmpty(indentifyCode)) {
            ToastUtils.show(this, R.string.please_input_indentify_code, Toast.LENGTH_SHORT);
            return;
        }

        final String pwd = mPassword.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show(this, R.string.please_input_pwd, Toast.LENGTH_SHORT);
            return;
        }
        if (!CommonUtils.checkPwd(pwd)) {
            ToastUtils.show(this, R.string.please_input_true_pwd, Toast.LENGTH_SHORT);
            ToastUtils.show(this, R.string.true_pwd_format, Toast.LENGTH_SHORT);
            return;
        }

        showRegisterProgress();

        HttpUser.Register registerEvent = new HttpUser.Register(phone, pwd, indentifyCode, mAppType,
                new HttpListener() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, "register error!");
                        }
                        dismissRegisterProgress();
                        ToastUtils.show(RegisterActivity.this, R.string.register_failed, Toast.LENGTH_SHORT);
                        ToastUtils.show(RegisterActivity.this, msg, Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onSuccess(Object o) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, "register success!");
                        }
                        dismissRegisterProgress();
                        login(phone, pwd);
                    }
                });

        mRegisterClient = new HttpClient(this, registerEvent, BaseApplication.instance.getHttpFilter());
        mRegisterClient.start();
    }

    private void login(final String phone, final String pwd) {
        showLoginDialog();

        HttpUser.Login loginEvent = new HttpUser.Login(phone, pwd, mAppType, BaseApplication.instance.getJpushRegisterID(), new HttpListener<UserData>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, "login error , code : " + code + " , msg : " + msg);
                }
                ToastUtils.show(RegisterActivity.this, R.string.login_failed, Toast.LENGTH_SHORT);
                dismissLoginDialog();
            }

            @Override
            public void onSuccess(UserData userData) {
//                if (DebugUtils.debug) {
//                    Log.d(TAG, "login result : " + PUtils.toJson(userData));
//                }
                if (DebugUtils.debug) {
                    Log.d(TAG, "login success!");
                }

                SPUtils.saveLoginInfo(RegisterActivity.this, phone, pwd);
                SPUtils.saveLoginTime(RegisterActivity.this, System.currentTimeMillis());

                BaseApplication.instance.setUserData(userData);
                dismissLoginDialog();
                registerSuccess(userData.mCheckState);
            }
        });

        mLoginClient = new HttpClient(this, loginEvent, BaseApplication.instance.getHttpFilter());
        mLoginClient.start();
    }

    private void registerSuccess(int state) {
        EventLoginApi.Login mLogin = new EventLoginApi.Login();
        mLogin.state = state;
        EventBus.getDefault().post(mLogin);
        finish();
    }

    private void showRegisterProgress() {
        mRegisterDialog = new ProgressDialog(this);
        mRegisterDialog.setMessage(getResources().getString(R.string.on_register_please_wait));
        mRegisterDialog.setCancelable(false);
        mRegisterDialog.show();
    }

    private void dismissRegisterProgress() {
        if (mRegisterDialog != null) {
            mRegisterDialog.dismiss();
        }
    }

    private void showLoginDialog() {
        mLoginDialog = new ProgressDialog(this);
        mLoginDialog.setMessage(getResources().getString(R.string.on_login_please_wait));
        mLoginDialog.setCancelable(false);
        mLoginDialog.show();
    }

    private void dismissLoginDialog() {
        if (mLoginDialog != null) {
            mLoginDialog.dismiss();
        }
    }

    /**
     * 根据三个输入框的内容，动态改变注册按钮是否可点击
     */
    public void setBtnEnable(){
        if(!mUserName.getText().toString().isEmpty() &&!mIdentifyCode.getText().toString().isEmpty() &&!mPassword.getText().toString().isEmpty()){

            if (!mForgetPwd && mAppType == LoginActivity.DOCTOR) {
                if(mProtocolsCheckBox.isChecked()){
                    mRegister.setEnabled(true);
                    mRegister.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn_bg));
                }else{
                    mRegister.setEnabled(false);
                    mRegister.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn_bg_gray));
                }
            }else{
                mRegister.setEnabled(true);
                mRegister.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn_bg));
            }
        }else{
            mRegister.setEnabled(false);
            mRegister.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn_bg_gray));
        }

    }

    private void startTime(){
        mCouldNotRepeatGetIndetifyCode = true;
        mGetIdentifyCode.setEnabled(false);
        mGetIdentifyCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn_bg_gray));
        mGetIdentifyCode.setText(String.format(mRepeatGetIndentifyCodeStr, 60));
        if (mTimer == null) {

            mTimer = new CountDownTimer(61000, 300) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mGetIdentifyCode.setText(String.format(mRepeatGetIndentifyCodeStr,
                            millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    mGetIdentifyCode.setEnabled(true);
                    mGetIdentifyCode.setText(R.string.get_identify_code);
                    mGetIdentifyCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn_bg));
                    mCouldNotRepeatGetIndetifyCode = false;
                }
            };
        }
        mTimer.cancel();
        mTimer.start();
    }

}
