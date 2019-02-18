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
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.Fragment.ENDrugFragment;
import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DrugBean;
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
public class EnDrugAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<DrugBean> list;
	private String type;
	private int CurrentItem;
	private ENDrugFragment mFragment;


	public EnDrugAdapter(Context context,ENDrugFragment fragment, List<DrugBean> list, String type) {
		this.context = context;
		mFragment = fragment;
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
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_endrug, null);
			holder.tv_add = (TextView) convertView.findViewById(R.id.tv_add);
			holder.tv_delete = (MyDeleteTextView) convertView.findViewById(R.id.tv_delete);
			holder.tv_unit = (TextView) convertView.findViewById(R.id.tv_unit);
			holder.tv_doseunit = (TextView) convertView.findViewById(R.id.tv_doseunit);
			holder.tv_endrug_subtotal = (TextView) convertView.findViewById(R.id.tv_endrug_subtotal);
			holder.tv_Specification = (TextView) convertView.findViewById(R.id.tv_Specification);
			holder.et_unit = (MyEditView) convertView.findViewById(R.id.et_unit);
			holder.et_doseunit = (MyEditView) convertView.findViewById(R.id.et_doseunit);
			holder.et_endrug_name = (DrugAutoCompleteTextView) convertView.findViewById(R.id.et_endrug_name);
			holder.et_endrug_name.setValue(context,type);
			holder.sp_freq = (MySpinner) convertView.findViewById(R.id.sp_endrug_unit3);
			holder.sp_route = (MySpinner) convertView.findViewById(R.id.sp_endrug_unit4);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		/**
		 * 增加删除
		 */
		holder.tv_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				add();
			}
		});

		holder.tv_delete.setIndex(position);
		holder.tv_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				remove(((MyDeleteTextView) view).getIndex());
			}
		});
		if( position == list.size() - 1){
			holder.tv_add.setVisibility(View.VISIBLE);
		}else{
			holder.tv_add.setVisibility(View.GONE);
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
		holder.et_endrug_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				DrugDetail drugDetail = Constant.getDrugBean(i);
				list.get(holder.tv_delete.getIndex()).Drug.DrugCode = drugDetail.DrugCode;
				list.get(holder.tv_delete.getIndex()).Drug.DrugName = drugDetail.DrugName;
				list.get(holder.tv_delete.getIndex()).Drug.DrugID = drugDetail.DrugID;
				list.get(holder.tv_delete.getIndex()).Drug.UnitPrice = drugDetail.UnitPrice;
				list.get(holder.tv_delete.getIndex()).Drug.Specification = drugDetail.Specification;
				holder.tv_unit.setText(drugDetail.Unit.trim());
				holder.tv_doseunit.setText(drugDetail.DoseUnit.trim());
				holder.tv_Specification.setText(drugDetail.Specification.trim());
				//小计
				list.get(holder.tv_delete.getIndex()).SubTotal = Constant.getFee(list.get(holder.tv_delete.getIndex()).Drug.UnitPrice,list.get(holder.tv_delete.getIndex()).Quantity);
				holder.tv_endrug_subtotal.setText(list.get(holder.tv_delete.getIndex()).SubTotal);
				//合计
				if(null !=mFragment){
					mFragment.setTotalFee();
				}
			}
		});
		holder.et_endrug_name.setText(list.get(position).Drug.DrugName.length() == 0?"":list.get(position).Drug.DrugName+" ");

		/**
		 * 规格
		 */
		holder.tv_Specification.setText(list.get(CurrentItem).Drug.Specification);


		/**
		 * 计费数量
		 */
		holder.et_unit.setIndex(position);
		holder.et_unit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void afterTextChanged(Editable editable) {
				int pos = holder.et_unit.getIndex();
				list.get(pos).Quantity = editable.toString();
				//小计
				list.get(pos).SubTotal = Constant.getFee(list.get(pos).Drug.UnitPrice,list.get(pos).Quantity);
				holder.tv_endrug_subtotal.setText(list.get(pos).SubTotal);
				//合计
				if(null !=mFragment){
					mFragment.setTotalFee();
				}
			}
		});
		holder.et_unit.setText(list.get(CurrentItem).Quantity);

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
				list.get(pos).Dose = charSequence.toString();
			}
			@Override
			public void afterTextChanged(Editable editable) {
			}
		});
		holder.et_doseunit.setText(list.get(CurrentItem).Dose);

		/**
		 * 小计
		 */
		list.get(CurrentItem).SubTotal = Constant.getFee(list.get(CurrentItem).Drug.UnitPrice,list.get(CurrentItem).Quantity);
		holder.tv_endrug_subtotal.setText(list.get(CurrentItem).SubTotal);

		return convertView;
	}

	private class ViewHolder {
		public TextView tv_add;
		public MyDeleteTextView tv_delete;
		public TextView tv_unit;
		public TextView tv_doseunit;
		public TextView tv_endrug_subtotal;
		public TextView tv_Specification;
		public DrugAutoCompleteTextView et_endrug_name;
		public MyEditView et_unit;
		public MyEditView et_doseunit;
		public MySpinner sp_freq;
		public MySpinner sp_route;

	}

	public void add(){
		DrugBean drugBean = new DrugBean();
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

	public List<DrugBean> getList(){
		return  list;
	}


	public List<DrugBean_Recipe> getSaveList(){
		List<DrugBean_Recipe> returnList = new ArrayList<>();
		for(int i = 0;i<list.size();i++){
			returnList.add(list.get(i).transform());
		}
		return returnList;
	}

}
