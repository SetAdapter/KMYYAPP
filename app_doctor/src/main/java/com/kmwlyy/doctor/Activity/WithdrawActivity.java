package com.kmwlyy.doctor.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.View.ConfirmPayPasswordDialog;
import com.kmwlyy.doctor.model.BankBean;
import com.kmwlyy.doctor.model.EventAccountApi;
import com.kmwlyy.doctor.model.UsedBankBean;
import com.kmwlyy.doctor.model.httpEvent.AccountEvent;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import cn.qqtheme.framework.picker.OptionPicker;


/**
 * Created by xcj on 2016/12/8.
 */

public class WithdrawActivity extends BaseActivity implements View.OnClickListener{
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_center)
    TextView tv_title;
    @ViewInject(R.id.et_money)
    EditText mMoneyEdit;
    @ViewInject(R.id.ll_bank_name)
    LinearLayout mBankNameLinearLayout;
    @ViewInject(R.id.tv_bank_name)
    TextView mBankNameTxt;
    @ViewInject(R.id.confirm)
    Button mConfirm;
    @ViewInject(R.id.et_name)
    EditText mName;
    @ViewInject(R.id.et_bank_number)
    EditText mBankNumber;

    private String[] option = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ViewUtils.inject(this);
        tv_title.setText("提现");
        btn_left.setVisibility(View.VISIBLE);
        tv_title.setVisibility(View.VISIBLE);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBankNameLinearLayout.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        getUseBankList();
        getBankList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_bank_name:
                OptionPicker picker = new OptionPicker(this,option);
                picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(String option) {
                        mBankNameTxt.setText(option);
                    }
                });
                picker.show();
                break;
            case R.id.confirm:
                withdraw();
                break;
        }
    }

    private void withdraw(){
        final String money = mMoneyEdit.getText().toString();
        if(TextUtils.isEmpty(money)){
            ToastUtils.showShort(WithdrawActivity.this, "提现金额不能为空");
            return;
        }
        if (Double.valueOf(money) < 100){
            ToastUtils.showShort(WithdrawActivity.this, "提现金额不能低于100元");
            return;
        }
        if(TextUtils.isEmpty(mName.getText().toString())){
            ToastUtils.showShort(WithdrawActivity.this, "请输入姓名");
            return;
        }
        if(TextUtils.isEmpty(mBankNumber.getText().toString())){
            ToastUtils.showShort(WithdrawActivity.this, "请输入卡号");
            return;
        }
        if(TextUtils.isEmpty(mBankNameTxt.getText().toString())){
            ToastUtils.showShort(WithdrawActivity.this, "请选择银行");
            return;
        }
        final ConfirmPayPasswordDialog dialog = new ConfirmPayPasswordDialog(this,R.style.CommonDialog);
        dialog.show();
        dialog.setClicklistener(new ConfirmPayPasswordDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                String psw = dialog.getSecPwd();
                withdrawInfo(money, psw);
                dialog.dismiss();
            }

            @Override
            public void doCancel() {
                dialog.dismiss();
            }

            @Override
            public void forget() {
                Intent set = new Intent(WithdrawActivity.this, SetPaymentCodeActivity.class);
                set.putExtra(SetPaymentCodeActivity.IS_SET_PAY_PASSWORD, true);
                startActivity(set);
                dialog.dismiss();
            }
        });
    }

    private void withdrawInfo(String money, String psw){
        showLoadDialog(R.string.withdraw_info);
        AccountEvent.Withdraw withdraw = new AccountEvent.Withdraw(money, mBankNameTxt.getText().toString(), "", mName.getText().toString(), mBankNumber.getText().toString(), psw, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(WithdrawActivity.this,msg);
                dismissLoadDialog();
            }

            @Override
            public void onSuccess(Object o) {
                ToastUtils.showShort(WithdrawActivity.this,"提现成功");
                dismissLoadDialog();
                EventBus.getDefault().post(new EventAccountApi.WithdrawSuc());
                finish();
            }

        });
        HttpClient getOrderClient = NetService.createClient(this, withdraw);
        getOrderClient.start();
    }

    private void getUseBankList(){
        AccountEvent.GetBankCardList usedBnakList = new AccountEvent.GetBankCardList(new HttpListener<ArrayList<UsedBankBean>>() {
            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onSuccess(ArrayList<UsedBankBean> usedBankBeen) {

            }
        });
        HttpClient getOrderClient = NetService.createClient(this, usedBnakList);
        getOrderClient.start();
    }

    private void getBankList(){
        AccountEvent.GetBankList bankList = new AccountEvent.GetBankList(new HttpListener<ArrayList<BankBean>>() {
            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onSuccess(ArrayList<BankBean> bankBean) {
                ArrayList arrayList = new ArrayList();
                for(BankBean bankBean1: bankBean){
                    arrayList.add(bankBean1.mBankName);
                }
                option = (String[])arrayList.toArray(new String[arrayList.size()]);
            }
        });
        HttpClient getOrderClient = NetService.createClient(this, bankList);
        getOrderClient.start();
    }
}
