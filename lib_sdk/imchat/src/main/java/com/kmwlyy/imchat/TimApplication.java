package com.kmwlyy.imchat;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.bean.IMConfig;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.imchat.page.AddAgoraListener;
import com.kmwlyy.imchat.page.ChatActivity;
import com.kmwlyy.imchat.utils.Foreground;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversationType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import com.tencent.TIMUser;
import com.tencent.TIMUserStatusListener;


/**
 * 全局Application
 */
public class TimApplication {

    private static final String TAG = TimApplication.class.getSimpleName();
    private Context context;

    private static TimApplication timApplication;
    private AddAgoraListener addAgoraListener;

    public void setAddAgoraListener(AddAgoraListener addAgoraListener) {
        this.addAgoraListener = addAgoraListener;
    }

    public AddAgoraListener getAddAgoraListener() {
        return addAgoraListener;
    }
    public static TimApplication newInstance(Context context) {
        if (timApplication == null) {
            synchronized (TimApplication.class) {
                if (timApplication == null) {
                    timApplication = new TimApplication(context);
                }
            }
        }
        return timApplication;
    }

    public static TimApplication getInstance() {
        return timApplication;
    }

    private TimApplication(Context context) {
        this.context = context;
    }

    public void init(Application app){
        setForeground(app);
        onCreate();
    }

    public void setForeground(Application app){
        Foreground.init(app);
    }

    public void onCreate() {
        //TODO
//        Foreground.init(context.getApplicationContext());
        Log.d(TAG, "initIMsdk");
        //初始化imsdk
        TIMManager.getInstance().init(context.getApplicationContext());
        //禁止服务器自动代替上报已读
        TIMManager.getInstance().disableAutoReport();
        //设置日志级别
        TIMManager.getInstance().setLogLevel(TIMLogLevel.DEBUG);

//        if (MsfSdkUtils.isMainProcess(this)) {
//            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
//                @Override
//                public void handleNotification(TIMOfflinePushNotification notification) {
//                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify) {
//                        //消息被设置为需要提醒
//                        notification.doNotify(getApplicationContext(), R.mipmap.ic_launcher);
//                    }
//                }
//            });
//        }
    }

    public static Context getContext() {
        return timApplication.context;
    }

    public SurfaceView addAgora(RelativeLayout layout, int uid, int type) {
        return null;
    }

    public static void checkLogin() {
        if (BaseApplication.instance.hasUserToken()) {
            TimApplication.loginTimchat(new TIMCallBack() {
                @Override
                public void onError(int i, String s) {
                    //TODO
                    ToastUtils.showShort(BaseApplication.getContext(), BaseApplication.getContext().getString(R.string.usersig_expired));
                    BaseApplication.instance.logout();
                }

                @Override
                public void onSuccess() {
                    TimApplication.setListener();
                }
            });
            TimApplication.setNickName();
        }
    }

    public void logout() {
//        super.logout();
        //TODO
        TIMManager.getInstance().logout();
    }

    public static void setListener() {
        //互踢下线逻辑
        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                ToastUtils.showShort(BaseApplication.getContext(), BaseApplication.getContext().getString(R.string.force_offline));
                BaseApplication.instance.logout();
            }

            @Override
            public void onUserSigExpired() {
                ToastUtils.showShort(BaseApplication.getContext(), BaseApplication.getContext().getString(R.string.usersig_expired));
                BaseApplication.instance.logout();
            }
        });
    }

    public static void loginTimchat(TIMCallBack callBack) {
        IMConfig config = BaseApplication.getInstance().getIMConfig();

        TIMUser user = new TIMUser();
        user.setIdentifier(config.getIdentifier());
        user.setAppIdAt3rd(String.valueOf(config.getSdkAppID()));
        user.setAccountType(String.valueOf(config.getAccountType()));

        //发起登录请求
        TIMManager.getInstance().login(config.getSdkAppID(), user, config.getUserSig(), callBack);
    }

    public static void setNickName() {
        try {
            String nickName = BaseApplication.instance.getUserData().mUserCNName;
            if (!TextUtils.isEmpty(nickName)) {
                TIMFriendshipManager
                        .getInstance().setNickName(nickName, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Log.d(TAG, "set nick name fail");
                    }

                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "set nick name success");
                    }
                });
            }
        } catch (Exception e) {
        }
    }


    public static void enterTimchat(FragmentActivity context, String roomid, String roomname) {
        enterTimchat(context, roomid, roomname, true, false, 0);
    }

    public static void enterTimchat(FragmentActivity context, String roomid, String roomname, boolean isChat) {
        enterTimchat(context, roomid, roomname, isChat, false, 0);
    }

    public static void enterTimchat(FragmentActivity context, String roomid, String roomname, boolean show, int showid) {
        ChatActivity.navToChat(context, roomid, roomname, true, TIMConversationType.Group, show, showid);
    }

    public static void enterTimchat(FragmentActivity context, String roomid, String roomname, boolean isChat, boolean show, int showid) {
        ChatActivity.navToChat(context, roomid, roomname, isChat, TIMConversationType.Group, show, showid);
    }

    /**
     * 语音视频点击收起时，跳到聊天界面
     *
     * @param show    = true 显示小界面
     * @param isVedio = RoomActivity1.CALLING_TYPE_VIDEO 视频
     *                RoomActivity1.CALLING_TYPE_VOICE 语音
     * @param time    表示语音的开始时间
     */
    public static void enterTimchat(FragmentActivity context, String roomid, String roomname, boolean show, int showid, int isVedio, int time, String registerID, boolean cameraIsUse) {
        ChatActivity.navToChat(context, roomid, roomname, true, TIMConversationType.Group, show, showid, isVedio, time, registerID, cameraIsUse);
    }
}
