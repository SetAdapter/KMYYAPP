package com.tencent.qcloud.timchat;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.bean.IMConfig;
import com.kmwlyy.core.util.ToastUtils;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupReceiveMessageOpt;
import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.TIMUser;
import com.tencent.TIMUserStatusListener;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.tencent.qcloud.timchat.ui.ChatActivity;
import com.tencent.qcloud.timchat.utils.Foreground;


/**
 * 全局Application
 */
public abstract class TimApplication extends BaseApplication {
    private static final String TAG = TimApplication.class.getSimpleName();
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Foreground.init(this);
        context = getApplicationContext();
        Log.d(TAG, "initIMsdk");
        //初始化imsdk
        TIMManager.getInstance().init(context.getApplicationContext());
        //禁止服务器自动代替上报已读
        TIMManager.getInstance().disableAutoReport();
        //设置日志级别
        TIMManager.getInstance().setLogLevel(TIMLogLevel.DEBUG);

        if (MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify) {
                        //消息被设置为需要提醒
                        notification.doNotify(getApplicationContext(), R.mipmap.ic_launcher);
                    }
                }
            });
        }
    }

    public static Context getContext() {
        return context;
    }

    public abstract SurfaceView addAgora(RelativeLayout layout, int uid);


    @Override
    public void logout() {
        super.logout();
        TIMManager.getInstance().logout();
    }

    public static void setListener(){
        //互踢下线逻辑
        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                ToastUtils.showShort(BaseApplication.instance,"账号在其它设备登录");
                BaseApplication.instance.logout();
            }

            @Override
            public void onUserSigExpired() {
                ToastUtils.showShort(BaseApplication.instance,"登录信息过期，请重新登录");
                BaseApplication.instance.logout();
            }
        });
    }

    public static void loginTimchat(TIMCallBack callBack) {
        IMConfig config = TimApplication.instance.getIMConfig();

        TIMUser user = new TIMUser();
        user.setIdentifier(config.getIdentifier());
        user.setAppIdAt3rd(String.valueOf(config.getSdkAppID()));
        user.setAccountType(String.valueOf(config.getAccountType()));

        //发起登录请求
        TIMManager.getInstance().login(config.getSdkAppID(), user, config.getUserSig(), callBack);
    }


    public static void enterTimchat(FragmentActivity context, String roomid, String roomname) {
        enterTimchat(context, roomid, roomname, false, 0);
    }

    public static void enterTimchat(final FragmentActivity context, final String roomid, final String roomname, final boolean show, final int showid) {
        ChatActivity.navToChat(context, roomid, roomname, TIMConversationType.Group, show, showid);
    }
}
