package com.kmwlyy.patient.kdoctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.patient.MainActivity;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.kdoctor.activity.AiChatActivity2;
import com.kmwlyy.patient.kdoctor.activity.NearbyHospitalActivity;
import com.kmwlyy.patient.kdoctor.bean.AiChatMessage;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.kmwlyy.patient.kdoctor.bean.AiChatMessage.MUSIC;
import static com.kmwlyy.patient.kdoctor.bean.AiChatMessage.SEND;


/**
 * @Description描述:
 * @Author作者: hx
 * @Date日期: 2016/11/24
 */

public class AichatAdapter2<T> extends BaseAdapter {
    private List<AiChatMessage> datas;
    private View view;
    private ViewHolder viewHolder;
    private MediaPlayer mediaPlayer;
    private AnimationDrawable frameAnimatio;

    public AichatAdapter2(List<T> datas) {
        this.datas = (List<AiChatMessage>) datas;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public T getItem(int position) {
        return (T) datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ai_chat2, null);
            viewHolder = new ViewHolder();
            viewHolder.leftMessage = (WebView) view.findViewById(R.id.leftMessage);
            viewHolder.rightMessage = (TextView) view.findViewById(R.id.rightMessage);
            viewHolder.leftPanel = (RelativeLayout) view.findViewById(R.id.leftPanel);
            viewHolder.rightPanel = (RelativeLayout) view.findViewById(R.id.rightPanel);
            viewHolder.sending = (ProgressBar) view.findViewById(R.id.sending);
            viewHolder.error = (ImageView) view.findViewById(R.id.sendError);
            viewHolder.sender = (TextView) view.findViewById(R.id.sender);
            viewHolder.rightDesc = (TextView) view.findViewById(R.id.rightDesc);
            viewHolder.leftDesc = (TextView) view.findViewById(R.id.leftDesc);
            viewHolder.systemMessage = (TextView) view.findViewById(R.id.systemMessage);
            viewHolder.mRightAvatar = (ImageView) view.findViewById(R.id.rightAvatar);
            viewHolder.iv_music = (ImageView) view.findViewById(R.id.iv_music);
            view.setTag(viewHolder);
        }
        AiChatMessage aiChatMessage = datas.get(position);
        //加载用户头像
        ImageLoader.getInstance().displayImage(BaseApplication.getInstance().getUserData().PhotoUrl, viewHolder.mRightAvatar, LibUtils.getCircleDisplayOptions(R.drawable.default_avatar_patient));

        viewHolder.systemMessage.setVisibility((aiChatMessage.hasTime && !TextUtils.isEmpty(aiChatMessage.getTime())) ? View.VISIBLE : View.GONE);

        viewHolder.systemMessage.setText(aiChatMessage.getTime() == null ? "" : new SimpleDateFormat("MM-dd EEEE HH:mm").format(Long.valueOf(aiChatMessage.getTime())));

        if (aiChatMessage.getType() == MUSIC) {
            showMusicMessage(viewHolder, parent.getContext(), aiChatMessage);
        } else if (aiChatMessage.getType() == SEND) {
            showSendMessage(viewHolder, aiChatMessage);
        } else {
            showReceivedMessage(position, viewHolder, aiChatMessage, parent.getContext());
        }
        return view;
    }

    /*显示音乐信息*/
    private void showMusicMessage(final ViewHolder viewHolder, final Context context, final AiChatMessage message) {
        viewHolder.leftPanel.setVisibility(View.VISIBLE);
        viewHolder.iv_music.setVisibility(View.VISIBLE);
        viewHolder.leftDesc.setVisibility(View.VISIBLE);
        viewHolder.rightPanel.setVisibility(View.GONE);
        viewHolder.leftMessage.setVisibility(View.GONE);

        if (message.getMusicTime() == null) {
            MediaPlayer m = MediaPlayer.create(context, message.getMusicId());
            message.setMusicTime(m.getDuration() / 1000 + "s");
            m.release();
        }
        viewHolder.leftDesc.setText(message.getMusicTime());

        viewHolder.iv_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMusic();
                playMusic(message.getMusicId(), viewHolder.iv_music, context);
            }
        });
        if (message.startPlayMusic) {
            message.startPlayMusic = false;
            playMusic(message.getMusicId(), viewHolder.iv_music, context);
        }
    }

    /*播放音乐*/
    private void playMusic(int MusicId, ImageView imageView, Context context) {
        mediaPlayer = MediaPlayer.create(context, MusicId);
        frameAnimatio = (AnimationDrawable) imageView.getBackground();
        mediaPlayer.start();
        frameAnimatio.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                frameAnimatio.stop();
                frameAnimatio.selectDrawable(0);
            }
        });
    }

    /*停止播放音乐*/
    public void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if (frameAnimatio != null) {
            frameAnimatio.stop();
            frameAnimatio.selectDrawable(0);
        }
    }


    /*显示接收到的*/
    private void showReceivedMessage(final int position, final ViewHolder viewHolder, AiChatMessage aiChatMessage, final Context context) {
        viewHolder.leftPanel.setVisibility(View.VISIBLE);
        viewHolder.leftMessage.setVisibility(View.VISIBLE);
        viewHolder.rightPanel.setVisibility(View.GONE);
        viewHolder.iv_music.setVisibility(View.GONE);
        viewHolder.leftDesc.setVisibility(View.GONE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(aiChatMessage.getWebViewWidth(), aiChatMessage.getWebViewHeight());
        viewHolder.leftMessage.setLayoutParams(params);
        viewHolder.leftMessage.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    url = URLDecoder.decode(url, "UTF-8");
                    String str = url.substring(url.lastIndexOf("=") + 1);
                    if (url.contains("flag=咨询医生")) {
//                        jumpToConsult(context);
                        return true;
                    }
                    if (url.contains("flag=预约挂号") && url.contains("id=") && canRegister(context)) {
//                        Intent intent = new Intent(context, ActivityRegisterDoctorInHospital.class);
//                        intent.putExtra("UNIT_ID", url.substring(url.indexOf("id=") + 3, url.indexOf("&hospitalName=")));
//                        intent.putExtra("UNIT_NAME", str);
//                        context.startActivity(intent);
                        return true;
                    } else if (url.contains("flag=预约挂号") && canRegister(context)) {
//                        context.startActivity(new Intent(context, ActivityRegisterHospitalInCity.class));
                        return true;
                    }
                    if (url.contains("flag=telephone")) {
                        BatDialogs.isCallDialog(context, str);
                        return true;
                    }
                    if (url.contains("flag=address")) {
                        ((AiChatActivity2) context).jumpMap(str, BatLocationUtils.latitude,BatLocationUtils.longitude);
                        return true;
                    }
                    if (url.contains("flag=周边医院")) {
                        NearbyHospitalActivity.jumpThiPage(context);
                        return true;
                    }
                    if (!url.contains("resultLength=-1")) {
                        datas.add(new AiChatMessage(SEND, str, AiChatMessage.SENDING));
                    }
                    ((AiChatActivity2) context).getAutoAnswerList(datas.size() - 1, url);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return true;
            }

        });
        viewHolder.leftMessage.loadDataWithBaseURL(null, aiChatMessage.getContent(), "text/html", "utf-8", null);
    }

    /**
     * 是否可以挂号
     *
     * @return
     */
    private boolean canRegister(Context context) {
//        if (BaseApplication.CAN_REGISTER) {
//            return true;
//        } else {
//            ToastUtil.show(context, "您好，预约挂号功能升级中，请稍后再试！");
//            return false;
//        }
        return false;
    }

    /*显示发送的信息*/
    private void showSendMessage(ViewHolder viewHolder, AiChatMessage aiChatMessage) {
        viewHolder.leftPanel.setVisibility(View.GONE);
        viewHolder.rightPanel.setVisibility(View.VISIBLE);
        viewHolder.rightMessage.setText(aiChatMessage.getContent());
        switch (aiChatMessage.getState()) {
            case AiChatMessage.SENDING:
                viewHolder.sending.setVisibility(View.VISIBLE);
                viewHolder.error.setVisibility(View.GONE);
                break;
            case AiChatMessage.SUCCEED:
                viewHolder.sending.setVisibility(View.GONE);
                viewHolder.error.setVisibility(View.GONE);
                break;
            case AiChatMessage.FAILED:
                viewHolder.sending.setVisibility(View.GONE);
                viewHolder.error.setVisibility(View.VISIBLE);
                break;

        }
    }

    public class ViewHolder {
        public WebView leftMessage;
        public TextView rightMessage;
        public RelativeLayout leftPanel;
        public RelativeLayout rightPanel;
        public ProgressBar sending;
        public ImageView error;
        public TextView sender;
        public TextView systemMessage;
        public TextView rightDesc;
        public TextView leftDesc;
        public ImageView mRightAvatar;
        public ImageView iv_music;
    }

    public void jumpToConsult(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
//        intent.putExtra(ContainerActivity.DATA_FRAGMENT, ContainerActivity.JUMP_CONSULT);
//        intent.putExtra(UserActionManager.MODULENAME, "咨询-咨询医生");
//        intent.putExtra(UserActionManager.STARTTIME, CommonUtils.getTime());
//        intent.putExtra(UserActionManager.MODULEID, 3);
        intent.putExtra("showitem", 3);
        context.startActivity(intent);
        ((Activity) context).finish();
    }
}
