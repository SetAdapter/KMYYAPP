package com.kmwlyy.patient.account;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.DoctorClinic;
import com.kmwlyy.patient.helper.net.bean.UserTransInfo;
import com.kmwlyy.patient.helper.net.event.AccountEvent;
import com.kmwlyy.patient.helper.net.event.HttpDoctorFreeClinic;
import com.kmwlyy.patient.helper.net.event.HttpUserConsults;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.home.DoctorDutyListActivity;
import com.kmwlyy.patient.onlinediagnose.BuyIMConsultActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;
import com.winson.ui.widget.listview.PageListView;
import com.winson.ui.widget.listview.PageListViewHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xcj on 2016/12/9.
 */

public class AccountDetailsActivity extends BaseActivity implements PageListView.OnPageLoadListener{
    private static final String TAG = AccountDetailsActivity.class.getSimpleName();
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.doctor_page_listview)
    PageListView mDoctorPageListview;

    private PageListViewHelper<UserTransInfo.ListItem> mPageListViewHelper;
    private HttpClient mGetFamilyDoctorListClient;
    private View root = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        butterknife.ButterKnife.bind(this);
        tv_center.setText(getString(R.string.account_detail_title));
        mPageListViewHelper = new PageListViewHelper<>(mDoctorPageListview, new AccountDetailListAdapter(AccountDetailsActivity.this, null));
        int diagnoseListPadding = getResources().getDimensionPixelSize(R.dimen.list_padding);
        mPageListViewHelper.getListView().setDividerHeight(0);
        mPageListViewHelper.getListView().setPadding(0, 0, 0, diagnoseListPadding);
        mPageListViewHelper.getListView().setClipToPadding(false);
        mPageListViewHelper.setOnPageLoadListener(this);
        mPageListViewHelper.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!PUtils.checkHaveUser(AccountDetailsActivity.this)) {
                    return;
                }
            }
        });
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
            TextView mTitle = (TextView) viewHolder.findViewById(R.id.tv_title);
            TextView mTime = (TextView) viewHolder.findViewById(R.id.tv_time);
            TextView mMoney = (TextView) viewHolder.findViewById(R.id.tv_money);
            //1: 收入,2: 充值,3: 消费,4: 提现,5: 退款
            if (data.mTransType == 1){
                mTitle.setText(getString(R.string.get_money));
            }else if(data.mTransType == 2){
                mTitle.setText(getString(R.string.recharge));
            }else if(data.mTransType == 3){
                mTitle.setText(getString(R.string.consumption));
            }else if(data.mTransType == 4){
                mTitle.setText(getString(R.string.withdraw));
            }else if(data.mTransType == 5){
                mTitle.setText(getString(R.string.reimburse));
            }
            if (TextUtils.isEmpty(data.mTradeTime)){
                mTime.setText(data.mCreateTime.substring(0,10));
            }else{
                mTime.setText(data.mTradeTime.substring(0,10));
            }
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
