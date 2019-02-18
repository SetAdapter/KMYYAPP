package doctor.kmwlyy.com.recipe.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;
import com.winson.ui.widget.AlterDialogView;

import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.DrugListActivity;
import doctor.kmwlyy.com.recipe.Fragment.RecipeFragment;
import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.RecipeBean;
import doctor.kmwlyy.com.recipe.R;
import doctor.kmwlyy.com.recipe.RecipeDetailActivity;
import doctor.kmwlyy.com.recipe.Utils.MyUtils;

/**
 * 中药处方的药品列表 适配器
 */
public class RecipeListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	private List<RecipeBean.RecipeListBean> list;
	private Fragment mFragment;

	public RecipeListAdapter(Context context , Fragment fragment,List<RecipeBean.RecipeListBean> list) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.list = list;
		this.mFragment = fragment;
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
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_total_recipe, null);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.btn_delete = (Button) convertView.findViewById(R.id.btn_delete);
			holder.btn_edit = (Button) convertView.findViewById(R.id.btn_edit);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final RecipeBean.RecipeListBean bean = list.get(position);
		holder.tv_title.setText("【" + MyUtils.getRecipeType(context,bean.getRecipeType()) +"】" + bean.getRecipeNo());
		holder.tv_name.setText(bean.getRecipeName());
		//删除
		holder.btn_delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
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
						((RecipeFragment)mFragment).deleteRecipe(bean.getRecipeNo());
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
		//编辑
		holder.btn_edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				RecipeDetailActivity.startDetailActivity(context, bean.getRecipeType()==1?Constant.CN_RECIPE:Constant.EN_RECIPE,Constant.RECIPE_MODIFY,bean.getOPDRegisterID(),bean);
			}
		});
		return convertView;
	}

	private class ViewHolder {
		public TextView tv_title;
		public TextView tv_name;
		public Button btn_delete;
		public Button btn_edit;
	}

	/**
	 * 加载数据
	 * @param recipelist
     */
	public void setData(List<RecipeBean.RecipeListBean> recipelist){
		if(null == list){
			list = new ArrayList<>();
		}
		list.clear();
		list.addAll(recipelist);
		notifyDataSetChanged();
	}
}
