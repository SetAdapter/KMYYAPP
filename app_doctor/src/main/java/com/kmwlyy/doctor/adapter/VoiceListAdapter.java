package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.ConsultBeanNew;
import com.kmwlyy.doctor.model.OPDRegister;
import com.kmwlyy.doctor.model.OrderVoiceBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;

import java.util.List;

public class VoiceListAdapter extends CommonAdapter<ConsultBeanNew> {

	private Context context;
	private LayoutInflater inflater;
	private List<ConsultBeanNew> list;

	public VoiceListAdapter(Context context, List<ConsultBeanNew> list) {
		super(context, R.layout.item_voice, list);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void setData(List<ConsultBeanNew> list){
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<ConsultBeanNew> list){
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public ConsultBeanNew getItem(int position) {
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

		final ConsultBeanNew bean = list.get(position);
		holder.tv_type.setText(Integer.parseInt(bean.Room.ServiceType) ==3?context.getResources().getString(R.string.string_video):context.getResources().getString(R.string.string_voice));
		holder.tv_name.setText(bean.Member.MemberName);
		holder.tv_time.setText(bean.OPDDate.substring(0,10) + " " + bean.Schedule.StartTime + " ~ " + bean.Schedule.EndTime);
		holder.tv_fee.setText("¥"+bean.Order.getTotalFee());
		holder.tv_desc.setText(bean.ConsultContent);


		// 订单状态（-1待确认，0待支付，1已支付，2已完成，3已取消）
		switch (bean.Order.getOrderState()){
			case -1:
				holder.tv_state.setText("待确认");
				holder.tv_state.setTextColor(context.getResources().getColor(R.color.color_main_yellow));
				break;
			case 0:
				holder.tv_state.setText("待支付");
				holder.tv_state.setTextColor(context.getResources().getColor(R.color.color_main_yellow));
				break;
			case 1:
				holder.tv_state.setText("已支付");
				holder.tv_state.setTextColor(context.getResources().getColor(R.color.app_color_main));
				break;
			case 2:
				holder.tv_state.setText("已完成");
				holder.tv_state.setTextColor(context.getResources().getColor(R.color.color_sub_string));
				break;

			case 3:
				holder.tv_state.setText("已取消");
				holder.tv_state.setTextColor(context.getResources().getColor(R.color.color_sub_string));
				break;


		}


		return convertView;
	}

	@Override
	public void convert(com.winson.ui.widget.ViewHolder viewHolder, ConsultBeanNew obj, int position) {

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
