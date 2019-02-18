package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.doctor.Activity.PatientDetailActivity;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.MedicalRecordBean;
import com.kmwlyy.doctor.model.PatientListBean;
import com.kmwlyy.personinfo.PersonInfoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;

import java.util.List;

public class MedicalRecordAdapter extends CommonAdapter<MedicalRecordBean> {

	private Context context;
	private LayoutInflater inflater;
	private List<MedicalRecordBean> list;

	public MedicalRecordAdapter(Context context, List<MedicalRecordBean> list) {
		super(context, R.layout.item_record_list, list);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void setData(List<MedicalRecordBean> list){
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<MedicalRecordBean> list){
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public MedicalRecordBean getItem(int position) {
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
			convertView = inflater.inflate(R.layout.item_record_list, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final MedicalRecordBean recordBean = list.get(position);

		holder.tv_record_time.setText(recordBean.getOPDDate().substring(0,11).replace("T"," "));
		//0-挂号、1-图文咨询、2-语音咨询、3-视频咨询、4-家庭医生、5-远程会诊
		holder.tv_record_type.setText(MyUtils.getMedicalRecord(context,recordBean.getOPDType()));
		return convertView;
	}

	@Override
	public void convert(com.winson.ui.widget.ViewHolder viewHolder, MedicalRecordBean obj, int position) {

	}

	private class ViewHolder {
		@ViewInject(R.id.tv_record_type)
		private TextView tv_record_type;
		@ViewInject(R.id.tv_record_time)
		private TextView tv_record_time;
	}
}
