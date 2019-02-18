package com.kmwlyy.patient.module.familydoctorteam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Stefan on 2017/7/29.
 */

public class DoctorTeamAdapter extends BaseAdapter {

    Context context;
    List<NewChangeDoctorTeam.DataBean> list;

    public DoctorTeamAdapter(Context context, List<NewChangeDoctorTeam.DataBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
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
    public View getView(int position, View view, ViewGroup parent) {
        MyHolder holder = null;
        if (view == null) {
            holder = new MyHolder();
            view = LayoutInflater.from(context).inflate(R.layout.doctor_search, null);
            holder.signed = (TextView) view.findViewById(R.id.signed);
            holder.name_tv = (TextView) view.findViewById(R.id.name_tv);
            holder.signNum = (TextView) view.findViewById(R.id.signNum);
            holder.team_name = (TextView) view.findViewById(R.id.team_name);
            holder.brief_tv = (TextView) view.findViewById(R.id.brief_tv);
            holder.image_tv= (ImageView) view.findViewById(R.id.image_tv);

            view.setTag(holder);
        } else {
            holder = (MyHolder) view.getTag();
        }
        holder.name_tv.setText(list.get(position).getGroupInfo().getLeaderName());
        holder.team_name.setText(list.get(position).getGroupInfo().getGroupName());
        holder.signNum.setText(list.get(position).getGroupInfo().getSignatureCount()+"");
        holder.brief_tv.setText(list.get(position).getGroupInfo().getLeaderSpecialty());
        ImageLoader.getInstance().displayImage(list.get(position).getGroupInfo().getLeaderPhotoUrl() ,holder.image_tv, LibUtils.getSquareDisplayOptionsServicePackage());

//        holder.signed.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(finalHolder.signed.getText().equals("已签约")){
//                    context.startActivity(new Intent(context, SignedActivity.class));
//                }else {
//                    context.startActivity(new Intent(context, MaySignActivity.class));
//                }
//            }
//        });
//
//        if (list.get(position).getSignstatus()==1){
//            holder.signed.setText("已签约");
//        }else {
//            holder.signed.setText("未签约");
//        }
//        holder.name_tv.setText(list.get(position).getDoctorgroupname());
//        holder.signNum.setText(list.get(position).getSigncount() +"");
//        holder.team_name.setText(list.get(position).getTeamleader());
//        holder.brief_tv.setText(list.get(position).getDoctorgroupdesc());
        return view;
    }

    class MyHolder {
        TextView signed, name_tv, signNum, team_name, brief_tv;
        ImageView image_tv;
    }
}
