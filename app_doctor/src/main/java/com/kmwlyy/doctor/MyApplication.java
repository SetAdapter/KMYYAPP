package com.kmwlyy.doctor;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpFilter;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.IMConfig;
import com.kmwlyy.core.net.bean.UserData;
import com.kmwlyy.core.net.event.HttpIMConfig;
import com.kmwlyy.core.net.event.HttpUser;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.httpEvent.Http_logout_Event;
import com.kmwlyy.imchat.TimApplication;


import com.kmwlyy.imchat.page.AddAgoraListener;

import com.tencent.TIMCallBack;

import cn.jpush.android.api.JPushInterface;
import io.agora.core.AgoraApplication;

/**
 * Created by Administrator on 2016-8-15.
 */
public class
MyApplication extends MultiDexApplication implements AddAgoraListener, BaseApplication.JPushListener, BaseApplication.LogoutListener {

    public static final String TAG = MyApplication.class.getSimpleName();

    private static MyApplication mApplication;

    public static MyApplication getMyApplication(Context context) {
        if (mApplication == null) {
            synchronized (MyApplication.class) {
                if (mApplication == null) {
                    mApplication = getApplication(context);
                }
            }
        }
        return mApplication;
    }

    private Gson gson = new Gson();

    public static final MyApplication getApplication(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    public Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush

        BaseApplication.newInstance(this);
        BaseApplication.getInstance().setLogoutListener(this);
        BaseApplication.getInstance().setJPushListener(this);
        BaseApplication.getInstance().onCreate();
        BaseApplication.getInstance().setHttpFilter(getHttpFilter());

        TimApplication.newInstance(this);
        AgoraApplication.newInstance(this);

        TimApplication.getInstance().init(this);
        TimApplication.getInstance().setAddAgoraListener(this);

        BaseApplication.getInstance().updateAppToken();

//        LogUtils.i("Agora sdkversion", AgoraApplication.getInstance().getRtcEngine().getSdkVersion());


    }

    public String getJpushRegisterID() {
        return JPushInterface.getRegistrationID(this);
    }


    public void logout() {
        logout(false);
    }

    public void logout(boolean ignoreBaseLogout) {
        TimApplication.getInstance().logout();
        if (!ignoreBaseLogout) {
            BaseApplication.getInstance().logout();
        }

        MyUtils.reLogin(this);
        new HttpClient(this, new Http_logout_Event(new HttpListener<String>() {
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

            @Override
            public void onAppTokenExpire() {
                super.onAppTokenExpire();
                if (DebugUtils.debug) {
                    Log.d(TAG, "onAppTokenExpire");
                }
                onAppTokenExpired(mHttpClient);
            }

            @Override
            public void onNoneUserLogin() {
                super.onNoneUserLogin();
                if (DebugUtils.debug) {
                    Log.d(TAG, "onNoneUserLogin");
                }
                checkLogoutAndRestartHttpClient();
            }

            private void checkLogoutAndRestartHttpClient() {
                long time = SPUtils.getLoginTime(MyApplication.this);
                long current = System.currentTimeMillis();

                long duration = (long) 60 * 60 * 24 * 30 * 1000;
                if ((current - time) > duration) {
                    logout();
                } else {
                    SPUtils.LoginInfo loginInfo = SPUtils.getLoginInfo(MyApplication.this);
                    new HttpClient(MyApplication.this, new HttpUser.Login(
                            loginInfo.userName
                            , loginInfo.pwd
                            , HttpUser.DOCTOR
                            , getJpushRegisterID()
                            , new HttpListener<UserData>() {
                        @Override
                        public void onError(int code, String msg) {
                            LogUtils.d(TAG, "login error , code : " + code + " , msg : " + msg);
                        }

                        @Override
                        public void onSuccess(final UserData userData) {
                            BaseApplication.instance.setUserData(userData);

                            SPUtils.saveLoginTime(MyApplication.this, System.currentTimeMillis());

                            new HttpClient(MyApplication.this, new HttpIMConfig(new HttpListener<IMConfig>() {
                                @Override
                                public void onError(int code, String msg) {
                                    LogUtils.d(TAG, "updateIMConfig onError!");
//                                    BaseApplication.instance.setUserData(new UserData());
                                }

                                @Override
                                public void onSuccess(IMConfig imConfig) {
                                    LogUtils.d(TAG, "updateIMConfig onSuccess : ");

                                    if (mHttpClient != null) {
                                        // refresh http client
                                        mHttpClient.start();
                                    }

                                    BaseApplication.instance.setIMConfig(imConfig);
                                    TimApplication.loginTimchat(new TIMCallBack() {
                                        @Override
                                        public void onError(int i, String s) {
//                                            BaseApplication.instance.setUserData(new UserData());
//                                            BaseApplication.instance.setIMConfig(new IMConfig());
                                        }

                                        @Override
                                        public void onSuccess() {
                                        }
                                    });
                                }

                            })).start();
                        }
                    })).start();
                }
            }
        };
    }

    public SurfaceView addAgora(RelativeLayout layout, int uid, int type) {
        return AgoraApplication.getInstance().addSurfaceView(layout, uid, type);
    }

    public void onAppTokenExpired(HttpClient mHttpClient) {
        if (DebugUtils.debug) {
            Log.d(TAG, "request app token");
        }

        if (BaseApplication.getInstance().updateAppToken()) {

            if (mHttpClient != null && !mHttpClient.overRetryCount()) {
                // refresh http client
                mHttpClient.hitRetryCount();
                mHttpClient.start();
            }
        }
    }

    public void onUserExpired() {
        if (DebugUtils.debug) {
            Log.d(TAG, "re login");
        }
        logout();
    }

    @Override
    public void onLogout() {
        logout(true);
    }
}
