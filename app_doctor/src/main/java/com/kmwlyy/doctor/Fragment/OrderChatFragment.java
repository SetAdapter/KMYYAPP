package com.kmwlyy.doctor.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ImageUtils;
import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.ConsultListAdapter;
import com.kmwlyy.doctor.model.Constant;
import com.kmwlyy.doctor.model.ConsultBean;
import com.kmwlyy.doctor.model.ConsultBeanNew;
import com.kmwlyy.doctor.model.httpEvent.Http_getTaskList_Event;
import com.kmwlyy.imchat.TimApplication;
import com.kmwlyy.imchat.message.CustomMessage;
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

public class OrderChatFragment extends SearchFragment implements OnClickListener, PageListView.OnPageLoadListener {

    public static final String TAG = "OrderChatFragment";
    public static final int INCLUDE = 0;

    /**
     * 接到推送，刷新列表
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPushData(CustomMessage.reLoadData bean) {
        loadData(true);
    }

    public static class RefreshOrderChatData {
    }

    private ConsultListAdapter consultListAdapter;
    private List<ConsultBean> list_consult;
    private EditText et_symptom;
    private PageListView mChatListView;
    private PageListViewHelper<ConsultBeanNew> mPageListViewHelper;
    private ViewGroup mContent;
    private String OPDType;

    @Override
    public void onRefreshData() {
        if (mUseSearchMode) {
            searchData(true, mSearchKey);
        } else {
            loadData(true);
        }
    }

    @Override
    public void onLoadPageData() {
        if (mUseSearchMode) {
            searchData(false, mSearchKey);
        } else {
            loadData(false);
        }
    }

    @Override
    public void updateSearchKey(String searchKey) {
        super.updateSearchKey(searchKey);
        searchData(true, searchKey);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshData(QueryChatFragment.RefreshOrderChatData refreshOrderChatData) {
        loadData(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_chat, null);
        mChatListView = (PageListView) view.findViewById(R.id.lv_chat);
        mContent = (ViewGroup) view.findViewById(R.id.content);

        list_consult = new ArrayList<>();
        consultListAdapter = new ConsultListAdapter(getActivity(), list_consult);
        mPageListViewHelper = new PageListViewHelper<>(mChatListView, new ConsultAdapter(getActivity(), null));
        mPageListViewHelper.getListView().setDivider(null);
        mPageListViewHelper.setOnPageLoadListener(this);
        mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ConsultBeanNew consultBeanNew = mPageListViewHelper.getAdapter().getItem(position);
                Boolean isChat = false;
//                "OrderState": 1, // 订单状态（-1待确认，0待支付，1已支付，2已完成，3已取消）
//                "RoomState": 1, //房间状态（-1 其它，0 未就诊，1 候诊中，2 就诊中，3 已就诊，4 呼叫中，5 离开中，6 断开连接，7重新候诊）   图文(0未就诊，2已回复，3已完成)
                if(consultBeanNew.Order.getOrderState() == 1){
                    isChat = true;
                }
                TimApplication.enterTimchat((AppCompatActivity) getActivity(), consultBeanNew.Room.ChannelID, consultBeanNew.Member.MemberName, isChat);
            }
        });

        //搜索
        et_symptom = (EditText) view.findViewById(R.id.et_symptom);
        et_symptom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchData(true, editable.toString());
                //隐藏软键盘

            }
        });

        if (getArguments() == null) {
            OPDType = Constant.OPDTYPE_ORDER_CHAT;
        } else {
            OPDType = getArguments().getString("OPDType");
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mUseSearchMode) {
            searchData(true, mSearchKey);
        } else {
            loadData(true);
        }
    }

    @Override
    public void onClick(View view) {
    }

    /**
     * 请求数据
     */

    private void loadData(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);
        NBSAppAgent.onEvent("诊室-图文咨询");
        EmptyViewUtils.removeAllView(mContent);
        Http_getTaskList_Event getTaskListEvent = new Http_getTaskList_Event(refresh ? "1" : mPageListViewHelper.getPageIndex() + "", "20", OPDType, new HttpListener<List<ConsultBeanNew>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, DebugUtils.errorFormat("getTaskListEvent", code, msg));
                }
                mPageListViewHelper.setRefreshing(false);
                EmptyViewUtils.removeAllView(mContent);
                if (refresh) {
                    EmptyViewUtils.showErrorView(mContent, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EmptyViewUtils.removeAllView(mContent);
                            mPageListViewHelper.setRefreshing(false);
                            loadData(true);
                        }
                    });
                }
            }

            @Override
            public void onSuccess(List<ConsultBeanNew> datas) {
                if (DebugUtils.debug) {
                    Log.i(TAG, DebugUtils.successFormat("getTaskListEvent", DebugUtils.toJson(datas)));
                }
                mPageListViewHelper.setRefreshing(false);
                if (refresh) {
                    if (datas == null || datas.isEmpty()) {
                        EmptyViewUtils.showEmptyView(mContent, R.mipmap.no_order, getResources().getString(R.string.emtpy_order), new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EmptyViewUtils.removeAllView(mContent);
                                loadData(true);
                            }
                        });
                    }else{
                        EmptyViewUtils.removeAllView(mContent);
                        mPageListViewHelper.refreshData(datas);
                    }
                } else {
                    EmptyViewUtils.removeAllView(mContent);
                    mPageListViewHelper.addPageData(datas);
                }
            }
        });

        HttpClient client = NetService.createClient(getActivity(), HttpClient.DOCTOR_API_URL, getTaskListEvent);
        client.start();
    }

    /**
     * 请求数据
     */
    private void searchData(final boolean refresh, String Keyword) {
        NBSAppAgent.onEvent("诊室-搜索图文咨询:" + Keyword);
        mPageListViewHelper.setRefreshing(true);

        List<String> list = new ArrayList<>();
        list.add("1");
        Http_getTaskList_Event getTaskListEvent = new Http_getTaskList_Event(Keyword, refresh ? "1" : mPageListViewHelper.getPageIndex() + "", "20", OPDType, new HttpListener<List<ConsultBeanNew>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, DebugUtils.errorFormat("getTaskListEvent", code, msg));
                }
                mPageListViewHelper.setRefreshing(false);

                EmptyViewUtils.removeAllView(mContent);
//                if (refresh) {
//                    EmptyViewUtils.showErrorView(mContent, new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            loadData(true);
//                        }
//                    });
//                }

            }

            @Override
            public void onSuccess(List<ConsultBeanNew> datas) {
                if (DebugUtils.debug) {
                    Log.i(TAG, DebugUtils.successFormat("getTaskListEvent", DebugUtils.toJson(datas)));
                }
                mPageListViewHelper.setRefreshing(false);
                EmptyViewUtils.removeAllView(mContent);
                if (refresh) {
                    mPageListViewHelper.refreshData(datas);
                    if (datas == null || datas.isEmpty()) {
                        EmptyViewUtils.showEmptyView(mContent, R.mipmap.no_search_data, getResources().getString(R.string.string_empty_search_result), null);
                    }
                } else {
                    mPageListViewHelper.addPageData(datas);
                }
            }
        });

        HttpClient client = NetService.createClient(getActivity(), HttpClient.DOCTOR_API_URL, getTaskListEvent);
        client.start();

//        HttpSearchConsultEvent event = new HttpSearchConsultEvent(refresh ? "1" : mPageListViewHelper.getPageIndex() + "", Keyword, INCLUDE, new HttpListener<List<ConsultBeanNew>>(
//        ) {
//            @Override
//            public void onError(int code, String msg) {
//                if (DebugUtils.debug) {
//                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
//                }
//                mPageListViewHelper.setRefreshing(false);
//
//            }
//
//            @Override
//            public void onSuccess(List<ConsultBeanNew> list) {
//                if (DebugUtils.debug) {
//                    Log.i(TAG, DebugUtils.successFormat("search consults", DebugUtils.toJson(list)));
//                }
//                mPageListViewHelper.setRefreshing(false);
//                if (refresh) {
//                    mPageListViewHelper.refreshData(list);
//                } else {
//                    mPageListViewHelper.addPageData(list);
//                }
//
//
//            }
//        });
//
//        HttpClient httpClient = NetService.createClient(getActivity(), event);
//        httpClient.start();
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
            int room = Integer.parseInt(data.Room.RoomState);
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
                if(room == 0 || room == 1){
                    consultStateTV.setText(R.string.string_consult_state0);
                    consultStateTV.setTextColor(context.getResources().getColor(R.color.app_color_string));
                }
                if(room == 2){
                    consultStateTV.setText(R.string.string_consult_state1);
                    consultStateTV.setTextColor(context.getResources().getColor(R.color.app_color_main));
                }
            }

        }

    }
}