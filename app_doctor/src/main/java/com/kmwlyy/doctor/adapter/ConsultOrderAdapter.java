package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.ConsultBean;
import com.kmwlyy.imchat.TimApplication;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.CommonAdapter;



import java.util.List;

public class ConsultOrderAdapter extends CommonAdapter<ConsultBean> {

	private Context context;
	private LayoutInflater inflater;
	private List<ConsultBean> list;
	public ConsultOrderAdapter(Context context, List<ConsultBean> list) {
		super(context, R.layout.item_consult, list);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void setData(List<ConsultBean> list){
		this.list.clear();
		this.list.addAll(list);
	}

	public void addData(List<ConsultBean> list){
		this.list.addAll(list);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

	@Override
	public ConsultBean getItem(int position) {
		// TODO Auto-generated method stub
		return null;
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
			convertView = inflater.inflate(R.layout.item_consult, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ConsultBean consultBean = list.get(position);

		holder.tv_name.setText(consultBean.UserMember.MemberName);
		holder.tv_time.setText(consultBean.ConsultTime.substring(0,16).replace("T"," "));
		holder.tv_content.setText(consultBean.ConsultContent);
		holder.tv_type.setText(MyUtils.getConsultType(context,consultBean.ConsultType,consultBean.InquiryType));
		holder.tv_state.setText(MyUtils.getConsultState(context,""+consultBean.ConsultState,holder.tv_state));
		holder.ll_consult.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				//0-未筛选、1-未领取、2-未回复、4-已回复、5-已完成
				//如果是已完成，就不让回复。隐藏发送栏
				Boolean isChat = true;
				//ConsultState = 0,1,5 这三种的时候,不让发送内容
				if(consultBean.ConsultState.equals("5") || consultBean.ConsultState.equals("0") || consultBean.ConsultState.equals("1")){
					isChat = false;
				}
				//还有ChannelId空或者0的时候。
				if(consultBean.Room.ChannelID.equals("0")){
					isChat = false;
				}
				TimApplication.enterTimchat((AppCompatActivity)context,""+consultBean.Room.ChannelID,consultBean.UserMember.MemberName,isChat);

			}
		});
		return convertView;
	}

	@Override
	public void convert(com.winson.ui.widget.ViewHolder viewHolder, ConsultBean obj, int position) {

	}

	private class ViewHolder {
		@ViewInject(R.id.tv_name)
		private TextView tv_name;

		@ViewInject(R.id.tv_time)
		private TextView tv_time;

		@ViewInject(R.id.tv_content)
		private TextView tv_content;

		@ViewInject(R.id.tv_type)
		private TextView tv_type;

		@ViewInject(R.id.tv_state)
		private TextView tv_state;

		@ViewInject(R.id.ll_consult)
		private LinearLayout ll_consult;

	}

}
