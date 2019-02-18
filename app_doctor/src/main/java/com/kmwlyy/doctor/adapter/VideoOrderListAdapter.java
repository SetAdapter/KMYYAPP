package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.OrderVoiceBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.CommonAdapter;

import java.util.List;

public class VideoOrderListAdapter extends CommonAdapter<OrderVoiceBean> {

    private Context context;
    private LayoutInflater inflater;
    private List<OrderVoiceBean> list;

    public VideoOrderListAdapter(Context context, List<OrderVoiceBean> list) {
        super(context, R.layout.item_voice, list);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void setData(List<OrderVoiceBean> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(List<OrderVoiceBean> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public OrderVoiceBean getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_voice, null);
            ViewUtils.inject(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final OrderVoiceBean bean = list.get(position);
        holder.tv_type.setText(bean.getOPDType() ==3?context.getResources().getString(R.string.string_video):context.getResources().getString(R.string.string_voice));
        holder.tv_state.setText(MyUtils.getState(context,""+bean.getOrderState(),holder.tv_state));
        holder.tv_name.setText(bean.getMemberName());
        holder.tv_time.setText(bean.getOPDDate().substring(0,10) + " " + bean.getSchedule().getStartTime() + " ~ " + bean.getSchedule().getEndTime());
        holder.tv_desc.setText(bean.getConsultContent());
        holder.tv_fee.setText("￥"+bean.getPrice());

        return convertView;
    }

    @Override
    public void convert(com.winson.ui.widget.ViewHolder viewHolder, OrderVoiceBean obj, int position) {

    }

    private class ViewHolder {
        @ViewInject(R.id.tv_type)
        private TextView tv_type;

        @ViewInject(R.id.tv_state)
        private TextView tv_state;

        @ViewInject(R.id.tv_name)
        private TextView tv_name;

        @ViewInject(R.id.tv_time)
        private TextView tv_time;

        @ViewInject(R.id.tv_desc)
        private TextView tv_desc;

        @ViewInject(R.id.tv_fee)
        private TextView tv_fee;

    }

}
