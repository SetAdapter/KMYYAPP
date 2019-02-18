package com.kmwlyy.doctor.adapter;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kmwlyy.doctor.Fragment.ClinicFragment;
import com.kmwlyy.doctor.Fragment.ElectRecipeFragment;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.MyUtils;
import com.kmwlyy.doctor.model.SignRecipeBean;
import com.kmwlyy.doctor.model.SignRecipeBean;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.networkbench.agent.impl.NBSAppAgent;
import com.winson.ui.widget.CommonAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.kmwlyy.doctor.R.id.tv_state;

public class UnSignRecipeAdapter extends CommonAdapter<SignRecipeBean> {

	private Context context;
	private Boolean mUserSearchMode;
	private LayoutInflater inflater;
	private List<SignRecipeBean> list;
	private String clientId;
	private SignListener mListener;
	public static final int TYPE_CUSTOM = 0; //自由选择模式
	public static final int TYPE_SELECT_ALL = 3; //全选模式
	public static final int TYPE_SELECT_NULL = 4;//全不选模式
	private int selectType = TYPE_CUSTOM;

	public UnSignRecipeAdapter(Context context, Boolean mode, String ClientId, List<SignRecipeBean> list, SignListener mListener) {
		super(context, R.layout.item_unsign_recipe, list);
		this.context = context;
		mUserSearchMode = mode;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.clientId = ClientId;
		this.mListener = mListener;
	}

	public interface SignListener {
		public void onSignClick();
	}

	public void setData(List<SignRecipeBean> list){
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<SignRecipeBean> list){
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public SignRecipeBean getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_unsign_recipe, null);
			ViewUtils.inject(holder, convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final SignRecipeBean recipeBean = list.get(position);
		holder.tv_name.setText( recipeBean.getMemberName());
		holder.tv_sex.setText( MyUtils.getGendar(context, recipeBean.getMemberGender()+"") );
		holder.tv_age.setText( recipeBean.getMemberAge()+context.getResources().getString(R.string.string_age));
		holder.tv_time.setText( recipeBean.getRecipeDate() );

		String tag = "";
		for(int i=0;i<recipeBean.getDiagnoses().size();i++){
			tag = tag + recipeBean.getDiagnoses().get(i);
			if(i != recipeBean.getDiagnoses().size() - 1){
				tag = tag + ",";
			}
		}
		holder.tv_tag.setText( tag );

		if(position == getCount()){
			holder.v_line.setVisibility(View.VISIBLE);
		}

		if(mUserSearchMode){
			holder.cb_sign.setVisibility(View.GONE);
			holder.tv_state.setVisibility(View.VISIBLE);
			if(recipeBean.getRecipeFileStatus() == 1){
				holder.tv_state.setText(context.getResources().getString(R.string.string_list_sign));
				holder.tv_state.setTextColor(context.getResources().getColor(R.color.color_main_green));
			}else {
				holder.tv_state.setText(context.getResources().getString(R.string.string_list_unsign));
				holder.tv_state.setTextColor(context.getResources().getColor(R.color.color_main_yellow));
			}
		}else if(list.get(position).getRecipeFileStatus() == 1){//已签1
			holder.cb_sign.setVisibility(View.GONE);
			holder.tv_state.setVisibility(View.GONE);
		}else if(list.get(position).getRecipeFileStatus() == 2){//待签2
			holder.tv_state.setVisibility(View.GONE);
			holder.cb_sign.setVisibility(View.VISIBLE);
			holder.cb_sign.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					NBSAppAgent.onEvent("诊室-电子处方-选择待签处方");
					list.get(position).isCheck = holder.cb_sign.isChecked();
					mListener.onSignClick();//通知主界面，打开签名窗口
				}
			});
			holder.cb_sign.setChecked(list.get(position).isCheck);
		}

		return convertView;
	}

	@Override
	public void convert(com.winson.ui.widget.ViewHolder viewHolder, SignRecipeBean obj, int position) {

	}

	private class ViewHolder {
		@ViewInject(R.id.tv_name)
		private TextView tv_name;

		@ViewInject(R.id.tv_sex)
		private TextView tv_sex;

		@ViewInject(R.id.tv_age)
		private TextView tv_age;

		@ViewInject(R.id.tv_time)
		private TextView tv_time;

		@ViewInject(R.id.tv_tag)
		private TextView tv_tag;

		@ViewInject(R.id.tv_state)
		private TextView tv_state;

		@ViewInject(R.id.v_line)
		private View v_line;

		@ViewInject(R.id.cb_sign)
		private CheckBox cb_sign;
	}

	public void setSelectType(int selectType) {
		this.selectType = selectType;
		for(int i=0;i<list.size();i++){
			list.get(i).isCheck = (selectType == TYPE_SELECT_ALL ? true : false );
		}
		notifyDataSetChanged();
	}

	public int getSelectType() {
		return selectType;
	}

	/**
	 * 获取选择的处方id 串
     * isSign true 返回签名数据   false 返回撤回数据
	 * @return
	 */
	public List<String> getSelectRecipe(boolean isSign){
		List<String> data = new ArrayList<>();
		for(int i=0;i<list.size();i++){
			SignRecipeBean bean = list.get(i);
			if(isSign && bean.isCheck && bean.getSignatureID() != null){
                data.add(bean.getSignatureID());
			}

            if(!isSign && bean.isCheck && bean.getRecipeFileID() != null){
                data.add(bean.getRecipeFileID());
            }
		}
		return data;
	}
}
