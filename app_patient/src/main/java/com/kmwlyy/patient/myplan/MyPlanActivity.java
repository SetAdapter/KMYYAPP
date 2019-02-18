package com.kmwlyy.patient.myplan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.casehistory.ElectronCaseHistoryActivity;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.UserPackage;
import com.kmwlyy.patient.helper.net.event.MemberPackages;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.pay.PayActivity;
import com.winson.ui.widget.AlterDialogView;
import com.winson.ui.widget.EmptyViewUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * Created by xcj on 2016/12/14.
 */

public class MyPlanActivity extends BaseActivity {
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.lv_count)
    ListView mCount;
    @BindView(R.id.tv_member_name)
    TextView mMemberName;
    @BindView(R.id.tv_state_txt)
    TextView mStateTxt;
    @BindView(R.id.btn_buy)
    Button mBuyBtn;
    @BindView(R.id.btn_cancel)
    Button mCancelBtn;
    @BindView(R.id.ll_root)
    LinearLayout mRoot;
    @BindView(R.id.tv_time)
    TextView mTimeView;

    private HttpClient mGetListClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_plan);
        butterknife.ButterKnife.bind(this);
        tv_center.setText(getString(R.string.my_plan_title));
        EmptyViewUtils.showLoadingView((ViewGroup) mRoot);
        getMyPlan();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payedRefresh(EventApi.RefreshMyPlan event) {
        getMyPlan();
    }

    private void getMyPlan(){
        MemberPackages.GetCurrentPackage info = new MemberPackages.GetCurrentPackage(new HttpListener<UserPackage>() {
            @Override
            public void onError(int code, String msg) {
                if ("当前用户没有套餐数据！".equals(msg)){
                    EmptyViewUtils.showErrorView((ViewGroup) mRoot, R.string.have_no_plan, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goToPlan();
                        }
                    });
                }else {
                    EmptyViewUtils.showErrorView((ViewGroup) mRoot, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getMyPlan();
                        }
                    });
                }
            }

            @Override
            public void onSuccess(final UserPackage userPackage) {
                EmptyViewUtils.removeAllView((ViewGroup) mRoot);
                if (userPackage == null){
                    mRoot.setVisibility(View.GONE);
                }
                mMemberName.setText(userPackage.mUserPackageName);
                mStateTxt.setText(userPackage.mStatusText);
                if (userPackage.mStatus == 0){
                    mBuyBtn.setVisibility(View.VISIBLE);
                    mCancelBtn.setVisibility(View.VISIBLE);
                    mTimeView.setText(getString(R.string.period_of_validity_title)+"    "+userPackage.mPeriod+userPackage.mPeriodUnitName);
                }else{
                    mBuyBtn.setVisibility(View.GONE);
                    mCancelBtn.setVisibility(View.GONE);
                    mTimeView.setText(getString(R.string.period_of_validity_title)+"    "+userPackage.mStartTime.substring(0,10)+"~"+userPackage.mEndTime.substring(0,10));
                }
                mBuyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PayActivity.startPayActivity(MyPlanActivity.this, userPackage.mOrderNo,userPackage.mAmount,PayActivity.MY_PLAN,true);
                    }
                });
                mCancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //取消接口
                        AlterDialogView.Builder builder = new AlterDialogView.Builder(MyPlanActivity.this);
                        builder.setTitle(getString(R.string.case_delete_title));
                        builder.setMessage(getString(R.string.is_confirm_cancel_this_plan));
                        builder.setNegativeButton(getString(R.string.string_exit_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton(getString(R.string.string_exit_yes),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0,
                                                        int arg1) {
                                        arg0.dismiss();
                                        cancelOrder(userPackage.mOrderNo);
                                    }
                                });
                        builder.create().show();
                    }
                });
                mCount.setAdapter(new MyAdapter(MyPlanActivity.this, userPackage.mDetails));
            }
        });
        mGetListClient = NetService.createClient(this, info);
        mGetListClient.start();
    }

    private void cancelOrder(String orderNo){
        MemberPackages.CancelOrder cancelOrder = new MemberPackages.CancelOrder(orderNo, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(MyPlanActivity.this,msg);
            }

            @Override
            public void onSuccess(Object o) {
                ToastUtils.showShort(MyPlanActivity.this, getString(R.string.cancel_suc));
                getMyPlan();
            }
        });
        mGetListClient = NetService.createClient(this, cancelOrder);
        mGetListClient.start();
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private List<UserPackage.Detail> datas;


        public MyAdapter(Context context, List<UserPackage.Detail> datas){
            this.mInflater = LayoutInflater.from(context);
            this.datas = datas;
        }


        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder= new ViewHolder();
                convertView = mInflater.inflate(R.layout.layout_mypaln_item, null);
                holder.info = (TextView) convertView.findViewById(R.id.tv_info);
                holder.count = (TextView) convertView.findViewById(R.id.tv_count);
                holder.totalCount = (TextView) convertView.findViewById(R.id.tv_total_count);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.info.setText(datas.get(position).mServiceGoodsName);
            holder.totalCount.setText(String.valueOf(datas.get(position).mServiceGoodsCount));
            holder.count.setText(String.valueOf(datas.get(position).mServiceGoodsCount - datas.get(position).mConsumeCount));
            return convertView;
        }

    }

    public final class ViewHolder{
        public TextView info;
        public TextView count;
        public TextView totalCount;
    }

    private void goToPlan(){
        Intent intent = new Intent();
        intent.setClass(MyPlanActivity.this, MembersPlanActivity.class);
        startActivity(intent);
    }
}
