package doctor.kmwlyy.com.recipe;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.winson.ui.widget.ChildClickableLinearLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.Adapter.CnDrugAdapter;
import doctor.kmwlyy.com.recipe.Adapter.DS_CnDrugAdapter;
import doctor.kmwlyy.com.recipe.Adapter.DS_DiagnosisAdapter;
import doctor.kmwlyy.com.recipe.Adapter.DS_EnDrugAdapter;
import doctor.kmwlyy.com.recipe.Event.Http_Refuse_DSRecipe_Event;
import doctor.kmwlyy.com.recipe.Event.Http_Save_DSRecipe_Event;
import doctor.kmwlyy.com.recipe.Event.Http_SendAgain_DSRecipe_Event;
import doctor.kmwlyy.com.recipe.Event.Http_getDSRecipeDetail_Event;
import doctor.kmwlyy.com.recipe.Event.Http_getDoseUnit_Event;
import doctor.kmwlyy.com.recipe.Event.NetService;
import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DSRecipeDetailBean;
import doctor.kmwlyy.com.recipe.Model.DS_SaveBean;
import doctor.kmwlyy.com.recipe.Utils.MyUtils;
import doctor.kmwlyy.com.recipe.View.MyListView;

/**
 * 药店处方界面
 */
public class StoreRecipeActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "StoreRecipeActivity";
    public static final int SEND = 1;
    public static final int SAVE = 2;
    public static final String CN_UNIT = "1";
    public static final String EN_UNIT = "2";
    private DSRecipeDetailBean recipeDetailBean;
    private Context mContext = StoreRecipeActivity.this;
    private LinearLayout ll_cn_layout,ll_en_layout,ll_operation;
    private ChildClickableLinearLayout ll_base;
    private MyListView lv_cn_diagnosis,lv_en_diagnosis,lv_cn_druglist,lv_en_druglist;
    public static List<DSRecipeDetailBean.RecipeListBean.DiagnosesBean> cn_diag_list,en_diag_list;
    private DS_DiagnosisAdapter en_diag_adapter,cn_diag_adapter;
    private List<DSRecipeDetailBean.RecipeListBean.DetailsBean> en_drug_list,cn_drug_list;
    private DS_EnDrugAdapter en_drugAdapter;
    private DS_CnDrugAdapter cn_drugAdapter;
    private String recipeId;
    private Boolean Flag_CN = false,Flag_EN = false;
    private TextView tv_type,btn_save,en_total,cn_total,tv_jishu;
    private EditText et_usage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_recipe);

        init();
//        getUnit(CN_UNIT);
    }

    private void init(){
        ((TextView) findViewById(R.id.tv_center)).setText(getResources().getString(R.string.app_confirm_recipe));
        findViewById(R.id.tv_left).setOnClickListener(this);
        btn_save = (TextView) findViewById(R.id.tv_right);
        btn_save.setOnClickListener(this);
        btn_save.setVisibility(View.VISIBLE);
        ll_cn_layout = (LinearLayout)findViewById(R.id.ll_cn_layout);
        ll_en_layout = (LinearLayout)findViewById(R.id.ll_en_layout);
        ll_operation = (LinearLayout)findViewById(R.id.ll_operation);
        ll_base = (ChildClickableLinearLayout)findViewById(R.id.ll_base);
        en_total = (TextView) findViewById(R.id.en_total);
        cn_total = (TextView) findViewById(R.id.cn_total);
        tv_jishu = (TextView) findViewById(R.id.tv_jishu);
        et_usage = (EditText) findViewById(R.id.et_usage);
        lv_cn_diagnosis = (MyListView)findViewById(R.id.lv_cn_diagnosis);
        lv_en_diagnosis = (MyListView)findViewById(R.id.lv_en_diagnosis);
        lv_cn_druglist = (MyListView)findViewById(R.id.lv_cn_druglist);
        lv_en_druglist = (MyListView)findViewById(R.id.lv_en_druglist);
        findViewById(R.id.btn_no).setOnClickListener(this);
        findViewById(R.id.btn_yes).setOnClickListener(this);

        //西药诊断
        en_diag_list = new ArrayList<>();
        en_diag_adapter = new DS_DiagnosisAdapter(mContext,en_diag_list, Constant.ICDType_EN);
        en_diag_adapter.add();
        lv_en_diagnosis.setAdapter(en_diag_adapter);

        //中药诊断
        cn_diag_list = new ArrayList<>();
        cn_diag_adapter = new DS_DiagnosisAdapter(mContext,cn_diag_list, Constant.ICDType_CN);
        cn_diag_adapter.add();
        lv_cn_diagnosis.setAdapter(cn_diag_adapter);

        //西药药品信息
        en_drug_list = new ArrayList<>();
        en_drugAdapter = new DS_EnDrugAdapter(mContext,en_drug_list, Constant.DrugType_EN);
        en_drugAdapter.add();
        lv_en_druglist.setAdapter(en_drugAdapter);

        //中药药品信息
        cn_drug_list = new ArrayList<>();
        cn_drugAdapter = new DS_CnDrugAdapter(mContext,cn_drug_list, Constant.DrugType_CN);
        cn_drugAdapter.add();
        lv_cn_druglist.setAdapter(cn_drugAdapter);

        recipeId = getIntent().getStringExtra("detail");
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDetail(recipeId);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_left){//返回
            finish();
        }
        else if(v.getId() == R.id.tv_right){//保存
            if(btn_save.getText().equals("保存")){
                save(SAVE);
            }else{
                sendAgain(recipeId);
            }
        }else if(v.getId() == R.id.btn_yes){//提交
            save(SEND);
        }else if(v.getId() == R.id.btn_no){//驳回
            //待处理和已保存都可以驳回
            if(recipeDetailBean.getDrugstoreRecipeStatus() == 1 || recipeDetailBean.getDrugstoreRecipeStatus() == 2){
                refuse(recipeId);
            }else{
                ToastUtils.showShort(mContext,getResources().getString(R.string.string_cant_refuse));
            }

        }
    }

    /**
     * 保存 和 提交
     * 保存 = 2  提交 = 1
     * 调用同一个接口，只是参数里的  DrugstoreRecipeStatus 不同
     * 保存时 DrugstoreRecipeStatus = 2
     * 提交时 DrugstoreRecipeStatus = 3
     */
    public void save(final int type){
        if(checkParm()){
            showLoadDialog(R.string.string_wait_save);
            DS_SaveBean saveBean = prepareRecipe(type);
            Http_Save_DSRecipe_Event event = new Http_Save_DSRecipe_Event(saveBean,new HttpListener<String>(
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
                    ToastUtils.showShort(mContext,getResources().getString(type == SAVE?R.string.string_response_save:R.string.string_response_send));
                    finish();


                }
            });
            HttpClient httpClient = NetService.createClient(mContext, event);
            httpClient.start();
        }

    }

    /**
     * 驳回
     */
    public void refuse(String DrugstoreRecipeID){
        showLoadDialog(R.string.string_refuse_recipe);
        Http_Refuse_DSRecipe_Event event = new Http_Refuse_DSRecipe_Event(DrugstoreRecipeID,new HttpListener<String>(
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

                ToastUtils.showShort(mContext,getResources().getString(R.string.string_response_refuse));
                finish();

            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    /**
     * 重新提交
     */
    public void sendAgain(String DrugstoreRecipeID){
        showLoadDialog(R.string.string_send_recipe);
        Http_SendAgain_DSRecipe_Event event = new Http_SendAgain_DSRecipe_Event(DrugstoreRecipeID,new HttpListener<String>(
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

                ToastUtils.showShort(mContext,getResources().getString(R.string.string_response_send));
                finish();


            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    /**
     * 请求处方内容数据
     */
    private void loadDetail(String DrugstoreRecipeID) {
        showLoadDialog(R.string.string_wait);
        Http_getDSRecipeDetail_Event event = new Http_getDSRecipeDetail_Event(DrugstoreRecipeID,new HttpListener<DSRecipeDetailBean>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
                finish();
            }

            @Override
            public void onSuccess(DSRecipeDetailBean recipeDetail) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                dismissLoadDialog();
                recipeDetailBean = recipeDetail;
                updateUI(recipeDetailBean);
            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    /**
     * 请求药品单位
     * 药品类型（2=西药,1=中药）
     */
    private void getUnit(final String type) {
        if(type.equals(CN_UNIT)){
            showLoadDialog(R.string.string_wait_cndrug);
        }else{
            showLoadDialog(R.string.string_wait_endrug);
        }
        Http_getDoseUnit_Event event = new Http_getDoseUnit_Event(type,new HttpListener<List<String>>(
        ) {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
                finish();
            }

            @Override
            public void onSuccess(List<String> list) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                dismissLoadDialog();
                if(type.equals(CN_UNIT)){
//                    cn_drugAdapter.setSpinner(list);
                    getUnit(EN_UNIT);
                }else{
                    en_drugAdapter.setSpinner(list);
                    loadDetail(recipeId);
                }

            }
        });
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    public void updateUI(DSRecipeDetailBean recipe){
        ((TextView)findViewById(R.id.tv_store_name)).setText(getIntent().getStringExtra("store"));
        ((TextView)findViewById(R.id.tv_patient)).setText(recipe.getUserMember().getMemberName());
        ((TextView)findViewById(R.id.tv_sex)).setText(MyUtils.getGendar(mContext,recipe.getUserMember().getGender()+""));
        ((TextView)findViewById(R.id.tv_age)).setText(recipe.getUserMember().getAge()+"");
        ((TextView)findViewById(R.id.tv_money)).setText(recipe.getAmount()+"");
//        ((TextView)findViewById(R.id.tv_deal_time)).setText();

        tv_type = (TextView) findViewById(R.id.tv_type);
        String type = "";
        switch ( recipe.getDrugstoreRecipeStatus()){ //1 2 3 4    在医生端显示 1-未处理  2-已保存  3-已提交签名 4-已开处方
            case 1:
                //未处理
                type = mContext.getResources().getString(R.string.string_state_undo);
                tv_type.setTextColor(mContext.getResources().getColor(R.color.app_color_string));
                btn_save.setText(mContext.getResources().getString(R.string.app_string_save));
                break;
            case 2:
                //已保存
                type = mContext.getResources().getString(R.string.string_state_save);
                tv_type.setTextColor(mContext.getResources().getColor(R.color.app_color_main));
                btn_save.setText(mContext.getResources().getString(R.string.app_string_save));
                break;
            case 3:
                //已提交
                type = mContext.getResources().getString(R.string.string_state_send);
                tv_type.setTextColor(mContext.getResources().getColor(R.color.app_color_main));
                btn_save.setText(mContext.getResources().getString(R.string.app_string_again));
                ll_operation.setVisibility(View.GONE);
                ll_base.setChildClickable(false);
                break;
        }
        tv_type.setText( type );

        List<DSRecipeDetailBean.RecipeListBean> list = recipe.getRecipeList();
        if( list.size() > 0){
            for(int i=0;i<list.size();i++){

                DSRecipeDetailBean.RecipeListBean recipeListBean = list.get(i);
                if(recipeListBean.RecipeTypeName.equals("西药处方")){
                    ll_en_layout.setVisibility(View.VISIBLE);
                    Flag_EN = true;
                    if(recipeListBean.Diagnoses.size() > 0){
                        en_diag_list.clear();
                        en_diag_list.addAll(recipeListBean.Diagnoses);
                    }
                    en_diag_adapter.notifyDataSetChanged();

                    if (recipeListBean.Details.size() > 0){
                        en_drug_list.clear();
                        en_drug_list.addAll(recipeListBean.Details);
                    }
                    en_drugAdapter.notifyDataSetChanged();

                    //合计
                    Double total = 0.0;
                    for(int j=0;j<recipeListBean.Details.size();j++){
                        total = total + recipeListBean.Details.get(j).Drug.UnitPrice * recipeListBean.Details.get(j).Quantity;
                    }
                    en_total.setText(mContext.getResources().getString(R.string.string_total) + "￥" + (new DecimalFormat(".##")).format(total));
            }

                if(recipeListBean.RecipeTypeName.equals("中药处方")){
                    ll_cn_layout.setVisibility(View.VISIBLE);
                    Flag_CN = true;
                    if( list.get(i).Diagnoses.size() > 0){
                        cn_diag_list.clear();
                        cn_diag_list.addAll(recipeListBean.Diagnoses);
                    }
                    cn_diag_adapter.notifyDataSetChanged();

                    if ( list.get(i).Details.size() > 0){
                        cn_drug_list.clear();
                        cn_drug_list.addAll(recipeListBean.Details);
                    }
                    cn_drugAdapter.notifyDataSetChanged();

                    et_usage.setText(recipeListBean.Usage);
                    tv_jishu.setText(recipeListBean.TCMQuantity + "");


                    //计算合计
//                    Double total = 0.0;
//                    for(int j=0;j<recipeListBean.Details.size();j++){
//                        total = total + recipeListBean.Details.get(j).Drug.UnitPrice * recipeListBean.Details.get(j).Dose;
//                    }
//                    cn_total.setText(mContext.getResources().getString(R.string.string_total) + "￥" + (new DecimalFormat(".##")).format(total * recipeListBean.TCMQuantity));
                    cn_total.setText(mContext.getResources().getString(R.string.string_total) + "￥" + recipe.getAmount());
                }
            }
        }
    }

    public DS_SaveBean prepareRecipe(int type){
        DS_SaveBean bean = new DS_SaveBean();
        bean.DoctorID = recipeDetailBean.getDoctorID();
        bean.MemberID = recipeDetailBean.getMemberID();
        bean.DrugSellDate = recipeDetailBean.getDrugSellDate();
        bean.DrugstoreRecipeID = recipeDetailBean.getDrugstoreRecipeID();
        bean.DrugstoreRecipeStatus = type == SAVE?2:3;
        bean.RecipeFileDTOs = recipeDetailBean.getRecipeList();

        for(int i=0;i<bean.RecipeFileDTOs.size();i++){
            if(Flag_EN && bean.RecipeFileDTOs.get(i).RecipeTypeName.equals("西药处方")){
                bean.RecipeFileDTOs.get(i).Diagnoses = en_diag_adapter.getList();
            }

            if(Flag_CN && bean.RecipeFileDTOs.get(i).RecipeTypeName.equals("中药处方")){
                bean.RecipeFileDTOs.get(i).Diagnoses = cn_diag_adapter.getList();
                bean.RecipeFileDTOs.get(i).Usage = et_usage.getText().toString().trim();
            }
        }
        return bean;
    }

    public boolean checkParm(){
        Boolean flag = true;
        List<DSRecipeDetailBean.RecipeListBean> list = recipeDetailBean.getRecipeList();
        for(int i=0;i<list.size();i++){
            if(Flag_EN && list.get(i).RecipeTypeName.equals("西药处方")){
                for(int j=0;j<en_diag_adapter.getList().size();j++){
                    DSRecipeDetailBean.RecipeListBean.DiagnosesBean diagnosesBean = en_diag_adapter.getList().get(j);
                    if(diagnosesBean.Detail.DiseaseName.trim().length() == 0){
                        ToastUtils.showShort(mContext,getResources().getString(R.string.string_remind1));
                        flag = false;
                        break;
                    }
                }

                for(int j=0;j<en_drugAdapter.getList().size();j++){
                    DSRecipeDetailBean.RecipeListBean.DetailsBean details = en_drugAdapter.getList().get(j);
                    if(details.Dose <= 0){
                        ToastUtils.showShort(mContext,getResources().getString(R.string.string_remind3));
                        flag = false;
                        break;
                    }

                    if(details.Frequency.equals("频率") || details.DrugRouteName.equals("用药途径")){
                        ToastUtils.showShort(mContext,getResources().getString(R.string.string_remind4));
                        flag = false;
                        break;
                    }
                }
            }

            if(Flag_CN && list.get(i).RecipeTypeName.equals("中药处方")){
                for(int j=0;j<cn_diag_adapter.getList().size();j++){
                    DSRecipeDetailBean.RecipeListBean.DiagnosesBean diagnosesBean = cn_diag_adapter.getList().get(j);
                    if(diagnosesBean.Detail.DiseaseName.trim().length() == 0){
                        ToastUtils.showShort(mContext,getResources().getString(R.string.string_remind2));
                        flag = false;
                        break;
                    }
                }

//                for(int j=0;j<cn_drugAdapter.getList().size();j++){
//                    DSRecipeDetailBean.RecipeListBean.DetailsBean details = cn_drugAdapter.getList().get(j);
//                    if(details.Dose <= 0){
//                        ToastUtils.showShort(mContext,getResources().getString(R.string.string_remind5));
//                        flag = false;
//                        break;
//                    }
//                }

                if(et_usage.getText().toString().trim().length() == 0){
                    ToastUtils.showShort(mContext,getResources().getString(R.string.string_remind6));
                    flag = false;
                    break;
                }
            }
        }

        return flag;
    }
}