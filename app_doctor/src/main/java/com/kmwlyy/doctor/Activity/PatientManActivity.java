package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.PatientListAdapter;
import com.kmwlyy.doctor.model.PatientListBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getPatientList_Event;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.ArrayList;
import java.util.List;

public class PatientManActivity extends BaseActivity implements PageListView.OnPageLoadListener {
    public static final String TAG = "PatientManActivity";
    Context mContext = PatientManActivity.this;
    private EditText et_symptom;
    private TextView tv_noResult;
    private PatientListAdapter adapter;
    private List<PatientListBean> list;

    private PageListView mPatientListView;
    private PageListViewHelper<PatientListBean> mPageListViewHelper;

    @Override
    public void onRefreshData() {loadData(true);}

    @Override
    public void onLoadPageData() {
        if(et_symptom.getText().toString().equals("")){
            loadData(false);
        }else{
            SearchData(true,et_symptom.getText().toString());
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_man);

        ((TextView)findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_manage));

        init();
    }

    public void init(){
        mLeftBtn = (TextView) findViewById(R.id.tv_left);
        mLeftBtn.setVisibility(View.VISIBLE);
        mLeftBtn.setOnClickListener(this);

        tv_noResult = (TextView) findViewById(R.id.tv_noResult);

        list = new ArrayList<>();
        adapter = new PatientListAdapter(mContext, list);
        //我的患者列表
        mPatientListView = (PageListView)findViewById(R.id.lv_patients);
        mPageListViewHelper = new PageListViewHelper<>(mPatientListView, adapter);
        mPageListViewHelper.getListView().setDivider(null);
        mPageListViewHelper.setOnPageLoadListener(this);

        //搜索
        et_symptom = (EditText)findViewById(R.id.et_symptom);
        et_symptom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                SearchData(true ,editable.toString());
            }
        });

        findViewById(R.id.base_title_line).setVisibility(View.GONE);

    }

    /**
     * 请求数据
     */
    private void loadData(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);
        Http_getPatientList_Event event = new Http_getPatientList_Event(refresh ? "1" : mPageListViewHelper.getPageIndex()+"","",
                new HttpListener<List<PatientListBean>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                mPageListViewHelper.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<PatientListBean> list) {
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

    /**
     * 请求数据 - 搜索
     */
    private void SearchData(final boolean refresh,String Keyword) {
        tv_noResult.setVisibility(View.GONE);
        mPageListViewHelper.setRefreshing(refresh);
        Http_getPatientList_Event event = new Http_getPatientList_Event(refresh ? "1" : mPageListViewHelper.getPageIndex()+"",Keyword,
                new HttpListener<List<PatientListBean>>(
                ) {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                        }
                        mPageListViewHelper.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(List<PatientListBean> list) {
                        if (DebugUtils.debug) {
                            Log.i(TAG, "request success");
                        }
                        mPageListViewHelper.setRefreshing(false);

                        if (refresh) {
                            if (list.size() <= 0 ){
                                tv_noResult.setVisibility(View.VISIBLE);
                            }else {
                                tv_noResult.setVisibility(View.GONE);
                            }
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
        et_symptom.setText("");
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
}
