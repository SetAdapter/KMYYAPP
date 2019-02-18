package com.kmwlyy.doctor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.EvaluateListBean;
import com.kmwlyy.doctor.model.FansListBean;
import com.kmwlyy.personinfo.PersonInfoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.TagBaseAdapter;
import com.winson.ui.widget.TagCloudLayout;
import com.winson.ui.widget.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class EvaluateListAdapter extends CommonAdapter<EvaluateListBean> {

	private Context context;
	private LayoutInflater inflater;
	private List<EvaluateListBean> list;

	public EvaluateListAdapter(Context context, List<EvaluateListBean> list) {
		super(context, R.layout.item_evaluate_list, list);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void setData(List<EvaluateListBean> list){
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<EvaluateListBean> list){
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public EvaluateListBean getItem(int position) {
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
			convertView = inflater.inflate(R.layout.item_evaluate_list, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final EvaluateListBean evaluateListBean = list.get(position);

		holder.tv_name.setText( evaluateListBean.getUserName());
		holder.tv_type.setText(MyUtils.getEvaluateType(context,""+evaluateListBean.getServiceType()));
		holder.tv_time.setText(evaluateListBean.getCreateTime().substring(0,10));
		holder.tv_content.setText(evaluateListBean.getContent());
		holder.rb_score.setRating(evaluateListBean.getScore());

		//分数
		if(evaluateListBean.getEvaluationTags() != null){
			holder.tcl_container.setData(new TagBaseAdapter(context,java.util.Arrays.asList(evaluateListBean.getEvaluationTags().split(";")),TagBaseAdapter.GRAY));
		}

		ImageLoader.getInstance().displayImage(evaluateListBean.getUserPhotoUrl(), holder.iv_head, PersonInfoActivity.getCircleDisplayOptions(R.drawable.default_avatar_patient));
		return convertView;
	}

	@Override
	public void convert(com.winson.ui.widget.ViewHolder viewHolder, EvaluateListBean obj, int position) {

	}

	private class ViewHolder {
		@ViewInject(R.id.tv_patient_name)
		private TextView tv_name;

		@ViewInject(R.id.ratingbarId)
		private RatingBar rb_score;

		@ViewInject(R.id.tv_content)
		private TextView tv_content;

		@ViewInject(R.id.tv_type)
		private TextView tv_type;

		@ViewInject(R.id.tv_time)
		private TextView tv_time;

		@ViewInject(R.id.iv_patient_head)
		private ImageView iv_head;

		@ViewInject(R.id.tcl_container)
		private TagCloudLayout tcl_container;
	}
}
