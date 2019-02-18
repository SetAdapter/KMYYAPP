package com.kangmeitongu.main.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 支付页面
 */
public class WXPayEntryActivity extends BaseActivity implements
        IWXAPIEventHandler {

    private static final String TAG = WXPayEntryActivity.class.getSimpleName();
    public static final String WEIXIN_APP_ID = "wxf1b0cceac4c331e3";

    @BindView(R.id.result)
    TextView mResult;

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        ButterKnife.bind(this);

        api = WXAPIFactory.createWXAPI(this, WEIXIN_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (DebugUtils.debug) {
            Log.d(TAG, "onPayFinish result" + DebugUtils.toJson(resp));
        }
        EventApi.Pay pay = new EventApi.Pay();
        if (resp.errStr == null && resp.errCode == 0) {
            pay.success = true;
        } else {
            pay.success = false;
        }
        EventBus.getDefault().post(pay);
        finish();
    }

}
