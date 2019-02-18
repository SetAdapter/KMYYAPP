package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kmwlyy.doctor.Activity.BaseInfoActivity;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.httpEvent.OfflineBookingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TFeng on 2017/7/28.
 */

public class GridViewNameAdapter extends BaseAdapter {
    private Context mContext;
    private List<OfflineBookingBean> mDataList;

    public GridViewNameAdapter(Context context,List<OfflineBookingBean> dataList){
        mContext = context;
        mDataList = dataList;
    }

    @Override
    public int getCount() {

        if(mDataList != null && mDataList.size()>0){
            return mDataList.size();
        }

        return 0;
    }

    @Override
    public OfflineBookingBean getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = View.inflate(mContext, R.layout.item_name,null);
        TextView name = (TextView) view.findViewById(R.id.tv_name);
        name.setText(getItem(i).getUserMember().getMemberName());
        return view;
    }
    public void replaceData(List<OfflineBookingBean> list){
        mDataList = new ArrayList<>();
        mDataList = list;
        notifyDataSetChanged();
    }
}
