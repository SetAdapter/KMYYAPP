package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.UserTransInfo;
import com.kmwlyy.doctor.model.httpEvent.AccountEvent;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by xcj on 2016/12/9.
 */

public class AccountDetailsActivity extends BaseActivity implements PageListView.OnPageLoadListener{
    private static final String TAG = AccountDetailsActivity.class.getSimpleName();
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_center)
    TextView tv_title;
    @ViewInject(R.id.doctor_page_listview)
    PageListView mDoctorPageListview;

    private PageListViewHelper<UserTransInfo.ListItem> mPageListViewHelper;
    private HttpClient mGetFamilyDoctorListClient;
    private View root = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        ViewUtils.inject(this);
        tv_title.setText("帐户明细");
        btn_left.setVisibility(View.VISIBLE);
        tv_title.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPageListViewHelper = new PageListViewHelper<>(mDoctorPageListview, new AccountDetailListAdapter(AccountDetailsActivity.this, null));
        int diagnoseListPadding = getResources().getDimensionPixelSize(R.dimen.list_padding);
        mPageListViewHelper.getListView().setDividerHeight(0);
        mPageListViewHelper.getListView().setPadding(0, 0, 0, diagnoseListPadding);
        mPageListViewHelper.getListView().setClipToPadding(false);
        mPageListViewHelper.setOnPageLoadListener(this);
        getDoctorClinicList(true);
    }
    @Override
    public void onRefreshData() {
        getDoctorClinicList(true);
    }

    @Override
    public void onLoadPageData() {
        getDoctorClinicList(false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        NetService.closeClient(mGetFamilyDoctorListClient);
    }

    class AccountDetailListAdapter extends CommonAdapter<UserTransInfo.ListItem> {
        public AccountDetailListAdapter(Context context, List<UserTransInfo.ListItem> datas) {
            super(context, R.layout.account_detail_list, datas);
        }

        @Override
        public void convert(ViewHolder viewHolder, UserTransInfo.ListItem data, int position) {
           /* 1: 收入,2: 充值,3: 消费,4: 提现,5: 退款*/
            TextView mTitle = (TextView) viewHolder.findViewById(R.id.tv_title);
            TextView mTime = (TextView) viewHolder.findViewById(R.id.tv_time);
            TextView mMoney = (TextView) viewHolder.findViewById(R.id.tv_money);
            //0挂号,1图文咨询,2语音问诊,3视频问诊,4处方支付,5家庭医生,6会员套餐,7远程会诊,8影像判读,9用户充值,100其他
            if (data.mOrderType == 0){
                mTitle.setText("挂号");
            }else if(data.mOrderType == 1){
                mTitle.setText("图文咨询");
            }else if(data.mOrderType == 2){
                mTitle.setText("语音问诊");
            }else if(data.mOrderType == 3){
                mTitle.setText("视频问诊");
            }else if(data.mOrderType == 4){
                mTitle.setText("处方支付");
            }else if(data.mOrderType == 5){
                mTitle.setText("家庭医生");
            }else if(data.mOrderType == 6){
                mTitle.setText("会员套餐");
            }else if(data.mOrderType == 7){
                mTitle.setText("远程会诊");
            }else if(data.mOrderType == 8){
                mTitle.setText("影像判读");
            }else if(data.mOrderType == 9){
                mTitle.setText("用户充值");
            }else if(data.mOrderType == 100){
                mTitle.setText("其他");
            }

            mTime.setText(data.mTradeTime.substring(0,10));
            if (data.mTransType == 1||data.mTransType == 2||data.mTransType == 5){
                mMoney.setText("+"+data.mAmount);
            }else{
                mMoney.setText("-"+data.mAmount);
            }

        }
    }

    private void getDoctorClinicList(final boolean refresh) {
        mPageListViewHelper.setRefreshing(refresh);
        AccountEvent.GetUserTransPagelist getUserTransPagelist = new AccountEvent.GetUserTransPagelist(0,
                refresh ? 1 : mPageListViewHelper.getPageIndex(),
                mPageListViewHelper.getPageSize(), new HttpListener<ArrayList<UserTransInfo.ListItem>>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getUserTransPagelist", code, msg));
                }

                mPageListViewHelper.setRefreshing(false);
            }

            @Override
            public void onSuccess(ArrayList<UserTransInfo.ListItem> listItems) {
                mPageListViewHelper.setRefreshing(false);
                if (refresh) {
                    mPageListViewHelper.refreshData(listItems);
                } else {
                    mPageListViewHelper.addPageData(listItems);
                }
            }
        });

        mGetFamilyDoctorListClient = NetService.createClient(this, getUserTransPagelist);
        mGetFamilyDoctorListClient.start();

    }
}
