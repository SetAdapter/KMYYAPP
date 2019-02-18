package io.agora.core;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.event.HttpConsult;
import com.kmwlyy.core.util.ImageUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.NetworkUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.imchat.TimApplication;
import com.kmwlyy.imchat.message.CustomMessage;
import com.kmwlyy.imchat.message.Message;
import com.kmwlyy.imchat.message.MessageFactory;
import com.kmwlyy.imchat.model.MessageEvent;
import com.kmwlyy.imchat.page.ChatActivity;
import com.networkbench.agent.impl.NBSAppAgent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.TIMMessage;
import com.winson.ui.widget.AlterDialogView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import doctor.kmwlyy.com.recipe.RecipeActivity;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

import static io.agora.rtc.Constants.USER_OFFLINE_DROPPED;

/**
 * * some refs:
 * 1. http://stackoverflow.com/questions/6026625/layout-design-surfaceview-doesnt-display
 * 2. http://stackoverflow.com/questions/1096618/android-surfaceview-scrolling/2216788#2216788
 * <p/>
 * Created by apple on 15/9/9.
 */
public class RoomActivity1 extends BaseHandlerActivity implements Observer {
    private static final String TAG = "RoomActivity1";
    public final static int CALLING_TYPE_VIDEO = 0x100; //100
    public final static int CALLING_TYPE_VOICE = 0x101; //101
    public final static int CALLING_TYPE_VIDEO_FULL = 102; //视频全屏模式

    private static final int PERMISSION_REQ_ID_RECORD_AUDIO = 22;
    private static final int PERMISSION_REQ_ID_CAMERA = PERMISSION_REQ_ID_RECORD_AUDIO + 1;

    public final static int USER_TYPE_DOCTOR = 0x102;
    public final static int USER_TYPE_PATIENT = 0x103;

    public final static String EXTRA_AGORA_BEAN = "EXTRA_AGORA_BEAN";

    private Context mContex = RoomActivity1.this;

    private TextView appNotification;
    private TextView tv_time;//左上角的时间显示
    private TextView tv_name;//左上角的名字显示
    private TextView mDuration;//时间

    private CheckBox mMuterEnabler;
    private CheckBox mSpeakerEnabler;
    private CheckBox mCameraEnabler;
    private ImageView mCameraSwitcher;

    private SurfaceView mLocalView;//本地图像
    private RelativeLayout mLocalContainer;
    private SurfaceView mRemoteView;//远程图像
    private RelativeLayout mRemoteContainer;
    private LinearLayout mUserInfoContainer;//视频时，左上角的人名、时间信息

    private int time = 0;

    private RtcEngine rtcEngine;
    private ConsultBean mConsultBean;

    private AlertDialog alertDialog;
    private MediaPlayer mMediaPlayer;

    private boolean isCalling;//是否正在通话
    private boolean display = true; //true 大屏幕是远程视频，小屏幕是本地视频   false 大屏幕是本地视频，小屏幕是远程视频
    private boolean showLocal = true; //是否显示本地视频 true显示  false不显示
    private boolean showRemote = true; //是否显示远程视频  true显示  false不显示
    private boolean isFisrtWait = true; //第一次等待=true  其它时候等待=false 表示医生取消呼叫，患者接着等
    private boolean isFisrtDoing = true; //第一次接通=true  其它时候接通=false 防止云通信第二次发送接通推送产生问题
    private boolean isFisrtNone = true; //第一次患者挂断=true  其它时候接通=false 防止云通信第二次发送挂断推送产生问题
    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() { // Tutorial Step 1
        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            //远端视频接收解码回调
            //收到第一帧远程视频流并解码成功时，触发此调用。应用程序可以在此回调中设置该用户的view。
            Log.i(TAG,"onFirstRemoteVideoDecoded: uid: " + uid + ", width: " + width + ", height: " + height);
            isCalling = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    NBSAppAgent.onEvent("诊室-视话问诊-接通视频通话");
                    mConsultBean.mRemoteID = uid;
                    setupRemoteVideo();
                }
            });
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            //如果对方主动挂断，会先改变房间状态，然后后台通过云通信发送消息， 在Update方法中进行处理
            //如果是对方断网或是接收不到数据，则自己挂断。
            //0 USER_OFFLINE_QUIT: 用户主动离开
            //1 USER_OFFLINE_DROPPED：因过长时间收不到对方数据包，超时掉线。
            Log.i(TAG,"onUserOffline: uid: " + uid + " reason: " + reason);
            if(USER_OFFLINE_DROPPED == reason) {
                if (isFinishing()) {
                    return;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isCalling) {
                            if (mConsultBean.mUserType == USER_TYPE_DOCTOR) {
                                ToastUtils.showShort(mContex, getString(R.string.patient_net_error));
                            } else {
                                ToastUtils.showShort(mContex, getString(R.string.doctor_net_error));
                            }
                            stopCalling();
                        }
                    }
                });
            }
        }

        @Override
        public void onUserMuteVideo(final int uid, final boolean muted) {
            // muted = true 对方关闭了摄像头
            // muted = false 对方打开了摄像头
            Log.i(TAG,"onUserMuteVideo uid: " + uid + ", muted: " + muted);
            if(isCalling){
                if (isFinishing()) {
                    return;
                }
                showRemote = !muted;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //通知聊天界面，关闭对方视频。切换自己的。或者切换成语音模式
                        ChatActivity.UpdateWindow event = new ChatActivity.UpdateWindow();
                        event.showRemote = chatActivityShow();
                        EventBus.getDefault().post(event);

                        if(display){
                            if(showLocal){
                                display = false;
                                changeWindow(display);
                            }else {
                                changeWindow(display);
                            }
                        }else{
                            if(showLocal){
                                changeWindow(display);
                            }else{
                                display = true;
                                changeWindow(display);
                            }

                        }
                    }
                });
            }
        }

        @Override
        public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
//            LogUtils.i(TAG,"onNetworkQuality 上行速度:" + txQuality + "下行速度：" + rxQuality);
            updateNetQuality(txQuality);
        }
    };

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        super.setContentView(R.layout.activity_room1);
        NBSAppAgent.onEvent("诊室-视话问诊-进入诊室");
        mConsultBean = (ConsultBean) getIntent().getSerializableExtra(EXTRA_AGORA_BEAN);
        EventBus.getDefault().register(this);

        //注册消息监听
        MessageEvent.getInstance().addObserver(this);

        new Thread(mRunnable1).run();

        // check network
        if (!NetworkUtils.isConnectedToNetwork(getApplicationContext())) {
            onError(104);
        }
    }

    private void initAgoraEngineAndJoinChannel() {
        initializeAgoraEngine();     //初始化
        initViews();
        setupCallMode();
    }

    // 1.初始化
    private void initializeAgoraEngine() {
        //Test vendor key:  6D7A26A1D3554A54A9F43BE6797FE3E2
        AgoraApplication.getInstance().setRtcEngine(mConsultBean.mAppID, mRtcEventHandler);
        rtcEngine = AgoraApplication.getInstance().getRtcEngine();
    }

    //设置本地视频
    private void setupLocalVideo() {
//        try {
//            LogUtils.i("testttttt:  ","try1");
//            if (this.mLocalView == null) {// local view has not been added before
//                LogUtils.i("testttttt:  ","try2");
//                RelativeLayout container = (RelativeLayout) findViewById(R.id.user_local_view);
//                mLocalView = AgoraApplication.addSurfaceView(container, -1,CALLING_TYPE_VIDEO);
//            } else {
//                LogUtils.i("testttttt:  ","try3");
//                rtcEngine.setupLocalVideo(new VideoCanvas(mLocalView));
//            }
//        } catch (Exception e) {
//            LogUtils.i("testttttt:  ","catch1");
//            e.printStackTrace();
//        }
//
//        PackageManager pm = getPackageManager();
//        boolean permission = (PackageManager.PERMISSION_GRANTED ==
//                pm.checkPermission("android.permission.RECORD_AUDIO", "packageName"));
//        if (permission) {
//            LogUtils.i("testttttt:  ","有这个权限android.permission.CAMERA");
//        }else {
//            LogUtils.i("testttttt:  ","木有这个权限android.permission.CAMERA");
//        }
//        permission = (PackageManager.PERMISSION_GRANTED ==
//                pm.checkPermission("android.permission.RECORD_AUDIO", "packageName"));
//        if (permission) {
//            LogUtils.i("testttttt:  ","有这个权限RECORD_AUDIO");
//        }else {
//            LogUtils.i("testttttt:  ","木有这个权限RECORD_AUDIO");
//        }

        if (this.mLocalView == null) {// local view has not been added before
            RelativeLayout container = (RelativeLayout) findViewById(R.id.user_local_view);
            mLocalView = AgoraApplication.addSurfaceView(container, -1,CALLING_TYPE_VIDEO);
        } else {
            rtcEngine.setupLocalVideo(new VideoCanvas(mLocalView));
        }
    }

    //设置远程视频
    private void setupRemoteVideo() {
        LogUtils.i(TAG,"funtion setupRemoteVideo start");
        if (mRemoteView == null) {// ensure remote video view setup
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            mRemoteView = RtcEngine.CreateRendererView(getApplicationContext());
            mRemoteContainer.addView(mRemoteView, params);
        }

        int successCode = rtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, mConsultBean.mRemoteID));
        if (successCode < 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    rtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, mConsultBean.mRemoteID));
                    mRemoteView.invalidate();
                }
            }, 500);
        }
    }

    //加入频道
    private void joinChannel() {
        //房间密钥，房间名，附加信息，用户id
        rtcEngine.joinChannel(mConsultBean.mVendorKey, mConsultBean.mRoomID, ""/*optionalInfo*/, mConsultBean.mLocalID/*optionalUid*/);
    }

    @Override
    public void finish() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// keep screen on - turned off
        EventBus.getDefault().post(new ChatActivity.CallFinish());
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        index = 0;
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册消息监听
        MessageEvent.getInstance().addObserver(this);
        if(mConsultBean.mCallType == CALLING_TYPE_VIDEO){
            changeWindow(display);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        AlterDialogView.Builder builder = new AlterDialogView.Builder(mContex);
        builder.setMessage(getString(R.string.calling_finish_prompt));
        builder.setTitle(getString(R.string.hint));
        builder.setNegativeButton(getString(R.string.error_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getString(R.string.error_confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        arg0.dismiss();
                        if (mConsultBean.mUserType == USER_TYPE_DOCTOR) {
                            setRoomState("3");//房间状态修改为：3=已就诊
                            stopCalling();
                        }else{
                            setRoomState("0");//房间状态修改为：0=未就诊
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    public void onUserInteraction(View view) {
        if (view.getId() == R.id.action_hung_up || view.getId() == R.id.iv_left || view.getId() == R.id.btn_stop) {//退出
            onBackPressed();
        } else if (view.getId() == R.id.tv_chat) {//聊天
            if (mConsultBean.mCallType == CALLING_TYPE_VIDEO) {
                TimApplication.enterTimchat(this, mConsultBean.mRoomID, mConsultBean.mUserName, true, chatActivityShow(), mConsultBean.mCallType == CALLING_TYPE_VIDEO ? CALLING_TYPE_VIDEO : CALLING_TYPE_VOICE, time, mConsultBean.mUserType == USER_TYPE_DOCTOR ? mConsultBean.mRegisterID : "", true);
            }else{
                TimApplication.enterTimchat(this, mConsultBean.mRoomID, mConsultBean.mUserName, true, mConsultBean.mRemoteID, mConsultBean.mCallType == CALLING_TYPE_VIDEO ? CALLING_TYPE_VIDEO : CALLING_TYPE_VOICE, time, mConsultBean.mUserType == USER_TYPE_DOCTOR ? mConsultBean.mRegisterID : "", false);
            }
        } else if (view.getId() == R.id.tv_recipe) {//开处方;
            Intent recipeIntent = new Intent(RoomActivity1.this, RecipeActivity.class);
            recipeIntent.putExtra("id", mConsultBean.mRegisterID);
            startActivity(recipeIntent);
        } else {
            super.onUserInteraction(view);
        }
    }

    /*
     * 内部方法
     */
    void initViews() {
        //初始化页面及监听器
        // muter 麦克风
        mMuterEnabler = (CheckBox) findViewById(R.id.action_muter);
        mMuterEnabler.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean mutes) {
                rtcEngine.muteLocalAudioStream(mutes);
            }
        });
        // speaker 扬声器
        mSpeakerEnabler = (CheckBox) findViewById(R.id.action_speaker);
        mSpeakerEnabler.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean usesSpeaker) {
                rtcEngine.setEnableSpeakerphone(!usesSpeaker);
            }
        });

        // camera enabler 摄像头开关
        mCameraEnabler = (CheckBox) findViewById(R.id.action_camera_enabler);
        mCameraEnabler.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean disablesCamera) {
                if(isCalling){
                    rtcEngine.muteLocalVideoStream(disablesCamera);
                    //disablesCamera true 关掉摄像头  false 打开摄像头
                    showLocal = !disablesCamera;
                    mCameraSwitcher.setVisibility(disablesCamera ? View.GONE : View.VISIBLE);
                    if (display) {
                        if(showRemote){
                            changeWindow(display);
                        }else{
                            display = false;
                            changeWindow(display);
                        }

                    }else{
                        if(showRemote){
                            display = true;
                            changeWindow(display);
                        }else{
                            changeWindow(display);
                        }
                    }
                }
            }
        });

        // camera switcher 前后 摄像头转换
        mCameraSwitcher = (ImageView) findViewById(R.id.action_camera_switcher);
        mCameraSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rtcEngine.switchCamera();
            }
        });

        findViewById(R.id.action_hung_up).setOnClickListener(getViewClickListener());//挂断
        findViewById(R.id.btn_stop).setOnClickListener(getViewClickListener());//退出

        findViewById(R.id.iv_left).setOnClickListener(getViewClickListener());//返回
        findViewById(R.id.tv_recipe).setOnClickListener(getViewClickListener());//开处方

        findViewById(R.id.tv_chat).setOnClickListener(getViewClickListener());//聊天


        mDuration = (TextView) findViewById(R.id.stat_time);//时间
        appNotification = (TextView) findViewById(R.id.app_notification);

        mLocalContainer = (RelativeLayout) findViewById(R.id.user_local_view);
        mRemoteContainer = (RelativeLayout) findViewById(R.id.user_remote_view);
        mUserInfoContainer = (LinearLayout) findViewById(R.id.ll_user_info);
        tv_name = (TextView) findViewById(R.id.tv_username);
        tv_name.setText(mConsultBean.mUserName);
        tv_time = (TextView) findViewById(R.id.tv_usertime);
    }

    void initTimer() {
        //初始化计时器
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        if (time >= 3600) {
                            appNotification.setText(String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60)));
                            tv_time.setText(String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60)));
                        } else {
                            appNotification.setText(String.format("%02d:%02d", (time % 3600) / 60, (time % 60)));
                            tv_time.setText(String.format("%02d:%02d", (time % 3600) / 60, (time % 60)));
                        }
                        appNotification.setTextColor(getResources().getColor(R.color.yellow_time));
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1000, 1000);
    }

    void setupCallMode() {
        findViewById(R.id.layout_title).setVisibility(View.GONE);//标题栏
        ((TextView) findViewById(R.id.remote_user_name)).setText(mConsultBean.mUserName);//名称
        ImageLoader.getInstance().displayImage(mConsultBean.mUserFace, (ImageView) findViewById(R.id.remote_user_icon), ImageUtils.getCircleDisplayOptions());//头像

        if (CALLING_TYPE_VIDEO == mConsultBean.mCallType) {// video call
            mCameraEnabler.setVisibility(View.VISIBLE);//相机开关
            mLocalContainer.setVisibility(View.VISIBLE);//本地图像
            mRemoteContainer.setVisibility(View.VISIBLE);//远程图像
        } else if (CALLING_TYPE_VOICE == mConsultBean.mCallType) {// voice call
            mCameraEnabler.setVisibility(View.GONE);
            mCameraSwitcher.setVisibility(View.GONE);
            mLocalContainer.setVisibility(View.GONE);
            mRemoteContainer.setVisibility(View.GONE);
        }

        if (mConsultBean.mUserType == USER_TYPE_DOCTOR) {//医生
            setRoomState("4");//房间状态修改为：4=呼叫中
            playMusic(mContex);
            appNotification.setText(getString(R.string.calling_patient));
            updateDoctorUI();
        } else {
            setRoomState("1");//房间状态修改为：1=候诊中
            if (mConsultBean.mCallType == CALLING_TYPE_VIDEO){
                getWaitingCount();
                appNotification.setText(getString(R.string.login_success));
                ((Button)findViewById(R.id.btn_stop)).setText(R.string.login_out);
            }else{
                appNotification.setText(getString(R.string.calling_doctor));
            }

        }

        rtcEngine.disableVideo();
        rtcEngine.muteLocalVideoStream(true);
        rtcEngine.muteAllRemoteVideoStreams(true);
        rtcEngine.muteLocalAudioStream(true);
        rtcEngine.muteAllRemoteAudioStreams(true);//((RtcEngineImpl)rtcEngine).setVideoCamera();

        mMuterEnabler.setEnabled(false);
        mSpeakerEnabler.setEnabled(false);
        mCameraEnabler.setEnabled(false);
        mCameraSwitcher.setEnabled(false);
    }

    void startCalling() {//开始通话
        Log.i(TAG,"startCalling");
        stopMusic();
        initTimer();
        appNotification.setText(getString(R.string.calling));
        //语音视频都有工具界面显示
        findViewById(R.id.top_actions_container).setVisibility(View.VISIBLE);//聊天工具
        findViewById(R.id.iv_line).setVisibility(View.VISIBLE);//不会动的波浪线

        if (mConsultBean.mUserType == USER_TYPE_DOCTOR) {
            //医生端去掉之前呼叫的 拒绝按钮
            findViewById(R.id.btn_stop).setVisibility(View.GONE);
            //开处方功能显示
            findViewById(R.id.tv_recipe).setVisibility(View.VISIBLE);
        }else{
            //患者端把之前的 接听拒绝按钮隐藏掉
            findViewById(R.id.btn_no).setVisibility(View.GONE);
            findViewById(R.id.btn_yes).setVisibility(View.GONE);
            //开处方功能隐藏
            findViewById(R.id.tv_recipe).setVisibility(View.GONE);
        }

        if (CALLING_TYPE_VIDEO == mConsultBean.mCallType) {// video call
            rtcEngine.enableVideo();
            rtcEngine.muteLocalVideoStream(false);
            rtcEngine.muteLocalAudioStream(false);
            rtcEngine.muteAllRemoteVideoStreams(false);
            rtcEngine.muteAllRemoteAudioStreams(false);
            mCameraSwitcher.setVisibility(View.VISIBLE);//相机切换
            mUserInfoContainer.setVisibility(View.VISIBLE);//视频时左上角信息显示
            findViewById(R.id.layout_title).setVisibility(View.GONE);//标题栏不显示
            setupLocalVideo(); // 设置本地视频
            joinChannel();  // 加入频道
            setupRemoteVideo();// 设置远程视频
        } else if (CALLING_TYPE_VOICE == mConsultBean.mCallType) {// voice call
            findViewById(R.id.btn_stop).setVisibility(View.GONE); //把结束语音按钮隐藏掉，新UI不需要了
            findViewById(R.id.layout_dialog).setVisibility(View.GONE);

            //摄像功能不可用
            mCameraEnabler.setVisibility(View.GONE);
            findViewById(R.id.action_camera_enabler_disable).setVisibility(View.VISIBLE);
            rtcEngine.muteLocalVideoStream(true);
            rtcEngine.muteLocalAudioStream(false);
            rtcEngine.muteAllRemoteVideoStreams(true);
            rtcEngine.muteAllRemoteAudioStreams(false);
            joinChannel();  // 加入频道
            setupRemoteVideo();// 设置远程视频
        }

        mMuterEnabler.setEnabled(true);
        mSpeakerEnabler.setEnabled(true);
        mCameraEnabler.setEnabled(true);
        mCameraSwitcher.setEnabled(true);

        // setup states of action buttons
        mMuterEnabler.setChecked(false);//打开麦克风
        mSpeakerEnabler.setChecked(false);//打开扬声器
        mCameraEnabler.setChecked(false);//打开摄像头
        rtcEngine.setEnableSpeakerphone(true);//直接把 扬声器 setChecked=true,手机还是听筒模式。 加上这一句才能变成扬声器模式。
    }

    void stopCalling() {
        isCalling = false;
        stopMusic();
        new Thread(new Runnable() {
            @Override
            public void run() {
                rtcEngine.leaveChannel();
            }
        }).run();
        finish();
    }

    void getWaitingCount(){
        HttpConsult.getWaitingCount event = new HttpConsult.getWaitingCount(mConsultBean.mRoomID,mConsultBean.mUserID);
        event.setHttpListener(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(mContex,msg);
            }

            @Override
            public void onSuccess(String s) {
                mDuration.setVisibility(View.VISIBLE);
                mDuration.setText(String.format(getString(R.string.waiting_count),s));

                SpannableStringBuilder builder = new SpannableStringBuilder(mDuration.getText().toString());
                ForegroundColorSpan Span = new ForegroundColorSpan(getResources().getColor(R.color.app_color_main));
                builder.setSpan(Span, 5, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mDuration.setText(builder);
            }
        });
        new HttpClient(mContex, event).start();
    }

    /**
     * 修改房间状态
     * @param state 状态 0=未就诊,1=候诊中,2=就诊中,3=已就诊,4=呼叫中,5=离开中
     */
    void setRoomState(final String state) {
        HttpConsult.setRoomState event = new HttpConsult.setRoomState(mConsultBean.mRoomID, state);
        event.setHttpListener(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                LogUtils.i(TAG,"把房间状态设为：" + state + "失败");
                if(mConsultBean.mUserType == USER_TYPE_PATIENT){
                    if("1".equals(state)){
                        ToastUtils.showShort(mContex,getString(R.string.enter_room_failed));
                        finish();
                    }else{
                        ToastUtils.showShort(mContex,getString(R.string.update_room_failed));
                    }
                }else {
                    ToastUtils.showShort(mContex,getString(R.string.update_room_failed));
                }
            }

            @Override
            public void onSuccess(String s) {
                LogUtils.i(TAG,"把房间状态设为：" + state + "成功");
            if(mConsultBean.mUserType == USER_TYPE_PATIENT){
                    if("0".equals(state)){
                        stopCalling();
                    }
                    if("2".equals(state)){
                        startCalling();
                    }
                }else {
                    if("1".equals(state)){
                        stopCalling();
                    }
                }
            }
        });
        new HttpClient(mContex, event).start();
    }

    /*
     * 呼叫
     */
    @Override
    public void update(Observable observable, Object data) {//接收消息
        if (observable instanceof MessageEvent && data != null) {
            Message mMessage = MessageFactory.getMessage((TIMMessage) data);
            if (mMessage != null && mMessage instanceof CustomMessage) {
                CustomMessage customMessage = (CustomMessage) mMessage;
                LogUtils.i(TAG,"接收到云通信数据：CustomMessage type: " + customMessage.getType());
                if (mConsultBean.mUserType == USER_TYPE_DOCTOR) {//医生
                    if (customMessage.getType() == CustomMessage.Type.DOING && !isCalling) {
                        if(isFisrtDoing){
                            isFisrtDoing = false;
                            startCalling();
                        }
                    }
                    if (customMessage.getType() == CustomMessage.Type.NONE) {
                        if(isCalling){
                            ToastUtils.showShort(mContex, getString(R.string.leave_room));//对方离开诊室
                        }else {
                            ToastUtils.showShort(mContex, getString(R.string.refuse_called));
                        }
                        stopCalling();
                    }
                    if(customMessage.getType() == CustomMessage.Type.PATIENT_LEAVE){
                        ToastUtils.showShort(mContex,getString(R.string.patient_finish));//患者离开了诊间
                        EventBus.getDefault().post(new ChatActivity.CallFinish());
                        stopCalling();
                    }
                    if(customMessage.getType() == CustomMessage.Type.WAITING){
                        ToastUtils.showShort(mContex,getString(R.string.refuse_called));
                        stopCalling();
                    }
                } else {//患者
                    if (customMessage.getType() == CustomMessage.Type.CALLING && !isCalling) {
                        playMusic(mContex);
                        updatePatientUI();
                        findViewById(R.id.btn_yes).setOnClickListener(new View.OnClickListener() {//接听
                            @Override
                            public void onClick(View v) {
                                LogUtils.i(TAG,"点击接听按钮");
                                setRoomState("2"); //房间状态修改为：2=就诊中
                            }
                        });
                        findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {//挂断
                            @Override
                            public void onClick(View v) {
                                LogUtils.i(TAG,"点击拒绝按钮");
                                setRoomState("0");//房间状态修改为：0=未就诊
                            }
                        });
                    }
                    //聊天过程中，Web端 医生挂断的时候，不会走 onUserOffline()方法。   app端 医生挂断，才会走onUserOffline()方法
                    if (customMessage.getType() == CustomMessage.Type.DONE) {
                        EventBus.getDefault().post(new ChatActivity.CallFinish());
                        if (isFinishing()) {
                            return;
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtils.showShort(mContex, getString(R.string.doctor_finish));
                                stopCalling();
                            }
                        });
                    }
                    //呼叫过程中，医生取消呼叫 推送了1过来。  手机端跳回呼叫界面
                    if (customMessage.getType() == CustomMessage.Type.WAITING) {
                        if (isFinishing()) {
                            return;
                        }
                        if(!isFisrtWait){
                            ToastUtils.showShort(mContex,getString(R.string.doctor_cancel_calling));
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                isFisrtWait = false;
                                cancelCallPatient();
                            }
                        });
                    }
                }
            }
        }
    }

    /*
     * BaseHandlerActivity
     */
    @Override
    public synchronized void onError(int err) {
        if (isFinishing()) {
            return;
        }
        // incorrect vendor key
        if (101 == err) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort(RoomActivity1.this, getString(R.string.auth_failed));
                    stopCalling();
                }
            });
        }

        // no network connection
        if (104 == err) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    appNotification.setText(R.string.network_error);
                }
            });
        }
    }

    @Override
    public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
        try {
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void onUserJoined(final int uid, int elapsed) {
        //其他用户加入当前频道回调
        //提示有用户加入了频道。如果该客户端加入频道时已经有人在频道中，SDK也会向应用程序上报这些已在频道中的用户。
        Log.i(TAG,"onUserJoined: uid: " + uid);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConsultBean.mRemoteID = uid;
                if (mConsultBean.mUserType == USER_TYPE_DOCTOR && !isCalling) {
                    setRoomState("4");//房间状态修改为：4=呼叫中
                }
            }
        });
    }


    /**
     * 监控网络状态
     *   UNKNOWN = 0;
     *   EXCELLENT = 1;
     *   GOOD = 2;
     *   POOR = 3;
     *   BAD = 4;
     *   VBAD = 5;
     *   DOWN = 6;
     * @param quality
     */
    public void updateNetQuality(final int quality) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {//update UI
                switch (quality){
                    case 0:
                        ((ImageView)findViewById(R.id.iv_speed)).setImageDrawable(getResources().getDrawable(R.drawable.ic_net0));
                        break;
                    case 1:
                        ((ImageView)findViewById(R.id.iv_speed)).setImageDrawable(getResources().getDrawable(R.drawable.ic_net1));
                        break;
                    case 2:
                        ((ImageView)findViewById(R.id.iv_speed)).setImageDrawable(getResources().getDrawable(R.drawable.ic_net2));
                        break;
                    case 3:
                        ((ImageView)findViewById(R.id.iv_speed)).setImageDrawable(getResources().getDrawable(R.drawable.ic_net3));
                        break;
                    case 4:
                        ((ImageView)findViewById(R.id.iv_speed)).setImageDrawable(getResources().getDrawable(R.drawable.ic_net4));
                        break;
                    case 5:
                        ((ImageView)findViewById(R.id.iv_speed)).setImageDrawable(getResources().getDrawable(R.drawable.ic_net5));
                        break;
                    default:
                        ((ImageView)findViewById(R.id.iv_speed)).setImageDrawable(getResources().getDrawable(R.drawable.ic_net0));
                        break;
                }
            }
        });
    }

    /*
        * 其它
        * */
    private static String[] Permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};

    private static int index = 0;

    private Runnable mRunnable1 = new Runnable() {
        @Override
        public void run() {
            if (index == Permissions.length) {
                initAgoraEngineAndJoinChannel();
            } else {
                requestPermission(index, Permissions[index++], mRunnable1, mRunnable2);
            }
        }
    };

    private Runnable mRunnable2 = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };

    /***
     * 播放接收到呼叫音乐提示
     *
     * @param context 上下文
     */
    private void playMusic(Context context) {
        ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).setMode(AudioManager.MODE_RINGTONE);
        mMediaPlayer = MediaPlayer.create(context, R.raw.call);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mMediaPlayer != null) {
                    mMediaPlayer.start();
                }
            }
        });
        mMediaPlayer.start();
    }

    /***
     * 停止播放
     */
    public void stopMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public static class ChangeWindow {
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClickWindow(ChangeWindow event) {//点击窗口，切换大小视屏
        LogUtils.i(TAG,"点击窗口，切换大小视屏");
        if(isCalling){
            if(display){
                display = false;
            }else {
                display = true;
            }
            changeWindow(display);
        }
    }


    /**
     *
     * @param flag  true 大屏幕显示对方，小屏幕显示自己。   false 小屏幕显示对方，大屏幕显示自己
     */
    public void changeWindow(boolean flag){
        if(isCalling){
            //mRemoteView 对方的视频
            //mLocalView 自己本地的视频
            RelativeLayout container = (RelativeLayout) findViewById(R.id.user_local_view);
            mRemoteContainer.removeAllViews();//大屏幕
            container.removeAllViews();//小屏幕

            if(flag){
                if(showLocal){
                    //把本地视频放到小框框里
                    AgoraApplication.addSurfaceView(container, -1,CALLING_TYPE_VIDEO);}
                if(showRemote){
                    //把对方视频放到大框框里
                    AgoraApplication.addSurfaceView(mRemoteContainer, mConsultBean.mRemoteID,CALLING_TYPE_VIDEO_FULL);}
            }else{
                if(showRemote){
                    //把对方视频放到小框框里
                    AgoraApplication.addSurfaceView(container, mConsultBean.mRemoteID,CALLING_TYPE_VIDEO);
                }
                if(showLocal){
                    //把本地视频放到大框框里
                    AgoraApplication.addSurfaceView(mRemoteContainer, -1,CALLING_TYPE_VIDEO_FULL);}
            }

            if(!showLocal && !showRemote){
                mUserInfoContainer.setVisibility(View.GONE);
                mCameraSwitcher.setVisibility(View.GONE);
            }else {
                mUserInfoContainer.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 调整医生端呼叫界面风格
     */
    private void updateDoctorUI(){
        appNotification.setTextColor(getResources().getColor(R.color.app_color_white));
        findViewById(R.id.iv_line).setVisibility(View.VISIBLE);//不会动的波浪线
        ((TextView) findViewById(R.id.remote_user_name)).setTextColor(getResources().getColor(R.color.app_color_white));
        findViewById(R.id.rl_container).setBackgroundColor(getResources().getColor(R.color.app_color_main));
        findViewById(R.id.ll_total_container).setBackgroundColor(getResources().getColor(R.color.app_color_main));
        Button btn_stop = ((Button)findViewById(R.id.btn_stop));
        btn_stop.setText("");
        btn_stop.setBackgroundDrawable(getResources().getDrawable(R.drawable.go_micro_button_background1));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btn_stop.getLayoutParams();
        params.width = dip2px(mContex, 70);
        params.height = dip2px(mContex, 70);
        params.setMargins(0,0,0,240);
        btn_stop.setLayoutParams(params);
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRoomState("1");//房间状态修改为：1=候诊中
            }
        });
    }

    /**
     * 医生呼叫患者后，患者端变换界面风格
     */
    private void updatePatientUI(){
        if (mConsultBean.mCallType == CALLING_TYPE_VIDEO){
            appNotification.setText(getString(R.string.video_request));
            mDuration.setVisibility(View.GONE);
        }else{
            appNotification.setText(getString(R.string.voice_request));
        }

        findViewById(R.id.btn_stop).setVisibility(View.GONE);
        findViewById(R.id.layout_dialog).setVisibility(View.VISIBLE);

        findViewById(R.id.iv_line).setVisibility(View.VISIBLE);//不会动的波浪线
        appNotification.setTextColor(getResources().getColor(R.color.app_color_white));
        ((TextView) findViewById(R.id.remote_user_name)).setTextColor(getResources().getColor(R.color.app_color_white));
        findViewById(R.id.rl_container).setBackgroundColor(getResources().getColor(R.color.app_color_main));
        findViewById(R.id.ll_total_container).setBackgroundColor(getResources().getColor(R.color.app_color_main));
    }

    /**
     * 医生取消呼叫患者后，患者端变换界面风格
     */
    private void cancelCallPatient(){
        stopMusic();
        if (mConsultBean.mCallType == CALLING_TYPE_VIDEO){
            getWaitingCount();
            appNotification.setText(getString(R.string.login_success));
            ((Button)findViewById(R.id.btn_stop)).setText(R.string.login_out);
        }else{
            appNotification.setText(getString(R.string.calling_doctor));
        }

        findViewById(R.id.btn_stop).setVisibility(View.VISIBLE);
        findViewById(R.id.layout_dialog).setVisibility(View.GONE);

        findViewById(R.id.iv_line).setVisibility(View.GONE);//不会动的波浪线
        appNotification.setTextColor(getResources().getColor(R.color.color_tag_string));
        ((TextView) findViewById(R.id.remote_user_name)).setTextColor(getResources().getColor(R.color.color_main_string));
        findViewById(R.id.rl_container).setBackgroundColor(getResources().getColor(R.color.app_color_white));
        findViewById(R.id.ll_total_container).setBackgroundColor(getResources().getColor(R.color.app_color_white));
    }

    /**
     * 获取聊天界面 视频框应该显示的内容
     */
    private int chatActivityShow(){
        int showid = -2; //-1表示本地视频，-2表示双方都关闭了摄像机，其它表示对方的视频号
        if(showLocal){
            showid = -1;
        }
        if(showRemote){
            showid = mConsultBean.mRemoteID;
        }
        return showid;
    }

    /**
     * dp转为px
     * @param context  上下文
     * @param dipValue dp值
     * @return
     */
    private int dip2px(Context context,float dipValue)
    {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dipValue, r.getDisplayMetrics());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销消息监听
        MessageEvent.getInstance().deleteObserver(this);
        EventBus.getDefault().unregister(this);
        RtcEngine.destroy();
        rtcEngine = null;
    }


}