package com.kmwlyy.patient.account;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.kangmeitongu.main.wxapi.WXPayEntryActivity;
import com.kmt518.kmpay.KMPayConfig;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.Pay;
import com.kmwlyy.patient.helper.net.bean.PayResult;
import com.kmwlyy.patient.helper.net.bean.RechargeDetail;
import com.kmwlyy.patient.helper.net.event.AccountEvent;
import com.kmwlyy.patient.helper.net.event.HttpCashier;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.pay.PayActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.winson.ui.widget.RateLayout;
import com.winson.ui.widget.ToastMananger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by xcj on 2016/12/8.
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = RechargeActivity.class.getSimpleName();
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.et_money)
    EditText mMoneyEdit;
    @BindView(R.id.alipay_pay)
    LinearLayout mAlipayPay;
    @BindView(R.id.wechat_pay)
    LinearLayout mWechatPay;
    @BindView(R.id.km_wallet)
    LinearLayout mKmWallet;
    @BindView(R.id.confirm)
    Button mConfirm;
    @BindView(R.id.tv_limit)
    TextView mLimitTxt;

    private HttpClient mHttpClient;

    static final int ALIAY_PAY = 0;
    static final int WECHAT_PAY = 1;
    static final int KM_WALLET = 2;
    private int mPayMode;
    private String mOrderNo = null;
    private IWXAPI mWXAPI;
    private ProgressDialog mPayDialog;
    private double mRechargeMinLimit = 0;
    private double mRechargeMaxLimit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        butterknife.ButterKnife.bind(this);
        tv_center.setText(getString(R.string.recharge));
        mRechargeMinLimit = getIntent().getDoubleExtra(AccountManagementActivity.RECHARGE_MIN_LIMIT, 0);
        mRechargeMaxLimit = getIntent().getDoubleExtra(AccountManagementActivity.RECHARGE_MAX_LIMIT, 0);
        mLimitTxt.setText(getString(R.string.recharge_money_min_limit)+mRechargeMinLimit+"   "+getString(R.string.recharge_money_max_limit)+mRechargeMaxLimit);
        tv_left.setOnClickListener(this);
        mAlipayPay.setOnClickListener(this);
        mWechatPay.setOnClickListener(this);
        mKmWallet.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mAlipayPay.performClick();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.alipay_pay:
                mWechatPay.setSelected(false);
                mKmWallet.setSelected(false);
                mAlipayPay.setSelected(true);
                mPayMode = ALIAY_PAY;
                break;
            case R.id.wechat_pay:
                mAlipayPay.setSelected(false);
                mKmWallet.setSelected(false);
                mWechatPay.setSelected(true);
                mPayMode = WECHAT_PAY;
                break;
            case R.id.km_wallet:
                mAlipayPay.setSelected(false);
                mWechatPay.setSelected(false);
                mKmWallet.setSelected(true);
                mPayMode = KM_WALLET;
                break;
            case R.id.confirm:
                getOrderNo();
                break;
        }
    }

    private void pay(){
        //获取支付详情
        if (mPayMode == ALIAY_PAY) {
            payWithAli();
        } else if (mPayMode == KM_WALLET) {
            payWidthKMWallet();
        } else {
            payWithWechat();
        }
    }

    private void getOrderNo(){
        int i = 0;
        if (mPayMode == ALIAY_PAY){
            i=2;
        }else if(mPayMode == WECHAT_PAY){
            i=1;
        }
        String  money = mMoneyEdit.getText().toString().trim();
        if (Double.valueOf(money) < mRechargeMinLimit){
            ToastUtils.showShort(RechargeActivity.this, getString(R.string.input_recharge_money_min_limit)+mRechargeMinLimit+getString(R.string.money_unit));
            return;
        }
        if (Double.valueOf(money) > mRechargeMaxLimit){
            ToastUtils.showShort(RechargeActivity.this, getString(R.string.input_recharge_money_max_limit)+mRechargeMaxLimit+getString(R.string.money_unit));
            return;
        }
        AccountEvent.Recharge recharge = new AccountEvent.Recharge(money, ""+i, new HttpListener<RechargeDetail>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(RechargeActivity.this, msg);
            }

            @Override
            public void onSuccess(RechargeDetail rechargeDetail) {
                mOrderNo = rechargeDetail.mOrderNo;
                pay();
            }
        });
        HttpClient getOrderClient = NetService.createClient(this, recharge);
        getOrderClient.start();
    }

    private void payWithAli() {
        if (!checkAliPayInstalled(this)) {
            ToastMananger.showToast(this, R.string.not_install_alipay_app, Toast.LENGTH_SHORT);
            return;
        }

        showPayDialog();

        HttpCashier.AliPay aliPay = new HttpCashier.AliPay(mOrderNo, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("aliPay", code, msg));
                }
                ToastMananger.showToast(RechargeActivity.this, R.string.get_pay_info_error, Toast.LENGTH_SHORT);
                dismissPayDialog();
            }

            @Override
            public void onSuccess(final Object o) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("aliPay", DebugUtils.toJson(o)));
                }
                dismissPayDialog();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(RechargeActivity.this);
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(o.toString(), true);
                        if (DebugUtils.debug) {
                            Log.d(TAG, "pay result : " + result);
                        }
                        PayResult payResult = new PayResult(result);
                        /**
                         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                         * docType=1) 建议商户依赖异步通知
                         */
                        String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                        String resultStatus = payResult.getResultStatus();

                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ToastMananger.showToast(PayActivity.this, R.string.pay_success, Toast.LENGTH_SHORT);
//                                }
//                            });
                            EventApi.Pay pay = new EventApi.Pay();
                            pay.success = true;
                            EventBus.getDefault().post(pay);

//                            finish();
                        } else {
                            // 判断resultStatus 为非"9000"则代表可能支付失败
                            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastMananger.showToast(RechargeActivity.this, R.string.pay_confirming, Toast.LENGTH_SHORT);
                                    }
                                });
                            } else {
                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        ToastMananger.showToast(PayActivity.this, R.string.pay_fail, Toast.LENGTH_SHORT);
//                                    }
//                                });

                                EventApi.Pay pay = new EventApi.Pay();
                                pay.success = false;
                                EventBus.getDefault().post(pay);
                            }
                        }

                    }
                }).start();

            }
        });

        HttpClient aliPayClient = NetService.createClient(this, aliPay);
        aliPayClient.start();

    }

    private void showPayDialog() {

        mPayDialog = new ProgressDialog(this);
        mPayDialog.setMessage(getResources().getString(R.string.on_paying));
        mPayDialog.setCancelable(false);
        mPayDialog.show();

    }

    private void dismissPayDialog() {
        if (mPayDialog != null) {
            mPayDialog.dismiss();
        }
    }

    public static boolean checkAliPayInstalled(Context context) {

        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    private void payWidthKMWallet() {
        showPayDialog();

        HttpCashier.KMPay kmPay = new HttpCashier.KMPay(mOrderNo, KMPayConfig.RETURN_URL, new HttpListener<Pay.KM>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("payWidthKMWallet", code, msg));
                }
                ToastMananger.showToast(RechargeActivity.this, R.string.get_pay_info_error, Toast.LENGTH_SHORT);
                dismissPayDialog();
            }

            @Override
            public void onSuccess(Pay.KM data) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("payWidthKMWallet", DebugUtils.toJson(data)));
                }
                dismissPayDialog();

                new com.kmt518.kmpay.PayTask(RechargeActivity.this)
                        .pay(
                                data.body == null ? "" : data.body,
                                data.inputCharset,
                                data.notifyUrl,
                                data.outTradeNo,
                                data.partner,
                                data.returnUrl,
                                data.sellerEmail,
                                data.sign,
                                data.subject,
                                data.timestamp,
                                data.totalAmount,
                                null,
                                null,
                                null
                        );

            }
        });

        HttpClient mkPayClient = NetService.createClient(this, kmPay);
        mkPayClient.start();

    }

    private void payWithWechat() {
        showPayDialog();

        HttpCashier.WxPay wxPay = new HttpCashier.WxPay(mOrderNo, WXPayEntryActivity.WEIXIN_APP_ID, new HttpListener<Pay.Wechat>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("wxPay", code, msg));
                }

                dismissPayDialog();
                ToastMananger.showToast(RechargeActivity.this, R.string.get_pay_info_error, Toast.LENGTH_SHORT);

            }

            @Override
            public void onSuccess(Pay.Wechat data) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("wxPay", DebugUtils.toJson(data)));
                }
                dismissPayDialog();

                PayReq req = new PayReq();
                req.appId = data.appid;
                req.partnerId = data.partnerId;
                req.prepayId = data.prepay_id;
                req.nonceStr = data.nonce_str;
                req.timeStamp = data.timeStamp;
                req.sign = data.sign;
                req.packageValue = data.packageV;
//                req.extData = "app data"; // optional
//									Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                if (mWXAPI == null) {
                    mWXAPI = WXAPIFactory.createWXAPI(RechargeActivity.this, data.appid);
                    mWXAPI.registerApp(data.appid);
                }
                boolean result = mWXAPI.sendReq(req);
                if (DebugUtils.debug) {
                    Log.d(TAG, "send result : " + result);
                }
            }
        });

        HttpClient wxPayClient = NetService.createClient(this, wxPay);
        wxPayClient.start();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payResult(EventApi.Pay pay) {
        if (pay.success) {
            finish();
            EventBus.getDefault().post(new EventApi.RechargeSuc());
            ToastMananger.showToast(this, R.string.pay_success, Toast.LENGTH_SHORT);
        }else{
            ToastMananger.showToast(this, R.string.pay_fail, Toast.LENGTH_SHORT);
        }
    }
}
