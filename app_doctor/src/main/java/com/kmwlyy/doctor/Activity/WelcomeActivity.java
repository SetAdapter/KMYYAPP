package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.IMConfig;
import com.kmwlyy.core.net.bean.UserData;
import com.kmwlyy.core.net.event.HttpIMConfig;
import com.kmwlyy.core.net.event.HttpUser;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.core.util.UpdateManager;
import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.R;
import com.kmwlyy.imchat.TimApplication;
import com.kmwlyy.login.EventLoginApi;
import com.tencent.TIMCallBack;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.jpush.android.api.JPushInterface;
import com.networkbench.agent.impl.NBSAppAgent;

public class WelcomeActivity extends BaseActivity {
    public static final String TAG = "WelcomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        EventBus.getDefault().register(this);
        initTinYun();
        if (getIntent().getStringExtra("msg") == null) {
            //检查更新
            UpdateManager.getUpdateManager().CheckUpdate(mContext, UpdateManager.DOCTOR, new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enterApp(-1);
                        }
                    }, 2000);
                }
            });
        } else {
            enterApp(-1);
        }
    }

    public void initTinYun(){
        NBSAppAgent nbsAppAgent = null;
        nbsAppAgent = NBSAppAgent.setLicenseKey(BaseApplication.getInstance().getDoctorNBSKey())
                .setRedirectHost("218.17.23.72:8081").setHttpEnabled(true)
                .withLocationServiceEnabled(true).setDefaultCert(false)
                .setUserIdentifier(BaseApplication.getInstance().getUserData().mMobile);
        nbsAppAgent.start(this.getApplicationContext());
    }
    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 进入APP
     */
    protected void enterApp(int state) {
        SPUtils.LoginInfo loginInfo = SPUtils.getLoginInfo(mContext);
        if(loginInfo == null || loginInfo.pwd == null || loginInfo.userName == null){
            //本地没有账号密码！ 重新登录
            goToLogin();
        }
        else{
            long time = SPUtils.getLoginTime(mContext);
            long current = System.currentTimeMillis();

            long duration = (long)60 * 60 * 24 * 30 * 1000;
            if ((current - time) > duration) {
                //有账号密码 但是过期了！ 重新登录
                goToLogin();
            }else{
                //有账号密码，没有过期！ 后台登录取token 和医生的审核信息
                loginInBackground();
            }
        }

    }

    /**
     * 登录成功
     * @param login
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogin(EventLoginApi.Login login) {
        int mState = login.state;
        goToMain(mState);
    }

    /**
     * 登录界面点击返回 回调
     * @param back
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBack(EventLoginApi.onBack back) {
        finish();
    }


    /*
     * 跳转到登录页面
     */
    private void goToLogin() {
        Intent intent = new Intent(this, com.kmwlyy.login.LoginActivity.class);
        intent.putExtra(com.kmwlyy.login.LoginActivity.APP_TYPE, com.kmwlyy.login.LoginActivity.DOCTOR);
        startActivity(intent);
    }

    /*
     * 跳转到主界面
     */
    private void goToMain(int mState) {
        if (mState == 0){
            //未认证，进入认证页面
            DoctorCertificationActivity.startDoctorCertificationActivity(WelcomeActivity.this, false);
        }
//        else if(mState == 1){
//            //已通过，不做任何操作
//        }
        else if(mState == 2){
            //未通过，进入失败页面
            CertificationResultActivity.startCertificationResultActivity(WelcomeActivity.this, 2);
        }else if(mState == 3){
            //认证中，进入申请中页面，不能修改
            CertificationResultActivity.startCertificationResultActivity(WelcomeActivity.this, 3);
        }else if(mState == 4){
            //等待认证，进入等待认证中页面，可以修改
            CertificationResultActivity.startCertificationResultActivity(WelcomeActivity.this, 4);
        }else{
            Intent intent = new Intent(mContext, MainActivity.class);
            startActivity(intent);
        }

        finish();
    }

    public void loginInBackground(){
        SPUtils.LoginInfo loginInfo = SPUtils.getLoginInfo(mContext);
        new HttpClient(mContext, new HttpUser.Login(
                loginInfo.userName
                , loginInfo.pwd
                , HttpUser.DOCTOR
                , BaseApplication.instance.getJpushRegisterID()
                , new HttpListener<UserData>() {
            @Override
            public void onError(int code, String msg) {
                LogUtils.d(TAG, "login error , code : " + code + " , msg : " + msg);
                showErrToast();
            }

            @Override
            public void onSuccess(final UserData userData) {
                BaseApplication.instance.setUserData(userData);

                SPUtils.saveLoginTime(mContext, System.currentTimeMillis());

                new HttpClient(mContext, new HttpIMConfig(new HttpListener<IMConfig>() {
                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.d(TAG, "updateIMConfig onError!");
                        BaseApplication.instance.setUserData(new UserData());
                        showErrToast();
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
                                showErrToast();
                            }

                            @Override
                            public void onSuccess() {
                                goToMain(userData.mCheckState);
                            }
                        });
                    }

                })).start();
            }
        })).start();
    }

    public void showErrToast(){
        ToastUtils.showShort(mContext,"获取登录信息失败，请重新登录！");
        SPUtils.logout(mContext);
        goToLogin();
    }
}
