package doctor.kmwlyy.com.recipe.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;

import java.util.List;

import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DSRecipeDetailBean;
import doctor.kmwlyy.com.recipe.Model.DiagnosisBean;
import doctor.kmwlyy.com.recipe.R;
import doctor.kmwlyy.com.recipe.View.ICDAutoCompleteTextView;
import doctor.kmwlyy.com.recipe.View.MyDeleteTextView;

public class DS_DiagnosisAdapter extends BaseAdapter {
	public static final String TAG = "DiagnosisAdapter";

	private Context context;
	private LayoutInflater inflater;
	private List<DSRecipeDetailBean.RecipeListBean.DiagnosesBean> list;
	private String type;
	private int CurrentItem;

	public DS_DiagnosisAdapter(Context context, List<DSRecipeDetailBean.RecipeListBean.DiagnosesBean> list, String type) {
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
	public DSRecipeDetailBean.RecipeListBean.DiagnosesBean getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		CurrentItem = position;
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_ds_prescription, null);
			holder.tv_add = (TextView) convertView.findViewById(R.id.tv_add);
			holder.tv_delete = (MyDeleteTextView) convertView.findViewById(R.id.tv_delete);
			holder.et_diag_detail = (EditText) convertView.findViewById(R.id.et_diag_detail);
			holder.et_diag_name = (ICDAutoCompleteTextView) convertView.findViewById(R.id.et_diag_name);
			holder.et_diag_name.setValue(context,type);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}


		/**
		 * 添加 删除按键
		 */
		if( position == list.size() - 1){
			holder.tv_add.setVisibility(View.VISIBLE);
		}else{
			holder.tv_add.setVisibility(View.GONE);
		}

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

		/**
		 * 诊断名字
		 */
		holder.et_diag_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				list.get(holder.tv_delete.getIndex()).Detail.DiseaseCode = Constant.getDiagBean(i).DiseaseCode;
				list.get(holder.tv_delete.getIndex()).Detail.DiseaseName = Constant.getDiagBean(i).DiseaseName;
			}
		});
		holder.et_diag_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				list.get(holder.tv_delete.getIndex()).Detail.DiseaseName = charSequence.toString();
			}
			@Override
			public void afterTextChanged(Editable editable) {}
		});
		holder.et_diag_name.setText(list.get(CurrentItem).Detail.DiseaseName.length() == 0?"":list.get(position).Detail.DiseaseName+" ");

		/**
		 * 诊断详情
		 */
		holder.et_diag_detail.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				list.get(holder.tv_delete.getIndex()).Description = charSequence.toString();
			}
			@Override
			public void afterTextChanged(Editable editable) {}
		});
		holder.et_diag_detail.setText(list.get(CurrentItem).Description);

		return convertView;
	}

	private class ViewHolder {
		private EditText et_diag_detail;
		private ICDAutoCompleteTextView et_diag_name;
		private TextView tv_add;
		private MyDeleteTextView tv_delete;

	}

	public void add(){
		DSRecipeDetailBean.RecipeListBean.DiagnosesBean diagnosesBean = new DSRecipeDetailBean.RecipeListBean.DiagnosesBean();
		list.add(diagnosesBean);
		notifyDataSetChanged();
	}

	public void remove(int pos){
		if(list.size() > 1){
			list.remove(pos);
			notifyDataSetChanged();
		}else{
			ToastUtils.showShort(context,context.getResources().getString(R.string.string_parm_remind2));
		}

	}

	public List<DSRecipeDetailBean.RecipeListBean.DiagnosesBean> getList(){
		for(int i=0 ;i<list.size();i++){
			list.get(i).Detail.DiseaseName = list.get(i).Detail.DiseaseName.trim();
		}
		return  list;
	}


}
