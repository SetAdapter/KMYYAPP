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

import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DSRecipeDetailBean;
import doctor.kmwlyy.com.recipe.Model.DrugBean_Recipe;
import doctor.kmwlyy.com.recipe.Model.DrugDetail;
import doctor.kmwlyy.com.recipe.R;
import doctor.kmwlyy.com.recipe.View.DrugAutoCompleteTextView;
import doctor.kmwlyy.com.recipe.View.MyDeleteTextView;
import doctor.kmwlyy.com.recipe.View.MyEditView;
import doctor.kmwlyy.com.recipe.View.MySpinner;

/**
 * 西药处方的药品列表 适配器
 */
public class DS_EnDrugAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<DSRecipeDetailBean.RecipeListBean.DetailsBean> list;
	private String type;
	private List<String> list_unit;
	private ArrayAdapter arr_adapter;
	private int CurrentItem;


	public DS_EnDrugAdapter(Context context, List<DSRecipeDetailBean.RecipeListBean.DetailsBean> list, String type) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.type = type;
	}

	public void setSpinner(List<String> list_unit){
		this.list_unit = list_unit;
		arr_adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, list_unit);
		arr_adapter.setDropDownViewResource(R.layout.item_unit);
		notifyDataSetChanged();
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
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_ds_endrug, null);
			holder.tv_unit = (TextView) convertView.findViewById(R.id.tv_unit);
			holder.tv_unit_text = (TextView) convertView.findViewById(R.id.tv_unit_text);
			holder.sp_doseunit = (MySpinner) convertView.findViewById(R.id.sp_doseunit);
			holder.tv_endrug_subtotal = (TextView) convertView.findViewById(R.id.tv_endrug_subtotal);
			holder.et_doseunit = (MyEditView) convertView.findViewById(R.id.et_doseunit);
			holder.et_endrug_name = (TextView) convertView.findViewById(R.id.et_endrug_name);
			holder.sp_freq = (MySpinner) convertView.findViewById(R.id.sp_endrug_unit3);
			holder.sp_route = (MySpinner) convertView.findViewById(R.id.sp_endrug_unit4);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		/**
		 * 频率
		 */
		final ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, Constant.getFreq(context)){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				View v = super.getView(position, convertView, parent);
				if (position == getCount()) {
					((TextView)v.findViewById(android.R.id.text1)).setText("");
					((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
				}

				return v;
			}

			@Override
			public int getCount() {
				return super.getCount()-1; // you dont display last item. It is used as hint.
			}
		};
		holder.sp_freq.setAdapter(spAdapter);
		holder.sp_freq.setIndex(position);
		holder.sp_freq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {}
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				int pos = holder.sp_freq.getIndex();
				list.get(pos).Frequency = spAdapter.getItem(i).toString();
			}
		});
		holder.sp_freq.setSelection(Constant.getFreqPostion(context,list.get(CurrentItem).Frequency));

		/**
		 * 用药途径
		 */
		final ArrayAdapter<String> spAdapter1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, Constant.getRouteName(context)){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				View v = super.getView(position, convertView, parent);
				if (position == getCount()) {
					((TextView)v.findViewById(android.R.id.text1)).setText("");
					((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
				}

				return v;
			}
			@Override
			public int getCount() {
				return super.getCount()-1; // you dont display last item. It is used as hint.
			}
		};
		holder.sp_route.setAdapter(spAdapter1);
		holder.sp_route.setIndex(position);
		holder.sp_route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onNothingSelected(AdapterView<?> adapterView) {}
			@Override
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				int pos = holder.sp_route.getIndex();
				list.get(pos).DrugRouteName = spAdapter1.getItem(i).toString();
			}
		});
		holder.sp_route.setSelection(Constant.getRouteNamePostion(context,list.get(CurrentItem).DrugRouteName));

		/**
		 * 药品名称
		 */
		holder.et_endrug_name.setText(list.get(position).Drug.DrugName.length() == 0?"":list.get(position).Drug.DrugName);

		/**
		 * 计费数量
		 */
		holder.tv_unit.setText(list.get(CurrentItem).Quantity+"");

		/**
		 *  计费数量单位
		 */
		holder.tv_unit_text.setText(list.get(CurrentItem).Drug.Unit);

		/**
		 * 剂量
		 */
		holder.et_doseunit.setIndex(position);
		holder.et_doseunit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				int pos = holder.et_doseunit.getIndex();
				if(charSequence.toString().length() != 0){
					list.get(pos).Dose = Double.parseDouble(charSequence.toString());
				}
			}
			@Override
			public void afterTextChanged(Editable editable) {
			}
		});
		holder.et_doseunit.setText(list.get(CurrentItem).Dose+"");

		/**
		 * 单位
		 */
		final ArrayAdapter<String> unitAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, Constant.getEnUnit(context)){
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
		holder.sp_doseunit.setSelection(Constant.getEnunitPostion(context,list.get(CurrentItem).Drug.DoseUnit));
		return convertView;
	}

	private class ViewHolder {
		public TextView tv_unit;
		public TextView tv_unit_text;
		public MySpinner sp_doseunit;
		public TextView tv_endrug_subtotal;
		public TextView et_endrug_name;
		public MyEditView et_doseunit;
		public MySpinner sp_freq;
		public MySpinner sp_route;

	}

	public void add(){
		DSRecipeDetailBean.RecipeListBean.DetailsBean drugBean = new DSRecipeDetailBean.RecipeListBean.DetailsBean();
		list.add(drugBean);
		notifyDataSetChanged();
	}

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


}
