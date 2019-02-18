package com.kmwlyy.doctor.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.BankCardBean;
import com.kmwlyy.doctor.model.httpEvent.Http_cashBalance_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_getBankCardList_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.winson.ui.widget.AlterDialogView;

import java.util.ArrayList;
import java.util.List;


/**
 * @Description描述: 来自康美通 KMT_Doctor_0513 项目 CashBalance.java
 * @Author作者: zhaoqile
 * @Date日期: 2016/8/22
 */
public class CashBalanceActivity extends BaseActivity {
    private final String TAG = "CashBalanceActivity";
    public static final String CASH_BALANCE_KEY = "balance";
    private List<BankCardBean> dataList = new ArrayList<>();
    public BankCardBean bankCardBean;
    public ArrayAdapter<String> bankadapter;
    public Context mContext = CashBalanceActivity.this;

    //返回
    @ViewInject(R.id.tv_left)
    TextView btn_left;
    @ViewInject(R.id.tv_center)
    TextView tv_title;
    @ViewInject(R.id.tv_right)
    TextView tv_right;


    @ViewInject(R.id.currentBalance)
    TextView currentBalance;
    @ViewInject(R.id.sp_card)
    private Spinner sp_card;// 银行卡信息


    @ViewInject(R.id.et_get)
    private EditText et_get;// 提现金额


    @ViewInject(R.id.lay_operate)
    private LinearLayout lay_operate;
    @ViewInject(R.id.lay_result)
    private LinearLayout lay_result;

    private double balance = 0.0;
    public String[] str_card;
    public ArrayAdapter<String> spAdapter;

    /**
     * 提现成功后，按手机返回键时刷新界面
     */
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_balance);
        ViewUtils.inject(this);

        try {
            balance = getIntent().getDoubleExtra(CASH_BALANCE_KEY,0);
        } catch (Exception e) {
            balance = 0.00;
        }

        initView();
        getData();
    }

    private void initView() {
        btn_left.setVisibility(View.VISIBLE);
        tv_title.setText(getResources().getString(R.string.string_money));
        tv_title.setVisibility(View.VISIBLE);
        tv_right.setText(getResources().getString(R.string.string_complete));

        String result = String.format("%.2f", balance);
        currentBalance.setText(result);

        sp_card.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bankCardBean = dataList.get(i);
            }
        });
    }

    private void getData()
    {
        showLoadDialog(R.string.string_wait);

        Http_getBankCardList_Event event = new Http_getBankCardList_Event(new HttpListener<List<BankCardBean>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
            }

            @Override
            public void onSuccess(List<BankCardBean> list) {
                dismissLoadDialog();

                //更新数据
                if(null != list){
                    dataList.clear();
                    dataList.addAll(list);
                    str_card = new String[dataList.size()];
                    for(int i=0;i<dataList.size();i++){
                        String info = dataList.get(i).Bank + " " + dataList.get(i).CardCode.substring(0,4) + "****" + dataList.get(i).CardCode.substring(dataList.get(i).CardCode.length()-4);
                        str_card[i] = info;
                    }

                    spAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, str_card);
                    sp_card.setAdapter(spAdapter);
                }
            }
        });

        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();

    }


    @OnClick(R.id.tv_left)
    public void bindBack(View v) {
        finish();
    }

    @OnClick(R.id.tv_right)
    public void ok(View v) {
        setResult(1012);
        finish();
    }

    @OnClick(R.id.btn_next)
    public void next(View v) {
        Double cashAmount = 0.0;
        String cashAmountStr = et_get.getText().toString().trim();


        if (bankCardBean == null) {
            ToastUtils.show(mContext, getResources().getString(R.string.string_choose_card), Toast.LENGTH_SHORT);
            return;
        }

        try {
            cashAmount = Double.parseDouble(cashAmountStr);
            String temp[] = cashAmountStr.split("\\.");
            if (temp.length > 1) {
                if (temp[1].length() > 2) {
                    ToastUtils.show(mContext, getResources().getString(R.string.string_bank_toast1), Toast.LENGTH_SHORT);
                    return;
                }
            }
        } catch (Exception e) {
            ToastUtils.show(mContext, getResources().getString(R.string.string_bank_toast2), Toast.LENGTH_SHORT);
            return;
        }


        if (cashAmount > balance) {
            ToastUtils.show(mContext, getResources().getString(R.string.string_bank_toast3), Toast.LENGTH_SHORT);
            return;
        }

        if (cashAmount <= 0) {
            ToastUtils.show(mContext, getResources().getString(R.string.string_bank_toast4), Toast.LENGTH_SHORT);
            return;
        }


        final String cashAmountShow = String.format("%.2f", cashAmount);

        AlterDialogView.Builder builder = new AlterDialogView.Builder(CashBalanceActivity.this);
        builder.setTitle(getResources().getString(R.string.string_bank_sure));
        builder.setMessage(getResources().getString(R.string.string_bank_info1) + bankCardBean.Bank + "   "
                + bankCardBean.CardCode.substring(0, 4) + "****"
                + bankCardBean.CardCode.substring(bankCardBean.CardCode.length() - 4)
                + "\n" + getResources().getString(R.string.string_bank_info2) + "    "+ cashAmountShow + getResources().getString(R.string.string_bank_info3) + "\n"
                + getResources().getString(R.string.string_bank_info4));
        builder.setNegativeButton(getResources().getString(R.string.string_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(getResources().getString(R.string.string_yes),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        dialog.dismiss();

                        showLoadDialog(R.string.string_wait_save);

                        Http_cashBalance_Event event = new Http_cashBalance_Event(
                                cashAmountShow, bankCardBean,
                                new HttpListener<String>() {
                                    @Override
                                    public void onError(int code, String msg) {
                                        dismissLoadDialog();
                                        ToastUtils.showShort(mContext, getResources().getString(R.string.string_bank_feedback1));
                                    }

                                    @Override
                                    public void onSuccess(String o) {
                                        dismissLoadDialog();
                                        lay_operate
                                                .setVisibility(View.GONE);
                                        lay_result
                                                .setVisibility(View.VISIBLE);
                                        tv_title.setText(getResources().getString(R.string.string_bank_result));
                                        tv_right.setVisibility(View.VISIBLE);
                                        btn_left.setVisibility(View.INVISIBLE);
                                        flag = true;
                                    }
                                }
                        );


                        HttpClient httpClient = NetService.createClient(mContext, event);
                        httpClient.start();

                    }
                });
        builder.create().show();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (flag) {
                setResult(1012);
                finish();
            } else {
                finish();
            }
        }
        return false;
    }
}
