package com.kmwlyy.doctor.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.VoiceListAdapter;
import com.kmwlyy.doctor.model.Constant;
import com.kmwlyy.doctor.model.ConsultBeanNew;
import com.kmwlyy.doctor.model.InfoEvent;
import com.kmwlyy.doctor.model.httpEvent.Http_getTaskList_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.networkbench.agent.impl.NBSAppAgent;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xcj on 2017/7/5.
 */

public class VideoVoiceChatListActivity extends BaseActivity implements PageListView.OnPageLoadListener{

    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_center)
    TextView tv_title;
    @ViewInject(R.id.tv_right)
    TextView tv_right;

    private ViewGroup mContent;

    public static final String TAG = "VideoVoiceChatListActivity";

    @Override
    public void onRefreshData() {
        loadData(true);
    }

    @Override
    public void onLoadPageData() {
        loadData(false);
    }


    private VoiceListAdapter consultListAdapter;
    private List<ConsultBeanNew> list_consult;
    private PageListView mChatListView;
    private PageListViewHelper<ConsultBeanNew> mPageListViewHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_voice_chat_list);
        ViewUtils.inject(this);
        mChatListView = (PageListView)findViewById(R.id.lv_chat);
        mContent = (ViewGroup)findViewById(R.id.content);
        tv_title.setText("视话问诊");
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
                NBSAppAgent.onEvent("服务-视话问诊-设置");
                //跳转到设置页面
                VideoInquirySettingActivity.startVideoInquirySettingActivity(VideoVoiceChatListActivity.this, 1, true);
            }
        });

        list_consult = new ArrayList<>();
        consultListAdapter = new VoiceListAdapter(this, list_consult);
        mPageListViewHelper = new PageListViewHelper<>(mChatListView, consultListAdapter);
        mPageListViewHelper.getListView().setDivider(null);
        mPageListViewHelper.setOnPageLoadListener(this);
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
        Http_getTaskList_Event event = new Http_getTaskList_Event(refresh ? "1" : mPageListViewHelper.getPageIndex()+"","20", Constant.OPDTYPE_ORDER_VOICE, new HttpListener<List<ConsultBeanNew>>(
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
            public void onSuccess(List<ConsultBeanNew> list) {
                if (DebugUtils.debug) {
                }
                mPageListViewHelper.setRefreshing(false);

                if (refresh) {
                    mPageListViewHelper.refreshData(list);
                    if (list == null || list.isEmpty()) {
                        EmptyViewUtils.showEmptyView(mContent, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadData(true);
                            }
                        });
                    }
                } else {
                    mPageListViewHelper.addPageData(list);
                }
            }
        });

        HttpClient httpClient = NetService.createClient(VideoVoiceChatListActivity.this,HttpClient.DOCTOR_API_URL, event);
        httpClient.start();
    }
}
