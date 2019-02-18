package com.kmwlyy.doctor.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.Activity.PatientDetailActivity;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.PatientListBean;
import com.kmwlyy.doctor.model.UnSignRecipeBean;
import com.kmwlyy.personinfo.PersonInfoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;

import java.util.List;

public class PatientListAdapter extends CommonAdapter<PatientListBean> {

	private Context context;
	private LayoutInflater inflater;
	private List<PatientListBean> list;

	public PatientListAdapter(Context context, List<PatientListBean> list) {
		super(context, R.layout.item_patient_list, list);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void setData(List<PatientListBean> list){
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<PatientListBean> list){
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void clear(){
		this.list.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public PatientListBean getItem(int position) {
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
			convertView = inflater.inflate(R.layout.item_patient_list, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final PatientListBean patientListBean = list.get(position);

		holder.tv_name.setText( patientListBean.getMemberName());
		holder.tv_sex.setText( MyUtils.getGendar(context,""+patientListBean.getGender()));
		holder.tv_age.setText( ""+patientListBean.getAge()+context.getResources().getString(R.string.string_age));
		holder.tv_phone.setText( patientListBean.getMobile());
		ImageLoader.getInstance().displayImage("", holder.iv_head, PersonInfoActivity.getCircleDisplayOptions(R.drawable.default_avatar_patient));
		holder.ll_patientlist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//进入患者详情界面
				Intent intent = new Intent();
				intent.setClass(context,PatientDetailActivity.class);
				intent.putExtra("id",patientListBean.getDoctorMemberID());
				context.startActivity(intent);
			}
		});

		if(position == 0){
			holder.v_line.setVisibility(View.VISIBLE);
		}else {
			holder.v_line.setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public void convert(com.winson.ui.widget.ViewHolder viewHolder, PatientListBean obj, int position) {

	}

	private class ViewHolder {
		@ViewInject(R.id.tv_patient_name)
		private TextView tv_name;

		@ViewInject(R.id.tv_patient_sex)
		private TextView tv_sex;

		@ViewInject(R.id.tv_patient_age)
		private TextView tv_age;

		@ViewInject(R.id.tv_patient_phone)
		private TextView tv_phone;

		@ViewInject(R.id.iv_patient_head)
		private ImageView iv_head;

		@ViewInject(R.id.ll_patientlist)
		private LinearLayout ll_patientlist;

		@ViewInject(R.id.v_line)
		private View v_line;
	}
}
