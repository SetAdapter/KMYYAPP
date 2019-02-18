package io.agora.core;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Space;
import android.widget.TextView;

import com.kmwlyy.core.util.NetworkUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.imchat.TimApplication;
import com.kmwlyy.imchat.page.ChatActivity;
import com.winson.ui.widget.AlterDialogView;

import java.util.HashMap;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import doctor.kmwlyy.com.recipe.RecipeActivity;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;

/**
 * @Description描述: 远程会诊
 * @Author作者: zhaoqile
 * @Date日期: 2017/1/3
 */
public class MeetingRoomActivity extends BaseHandlerActivity {


    public final static String EXTRA_AGORA_BEAN = "EXTRA_AGORA_BEAN";

    private Context mContex = MeetingRoomActivity.this;


    private ArrayList<SurfaceView> smallViewList = new ArrayList<>();

    private FrameLayout root;//根
    private RelativeLayout rl_big_view;//大图控件
    /* 小图容器 */
    private LinearLayout ll_scroll;//scrollview内的LinearLayout
    private HorizontalScrollView scroll_small_view;
    private RelativeLayout rl_small0,rl_small1,rl_small2,rl_small3,rl_small4,rl_small5;
    private TextView tv_small0,tv_small1,tv_small2,tv_small3,tv_small4,tv_small5;


    TextView tv_big;

    private int bigViewUid = -1;
    private SurfaceView localSurfaceView;


    private MeetingRoomBean meetingBean;

    private AlertDialog alertDialog;

    private RtcEngine rtcEngine;

    private boolean  isPause = false;


    private HashMap<Integer,RelativeLayout>  uidView = new HashMap<>();
    private HashMap<Integer,TextView>  uidTextView = new HashMap<>();
    private HashMap<Integer,String>  uidName = new HashMap<>();
    private HashMap<Integer,SurfaceView>  uidSurfaceView = new HashMap<>();



    /*
    * Activity 方法
    * */
    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        super.setContentView(R.layout.activity_meeting_room);
        super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// keep screen on - turned on

        meetingBean = (MeetingRoomBean)getIntent().getSerializableExtra(EXTRA_AGORA_BEAN);

        initFindViewById();
        initViews();
        new Thread(mRunnable1).run();

        // check network
        if (!NetworkUtils.isConnectedToNetwork(getApplicationContext())) {
            onError(104);
        }
    }

    private void initFindViewById(){
        ((TextView)findViewById(R.id.tv_center)).setText(R.string.meeting);
        findViewById(R.id.iv_left).setOnClickListener(getViewClickListener());//返回
        findViewById(R.id.tv_left).setOnClickListener(getViewClickListener());//返回
        findViewById(R.id.btn_chat).setOnClickListener(getViewClickListener());//聊天

        root = (FrameLayout)findViewById(R.id.root);
        rl_big_view = (RelativeLayout) findViewById(R.id.rl_big_view);
        ll_scroll = (LinearLayout) findViewById(R.id.ll_scroll);
        scroll_small_view = (HorizontalScrollView) findViewById(R.id.scroll_small_view);
        rl_small0 = (RelativeLayout) findViewById(R.id.rl_small0);
        rl_small1 = (RelativeLayout) findViewById(R.id.rl_small1);
        rl_small2 = (RelativeLayout) findViewById(R.id.rl_small2);
        rl_small3 = (RelativeLayout) findViewById(R.id.rl_small3);
        rl_small4 = (RelativeLayout) findViewById(R.id.rl_small4);
        rl_small5 = (RelativeLayout) findViewById(R.id.rl_small5);
        tv_small0 = (TextView) findViewById(R.id.tv_small0);
        tv_small1 = (TextView) findViewById(R.id.tv_small1);
        tv_small2 = (TextView) findViewById(R.id.tv_small2);
        tv_small3 = (TextView) findViewById(R.id.tv_small3);
        tv_small4 = (TextView) findViewById(R.id.tv_small4);
        tv_small5 = (TextView) findViewById(R.id.tv_small5);
        tv_big = (TextView) findViewById(R.id.tv_big);

    }

    /*
    * 内部方法
    */
    void initViews() {
        uidTextView.put(-1,tv_big);
        uidName.put(-1,"我");
        uidView.put(-1,rl_big_view);
        for (int i = 0 ; i < meetingBean.doctors.size(); i++){
            MeetingDoctor doctor = meetingBean.doctors.get(i);
            uidName.put(doctor.doctorAgoraUid,doctor.doctorName);
            switch (i) {
                case 0:
                    rl_small0.setVisibility(View.VISIBLE);
                    uidView.put(doctor.doctorAgoraUid,rl_small0);
                    uidTextView.put(doctor.doctorAgoraUid,tv_small0);
                    tv_small0.setText(doctor.doctorName);
                    break;
                case 1:
                    rl_small1.setVisibility(View.VISIBLE);
                    uidView.put(doctor.doctorAgoraUid,rl_small1);
                    uidTextView.put(doctor.doctorAgoraUid,tv_small1);
                    tv_small1.setText(doctor.doctorName);
                    break;
                case 2:
                    rl_small2.setVisibility(View.VISIBLE);
                    uidView.put(doctor.doctorAgoraUid,rl_small2);
                    uidTextView.put(doctor.doctorAgoraUid,tv_small2);
                    tv_small2.setText(doctor.doctorName);
                    break;
                case 3:
                    rl_small3.setVisibility(View.VISIBLE);
                    uidView.put(doctor.doctorAgoraUid,rl_small3);
                    uidTextView.put(doctor.doctorAgoraUid,tv_small3);
                    tv_small3.setText(doctor.doctorName);
                    break;
                case 4:
                    rl_small4.setVisibility(View.VISIBLE);
                    uidView.put(doctor.doctorAgoraUid,rl_small4);
                    uidTextView.put(doctor.doctorAgoraUid,tv_small4);
                    tv_small4.setText(doctor.doctorName);
                    break;
                case 5:
                    rl_small5.setVisibility(View.VISIBLE);
                    uidView.put(doctor.doctorAgoraUid,rl_small5);
                    uidTextView.put(doctor.doctorAgoraUid,tv_small5);
                    tv_small5.setText(doctor.doctorName);
                    break;
            }
        }
    }




    void setupRtcEngine() {
        //Test vendor key:  6D7A26A1D3554A54A9F43BE6797FE3E2
        AgoraApplication.getInstance().setRtcEngine(this, meetingBean.mVendorKey);
        rtcEngine = AgoraApplication.getInstance().getRtcEngine();
        rtcEngine.enableVideo();
        ensureLocalViewIsCreated();
        rtcEngine.joinChannel(meetingBean.mVendorKey, meetingBean.mRoomID, ""/*optionalInfo*/, meetingBean.mLocalID/*optionalUid*/);
    }

    void resetBigView(){
        rl_big_view.removeAllViews();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        rl_big_view.addView(uidSurfaceView.get(bigViewUid),params);
    }


    void ensureLocalViewIsCreated() {
        if (localSurfaceView == null){
            localSurfaceView = rtcEngine.CreateRendererView(getApplicationContext());
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            rl_big_view.addView(localSurfaceView,params);
            rtcEngine.setupLocalVideo(new VideoCanvas(localSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, -1));
            uidSurfaceView.put(bigViewUid,localSurfaceView);//相当于uidSurfaceView.put(-1,localSurfaceView);
        }
    }


    void ensureRemoteViewIsCreated(int uid) {
        SurfaceView mRemoteView = rtcEngine.CreateRendererView(getApplicationContext());
        RelativeLayout smallRlView = uidView.get(uid);
        if (smallRlView!=null){
            uidSurfaceView.put(uid,mRemoteView);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(AgoraApplication.getSize(80), AgoraApplication.getSize(100));
            smallRlView.removeAllViews();
            smallRlView.addView(mRemoteView,params);
            TextView tv_name = uidTextView.get(uid);
            tv_name.setText(uidName.get(uid));
            smallRlView.addView(tv_name);
            VideoCanvas vc = new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid);
            rtcEngine.setupRemoteVideo(vc);
            smallRlView.setTag(R.id.rl_big_view,uid);
            smallRlView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        changeBigView(v,Integer.parseInt(v.getTag(R.id.rl_big_view).toString()));
                    }catch (Exception e){}
                }
            });
        }
        resetBigView();

    }

    void changeBigView(View v,int smallUid){
        SurfaceView smallView = uidSurfaceView.get(smallUid);
        SurfaceView bigView =uidSurfaceView.get(bigViewUid);

        RelativeLayout smallRlView = uidView.get(smallUid);
        RelativeLayout bigRlView = uidView.get(bigViewUid);
        smallRlView.removeAllViews();
        bigRlView.removeAllViews();


        RelativeLayout.LayoutParams bigParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams smallParams = new RelativeLayout.LayoutParams(AgoraApplication.getSize(80), AgoraApplication.getSize(100));

        bigRlView.addView(smallView,bigParams);
        smallRlView.addView(bigView,smallParams);

        TextView tv_name = uidTextView.get(smallUid);
        tv_name.setText(uidName.get(bigViewUid));
        smallRlView.addView(tv_name);



        /* 交换 */
        RelativeLayout smallRL = uidView.get(smallUid);
        RelativeLayout bigRL = uidView.get(bigViewUid);
        uidView.remove(smallRL);
        uidView.remove(bigRL);
        uidView.put(bigViewUid,smallRL);
        uidView.put(smallUid,bigRL);


        TextView smallTv = uidTextView.get(smallUid);
        TextView bigTv = uidTextView.get(bigViewUid);
        uidTextView.remove(smallTv);
        uidTextView.remove(bigTv);
        uidTextView.put(smallUid,bigTv);
        uidTextView.put(bigViewUid,smallTv);

//        uidSurfaceView.remove(smallView);
//        uidSurfaceView.remove(bigView);
//        uidSurfaceView.put(bigViewUid,smallView);
//        uidSurfaceView.put(smallUid,bigView);

        v.setTag(R.id.rl_big_view,bigViewUid);
        bigViewUid = smallUid;

        resetBigView();
    }


    @Override
    public void finish() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// keep screen on - turned off
        EventBus.getDefault().post(new ChatActivity.CallFinish());
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPause){
            SurfaceView sv = uidSurfaceView.get(-1);
                rtcEngine.setupLocalVideo(new VideoCanvas(sv, VideoCanvas.RENDER_MODE_HIDDEN, -1));
        }
        isPause = false;
    }

    @Override
    protected void onPause() {
        isPause = true;
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        AlterDialogView.Builder builder = new AlterDialogView.Builder(mContex);
        builder.setMessage(getString(R.string.calling_finish_prompt));
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
                        stopCalling();
                    }
                });
        builder.create().show();
    }

    @Override
    public void onUserInteraction(View view) {
        if (view.getId() == R.id.iv_left || view.getId() == R.id.tv_left) {//退出
            onBackPressed();
        } else if (view.getId() == R.id.btn_chat) {//聊天
            TimApplication.enterTimchat(this, meetingBean.mRoomID, meetingBean.mUserName, true, meetingBean.mRemoteID);
        } else if (view.getId() == R.id.tv_right) {//开处方;
            Intent recipeIntent = new Intent(MeetingRoomActivity.this, RecipeActivity.class);
            recipeIntent.putExtra("id", meetingBean.mRegisterID);
            startActivity(recipeIntent);
        } else {
            super.onUserInteraction(view);
        }
    }







    void stopCalling() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                rtcEngine.leaveChannel();
            }
        }).run();
        finish();
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
                    ToastUtils.showShort(MeetingRoomActivity.this, getString(R.string.auth_failed));
                    stopCalling();
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
    }

    //其他用户加入当前频道回调
    @Override
    public synchronized void onUserJoined(final int uid, int elapsed) {
        log("onUserJoined: uid: " + uid);

        rtcEngine.enableVideo();
        rtcEngine.muteLocalVideoStream(false);
        rtcEngine.muteLocalAudioStream(false);
        rtcEngine.muteAllRemoteVideoStreams(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mConsultBean.mRemoteID = uid;
                // enable video call
            }
        });
    }

    //其他用户离开当前频道回调
    @Override
    public void onUserOffline(final int uid) {
        log("onUserOffline: uid: " + uid);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uid == bigViewUid){
                    changeBigView(uidView.get(-1),-1);
                }
                uidView.get(uid).removeView(uidSurfaceView.get(uid));
                uidView.get(uid).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                uidSurfaceView.remove(uid);
            }
        });


        if (isFinishing()) {
            return;
        }
    }

    //其他用户停止/重启视频回调
    @Override
    public void onUserMuteVideo(final int uid, final boolean muted) {
        log("onUserMuteVideo uid: " + uid + ", muted: " + muted);
        if (isFinishing()) {
            return;
        }

    }

    //远端视频接收解码回调
    @Override
    public synchronized void onFirstRemoteVideoDecoded(final int uid, int width, int height, final int elapsed) {
        log("onFirstRemoteVideoDecoded: uid: " + uid + ", width: " + width + ", height: " + height);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // ensureRemoteViewIsCreated();
                // app hints before you join
//                mConsultBean.mRemoteID = uid;
                ensureRemoteViewIsCreated(uid);

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
                setupRtcEngine();
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


}
