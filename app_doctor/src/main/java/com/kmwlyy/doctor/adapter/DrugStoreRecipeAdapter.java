package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.DrugStoreRecipeBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.CommonAdapter;


import java.util.ArrayList;
import java.util.List;

public class DrugStoreRecipeAdapter extends CommonAdapter<DrugStoreRecipeBean> {

	private Context context;
	private LayoutInflater inflater;
	private List<DrugStoreRecipeBean> list;

	public DrugStoreRecipeAdapter(Context context, List<DrugStoreRecipeBean> list) {
		super(context, R.layout.item_store_recipe, list);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void setData(List<DrugStoreRecipeBean> list){
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<DrugStoreRecipeBean> list){
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public List<DrugStoreRecipeBean> getList(){
		if(list == null){
			list = new ArrayList<DrugStoreRecipeBean>();
		}
		return list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public DrugStoreRecipeBean getItem(int position) {
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
			convertView = inflater.inflate(R.layout.item_store_recipe, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		DrugStoreRecipeBean recipeBean = list.get(position);

		holder.tv_time.setText( recipeBean.ApplyDate);
		holder.tv_store_name.setText( recipeBean.Drugstore.DrugstoreName);
		holder.tv_patient.setText( recipeBean.UserMember.MemberName);
		holder.tv_money.setText( recipeBean.Amount + context.getResources().getString(R.string.string_price_yuan));

		if(recipeBean.ReceiveDate != null){
			holder.tv_deal_time.setText( recipeBean.ReceiveDate);
			holder.tv_deal_time.setVisibility(View.VISIBLE);
			holder.tv_time_title.setVisibility(View.VISIBLE);
		}else{
			holder.tv_deal_time.setVisibility(View.GONE);
			holder.tv_time_title.setVisibility(View.GONE);
		}


		String type = "";
		switch ( recipeBean.DrugstoreRecipeStatus){ //1 2 3 4    在医生端显示 1-未处理  2-已保存  3-已提交签名 4-已开处方
			case "1":
				//未处理
				type = context.getResources().getString(R.string.string_state_undo);
				holder.tv_type.setTextColor(context.getResources().getColor(R.color.app_color_string));
				break;
			case "2":
				//已保存
				type = context.getResources().getString(R.string.string_state_save);
				holder.tv_type.setTextColor(context.getResources().getColor(R.color.app_color_main));
				break;
			case "3":
				//已提交
				type = context.getResources().getString(R.string.string_state_send);
				holder.tv_type.setTextColor(context.getResources().getColor(R.color.app_color_main));
				break;
			case "4":
				//未完成
				type = context.getResources().getString(R.string.string_state_finish);
				holder.tv_type.setTextColor(context.getResources().getColor(R.color.color_tag_string));
				break;
		}
		holder.tv_type.setText( type );


		return convertView;
	}

	@Override
	public void convert(com.winson.ui.widget.ViewHolder viewHolder, DrugStoreRecipeBean obj, int position) {

	}

	private class ViewHolder {

		@ViewInject(R.id.tv_time)
		private TextView tv_time;

		@ViewInject(R.id.tv_type)
		private TextView tv_type;

		@ViewInject(R.id.tv_store_name)
		private TextView tv_store_name;

		@ViewInject(R.id.tv_patient)
		private TextView tv_patient;

		@ViewInject(R.id.tv_money)
		private TextView tv_money;

		@ViewInject(R.id.tv_deal_time)
		private TextView tv_deal_time;

		@ViewInject(R.id.tv_time_title)
		private TextView tv_time_title;
	}

}
