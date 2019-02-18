package io.agora.core;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.util.NetworkUtils;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * * some refs:
 * 1. http://stackoverflow.com/questions/6026625/layout-design-surfaceview-doesnt-display
 * 2. http://stackoverflow.com/questions/1096618/android-surfaceview-scrolling/2216788#2216788
 * <p/>
 * Created by apple on 15/9/9.
 */
public class RoomActivity extends BaseHandlerActivity {

    public final static int CALLING_TYPE_VIDEO = 0x100;
    public final static int CALLING_TYPE_VOICE = 0x101;

    public final static String EXTRA_CALLING_TYPE = "EXTRA_CALLING_TYPE";
    public final static String EXTRA_VENDOR_KEY = "EXTRA_VENDOR_KEY";
    public final static String EXTRA_CHANNEL_ID = "EXTRA_CHANNEL_ID";

    private int mCallingType;
    private String vendorKey = "";
    private String channelId = "";

    private TextView mDuration;//时间
    private TextView mByteCounts;//网速
    private View mCameraEnabler;
    private View mCameraSwitcher;
    private SurfaceView mLocalView;//本地图像
    private LinearLayout mRemoteUserContainer;//远程图像

    private AlertDialog alertDialog;
    private int time = 0;
    private int mLastRxBytes = 0;
    private int mLastTxBytes = 0;
    private int mLastDuration = 0;
    private int mRemoteUserViewWidth = 0;

    private RtcEngine rtcEngine;
    private BaseMessageHandler messageHandler;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        super.setContentView(R.layout.activity_room);
        super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// keep screen on - turned on

        mCallingType = getIntent().getIntExtra(EXTRA_CALLING_TYPE, CALLING_TYPE_VOICE);// default is voice call
        vendorKey = getIntent().getStringExtra(EXTRA_VENDOR_KEY);
        channelId = getIntent().getStringExtra(EXTRA_CHANNEL_ID);

        setupRtcEngine();
        initViews();
        setupTime();

        if (CALLING_TYPE_VIDEO == mCallingType) {// video call
            View simulateClick = new View(getApplicationContext());
            simulateClick.setId(R.id.wrapper_action_video_calling);
            this.onUserInteraction(simulateClick);
        } else if (CALLING_TYPE_VOICE == mCallingType) {// voice call
            View simulateClick = new View(getApplicationContext());
            simulateClick.setId(R.id.wrapper_action_voice_calling);
            this.onUserInteraction(simulateClick);
        }
        // check network
        if (!NetworkUtils.isConnectedToNetwork(getApplicationContext())) {
            onError(104);
        }
    }

    @Override
    public void finish() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        super.finish();
    }

    @Override
    public void onBackPressed() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                rtcEngine.leaveChannel();
            }
        }).run();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// keep screen on - turned off
    }

    @Override
    public void onUserInteraction(View view) {
        if (view.getId() == R.id.action_hung_up && view.getId() == R.id.action_back) {
            onBackPressed();
        } else if (view.getId() == R.id.wrapper_action_video_calling) {
            mCallingType = CALLING_TYPE_VIDEO;
            mCameraEnabler.setVisibility(View.VISIBLE);
            mCameraSwitcher.setVisibility(View.VISIBLE);

            removeBackgroundOfCallingWrapper();
            findViewById(R.id.user_local_voice_bg).setVisibility(View.GONE);
            findViewById(R.id.wrapper_action_video_calling).setBackgroundResource(R.drawable.ic_room_button_yellow_bg);

            ensureLocalViewIsCreated();
            // enable video call
            rtcEngine.enableVideo();
            rtcEngine.muteLocalVideoStream(false);
            rtcEngine.muteLocalAudioStream(false);
            rtcEngine.muteAllRemoteVideoStreams(false);

            // join video call
            if (mRemoteUserContainer.getChildCount() == 0) {
                this.setupChannel();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateRemoteUserViews(CALLING_TYPE_VIDEO);
                }
            }, 500);

            // ensure video camera enabler states
            CheckBox cameraEnabler = (CheckBox) findViewById(R.id.action_camera_enabler);
            cameraEnabler.setChecked(false);
        } else if (view.getId() == R.id.wrapper_action_voice_calling) {
            mCallingType = CALLING_TYPE_VOICE;
            mCameraEnabler.setVisibility(View.GONE);
            mCameraSwitcher.setVisibility(View.GONE);

            removeBackgroundOfCallingWrapper();
            findViewById(R.id.user_local_voice_bg).setVisibility(View.VISIBLE);
            findViewById(R.id.wrapper_action_voice_calling).setBackgroundResource(R.drawable.ic_room_button_yellow_bg);

            ensureLocalViewIsCreated();
            // disable video call when necessary
            rtcEngine.disableVideo();
            rtcEngine.muteLocalVideoStream(true);
            rtcEngine.muteAllRemoteVideoStreams(true);

            // join voice call
            if (mRemoteUserContainer.getChildCount() == 0) {
                this.setupChannel();
            }

            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateRemoteUserViews(CALLING_TYPE_VOICE);
                }
            }, 500);
        } else {
            super.onUserInteraction(view);
        }
    }

    /****************************************
     * 内部方法
     ***************************************/
    //Test vendor key:  6D7A26A1D3554A54A9F43BE6797FE3E2
    void setupRtcEngine() {
        // setup messageHandler
        messageHandler = new BaseMessageHandler();
        messageHandler.setActivity(this);

        // setup engine
        try {
            rtcEngine = RtcEngine.create(getApplicationContext(), vendorKey, messageHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rtcEngine.setLogFile(getApplicationContext().getExternalFilesDir(null).toString() + "/agorasdk.log");

        //
        rtcEngine.enableVideo();
    }

    //初始化页面及监听器
    void initViews() {

        // muter
        CheckBox muter = (CheckBox) findViewById(R.id.action_muter);
        muter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean mutes) {
                rtcEngine.muteLocalAudioStream(mutes);
                compoundButton.setBackgroundResource(mutes ? R.drawable.ic_room_mute_pressed : R.drawable.ic_room_mute);
            }
        });

        // speaker
        CheckBox speaker = (CheckBox) findViewById(R.id.action_speaker);
        speaker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean usesSpeaker) {
                rtcEngine.setEnableSpeakerphone(usesSpeaker);
                compoundButton.setBackgroundResource(usesSpeaker ? R.drawable.ic_room_loudspeaker : R.drawable.ic_room_loudspeaker_pressed);
            }
        });

        // camera enabler
        CheckBox cameraEnabler = (CheckBox) findViewById(R.id.action_camera_enabler);
        cameraEnabler.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean disablesCamera) {
                rtcEngine.muteLocalVideoStream(disablesCamera);
                if (disablesCamera) {
                    findViewById(R.id.user_local_voice_bg).setVisibility(View.VISIBLE);
                    rtcEngine.muteLocalVideoStream(true);

                } else {
                    findViewById(R.id.user_local_voice_bg).setVisibility(View.GONE);
                    rtcEngine.muteLocalVideoStream(false);
                }
                compoundButton.setBackgroundResource(disablesCamera ? R.drawable.ic_room_button_close_pressed : R.drawable.ic_room_button_close);
            }
        });

        // camera switcher
        CheckBox cameraSwitch = (CheckBox) findViewById(R.id.action_camera_switcher);
        cameraSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean switches) {
                rtcEngine.switchCamera();
                compoundButton.setBackgroundResource(switches ? R.drawable.ic_room_button_change_pressed : R.drawable.ic_room_button_change);
            }
        });

        // setup states of action buttons
        muter.setChecked(false);//关闭麦克
        speaker.setChecked(true);//打开扬声器
        cameraEnabler.setChecked(false);//关闭摄像头
        cameraSwitch.setChecked(false);//切换前后摄像头

        findViewById(R.id.wrapper_action_video_calling).setOnClickListener(getViewClickListener());//视频通话
        findViewById(R.id.wrapper_action_voice_calling).setOnClickListener(getViewClickListener());//语音通话
        findViewById(R.id.action_hung_up).setOnClickListener(getViewClickListener());//挂断
        findViewById(R.id.action_back).setOnClickListener(getViewClickListener());//返回

        mDuration = (TextView) findViewById(R.id.stat_time);//时间
        mByteCounts = (TextView) findViewById(R.id.stat_bytes);//流量
        mCameraEnabler = findViewById(R.id.wrapper_action_camera_enabler);
        mCameraSwitcher = findViewById(R.id.wrapper_action_camera_switcher);

        mRemoteUserViewWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics());
        mRemoteUserContainer = (LinearLayout) findViewById(R.id.user_remote_views);
        setRemoteUserViewVisibility(false);
    }

    //初始化计时器
    void setupTime() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        if (time >= 3600) {
                            mDuration.setText(String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60)));
                        } else {
                            mDuration.setText(String.format("%02d:%02d", (time % 3600) / 60, (time % 60)));
                        }
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 1000, 1000);
    }

    void setupChannel() {
        this.rtcEngine.joinChannel(
                this.vendorKey,
                this.channelId,
                "" /*optionalInfo*/,
                new Random().nextInt(Math.abs((int) System.currentTimeMillis()))/*optionalUid*/);

        ((TextView) findViewById(R.id.channel_id)).setText(String.format(getString(R.string.title_channel), channelId));

    }

    void ensureLocalViewIsCreated() {
        if (this.mLocalView == null) {
            // local view has not been added before
            mLocalView = rtcEngine.CreateRendererView(getApplicationContext());
            FrameLayout localViewContainer = (FrameLayout) findViewById(R.id.user_local_view);
            localViewContainer.addView(mLocalView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

            rtcEngine.enableVideo();
            rtcEngine.setupLocalVideo(new VideoCanvas(this.mLocalView));
        }
    }

    //切换视频音频通话时，更新 view 的显示。只是更新重用的 view，并不新添加。
    void updateRemoteUserViews(int callingType) {

        int visibility = View.GONE;
        if (CALLING_TYPE_VIDEO == callingType) {
            visibility = View.GONE;
        } else if (CALLING_TYPE_VOICE == callingType) {
            visibility = View.VISIBLE;
        }

        for (int i = 0, size = mRemoteUserContainer.getChildCount(); i < size; i++) {
            View singleRemoteView = mRemoteUserContainer.getChildAt(i);
            singleRemoteView.findViewById(R.id.remote_user_voice_container).setVisibility(visibility);

            if (CALLING_TYPE_VIDEO == callingType) {
                // re-setup remote video
                FrameLayout remoteVideoUser = (FrameLayout) singleRemoteView.findViewById(R.id.viewlet_remote_video_user);
                // ensure remote video view setup
                if (remoteVideoUser.getChildCount() > 0) {
                    final SurfaceView remoteView = (SurfaceView) remoteVideoUser.getChildAt(0);
                    if (remoteView != null) {
                        remoteView.setZOrderOnTop(true);
                        remoteView.setZOrderMediaOverlay(true);
                        int savedUid = (Integer) remoteVideoUser.getTag();
                        log("saved uid: " + savedUid);
                        rtcEngine.setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_ADAPTIVE, savedUid));
                    }
                }

            }
        }
    }

    void removeBackgroundOfCallingWrapper() {
        findViewById(R.id.wrapper_action_video_calling).setBackgroundResource(R.drawable.shape_transparent);
        findViewById(R.id.wrapper_action_voice_calling).setBackgroundResource(R.drawable.shape_transparent);
    }

    void setRemoteUserViewVisibility(boolean isVisible) {
        findViewById(R.id.user_remote_views).getLayoutParams().height = isVisible ?LinearLayout.LayoutParams.MATCH_PARENT:0;//mRemoteUserViewWidth : 0;
    }

    /****************************************
     * BaseHandlerActivity
     ***************************************/
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
                    if (alertDialog != null) {
                        return;
                    }
                    alertDialog = new AlertDialog.Builder(RoomActivity.this).setCancelable(false)
                            .setMessage(getString(R.string.error_101))
                            .setPositiveButton(getString(R.string.error_confirm), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Go to login
                                    Intent toLogin = new Intent(RoomActivity.this, LoginActivity.class);
                                    toLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(toLogin);

                                    rtcEngine.leaveChannel();
                                }
                            }).setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create();
                    alertDialog.show();
                }
            });
        }

        // no network connection
        if (104 == err) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView appNotification = (TextView) findViewById(R.id.app_notification);
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
    public void onUpdateSessionStats(final IRtcEngineEventHandler.RtcStats stats) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // bytes
                mByteCounts.setText(((stats.txBytes + stats.rxBytes - mLastTxBytes - mLastRxBytes) / 1024 / (stats.totalDuration - mLastDuration + 1)) + "KB/s");

                // remember data from this call back
                mLastRxBytes = stats.rxBytes;
                mLastTxBytes = stats.txBytes;
                mLastDuration = stats.totalDuration;
            }
        });
    }

    @Override
    public synchronized void onUserJoined(final int uid, int elapsed) {
        log("onUserJoined: uid: " + uid);
        View existedUser = mRemoteUserContainer.findViewById(Math.abs(uid));
        if (existedUser != null) {
            // user view already added
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Handle the case onFirstRemoteVideoDecoded() is called before onUserJoined()
                View singleRemoteUser = mRemoteUserContainer.findViewById(Math.abs(uid));
                if (singleRemoteUser != null) {
                    return;
                }
                LayoutInflater layoutInflater = getLayoutInflater();
                singleRemoteUser = layoutInflater.inflate(R.layout.view_remote_user, null);
                singleRemoteUser.setId(Math.abs(uid));

                TextView username = (TextView) singleRemoteUser.findViewById(R.id.remote_user_name);
                username.setText(String.valueOf(uid));

                mRemoteUserContainer.addView(singleRemoteUser, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));
                //mRemoteUserViewWidth, mRemoteUserViewWidth));

                // app hints before you join
                TextView appNotification = (TextView) findViewById(R.id.app_notification);
                appNotification.setText("");
                setRemoteUserViewVisibility(true);
            }
        });


    }

    @Override
    public void onUserOffline(final int uid) {
        log("onUserOffline: uid: " + uid);
        if (isFinishing()) {
            return;
        }
        if (mRemoteUserContainer == null) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View userViewToRemove = mRemoteUserContainer.findViewById(Math.abs(uid));
                mRemoteUserContainer.removeView(userViewToRemove);
                // no joined users any more
                if (mRemoteUserContainer.getChildCount() == 0) {
                    setRemoteUserViewVisibility(false);
                    TextView appNotification = (TextView) findViewById(R.id.app_notification);
                    appNotification.setText(R.string.room_prepare);
                }
            }
        });


    }

    @Override
    public void onUserMuteVideo(final int uid, final boolean muted) {
        log("onUserMuteVideo uid: " + uid + ", muted: " + muted);
        if (isFinishing()) {
            return;
        }
        if (mRemoteUserContainer == null) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View remoteView = mRemoteUserContainer.findViewById(Math.abs(uid));
                remoteView.findViewById(R.id.remote_user_voice_container).setVisibility(
                        (CALLING_TYPE_VOICE == mCallingType || (CALLING_TYPE_VIDEO == mCallingType && muted))
                                ? View.VISIBLE
                                : View.GONE);
                remoteView.invalidate();
            }
        });

    }

    @Override
    public synchronized void onFirstRemoteVideoDecoded(final int uid, int width, int height, final int elapsed) {
        log("onFirstRemoteVideoDecoded: uid: " + uid + ", width: " + width + ", height: " + height);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View remoteUserView = mRemoteUserContainer.findViewById(Math.abs(uid));
                // ensure container is added
                if (remoteUserView == null) {

                    LayoutInflater layoutInflater = getLayoutInflater();
                    View singleRemoteUser = layoutInflater.inflate(R.layout.view_remote_user, null);
                    singleRemoteUser.setId(Math.abs(uid));

                    TextView username = (TextView) singleRemoteUser.findViewById(R.id.remote_user_name);
                    username.setText(String.valueOf(uid));

                    mRemoteUserContainer.addView(singleRemoteUser, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));//mRemoteUserViewWidth, mRemoteUserViewWidth));

                    remoteUserView = singleRemoteUser;
                }


                FrameLayout remoteVideoUser = (FrameLayout) remoteUserView.findViewById(R.id.viewlet_remote_video_user);
                remoteVideoUser.removeAllViews();
                remoteVideoUser.setTag(uid);

                // ensure remote video view setup
                final SurfaceView remoteView = RtcEngine.CreateRendererView(getApplicationContext());
                remoteVideoUser.addView(remoteView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                //remoteView.setZOrderOnTop(true);
                //remoteView.setZOrderMediaOverlay(true);

                rtcEngine.enableVideo();
                int successCode = rtcEngine.setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));

                if (successCode < 0) {
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            rtcEngine.setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));
                            remoteView.invalidate();
                        }
                    }, 500);
                }


                if (remoteUserView != null && CALLING_TYPE_VIDEO == mCallingType) {
                    remoteUserView.findViewById(R.id.remote_user_voice_container).setVisibility(View.GONE);
                } else {
                    remoteUserView.findViewById(R.id.remote_user_voice_container).setVisibility(View.VISIBLE);
                }

                // app hints before you join
                TextView appNotification = (TextView) findViewById(R.id.app_notification);
                appNotification.setText("");
                setRemoteUserViewVisibility(true);
            }
        });

    }
}