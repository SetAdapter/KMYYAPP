package com.kmwlyy.patient.pay;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.kmwlyy.imchat.utils.PushUtil;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.account.ConfirmPayPasswordDialog;
import com.kmwlyy.patient.account.SetPaymentCodeActivity;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.AccountDetail;
import com.kmwlyy.patient.helper.net.bean.Consignee;
import com.kmwlyy.patient.helper.net.bean.Order;
import com.kmwlyy.patient.helper.net.bean.Pay;
import com.kmwlyy.patient.helper.net.bean.PayResult;
import com.kmwlyy.patient.helper.net.event.AccountEvent;
import com.kmwlyy.patient.helper.net.event.HttpCashier;
import com.kmwlyy.patient.helper.net.event.HttpOrders;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.myplan.MyPlanActivity;
import com.kmwlyy.patient.myservice.MyConsultActivity;
import com.kmwlyy.patient.myservice.MyDiagnoseActivity;
import com.kmwlyy.patient.myservice.MyFamilyDoctorActivity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.winson.ui.widget.AlterDialogView;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.ToastMananger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


import butterknife.BindView;

/**
 * Created by Winson on 2016/8/20.
 */
public class PayActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = PayActivity.class.getSimpleName();

    public static final String ORDER_NO = "ORDER_NO";
    public static final String IS_CONFIRM = "IS_CONFIRM";
    public static final String SHOW_NOTIFY = "SHOW_NOTIFY";
    public static final String PAGE_SIGN = "PAGE_SIGN";
    public static final String PRICE = "PRICE";
    public static final String COMBO = "COMBO";

    public static final int IMAGE_CONSULT = 1548;//图文咨询
    public static final int VIDEO_VOICE_CONSULT = 1549;//音视频咨询
    public static final int MEETING_CONSULT = 1550;//会诊咨询
    public static final int MEMBERS_PLAN = 1551;//会员套餐
    public static final int MY_PLAN = 1552;//我的套餐
    public static final int BUY_FAMILY_DOCTOR = 1553;//购买家庭医生
    public static final int BUY_VOICE_VIDEO = 1554;//购买音视频
    public static final int BUY_IM = 1555;//购买图文咨询
    public static final int BUY_PRESCRIPTION = 1556;//购买处方
    public static final int FAMILY_DOCTOR = 1557;//家庭医生支付
    public static void startPayActivity(Context context, String orderNo,String price, int pageSign, boolean isConfirm) {

        startPayActivity(context, orderNo, price, 0, pageSign,isConfirm, true);

    }

    /**
     *
     * @param context
     * @param orderNo 订单号
     * @param combo 使用套餐
     * @param pageSign 跳转来源
     * @param isConfirm 是否需要确认订单
     * @param showNotify 是否需要显示支付提示
     */
    public static void startPayActivity(Context context, String orderNo, String price, int combo, int pageSign, boolean isConfirm, boolean showNotify){
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra(ORDER_NO, orderNo);
        intent.putExtra(IS_CONFIRM, isConfirm);
        intent.putExtra(PRICE, price);
        intent.putExtra(SHOW_NOTIFY, true);
        intent.putExtra(PAGE_SIGN, pageSign);
        intent.putExtra(COMBO, combo);
        intent.putExtra(SHOW_NOTIFY, showNotify);
        context.startActivity(intent);
    }

    static final int ALIAY_PAY = 0;
    static final int WECHAT_PAY = 1;
    static final int KM_WALLET = 2;
    static final int ASSETS_PAY = 3;

    @BindView(R.id.alipay_pay)
    LinearLayout mAlipayPay;
    @BindView(R.id.wechat_pay)
    LinearLayout mWechatPay;
    @BindView(R.id.km_wallet)
    LinearLayout mKmWallet;
    @BindView(R.id.assets_pay)
    LinearLayout mAssetsPay;
    @BindView(R.id.confirm)
    Button mConfirm;
    @BindView(R.id.tv_service_money)
    TextView mServiceMoneyTxt;
    @BindView(R.id.tv_order_no)
    TextView mOrderNoTxt;
    @BindView(R.id.tv_money)
    TextView mMoneyTxt;

    private int mPayMode;
    private String mOrderNo;
    private IWXAPI mWXAPI;
    private String mPrice;

    private ProgressDialog mPayDialog;
    private boolean mIsConfirm;
    private ViewGroup root = null;
    private Consignee mConsignee = new Consignee();
    private boolean mShowNotify;
    private int mCombo;
    private boolean isSetPayPassword = false;
    private double mTotalMoney = 0;
    private double accountMoney = 0;

    private int pageSign = 0;//用于标记哪个页面进行支付，支付完成后区分

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_layout);
        butterknife.ButterKnife.bind(this);
        root = ((ViewGroup) findViewById(R.id.root));


        mOrderNo = getIntent().getStringExtra(ORDER_NO);
        pageSign = getIntent().getIntExtra(PAGE_SIGN, 0);
        mIsConfirm = getIntent().getBooleanExtra(IS_CONFIRM, false);
        mShowNotify = getIntent().getBooleanExtra(SHOW_NOTIFY, true);
        mPrice = getIntent().getStringExtra(PRICE);
        mCombo = getIntent().getIntExtra(COMBO, 0);
        mAssetsPay.setVisibility(View.GONE);

        EmptyViewUtils.showLoadingView((ViewGroup) root);
        if (mIsConfirm) {
            confirmOrder(mOrderNo, mCombo);
        }else{
            EmptyViewUtils.removeAllView((ViewGroup) root);
            mServiceMoneyTxt.setText("¥"+mPrice);
            mTotalMoney = Double.parseDouble(mPrice);
            getAccountDetail();
        }


        mOrderNoTxt.setText(mOrderNo);
        setBarTitle(R.string.select_pay_way);
        mAlipayPay.setOnClickListener(this);
        mWechatPay.setOnClickListener(this);
        mKmWallet.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mAssetsPay.setOnClickListener(this);

        mAlipayPay.performClick();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.alipay_pay:
                mWechatPay.setSelected(false);
                mKmWallet.setSelected(false);
                mAssetsPay.setSelected(false);
                mAlipayPay.setSelected(true);
                mPayMode = ALIAY_PAY;
                break;
            case R.id.wechat_pay:
                mAlipayPay.setSelected(false);
                mKmWallet.setSelected(false);
                mWechatPay.setSelected(true);
                mAssetsPay.setSelected(false);
                mPayMode = WECHAT_PAY;
                break;
            case R.id.km_wallet:
                mAlipayPay.setSelected(false);
                mWechatPay.setSelected(false);
                mAssetsPay.setSelected(false);
                mKmWallet.setSelected(true);
                mPayMode = KM_WALLET;
                break;
            case R.id.assets_pay:
                mAlipayPay.setSelected(false);
                mWechatPay.setSelected(false);
                mAssetsPay.setSelected(true);
                mKmWallet.setSelected(false);
                mPayMode = ASSETS_PAY;
                break;
            case R.id.confirm:
                pay();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectTab(EventApi.SetPayPasswordSuc suc) {
        isSetPayPassword = true;
    }


    private void pay() {
        if (mPayMode == ALIAY_PAY) {
            payWithAli();
        } else if (mPayMode == KM_WALLET) {
//            ToastMananger.showToast(this, R.string.pay_function_cannot_use, Toast.LENGTH_SHORT);
            payWidthKMWallet();
        } else if(mPayMode == WECHAT_PAY){
//            ToastMananger.showToast(this, R.string.pay_function_cannot_use, Toast.LENGTH_SHORT);
            payWithWechat();
        }else if(mPayMode == ASSETS_PAY){
            payWithAssets();
        }
    }

 /*   private String formatParam(Map params) {
        Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator();
        StringBuilder builder = new StringBuilder();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            String value = entry.getValue();
            builder.append(entry.getKey()).append("=").append(value);
        }
        return builder.toString();
    }*/

    private void payWithAssets(){
        if (!isSetPayPassword){
            AlterDialogView.Builder builder = new AlterDialogView.Builder(PayActivity.this);
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
                            Intent set = new Intent(PayActivity.this, SetPaymentCodeActivity.class);
                            set.putExtra(SetPaymentCodeActivity.IS_SET_PAY_PASSWORD, isSetPayPassword);
                            startActivity(set);
                            arg0.dismiss();
                        }
                    });
            builder.create().show();
            return;
        }
        if (mTotalMoney > accountMoney){
            ToastUtils.showShort(PayActivity.this, getString(R.string.not_sufficient_funds));
            return;
        }
        final ConfirmPayPasswordDialog dialog = new ConfirmPayPasswordDialog(this,R.style.CommonDialog);
        dialog.show();
        dialog.setClicklistener(new ConfirmPayPasswordDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                String psw = dialog.getSecPwd();
                assetsPay(psw);
                dialog.dismiss();
            }

            @Override
            public void doCancel() {
                dialog.dismiss();
            }

            @Override
            public void forget() {
                Intent set = new Intent(PayActivity.this, SetPaymentCodeActivity.class);
                set.putExtra(SetPaymentCodeActivity.IS_SET_PAY_PASSWORD, true);
                startActivity(set);
                dialog.dismiss();
            }
        });
    }

    private void assetsPay(String pwd){
        showPayDialog();
        AccountEvent.BlancePay blancePay = new AccountEvent.BlancePay(mOrderNo, pwd, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                ToastMananger.showToast(PayActivity.this, msg, Toast.LENGTH_SHORT);
                dismissPayDialog();
            }

            @Override
            public void onSuccess(Object o) {
                dismissPayDialog();
                //1支付成功，0支付失败，-1用户不存在，-2交易已存在，-3余额不足，-4充值不能用余额支付，-5支付密码错误
                if ("1".equals(o.toString())) {
                    EventApi.Pay pay = new EventApi.Pay();
                    pay.success = true;
                    EventBus.getDefault().post(pay);
                }else if("0".equals(o.toString())){
                    ToastMananger.showToast(PayActivity.this, getString(R.string.assets_pay_fail), Toast.LENGTH_SHORT);
                }else if("-1".equals(o.toString())){
                    ToastMananger.showToast(PayActivity.this, getString(R.string.assets_pay_use_not_exist), Toast.LENGTH_SHORT);
                }else if("-2".equals(o.toString())){
                    ToastMananger.showToast(PayActivity.this, getString(R.string.assets_pay_is_exist), Toast.LENGTH_SHORT);
                }else if("-3".equals(o.toString())){
                    ToastMananger.showToast(PayActivity.this, getString(R.string.not_sufficient_funds), Toast.LENGTH_SHORT);
                }else if("-4".equals(o.toString())){
                    ToastMananger.showToast(PayActivity.this, getString(R.string.recharge_can_not_use_assets_pay), Toast.LENGTH_SHORT);
                }else if("-5".equals(o.toString())){
                    ToastMananger.showToast(PayActivity.this, getString(R.string.pay_password_error), Toast.LENGTH_SHORT);
                }
            }
        });
        HttpClient blancePayClient = NetService.createClient(this, blancePay);
        blancePayClient.start();
    }

    private void payWidthKMWallet() {
        showPayDialog();

        HttpCashier.KMPay kmPay = new HttpCashier.KMPay(mOrderNo,KMPayConfig.RETURN_URL, new HttpListener<Pay.KM>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("payWidthKMWallet", code, msg));
                }
                ToastMananger.showToast(PayActivity.this, R.string.get_pay_info_error, Toast.LENGTH_SHORT);
                dismissPayDialog();
            }

            @Override
            public void onSuccess(Pay.KM data) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("payWidthKMWallet", DebugUtils.toJson(data)));
                }
                dismissPayDialog();

                new com.kmt518.kmpay.PayTask(PayActivity.this)
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
                ToastMananger.showToast(PayActivity.this, R.string.get_pay_info_error, Toast.LENGTH_SHORT);

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
                    mWXAPI = WXAPIFactory.createWXAPI(PayActivity.this, data.appid);
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


    public static boolean checkAliPayInstalled(Context context) {

        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
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
                ToastMananger.showToast(PayActivity.this, R.string.get_pay_info_error, Toast.LENGTH_SHORT);
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
                        PayTask alipay = new PayTask(PayActivity.this);
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
                                        ToastMananger.showToast(PayActivity.this, R.string.pay_confirming, Toast.LENGTH_SHORT);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payResult(EventApi.Pay pay) {
        if (pay.success) {
            if (mShowNotify) {
                ToastMananger.showToast(this, pay.repeat ? R.string.have_buyed : R.string.pay_success, Toast.LENGTH_SHORT);
            }

            switch (pageSign) {
                case IMAGE_CONSULT:
                    EventBus.getDefault().post(new EventApi.RefreshImageConsultList());
                    finish();
                    break;
                case VIDEO_VOICE_CONSULT:
                    EventBus.getDefault().post(new EventApi.RefreshVVConsultList());
                    finish();
                    break;
                case MEETING_CONSULT:
                    EventBus.getDefault().post(new EventApi.RefreshMeetingConsultList());
                    finish();
                    break;
                case BUY_IM:
                    PUtils.goToMyService(PayActivity.this, 0, EventApi.MainTabSelect.MY_COSULT, true);
                    finish();
                    break;
                case BUY_VOICE_VIDEO:
                    PUtils.goToMyService(PayActivity.this, 1, EventApi.MainTabSelect.MY_COSULT,true);
                    finish();
                    break;
                case BUY_FAMILY_DOCTOR:
                    PUtils.goToMyService(PayActivity.this, 0, EventApi.MainTabSelect.MY_DOCTOR,true);
                    finish();
                    break;
                case MEMBERS_PLAN:
                    PUtils.goToMyService(PayActivity.this, 0, EventApi.MainTabSelect.MY_PLAN,true);
                    finish();
                    break;
                case MY_PLAN:
                    EventBus.getDefault().post(new EventApi.RefreshMyPlan());
                    finish();
                    break;
                case BUY_PRESCRIPTION:
                    PUtils.goToMyService(PayActivity.this, 1, EventApi.MainTabSelect.MY_DIOAGNOSE,true);
                    finish();
                    break;
                case FAMILY_DOCTOR:
                    PUtils.goToMyService(PayActivity.this, 0, EventApi.MainTabSelect.MY_DOCTOR,true);
                    finish();
                    break;
                default:

            }

        } else {
            ToastMananger.showToast(this, R.string.pay_fail, Toast.LENGTH_SHORT);
        }
    }


    private void confirmOrder(final String orderNo, final int combo) {
        HttpOrders.Confirm confirm = new HttpOrders.Confirm(orderNo, combo, mConsignee, new HttpListener<Order>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("confirmOrder", code, msg));
                }
                EmptyViewUtils.showErrorView((ViewGroup) root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmOrder(orderNo, combo);
                    }
                });
            }

            @Override
            public void onSuccess(Order order) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("confirmOrder", PUtils.toJson(order)));
                }
                mServiceMoneyTxt.setText("¥"+order.mTotalFee);
                mTotalMoney = Double.valueOf(order.mTotalFee);
                getAccountDetail();
                if (order.mOrderState == 1) {
                    EventApi.Pay pay = new EventApi.Pay();
                    pay.success = true;
                    pay.repeat = true;
                    EventBus.getDefault().post(pay);
                }
            }
        });

        new HttpClient(this, confirm).start();
    }

    private void getAccountDetail(){
        showLoadDialog(R.string.get_account_info);
        AccountEvent.GetAccountDetail detail = new AccountEvent.GetAccountDetail(new HttpListener<AccountDetail>() {
            @Override
            public void onError(int code, String msg) {
                EmptyViewUtils.removeAllView((ViewGroup) root);
                ToastUtils.showShort(PayActivity.this, getString(R.string.get_my_account_info_fail));
                dismissLoadDialog();
            }

            @Override
            public void onSuccess(AccountDetail accountDetail) {
                Log.d(TAG, DebugUtils.successFormat("accountDetail", PUtils.toJson(accountDetail)));
                EmptyViewUtils.removeAllView((ViewGroup) root);
                dismissLoadDialog();
                mMoneyTxt.setText(getString(R.string.balance)+String.valueOf(accountDetail.mAvailable)+getString(R.string.money_unit));
                accountMoney = accountDetail.mAvailable;
                isSetPayPassword = accountDetail.mHavePayPassword;
                if (mTotalMoney <= accountMoney){
                    mAssetsPay.setVisibility(View.VISIBLE);
                    mAssetsPay.performClick();
                }else{
                    mAssetsPay.setVisibility(View.GONE);
                    mAlipayPay.performClick();
                }
            }
        });
        new HttpClient(this, detail).start();
    }

}
