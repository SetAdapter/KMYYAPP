package doctor.kmwlyy.com.recipe.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.winson.ui.widget.CTRefreshListView;

import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.Adapter.RecipeListAdapter;
import doctor.kmwlyy.com.recipe.DrugListActivity;
import doctor.kmwlyy.com.recipe.DrugListSelActivity;
import doctor.kmwlyy.com.recipe.Event.Http_deleteRecipe_Event;
import doctor.kmwlyy.com.recipe.Event.NetService;
import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.RecipeBean;
import doctor.kmwlyy.com.recipe.R;
import doctor.kmwlyy.com.recipe.RecipeActivity;
import doctor.kmwlyy.com.recipe.RecipeDetailActivity;

/**
 * Created by Administrator on 2016/8/20.
 */
public class RecipeFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "RecipeFragment";
    private CTRefreshListView lv_recipe;
    private RecipeListAdapter adapter;
    private List<RecipeBean.RecipeListBean> list;
    public Context mContext;
    public static final String[] type = new String[]{Constant.COMMON_RECIPE,Constant.CN_RECIPE, Constant.EN_RECIPE};
    public String OPDRegisterID = ""; //预约ID

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe, null);
        mContext = getActivity();
        lv_recipe = (CTRefreshListView) view.findViewById(R.id.lv_recipe);
        view.findViewById(R.id.btn_add).setOnClickListener(this);
        list = new ArrayList<>();
        adapter = new RecipeListAdapter(mContext,this,list);
        lv_recipe.setAdapter(adapter);
        lv_recipe.setHeaderRefreshEnable(false);
        lv_recipe.setFooterRefreshEnable(false);

        Bundle bundle = getArguments();
        if(bundle!=null){
            OPDRegisterID = bundle.getString("id");
        }
        return view;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_add){
            newRecipeList();
        }
    }

    public void setData(List<RecipeBean.RecipeListBean> list){
        adapter.setData(list);
    }

    /**
     * 添加处方的多选对话框
     */
    public void newRecipeList() {
        new AlertDialog.Builder(mContext)
                .setItems(type, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0://添加常用处方
                                startListActivity();
                                break;
                            case 1://添加中药处方
                                RecipeDetailActivity.startDetailActivity(mContext,Constant.CN_RECIPE,Constant.RECIPE_ADD,OPDRegisterID,null);
                                break;
                            case 2://添加西药处方
                                RecipeDetailActivity.startDetailActivity(mContext,Constant.EN_RECIPE,Constant.RECIPE_ADD,OPDRegisterID,null);
                                break;
                        }
                    }
                }).show()
                .setCanceledOnTouchOutside(true);
    }

    /**
     * 打开处方列表，选择一条常用处方
     */
    public void startListActivity(){
        Intent intent = new Intent();
        intent.putExtra("type",Constant.RECIPE_SELECT);
        intent.putExtra("OPDRegisterID",OPDRegisterID);
        intent.setClass(mContext, DrugListSelActivity.class);
        startActivity(intent);
    }

    /**
     * 删除处方
     */
    public void deleteRecipe(String recipeNo) {
        Http_deleteRecipe_Event event = new Http_deleteRecipe_Event(recipeNo,new HttpListener<String>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(String str) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_delete_save));
                ((RecipeActivity)getActivity()).getRecipe(OPDRegisterID);
            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

}
