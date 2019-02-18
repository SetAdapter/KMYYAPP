package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.util.ImageUtils;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.FansListBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.ViewHolder;

import java.util.List;

public class FansListAdapter extends CommonAdapter<FansListBean> {

	private Context context;
	private LayoutInflater inflater;
	private List<FansListBean> list;

	public FansListAdapter(Context context, List<FansListBean> list) {
		super(context, R.layout.item_fans_list, list);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void setData(List<FansListBean> list){
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<FansListBean> list){
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public FansListBean getItem(int position) {
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
			convertView = inflater.inflate(R.layout.item_fans_list, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final FansListBean fansListBean = list.get(position);

		holder.tv_name.setText( fansListBean.getUserCNName());
		holder.tv_sex.setText( MyUtils.getGendar(context,""+fansListBean.getGender()));
		holder.tv_age.setText( ""+fansListBean.getAge()+context.getResources().getString(R.string.string_age));
		ImageLoader.getInstance().displayImage(fansListBean.getPhotoUrl(), holder.iv_head, ImageUtils.getCircleDisplayOptions());//头像
		return convertView;
	}

	@Override
	public void convert(com.winson.ui.widget.ViewHolder viewHolder, FansListBean obj, int position) {

	}

	private class ViewHolder {
		@ViewInject(R.id.tv_patient_name)
		private TextView tv_name;

		@ViewInject(R.id.tv_patient_sex)
		private TextView tv_sex;

		@ViewInject(R.id.tv_patient_age)
		private TextView tv_age;

		@ViewInject(R.id.iv_patient_head)
		private ImageView iv_head;
	}
}
