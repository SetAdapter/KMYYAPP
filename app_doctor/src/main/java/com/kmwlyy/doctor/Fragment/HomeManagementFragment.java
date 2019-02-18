package com.kmwlyy.doctor.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.Activity.BaseInfoActivity;
import com.kmwlyy.doctor.Activity.HomePatientActivity;
import com.kmwlyy.doctor.Activity.PatientActivity;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.AppEventApi;
import com.kmwlyy.doctor.adapter.HomeMemberListAdapter;
import com.kmwlyy.doctor.model.HomeMemberBean;
import com.kmwlyy.doctor.model.SingedMemberBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getHomeMemberList_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
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

/**
 * Created by TFeng on 2017/7/2.
 */

public class HomeManagementFragment extends Fragment implements PageListView.OnPageLoadListener {

    @ViewInject(R.id.btn_add_member)
    Button btn_add_member;
    @ViewInject(R.id.lv_member)
    PageListView lv_member;
    @ViewInject(R.id.ll_fragment_content)
    LinearLayout ll_fragment_content;
    private Context context;
    private List<SingedMemberBean.UserMemberBean> mDataList;
    private HomeMemberListAdapter mMemberListAdapter;
    private PageListViewHelper<SingedMemberBean.UserMemberBean> mPageListViewHelper;
    private String mFamilyID;
    private String mFamilyDoctorID;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_management,null);
        ViewUtils.inject(this,view);
//        mFamilyDoctorID = getArguments().getString("FamilyDoctorID");
        context = getActivity();
        mDataList = new ArrayList<>();


        initListener();
//       假数据
        initData();

        mMemberListAdapter = new HomeMemberListAdapter(context,mDataList);
        mPageListViewHelper = new PageListViewHelper<>(lv_member,mMemberListAdapter);
        mPageListViewHelper.setOnPageLoadListener(this);
        mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                SingedMemberBean.UserMemberBean userMemberBean = mDataList.get(i);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("HomeMember",userMemberBean);
                intent.putExtra("patient",1);
                intent.putExtra("FamilyDoctorID",mFamilyID);
                intent.putExtras(bundle);
                intent.setClass(context,HomePatientActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }

    private void initListener() {
        btn_add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("FamilyDoctorID",mFamilyID);
                intent.setClass(context,BaseInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {

    }


    @Override
    public void onRefreshData() {
        getListData(true);

    }

    @Override
    public void onLoadPageData() {
        getListData(false);

    }

    public void getListData(final boolean refesh){
        mPageListViewHelper.setRefreshing(refesh);
        Http_getHomeMemberList_Event event = new Http_getHomeMemberList_Event(mFamilyID, new HttpListener<List<SingedMemberBean.UserMemberBean>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(context, msg);
                mPageListViewHelper.setRefreshing(false);

            }

            @Override
            public void onSuccess(List<SingedMemberBean.UserMemberBean> userMemberBeen) {
                mPageListViewHelper.setRefreshing(false);
                if(refesh){
                    if(userMemberBeen == null || userMemberBeen.size() == 0){
                        ToastUtils.showShort(context,getString(R.string.no_message));
                        EmptyViewUtils.removeAllView(ll_fragment_content);
                        EmptyViewUtils.showEmptyView(ll_fragment_content, R.mipmap.no_message, "暂无数据", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getListData(true);
                            }
                        });

                    }else{
                        mDataList = userMemberBeen;
                        mPageListViewHelper.refreshData(userMemberBeen);
                    }
                }else{
                    if(userMemberBeen == null || userMemberBeen.size() == 0){
                        ToastUtils.showShort(context,getString(R.string.no_more_message));
                    }else{
                        mDataList.addAll(userMemberBeen);
                        mPageListViewHelper.addPageData(userMemberBeen);
                    }
                }


            }
        });
        new HttpClient(context,event).startNewApi();

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getFamilyDoctorID(SingedMemberBean singedMemberBean){
        mFamilyID = singedMemberBean.getFamilyDoctorID();
        getListData(true);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getFamilyDoctorID(SingedMemberBean.UserMemberBean homeMemberBean){
//        // TODO: 2017/7/8 通知重新加载listview
        getListData(true);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getNew(AppEventApi.Add add){
//        // TODO: 2017/7/8 通知重新加载listview
        getListData(true);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
