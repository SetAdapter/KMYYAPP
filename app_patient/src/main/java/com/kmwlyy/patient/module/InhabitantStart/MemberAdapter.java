package com.kmwlyy.patient.module.InhabitantStart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.kmwlyy.core.net.bean.UserMember;
import com.kmwlyy.patient.R;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */

public class MemberAdapter extends BaseAdapter {
    private Context mContext;
    private List<MySignInformBean.MembersBean> mMemberbeanList;
    private boolean ischange = true;

    public MemberAdapter(Context context, List<MySignInformBean.MembersBean>   memberbeanList) {
        mContext = context;
        mMemberbeanList = memberbeanList;
    }

    @Override
    public int getCount() {
        return mMemberbeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMemberbeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final MyViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_member, viewGroup, false);
            holder = new MyViewHolder();
            holder.member_name_edt = (TextView) view.findViewById(R.id.member_name_edt);
            holder.member_relation_edt = (TextView) view.findViewById(R.id.member_relation_edt);
            holder.member_number = (TextView) view.findViewById(R.id.member_number);
            view.setTag(holder);
        } else {
            holder = (MyViewHolder) view.getTag();
        }
        holder.member_name_edt.setText(mMemberbeanList.get(position).getMemberName());
        int relation = mMemberbeanList.get(position).getRelation();
        if (relation == 0) { //成员关系 （0-自己、1-配偶、2-父亲、3-母亲、4-儿子、5女儿、6-其他）
            holder.member_relation_edt.setText("自己");
        } else if (relation == 1) {
            holder.member_relation_edt.setText("配偶");
        } else if (relation == 2) {
            holder.member_relation_edt.setText("父亲");
        } else if (relation == 3) {
            holder.member_relation_edt.setText("母亲");
        } else if (relation == 4) {
            holder.member_relation_edt.setText("儿子");
        } else if (relation == 5) {
            holder.member_relation_edt.setText("女儿");
        } else if (relation == 6) {
            holder.member_relation_edt.setText("其他");
        }

        holder.member_number.setText(mMemberbeanList.get(position).getIDNumber());

        return view;
    }


    private class MyViewHolder {
        private TextView member_name_edt;
        private TextView member_relation_edt;
        private TextView member_number;
    }

}
