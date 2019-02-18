package com.kmwlyy.patient.myservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.address.net.Address;
import com.kmwlyy.address.net.AddressEvent;
import com.kmwlyy.address.page.ContainerActivity;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.pay.PayActivity;
import com.kmwlyy.patient.helper.net.bean.Consignee;
import com.kmwlyy.patient.helper.net.bean.Order;
import com.kmwlyy.patient.helper.net.event.HttpOrders;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.winson.ui.widget.EmptyViewUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by xcj on 2016/8/20.
 * 处方支付
 */
public class PrescriptionPayActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = PrescriptionPayActivity.class.getSimpleName();

    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.tv_info)
    TextView mInfo;
    @BindView(R.id.ll_consignee)
    RelativeLayout mllConsignee;
    @BindView(R.id.ll_consignee_address)
    RelativeLayout mAddress;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_phone)
    TextView tv_phone;
    @BindView(R.id.tv_address)
    TextView tv_address;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_detail)
    TextView tv_detail;
    @BindView(R.id.tv_drug_number)
    TextView tv_drug_number;
    @BindView(R.id.tv_drug_amount)
    TextView tv_drug_amount;

    private Address mAddressData = null;
    private Consignee mConsignee = new Consignee();
    private String mOrderNo = null;
    private View root = null;
    private ProgressDialog mGetServiceInfoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_pay);
        butterknife.ButterKnife.bind(this);
        root = ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        EmptyViewUtils.showLoadingView((ViewGroup) root);
        tv_center.setText(R.string.buy_prescription);
        mllConsignee.setVisibility(View.GONE);
        mAddress.setVisibility(View.GONE);
        double totalFee = getIntent().getDoubleExtra("totalFee", 0);
        String info = getIntent().getStringExtra("info");
        int num = getIntent().getIntExtra("num", 0);
        String time = getIntent().getStringExtra("time");
        mOrderNo = getIntent().getStringExtra("orderNo");
        tv_time.setText(time);
        tv_detail.setText(info);
        tv_drug_number.setText(String.valueOf(num));
        tv_drug_amount.setText("￥" + CommonUtils.convertTowDecimalStr(totalFee));
        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrescriptionPayActivity.this, ContainerActivity.class);
                intent.putExtra(ContainerActivity.TAG_FRAGMENT, ContainerActivity.ADDRESSSELECTE);
                startActivity(intent);
            }
        });
        getAddressList();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Address address) {
        mAddressData = address;
        mllConsignee.setVisibility(View.VISIBLE);
        mAddress.setVisibility(View.VISIBLE);
        tv_name.setText(address.getUserName());
        tv_phone.setText(address.getMobile());
        tv_address.setText(address.getProvinceName() + address.getCityName() + address.getAreaName() + address.getDetailAddress());
        mConsignee.mId = address.getAddressID();
    }

    private void getAddressList() {//获取地址列表
        AddressEvent.GetList event = new AddressEvent.GetList(new HttpListener<ArrayList<Address>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(PrescriptionPayActivity.this, msg);
                EmptyViewUtils.removeAllView((ViewGroup) root);
            }

            @Override
            public void onSuccess(ArrayList<Address> addresses) {
                if (addresses != null) {
                    for (int i = 0; i < addresses.size(); i++) {
                        if (addresses.get(i).isIsDefault()) {
                            mAddressData = addresses.get(i);
                            mConsignee.mId = mAddressData.getAddressID();
                            mllConsignee.setVisibility(View.VISIBLE);
                            mAddress.setVisibility(View.VISIBLE);
                            tv_name.setText(addresses.get(i).getUserName());
                            tv_phone.setText(addresses.get(i).getMobile());
                            tv_address.setText(addresses.get(i).getProvinceName() + addresses.get(i).getCityName() + addresses.get(i).getAreaName() + addresses.get(i).getDetailAddress());
                        }
                    }
                }
                EmptyViewUtils.removeAllView((ViewGroup) root);
            }
        });
        new HttpClient(this, event).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_now:
                if (mAddressData == null) {
                    ToastUtils.show(PrescriptionPayActivity.this, getResources().getString(R.string.plz_choose_shipping_address), 1);
                } else {
                    confirmOrder();
                }
                break;
        }
    }

    public void confirmOrder() {
        showGetServiceInfoDialog();
        HttpOrders.Confirm confirm = new HttpOrders.Confirm(mOrderNo, 0, mConsignee, new HttpListener<Order>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("confirm", code, msg));
                }
                ToastUtils.showShort(PrescriptionPayActivity.this,msg);
                dismissServiceInfoDialog();
            }

            @Override
            public void onSuccess(Order order) {
                Log.d(TAG, DebugUtils.successFormat("confirm", PUtils.toJson(order)));
                dismissServiceInfoDialog();
                PayActivity.startPayActivity(PrescriptionPayActivity.this, mOrderNo,order.mTotalFee+"",PayActivity.BUY_PRESCRIPTION, false);
            }
        });

        new HttpClient(this, confirm).start();
    }

    private void showGetServiceInfoDialog() {
        mGetServiceInfoDialog = new ProgressDialog(this);
        mGetServiceInfoDialog.setMessage(getResources().getString(R.string.confirm_order_ing));
        mGetServiceInfoDialog.setCancelable(false);
        mGetServiceInfoDialog.show();
    }

    private void dismissServiceInfoDialog() {
        if (mGetServiceInfoDialog != null) {
            mGetServiceInfoDialog.dismiss();
        }
    }
}
