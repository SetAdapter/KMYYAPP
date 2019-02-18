package com.kmwlyy.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.IMConfig;
import com.kmwlyy.core.net.bean.SignatureBean;
import com.kmwlyy.core.net.bean.UserData;
import com.kmwlyy.core.net.event.GetMySignature;
import com.kmwlyy.core.net.event.HttpIMConfig;
import com.kmwlyy.core.net.event.HttpUser;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.imchat.TimApplication;
import com.tencent.TIMCallBack;
import com.winson.ui.widget.CleanableEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Winson on 2016/8/6.
 */
public class LoginActivity extends NormalActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();

    public static final String APP_TYPE = "APP_TYPE";
    public static final int DOCTOR = HttpUser.DOCTOR;
    public static final int PATIENT = HttpUser.PATIENT;

    @BindView(R2.id.user_name)
    CleanableEditText mUserName;
    @BindView(R2.id.password)
    CleanableEditText mPassword;
    @BindView(R2.id.register)
    TextView mRegister;
    @BindView(R2.id.iv_icon)
    ImageView iv_icon;
    @BindView(R2.id.login)
    Button mLogin;
    @BindView(R2.id.forget_password)
    TextView mForgetPassword;
    @BindView(R2.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R2.id.navigation_icon)
    ImageView mNavigationIcon;
    @BindView(R2.id.navigation_btn)
    View mNavigationBtn;
    @BindView(R2.id.cancel_btn)
    View mCancelBtn;

    private int mAppType;
    private ProgressDialog mLoginDialog;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mAppType = getIntent().getIntExtra(APP_TYPE, PATIENT);
        if (mAppType == HttpUser.DOCTOR) {
            mNavigationBtn.setVisibility(View.INVISIBLE);
        }

        mSkipFinsih = true;
//        mNavigationBtn.setVisibility(View.INVISIBLE);
        mNavigationIcon.setBackgroundResource(R.drawable.back_white);
        mToolbarTitle.setText(R.string.login);
        mToolbarTitle.setTextColor(getResources().getColor(R.color.white));
//        mRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra(APP_TYPE, mAppType);
                startActivity(intent);
            }
        });

        findViewById(R.id.forget_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra(APP_TYPE, mAppType);
                intent.putExtra(RegisterActivity.FORGET_PWD, true);
                startActivity(intent);
            }
        });


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRegisterSuccess(EventLoginApi.Login registerSuccess) {
        finish();
    }

    private void login() {

        final String phone = mUserName.getText().toString();
//        if(TextUtils.isEmpty(phone)){
//            ToastUtils.show(this, R.string.please_input_true_phone, Toast.LENGTH_SHORT);
//            return;
//        }
        if (!CommonUtils.checkUserName(phone)) {
            ToastUtils.show(this, R.string.please_input_true_user_name, Toast.LENGTH_SHORT);
            ToastUtils.show(this, R.string.true_user_name_format, Toast.LENGTH_SHORT);
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

        showLoginDialog();

        new HttpClient(LoginActivity.this, new HttpUser.Login(phone, pwd, mAppType, BaseApplication.instance.getJpushRegisterID(), new HttpListener<UserData>() {
            @Override
            public void onError(int code, String msg) {
                LogUtils.d(TAG, "login error , code : " + code + " , msg : " + msg);
                loginFailed(msg);
            }

            @Override
            public void onSuccess(final UserData userData) {
                BaseApplication.instance.setUserData(userData);
                SPUtils.saveLoginInfo(LoginActivity.this, phone, pwd);
                SPUtils.saveLoginTime(LoginActivity.this, System.currentTimeMillis());
                //获取签约状态
                GetMySignature getMySignature = new GetMySignature(new HttpListener<SignatureBean>() {
                    @Override
                    public void onError(int code, String msg) {
                        // ToastUtils.showShort();
                    }

                    @Override
                    public void onSuccess(SignatureBean signatureBean) {
                        if (signatureBean != null){
                            if (!TextUtils.isEmpty(signatureBean.mFDGroupID)) {
                                BaseApplication.getInstance().setSignatureData(signatureBean);
                            }else{
                                BaseApplication.getInstance().setSignatureData(new SignatureBean());
                            }
                        }else{
                            BaseApplication.getInstance().setSignatureData(new SignatureBean());
                        }
                    }
                });
                new HttpClient(LoginActivity.this,getMySignature,HttpClient.FAMILY_URL,BaseApplication.getInstance().getHttpFilter()).start();
                new HttpClient(LoginActivity.this, new HttpIMConfig(new HttpListener<IMConfig>() {
                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.d(TAG, "updateIMConfig onError!");
                        BaseApplication.instance.setUserData(new UserData());
                        loginFailed(msg);
                    }

                    @Override
                    public void onSuccess(IMConfig imConfig) {
                        LogUtils.d(TAG, "updateIMConfig onSuccess : ");
                        BaseApplication.instance.setIMConfig(imConfig);
                        TimApplication.loginTimchat(new TIMCallBack() {
                            @Override
                            public void onError(int i, String s) {
                                BaseApplication.instance.setUserData(new UserData());
                                BaseApplication.instance.setIMConfig(new IMConfig());
                                if (i == 6208) {
                                    s = getResources().getString(R.string.on_login_please_relogin);
                                }
                                loginFailed(s);
                            }

                            @Override
                            public void onSuccess() {
                                SPUtils.put(LoginActivity.this, SPUtils.JPUSH_STATE, Boolean.valueOf(true));
                                loginSuccess(userData.mCheckState);
                            }
                        });
                    }
                }), BaseApplication.getInstance().getHttpFilter()).start();
            }
        }), BaseApplication.getInstance().getHttpFilter()).start();
    }

    private void loginFailed(String msg) {
        ToastUtils.show(LoginActivity.this, msg, Toast.LENGTH_SHORT);
        dismissLoginDialog();
    }

    private void loginSuccess(int state) {
        EventLoginApi.Login mLogin = new EventLoginApi.Login();
        mLogin.state = state;
        EventBus.getDefault().post(mLogin);
        dismissLoginDialog();
        finish();
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
}
