package com.kmwlyy.imchat.page;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.imchat.R;
import com.kmwlyy.imchat.TimApplication;
import com.kmwlyy.imchat.adapter.ChatAdapter;
import com.kmwlyy.imchat.message.CustomMessage;
import com.kmwlyy.imchat.message.FileMessage;
import com.kmwlyy.imchat.message.ImageMessage;
import com.kmwlyy.imchat.message.Message;
import com.kmwlyy.imchat.message.MessageFactory;
import com.kmwlyy.imchat.message.TextMessage;
import com.kmwlyy.imchat.message.VideoMessage;
import com.kmwlyy.imchat.message.VoiceMessage;
import com.kmwlyy.imchat.model.Chat_EventApi;
import com.kmwlyy.imchat.model.ReceiveImgEvent;
import com.kmwlyy.imchat.utils.FileUtil;
import com.kmwlyy.imchat.utils.MediaUtil;
import com.kmwlyy.imchat.utils.RecorderUtil;
import com.kmwlyy.imchat.view.ChatInput;
import com.kmwlyy.imchat.view.TemplateTitle;
import com.kmwlyy.imchat.view.VoiceSendingView;
import com.networkbench.agent.impl.NBSAppAgent;
import com.tencent.TIMConversationType;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageStatus;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import doctor.kmwlyy.com.recipe.RecipeActivity;

public class ChatActivity extends FragmentActivity implements ChatView ,CustomMessage.MyKBSClickListener{

    private static final String TAG = "ChatActivity";

    public final static int CALLING_TYPE_VIDEO = 0x100; //100
    public final static int CALLING_TYPE_VOICE = 0x101; //101
    public final static int CALLING_TYPE_VIDEO_CHAT = 103; //聊天界面小屏模式

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int IMAGE_STORE = 200;
    private static final int FILE_CODE = 300;
    private static final int IMAGE_PREVIEW = 400;

    private List<Message> messageList = new ArrayList<>();
    private ChatAdapter adapter;

    private ListView listView;
    private ChatInput input;
    private VoiceSendingView voiceSendingView;
    private RelativeLayout videoContainer;
    private Boolean updateTime = true;
    private Boolean isStart = true;
    private Thread mThread;
    private Context mContext;

    private Uri fileUri;
    private String identify;
    private String titleStr;
    private TIMConversationType type;

    private boolean cameraIsUse = false;//摄像头是否正在被占用

    private ChatPresenter presenter;
    private Handler handler = new Handler();
    private RecorderUtil recorder = new RecorderUtil();


    public static void navToChat(Context context, String identify, TIMConversationType type) {
        navToChat(context, identify, "", true, type, false, 0);
    }

    public static void navToChat(Context context, String identify, String name, boolean isChat, TIMConversationType type, boolean show, int showid) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("identify", identify);
        intent.putExtra("name", name);
        intent.putExtra("type", type);
        intent.putExtra("ischat", isChat);
        intent.putExtra("show", show);
        intent.putExtra("showid", showid);
        context.startActivity(intent);
    }

    public static void navToChat(Context context, String identify, String name, boolean isChat, TIMConversationType type, boolean show, int showid,int isVedio,int time,String registerID, boolean cameraIsUse) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("identify", identify);
        intent.putExtra("name", name);
        intent.putExtra("type", type);
        intent.putExtra("ischat", isChat);
        intent.putExtra("show", show);
        intent.putExtra("showid", showid);
        intent.putExtra("isVedio", isVedio);
        intent.putExtra("time", time);
        intent.putExtra("registerID", registerID);
        intent.putExtra("cameraIsUse", cameraIsUse);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        NBSAppAgent.onEvent("诊室-进入聊天");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        EventBus.getDefault().register(this);

        mContext = ChatActivity.this;
        identify = getIntent().getStringExtra("identify");
        type = (TIMConversationType) getIntent().getSerializableExtra("type");

        cameraIsUse = getIntent().getBooleanExtra("cameraIsUse",false);

        View closeNotify = findViewById(R.id.close_notify);
        presenter = new ChatPresenter(this, identify, type);
        input = (ChatInput) findViewById(R.id.input_panel);
        input.setChatView(this);
        input.setTakePhotoEnable(cameraIsUse);
        if (!getIntent().getBooleanExtra("ischat", true)) {
            input.setVisibility(View.GONE);
            closeNotify.setVisibility(View.VISIBLE);
        }else{
            input.setVisibility(View.VISIBLE);
            closeNotify.setVisibility(View.GONE);
        }

        adapter = new ChatAdapter(this, R.layout.item_message, messageList);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        input.setInputMode(ChatInput.InputMode.NONE);
                        break;
                }
                return false;
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int firstItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && firstItem == 0) {
                    //如果拉到顶端读取更多消息
                    presenter.getMessage(messageList.size() > 0 ? messageList.get(0).getMessage() : null);

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstItem = firstVisibleItem;
            }
        });
        registerForContextMenu(listView);
        final TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
        switch (type) {
            case C2C:
//                title.setMoreImg(R.drawable.btn_person);
//                if (FriendshipInfo.getInstance().isFriend(identify)) {
//                    title.setMoreImgAction(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
//                            intent.putExtra("identify", identify);
//                            startActivity(intent);
//                        }
//                    });
//                    FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
//                    title.setTitleText(titleStr = profile == null ? identify : profile.getName());
//                } else {
//                    title.setMoreImgAction(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent person = new Intent(ChatActivity.this, AddFriendActivity.class);
//                            person.putExtra("id", identify);
//                            person.putExtra("name", identify);
//                            startActivity(person);
//                        }
//                    });
//                    title.setTitleText(titleStr = identify);
//                }
                break;
            case Group:
//                title.setMoreImg(R.drawable.btn_group);
//                title.setMoreImgAction(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(ChatActivity.this, GroupProfileActivity.class);
//                        intent.putExtra("identify", identify);
//                        startActivity(intent);
//                    }
//                });
//                TIMGroupManager.getInstance().getGroupDetailInfo(Collections.singletonList(identify), new TIMValueCallBack<List<TIMGroupDetailInfo>>() {
//                    @Override
//                    public void onError(int i, String s) {
//                        title.setTitleText(titleStr = identify);
//                    }
//
//                    @Override
//                    public void onSuccess(List<TIMGroupDetailInfo> timGroupDetailInfos) {
//                        if (timGroupDetailInfos != null && timGroupDetailInfos.size() > 0) {
//                            title.setTitleText(titleStr = timGroupDetailInfos.get(0).getGroupName());
//                        }
//                    }
//                });
                title.setTitleText(getIntent().getStringExtra("name"));
                //title.setTitleText(titleStr = GroupInfo.getInstance().getGroupName(identify));//= identify);
                break;

        }
        voiceSendingView = (VoiceSendingView) findViewById(R.id.voice_sending);
        videoContainer = (RelativeLayout) findViewById(R.id.user_local_view);
        ensureLocalViewIsCreated(getIntent().getIntExtra("showid", -1));
        presenter.start();
    }

    void ensureLocalViewIsCreated(int showId) {
        if (getIntent().getBooleanExtra("show", false)) {
            findViewById(R.id.ll_voice_cotainer).setVisibility(View.INVISIBLE);
            videoContainer.removeAllViews();
            if(getIntent().getIntExtra("isVedio",0) == CALLING_TYPE_VIDEO && showId != -2){  //-1表示本地视频，-2表示双方都关闭了摄像机，其它表示对方的视频号
                //视频问诊，跳到聊天界面，加载视频小界面
                TimApplication.getInstance().getAddAgoraListener().addAgora(videoContainer, showId, CALLING_TYPE_VIDEO_CHAT);
            }else if(getIntent().getIntExtra("isVedio",0) == CALLING_TYPE_VOICE || showId == -2){
                //语音问诊，跳到聊天界面，加载时间显示小界面
                final RelativeLayout container = (RelativeLayout) findViewById(R.id.ll_voice_scope);
                final LinearLayout ll_voice_cotainer = (LinearLayout) findViewById(R.id.ll_voice_cotainer);
                ll_voice_cotainer.setVisibility(View.VISIBLE);
                ll_voice_cotainer.setOnTouchListener(new View.OnTouchListener() {
                    private int startx;
                    private int starty;

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        int screenHeight = container.getMeasuredHeight();
                        int screenWidth = container.getMeasuredWidth();
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:// 获取手指第一次接触屏幕
                                startx = (int) event.getRawX();
                                starty = (int) event.getRawY();
                                updateTime = false;
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
                                // 记录最后图片在窗体的位置

                                // 每次移动都要设置其layout，不然由于父布局可能嵌套listview，当父布局发生改变冲毁（如下拉刷新时）则移动的view会回到原来的位置
                                RelativeLayout.LayoutParams newPos = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                newPos.leftMargin = v.getLeft();
                                newPos.topMargin = v.getTop();
                                newPos.width = ll_voice_cotainer.getWidth();
                                newPos.height = ll_voice_cotainer.getHeight();
                                newPos.setMargins(v.getLeft(), v.getTop(), 0, 0);
                                v.setLayoutParams(newPos);
                                updateTime = true;
                                break;
                        }
                        return true;
                    }
                });
                if(mThread == null){
                    mThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int time = getIntent().getIntExtra("time",0);
                            String timeStr = "";
                            try {
                                while(isStart){
                                    if (time >= 3600) {
                                        timeStr = String.format("%d:%02d:%02d", time / 3600, (time % 3600) / 60, (time % 60));
                                    } else {
                                        timeStr = String.format("%02d:%02d", (time % 3600) / 60, (time % 60));
                                    }
                                    Chat_EventApi.updateTime bean = new Chat_EventApi.updateTime();
                                    bean.setTimeStr(timeStr);
                                    EventBus.getDefault().post(bean);
                                    Thread.sleep(1000);
                                    time++;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                isStart = false;
                            }
                        }
                    });
                    mThread.start();
                }
            }else {
                //do nothing
            }

            //开处方功能
            final String id = getIntent().getStringExtra("registerID");
            if(!TextUtils.isEmpty(id)){
                TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
                title.setMoreTextContext(getResources().getString(R.string.chat_recipe));
                title.setMoreTextColor(getResources().getColor(R.color.color_main_green));
                title.setMoreTextAction(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //开处方;
                        Intent recipeIntent = new Intent(mContext, RecipeActivity.class);
                        recipeIntent.putExtra("id",id );
                        startActivity(recipeIntent);
                    }
                });
            }
        }
    }

    /**
     * 修改时间
     * @param bean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTime(Chat_EventApi.updateTime bean){
        if(updateTime){
            ((TextView)findViewById(R.id.tv_time)).setText(bean.getTimeStr());
        }
    }

    @Override
    public void kbsClickListener(String msg) {
        Message message = new TextMessage(msg);
        presenter.sendMessage(message.getMessage());
    }

    public static class CallFinish {
    }
    public static class ClickWindow {
    }
    public static class UpdateWindow {
        public int showRemote;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCallFinish(CallFinish event) {
        finish();
        isStart = false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClickWindow(ClickWindow event) {//点击窗口，返回视频界面
        LogUtils.i(TAG,"点击窗口，返回视频界面");
        finish();
        isStart = false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateWindow(final UpdateWindow event) {//更新SurfaceView
        LogUtils.i(TAG,"更新SurfaceView");
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ensureLocalViewIsCreated(event.showRemote);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveImg(ReceiveImgEvent event) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                listView.setSelection(adapter.getCount() - 1);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.readMessages();
        MediaUtil.getInstance().stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        presenter.stop();
        isStart = false;
    }


    /**
     * 显示消息
     *
     * @param message
     */
    @Override
    public void showMessage(TIMMessage message) {
        if (message == null) {
            adapter.notifyDataSetChanged();
        } else {
            Message mMessage = MessageFactory.getMessage(message);
            if (mMessage != null) {
                if (mMessage instanceof CustomMessage) {
                    if (((CustomMessage) mMessage).getType() == CustomMessage.Type.TYPING) {
                        TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
                        title.setTitleText(getString(R.string.chat_typing));
                        handler.removeCallbacks(resetTitle);
                        handler.postDelayed(resetTitle, 3000);
                    } else if (CustomMessage.EXT_SURVEYQUESTION.equals(((CustomMessage) mMessage).getExt())){
                        ((CustomMessage)mMessage).setMyKBSClickListener(this);
                    }

                }
                    if (messageList.size() == 0) {
                        mMessage.setHasTime(null);
                    } else {
                        mMessage.setHasTime(messageList.get(messageList.size() - 1).getMessage());
                    }
                    messageList.add(mMessage);
                    adapter.notifyDataSetChanged();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            listView.setSelection(adapter.getCount() - 1);
                        }
                    });
                }

        }

    }

    /**
     * 显示消息
     *
     * @param messages
     */
    @Override
    public void showMessage(List<TIMMessage> messages) {
        int newMsgNum = 0;
        for (int i = 0; i < messages.size(); ++i) {
            Message mMessage = MessageFactory.getMessage(messages.get(i));
            if (mMessage == null || messages.get(i).status() == TIMMessageStatus.HasDeleted)
                continue;
            if (mMessage instanceof CustomMessage){
                if (CustomMessage.EXT_SURVEYQUESTION.equals(((CustomMessage) mMessage).getExt())){
                    ((CustomMessage) mMessage).setMyKBSClickListener(this);
                }else{
                    continue;
                }

            }
//                continue; //&& (((CustomMessage) mMessage).getType() == CustomMessage.Type.TYPING || ((CustomMessage) mMessage).getType() == CustomMessage.Type.INVALID)) continue;
            ++newMsgNum;
            if (i != messages.size() - 1) {

                mMessage.setHasTime(messages.get(i + 1));
                messageList.add(0, mMessage);
            } else {
                messageList.add(0, mMessage);
            }
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(newMsgNum);
    }

    /**
     * 清除所有消息，等待刷新
     */
    @Override
    public void clearAllMessage() {
        messageList.clear();
    }


    /**
     * 发送消息成功
     *
     * @param message 返回的消息
     */
    @Override
    public void onSendMessageSuccess(TIMMessage message) {
        showMessage(message);
    }

    /**
     * 发送消息失败
     *
     * @param code 返回码
     * @param desc 返回描述
     */
    @Override
    public void onSendMessageFail(int code, String desc, TIMMessage message) {
        long id = message.getMsgUniqueId();
        for (Message msg : messageList) {
            if (msg.getMessage().getMsgUniqueId() == id) {
                switch (code) {
                    case 80001:
                        //发送内容包含敏感词
                        msg.setDesc(getString(R.string.chat_content_bad));
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        }

    }

    /**
     * 发送文本消息
     */
    @Override
    public void sendText() {
        Message message = new TextMessage(input.getText());
        presenter.sendMessage(message.getMessage());
        input.setText("");
    }

    /**
     * 发送图片消息
     */
    @Override
    public void sendImage() {
        Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
        intent_album.setType("image/*");
        startActivityForResult(intent_album, IMAGE_STORE);
    }

    /**
     * 发送照片消息
     */
    @Override
    public void sendPhoto() {
        Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent_photo.resolveActivity(getPackageManager()) != null) {
            File tempFile = FileUtil.getTempFile(FileUtil.FileType.IMG);
            if (tempFile != null) {
                fileUri = Uri.fromFile(tempFile);
            }
            intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent_photo, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    /**
     * 发送文件
     */
    @Override
    public void sendFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_CODE);
    }

    private void sendFile(String path) {
        if (path == null) return;
        File file = new File(path);
        if (file.exists()) {
            if (file.length() > 1024 * 1024 * 10) {
                Toast.makeText(this, getString(R.string.chat_file_too_large), Toast.LENGTH_SHORT).show();
            } else {
                Message message = new FileMessage(path);
                presenter.sendMessage(message.getMessage());
            }
        } else {
            Toast.makeText(this, getString(R.string.chat_file_not_exist), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 开始发送语音消息
     */
    @Override
    public void startSendVoice() {
        voiceSendingView.setVisibility(View.VISIBLE);
        voiceSendingView.showRecording();
        recorder.startRecording();

    }

    /**
     * 结束发送语音消息
     */
    @Override
    public void endSendVoice() {
        voiceSendingView.release();
        voiceSendingView.setVisibility(View.GONE);
        recorder.stopRecording();
        if (recorder.getTimeInterval() < 1) {
            Toast.makeText(this, getResources().getString(R.string.chat_audio_too_short), Toast.LENGTH_SHORT).show();
        } else {
            Message message = new VoiceMessage(recorder.getTimeInterval(), recorder.getFilePath());
            presenter.sendMessage(message.getMessage());
        }
    }

    /**
     * 发送小视频消息
     *
     * @param fileName 文件名
     */
    @Override
    public void sendVideo(String fileName) {
        Message message = new VideoMessage(fileName);
        presenter.sendMessage(message.getMessage());
    }

    /**
     * 结束发送语音消息
     */
    @Override
    public void cancelSendVoice() {

    }

    /**
     * 正在发送
     */
    @Override
    public void sending() {
        if (type == TIMConversationType.C2C) {
            Message message = new CustomMessage(CustomMessage.Type.TYPING);
            presenter.sendOnlineMessage(message.getMessage());
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Message message = messageList.get(info.position);
        menu.add(0, 1, Menu.NONE, getString(R.string.chat_del));
        if (message.isSendFail()) {
            menu.add(0, 2, Menu.NONE, getString(R.string.chat_resend));
        }
        if (message instanceof ImageMessage || message instanceof FileMessage) {
            menu.add(0, 3, Menu.NONE, getString(R.string.chat_save));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Message message = messageList.get(info.position);
        switch (item.getItemId()) {
            case 1:
                message.remove();
                messageList.remove(info.position);
                adapter.notifyDataSetChanged();
                break;
            case 2:
                messageList.remove(message);
                presenter.sendMessage(message.getMessage());
                break;
            case 3:
                message.save();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK && fileUri != null) {
                showImagePreview(fileUri.getPath());
            }
        } else if (requestCode == IMAGE_STORE) {
            if (resultCode == RESULT_OK && data != null) {
                showImagePreview(FileUtil.getImageFilePath(this, data.getData()));
            }

        } else if (requestCode == FILE_CODE) {
            if (resultCode == RESULT_OK) {
                sendFile(FileUtil.getFilePath(this, data.getData()));
            }
        } else if (requestCode == IMAGE_PREVIEW) {
            if (resultCode == RESULT_OK) {
                boolean isOri = data.getBooleanExtra("isOri", false);
                String path = data.getStringExtra("path");
                File file = new File(path);
                if (file.exists() && file.length() > 0) {
                    if (file.length() > 1024 * 1024 * 10) {
                        Toast.makeText(this, getString(R.string.chat_file_too_large), Toast.LENGTH_SHORT).show();
                    } else {
                        Message message = new ImageMessage(path, isOri);
                        presenter.sendMessage(message.getMessage());
                    }
                } else {
                    Toast.makeText(this, getString(R.string.chat_file_not_exist), Toast.LENGTH_SHORT).show();
                }
            }
        }

    }


    private void showImagePreview(String path) {
        if (path == null) return;
        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra("path", path);
        startActivityForResult(intent, IMAGE_PREVIEW);
    }

    /**
     * 将标题设置为对象名称
     */
    private Runnable resetTitle = new Runnable() {
        @Override
        public void run() {
            TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
            title.setTitleText(titleStr);
        }
    };
}
