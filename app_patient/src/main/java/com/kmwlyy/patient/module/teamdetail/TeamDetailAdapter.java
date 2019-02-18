package com.kmwlyy.patient.module.teamdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.module.familydoctorteam.ChangeDoctorTeam;
import com.kmwlyy.patient.module.familydoctorteam.NewChangeDoctorTeam;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Stefan on 2017/7/30.
 */

public class TeamDetailAdapter extends BaseAdapter {
    Context context;
    List<NewChangeDoctorTeam.DataBean.GroupInfoBean.DoctorGroupMembersBean> doctorList;

    public TeamDetailAdapter(Context context, List<NewChangeDoctorTeam.DataBean.GroupInfoBean.DoctorGroupMembersBean> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
    }


    @Override
    public int getCount() {
        return doctorList.size();
    }

    @Override
    public Object getItem(int position) {
        return doctorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        MyHolder holder=null;
        if(view==null){
            holder=new MyHolder();
            view=LayoutInflater.from(context).inflate(R.layout.team_detail,parent,false);
            holder.doctor_name= (TextView) view.findViewById(R.id.doctor_name);
            holder.information= (TextView) view.findViewById(R.id.information);
            holder.doctor_img= (ImageView) view.findViewById(R.id.doctor_img);
            holder.dept_name= (TextView) view.findViewById(R.id.dept_name);
            view.setTag(holder);
        }else {
            holder= (MyHolder) view.getTag();
        }
        holder.doctor_name.setText(doctorList.get(position).getDoctorName());//医生名字
        //职称部门
        holder.dept_name.setText(doctorList.get(position).getTitleName());
        holder.information.setText(doctorList.get(position).getSpecialty());//擅长

        ImageLoader.getInstance().displayImage(doctorList.get(position).getPhotoUrl() ,holder.doctor_img, LibUtils.getSquareDisplayOptionsServicePackage());

        return view;
    }

    class MyHolder{
        TextView doctor_name,information,dept_name;
        ImageView doctor_img;
    }
}
