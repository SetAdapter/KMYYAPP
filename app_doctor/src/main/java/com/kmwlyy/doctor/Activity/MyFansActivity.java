package com.kmwlyy.doctor.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.FansListAdapter;
import com.kmwlyy.doctor.model.FansListBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getFansList_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.ArrayList;
import java.util.List;

public class MyFansActivity extends BaseActivity implements PageListView.OnPageLoadListener{
    public static final String TAG = "MyFansActivity";
    private FansListAdapter adapter;
    private List<FansListBean> list;

    private PageListView mFansListView;
    private PageListViewHelper<FansListBean> mPageListViewHelper;

    //返回和保存
    @ViewInject(R.id.tv_left)
    TextView btn_left;


    @Override
    public void onRefreshData() {loadData(true);}

    @Override
    public void onLoadPageData() {loadData(false);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans);
        ViewUtils.inject(this); //注入view和事件
        //初使化
        init();
    }

    private void init() {
        ((TextView)findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_myfans));
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(this);
        btn_left.setText(getResources().getString(R.string.string_back));

        list = new ArrayList<>();
        adapter = new FansListAdapter(mContext, list);
        //我的粉丝列表
        mFansListView = (PageListView)findViewById(R.id.lv_fans);
        mPageListViewHelper = new PageListViewHelper<>(mFansListView, adapter);
        mPageListViewHelper.getListView().setDivider(null);
        mPageListViewHelper.setOnPageLoadListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_left://退出
                finish();
                break;
        }
    }

    /**
     * 请求数据
     */
    private void loadData(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);
        Http_getFansList_Event event = new Http_getFansList_Event(refresh ? "1" : mPageListViewHelper.getPageIndex()+"",new HttpListener<List<FansListBean>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }	mPageListViewHelper.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<FansListBean> list) {
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

    @Override
    public void onResume() {
        super.onResume();
        loadData(true);
    }
}
