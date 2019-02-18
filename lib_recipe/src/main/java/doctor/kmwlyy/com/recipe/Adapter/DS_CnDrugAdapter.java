package doctor.kmwlyy.com.recipe.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.Fragment.CNDrugFragment;
import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DSRecipeDetailBean;
import doctor.kmwlyy.com.recipe.Model.DrugBean;
import doctor.kmwlyy.com.recipe.Model.DrugBean_Recipe;
import doctor.kmwlyy.com.recipe.Model.DrugDetail;
import doctor.kmwlyy.com.recipe.R;
import doctor.kmwlyy.com.recipe.View.DrugAutoCompleteTextView;
import doctor.kmwlyy.com.recipe.View.MyDeleteTextView;
import doctor.kmwlyy.com.recipe.View.MyEditView;
import doctor.kmwlyy.com.recipe.View.MySpinner;

/**
 * 中药处方的药品列表 适配器
 */
public class DS_CnDrugAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<DSRecipeDetailBean.RecipeListBean.DetailsBean> list;
	private String type;
	private DrugDetail drugDetail;
	private int CurrentItem;

	public DS_CnDrugAdapter(Context context, List<DSRecipeDetailBean.RecipeListBean.DetailsBean> list, String type) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.type = type;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
		CurrentItem = position;
		drugDetail = new DrugDetail();
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_ds_cndrug, null);
			holder.tv_Specification = (TextView) convertView.findViewById(R.id.tv_Specification);
			holder.sp_doseunit = (MySpinner) convertView.findViewById(R.id.sp_doseunit);
			holder.tv_cndrug_subtotal = (TextView) convertView.findViewById(R.id.tv_cndrug_subtotal);
			holder.et_dose = (MyEditView) convertView.findViewById(R.id.et_dose);
			holder.et_cndrug_name = (TextView) convertView.findViewById(R.id.et_cndrug_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		/**
		 * 药品名称
		 */
		holder.et_cndrug_name.setText(list.get(position).Drug.DrugName.length() == 0?"":list.get(position).Drug.DrugName+" ");

		/**
		 * 剂量
		 */
		holder.et_dose.setIndex(position);
		holder.et_dose.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if(charSequence.toString().length() != 0){
					list.get(holder.et_dose.getIndex()).Dose = Double.parseDouble(charSequence.toString());
				}
			}
			@Override
			public void afterTextChanged(Editable editable) {}
		});
		holder.et_dose.setText(list.get(CurrentItem).Dose + "");

		/**
		 * 规格
		 */
		holder.tv_Specification.setText(list.get(CurrentItem).Drug.Specification);

		/**
		 * 单位
		 */
//		holder.ap_doseunit.setText(list.get(CurrentItem).Drug.DoseUnit);
		final ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, Constant.getCnUnit(context)){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				View v = super.getView(position, convertView, parent);
				return v;
			}
		};
		holder.sp_doseunit.setAdapter(unitAdapter);
		holder.sp_doseunit.setIndex(position);
		holder.sp_doseunit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {}
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				int pos = holder.sp_doseunit.getIndex();
				list.get(pos).Drug.DoseUnit = unitAdapter.getItem(i).toString();
			}
		});
		holder.sp_doseunit.setSelection(Constant.getCnUnitPostion(context,list.get(CurrentItem).Drug.DoseUnit));
		return convertView;
	}

	private class ViewHolder {
		public MySpinner sp_doseunit;
		public TextView tv_cndrug_subtotal;
		public TextView tv_Specification;
		public MyEditView et_dose;
		public TextView et_cndrug_name;

	}

	/**
	 * 增加一个中药
	 */
	public void add(){
		DSRecipeDetailBean.RecipeListBean.DetailsBean detailsBean = new DSRecipeDetailBean.RecipeListBean.DetailsBean();
		list.add(detailsBean);
		notifyDataSetChanged();
	}

	/**
	 * 删除一个中药
	 * @param pos
     */
	public void remove(int pos){
		if(list.size() > 1){
			list.remove(pos);
			notifyDataSetChanged();
		}else{
			ToastUtils.showShort(context,context.getResources().getString(R.string.string_parm_remind1));
		}
	}



	public List<DSRecipeDetailBean.RecipeListBean.DetailsBean> getList(){
		return  list;
	}

	public List<DSRecipeDetailBean.RecipeListBean.DetailsBean> getSaveList(){
		List<DSRecipeDetailBean.RecipeListBean.DetailsBean> returnList = new ArrayList<>();
		for(int i = 0;i<list.size();i++){
			returnList.add(list.get(i));
		}
		return returnList;
	}
}
