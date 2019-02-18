package doctor.kmwlyy.com.recipe.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
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
public class EnRecipeListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<DrugBean> list;
	private String type;
	private int CurrentItem;
	private int index = -1;


	public EnRecipeListAdapter(Context context,List<DrugBean> list, String type) {
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
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_rl_endrug, null);
			holder.tv_add = (TextView) convertView.findViewById(R.id.tv_add);
			holder.tv_delete = (MyDeleteTextView) convertView.findViewById(R.id.tv_delete);
			holder.et_dose = (MyEditView) convertView.findViewById(R.id.et_dose);
			holder.et_quantity = (MyEditView) convertView.findViewById(R.id.et_quantity);
			holder.tv_doseunit = (TextView) convertView.findViewById(R.id.tv_doseunit);
			holder.tv_quantity_unit = (TextView) convertView.findViewById(R.id.tv_quantity_unit);
			holder.tv_subtotal = (TextView) convertView.findViewById(R.id.tv_subtotal);
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
		final ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(context, R.layout.item_spinner, Constant.getFreq(context)){
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
		final ArrayAdapter<String> spAdapter1 = new ArrayAdapter<String>(context, R.layout.item_spinner, Constant.getRouteName(context)){
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
				list.get(holder.tv_delete.getIndex()).Drug.DoseUnit = drugDetail.DoseUnit;
				list.get(holder.tv_delete.getIndex()).Drug.Unit = drugDetail.Unit;
				holder.tv_doseunit.setText(drugDetail.DoseUnit);
				holder.tv_quantity_unit.setText(drugDetail.Unit);
				holder.et_endrug_name.setText((list.get(holder.tv_delete.getIndex()).Drug.DrugName.length() == 0?"":list.get(holder.tv_delete.getIndex()).Drug.DrugName) + (list.get(holder.tv_delete.getIndex()).Drug.Specification.length() == 0?"":"(" + list.get(holder.tv_delete.getIndex()).Drug.Specification + ")"));

				//小计
				list.get(holder.tv_delete.getIndex()).SubTotal = Constant.getSubTotal(context,list.get(holder.tv_delete.getIndex()).Drug.UnitPrice,list.get(holder.tv_delete.getIndex()).Quantity);
				holder.tv_subtotal.setText(list.get(holder.tv_delete.getIndex()).SubTotal);
			}
		});
		holder.et_endrug_name.setText((list.get(position).Drug.DrugName.length() == 0?"":list.get(position).Drug.DrugName) + (list.get(position).Drug.Specification.length() == 0?"":"(" + list.get(position).Drug.Specification + ")"));
		holder.et_endrug_name.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					// 在TOUCH的UP事件中，要保存当前的行下标，因为弹出软键盘后，整个画面会被重画
					// 在getView方法的最后，要根据index和当前的行下标手动为EditText设置焦点
					index = CurrentItem;
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					//打开搜索功能
					DrugAutoCompleteTextView.ENABLE = true;
				}
				return false;
			}
		});

		/**
		 * 数量，一共开了几盒药 Quantity
		 */
		holder.et_quantity.setIndex(position);
		holder.et_quantity.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				int pos = holder.et_quantity.getIndex();
				list.get(pos).Quantity = charSequence.toString();
				//小计
				list.get(holder.tv_delete.getIndex()).SubTotal = Constant.getSubTotal(context,list.get(holder.tv_delete.getIndex()).Drug.UnitPrice,list.get(holder.tv_delete.getIndex()).Quantity);
				holder.tv_subtotal.setText(list.get(holder.tv_delete.getIndex()).SubTotal);
			}
			@Override
			public void afterTextChanged(Editable editable) {
			}
		});
		holder.et_quantity.setText(list.get(CurrentItem).Quantity.equals("0")?"":list.get(CurrentItem).Quantity);

		/**
		 * 数量的单位 Unit
		 */
		holder.tv_quantity_unit.setText(list.get(CurrentItem).Drug.Unit);

		/**
		 * 用量，每次吃几片 Dose
		 */
		holder.et_dose.setIndex(position);
		holder.et_dose.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				int pos = holder.et_dose.getIndex();
				list.get(pos).Dose = charSequence.toString();
			}
			@Override
			public void afterTextChanged(Editable editable) {
			}
		});
		holder.et_dose.setText(list.get(CurrentItem).Dose.equals("0")?"":list.get(CurrentItem).Dose);

		/**
		 * 用量的单位 DoseUnit
		 */
		holder.tv_doseunit.setText(list.get(CurrentItem).Drug.DoseUnit);

		/**
		 * 小计
		 */
		holder.tv_subtotal.setText(list.get(CurrentItem).SubTotal);

		/**
		 * 内容换行，listview重绘后，EditText 失去焦点处理
		 */
		holder.et_endrug_name.clearFocus();
		// 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
		if(index!= -1 && index == position) {
			holder.et_endrug_name.requestFocus();
		}
		// 焦点移到最后
		holder.et_endrug_name.setSelection(holder.et_endrug_name.getText().length());
		return convertView;
	}

	private class ViewHolder {
		public TextView tv_add;
		public MyDeleteTextView tv_delete;
		public TextView tv_doseunit;
		public TextView tv_quantity_unit;
		public TextView tv_subtotal;
		public DrugAutoCompleteTextView et_endrug_name;
		public MyEditView et_dose;
		public MyEditView et_quantity;
		public MySpinner sp_freq;
		public MySpinner sp_route;

	}

	public void add(){
		DrugBean drugBean = new DrugBean();
		list.add(drugBean);
		notifyDataSetChanged();
	}

	/**
	 * 加载多种西药
	 */
	public void setData(List<DrugBean> data){
		list.clear();
		list.addAll(data);
		notifyDataSetChanged();
	}

	public void remove(int pos){
		if(list.size() > 1){
			list.remove(pos);
			notifyDataSetChanged();
		}else{
			ToastUtils.showShort(context,context.getResources().getString(R.string.string_parm_remind3));
		}

	}

	public List<DrugBean> getList(){
		if(null == list){
			return new ArrayList<DrugBean>();
		}
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
