package io.agora.core;

import io.agora.rtc.IRtcEngineEventHandler;

/**
 * Created by apple on 15/9/10.
 */
public class BaseMessageHandler extends IRtcEngineEventHandler {

    private BaseHandlerActivity mHandlerActivity;

    public void setActivity(BaseHandlerActivity activity) {
        this.mHandlerActivity = activity;
    }

    public BaseHandlerActivity getActivity(){
        return mHandlerActivity;
    }

    //显示房间内其他用户的视频
    @Override
    public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {

        BaseHandlerActivity activity = getActivity();
        if (activity != null) {
            activity.onFirstRemoteVideoDecoded(uid, width, height, elapsed);
        }
    }

    //用户进入
    @Override
    public void onUserJoined(int uid, int elapsed){

        BaseHandlerActivity activity = getActivity();

        if (activity != null) {
            activity.onUserJoined(uid, elapsed);
        }
    }

    //用户退出
    @Override
    public void onUserOffline(int uid, int reason) {

        BaseHandlerActivity activity = getActivity();

        if (activity != null) {
            activity.onUserOffline(uid,reason);
        }
    }

    //监听其他用户是否关闭视频
    @Override
    public void onUserMuteVideo(int uid,boolean muted){
        BaseHandlerActivity activity = getActivity();
        if (activity != null) {
            activity.onUserMuteVideo(uid, muted);
        }
    }

    //监听其他用户是否关闭音频
    @Override
    public void onUserMuteAudio(int uid, boolean muted) {
        super.onUserMuteAudio(uid, muted);
    }

    //更新聊天数据
    @Override
    public void onRtcStats(RtcStats stats){
        BaseHandlerActivity activity = getActivity();
        if (activity != null) {
            activity.onUpdateSessionStats(stats);
        }
    }

    //离开房间
    @Override
    public void onLeaveChannel(RtcStats stats) {
        BaseHandlerActivity activity = getActivity();
        if (activity != null) {
            activity.onLeaveChannel(stats);
        }
    }

    //发生错误
    @Override
    public void onError(int err) {
        BaseHandlerActivity activity = getActivity();

        if (activity != null) {
            activity.onError(err);
        }
    }

    //监控网络状态
    @Override
    public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
        BaseHandlerActivity activity = getActivity();
        if (activity != null) {
            activity.onNetworkQuality(rxQuality);
//            activity.onLastmileQuality((int)(1+Math.random()*(6))); //从1到6的int型随数); 测试一下信号图标
        }
    }
}
