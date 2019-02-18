package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ImageUtils;
import com.kmwlyy.doctor.Fragment.QueryChatFragment;
import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.adapter.ConsultListAdapter;
import com.kmwlyy.doctor.model.Constant;
import com.kmwlyy.doctor.model.ConsultBean;
import com.kmwlyy.doctor.model.ConsultBeanNew;
import com.kmwlyy.doctor.model.InfoEvent;
import com.kmwlyy.doctor.model.httpEvent.Http_getTaskList_Event;
import com.kmwlyy.imchat.TimApplication;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.networkbench.agent.impl.NBSAgent;
import com.networkbench.agent.impl.NBSAppAgent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xcj on 2017/7/6.
 */

public class IMConsultListActivity extends BaseActivity implements PageListView.OnPageLoadListener {
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_center)
    TextView tv_title;
    @ViewInject(R.id.tv_right)
    TextView tv_right;

    public static final String TAG = "VideoVoiceChatListActivity";

    @Override
    public void onRefreshData() {
        loadData(true);
    }

    @Override
    public void onLoadPageData() {
        loadData(false);
    }


    private List<ConsultBean> list_consult;
    private PageListView mChatListView;
    private PageListViewHelper<ConsultBeanNew> mPageListViewHelper;
    private ViewGroup mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_voice_chat_list);
        ViewUtils.inject(this);
        mContent = (ViewGroup) findViewById(R.id.content);
        mChatListView = (PageListView) findViewById(R.id.lv_chat);
        tv_title.setText("图文问诊");
        btn_left.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("设置");
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NBSAppAgent.onEvent("服务-图文咨询-设置");
                //跳转到设置页面
                VideoInquirySettingActivity.startVideoInquirySettingActivity(IMConsultListActivity.this, 2, true);
            }
        });
        list_consult = new ArrayList<>();
        mPageListViewHelper = new PageListViewHelper<>(mChatListView, new ConsultAdapter(IMConsultListActivity.this, null));
        mPageListViewHelper.getListView().setDivider(null);
        mPageListViewHelper.setOnPageLoadListener(this);
        mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConsultBeanNew consultBeanNew = mPageListViewHelper.getAdapter().getItem(position);

               /* //0-未筛选、1-未领取、2-未回复、4-已回复、5-已完成
                //如果是已完成，就不让回复。隐藏发送栏
                //                "OrderState": 1, // 订单状态（-1待确认，0待支付，1已支付，2已完成，3已取消）
//                "RoomState": 1, //房间状态（-1 其它，0 未就诊，1 候诊中，2 就诊中，3 已就诊，4 呼叫中，5 离开中，6 断开连接，7重新候诊）   图文(0未就诊，2已回复，3已完成)
                Boolean isChat = true;
                //ConsultState = 0,1,5 这三种的时候,不让发送内容
                if (consultBeanNew.Room.RoomState.equals("5") || consultBeanNew.Room.RoomState.equals("0") || consultBeanNew.Room.RoomState.equals("1")) {
                    isChat = false;
                }
                //还有ChannelId空或者0的时候。
                if (consultBeanNew.Room.ChannelID.equals("0") || consultBeanNew.Room.ChannelID.equals("")) {
                    isChat = false;
                }
                TimApplication.enterTimchat(IMConsultListActivity.this, consultBeanNew.Room.ChannelID, consultBeanNew.Member.MemberName, isChat);
               *//* if(consultBeanNew.Order.getOrderState() == 1){
                    isChat = true;
                }
                TimApplication.enterTimchat(IMConsultListActivity.this, consultBeanNew.Room.ChannelID, consultBeanNew.Member.MemberName, isChat);*//*


*/

                Boolean isChat = false;
//                "OrderState": 1, // 订单状态（-1待确认，0待支付，1已支付，2已完成，3已取消）
//                "RoomState": 1, //房间状态（-1 其它，0 未就诊，1 候诊中，2 就诊中，3 已就诊，4 呼叫中，5 离开中，6 断开连接，7重新候诊）   图文(0未就诊，2已回复，3已完成)
                if(consultBeanNew.Order.getOrderState() == 1){
                    isChat = true;
                }
                TimApplication.enterTimchat(IMConsultListActivity.this, consultBeanNew.Room.ChannelID, consultBeanNew.Member.MemberName, isChat);
            }
        });
        loadData(true);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshData(InfoEvent.SettingSuc refreshData) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 请求数据
     */
    private void loadData(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);
        Http_getTaskList_Event getTaskListEvent = new Http_getTaskList_Event(refresh ? "1" : mPageListViewHelper.getPageIndex() + "", "20", Constant.OPDTYPE_CHAT_SERVICE, new HttpListener<List<ConsultBeanNew>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                }
                mPageListViewHelper.setRefreshing(false);

                EmptyViewUtils.removeAllView(mContent);
                if (refresh) {
                    EmptyViewUtils.showErrorView(mContent, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadData(true);
                        }
                    });
                }
            }

            @Override
            public void onSuccess(List<ConsultBeanNew> datas) {
                if (DebugUtils.debug) {
                }
                mPageListViewHelper.setRefreshing(false);
                if (refresh) {
                    mPageListViewHelper.refreshData(datas);
                    if (datas == null || datas.isEmpty()) {
                        EmptyViewUtils.showEmptyView(mContent, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadData(true);
                            }
                        });
                    }
                } else {
                    mPageListViewHelper.addPageData(datas);
                }
            }
        });

        HttpClient client = NetService.createClient(IMConsultListActivity.this, HttpClient.DOCTOR_API_URL, getTaskListEvent);
        client.start();
    }

    static class ConsultAdapter extends CommonAdapter<ConsultBeanNew> {

        public ConsultAdapter(Context context, List<ConsultBeanNew> datas) {
            super(context, R.layout.item_consult_new, datas);
        }

        @Override
        public void convert(ViewHolder viewHolder, ConsultBeanNew data, int position) {
            TextView patientName = (TextView) viewHolder.findViewById(R.id.tv_name);
            ImageView avatarIV = (ImageView) viewHolder.findViewById(R.id.iv_patient_portrait);
            if (data.Member != null) {
                patientName.setText(data.Member.MemberName);
                ImageLoader.getInstance().displayImage(data.Member.PhotoUrl, avatarIV, ImageUtils.getCircleDisplayOptions());
            } else {
                patientName.setText("");
                ImageLoader.getInstance().displayImage("", avatarIV, ImageUtils.getCacheOptions());
            }

            TextView dateTV = (TextView) viewHolder.findViewById(R.id.consult_date);
            if (!TextUtils.isEmpty(data.OPDDate)) {
                if (data.OPDDate.length() >= 10) {
                    dateTV.setText(data.OPDDate.substring(0, 10));
                } else {
                    dateTV.setText(data.OPDDate);
                }
            } else {
                dateTV.setText("");
            }

            ((TextView) viewHolder.findViewById(R.id.tv_content)).setText("");
            if (data.Messages != null && data.Messages.size() > 0) {
                ConsultBeanNew.Messages messages = data.Messages.get(0);
                if (messages.messageContent != null) {

                    Gson gson = MyApplication.getMyApplication(viewHolder.getConvertView().getContext()).getGson();
                    ConsultBeanNew.MessageContent content = gson.fromJson(messages.messageContent, ConsultBeanNew.MessageContent.class);
                    if (content != null) {
                        //MessageType : TIMSoundElem 声音，TIMImageElem 图片，TIMFileElem 文件，TIMTextElem 文本，TIMFaceElem 表情，TIMCustomElem 自定义
                        if ("TIMSoundElem".equals(content.msgType)) {
                            ((TextView) viewHolder.findViewById(R.id.tv_content)).setText(context.getString(R.string.string_sound));
                        } else if ("TIMImageElem".equals(content.msgType)) {
                            ((TextView) viewHolder.findViewById(R.id.tv_content)).setText(context.getString(R.string.string_image));
                        } else if ("TIMFileElem".equals(content.msgType)) {
                            ((TextView) viewHolder.findViewById(R.id.tv_content)).setText(context.getString(R.string.string_file));
                        } else if ("TIMFaceElem".equals(content.msgType)) {
                            ((TextView) viewHolder.findViewById(R.id.tv_content)).setText(context.getString(R.string.string_face));
                        } else if ("TIMTextElem".equals(content.msgType)) {
                            if (content.msgContent != null) {
                                ((TextView) viewHolder.findViewById(R.id.tv_content)).setText(content.msgContent.text);
                            }
                        } else {
                            if (content.msgContent != null) {
                                ((TextView) viewHolder.findViewById(R.id.tv_content)).setText(content.msgContent.desc);
                            }
                        }

                    }
                }
            }

            if (data.OPDDate != null) {
                ((TextView) viewHolder.findViewById(R.id.consult_date)).setText(data.OPDDate.substring(0, 16).replace("T", " "));
            } else {
                ((TextView) viewHolder.findViewById(R.id.consult_date)).setText("");
            }

            TextView consultStateTV = (TextView) viewHolder.findViewById(R.id.reply_flag);
            int state = Integer.parseInt(data.Room.RoomState);

          /*  if (state > 1) {
                consultStateTV.setText(R.string.string_consult_state1);
                consultStateTV.setTextColor(context.getResources().getColor(R.color.app_color_main));
            } else {
                consultStateTV.setText(R.string.string_consult_state0);
                consultStateTV.setTextColor(context.getResources().getColor(R.color.app_color_string));
            }*/

            int order =data.Order.getOrderState();
//            OrderState=3 已取消
//            OrderState=2 已完成
//            OrderState!=3并OrderState!=2
//            RoomState=0或1  未回复
//            RoomState=2     已回复
            if( order == 3){
                consultStateTV.setText(R.string.string_order_type4);
                consultStateTV.setTextColor(context.getResources().getColor(R.color.color_sub_string));
            }else if(order == 2){
                consultStateTV.setText(R.string.string_consult_state2);
                consultStateTV.setTextColor(context.getResources().getColor(R.color.color_sub_string));
            }else{
                if(state == 0 || state == 1){
                    consultStateTV.setText(R.string.string_consult_state0);
                    consultStateTV.setTextColor(context.getResources().getColor(R.color.app_color_string));
                }
                if(state == 2){
                    consultStateTV.setText(R.string.string_consult_state1);
                    consultStateTV.setTextColor(context.getResources().getColor(R.color.app_color_main));
                }
            }


        }

    }
}
