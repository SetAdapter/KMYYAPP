package com.kmwlyy.patient.module.myservice.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.iflytek.thirdparty.P;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.NewHttpClient;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.module.myservice.Adapter.ServiceAdapter;
import com.kmwlyy.patient.module.myservice.BaseServiceActivity;
import com.kmwlyy.patient.module.myservice.Bean.Doorservoce;
import com.kmwlyy.patient.module.myservice.Bean.Http_serviceList;
import com.kmwlyy.patient.weight.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by xcj on 2017/8/10.
 * 上门服务
 */

public class VisitingServiceFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {

    @BindView(R.id.gridView)
    GridView mGridView;
    private ServiceAdapter mAdapter;
    private ProgressDialog mLoadingDialog;// 加载框

    @Override
    protected int getContentLayout() {
        return R.layout.activity_doorservices;
    }

    @Override
    protected void baseInitView() {
        getDoorService("1", "10", "1");//当前页数,分页大小,服务包类型
        mGridView.setOnItemClickListener(this);
    }

    private void getDoorService(String CurrentPage, String PageSize, String PackageType) {
//        mLoadingDialog = ProgressDialog.show(mContext, null, getString(R.string.loading_dialog),false,true);
        Http_serviceList event = new Http_serviceList(CurrentPage, PageSize, PackageType, new HttpListener<List<Doorservoce.DataBean>>() {
            @Override
            public void onError(int code, String msg) {
                Log.i("packageID", msg + code);
                dismissLoadDialog();
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

    @Override
    public void onResume() {
        super.onResume();
        getDoorService("1", "10", "1");
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_tools_left:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Doorservoce.DataBean bean = (Doorservoce.DataBean) view.getTag();
        Intent intent = new Intent(getActivity(), BaseServiceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("packageID", bean.getPackageID());//服务套餐编号
        bundle.putInt("BuyUserNum", bean.getBuyUserNum());//已购买用户数
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void dismissLoadDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
}
