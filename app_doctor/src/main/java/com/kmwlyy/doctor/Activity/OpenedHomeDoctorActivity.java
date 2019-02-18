package com.kmwlyy.doctor.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.adapter.HealthInjuryAdapter;
import com.kmwlyy.doctor.adapter.SingedPeopleAdapter;
import com.kmwlyy.doctor.model.SingedMemberBean;
import com.kmwlyy.doctor.model.SingedPeopleBean;
import com.kmwlyy.doctor.model.httpEvent.Htttp_getSingedList_Event;
import com.kmwlyy.doctor.model.httpEvent.OfflineBookingBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by TFeng on 2017/6/30.
 */

public class OpenedHomeDoctorActivity extends BaseActivity implements PageListView.OnPageLoadListener{

    @ViewInject(R.id.tv_left)
    TextView tv_left;
    @ViewInject(R.id.iv_left)
    ImageView iv_left;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.tv_center)
    TextView tv_center;
    @ViewInject(R.id.ll_health_injury)
    LinearLayout ll_health_injury;
    @ViewInject(R.id.ll_offline_booking)
    LinearLayout ll_offline_booking;
    @ViewInject(R.id.lv_singed_people)
    PageListView lv_singed_people;
    @ViewInject(R.id.rl_wrong)
    RelativeLayout rl_wrong;
    @ViewInject(R.id.iv_wrong_img)
    ImageView iv_wrong_img;
    @ViewInject(R.id.tv_wrong)
    TextView tv_wrong;
    @ViewInject(R.id.ll_content)
    LinearLayout ll_content;


    private PageListViewHelper<SingedMemberBean> mPageListViewHelper;
    private List<SingedMemberBean> mListData;
    private SingedPeopleAdapter mAdapter;
    private SingedMemberBean mSingedMemberBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened_home_doctor);
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);
        initView();
        initListener();
    }


    private void initView() {
        tv_left.setVisibility(View.GONE);
        iv_left.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_center.setText(R.string.home_doctor);
        tv_right.setText(R.string.string_setting);


        mListData = new ArrayList<>();
        mAdapter = new SingedPeopleAdapter(mContext,mListData);

        mPageListViewHelper = new PageListViewHelper(lv_singed_people, mAdapter);

        mPageListViewHelper.setOnPageLoadListener(this);
        mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mSingedMemberBean = mListData.get(i);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("singedMember",mSingedMemberBean);
                intent.putExtra("patient",0);
                intent.putExtra("FamilyDoctorID",mSingedMemberBean.getFamilyDoctorID());
                intent.putExtras(bundle);
                intent.setClass(mContext,PatientActivity.class);
                startActivity(intent);
            }
        });
        getSingedList(true);

    }

    private void getSingedList(final boolean refesh) {
        mPageListViewHelper.setRefreshing(refesh);
        Htttp_getSingedList_Event event = new Htttp_getSingedList_Event(refesh ? "1" : mPageListViewHelper.getPageIndex()+"","20", new HttpListener<List<SingedMemberBean>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(mContext, msg);
                mPageListViewHelper.setRefreshing(false);
            }

            @Override
            public void onSuccess(List<SingedMemberBean> beans) {
                mPageListViewHelper.setRefreshing(false);
                if(refesh){
                    if(beans == null || beans.size() == 0){
                        ToastUtils.showShort(mContext,getString(R.string.no_message));
                       /* rl_wrong.setVisibility(View.VISIBLE);
                        lv_singed_people.setVisibility(View.GONE);
                        iv_wrong_img.setImageResource(R.mipmap.no_message);
                        tv_wrong.setText("暂无数据");*/
                       EmptyViewUtils.removeAllView(ll_content);
                        EmptyViewUtils.showEmptyView(ll_content,R.mipmap.no_message , "暂无数据", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getSingedList(true);
                            }
                        });
                    }else{
                        mListData = beans;
                        mPageListViewHelper.refreshData(beans);
                    }
                }else{
                    if(beans == null || beans.size() == 0){
                        ToastUtils.showShort(mContext,getString(R.string.no_more_message));
                    }else{
                        mListData.addAll(beans);
                        mPageListViewHelper.addPageData(beans);
                    }
                }

            }
        });
       new HttpClient(mContext,event).startNewApi();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeSingedMember(SingedMemberBean singedMemberBean) {
        // TODO: 2017/7/7 重新发送网络请求
        getSingedList(true);
    }


    private void initListener() {
        iv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        ll_health_injury.setOnClickListener(this);
        ll_offline_booking.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch(arg0.getId()){
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_right:
                Intent intent = new Intent();
                intent.putExtra("IsOpened",true);
                intent.setClass(mContext,HomeDoctorSettingActivity.class);
                startActivity(intent);

                break;
            case R.id.ll_health_injury:
                startActivity(new Intent(mContext, HealthInjuryActivity.class));
                break;
            case R.id.ll_offline_booking:
                // TODO: 2017/6/30 跳转到线下预约页面
//                ToastUtils.showShort(mContext,"功能暂未上线，敬请期待");
                startActivity(new Intent(mContext, OfflineBookingActivity.class));
                break;
        }
    }

    @Override
    public void onRefreshData() {
        getSingedList(true);

    }

    @Override
    public void onLoadPageData() {
        getSingedList(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
