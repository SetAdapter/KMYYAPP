package doctor.kmwlyy.com.recipe.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.winson.ui.widget.AlterDialogView;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.InsideListView;
import com.winson.ui.widget.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.DrugListActivity;
import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DrugListBean;
import doctor.kmwlyy.com.recipe.Model.RecipeBean;
import doctor.kmwlyy.com.recipe.R;
import doctor.kmwlyy.com.recipe.RecipeDetailActivity;

public class DrugListAdapter extends CommonAdapter<DrugListBean> {

	private Context context;
	private LayoutInflater inflater;
	private List<DrugListBean> list;
	private Activity mActivity;
	private String mType;
	private String mOPDRegisterID;

	public DrugListAdapter(Context context, Activity activity, List<DrugListBean> list) {
		super(context, R.layout.item_druglist, list);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.mActivity = activity;
	}

	public DrugListAdapter(Context context, Activity activity, List<DrugListBean> list,String type,String OPDRegisterID) {
		super(context, R.layout.item_druglist, list);
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.mActivity = activity;
		this.mType = type;
		this.mOPDRegisterID = OPDRegisterID;
	}

	public void setData(List<DrugListBean> list){
		this.list.clear();
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<DrugListBean> list){
		this.list.addAll(list);
		notifyDataSetChanged();
	}

	public List<DrugListBean> getList(){
		if(list == null){
			list = new ArrayList<DrugListBean>();
		}
		return list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public DrugListBean getItem(int position) {
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
			convertView = inflater.inflate(R.layout.item_druglist, null);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_cnlist = (TextView) convertView.findViewById(R.id.tv_cnlist);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.btn_delete = (Button) convertView.findViewById(R.id.btn_delete);
			holder.btn_edit = (Button) convertView.findViewById(R.id.btn_edit);
			holder.lv_drug = (InsideListView) convertView.findViewById(R.id.lv_endrug);
			holder.ll_recipe = (LinearLayout) convertView.findViewById(R.id.ll_recipelayout);
			holder.ll_operation = (LinearLayout) convertView.findViewById(R.id.ll_operation);
			holder.ll_cn = (LinearLayout) convertView.findViewById(R.id.ll_cn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final DrugListBean drugListBean = list.get(position);

		holder.tv_title.setText((drugListBean.getRecipeType()==1? Constant.CN_RECIPE:Constant.EN_RECIPE)  + "【" + drugListBean.getRecipeFormulaName()+ "】");
		holder.tv_time.setText(context.getResources().getString(R.string.string_druglist_updatetime) + drugListBean.getModifyTime().substring(0,11).replace("T"," "));
		holder.btn_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {//删除处方集
				AlterDialogView.Builder builder = new AlterDialogView.Builder(context);
				builder.setMessage(context.getResources().getString(R.string.string_recipe_dialog));
				builder.setNegativeButton(context.getResources().getString(R.string.string_exit_no), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.setPositiveButton(context.getResources().getString(R.string.string_exit_yes), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						((DrugListActivity)mActivity).deleteRecipe(drugListBean.getRecipeFormulaFileID());
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
		holder.btn_edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {//编辑处方集
			((DrugListActivity)mActivity).editRecipe(drugListBean.getRecipeFormulaFileID(),drugListBean.getRecipeType());
			}
		});

		//开处方界面才能使用，插入一条处方集
		holder.ll_recipe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(null!=mType){
						RecipeDetailActivity.startDetailActivity(context,
								drugListBean.getRecipeType()==1?Constant.CN_RECIPE:Constant.EN_RECIPE,
								Constant.RECIPE_ADD,mOPDRegisterID,
								parseData(drugListBean,drugListBean.getRecipeFormulaName()));
						mActivity.finish();
				}

			}
		});

		if(drugListBean.getRecipeType() == 1){
			//中药不显示列表
			holder.lv_drug.setVisibility(View.GONE);
			holder.ll_cn.setVisibility(View.VISIBLE);
			holder.tv_cnlist.setText(getCNList(drugListBean.getDetails()));
		}else{
			holder.lv_drug.setVisibility(View.VISIBLE);
			holder.ll_cn.setVisibility(View.GONE);
			holder.lv_drug.setAdapter(new DrugAdapter(context,drugListBean.getRecipeType(),drugListBean.getDetails()));
		}

		//如果mType不为空的时候，表示是选择处方模式，不需要显示删除编辑功能
		if(null!=mType){
			holder.ll_operation.setVisibility(View.GONE);
		}

		return convertView;
	}

	@Override
	public void convert(com.winson.ui.widget.ViewHolder viewHolder, DrugListBean obj, int position) {

	}

	private class ViewHolder {
		private TextView tv_title;

		private TextView tv_cnlist;

		private InsideListView lv_drug;

		private TextView tv_time;

		private Button btn_delete;

		private Button btn_edit;

		private LinearLayout ll_recipe;

		private LinearLayout ll_operation;

		private LinearLayout ll_cn;
	}

	private String getCNList(List<DrugListBean.DetailsBean> list){
		String str = "";
		for(int i=0;i<list.size();i++){
			str = str + list.get(i).getDrug().getDrugName() + (list.get(i).getDrug().getSpecification().isEmpty()?"":("(" + list.get(i).getDrug().getSpecification() +  ")")) + " "+list.get(i).getDose() + list.get(i).getDrug().getDoseUnit();
			if( i != list.size()-1){
				str = str + "、";
			}
		}

		return str;
	}

	private class DrugAdapter extends BaseAdapter {
		private int mType;
		private Context mContext;
		private List<DrugListBean.DetailsBean> mList;
		private LayoutInflater mInflater;

		/**构造函数*/
		public DrugAdapter(Context context,int type,List<DrugListBean.DetailsBean> list) {
			mContext = context;
			mInflater = LayoutInflater.from(context);
			mList = list;
			mType = type;
		}


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
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
			DrugViewHolder holder;
			if (convertView == null) {
				holder = new DrugViewHolder();
				convertView = mInflater.inflate(R.layout.item_drugdesc, null);

				holder.tv_endrug = (TextView) convertView.findViewById(R.id.tv_endrug);
				holder.tv_ensp = (TextView) convertView.findViewById(R.id.tv_ensp);
				holder.tv_usage = (TextView) convertView.findViewById(R.id.tv_usage);
				holder.rl_endrug = (LinearLayout) convertView.findViewById(R.id.rl_endrug);
				holder.v_line = (View) convertView.findViewById(R.id.v_line);
				convertView.setTag(holder);
			} else {
				holder = (DrugViewHolder) convertView.getTag();
			}

			DrugListBean.DetailsBean bean = mList.get(position);

			if(mType == 1){//中药列表
				holder.rl_endrug.setVisibility(View.GONE);
			}else{
				holder.rl_endrug.setVisibility(View.VISIBLE);
				holder.tv_endrug.setText(bean.getDrug().getDrugName());
				holder.tv_ensp.setText(bean.getDrug().getSpecification());
				holder.tv_usage.setText(bean.getDrugRouteName() + "," + bean.getFrequency());
			}

			if(position == mList.size() - 1){
				holder.v_line.setVisibility(View.GONE);
			}else{
				holder.v_line.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

	}

	private class DrugViewHolder {

		private LinearLayout rl_endrug;

		private TextView tv_endrug;

		private TextView tv_ensp;

		private TextView tv_usage;

		private View v_line;
	}

	/**
	 * 转换数据类型.....
     */
	public RecipeBean.RecipeListBean parseData(DrugListBean drugListBean,String recipeName){
		RecipeBean.RecipeListBean bean = new RecipeBean.RecipeListBean();
		if(drugListBean.getDetails() != null){
			for (int i=0;i<drugListBean.getDetails().size();i++){
				RecipeBean.RecipeListBean.DetailsBean detailsBean = new RecipeBean.RecipeListBean.DetailsBean();
				detailsBean.setDose(drugListBean.getDetails().get(i).getDose());
				detailsBean.setQuantity(drugListBean.getDetails().get(i).getQuantity());
				detailsBean.setDrugRouteName(drugListBean.getDetails().get(i).getDrugRouteName());
				detailsBean.setFrequency(drugListBean.getDetails().get(i).getFrequency());
				detailsBean.getDrug().setDrugCode(drugListBean.getDetails().get(i).getDrug().getDrugCode());
				detailsBean.getDrug().setDrugName(drugListBean.getDetails().get(i).getDrug().getDrugName());
				detailsBean.getDrug().setDrugID(drugListBean.getDetails().get(i).getDrug().getDrugID());
				detailsBean.getDrug().setSpecification(drugListBean.getDetails().get(i).getDrug().getSpecification());
				detailsBean.getDrug().setDoseUnit(drugListBean.getDetails().get(i).getDrug().getDoseUnit());
				detailsBean.getDrug().setUnit(drugListBean.getDetails().get(i).getDrug().getUnit());
				detailsBean.getDrug().setUnitPrice(drugListBean.getDetails().get(i).getDrug().getUnitPrice());
				bean.getDetails().add(detailsBean);
			}
		}
		bean.setRecipeName(recipeName);
		return bean;
	}

}
