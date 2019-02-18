package com.kmwlyy.patient.module.myconsult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.kmwlyy.patient.R;
import com.kmwlyy.patient.weight.TimeUtils;

import java.util.List;


/**
 * Created by Administrator on 2017/8/14.
 */

public class MyConsultAdapter extends BaseAdapter {
    private Context mContext;
    private List<MyConsultBean.ResultDataBean> data;

    public MyConsultAdapter(Context context, List<MyConsultBean.ResultDataBean> data) {
        mContext = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_consults, viewGroup, false);
            holder.mTvTitle = (TextView) view.findViewById(R.id.tv_title);
            holder.mTvState = (TextView) view.findViewById(R.id.tv_state);
            holder.mTvOfPatient = (TextView) view.findViewById(R.id.tv_of_patient);
            holder.mSeeingTheDoctor = (TextView) view.findViewById(R.id.seeing_the_doctor);
            holder.mSubscribeTimeFrame = (TextView) view.findViewById(R.id.subscribe_time_frame);
            holder.mIllnessDescribe = (TextView) view.findViewById(R.id.illness_describe);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.mTvOfPatient.setText(data.get(position).getResidentname());
        long createtime = data.get(position).getCreatetime();
        String createtimeStr = TimeUtils.Long2DataString(createtime, "yyyy-MM-dd HH:mm");
        holder.mSubscribeTimeFrame.setText(createtimeStr);
        holder.mSeeingTheDoctor.setText(data.get(position).getDoctorname());
        holder.mIllnessDescribe.setText(data.get(position).getContent());

        return view;
    }


    static class ViewHolder {
        private TextView mTvTitle;
        private TextView mTvState;
        private TextView mTvOfPatient;
        private TextView mSeeingTheDoctor;
        private TextView mSubscribeTimeFrame;
        private TextView mIllnessDescribe;

        private TextView mTvAmount;
        private TextView mCancelOrder;
        private TextView mDeleteOrder;
        private TextView mTvEvaluate;


    }
}
