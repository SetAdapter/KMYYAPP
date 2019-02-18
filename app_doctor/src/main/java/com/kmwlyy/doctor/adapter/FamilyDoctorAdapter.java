package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.FamilyDoctor;
import com.kmwlyy.doctor.model.OPDRegister;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;

import java.util.List;

public class FamilyDoctorAdapter extends CommonAdapter<FamilyDoctor> {

	private Context context;
	private LayoutInflater inflater;
	private List<FamilyDoctor> list;

	public FamilyDoctorAdapter(Context context, List<FamilyDoctor> list) {
		super(context, R.layout.item_familydoctor, list);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void setData(List<FamilyDoctor> list){
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<FamilyDoctor> list){
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public FamilyDoctor getItem(int position) {
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
			convertView = inflater.inflate(R.layout.item_familydoctor, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		FamilyDoctor familyDoctor = list.get(position);
		//服务时间
		holder.tv_time.setText(familyDoctor.StartDate.substring(0,16).replace("T"," ") + " ~ " + familyDoctor.EndDate.substring(0,16).replace("T"," "));
		//用户信息
		holder.tv_name.setText(familyDoctor.UserCNName);
		//状态
		holder.tv_state.setText(MyUtils.getFamilyDoctorState(context,familyDoctor.Status,holder.tv_state));

		return convertView;
	}

	@Override
	public void convert(com.winson.ui.widget.ViewHolder viewHolder, FamilyDoctor obj, int position) {

	}

	private class ViewHolder {
		@ViewInject(R.id.tv_time)
		private TextView tv_time;

		@ViewInject(R.id.tv_state)
		private TextView tv_state;

		@ViewInject(R.id.tv_name)
		private TextView tv_name;

	}

}
