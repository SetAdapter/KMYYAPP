package com.kmwlyy.patient.account;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.core.util.Validator;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.BankBean;
import com.kmwlyy.patient.helper.net.bean.UsedBankBean;
import com.kmwlyy.patient.helper.net.event.AccountEvent;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.winson.ui.widget.RateLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Created by xcj on 2016/12/8.
 */

public class WithdrawActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.et_money)
    EditText mMoneyEdit;
    @BindView(R.id.ll_bank_name)
    LinearLayout mBankNameLinearLayout;
    @BindView(R.id.tv_bank_name)
    TextView mBankNameTxt;
    @BindView(R.id.confirm)
    Button mConfirm;
    @BindView(R.id.et_name)
    EditText mName;
    @BindView(R.id.et_bank_number)
    EditText mBankNumber;
    @BindView(R.id.lv_history)
    ListView mWithdrawHistory;
    @BindView(R.id.ll_withdraw_history)
    LinearLayout mHistoryTitle;
    @BindView(R.id.tv_limit)
    TextView mLimitTxt;

    private String[] option = {};
    private double mCashMinLimit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        butterknife.ButterKnife.bind(this);
        tv_center.setText(getString(R.string.withdraw));
        mBankNameLinearLayout.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mHistoryTitle.setVisibility(View.GONE);
        mCashMinLimit = getIntent().getDoubleExtra(AccountManagementActivity.CASE_MIN_LIMIT, 0);
        mLimitTxt.setText(getString(R.string.withdraw_money_min_limit)+mCashMinLimit);
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
            ToastUtils.showShort(WithdrawActivity.this, getString(R.string.input_withdraw_money_not_empty));
            return;
        }
        if (Double.valueOf(money) < mCashMinLimit){
            ToastUtils.showShort(WithdrawActivity.this, getString(R.string.input_withdraw_money_min_limit)+mCashMinLimit+getString(R.string.money_unit));
            return;
        }
        if(TextUtils.isEmpty(mName.getText().toString())){
            ToastUtils.showShort(WithdrawActivity.this, getString(R.string.plz_input_name));
            return;
        }
        if(TextUtils.isEmpty(mBankNumber.getText().toString())){
            ToastUtils.showShort(WithdrawActivity.this, getString(R.string.plz_input_bank_no));
            return;
        }
        if(TextUtils.isEmpty(mBankNameTxt.getText().toString())){
            ToastUtils.showShort(WithdrawActivity.this, getString(R.string.plz_choose_bank));
            return;
        }
        if (!Validator.checkBankCard(mBankNumber.getText().toString())){
            ToastUtils.showShort(WithdrawActivity.this, getString(R.string.plz_input_correct_bank_no));
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
                ToastUtils.showShort(WithdrawActivity.this, getString(R.string.withdraw_suc));
                dismissLoadDialog();
                EventBus.getDefault().post(new EventApi.WithdrawSuc());
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
                mHistoryTitle.setVisibility(View.GONE);
                ToastUtils.showShort(WithdrawActivity.this, getString(R.string.get_withdraw_history_fail));
            }

            @Override
            public void onSuccess(final ArrayList<UsedBankBean> usedBankBeen) {
                if (usedBankBeen != null&& usedBankBeen.size() != 0) {
                    mHistoryTitle.setVisibility(View.VISIBLE);
                    mWithdrawHistory.setAdapter(new HistoryAdapter(WithdrawActivity.this, usedBankBeen));
                    fixListViewHeight(mWithdrawHistory);
                    mWithdrawHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mName.setText(usedBankBeen.get(position).mAccountName);
                            mBankNameTxt.setText(usedBankBeen.get(position).mBank);
                            mBankNumber.setText(usedBankBeen.get(position).mCardCode);
                        }
                    });
                }
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

    public class HistoryAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private List<UsedBankBean> datas;


        public HistoryAdapter(Context context, List<UsedBankBean> datas){
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            HistoryViewHolder holder;
            if (convertView == null) {
                holder= new HistoryViewHolder();
                convertView = mInflater.inflate(R.layout.layout_withdraw_history, null);
                holder.mName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.mBankName = (TextView) convertView.findViewById(R.id.tv_bank_name);
                holder.mBankNumber = (TextView) convertView.findViewById(R.id.tv_bank_number);
                convertView.setTag(holder);
            }else {
                holder = (HistoryViewHolder)convertView.getTag();
            }
            holder.mName.setText(datas.get(position).mAccountName);
            holder.mBankNumber.setText(datas.get(position).mCardCode);
            holder.mBankName.setText(datas.get(position).mBank);
            return convertView;
        }
    }

    public final class HistoryViewHolder{
        public TextView mName;
        public TextView mBankName;
        public TextView mBankNumber;
    }

    public void fixListViewHeight(ListView listView) {
        // 如果没有设置数据适配器，则ListView没有子项，返回。
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {
            return;
        }
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listViewItem = listAdapter.getView(i , null, listView);
            // 计算子项View 的宽高
            listViewItem.measure(0, 0);
            // 计算所有子项的高度和
            totalHeight += listViewItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // listView.getDividerHeight()获取子项间分隔符的高度
        // params.height设置ListView完全显示需要的高度
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
