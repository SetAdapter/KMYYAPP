package com.kmwlyy.patient.module.myservice.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.module.myservice.Bean.ServicePackageDetailBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by xcj on 2017/8/9.
 */

public class ServicePackageItemAdapter extends BaseAdapter {
    Context context;
    List<ServicePackageDetailBean> datas;

    public ServicePackageItemAdapter(Context context, List<ServicePackageDetailBean> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_service, null);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.detail = (TextView) convertView.findViewById(R.id.detail);
            viewHolder.text_price = (TextView) convertView.findViewById(R.id.text_price);
            viewHolder.text_buy = (TextView) convertView.findViewById(R.id.text_buy);
            viewHolder.image_pic = (ImageView) convertView.findViewById(R.id.image_pic);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(datas.get(position).servicename);
        viewHolder.detail.setText(datas.get(position).servicedesc);
        viewHolder.text_price.setText(datas.get(position).serviceprice + "元");
        viewHolder.text_buy.setText(datas.get(position).totalbuycount + "人已购");
        ImageLoader.getInstance().displayImage(datas.get(position).imageurl, viewHolder.image_pic, LibUtils.getSquareDisplayOptionsServicePackage());
        return convertView;
    }

    class ViewHolder {
        TextView title;
        TextView detail;
        TextView text_price;
        TextView text_buy;
        ImageView image_pic;
    }
}
