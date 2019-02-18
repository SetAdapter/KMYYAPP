package com.kmwlyy.patient.module.myservice;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.NewHttpClient;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.module.InhabitantStart.FamilyListBean;
import com.kmwlyy.patient.module.myservice.Bean.CouponInfoBean;
import com.kmwlyy.patient.module.myservice.Bean.EventApi;
import com.kmwlyy.patient.module.myservice.Bean.Http_Services_Package;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by xcj on 2017/8/10.
 * 支付界面
 */

public class BuyServicePackageInfoActivity extends BaseActivity {
    @BindView(R.id.tv_title_center)
    TextView tv_title_center;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.tv_ServiceItemName)
    TextView tv_ServiceItemName;
    @BindView(R.id.iv_tools_left)
    Button iv_tools_left;
    @BindView(R.id.iv_tools_right)
    Button iv_tools_right;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.select_user)
    LinearLayout select_user;
    @BindView(R.id.user_member_info)
    TextView user_member_info;

    private String packageID;
    private String ServiceItemName;
    private int Price;
    private static final int REQUEST_USE = 2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_service_package_info;
    }

    @Override
    protected void afterBindView() {
        EventBus.getDefault().register(this);
        tv_title_center.setText("常规体检服务包");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_tools_right.setVisibility(View.GONE);
        submit.setOnClickListener(this);
        select_user.setOnClickListener(this);
        packageID = getIntent().getStringExtra("packageID");
        ServiceItemName = getIntent().getStringExtra("ServiceItemName");
        Price = getIntent().getIntExtra("Price", -1);
        tv_price.setText(Price + "元");
        tv_ServiceItemName.setText(ServiceItemName);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.submit:
                    startActivity(new Intent(BuyServicePackageInfoActivity.this, SelectBuyWayActivity.class));
                    submitOrder();
                break;
            case R.id.select_user:
                startActivityForResult(new Intent(BuyServicePackageInfoActivity.this, SelectVisitingPersonActivity.class), REQUEST_USE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_USE) {
            FamilyListBean.ResultDataBean bean = (FamilyListBean.ResultDataBean) data.getSerializableExtra("FamilyListBeanResultDataBean");
            user_member_info.setText(bean.getName());
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventApi.BuySuc event) {
        finish();
    }

    private void submitOrder() {
        Http_Services_Package.SubmitBuyService submitBuyService = new Http_Services_Package.SubmitBuyService(packageID, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(BuyServicePackageInfoActivity.this, msg);
            }

            @Override
            public void onSuccess(Object o) {
                ToastUtils.showShort(BuyServicePackageInfoActivity.this, "购买成功");
            }
        });
        NetService.createClient(mContext, HttpClient.FAMILY_URL, submitBuyService).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
