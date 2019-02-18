package com.kmwlyy.patient.evaluate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.EvaluateListBean;
import com.kmwlyy.patient.helper.net.bean.EvaluateTagBean;
import com.kmwlyy.patient.helper.net.event.HttpDoctorService;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.onlinediagnose.DoctorServiceActivity;
import com.winson.ui.widget.RateLayout;
import com.winson.ui.widget.TagBaseAdapter;
import com.winson.ui.widget.TagCloudLayout;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xcj on 2016/12/20.
 */

public class EvaluateListActivity extends BaseActivity implements View.OnClickListener,PageListView.OnPageLoadListener {
    public static final String TAG = EvaluateListActivity.class.getSimpleName();
    private List<EvaluateListBean> evaluateList,selectedList;
    private DoctorServiceActivity.EvaluateListAdapter evaluateAdapter;
    private TagBaseAdapter tagAdapter;
    private List<String> mTagList;
    private Context mContext = EvaluateListActivity.this;
    private HttpClient mEvaluateClient;

    private PageListView mEvaluateListView;
    private PageListViewHelper<EvaluateListBean> mPageListViewHelper;

    @BindView(R.id.tcl_container)
    TagCloudLayout tcl_container;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.ll_container)
    LinearLayout ll_container;

    @Override
    public void onRefreshData() {getData(true);}

    @Override
    public void onLoadPageData() {getData(false);}

    public static void startEvaluateListActivity(Context context, String doctorId) {
        Intent intent = new Intent(context, EvaluateListActivity.class);
        intent.putExtra("id", doctorId);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_list);
        butterknife.ButterKnife.bind(this);
        init();
    }

    private void init() {
        tv_center.setText(getResources().getString(R.string.string_patient_evaluate));
        tv_left.setOnClickListener(this);
        //Tag 列表
        mTagList = new ArrayList<>();
        tagAdapter = new TagBaseAdapter(this,mTagList,TagBaseAdapter.WHITE);
        tcl_container.setAdapter(tagAdapter);

        tcl_container.setItemClickListener(new TagCloudLayout.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                tagAdapter.setHighLight(mTagList.get(position));
                //点击全部标签、点击其它标签 分类显示下面的列表数据
                if(mTagList.get(position).equals(getResources().getString(R.string.string_total_tag))){
                    evaluateAdapter.setData(evaluateList);
                }else{
                    getfiterData(mTagList.get(position),true);
                }
            }
        });

        //我的评论列表
        evaluateList = new ArrayList<>();
        selectedList = new ArrayList<>();
        evaluateAdapter = new DoctorServiceActivity.EvaluateListAdapter(mContext, evaluateList);

        mEvaluateListView = (PageListView)findViewById(R.id.lv_evaluate);
        mPageListViewHelper = new PageListViewHelper<>(mEvaluateListView, evaluateAdapter);
        mPageListViewHelper.getListView().setDivider(null);
        mPageListViewHelper.setOnPageLoadListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTag();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_left:
                onBackPressed();
                break;
        }
    }

    /**
     * 请求网络数据
     */
    public void getData(final boolean refresh){
        if(tagAdapter.getHighLight().isEmpty() || tagAdapter.getHighLight().equals(getResources().getString(R.string.string_total_tag))){
            getAllData(true);
        }else {
            getfiterData(tagAdapter.getHighLight(),true);
        }
    }

    /**
     * 加载Tag数据
     */
    public void loadTag(){showLoadDialog(R.string.string_wait);
        HttpDoctorService.GetTags getTags = new HttpDoctorService.GetTags(new HttpListener<ArrayList<EvaluateTagBean>>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("HttpDoctorService.GetTags", code, msg));
                }

                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(ArrayList<EvaluateTagBean> list) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("HttpDoctorService.GetTags", PUtils.toJson(list)));
                }
                dismissLoadDialog();
                if(null != list && list.size() > 0){
                    ll_container.setVisibility(View.VISIBLE);
                    mTagList.clear();
                    mTagList.add(getResources().getString(R.string.string_total_tag));
                    for(int i=0;i<list.size();i++){
                        mTagList.add(list.get(i).getTagName());
                    }
                    tagAdapter.notifyDataSetChanged();
                    getAllData(true);
                }
            }
        });

        mEvaluateClient = NetService.createClient(this, getTags);
        mEvaluateClient.start();
    }

    /**
     * 加载列表数据
     */
    public void getAllData(final boolean refresh){
        mPageListViewHelper.setRefreshing(refresh);
        HttpDoctorService.GetEvaluate getEvaluate = new HttpDoctorService.GetEvaluate(getIntent().getStringExtra("id"),refresh ? "1" : mPageListViewHelper.getPageIndex()+"", new HttpListener<ArrayList<EvaluateListBean>>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("HttpDoctorService.GetEvaluate", code, msg));
                }
                mPageListViewHelper.setRefreshing(false);
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(ArrayList<EvaluateListBean> list) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("HttpDoctorService.GetEvaluate", PUtils.toJson(list)));
                }
                mPageListViewHelper.setRefreshing(false);
                if(null != list){
                    if (refresh) {
                        mPageListViewHelper.refreshData(list);
                        evaluateList = list;
                    } else {
                        mPageListViewHelper.addPageData(list);
                        evaluateList.addAll(list);
                    }
                }
                tagAdapter.setHighLight("");
            }
        });

        mEvaluateClient = NetService.createClient(this, getEvaluate);
        mEvaluateClient.start();
    }

    /**
     * 数据分类显示
     */
    public void getfiterData(String tagName,final boolean refresh){
        mPageListViewHelper.setRefreshing(refresh);
        HttpDoctorService.GetTagEvaluate getEvaluate = new HttpDoctorService.GetTagEvaluate(getIntent().getStringExtra("id"),refresh ? "1" : mPageListViewHelper.getPageIndex()+"",tagName, new HttpListener<ArrayList<EvaluateListBean>>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("HttpDoctorService.GetEvaluate", code, msg));
                }
                mPageListViewHelper.setRefreshing(false);
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(ArrayList<EvaluateListBean> list) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("HttpDoctorService.GetEvaluate", PUtils.toJson(list)));
                }
                mPageListViewHelper.setRefreshing(false);
                if(null != list){
                    if (refresh) {
                        mPageListViewHelper.refreshData(list);
                    } else {
                        mPageListViewHelper.addPageData(list);
                    }
                }
            }
        });

        mEvaluateClient = NetService.createClient(this, getEvaluate);
        mEvaluateClient.start();
    }
}
