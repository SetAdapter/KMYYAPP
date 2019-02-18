package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.MedicalRecordAdapter;
import com.kmwlyy.doctor.model.MedicalRecordBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getMedicalRecord_Event;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecordActivity extends BaseActivity implements PageListView.OnPageLoadListener {
    public static final String TAG = "MedicalRecordActivity";
    Context mContext = MedicalRecordActivity.this;
    private MedicalRecordAdapter adapter;
    private List<MedicalRecordBean> list;
    private String memberId;

    private PageListView mRecordListView;
    private PageListViewHelper<MedicalRecordBean> mPageListViewHelper;

    @Override
    public void onRefreshData() {loadData(true);}

    @Override
    public void onLoadPageData() {loadData(false);}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_record);

        ((TextView)findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_medical_record));

        memberId = getIntent().getStringExtra("id");
        init();
    }

    public void init(){
        mLeftBtn = (TextView) findViewById(R.id.tv_left);
        mLeftBtn.setVisibility(View.VISIBLE);
        mLeftBtn.setOnClickListener(this);

        list = new ArrayList<>();
        adapter = new MedicalRecordAdapter(mContext, list);
        //我的就诊记录
        mRecordListView = (PageListView)findViewById(R.id.lv_record);
        mPageListViewHelper = new PageListViewHelper<>(mRecordListView, adapter);
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

    @Override
    public void onResume() {
        super.onResume();
        loadData(true);
    }

    /**
     * 请求患者详情数据
     * @param memberId
     */
    /**
     * 请求数据
     */
    private void loadData(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);
        Http_getMedicalRecord_Event event = new Http_getMedicalRecord_Event(refresh ? "1" : mPageListViewHelper.getPageIndex()+"",memberId,new HttpListener<List<MedicalRecordBean>>(
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
            public void onSuccess(List<MedicalRecordBean> list) {
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