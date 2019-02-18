package com.kmwlyy.doctor.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.doctor.Activity.VoiceDetailActivity;
import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.VideoOrderListAdapter;
import com.kmwlyy.doctor.model.OrderVoiceBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getOrderVoice_Event;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderVoiceFragment extends Fragment implements OnClickListener,PageListView.OnPageLoadListener{

    public static final String TAG = "OrderVoiceFragment";
    private Context mContext;
    private VideoOrderListAdapter voiceListAdapter;
    private List<OrderVoiceBean> list_voice;
    public HttpClient httpClient;
    private ViewGroup mContent;

    private PageListView mVoiceListView;
    private PageListViewHelper<OrderVoiceBean> mPageListViewHelper;

    @Override
    public void onRefreshData() {loadData(true);}

    @Override
    public void onLoadPageData() {loadData(false);}

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_list, null);

        list_voice = new ArrayList<>();
        mContext = getActivity();
        voiceListAdapter = new VideoOrderListAdapter(mContext,list_voice);
        mContent = (ViewGroup) view.findViewById(R.id.content);
        mVoiceListView = (PageListView)view.findViewById(R.id.lv_chat);
        mPageListViewHelper = new PageListViewHelper<>(mVoiceListView, voiceListAdapter);
        mPageListViewHelper.getListView().setDivider(null);
        mPageListViewHelper.setOnPageLoadListener(this);

        mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderVoiceBean listItem = mPageListViewHelper.getAdapter().getItem(position);
                Intent intent = new Intent(mContext, VoiceDetailActivity.class);
                intent.putExtra("id",listItem.getOPDRegisterID());
                intent.putExtra("name",listItem.getMemberName());
                intent.putExtra("doctor", BaseApplication.getInstance().getUserData().mUserCNName);
                intent.putExtra("age",listItem.getAge());
                intent.putExtra("sex",listItem.getGender());
                startActivity(intent);
            }
        });
        loadData(true);
        return view;
    }

    @Override
    public void onClick(View view) {
    }

    /**
     * 请求数据
     */
    private void loadData(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);
        EmptyViewUtils.removeAllView(mContent);
        Http_getOrderVoice_Event event = new Http_getOrderVoice_Event(refresh ? "1" : mPageListViewHelper.getPageIndex()+"",new HttpListener<List<OrderVoiceBean>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                mPageListViewHelper.setRefreshing(false);
                EmptyViewUtils.removeAllView(mContent);
                if (refresh) {
                    EmptyViewUtils.showErrorView(mContent, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadData(true);
                        }
                    });
                }
            }

            @Override
            public void onSuccess(List<OrderVoiceBean> list) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                mPageListViewHelper.setRefreshing(false);

                if (refresh) {
                    mPageListViewHelper.refreshData(list);
                    if (list == null || list.isEmpty()) {
                        EmptyViewUtils.showEmptyView(mContent, new OnClickListener() {
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

        httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }
}
