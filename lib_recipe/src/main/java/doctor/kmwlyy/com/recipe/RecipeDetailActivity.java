package doctor.kmwlyy.com.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.networkbench.agent.impl.NBSAppAgent;

import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.Adapter.CnRecipeListAdapter;
import doctor.kmwlyy.com.recipe.Adapter.DiagnosisAdapter;
import doctor.kmwlyy.com.recipe.Adapter.EnRecipeListAdapter;
import doctor.kmwlyy.com.recipe.Event.Http_addRecipeList_Event;
import doctor.kmwlyy.com.recipe.Event.Http_addRecipe_Event;
import doctor.kmwlyy.com.recipe.Event.NetService;
import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DiagnosisBean;
import doctor.kmwlyy.com.recipe.Model.DrugBean;
import doctor.kmwlyy.com.recipe.Model.RecipeBean;
import doctor.kmwlyy.com.recipe.Model.RecipeSaveBean;
import doctor.kmwlyy.com.recipe.View.DrugAutoCompleteTextView;
import doctor.kmwlyy.com.recipe.View.ICDAutoCompleteTextView;
import doctor.kmwlyy.com.recipe.View.MyListView;
import doctor.kmwlyy.com.recipe.View.MySpinner;

/**
 * 开处方编辑界面
 */
public class RecipeDetailActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "RecipeDetailActivity";
    public Context mContext;
    public String Type; // 中药、西药处方类型
    public String Action; //add 或者 modify 操作
    public String mRecipeName; //处方名称
    public RecipeBean.RecipeListBean recipeListBean; //处方详情类对象
    public List<DrugBean> cn_drug_list,en_drug_list,drugList; //药品列表
    public MyListView lv_CN,lv_EN,lv_diagnosis;//ListView控件
    public EditText et_name,et_jishu,et_jian,et_before,et_after,et_FreqDay,et_FreqTimes,et_Times,et_remark; //中药的一大推属性输入框
    public LinearLayout ll_daijian;
    public ImageView iv_daijian;//代煎
    public MySpinner sp_zhifa,sp_usage;//制法、用法
    public Boolean daijian_flag = false;
    public CnRecipeListAdapter cn_drugAdapter; //中药列表适配器
    public EnRecipeListAdapter en_drugAdapter;//西药列表适配器
    public DiagnosisAdapter diag_adapter;//诊断列表适配器
    public LinearLayout ll_cn,ll_en;
    public ArrayAdapter<String> zhifaAdapter,usageAdapter;
    public static List<DiagnosisBean> diag_list;
    public String OPDRegisterID = ""; //预约ID

    /**
     * 打开处方集详情界面
     * @param type 中药、西药处方
    public final static String CN_RECIPE = "中药处方";
    public final static String EN_RECIPE = "西药处方";
     * @param action add/modify
            public final static String RECIPE_ADD = "ADD";
            public final static String RECIPE_MODIFY = "MODIFY";

     */
    public static void startDetailActivity(Context mContext,String type, String action,String OPDRegisterID,RecipeBean.RecipeListBean bean){
        Intent intent = new Intent();
        intent.setClass(mContext, RecipeDetailActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("action", action);
        intent.putExtra("OPDRegisterID", OPDRegisterID);
        if(null != bean){
            Bundle bundle = new Bundle();
            bundle.putSerializable("bean", bean);
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipedetail);
        DrugAutoCompleteTextView.ENABLE = false; //关闭药品自动搜索功能
        ICDAutoCompleteTextView.ENABLE = false; //关闭诊断自动搜索功能
        Type = getIntent().getStringExtra("type");
        Action = getIntent().getStringExtra("action");
        OPDRegisterID = getIntent().getStringExtra("OPDRegisterID");
        //初使化
        init();

        recipeListBean = (RecipeBean.RecipeListBean)getIntent().getSerializableExtra("bean");
        if(null != recipeListBean){
            loadData();//编辑处方时 先加载数据
        }
    }

    public void init() {
        mContext = RecipeDetailActivity.this;
        lv_diagnosis = (MyListView) findViewById(R.id.lv_diagnosis);
        //诊断
        diag_list = new ArrayList<>();
        diag_adapter = new DiagnosisAdapter(mContext,diag_list, Type.equals(Constant.CN_RECIPE)?Constant.ICDType_CN:Constant.ICDType_EN);
        diag_adapter.add();
        lv_diagnosis.setAdapter(diag_adapter);
        //标题
        ((TextView) findViewById(R.id.tv_center)).setText(Action.equals(Constant.RECIPE_ADD)?getResources().getString(R.string.string_add_recipe):getResources().getString(R.string.string_modify_recipe));
        findViewById(R.id.tv_left).setOnClickListener(this);
        findViewById(R.id.tv_right).setOnClickListener(this);
        findViewById(R.id.tv_right).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.tv_right)).setText(getResources().getString(R.string.app_string_save));
        et_name = (EditText)findViewById(R.id.et_name);
        et_jishu = (EditText)findViewById(R.id.et_jishu_num);
        ll_daijian = (LinearLayout) findViewById(R.id.ll_daijian);
        iv_daijian = (ImageView) findViewById(R.id.iv_daijian);
        et_jian = (EditText)findViewById(R.id.et_jian);
        et_before = (EditText)findViewById(R.id.et_before);
        et_after = (EditText)findViewById(R.id.et_after);
        et_FreqDay = (EditText)findViewById(R.id.et_FreqDay);
        et_FreqTimes = (EditText)findViewById(R.id.et_FreqTimes);
        et_Times = (EditText)findViewById(R.id.et_Times);
        et_remark = (EditText)findViewById(R.id.et_remark);
        sp_usage = (MySpinner)findViewById(R.id.sp_usage);
        sp_zhifa = (MySpinner)findViewById(R.id.sp_zhifa);

        //药品信息
        if(Type.equals(Constant.CN_RECIPE)){
            lv_CN = (MyListView) findViewById(R.id.lv_cn_druglist);
            cn_drug_list = new ArrayList<>();
            cn_drugAdapter = new CnRecipeListAdapter(mContext,cn_drug_list, Constant.DrugType_CN);
            cn_drugAdapter.add();
            lv_CN.setAdapter(cn_drugAdapter);
            ll_cn = (LinearLayout) findViewById(R.id.ll_cn);
            ll_cn.setVisibility(View.VISIBLE);
            //代煎
            ll_daijian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(daijian_flag){
                        daijian_flag = false;
                        iv_daijian.setImageDrawable(getResources().getDrawable(R.mipmap.select_unable));
                    }else{
                        daijian_flag = true;
                        iv_daijian.setImageDrawable(getResources().getDrawable(R.mipmap.selected));
                    }
                }
            });
            //制法的下拉框
            zhifaAdapter = new ArrayAdapter<String>(mContext, R.layout.item_spinner, Constant.getZhiFa(mContext)){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View v = super.getView(position, convertView, parent);
                    return v;
                }
            };
            sp_zhifa.setAdapter(zhifaAdapter);
            sp_zhifa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
            //用法的下拉框
            usageAdapter = new ArrayAdapter<String>(mContext, R.layout.item_spinner, Constant.getUsage(mContext)){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View v = super.getView(position, convertView, parent);
                    return v;
                }
            };
            sp_usage.setAdapter(usageAdapter);
            sp_usage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });

        }else{
            lv_EN = (MyListView) findViewById(R.id.lv_en_druglist);
            en_drug_list = new ArrayList<>();
            en_drugAdapter = new EnRecipeListAdapter(mContext,en_drug_list, Constant.DrugType_EN);
            en_drugAdapter.add();
            lv_EN.setAdapter(en_drugAdapter);
            ll_en = (LinearLayout) findViewById(R.id.ll_en);
            ll_en.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 加载数据
     */
    public void loadData(){
        et_name.setText(recipeListBean.getRecipeName());
        //加载药品
        if(recipeListBean.getDetails() != null && recipeListBean.getDetails().size()>0 ){
            List<DrugBean> list = new ArrayList<>();
            for(int i=0;i<recipeListBean.getDetails().size();i++){
                RecipeBean.RecipeListBean.DetailsBean bean = recipeListBean.getDetails().get(i);
                DrugBean drugBean = new DrugBean();
                drugBean.Dose = ""+bean.getDose();
                drugBean.Quantity = ""+bean.getQuantity();
                drugBean.DrugRouteName = bean.getDrugRouteName();
                drugBean.Frequency = bean.getFrequency();
                drugBean.Drug.DrugName = bean.getDrug().getDrugName();
                drugBean.Drug.DrugID = bean.getDrug().getDrugID();
                drugBean.Drug.DrugCode = bean.getDrug().getDrugCode();
                drugBean.Drug.Specification = bean.getDrug().getSpecification();
                drugBean.Drug.DoseUnit = bean.getDrug().getDoseUnit();
                drugBean.Drug.Unit = bean.getDrug().getUnit();
                drugBean.Drug.UnitPrice = ""+bean.getDrug().getUnitPrice();
                list.add(drugBean);
            }

            if(Type.equals(Constant.CN_RECIPE)){
                cn_drugAdapter.setData(list);
            }else{
                en_drugAdapter.setData(list);
            }
        }
        //加载诊断
        if(recipeListBean.getDiagnoseList() != null && recipeListBean.getDiagnoseList().size()>0 ){
            List<DiagnosisBean> list = new ArrayList<>();
            for(int i=0;i<recipeListBean.getDiagnoseList().size();i++){
                DiagnosisBean bean = new DiagnosisBean();
                bean.Description = recipeListBean.getDiagnoseList().get(i).getDescription();
                bean.Detail.DiseaseName = recipeListBean.getDiagnoseList().get(i).getDetail().getDiseaseName();
                bean.Detail.DiseaseCode = recipeListBean.getDiagnoseList().get(i).getDetail().getDiseaseCode();
                list.add(bean);
            }
            diag_adapter.setData(list);
        }
        //加载 剂数，嘱托等数据
        //加载制法、用法
        if(Type.equals(Constant.CN_RECIPE)) {
            et_jishu.setText(recipeListBean.getTCMQuantity()==0?"":""+recipeListBean.getTCMQuantity());
            et_jian.setText(recipeListBean.getDecoctNum()==0?"":""+recipeListBean.getDecoctNum());
            et_after.setText(recipeListBean.getDecoctTargetWater()==0?"":""+recipeListBean.getDecoctTargetWater());
            et_before.setText(recipeListBean.getDecoctTotalWater()==0?"":""+recipeListBean.getDecoctTotalWater());
            et_FreqDay.setText(recipeListBean.getFreqDay()==0?"":""+recipeListBean.getFreqDay());
            et_FreqTimes.setText(recipeListBean.getFreqTimes()==0?"":""+recipeListBean.getFreqTimes());
            et_Times.setText(recipeListBean.getTimes()==0?"":""+recipeListBean.getTimes());
            et_remark.setText(recipeListBean.getRemark());
            if(recipeListBean.getTCMQuantity() == recipeListBean.getReplaceDose()){
                daijian_flag = true;
                iv_daijian.setImageDrawable(getResources().getDrawable(R.mipmap.selected));
            }else{
                daijian_flag = false;
                iv_daijian.setImageDrawable(getResources().getDrawable(R.mipmap.select_unable));
            }
            sp_zhifa.setSelection(recipeListBean.getBoilWay()==0?0:1);
            sp_usage.setSelection(Constant.getUsagePostion(mContext,recipeListBean.getUsage()));
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
            if(!checkDiag()){
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind2));
                return;
            }
            if(!checkParms()){
                return;
            }

        save(prepareData());

        }
    }

    /**
     * 拼装数据，发送请求
     */
    public RecipeSaveBean prepareData(){
        RecipeSaveBean bean = new RecipeSaveBean();
        //处方名称
        bean.RecipeName = (et_name.getText().toString());
        //处方类型（处方类型 1=中药处方,2=西药处方）
        bean.RecipeType = (Type.equals(Constant.CN_RECIPE)?1:2);
        //处方编号 及 预约编号
        bean.OPDRegisterID = OPDRegisterID;
        if(Action.equals(Constant.RECIPE_MODIFY)){
            bean.RecipeNo = (recipeListBean.getRecipeNo());
        }
        //诊断
        bean.DiagnoseList = (diag_adapter.getList());
        //药品信息
        if(Type.equals(Constant.CN_RECIPE)){
            bean.Details = (cn_drugAdapter.getSaveList());
        }else{
            bean.Details = (en_drugAdapter.getSaveList());
        }
        //用法，剂数，嘱托等数据
        if(Type.equals(Constant.CN_RECIPE)){
            bean.TCMQuantity = Integer.parseInt(et_jishu.getText().toString().isEmpty()?"0":et_jishu.getText().toString());
            bean.DecoctNum = Integer.parseInt(et_jian.getText().toString().isEmpty()?"0":et_jian.getText().toString());
            bean.ReplaceDose = daijian_flag?bean.TCMQuantity:0; //选择代煎，就传总剂数。  不选择就传 0
            bean.BoilWay = zhifaAdapter.getItem(sp_zhifa.getSelectedItemPosition()).equals(getResources().getString(R.string.string_zhifa1))?"0":"1";
            bean.Usage = usageAdapter.getItem(sp_usage.getSelectedItemPosition()).equals(getResources().getString(R.string.string_usage1))?"":usageAdapter.getItem(sp_usage.getSelectedItemPosition());
            bean.DecoctTotalWater= Integer.parseInt(et_before.getText().toString().isEmpty()?"0":et_before.getText().toString());
            bean.DecoctTargetWater = Integer.parseInt(et_after.getText().toString().isEmpty()?"0":et_after.getText().toString());
            bean.FreqDay = Integer.parseInt(et_FreqDay.getText().toString().isEmpty()?"0":et_FreqDay.getText().toString());
            bean.FreqTimes = Integer.parseInt(et_FreqTimes.getText().toString().isEmpty()?"0":et_FreqTimes.getText().toString());
            bean.Times = Integer.parseInt(et_Times.getText().toString().isEmpty()?"0":et_Times.getText().toString());
            bean.Remark = et_remark.getText().toString();
        }else{
            //西药暂时设为1，这些字段用不上，但是不能传0，会验证失败
            bean.FreqDay = 1;
            bean.TCMQuantity = 1;
            bean.DecoctNum = 1;
            bean.DecoctTargetWater = 1;
            bean.DecoctTotalWater = 1;
            bean.FreqTimes = 1;
            bean.Times = 1;
        }
        return bean;
    }

    /**
     * 保存
     */
    public void save(RecipeSaveBean bean){
        showLoadDialog(R.string.string_wait_save);
        NBSAppAgent.onEvent("诊室-保存处方");
        Http_addRecipe_Event event = new Http_addRecipe_Event(bean,new HttpListener<String>(
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
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_response_save));
                finish();


            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    /**
     * 检查处方名称
     */
    public boolean checkRecipeName(){
        //检查处方名称
        mRecipeName = et_name.getText().toString().trim();
        if(mRecipeName.isEmpty()){
            return false;
        }
        return true;
    }

    /**
     * 检查诊断参数
     * @return
     */
    public boolean checkDiag(){
        List<DiagnosisBean> list = diag_adapter.getList();
        if(list.size() == 0){
            return false;
        }else{
            for(int i=0;i<list.size();i++){
                if(list.get(i).Description.trim().length() == 0 || list.get(i).Detail.DiseaseName.trim().length() == 0){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 检查药品和其它参数
     * @return
     */
    public boolean checkParms(){
        if(Type.equals(Constant.CN_RECIPE)){
            //检查中药内容
            drugList = cn_drugAdapter.getList();
            if(drugList.size() == 0){
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind1));
                return false;
            }else{
                for(int i=0;i<drugList.size();i++){
                    DrugBean drug = drugList.get(i);
                    if(drug.Dose.trim().length() == 0 ||  drug.Drug.DrugCode.trim().length() == 0){
                        ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind1));
                        return false;
                    }
                }
            }
            //检查制法
            if(zhifaAdapter.getItem(sp_zhifa.getSelectedItemPosition()).equals(getResources().getString(R.string.string_zhifa1))){
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind3));
                return false;
            }
            //检查其它参数
            if(Integer.parseInt(et_FreqDay.getText().toString().isEmpty()?"0":et_FreqDay.getText().toString()) <= 0){
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind6));
                return false;
            }

            if(Integer.parseInt(et_FreqTimes.getText().toString().isEmpty()?"0":et_FreqTimes.getText().toString()) <= 0 || Integer.parseInt(et_FreqTimes.getText().toString().isEmpty()?"0":et_FreqTimes.getText().toString()) >=10){
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind7));
                return false;
            }

            if(Integer.parseInt(et_Times.getText().toString().isEmpty()?"0":et_Times.getText().toString()) <= 0 || Integer.parseInt(et_Times.getText().toString().isEmpty()?"0":et_Times.getText().toString()) >= 10){
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind8));
                return false;
            }

            if(Integer.parseInt(et_jishu.getText().toString().isEmpty()?"0":et_Times.getText().toString()) <= 0 ){
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind9));
                return false;
            }

            if(Integer.parseInt(et_jian.getText().toString().isEmpty()?"0":et_jian.getText().toString()) <= 0 || Integer.parseInt(et_jian.getText().toString().isEmpty()?"0":et_jian.getText().toString()) >= 10){
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind10));
                return false;
            }

            if(Integer.parseInt(et_before.getText().toString().isEmpty()?"0":et_before.getText().toString()) <= 0 || Integer.parseInt(et_before.getText().toString().isEmpty()?"0":et_before.getText().toString()) >= 9999){
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind11));
                return false;
            }

            if(Integer.parseInt(et_after.getText().toString().isEmpty()?"0":et_after.getText().toString()) <= 0 || Integer.parseInt(et_after.getText().toString().isEmpty()?"0":et_after.getText().toString()) >= 9999){
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind12));
                return false;
            }

            if(Integer.parseInt(et_after.getText().toString().isEmpty()?"0":et_after.getText().toString()) > Integer.parseInt(et_before.getText().toString().isEmpty()?"0":et_before.getText().toString())){
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind13));
                return false;
            }

        }else{
            //检查西药内容
            drugList = en_drugAdapter.getList();
            if(drugList.size() == 0){
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind1));
                return false;
            }else{
                for(int i=0;i<drugList.size();i++){
                    DrugBean drug = drugList.get(i);
                    if( drug.Dose.trim().length() == 0 || drug.Quantity.trim().length() == 0 || drug.Drug.DrugCode.trim().length() == 0){
                        ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind1));
                        return false;
                    }
                    if( Double.parseDouble(drug.Dose.trim()) <= 0 || Integer.parseInt(drug.Quantity.trim()) <= 0 ){
                        ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind1a));
                        return false;
                    }
                    if( drug.Frequency.equals("频率")){
                        ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind4));
                        return false;}
                    if( drug.DrugRouteName.equals("途径")){
                        ToastUtils.showShort(mContext,getResources().getString(R.string.string_recipe_name_remind5));
                        return false;}
                }
            }
        }
        return true;
    }


}
