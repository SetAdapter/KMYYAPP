package com.kmwlyy.doctor.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ImageUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.adapter.AdviceListAdapter;
import com.kmwlyy.doctor.adapter.ConsultVoiceListAdapter;
import com.kmwlyy.doctor.model.Constant;
import com.kmwlyy.doctor.model.ConsultBeanNew;
import com.kmwlyy.doctor.model.OPDRegister;
import com.kmwlyy.doctor.model.httpEvent.HttpGetWaitPatientEvent;
import com.kmwlyy.doctor.model.httpEvent.Http_getGetWaitPatient_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_getTaskList_Event;
import com.kmwlyy.imchat.message.CustomMessage;
import com.kmwlyy.imchat.message.Message;
import com.kmwlyy.imchat.message.MessageFactory;
import com.kmwlyy.imchat.model.MessageEvent;
import com.networkbench.agent.impl.NBSAppAgent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.TIMMessage;
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
import java.util.Observable;
import java.util.Observer;

public class QueryVoiceFragment extends SearchFragment implements OnClickListener, PageListView.OnPageLoadListener {
    public static final String TAG = "QueryVoiceFragment";
    //    private AdviceListAdapter adviceAdapter;
    private List<OPDRegister> list_opd;
    private String OPDType;

    private EditText et_symptom;
    private PageListView mVoiceListView;
    private PageListViewHelper<ConsultBeanNew> mPageListViewHelper;
    private ViewGroup mContent;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_chat, null);
        EventBus.getDefault().register(this);
        mVoiceListView = (PageListView) view.findViewById(R.id.lv_chat);
        mContent = (ViewGroup) view.findViewById(R.id.content);

        // 2=语音咨询 3=视频咨询
        if (getArguments() == null) {
            OPDType = Constant.OPDTYPE_VIDEO;
        } else {
            OPDType = getArguments().getString("OPDType");
        }

        list_opd = new ArrayList<>();
//        adviceAdapter = new AdviceListAdapter(getActivity(),list_opd);
        mPageListViewHelper = new PageListViewHelper<>(mVoiceListView, new ConsultVoiceListAdapter(getActivity(), null));
        mPageListViewHelper.getListView().setDivider(null);
        mPageListViewHelper.setOnPageLoadListener(this);

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
            }
        });
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
        NBSAppAgent.onEvent("诊室-视话问诊");
        Http_getTaskList_Event getTaskListEvent = new Http_getTaskList_Event(refresh ? "1" : mPageListViewHelper.getPageIndex() + "", "20", Constant.OPDTYPE_VOICE, new HttpListener<List<ConsultBeanNew>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, DebugUtils.errorFormat("getTaskListEvent", code, msg));
                }
                mPageListViewHelper.setRefreshing(false);
                EmptyViewUtils.removeAllView(mContent);
                EmptyViewUtils.showEmptyView(mContent, R.mipmap.no_network, "请求错误", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EmptyViewUtils.removeAllView(mContent);
                        loadData(true);
                    }
                });
            }

            @Override
            public void onSuccess(List<ConsultBeanNew> datas) {
                if (DebugUtils.debug) {
                    Log.i(TAG, DebugUtils.successFormat("getTaskListEvent", DebugUtils.toJson(datas)));
                }
                mPageListViewHelper.setRefreshing(false);
                if (refresh) {
                    if (datas == null || datas.size() == 0) {
                        mPageListViewHelper.refreshData(new ArrayList<ConsultBeanNew>());
                        EmptyViewUtils.removeAllView(mContent);
                        EmptyViewUtils.showEmptyView(mContent, R.mipmap.no_order, getResources().getString(R.string.emtpy_order), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EmptyViewUtils.removeAllView(mContent);
                                loadData(true);
                            }
                        });
                    } else {
                        EmptyViewUtils.removeAllView(mContent);
                        mPageListViewHelper.refreshData(datas);
                    }
                } else {
                    if (datas == null || datas.size() == 0) {
                        ToastUtils.showShort(getActivity(), getString(R.string.no_more_message));
                    } else {
                        EmptyViewUtils.removeAllView(mContent);
                        mPageListViewHelper.addPageData(datas);
                    }
                }
            }
        });

        HttpClient client = NetService.createClient(getActivity(), HttpClient.DOCTOR_API_URL, getTaskListEvent);
        client.start();

    }

    /**
     * 请求数据 - 搜索
     */
    private void searchData(final boolean refresh, String Keyword) {
        NBSAppAgent.onEvent("诊室-搜索视话问诊:" + Keyword);
        mPageListViewHelper.setRefreshing(refresh);
        Http_getTaskList_Event getTaskListEvent = new Http_getTaskList_Event(Keyword, refresh ? "1" : mPageListViewHelper.getPageIndex() + "", "20", Constant.OPDTYPE_VOICE, new HttpListener<List<ConsultBeanNew>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, DebugUtils.errorFormat("getTaskListEvent", code, msg));
                }
                mPageListViewHelper.setRefreshing(false);

//                EmptyViewUtils.removeAllView(mContent);

            }

            @Override
            public void onSuccess(List<ConsultBeanNew> datas) {
                if (DebugUtils.debug) {
                    Log.i(TAG, DebugUtils.successFormat("getTaskListEvent", DebugUtils.toJson(datas)));
                }
                EmptyViewUtils.removeAllView(mContent);
                mPageListViewHelper.setRefreshing(false);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 接到推送，刷新列表
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPushData(CustomMessage.reLoadData bean) {
        loadData(true);
    }
}
