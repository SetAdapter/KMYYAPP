package com.kmwlyy.patient.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.kmwlyy.patient.helper.net.bean.AccountDetail;
import com.kmwlyy.patient.helper.net.event.AccountEvent;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.myplan.MyPlanActivity;
import com.kmwlyy.patient.myservice.DeliveryActivity;
import com.winson.ui.widget.AlterDialogView;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.RateLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by xcj on 2016/12/7.
 */

public class AccountManagementActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = AccountManagementActivity.class.getSimpleName();
    public static final String RECHARGE_MAX_LIMIT = "RECHARGE_MAX_LIMIT";
    public static final String RECHARGE_MIN_LIMIT = "RECHARGE_MIN_LIMIT";
    public static final String CASE_MIN_LIMIT = "CASE_MIN_LIMIT";

    @BindView(R.id.navigation_icon)
    ImageView mNavigationIcon;
    @BindView(R.id.navigation_btn)
    RateLayout mNavigationBtn;
    @BindView(R.id.toolbar_title)
    TextView mNoolbarTitle;
    @BindView(R.id.ll_recharge)
    LinearLayout mRechargeLinlayout;
    @BindView(R.id.ll_withdraw)
    LinearLayout mWithdrawLinlayout;
    @BindView(R.id.ll_pay_password)
    LinearLayout mPayPasswordLinlayout;
    @BindView(R.id.tv_save)
    TextView mDetailsTxt;
    @BindView(R.id.tv_account_balance)
    TextView mAccountBalanceTxt;
    @BindView(R.id.ll_root)
    LinearLayout mRoot;
    @BindView(R.id.tv_havePayPassword)
    TextView mHavePayPasswordTxt;

    private HttpClient mHttpClient;
    private boolean mHavePayPassword = false;
    private double mCashMinLimit = 0;
    private double mRechargeMinLimit = 0;
    private double mRechargeMaxLimit = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manage);
        butterknife.ButterKnife.bind(this);

        mNoolbarTitle.setText(getString(R.string.account_mange_title));
        mNavigationBtn.setOnClickListener(this);
        mRechargeLinlayout.setOnClickListener(this);
        mWithdrawLinlayout.setOnClickListener(this);
        mPayPasswordLinlayout.setOnClickListener(this);
        mDetailsTxt.setOnClickListener(this);
        EmptyViewUtils.showLoadingView((ViewGroup) mRoot);
        getAccountDetail();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectTab(EventApi.SetPayPasswordSuc suc) {
        mHavePayPasswordTxt.setText(getString(R.string.have_set));
        mHavePayPassword = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectTab(EventApi.WithdrawSuc suc) {
        EmptyViewUtils.showLoadingView((ViewGroup) mRoot);
        getAccountDetail();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectTab(EventApi.RechargeSuc suc) {
        EmptyViewUtils.showLoadingView((ViewGroup) mRoot);
        getAccountDetail();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.navigation_btn:
                finish();
                break;
            case R.id.ll_recharge:
                Intent intent = new Intent(this, RechargeActivity.class);
                intent.putExtra(RECHARGE_MIN_LIMIT, mRechargeMinLimit);
                intent.putExtra(RECHARGE_MAX_LIMIT, mRechargeMaxLimit);
                startActivity(intent);
                //充值
                break;
            case R.id.ll_withdraw:
                if (!mHavePayPassword){
                    AlterDialogView.Builder builder = new AlterDialogView.Builder(AccountManagementActivity.this);
                    builder.setTitle(getString(R.string.not_set_pay_password_title));
                    builder.setMessage(getString(R.string.is_set_pay_password));
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
                                    //打开设置页面
                                    arg0.dismiss();
                                    Intent set = new Intent(AccountManagementActivity.this, SetPaymentCodeActivity.class);
                                    set.putExtra(SetPaymentCodeActivity.IS_SET_PAY_PASSWORD, mHavePayPassword);
                                    startActivity(set);
                                }
                            });
                    builder.create().show();
                    return;
                }
                Intent withdraw = new Intent(this, WithdrawActivity.class);
                withdraw.putExtra(CASE_MIN_LIMIT, mCashMinLimit);
                startActivity(withdraw);
                //提现
                break;
            case R.id.ll_pay_password:
                //设置密码
                Intent set = new Intent(this, SetPaymentCodeActivity.class);
                set.putExtra(SetPaymentCodeActivity.IS_SET_PAY_PASSWORD, mHavePayPassword);
                startActivity(set);
                break;
            case R.id.tv_save:
                //明细
                Intent details = new Intent(this, AccountDetailsActivity.class);
                startActivity(details);
                break;
        }
    }

    private void getAccountDetail(){
        AccountEvent.GetAccountDetail detail = new AccountEvent.GetAccountDetail(new HttpListener<AccountDetail>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(AccountManagementActivity.this,getString(R.string.get_my_account_info_fail));
                EmptyViewUtils.showErrorView((ViewGroup) mRoot, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAccountDetail();
                    }
                });
            }

            @Override
            public void onSuccess(AccountDetail accountDetail) {
                Log.d(TAG, DebugUtils.successFormat("accountDetail", PUtils.toJson(accountDetail)));
                EmptyViewUtils.removeAllView((ViewGroup) mRoot);
                mAccountBalanceTxt.setText(String.valueOf(accountDetail.mAvailable));
                mHavePayPassword = accountDetail.mHavePayPassword;
                if (mHavePayPassword){
                    mHavePayPasswordTxt.setText(getString(R.string.have_set));
                }else{
                    mHavePayPasswordTxt.setText(getString(R.string.not_set));
                }
                mCashMinLimit = accountDetail.mConfig.mCashMinLimit;
                mRechargeMinLimit = accountDetail.mConfig.mRechargeMinLimit;
                mRechargeMaxLimit = accountDetail.mConfig.mRechargeMaxLimit;
            }
        });

        mHttpClient = NetService.createClient(this, detail);
        mHttpClient.start();
    }
}
