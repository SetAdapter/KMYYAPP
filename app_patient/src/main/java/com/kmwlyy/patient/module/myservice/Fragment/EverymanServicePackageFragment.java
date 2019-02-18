package com.kmwlyy.patient.module.myservice.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.module.myservice.Adapter.ServiceAdapter;
import com.kmwlyy.patient.module.myservice.Bean.Doorservoce;
import com.kmwlyy.patient.module.myservice.Bean.Http_Services_Package;
import com.kmwlyy.patient.module.myservice.ServicePackageDetailActivity;
import com.kmwlyy.patient.weight.BaseFragment;

import java.util.List;

import butterknife.BindView;


/**
 * Created by xcj on 2017/8/8.
 */

public class EverymanServicePackageFragment extends BaseFragment {

    @BindView(R.id.gridView)
    GridView mGridView;
    private ServiceAdapter mAdapter;
    private String type;


    @Override
    protected int getContentLayout() {
        return R.layout.fragment_everyman_service_package;
    }

    @Override
    protected void baseInitView() {
        type = getArguments().getString("Type");
        getDoorService("1", "10", "1", type);//当前页数,分页大小,服务部类型
        initView();
    }


    private void initView() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Doorservoce.DataBean bean = (Doorservoce.DataBean) view.getTag();
                Intent intent = new Intent(getActivity(), ServicePackageDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("packageID", bean.getPackageID());//服务套餐编号
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private void getDoorService(String CurrentPage, String PageSize, String PackageType, String Type) {
        Http_Services_Package.GetServicePackageList event = new Http_Services_Package.GetServicePackageList(CurrentPage, PageSize, PackageType, Type, new HttpListener<List<Doorservoce.DataBean>>() {
            @Override
            public void onError(int code, String msg) {
                Log.i("packageID", msg + code);
                Toast.makeText(getActivity(), msg + code, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(List<Doorservoce.DataBean> dataBeen) {
                if (dataBeen != null) {
                    mAdapter = new ServiceAdapter(getActivity(), dataBeen);
                    mGridView.setAdapter(mAdapter);
//                    mAdapter.setData(dataBeen);
                }
            }

        });
        NetService.createClient(getActivity(), HttpClient.FAMILY_URL, event).start();
    }

}
