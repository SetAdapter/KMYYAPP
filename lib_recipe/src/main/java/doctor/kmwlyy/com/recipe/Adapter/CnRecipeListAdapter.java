package doctor.kmwlyy.com.recipe.Adapter;

import android.content.Context;
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
import doctor.kmwlyy.com.recipe.View.MySpinner;

/**
 * 中药处方的药品列表 适配器
 */
public class CnRecipeListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<DrugBean> list;
	private String type;
	private DrugDetail drugDetail;
	private int CurrentItem;
	private int index = -1;

	public CnRecipeListAdapter(Context context, List<DrugBean> list, String type) {
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
			convertView = inflater.inflate(R.layout.item_rl_cndrug, null);
			holder.tv_add = (TextView) convertView.findViewById(R.id.tv_add);
			holder.tv_delete = (MyDeleteTextView) convertView.findViewById(R.id.tv_delete);
			holder.tv_doseunit = (TextView) convertView.findViewById(R.id.tv_doseunit);
			holder.et_dose = (MyEditView) convertView.findViewById(R.id.et_dose);
			holder.et_cndrug_name = (DrugAutoCompleteTextView) convertView.findViewById(R.id.et_cndrug_name);
			holder.et_cndrug_name.setValue(context,type);
			holder.tv_subtotal = (TextView) convertView.findViewById(R.id.tv_subtotal);
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
				list.get(holder.tv_delete.getIndex()).Drug.DoseUnit = drugDetail.DoseUnit;
				holder.tv_doseunit.setText(drugDetail.DoseUnit);
				//小计
				list.get(holder.et_dose.getIndex()).SubTotal = Constant.getSubTotal(context,list.get(holder.tv_delete.getIndex()).Drug.UnitPrice,list.get(holder.tv_delete.getIndex()).Dose);
				holder.tv_subtotal.setText(list.get(holder.tv_delete.getIndex()).SubTotal);
			}
		});
		holder.et_cndrug_name.setText(list.get(position).Drug.DrugName.length() == 0?"":list.get(position).Drug.DrugName);
		holder.et_cndrug_name.setOnTouchListener(new View.OnTouchListener() {
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
				list.get(holder.et_dose.getIndex()).SubTotal = Constant.getSubTotal(context,list.get(holder.tv_delete.getIndex()).Drug.UnitPrice,list.get(holder.tv_delete.getIndex()).Dose);
				holder.tv_subtotal.setText(list.get(holder.tv_delete.getIndex()).SubTotal);
 			}
			@Override
			public void afterTextChanged(Editable editable) {}
		});
		holder.et_dose.setText(list.get(CurrentItem).Dose);

		/**
		 * 单位
		 */
		holder.tv_doseunit.setText(list.get(CurrentItem).Drug.DoseUnit);

		/**
		 * 小计
		 */
		holder.tv_subtotal.setText(list.get(CurrentItem).SubTotal);

		/**
		 * 内容换行，listview重绘后，EditText 失去焦点处理
		 */
		holder.et_cndrug_name.clearFocus();
		// 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
		if(index!= -1 && index == position) {
			holder.et_cndrug_name.requestFocus();
		}
		// 焦点移到最后
		holder.et_cndrug_name.setSelection(holder.et_cndrug_name.getText().length());

		return convertView;
	}

	private class ViewHolder {
		public TextView tv_add;
		public MyDeleteTextView tv_delete;
		public MyEditView et_dose;
		public DrugAutoCompleteTextView et_cndrug_name;
		public TextView tv_doseunit;
		public TextView tv_subtotal;
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
	 * 加载多种中药
	 */
	public void setData(List<DrugBean> data){
		list.clear();
		list.addAll(data);
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
