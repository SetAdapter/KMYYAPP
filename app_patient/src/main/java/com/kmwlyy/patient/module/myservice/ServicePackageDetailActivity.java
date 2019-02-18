package com.kmwlyy.patient.module.myservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.module.myservice.Adapter.ServicePackageAdapter;
import com.kmwlyy.patient.module.myservice.Bean.EventApi;
import com.kmwlyy.patient.module.myservice.Bean.Http_Services_Package;
import com.kmwlyy.patient.module.myservice.Bean.ServicePackageInfoBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xcj on 2017/8/9.
 * 常规检查
 */

public class ServicePackageDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.tv_amount)
    TextView tv_amount;
    @BindView(R.id.tv_use)
    TextView tv_use;
    @BindView(R.id.tv_Remark)
    TextView tv_Remark;
    @BindView(R.id.tv_title_center)
    TextView tv_title_center;
    @BindView(R.id.iv_info)
    ListView mListView;
    @BindView(R.id.iv_tools_left)
    Button iv_tools_left;
    @BindView(R.id.submit)
    Button submit;

    private String packageID;
    private String ServiceItemName;
    private int Price;
    private ServicePackageAdapter mAdapter;
    private List<ServicePackageInfoBean.DataBean.DetailsBean> list = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_service_package_detail;
    }

    @Override
    protected void afterBindView() {
        EventBus.getDefault().register(this);
        tv_title_center.setText("常规检查");
        iv_tools_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        packageID = getIntent().getStringExtra("packageID");
        submit.setOnClickListener(this);
        getInfo();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.submit:
                //打开购买页面
                Intent intent = new Intent(ServicePackageDetailActivity.this, BuyServicePackageInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("packageID", packageID);//服务套餐编号
                bundle.putString("ServiceItemName",ServiceItemName);
                bundle.putInt("Price", Price);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void getInfo() {
        Http_Services_Package.GetServicePackageInfo event = new Http_Services_Package.GetServicePackageInfo(packageID, new HttpListener<ServicePackageInfoBean.DataBean>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(ServicePackageDetailActivity.this, msg);
            }

            @Override
            public void onSuccess(ServicePackageInfoBean.DataBean detailsBean) {
                if (detailsBean != null) {
                    tv_title.setText(detailsBean.getPackageName());
                    tv_amount.setText(detailsBean.getPrice() + "元/次");
                    tv_Remark.setText(detailsBean.getRemark());
                    tv_use.setText(detailsBean.getFitRemark());
                    Price = (int) detailsBean.getPrice();
                    ServiceItemName = detailsBean.getPackageName();
//                    ImageLoader.getInstance().displayImage(servicePackageInfoBean.resultData.imageurl ,image, LibUtils.getSquareDisplayOptionsServicePackage());
                    list = detailsBean.getDetails();
                    mAdapter = new ServicePackageAdapter(mContext, list);
                    mListView.setAdapter(mAdapter);
                }
            }
        });
        NetService.createClient(mContext, HttpClient.FAMILY_URL, event).start();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventApi.BuySuc event) {
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
