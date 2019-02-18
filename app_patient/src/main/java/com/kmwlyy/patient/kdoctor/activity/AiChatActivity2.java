package com.kmwlyy.patient.kdoctor.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;
import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.kdoctor.AichatAdapter2;
import com.kmwlyy.patient.kdoctor.BatDialogs;
import com.kmwlyy.patient.kdoctor.BatLocationUtils;
import com.kmwlyy.patient.kdoctor.BottomMenuDialog;
import com.kmwlyy.patient.kdoctor.HotSearchLayout;
import com.kmwlyy.patient.kdoctor.KeyBoardUtils;
import com.kmwlyy.patient.kdoctor.LinearGradientBorderLayout;
import com.kmwlyy.patient.kdoctor.LinearGradientTextView;
import com.kmwlyy.patient.kdoctor.MapUtils;
import com.kmwlyy.patient.kdoctor.SendFeedbackWithStringEvent;
import com.kmwlyy.patient.kdoctor.XunFeiJsonParser;
import com.kmwlyy.patient.kdoctor.bean.AiChatMessage;
import com.kmwlyy.patient.kdoctor.bean.KdoctorReply;
import com.kmwlyy.patient.kdoctor.net.AiDoctorHttpEvent;
import com.kmwlyy.patient.kdoctor.net.BaseConstants;
import com.kmwlyy.patient.kdoctor.permission.OnPermissionCallback;
import com.kmwlyy.patient.kdoctor.permission.PermissionManager;
import com.kmwlyy.patient.kdoctor.permission.PermissionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

import static com.kmwlyy.patient.kdoctor.bean.AiChatMessage.RECEIVED;
import static com.kmwlyy.patient.kdoctor.bean.AiChatMessage.SEND;


/**
 * @Description描述:K博士
 * @Author作者: hx
 * @Date日期: 2016/11/23
 */
public class AiChatActivity2 extends BaseActivity {

    private static final int TYPE_SMART_CONSULT = 1;//智能问诊
    private static final int TYPE_COMMON_DISEASE = 2;//常见疾病
    private static final int TYPE_HISTORICAL_EVALUATION = 3;//历史评估
    @BindView(R.id.input)
    EditText input;
    @BindView(R.id.list)
    ListView list;
    @BindView(R.id.iv_voice)
    ImageView iv_voice;
    @BindView(R.id.iv_cancel_voice)
    ImageView iv_cancel_voice;
    @BindView(R.id.toolbar_title)
    TextView tvTitleBarTitle;
    @BindView(R.id.input_panel)
    LinearLayout input_panel;
    @BindView(R.id.voice_panel)
    LinearLayout voice_panel;
    @BindView(R.id.btn_voice)
    ImageButton btn_voice;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.hotDiscoverLayout)
    HotSearchLayout hotDiscoverLayout;
    @BindView(R.id.frame_message)
    PtrClassicFrameLayout frame_message;

    private List<AiChatMessage> messages;
    private AichatAdapter2 aichatAdapter;
    private final static String TAG = "AiChatActivity";
    private final static int GETAUTOANSWER = 1;
    private final static String CHAT_MESSAGE = "chat";//常聊的内容
    private final static String MUSIC_MESSAGE = "music";//语音显示内容

    float downY = 0;
    float leftY = 0;

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    int ret = 0; // 函数调用返回值

    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音合成对象
    private SpeechSynthesizer mTts;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private boolean isVoiceSend;//是否为语音发送

    private int maxWords;//显示的最大字数

    private final static int[] MUSIC_ID = new int[]{R.raw.k_music1, R.raw.k_music2,
            R.raw.k_music3, R.raw.k_music4, R.raw.k_music5};

    private long currentShowTime = 0;//当前显示信息的时间
    private int loadHisMessageTiem = 1;//加载聊天记录的次数
    private boolean isFristSend = true;//是否为首次发送
    public boolean salutatory = true;//是否为欢迎词
    private int newSaveCount = 0;//新保存的信息数量

    public static final int GATHER_INFORMATION = 1;//首次签约成功，收集信息
    public static final int FOLLOW_UP = 2;//随访的
    public static final String PAGE_TYPE = "page_type";//页面功能类型
    public static final String SESSION_ID = "sessionId";//会话唯一标识ID
    private int page_type = 0;
    private String sessionId = "";//会话id
    private String respId = "";//康博士回复的id

    /**
     * 跳转到康博士页面
     * @param context
     * @param pageType
     * @param sessionId 唯一标识ID,首次签约传身份证，随访传sessionID
     */
    public static void jump(Context context, int pageType,String sessionId) {
        Intent intent = new Intent(context, AiChatActivity2.class);
        intent.putExtra(PAGE_TYPE, pageType);
        intent.putExtra(SESSION_ID,sessionId);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.bat_activity_ai_chat;
    }

    @Override
    public void afterBindView() {
        EventBus.getDefault().register(this);
        page_type = getIntent().getIntExtra(PAGE_TYPE, 0);
        initFlashActivity();
        //讯飞初始化
        initXunFei();
        initView();

        //定位
        BatLocationUtils.location(this);
    }

    private void initView() {
        tvTitleBarTitle.setText("康博士");
        btn_voice.setVisibility(View.VISIBLE);
        messages = new ArrayList<>();
        iv_voice.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.FROYO)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        iv_cancel_voice.setVisibility(View.GONE);
                        if (leftY > CommonUtils.dip2px(mContext, 60)) {
                            mIat.cancel();
                        } else {
                            mIat.stopListening();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        leftY = downY - event.getY();
                        if (leftY > CommonUtils.dip2px(mContext, 60)) {//向上滑取消录音，并且不发送
                            iv_cancel_voice.setImageResource(R.mipmap.icon_cancel_voice2);
                        } else {
                            iv_cancel_voice.setImageResource(R.mipmap.icon_cancel_voice);
                        }
                        break;
                }
                return false;
            }
        });
//        addListHead();
        initMessageList();
//        initKeyWordList();
        if (page_type == 0) {
            initChildViews();
        }else {
            voice_panel.setVisibility(View.GONE);
            input_panel.setVisibility(View.GONE);
            btn_voice.setVisibility(View.GONE);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    /*初始化信息列表*/
    private void initMessageList() {
        frame_message.setLoadMoreEnable(false);
        frame_message.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return false;
            }
        });

        salutatory = false;
        list.setAdapter(aichatAdapter = new AichatAdapter2(messages));
        list.setSelection(aichatAdapter.getCount() - 1);
        if (page_type == 0) {
            initFirstSentence();
        } else {
            sessionId = getIntent().getStringExtra(SESSION_ID);
            requestDrKangServe();
        }
    }

    /**
     * 请求康博士服务
     */
    private void requestDrKangServe() {
        AiDoctorHttpEvent aiDoctorHttpEvent = new AiDoctorHttpEvent();
        aiDoctorHttpEvent.requestDrKangServe(page_type == GATHER_INFORMATION ? "followup_firstTime" : "followup", sessionId);
        aiDoctorHttpEvent.setHttpListener(new HttpListener() {
            @Override
            public void onError(int code, String msg) {
            }

            @Override
            public void onSuccess(Object o) {
                try {
                    JSONObject jsonObject = new JSONObject((String) o);
                    sessionId = jsonObject.getJSONObject("resultData").getString("sessionId");
                    String body = jsonObject.getJSONObject("resultData").getString("body");
                    measureContent(body, CHAT_MESSAGE, RECEIVED, true);
                    getReply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        new HttpClient(mContext, aiDoctorHttpEvent, "", null).start();
    }

    /**
     * 获取康博士回复
     */
    private void getReply() {
        AiDoctorHttpEvent aiDoctorHttpEvent = new AiDoctorHttpEvent();
        aiDoctorHttpEvent.pull(sessionId);
        aiDoctorHttpEvent.setHttpListener(new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.show(mContext,"网络异常",0);
            }

            @Override
            public void onSuccess(Object o) {
                KdoctorReply kDoctorReply = new Gson().fromJson((String) o, KdoctorReply.class);
                final KdoctorReply.ResultDataBean bean = kDoctorReply.getResultData();
                respId = bean.getRespId();
                measureContent(bean.getBody(), CHAT_MESSAGE, RECEIVED, false);
                if("completion_num".equals(bean.getAnswerType())){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            input_panel.setVisibility(View.VISIBLE);
                            input.requestFocus();
                            InputMethodManager imm = (InputMethodManager) input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

                        }
                    },2000);
                }else {
                    input_panel.setVisibility(View.GONE);
                }
            }
        });
        new HttpClient(mContext, aiDoctorHttpEvent, "", null).start();
    }

    /**
     * 回复康博士
     *
     * @param text 回复内容
     */
    private void pushReply(String text, final int index) {
        updataList();
        AiDoctorHttpEvent aiDoctorHttpEvent = new AiDoctorHttpEvent();
        aiDoctorHttpEvent.push(text, sessionId, respId);
        aiDoctorHttpEvent.setHttpListener(new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                messages.get(index).setState(AiChatMessage.FAILED);
                updataList();
            }

            @Override
            public void onSuccess(Object o) {
                messages.get(index).setState(AiChatMessage.SUCCEED);
                aichatAdapter.notifyDataSetChanged();
                updataList();
                try {
                    JSONObject jsonObject = new JSONObject((String) o);
                    if("hasFinished".equals(jsonObject.optJSONObject("resultData").optString("type"))){
                        BatDialogs.showCloseDialog(AiChatActivity2.this,"感谢您的耐心回复，我们将根据您的身体情况进行全方位评估，如有需要医生会第一时间与您联系。祝您生活愉快！");
                        return;
                    }
                    getReply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        new HttpClient(mContext, aiDoctorHttpEvent, "", null).start();
    }

    private void initFirstSentence() {
        AiDoctorHttpEvent event = new AiDoctorHttpEvent();
        HttpClient httpClient = new HttpClient(mContext, event, "", null);
        event.setHttpListener(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
            }

            @Override
            public void onSuccess(String o) {
                try {
                    JSONObject jsonObject = new JSONObject(o);
                    String jsonStr = jsonObject.optString("resultData");
                    JSONObject jsonFirstSentence = new JSONObject(jsonStr);
                    final JSONArray firstSentences = jsonFirstSentence.optJSONArray("body");
//                    for (int i = 0;i<firstSentences.length();i++){
//                        final int finalI = i;
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                measureContent(firstSentences.optString(finalI), "chat", RECEIVED,true);
//                            }
//                        },finalI==0?0:100);
//                    }
                    measureContent(firstSentences.optString(0), "chat", RECEIVED, true);
                    aichatAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        httpClient.start();
    }

    //底部三种大类型
    private ArrayList<LinearGradientTextView> views = new ArrayList<>();
    private ArrayList<LinearGradientBorderLayout> gradientBorderLayouts = new ArrayList<>();
    private List<TypeBean> TypeBeanList = new ArrayList<>();

    private void initChildViews() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        maxWords = (windowManager.getDefaultDisplay().getWidth() - CommonUtils.dip2px(mContext, 120)) / CommonUtils.sp2px(mContext, 16) * 3 - 5;
        TypeBeanList.add(new TypeBean("智能问诊", TYPE_SMART_CONSULT));
        TypeBeanList.add(new TypeBean("常见疾病", TYPE_COMMON_DISEASE));
        TypeBeanList.add(new TypeBean("历史评估", TYPE_HISTORICAL_EVALUATION));
        hotDiscoverLayout.removeAllViews();
        views.clear();
        gradientBorderLayouts.clear();
        for (int i = 0; i < TypeBeanList.size(); i++) {
            final View view = LayoutInflater.from(mContext).inflate(R.layout.hot_search_gradient_tv, hotDiscoverLayout, false);
            final LinearGradientBorderLayout gradientLayout = (LinearGradientBorderLayout) view.findViewById(R.id.gradient_layout);
            final LinearGradientTextView gradientTextView = (LinearGradientTextView) view.findViewById(R.id.text);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            view.setLayoutParams(lp);
            final TypeBean typeBean = TypeBeanList.get(i);
            gradientTextView.setText(typeBean.getText());
            gradientTextView.setTag(typeBean);
            gradientTextView.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onClick(View v) {
                    gradientTextView.setChecked(true);
                    gradientLayout.setChecked(true);
                    int i = 0;
                    for (LinearGradientTextView textView : views) {
                        boolean isSameView = gradientTextView.getText().equals(textView.getText());
                        if (!isSameView) {
                            textView.setChecked(false);
                            gradientBorderLayouts.get(i).setChecked(false);
                        }
                        i++;
                    }
                    switch (typeBean.getType()) {
                        case TYPE_SMART_CONSULT:
                            messages.add(new AiChatMessage(SEND, "智能问诊", AiChatMessage.SENDING));
                            getAutoAnswerList(messages.size() - 1, BaseConstants.SEARCH_SERVER_URL + "/elasticsearch/DrKang/chatWithDrKang?flag=智能问诊&keyword=智能问诊");
                            break;
                        case TYPE_COMMON_DISEASE:
                            showCommonDisease();
                            break;
                        case TYPE_HISTORICAL_EVALUATION:
                            //// TODO: 2017/7/31 历史评估
                            Intent intent = new Intent();
                            intent.setClass(AiChatActivity2.this, CommonWebPageActivity.class);
                            intent.putExtra("url", BaseConstants.APP_SERVER_URL + "weixin/app/KDoctor/HistoryList?userDeviceId=" + BaseApplication.getInstance().getUniqueID());
                            intent.putExtra("title", "历史评估");
                            AiChatActivity2.this.startActivity(intent);
                            break;
                    }
                }
            });
            hotDiscoverLayout.addView(view);
            views.add(gradientTextView);
            gradientBorderLayouts.add(gradientLayout);
        }
    }

    class TypeBean {
        private String mTextView;
        private int mType;

        public TypeBean(String textView, int type) {
            mTextView = textView;
            mType = type;
        }

        public String getText() {
            return mTextView;
        }

        public void setText(String textView) {
            mTextView = textView;
        }

        public int getType() {
            return mType;
        }

        public void setType(int type) {
            mType = type;
        }
    }

    /*初始化关键词列表*/
    private void initKeyWordList() {
//        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        maxWords = (windowManager.getDefaultDisplay().getWidth() - CommonUtils.dip2px(mContext, 120)) / CommonUtils.sp2px(mContext, 16) * 3 - 5;
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        keyword_list.setLayoutManager(linearLayoutManager);
//        final List<String> keyworList = new ArrayList<>();
//        keyworList.add("高血压");
//        keyworList.add("糖尿病");
//        keyworList.add("心脏病");
//        keyworList.add("肩周炎");
//        keyworList.add("颈椎病");
//        keyworList.add("感冒");
//        AikeyWordAdapter aiKeyWordAdapter = new AikeyWordAdapter(mContext, keyworList);
//        keyword_list.setAdapter(aiKeyWordAdapter);
//        aiKeyWordAdapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                sendMessage(keyworList.get(position));
//            }
//        });

    }

    /*添加聊天列表的头部时间*/
    private void addListHead() {
        LinearLayout l = new LinearLayout(mContext);
        AbsListView.LayoutParams p = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
        l.setLayoutParams(p);
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setTextColor(Color.parseColor("#666666"));
        textView.setTextSize(14);
        textView.setGravity(Gravity.CENTER);
        params.setMargins(0, CommonUtils.dip2px(mContext, 26), 0, CommonUtils.dip2px(mContext, 10));
        textView.setLayoutParams(params);
        currentShowTime = System.currentTimeMillis();
        textView.setText(new SimpleDateFormat("MM-dd EEEE HH:mm").format(currentShowTime));
        l.addView(textView);
        list.addHeaderView(l);
    }

    //引导页
    private void initFlashActivity() {
        //是否第一次进来康博士页面
//        if (SPUtils.getBoolean("isFirstAi", true)) {
//            startActivityForResult(new Intent(mContext, AiGuidePageActivity.class), 10086);
//        }
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            LogUtils.d(TAG, "开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            ToastUtils.show(mContext, error.toString().substring(0, error.toString().indexOf(".")), 0);
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            LogUtils.d(TAG, "结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String text = XunFeiJsonParser.parseIatResult(results.getResultString());
            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }
            if (isLast) {
                String voiceWord = resultBuffer.toString().substring(0, resultBuffer.toString().length());
                isVoiceSend = true;
                sendMessage(voiceWord);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
//            LogUtil.d(TAG, "返回音频数据：" + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    @OnClick({R.id.navigation_btn, R.id.input, R.id.btn_send, R.id.iv_voice, R.id.btn_voice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navigation_btn:
                finish();
                break;
            case R.id.input:
                break;
            case R.id.iv_voice:
                input_panel.setVisibility(View.VISIBLE);
//                keyword_list.setVisibility(View.VISIBLE);
                hotDiscoverLayout.setVisibility(View.VISIBLE);
                voice_panel.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        list.setSelection(aichatAdapter.getCount() - 1);
                    }
                }, 200);
                break;
            case R.id.btn_voice:
                KeyBoardUtils.closeKeybord(input, mContext);
                input_panel.setVisibility(View.GONE);
//                keyword_list.setVisibility(View.GONE);
                hotDiscoverLayout.setVisibility(View.GONE);
                voice_panel.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        list.setSelection(aichatAdapter.getCount() - 1);
                    }
                }, 200);
                break;
            case R.id.btn_send:
                if (!TextUtils.isEmpty(input.getText().toString())) {
                    isVoiceSend = false;
                    sendMessage(input.getText().toString());
                    input.setText("");
                    KeyBoardUtils.closeKeybord(input, AiChatActivity2.this);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SendFeedbackWithStringEvent event) {
        if (!TextUtils.isEmpty(event.getStr())) {
            isVoiceSend = false;
            String str = "";
            // "1" 满意   ；   "0" 不满意
            switch (event.getStr()) {
                case "0":
                    str = "感谢您的反馈,我们将持续优化,为您提供更好的服务.";
                    break;
                case "1":
                    str = "感谢您的使用,您的满意是我们最大的动力.";
                    break;
            }

            measureContent(str, "chat", RECEIVED, false);

            input.setText("");
            KeyBoardUtils.closeKeybord(input, AiChatActivity2.this);
        }
    }


    @OnLongClick({R.id.iv_voice})
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.iv_voice:
                checkPermission();
                break;
        }
        return true;
    }

    /*开始录音*/
    private void startRecord() {
        aichatAdapter.stopMusic();
        if (mTts.isSpeaking()) {
            mTts.stopSpeaking();
        }
        iv_cancel_voice.setVisibility(View.VISIBLE);
        iv_cancel_voice.setImageResource(R.mipmap.icon_cancel_voice);
        // 移动数据分析，收集开始听写事件
        FlowerCollector.onEvent(this, "iat_recognize");

        mIatResults.clear();
        // 不显示听写对话框
        ret = mIat.startListening(mRecognizerListener);
        if (ret != ErrorCode.SUCCESS) {
            ToastUtils.show(mContext, "听写失败,错误码：" + ret, 0);
        } else {
//                    showTip(getString(R.string.text_begin));
        }
    }

    /**
     * 开始语音合成
     *
     * @param text 合成内容
     */
    private void startCompound(String text) {
        // 移动数据分析，收集开始合成事件
        FlowerCollector.onEvent(mContext, "tts_play");

        // 设置参数
        setParam();
        int code = mTts.startSpeaking(text, mTtsListener);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);

        if (code != ErrorCode.SUCCESS) {
        }
    }

    /*判断权限*/
    private void checkPermission() {
        //判断是否有录音的权限
        if (!PermissionUtil.getInstance().checkPermission(this, Manifest.permission.RECORD_AUDIO)) {
            PermissionManager.getInstance().requestPermission(this, new OnPermissionCallback() {
                @Override
                public void onSuccess(String... permissions) {
                }

                @Override
                public void onFail(String... permissions) {
                    BatDialogs.showHintDialog(mContext, "语音输入需要录音权限！");
                }
            }, Manifest.permission.RECORD_AUDIO);
        } else {
            startRecord();
        }
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void initXunFei() {
        SpeechUtility.createUtility(mContext, SpeechConstant.APPID + "=58635bd3");
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(this, mTtsInitListener);
        setParam();

        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        mIat = SpeechRecognizer.createRecognizer(mContext, null);
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
//                startCompound(StringUtils.delHTMLTag(FIRST_SENTENCE));
            }
        }
    };
    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
        }

        @Override
        public void onSpeakPaused() {
        }

        @Override
        public void onSpeakResumed() {
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        @Override
        public void onCompleted(SpeechError error) {
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    /**
     * 语音合成参数设置
     *
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    /**
     * 发送信息
     *
     * @param word 内容
     */
    private void sendMessage(String word) {
        AiChatMessage aiChatMessage = new AiChatMessage(SEND, word, AiChatMessage.SENDING);
        setMessagesTime(aiChatMessage);
        messages.add(aiChatMessage);

        if (page_type != 0) {
            pushReply(word, messages.size() - 1);
            return;
        }
        getAutoAnswerList(word, messages.size() - 1);
    }

    /**
     * 保存信息，并记录新添加到数据库的数量
     *
     * @param aiChatMessage
     */
    private void saveMessage(AiChatMessage aiChatMessage) {
        newSaveCount++;
    }

    /**
     * 设置信息显示时间，4分钟内的不重复显示
     */
    private void setMessagesTime(AiChatMessage aiChatMessage) {
        if (isFristSend && !salutatory) {
            aiChatMessage.hasTime = false;
            aiChatMessage.setTime(currentShowTime + "");
        }
        if (System.currentTimeMillis() - currentShowTime > 60000 * 4) {
            aiChatMessage.hasTime = true;
            currentShowTime = System.currentTimeMillis();
            aiChatMessage.setTime(currentShowTime + "");
        }
    }

    /*刷新聊天列表,并显示最新的消息*/
    private void updataList() {
        aichatAdapter.notifyDataSetChanged();
        list.setSelection(aichatAdapter.getCount() - 1);
    }

    /*获取发送回复*/
    public void getAutoAnswerList(String keyword, final int index) {
        getAutoAnswerList(keyword, index, "");
    }

    /*获取链接回复*/
    public void getAutoAnswerList(final int index, String url) {
        if (page_type != 0) {
            pushReply(url.substring(url.lastIndexOf("=")+1), index);
            return;
        }
        getAutoAnswerList("", index, url);
        isVoiceSend = false;
    }

    /**
     * 获取自动回复信息
     *
     * @param keyword 发送的信息
     * @param index   信息在聊天列表中的位置
     */
    private void getAutoAnswerList(String keyword, final int index, final String url) {
        if (!url.contains("resultLength=-1")) {
            updataList();
        }
        AiDoctorHttpEvent event = new AiDoctorHttpEvent(url, keyword, maxWords + "");
        event.setHttpListener(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                messages.get(index).setState(AiChatMessage.FAILED);
                updataList();
            }

            @Override
            public void onSuccess(String o) {
                try {
                    JSONObject jsonObject = new JSONObject(o);
                    JSONObject resultData = jsonObject.optJSONObject("resultData");
                    final String body = resultData.optString("body");
                    String type = resultData.optString("type");
                    if (url.contains("resultLength=-1")) {
                        showDetailDialog(body);
                        return;
                    }
                    //刷新发送的
                    messages.get(index).setState(AiChatMessage.SUCCEED);
                    saveMessage(messages.get(index));
                    isFristSend = false;
                    if (type.equals("music")) {
                        int MusicId = MUSIC_ID[(int) (Math.random() * MUSIC_ID.length)];
                        AiChatMessage aiChatMessage = new AiChatMessage(AiChatMessage.MUSIC, MusicId, true);
                        setMessagesTime(aiChatMessage);
                        saveMessage(aiChatMessage);
                        messages.add(aiChatMessage);
                        aichatAdapter.stopMusic();
                        updataList();
                        return;
                    }
                    updataList();
                    tvTitleBarTitle.setText("正在输入");
                    measureContent(body, type, RECEIVED, false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        HttpClient httpClient = new HttpClient(mContext, event, "", null);
        httpClient.start();
    }

    /**
     * 测量加载内容需要的webview宽高
     *
     * @param message     内容
     * @param contentType 信息内容类型
     * @param messageType 信息类型 发送或者接收
     */
    private void measureContent(final String message, final String contentType, final int messageType,
                                final boolean isFirstSentence) {

        final WebView temp_wb = new WebView(mContext);
        temp_wb.setVisibility(View.INVISIBLE);
        rootView.addView(temp_wb);

        final TextView textView = new TextView(mContext);
        textView.setVisibility(View.INVISIBLE);
        rootView.addView(textView);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(CommonUtils.dip2px(mContext, 55), 0, CommonUtils.dip2px(mContext, 65), 0);
        temp_wb.setLayoutParams(params);
        textView.setLayoutParams(params);

        textView.setPadding(CommonUtils.dip2px(mContext, 10),
                CommonUtils.dip2px(mContext, 10), CommonUtils.dip2px(mContext, 20)
                , CommonUtils.dip2px(mContext, 10));
        textView.setTextSize(CommonUtils.dip2px(mContext, 14));
        textView.setText(delHTMLTag(message));

        temp_wb.loadDataWithBaseURL(null, message, "text/html", "utf-8", null);
        temp_wb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, String url) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        tvTitleBarTitle.setText("康博士");
                        AiChatMessage aiChatMessage = new AiChatMessage(messageType, message,
                                view.getHeight(), textView.getWidth());
                        setMessagesTime(aiChatMessage);
                        if (!isFirstSentence) {//不是首句才保存
                            saveMessage(aiChatMessage);
                        }
                        messages.add(aiChatMessage);
                        if (isVoiceSend && "chat".equals(contentType)) {
                            startCompound(delHTMLTag(message));
                        } else if (isVoiceSend) {
                            startCompound("这些是我搜集到的信息。");
                        }
                        updataList();
                        rootView.removeView(textView);
                        rootView.removeView(temp_wb);
                    }
                }, isFirstSentence ? 200 : 2000);
                super.onPageFinished(view, url);
            }
        });
    }

    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); //过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); //过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }

    private BottomMenuDialog mBottomMenuDialog;

    /**
     * 跳转到地图导航
     *
     * @param address  目的地
     * @param startlat 开始坐标
     * @param startlon 结束坐标
     */
    public void jumpMap(final String address, final double startlat, final double startlon) {
        mBottomMenuDialog = new BottomMenuDialog.Builder(mContext)
                .setTitle("请选择")
                .addMenu("百度地图", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!MapUtils.isInstallPackage(mContext, "com.baidu.BaiduMap")) {
                            ToastUtils.show(mContext, "未安装百度地图", 0);
                            return;
                        }
                        mBottomMenuDialog.dismiss();


                        //Context mContext, String startAddName, String addInfo, String startlat, String startlon, String endlat, String endlon
                        MapUtils.goToBaiduNaviActivity(mContext, "我的位置", address, startlat, startlon);
                    }
                }).addMenu("高德地图", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!MapUtils.isInstallPackage(mContext, "com.autonavi.minimap")) {
                            ToastUtils.show(mContext, "未安装高德地图", 0);
                            return;
                        }

                        mBottomMenuDialog.dismiss();
                        MapUtils.goToGaodeNaviActivity(mContext, "我的位置", address
                                , startlat, startlon
                        );
                    }
                }).addMenu("腾讯地图", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!MapUtils.isInstallPackage(mContext, "com.tencent.map")) {
                            ToastUtils.show(mContext, "未安装腾讯地图", 0);
                            return;
                        }

                        mBottomMenuDialog.dismiss();
                        MapUtils.goToTencentNaviActivity(mContext, "我的位置", address, startlat, startlon
                        );
                    }
                }).create();
        mBottomMenuDialog.show();

    }

    //常见疾病弹框
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void showCommonDisease() {
        final ArrayList<LinearGradientTextView> views = new ArrayList<>();
        final ArrayList<LinearGradientBorderLayout> gradientBorderLayouts = new ArrayList<>();
        List<String> TypeBeanList = new ArrayList<>();
        TypeBeanList.add("高血压");
        TypeBeanList.add("糖尿病");
        TypeBeanList.add("心脏病");
        TypeBeanList.add("肩周炎");
        TypeBeanList.add("颈椎病");
        TypeBeanList.add("感冒");
        final Dialog dialog = new Dialog(mContext, R.style.DialogTheme);
        View view = LayoutInflater.from(mContext).inflate(R.layout.ai_common_disease_dialog, null);
        HotSearchLayout hotDiscoverLayout = (HotSearchLayout) view.findViewById(R.id.hsl_hotDiscoverLayout_list);
        hotDiscoverLayout.removeAllViews();
        views.clear();
        gradientBorderLayouts.clear();
        for (int i = 0; i < TypeBeanList.size(); i++) {
            final View v = LayoutInflater.from(mContext).inflate(R.layout.hot_search_gradient_tv, hotDiscoverLayout, false);
            final LinearGradientBorderLayout gradientLayout = (LinearGradientBorderLayout) v.findViewById(R.id.gradient_layout);
            final LinearGradientTextView gradientTextView = (LinearGradientTextView) v.findViewById(R.id.text);
            final String strValue = TypeBeanList.get(i);
            gradientTextView.setText(strValue);
            gradientTextView.setTag(strValue);
            gradientTextView.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onClick(View v) {
                    gradientTextView.setChecked(true);
                    gradientLayout.setChecked(true);
                    int i = 0;
                    for (LinearGradientTextView textView : views) {
                        boolean isSameView = gradientTextView.getText().equals(textView.getText());
                        if (!isSameView) {
                            textView.setChecked(false);
                            gradientBorderLayouts.get(i).setChecked(false);
                        }
                        i++;
                    }
                    sendMessage(strValue);
                    dialog.dismiss();
                }
            });
            hotDiscoverLayout.addView(v);
            views.add(gradientTextView);
            gradientBorderLayouts.add(gradientLayout);
        }

        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.Animation_Bottom_Rising);
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = d.getHeight() - CommonUtils.dip2px(mContext, 140);
        window.setAttributes(lp);
        dialog.show();
    }

    //更多详情弹框
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    public void showDetailDialog(String content) {
        final Dialog dialog = new Dialog(mContext, R.style.DialogTheme);
        View view = LayoutInflater.from(mContext).inflate(R.layout.ai_message_detail, null);

        TextView textView = (TextView) view.findViewById(R.id.tv);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setText(content);

        ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.Animation_Bottom_Rising);
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = d.getHeight() - CommonUtils.dip2px(mContext, 140);
        window.setAttributes(lp);

        dialog.show();


    }

    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    @Override
    public void onDestroy() {
        super.onDestroy();
        aichatAdapter.stopMusic();
        // 退出时释放连接
        mIat.cancel();
        mIat.destroy();
        mTts.stopSpeaking();
        mTts.destroy();
        EventBus.getDefault().unregister(this);
    }

}
