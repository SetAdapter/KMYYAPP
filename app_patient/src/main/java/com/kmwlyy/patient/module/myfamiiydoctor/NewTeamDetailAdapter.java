package com.kmwlyy.patient.module.myfamiiydoctor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.net.event.HttpUserConsults;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.module.familydoctorteam.ChangeDoctorTeam;
import com.kmwlyy.patient.onlinediagnose.BuyIMConsultActivity;
import com.kmwlyy.patient.onlinediagnose.BuyIMConsultFamilyActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Stefan on 2017/7/30.
 */

public class NewTeamDetailAdapter extends BaseAdapter {
    Context context;
    List<MyFamilyDoctorDean.DataBean.DoctorGroupMembersBean> doctorList;

    public NewTeamDetailAdapter(Context context, List<MyFamilyDoctorDean.DataBean.DoctorGroupMembersBean> doctorList) {
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
    public View getView(final int position, View view, ViewGroup parent) {
        MyHolder holder=null;
        if(view==null){
            holder=new MyHolder();
            view=LayoutInflater.from(context).inflate(R.layout.new_team_detail,null);
            holder.doctor_name= (TextView) view.findViewById(R.id.doctor_name);
            holder.information= (TextView) view.findViewById(R.id.information);
            holder.doctor_img= (ImageView) view.findViewById(R.id.doctor_img);
            holder.submit = (TextView) view.findViewById(R.id.submit);
            holder.dept_name= (TextView) view.findViewById(R.id.dept_name);
            view.setTag(holder);
        }else {
            holder= (MyHolder) view.getTag();
        }
        holder.doctor_name.setText(doctorList.get(position).getDoctorName());
        holder.dept_name.setText(doctorList.get(position).getTitleName());
        holder.information.setText(doctorList.get(position).getSpecialty());
        ImageLoader.getInstance().displayImage(doctorList.get(position).get_PhotoUrl(),holder.doctor_img, LibUtils.getSquareDisplayOptionsServicePackage());
        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyIMConsultFamilyActivity.startBuyIMConsultFamilyActivity(context,doctorList.get(position).getDoctorID(),doctorList.get(position).getDoctorName(), HttpUserConsults.FAMILY_DOCTOR, false);
            }
        });

        return view;
    }

    class MyHolder{
        TextView doctor_name,information,submit,dept_name;
        ImageView doctor_img;
    }
}
