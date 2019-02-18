package doctor.kmwlyy.com.recipe.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.Fragment.CNDrugFragment;
import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DrugBean;
import doctor.kmwlyy.com.recipe.Model.DrugBean_Recipe;
import doctor.kmwlyy.com.recipe.Model.DrugDetail;
import doctor.kmwlyy.com.recipe.R;
import doctor.kmwlyy.com.recipe.View.DrugAutoCompleteTextView;
import doctor.kmwlyy.com.recipe.View.MyDeleteTextView;
import doctor.kmwlyy.com.recipe.View.MyEditView;

/**
 * 中药处方的药品列表 适配器
 */
public class CnDrugAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<DrugBean> list;
	private String type;
	private DrugDetail drugDetail;
	private int CurrentItem;
	private CNDrugFragment mFragment;

	public CnDrugAdapter(Context context, CNDrugFragment fragment, List<DrugBean> list, String type) {
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
		drugDetail = new DrugDetail();
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_cndrug, null);
			holder.tv_add = (TextView) convertView.findViewById(R.id.tv_add);
			holder.tv_delete = (MyDeleteTextView) convertView.findViewById(R.id.tv_delete);
			holder.tv_Specification = (TextView) convertView.findViewById(R.id.tv_Specification);
			holder.tv_doseunit = (TextView) convertView.findViewById(R.id.tv_doseunit);
			holder.tv_cndrug_subtotal = (TextView) convertView.findViewById(R.id.tv_cndrug_subtotal);
			holder.et_dose = (MyEditView) convertView.findViewById(R.id.et_dose);
			holder.et_cndrug_name = (DrugAutoCompleteTextView) convertView.findViewById(R.id.et_cndrug_name);
			holder.et_cndrug_name.setValue(context,type);
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
		 * 药品名称
		 */
		holder.et_cndrug_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				drugDetail = Constant.getDrugBean(i);
				list.get(holder.tv_delete.getIndex()).Drug.DrugCode = drugDetail.DrugCode;
				list.get(holder.tv_delete.getIndex()).Drug.DrugName = drugDetail.DrugName;
				list.get(holder.tv_delete.getIndex()).Drug.DrugID = drugDetail.DrugID;
				list.get(holder.tv_delete.getIndex()).Drug.UnitPrice = drugDetail.UnitPrice;
				list.get(holder.tv_delete.getIndex()).Drug.Specification = drugDetail.Specification;
				holder.tv_Specification.setText(drugDetail.Specification.trim());
				holder.tv_doseunit.setText(drugDetail.DoseUnit.trim());

				//小计
				list.get(holder.tv_delete.getIndex()).SubTotal = Constant.getFee(list.get(holder.tv_delete.getIndex()).Drug.UnitPrice,list.get(holder.tv_delete.getIndex()).Dose);
				holder.tv_cndrug_subtotal.setText(list.get(holder.tv_delete.getIndex()).SubTotal);
				//合计
				if(null !=mFragment){
					mFragment.setTotalFee();
				}

			}
		});
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
				list.get(holder.et_dose.getIndex()).Dose = charSequence.toString();
				//小计
				list.get(holder.et_dose.getIndex()).SubTotal = Constant.getFee(list.get(holder.et_dose.getIndex()).Drug.UnitPrice,list.get(holder.et_dose.getIndex()).Dose);
				holder.tv_cndrug_subtotal.setText(list.get(holder.et_dose.getIndex()).SubTotal);
				//合计
				if(null !=mFragment){
					mFragment.setTotalFee();
				}
 }
			@Override
			public void afterTextChanged(Editable editable) {}
		});
		holder.et_dose.setText(list.get(CurrentItem).Dose);

		/**
		 * 小计
		 */
		list.get(CurrentItem).SubTotal = Constant.getFee(list.get(CurrentItem).Drug.UnitPrice,list.get(CurrentItem).Dose);
		holder.tv_cndrug_subtotal.setText(list.get(CurrentItem).SubTotal);

		/**
		 * 规格
		 */
		holder.tv_Specification.setText(list.get(CurrentItem).Drug.Specification);

		return convertView;
	}

	private class ViewHolder {
		public TextView tv_add;
		public MyDeleteTextView tv_delete;
		public TextView tv_doseunit;
		public TextView tv_cndrug_subtotal;
		public TextView tv_Specification;
		public MyEditView et_dose;
		public DrugAutoCompleteTextView et_cndrug_name;

	}

	/**
	 * 增加一个中药
	 */
	public void add(){
		DrugBean drugBean = new DrugBean();
		list.add(drugBean);
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
