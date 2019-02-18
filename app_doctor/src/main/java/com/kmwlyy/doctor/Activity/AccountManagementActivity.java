package com.kmwlyy.doctor.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;

import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.AccountDetail;
import com.kmwlyy.doctor.model.EventAccountApi;
import com.kmwlyy.doctor.model.httpEvent.AccountEvent;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.AlterDialogView;
import com.winson.ui.widget.EmptyViewUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by xcj on 2016/12/7.
 */

public class AccountManagementActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = AccountManagementActivity.class.getSimpleName();
    @ViewInject(R.id.tv_tools_left)
    TextView btn_left;
    @ViewInject(R.id.tv_title_center)
    TextView tv_title;
    @ViewInject(R.id.iv_tools_right)
    Button iv_tools_right;
    @ViewInject(R.id.ll_withdraw)
    LinearLayout mWithdrawLinlayout;
    @ViewInject(R.id.ll_pay_password)
    LinearLayout mPayPasswordLinlayout;
    @ViewInject(R.id.tv_account_balance)
    TextView mAccountBalanceTxt;
    @ViewInject(R.id.ll_root)
    LinearLayout mRoot;
    @ViewInject(R.id.tv_havePayPassword)
    TextView mHavePayPasswordTxt;

    private HttpClient mHttpClient;
    private boolean mHavePayPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manage);
        ViewUtils.inject(this);
        EventBus.getDefault().register(this);
        tv_title.setText("账户管理");
        btn_left.setVisibility(View.VISIBLE);
        tv_title.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_tools_right.setText("账单");
        iv_tools_right.setVisibility(View.VISIBLE);
        iv_tools_right.setOnClickListener(this);
        mWithdrawLinlayout.setOnClickListener(this);
        mPayPasswordLinlayout.setOnClickListener(this);
        EmptyViewUtils.showLoadingView((ViewGroup) mRoot);
        getAccountDetail();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_withdraw:
                if (!mHavePayPassword){
                    AlterDialogView.Builder builder = new AlterDialogView.Builder(this);
                    builder.setTitle("未设置支付密码");
                    builder.setMessage("是否去设置支付密码？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    arg0.dismiss();
                                    //打开设置页面
                                    Intent set = new Intent(AccountManagementActivity.this, SetPaymentCodeActivity.class);
                                    set.putExtra(SetPaymentCodeActivity.IS_SET_PAY_PASSWORD, mHavePayPassword);
                                    startActivity(set);
                                }
                            });
                    builder.create().show();
                    return;
                }
                Intent withdraw = new Intent(this, WithdrawActivity.class);
                startActivity(withdraw);
                //提现
                break;
            case R.id.ll_pay_password:
                //设置密码
                Intent set = new Intent(this, SetPaymentCodeActivity.class);
                set.putExtra(SetPaymentCodeActivity.IS_SET_PAY_PASSWORD, mHavePayPassword);
                startActivity(set);
                break;
            case R.id.iv_tools_right:
                //明细
                Intent details = new Intent(this, AccountDetailsActivity.class);
                startActivity(details);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectTab(EventAccountApi.SetPayPasswordSuc suc) {
        mHavePayPasswordTxt.setText("已设置");
        mHavePayPassword = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectTab(EventAccountApi.WithdrawSuc suc) {
        EmptyViewUtils.showLoadingView((ViewGroup) mRoot);
        getAccountDetail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getAccountDetail(){
        AccountEvent.GetAccountDetail detail = new AccountEvent.GetAccountDetail(new HttpListener<AccountDetail>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(AccountManagementActivity.this,"获取我的帐户信息失败");
                EmptyViewUtils.showErrorView((ViewGroup) mRoot, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAccountDetail();
                    }
                });
            }

            @Override
            public void onSuccess(AccountDetail accountDetail) {
                EmptyViewUtils.removeAllView((ViewGroup) mRoot);
                mAccountBalanceTxt.setText(String.valueOf(accountDetail.mAvailable));
                mHavePayPassword = accountDetail.mHavePayPassword;
                if (mHavePayPassword){
                    mHavePayPasswordTxt.setText("已设置");
                }else{
                    mHavePayPasswordTxt.setText("未设置");
                }

            }
        });

        mHttpClient = NetService.createClient(this, detail);
        mHttpClient.start();
    }


}
