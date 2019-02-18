package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.OfflineDayTimeBean;
import com.kmwlyy.doctor.model.httpEvent.OfflineBookingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by TFeng on 2017/7/28.
 */

public class OfflineBookingAdapter extends BaseAdapter {

    private Context mContext;
    private List<OfflineDayTimeBean> data;
    private GridViewNameAdapter mAdapter;

    public OfflineBookingAdapter(Context context, List<OfflineDayTimeBean> dataMap){
        mContext = context;
        data = dataMap;
    }

    @Override
    public int getCount() {

        if(data != null && data.size()>0){
            return data.size();
        }
        return 0;
    }

    @Override
    public OfflineDayTimeBean getItem(int i) {

        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int postion, View convertView, ViewGroup viewGroup) {

        convertView = View.inflate(mContext, R.layout.item_offline_booking,null);
        TextView dayTime  = (TextView) convertView.findViewById(R.id.tv_offline);
        GridView name = (GridView)convertView.findViewById(R.id.gv_offline);

        OfflineDayTimeBean offlineDayTimeBean = getItem(postion);

        dayTime.setText(offlineDayTimeBean.getDayTime());

        List<OfflineBookingBean> list = offlineDayTimeBean.getList();
        mAdapter = new GridViewNameAdapter(mContext,list);
        name.setAdapter(mAdapter);

        return convertView;
    }
    public void replaceData(List<OfflineDayTimeBean> list){
        data = new ArrayList<>();
        data = list;
        notifyDataSetChanged();
    }
}
