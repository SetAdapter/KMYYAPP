package com.kmwlyy.doctor.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.EvaluateListAdapter;
import com.kmwlyy.doctor.model.EvaluateListBean;
import com.kmwlyy.doctor.model.EvaluateTagBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getEvaluateList_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_getEvaluateTag_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.TagBaseAdapter;
import com.winson.ui.widget.TagCloudLayout;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.ArrayList;
import java.util.List;

public class EvaluateActivity extends BaseActivity implements PageListView.OnPageLoadListener {
    public static final String TAG = "EvaluateActivity";
    private List<EvaluateListBean> evaluateList,selectedList;
    private EvaluateListAdapter evaluateAdapter;
    private TagBaseAdapter tagAdapter;
    private List<String> mEvaluateList;

    private PageListView mEvaluateListView;
    private PageListViewHelper<EvaluateListBean> mPageListViewHelper;

    //返回
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tcl_container)
    TagCloudLayout tcl_container;
    @ViewInject(R.id.ll_container)
    LinearLayout ll_container;

    @Override
    public void onRefreshData() {getData(true);}

    @Override
    public void onLoadPageData() {getData(false);}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        ViewUtils.inject(this); //注入view和事件
        //初使化
        init();
    }

    private void init() {
        ((TextView)findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_my_evaluate));
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(this);
        btn_left.setText(getResources().getString(R.string.string_back));
        //Tag 列表
        mEvaluateList = new ArrayList<>();
        tagAdapter = new TagBaseAdapter(this,mEvaluateList,TagBaseAdapter.WHITE);
        tcl_container.setAdapter(tagAdapter);

        tcl_container.setItemClickListener(new TagCloudLayout.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                tagAdapter.setHighLight(mEvaluateList.get(position));
                //点击全部标签、点击其它标签 分类显示下面的列表数据
                if(mEvaluateList.get(position).equals(getResources().getString(R.string.string_total))){
                    evaluateAdapter.setData(evaluateList);
                }else{
                    getfiterData(mEvaluateList.get(position),true);
                }
            }
        });

        //我的评论列表
        evaluateList = new ArrayList<>();
        selectedList = new ArrayList<>();
        evaluateAdapter = new EvaluateListAdapter(mContext, evaluateList);

        mEvaluateListView = (PageListView)findViewById(R.id.lv_evaluate);
        mPageListViewHelper = new PageListViewHelper<>(mEvaluateListView, evaluateAdapter);
        mPageListViewHelper.getListView().setDivider(null);
        mPageListViewHelper.setOnPageLoadListener(this);
    }
;
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.tv_left://退出
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTag();
    }

    /**
     * 加载Tag数据
     */
    public void loadTag(){
        showLoadDialog(R.string.string_wait);
        Http_getEvaluateTag_Event event = new Http_getEvaluateTag_Event(getIntent().getStringExtra("id"),new HttpListener<List<EvaluateTagBean>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
                finish();
            }

            @Override
            public void onSuccess(List<EvaluateTagBean> list) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                dismissLoadDialog();
                if(null != list && list.size() > 0){
                    ll_container.setVisibility(View.VISIBLE);
                    mEvaluateList.clear();
                    mEvaluateList.add(getResources().getString(R.string.string_total));
                    for(int i=0;i<list.size();i++){
                        mEvaluateList.add(list.get(i).getTagName() + " (" + list.get(i).getEvaluatedCount() + ")");
                    }
                    tagAdapter.notifyDataSetChanged();
                    getAllData(true);
                }
            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    /**
     * 请求网络数据
     */
    public void getData(final boolean refresh){
        if(tagAdapter.getHighLight().isEmpty() || tagAdapter.getHighLight().equals(getResources().getString(R.string.string_total))){
            getAllData(refresh);
        }else {
            getfiterData(tagAdapter.getHighLight(),refresh);
        }
    }

    /**
     * 加载列表数据
     */
    public void getAllData(final boolean refresh){
        mPageListViewHelper.setRefreshing(refresh);
        Http_getEvaluateList_Event event = new Http_getEvaluateList_Event(refresh ? "1" : mPageListViewHelper.getPageIndex()+"",mContext,new HttpListener<List<EvaluateListBean>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                mPageListViewHelper.setRefreshing(false);
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(List<EvaluateListBean> list) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                mPageListViewHelper.setRefreshing(false);

                if (refresh) {
                    mPageListViewHelper.refreshData(list);
                    evaluateList = list;
                } else {
                    mPageListViewHelper.addPageData(list);
                    evaluateList.addAll(list);
                }

                tagAdapter.setHighLight("");
            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    /**
     * 过滤数据
     */
    public void getfiterData(String TagName,final boolean refresh){
        mPageListViewHelper.setRefreshing(refresh);
        Http_getEvaluateList_Event event = new Http_getEvaluateList_Event(refresh ? "1" : mPageListViewHelper.getPageIndex()+"",mContext,TagName.substring(0,TagName.indexOf("(")-1),new HttpListener<List<EvaluateListBean>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                mPageListViewHelper.setRefreshing(false);
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(List<EvaluateListBean> list) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                mPageListViewHelper.setRefreshing(false);

                if(null != list){
                    if(refresh){
                        selectedList.clear();
                    }
                    selectedList.addAll(list);
                    evaluateAdapter.setData(selectedList);
                }

            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }
}
