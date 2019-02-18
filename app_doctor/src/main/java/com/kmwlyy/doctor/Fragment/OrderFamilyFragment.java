package com.kmwlyy.doctor.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.FamilyDoctorAdapter;
import com.kmwlyy.doctor.model.FamilyDoctor;
import com.kmwlyy.doctor.model.httpEvent.Http_getOrderDoctor_Event;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.ArrayList;
import java.util.List;

public class OrderFamilyFragment extends Fragment implements OnClickListener,PageListView.OnPageLoadListener {

    public static final String TAG = "OrderFamilyFragment";
    private Context mContext;
    private FamilyDoctorAdapter familyDoctorAdapter;
    private List<FamilyDoctor> list_family;
    public HttpClient httpClient;

    private PageListView mFamilyListView;
    private PageListViewHelper<FamilyDoctor> mPageListViewHelper;

    public void onRefreshData() {loadData(true);}

    @Override
    public void onLoadPageData() {loadData(false);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_list, null);

        list_family = new ArrayList<>();
        mContext = getActivity();
        familyDoctorAdapter = new FamilyDoctorAdapter(mContext,list_family);

        mFamilyListView = (PageListView)view.findViewById(R.id.lv_chat);
        mPageListViewHelper = new PageListViewHelper<>(mFamilyListView, familyDoctorAdapter);
        mPageListViewHelper.getListView().setDivider(null);
        mPageListViewHelper.setOnPageLoadListener(this);

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
        Http_getOrderDoctor_Event event = new Http_getOrderDoctor_Event(refresh ? "1" : mPageListViewHelper.getPageIndex()+"",new HttpListener<List<FamilyDoctor>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                mPageListViewHelper.setRefreshing(false);

            }

            @Override
            public void onSuccess(List<FamilyDoctor> list) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                mPageListViewHelper.setRefreshing(false);

                if (refresh) {
                    mPageListViewHelper.refreshData(list);
                } else {
                    mPageListViewHelper.addPageData(list);
                }
            }
        });

        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }
}
