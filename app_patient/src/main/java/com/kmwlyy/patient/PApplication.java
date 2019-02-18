package com.kmwlyy.patient;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpFilter;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UserData;
import com.kmwlyy.core.net.event.HttpUser;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.imchat.TimApplication;
import com.kmwlyy.imchat.page.AddAgoraListener;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.registry.bean.Contants;
import com.winson.ui.widget.ToastMananger;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import io.agora.core.AgoraApplication;

/**
 * Created by Winson on 2016/8/6.
 */
public class PApplication extends MultiDexApplication implements AddAgoraListener, BaseApplication.JPushListener {

    public static final String TAG = PApplication.class.getSimpleName();

    private Timer mRefreshAppTokenTimer;

    private static PApplication mPApplication;

    public static PApplication getPApplication(Context context) {
        if (mPApplication == null) {
            synchronized (PApplication.class) {
                if (mPApplication == null) {
                    mPApplication = getApplication(context);
                }
            }
        }
        return mPApplication;
    }

    public static final PApplication getApplication(Context context) {
        return (PApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        // product
        //HttpClient.KMYY_URL = "http://api.kmwlyy.com/";

        BaseApplication.newInstance(this);
        BaseApplication.getInstance().setJPushListener(this);
        BaseApplication.getInstance().onCreate();
        BaseApplication.getInstance().setHttpFilter(getHttpFilter());

        TimApplication.newInstance(this);
        AgoraApplication.newInstance(this);

        TimApplication.getInstance().init(this);
        TimApplication.getInstance().setAddAgoraListener(this);

        BaseApplication.getInstance().updateAppToken();
        Contants.initImageLoader(this);
    }

    public String getJpushRegisterID() {
        return JPushInterface.getRegistrationID(this);
    }

    public void logout() {
        logout(true);
        SPUtils.logout(this);
    }

    public void logout(boolean active) {

        TimApplication.getInstance().logout();
        BaseApplication.getInstance().logout();

        //跳转页面
        EventApi.Logout logout = new EventApi.Logout();
        logout.active = active;
        EventBus.getDefault().post(logout);
        new HttpClient(BaseApplication.getContext(), new HttpUser.Logout(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                LogUtils.d(TAG, "logout error , code : " + code + " , msg : " + msg);
            }

            @Override
            public void onSuccess(String data) {
                LogUtils.d(TAG, "logout success");
            }
        }));
    }

    public HttpFilter getHttpFilter() {
        return new HttpFilter() {

            @Override
            public void onTokenExpire() {
                super.onTokenExpire();
                if (DebugUtils.debug) {
                    Log.d(TAG, "onTokenExpire");
                }
                checkLogoutAndRestartHttpClient();
            }

            private void checkLogoutAndRestartHttpClient() {
                // check time and refresh user token
                long time = SPUtils.getLoginTime(PApplication.this);
                long current = System.currentTimeMillis();

                long duration = 60 * 60 * 24 * 30 * 1000l;
                if ((current - time) > duration) {
                    logout();
                } else {
                    SPUtils.LoginInfo loginInfo = SPUtils.getLoginInfo(PApplication.this);
                    if (loginInfo == null || loginInfo.userName == null) {
                        logout();
                        return;
                    }
                    new HttpClient(PApplication.this, new HttpUser.Login(
                            loginInfo.userName
                            , loginInfo.pwd
                            , HttpUser.PATIENT
                            , BaseApplication.instance.getJpushRegisterID()
                            , new HttpListener<UserData>() {
                        @Override
                        public void onError(int code, String msg) {
                            LogUtils.d(TAG, "login error , code : " + code + " , msg : " + msg);
                        }

                        @Override
                        public void onSuccess(final UserData userData) {
                            BaseApplication.instance.setUserData(userData);

                            SPUtils.saveLoginTime(PApplication.this, System.currentTimeMillis());

                            if (mHttpClient != null) {
                                // refresh http client
                                mHttpClient.start();
                            }

//                            new HttpClient(PApplication.this, new HttpIMConfig(new HttpListener<IMConfig>() {
//                                @Override
//                                public void onError(int code, String msg) {
//                                    LogUtils.d(TAG, "updateIMConfig onError!");
//                                    BaseApplication.instance.setUserData(new UserData());
//                                }
//
//                                @Override
//                                public void onSuccess(IMConfig imConfig) {
//                                    LogUtils.d(TAG, "updateIMConfig onSuccess : ");
//
//                                    BaseApplication.instance.setIMConfig(imConfig);
//                                    TimApplication.loginTimchat(new TIMCallBack() {
//                                        @Override
//                                        public void onError(int i, String s) {
//                                            BaseApplication.instance.setUserData(new UserData());
//                                            BaseApplication.instance.setIMConfig(new IMConfig());
//                                        }
//
//                                        @Override
//                                        public void onSuccess() {
//                                        }
//                                    });
//                                }
//
//                            })).start();
                        }
                    })).start();
                }
            }

            @Override
            public void onAppTokenExpire() {
                super.onAppTokenExpire();
                if (DebugUtils.debug) {
                    Log.d(TAG, "onAppTokenExpire");
                }
                ToastMananger.showToast(PApplication.this, R.string.system_error_please_try_again, Toast.LENGTH_SHORT);
                EventApi.AppTokenExpired expired = new EventApi.AppTokenExpired();
                expired.mOldAppToken = BaseApplication.getInstance().getAppToken();
                onAppTokenExpired(expired, mHttpClient);
            }

            @Override
            public void onNoneUserLogin() {
                super.onNoneUserLogin();
                if (DebugUtils.debug) {
                    Log.d(TAG, "onNoneUserLogin");
                }
                checkLogoutAndRestartHttpClient();
            }
        };
    }

    public SurfaceView addAgora(RelativeLayout layout, int uid, int type) {
        return AgoraApplication.getInstance().addSurfaceView(layout, uid, type);
    }

    private void restartRefreshAppTokenTimer() {
        if (mRefreshAppTokenTimer != null) {
            mRefreshAppTokenTimer.cancel();
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                BaseApplication.getInstance().updateAppToken();
            }
        };

        mRefreshAppTokenTimer = new Timer();
        mRefreshAppTokenTimer.schedule(task, 30 * 1000 * 60, 30 * 1000 * 60);
    }

    public void onAppTokenExpired(EventApi.AppTokenExpired expired, HttpClient mHttpClient) {
        if (DebugUtils.debug) {
            Log.d(TAG, "onAppTokenExpired old app token : " + expired.mOldAppToken);
        }
        // if old app token is the same as the application app token
        //    that's mean token not refreshed
        // else refreshed and it not need to refresh again
        if (expired.mOldAppToken != null && !expired.mOldAppToken.equals(BaseApplication.getInstance().getAppToken())) {
            if (DebugUtils.debug) {
                Log.d(TAG, "app token has refreshed!");
            }
            return;
        }
        if (BaseApplication.getInstance().updateAppToken()) {
            restartRefreshAppTokenTimer();

            if (mHttpClient != null) {
                // refresh http client
                mHttpClient.start();
            }
        }
    }

}
