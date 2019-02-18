package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.AccountBalanceInfoBean;
import com.kmwlyy.doctor.model.BankCardBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getBalanceInfo_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_getBankCardList_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class YueActivity extends BaseActivity {
    private final String TAG = "YueActivity";

    //返回
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.lay_bank_card)
    LinearLayout lay_bank_card;
    @ViewInject(R.id.lay_money)
    LinearLayout lay_money;
    @ViewInject(R.id.tv_balanceTotal)
    TextView tv_balanceTotal;
    @ViewInject(R.id.tv_availableBalance)
    TextView tv_availableBalance;

    private  AccountBalanceInfoBean balanceBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yue);
        ViewUtils.inject(this);

        ((TextView)findViewById(R.id.tv_center)).setText(getResources().getString(R.string.string_yue));
        btn_left.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(this);
        lay_bank_card.setOnClickListener(this);
        lay_money.setOnClickListener(this);

        getData();
    }

    //用请求回来的数据更新界面
    private void updateView()
    {
        if (balanceBean == null)
        {
            ToastUtils.showShort(mContext,getResources().getString(R.string.string_apptoken));
            finish();
            return;
        }

        try{

            String balanceStr = String.format("%.2f", balanceBean.Balance);
            String availableStr = String.format("%.2f", balanceBean.Available);
            tv_balanceTotal.setText("￥"+balanceStr);
            tv_availableBalance.setText("￥"+availableStr);
        }
        catch (Exception e){
            ToastUtils.showShort(mContext,getResources().getString(R.string.string_apptoken));
            finish();
        }

    }

    private void getData()
    {
        showLoadDialog(R.string.string_wait);
        Http_getBalanceInfo_Event event = new Http_getBalanceInfo_Event(new HttpListener<AccountBalanceInfoBean>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_apptoken));
                ToastUtils.showShort(mContext,msg);
                finish();
            }

            @Override
            public void onSuccess(AccountBalanceInfoBean bean) {
                balanceBean = bean;
                dismissLoadDialog();
                updateView();
            }
        });



        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();

    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()){
            case R.id.tv_left:
                finish();
                break;
            case R.id.lay_bank_card:
                startActivity(new Intent(this,BankCardListActivity.class));
                break;
            case R.id.lay_money:
                if (balanceBean!=null && balanceBean.Available > 0) {
                    Intent intent = new Intent(this, CashBalanceActivity.class);
                    //TODO 传入String的余额。
                    intent.putExtra(CashBalanceActivity.CASH_BALANCE_KEY, balanceBean.Available);
                    startActivityForResult(intent,1);
                }
                else{
                    ToastUtils.showShort(mContext,getResources().getString(R.string.string_no_money));
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == RESULT_CANCELED)
            return;
        switch (resultCode) {
            case 1012:
                //TODO 提现重新获取余额
                getData();
                break;
        }

    }
}
