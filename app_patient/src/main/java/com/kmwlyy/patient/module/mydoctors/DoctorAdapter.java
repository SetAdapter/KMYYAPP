package com.kmwlyy.patient.module.mydoctors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.kmwlyy.patient.R;

import java.util.List;

/**
 * Created by ljf on 2017/7/30.
 * 医生适配器
 */

public class DoctorAdapter extends BaseAdapter {

    private  List<MyDoctorBean.ResultDataBean> list;
    Context context;

    public DoctorAdapter(Context context, List<MyDoctorBean.ResultDataBean> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_doctor_name, null);
            holder =new MyViewHolder();
            holder.doctor_icon = (ImageView) convertView.findViewById(R.id.doctor_img);
            holder.doctor_name = (TextView) convertView.findViewById(R.id.doctor_name);
            holder.doctor_desc = (TextView) convertView.findViewById(R.id.doctor_desc);
            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }
//        Glide.with(context).load(list.get(position).getImageurl()).error(R.mipmap.ic_launcher).into(holder.doctor_icon);
        holder.doctor_name.setText(list.get(position).getDoctorname().toString());
        holder.doctor_desc.setText(list.get(position).getIntro().toString());
        return convertView;
    }

    static class MyViewHolder{
        private ImageView doctor_icon;
        private TextView doctor_name;
        private TextView doctor_desc;
    }
}
