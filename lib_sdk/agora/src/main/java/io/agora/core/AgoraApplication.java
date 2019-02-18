package io.agora.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.event.HttpConsult;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.imchat.TimApplication;
import com.kmwlyy.imchat.page.ChatActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * Created by apple on 15/9/9.
 */
public class AgoraApplication {

    private Context context;

    private AgoraApplication(Context context) {
        this.context = context;
    }

    private static AgoraApplication agoraApplication;

    public static AgoraApplication newInstance(Context context) {
        if (agoraApplication == null) {
            synchronized (AgoraApplication.class) {
                if (agoraApplication == null) {
                    agoraApplication = new AgoraApplication(context);
                }
            }
        }
        return agoraApplication;
    }

    public static AgoraApplication getInstance() {
        return agoraApplication;
    }

    private RtcEngine rtcEngine;

    public RtcEngine getRtcEngine() {
        return rtcEngine;
    }

    public void setRtcEngine(String vendorKey, IRtcEngineEventHandler handler) {
        // setup engine
        try {
            rtcEngine = RtcEngine.create(BaseApplication.getContext(), vendorKey, handler);
            LogUtils.i("Agora SDK Version: ",rtcEngine.getSdkVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
        rtcEngine.setLogFile(BaseApplication.getContext().getExternalFilesDir(null).toString() + "/agorasdk.log");
    }

    public void setRtcEngine(BaseHandlerActivity activity, String vendorKey) {
        // setup messageHandler
        BaseMessageHandler messageHandler = new BaseMessageHandler();
        messageHandler.setActivity(activity);

        // setup engine
        try {
            rtcEngine = RtcEngine.create(BaseApplication.getContext(), vendorKey, messageHandler);
            LogUtils.i("Agora SDK Version: ",rtcEngine.getSdkVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
        rtcEngine.setLogFile(BaseApplication.getContext().getExternalFilesDir(null).toString() + "/agorasdk.log");
    }

    /***************************************************/
    public static int getSize(int size) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, BaseApplication.getContext().getResources().getDisplayMetrics());
    }

    /**
     * @param layout
     * @param uid
     * @param type   语音问诊 - 聊天界面显示默认图片
     *               视频问诊 - 显示小视频窗口
     *               * @return
     */
    public static SurfaceView addSurfaceView(final RelativeLayout layout, final int uid, final int type) {
        RelativeLayout.LayoutParams params;
        final SurfaceView mLocalView;
        final RtcEngine rtcEngine = AgoraApplication.getInstance().getRtcEngine();
        if (type == RoomActivity1.CALLING_TYPE_VIDEO_FULL) {//增加一个全屏模式的SurfaceView
            params = new RelativeLayout.LayoutParams(layout.getWidth(), layout.getHeight());
            mLocalView = rtcEngine.CreateRendererView(BaseApplication.getContext());
        } else {//增加一个可移动的SurfaceView
            params = new RelativeLayout.LayoutParams(getSize(100), getSize(150));
            params.setMargins(0, 150, 0, 0);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            mLocalView = rtcEngine.CreateRendererView(BaseApplication.getContext());

            mLocalView.setOnTouchListener(new View.OnTouchListener() {
                private int startx;
                private int starty;
                private int startx_down;
                private int starty_down;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int screenHeight = layout.getMeasuredHeight();
                    int screenWidth = layout.getMeasuredWidth();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:// 获取手指第一次接触屏幕
                            startx = (int) event.getRawX();
                            starty = (int) event.getRawY();
                            startx_down = (int) event.getRawX();
                            starty_down = (int) event.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动对应的事件
                            int dx = (int) event.getRawX() - startx;
                            int dy = (int) event.getRawY() - starty;

                            int left = v.getLeft() + dx;
                            int top = v.getTop() + dy;
                            int right = v.getRight() + dx;
                            int bottom = v.getBottom() + dy;
                            if (left < 0) {
                                left = 0;
                                right = left + v.getWidth();
                            }
                            if (right > screenWidth) {
                                right = screenWidth;
                                left = right - v.getWidth();
                            }
                            if (top < 0) {
                                top = 0;
                                bottom = top + v.getHeight();
                            }
                            if (bottom > screenHeight) {
                                bottom = screenHeight;
                                top = bottom - v.getHeight();
                            }
                            v.layout(left, top, right, bottom);
                            Log.i("LocalViewContainer", "screen:" + screenHeight + ", " + screenWidth);
                            Log.i("LocalViewContainer", "position:" + left + ", " + top + ", " + right + ", " + bottom);
                            startx = (int) event.getRawX();
                            starty = (int) event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:// 手指离开屏幕对应事件
                            Log.i("LocalViewContainer", "手指离开屏幕");
                            //位移太小，当作点击事件。跳回视频界面
                            if ((Math.abs(startx_down - (int) event.getRawX()) < 10) && (Math.abs(starty_down - (int) event.getRawY()) < 10)) {
                                if (type == ChatActivity.CALLING_TYPE_VIDEO_CHAT) {
                                    EventBus.getDefault().post(new ChatActivity.ClickWindow());//通知聊天界面，返回视频界面
                                } else {
                                    EventBus.getDefault().post(new RoomActivity1.ChangeWindow());//通知视频界面，切换大小屏幕
                                }
                            }
                            break;
                    }
                    return true;
                }
            });
        }
        mLocalView.setZOrderMediaOverlay(true);
        layout.addView(mLocalView, 0, params);

        /**
         * AgoraRtc_Render_Hidden(1): 如果视频尺寸与显示视窗尺寸不一致，则视频流会按照显示视窗的比例进行周边裁剪或图像拉伸后填满视窗。
         AgoraRtc_Render_Fit(2): 如果视频尺寸与显示视窗尺寸不一致，在保持长宽比的前提下，将视频进行缩放后填满视窗。
         AgoraRtc_Render_Adaptive(3)：如果自己和对方都是竖屏，或者如果自己和对方都是横屏，使用AgoraRtc_Render_Hidden；如果对方和自己一个竖屏一个横屏，则使用AgoraRtc_Render_Fit。
         */
        if (uid == -1) {
            rtcEngine.setupLocalVideo(new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0));//uid = -1 加载本地视频流
        } else {
            int successCode = rtcEngine.setupRemoteVideo(new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
            if (successCode < 0) {
                Log.i("RoomActivity1", "setupRemoteVideo failed return:" + successCode);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rtcEngine.setupRemoteVideo(new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
                        mLocalView.invalidate();
                    }
                }, 500);
            }
        }
        return mLocalView;
    }

    private static ProgressDialog mLoadingDialog;

    public static void loginAgora(final Context context, final ConsultBean bean) {
        mLoadingDialog = ProgressDialog.show(context, "", context.getString(R.string.login_agora));
        bean.mRemoteID = -1;
        bean.mLocalID = Integer.valueOf(BaseApplication.instance.getIMConfig().getIdentifier());

        if (bean.mUserType == RoomActivity1.USER_TYPE_DOCTOR) {
            HttpConsult.getChannelState event = new HttpConsult.getChannelState(bean.mRoomID);
            event.setHttpListener(new HttpListener<String>() {
                @Override
                public void onError(int code, String msg) {
                    ToastUtils.showShort(context, msg);
                    if (mLoadingDialog != null) {
                        mLoadingDialog.dismiss();
                    }
                }

                @Override
                public void onSuccess(String s) {
                    if (Integer.valueOf(s) == 1) { //房间状态：0=未就诊,1=候诊中,2=就诊中,3=已就诊,4=呼叫中,5=离开中

//                        try{
//                            Thread.sleep(1000);
//                        }catch (InterruptedException e) {}

                        loginAgoraEx(context, bean);
                    } else {
                        ToastUtils.showShort(context, context.getString(R.string.login_agora_error));
                        if (mLoadingDialog != null) {
                            mLoadingDialog.dismiss();
                        }
                    }
                }
            });
            new HttpClient(context, event).start();
        } else {
            loginAgoraEx(context, bean);
        }
    }

    public static void loginAgoraEx(final Context context, final ConsultBean bean) {
        HttpConsult.getAgoraConfig event = new HttpConsult.getAgoraConfig(bean.mRoomID, bean.mLocalID + "");
        event.setHttpListener(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(context, msg);
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
            }

            @Override
            public void onSuccess(String s) {
                if (mLoadingDialog != null) {
                    mLoadingDialog.dismiss();
                }
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    bean.mVendorKey = jsonObject.getString("MediaChannelKey");
                    bean.mAppID = jsonObject.getString("AppID");
                    if (!TextUtils.isEmpty(bean.mVendorKey) && !TextUtils.isEmpty(bean.mAppID)) {
                        //bean.mVendorKey = "25b77c09d6384632b4215f2d1355172a";
                        Intent intent = new Intent(context, RoomActivity1.class);
                        intent.putExtra(RoomActivity1.EXTRA_AGORA_BEAN, bean);
                        context.startActivity(intent);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ToastUtils.showShort(context, context.getString(R.string.login_agora_failed));
            }
        });
        new HttpClient(context, event).start();
    }


    public static void loginAgoraMeeting(final Context context, final MeetingRoomBean bean) {
        bean.mRemoteID = -1;
        bean.mLocalID = Integer.valueOf(BaseApplication.instance.getIMConfig().getIdentifier());

        if (bean.doctors == null || bean.doctors.size() <= 0) {
            ToastUtils.showShort(context, R.string.meeting_doctor_error);
            Log.d("Agora", "meeting doctor info error");
            return;
        }

        HttpConsult.getAgoraConfig event = new HttpConsult.getAgoraConfig(bean.mRoomID, bean.mLocalID + "");
        event.setHttpListener(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(context, msg);
            }

            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    bean.mVendorKey = jsonObject.getString("MediaChannelKey");
                    if (!TextUtils.isEmpty(bean.mVendorKey)) {
                        //bean.mVendorKey = "25b77c09d6384632b4215f2d1355172a";
                        Intent intent = new Intent(context, MeetingRoomActivity.class);
                        intent.putExtra(MeetingRoomActivity.EXTRA_AGORA_BEAN, bean);
                        context.startActivity(intent);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ToastUtils.showShort(context, context.getString(R.string.login_agora_failed));
            }
        });
        new HttpClient(context, event).start();
    }
}
