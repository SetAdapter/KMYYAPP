package com.kmwlyy.patient.module.myservice.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kmwlyy.patient.R;
import com.kmwlyy.patient.module.myservice.Bean.ServicePackageInfoBean;

import java.util.List;

/**
 * Created by Gab on 2017/8/23 0023.
 * 常规检查adapter
 */

public class ServicePackageAdapter extends AdapterBase<ServicePackageInfoBean.DataBean.DetailsBean> {

    public ServicePackageAdapter(Context context, List<ServicePackageInfoBean.DataBean.DetailsBean> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View itemView = getViewCache().get(position);
        if (null == itemView) {
            final ServicePackageInfoBean.DataBean.DetailsBean dataBean = getData().get(position);
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_service_detail, null);
            TextView tv_info = (TextView) itemView.findViewById(R.id.tv_info);
            TextView tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            tv_info.setText(dataBean.getServiceType() + "." + dataBean.getServiceItemName());
            tv_count.setText(dataBean.getServiceCount() + "次");
            itemView.setTag(dataBean);
            getViewCache().put(position, itemView);
        }
        return itemView;
    }
}
