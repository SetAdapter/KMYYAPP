package doctor.kmwlyy.com.recipe;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.Adapter.CnRecipeListAdapter;
import doctor.kmwlyy.com.recipe.Adapter.EnRecipeListAdapter;
import doctor.kmwlyy.com.recipe.Event.Http_addRecipeList_Event;
import doctor.kmwlyy.com.recipe.Event.Http_modifyRecipeList_Event;
import doctor.kmwlyy.com.recipe.Event.NetService;
import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DrugBean;
import doctor.kmwlyy.com.recipe.Model.RecipeLAddBean;
import doctor.kmwlyy.com.recipe.Model.RecipeLModifyBean;
import doctor.kmwlyy.com.recipe.View.DrugAutoCompleteTextView;
import doctor.kmwlyy.com.recipe.View.MyListView;

/**
 * 处方集编辑界面
 */
public class RecipeListActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "RecipeListActivity";
    public Context mContext;
    public String Type; // 中药、西药处方类型
    public String Action; //add 或者 modify 操作
    public String mRecipeName; //处方名称
    public RecipeLModifyBean bean; //处方详情类对象
    public List<DrugBean> cn_drug_list,en_drug_list,drugList; //药品列表
    public MyListView lv_CN,lv_EN;//ListView控件
    public EditText et_name;
    public CnRecipeListAdapter cn_drugAdapter;
    public EnRecipeListAdapter en_drugAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipelist);
        DrugAutoCompleteTextView.ENABLE = false; //关闭药品自动搜索功能
        Type = getIntent().getStringExtra("type");
        Action = getIntent().getStringExtra("action");
        //初使化
        init();

        bean = (RecipeLModifyBean)getIntent().getSerializableExtra("bean");
        if(null != bean){
            loadData();//编辑处方时 先加载数据
        }
    }

    public void init() {
        mContext = RecipeListActivity.this;
        //标题
        ((TextView) findViewById(R.id.tv_center)).setText(Action.equals(Constant.RECIPE_ADD)?getResources().getString(R.string.string_add_recipe):getResources().getString(R.string.string_modify_recipe));
        findViewById(R.id.tv_left).setOnClickListener(this);
        findViewById(R.id.tv_right).setOnClickListener(this);
        findViewById(R.id.tv_right).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.tv_right)).setText(getResources().getString(R.string.app_string_save));

        et_name = (EditText)findViewById(R.id.et_name);

        if(Type.equals(Constant.CN_RECIPE)){
            lv_CN = (MyListView) findViewById(R.id.lv_cn_druglist);
            lv_CN.setVisibility(View.VISIBLE);
            cn_drug_list = new ArrayList<>();
            cn_drugAdapter = new CnRecipeListAdapter(mContext,cn_drug_list, Constant.DrugType_CN);
            cn_drugAdapter.add();
            lv_CN.setAdapter(cn_drugAdapter);
        }else{
            lv_EN = (MyListView) findViewById(R.id.lv_cn_druglist);
            lv_EN.setVisibility(View.VISIBLE);
            en_drug_list = new ArrayList<>();
            en_drugAdapter = new EnRecipeListAdapter(mContext,en_drug_list, Constant.DrugType_EN);
            en_drugAdapter.add();
            lv_EN.setAdapter(en_drugAdapter);
        }
    }

    /**
     * 加载数据
     */
    public void loadData(){
        et_name.setText(bean.RecipeFormulaName);
        if(bean.Details != null){
            List<DrugBean> list = new ArrayList<>();
            for(int i=0;i<bean.Details.size();i++){
                DrugBean drugBean = new DrugBean();
                drugBean.Dose = ""+bean.Details.get(i).Dose;
                drugBean.RecipeFormulaDetailID = ""+bean.Details.get(i).RecipeFormulaDetailID;
                drugBean.Drug.DoseUnit = bean.Details.get(i).DoseUnit;
                drugBean.DrugRouteName = bean.Details.get(i).DrugRouteName;
                drugBean.Frequency = bean.Details.get(i).Frequency;
                drugBean.Drug.DrugName = bean.Details.get(i).Drug.DrugName;
                drugBean.Drug.DrugID = bean.Details.get(i).Drug.DrugID;
                drugBean.Drug.DrugCode = bean.Details.get(i).Drug.DrugCode;
                drugBean.Drug.Specification = bean.Details.get(i).Drug.Specification;
                drugBean.Drug.UnitPrice = ""+bean.Details.get(i).Drug.UnitPrice;
                drugBean.Quantity = ""+bean.Details.get(i).Quantity;
                drugBean.Drug.Unit = ""+bean.Details.get(i).Drug.Unit;
                list.add(drugBean);
            }

            if(Type.equals(Constant.CN_RECIPE)){
                cn_drugAdapter.setData(list);
            }else{
                en_drugAdapter.setData(list);
            }
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_left){//返回
            finish();
        }
        else if(v.getId() == R.id.tv_right){//保存
            if(!checkRecipeName()){
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind));
                return;
            }
            if(!checkParms()){
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind1));
                return;
            }

            if(Action.equals("ADD")){
                add();
            }else{
                save();
            }
        }
    }

    /**
     * 新增
     */
    public void add(){
        showLoadDialog(R.string.string_wait_save);
        Http_addRecipeList_Event event = new Http_addRecipeList_Event(mRecipeName,Type,drugList,new HttpListener<String>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(String str) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                dismissLoadDialog();
                finish();


            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }
    /**
     * 保存
     */
    public void save(){
        showLoadDialog(R.string.string_wait_save);
        Http_modifyRecipeList_Event event = new Http_modifyRecipeList_Event(mRecipeName,drugList,bean,new HttpListener<String>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(String str) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                dismissLoadDialog();
                finish();


            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }
    /**
     * 检查必填项
     */
    public boolean checkRecipeName(){
        //检查处方名称
        mRecipeName = et_name.getText().toString().trim();
        if(mRecipeName.isEmpty()){
            return false;
        }
        return true;
    }
    public boolean checkParms(){
        if(Type.equals(Constant.CN_RECIPE)){
            //检查中药内容
            drugList = cn_drugAdapter.getList();
            if(drugList.size() == 0){
                return false;
            }else{
                for(int i=0;i<drugList.size();i++){
                    DrugBean drug = drugList.get(i);
                    if(drug.Dose.trim().length() == 0 ||  drug.Drug.DrugCode.trim().length() == 0){
                        return false;
                    }
                }
            }
        }else{
            //检查西药内容
            drugList = en_drugAdapter.getList();
            if(drugList.size() == 0){
                return false;
            }else{
                for(int i=0;i<drugList.size();i++){
                    DrugBean drug = drugList.get(i);
                    if( drug.Dose.trim().length() == 0 || drug.Quantity.trim().length() == 0 || drug.Drug.DrugCode.trim().length() == 0){
                        return false;
                    }
                    if( Double.parseDouble(drug.Dose) <= 0 || Integer.parseInt(drug.Quantity) <= 0){
                        return false;
                    }
                    if( drug.Frequency.equals("频率")){return false;}
                    if( drug.DrugRouteName.equals("途径")){return false;}
                }
            }
        }
        return true;
    }

}
