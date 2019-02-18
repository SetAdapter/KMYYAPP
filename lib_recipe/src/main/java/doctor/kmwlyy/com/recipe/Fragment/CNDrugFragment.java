package doctor.kmwlyy.com.recipe.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import doctor.kmwlyy.com.recipe.Adapter.CnDrugAdapter;
import doctor.kmwlyy.com.recipe.Adapter.DiagnosisAdapter;
import doctor.kmwlyy.com.recipe.Adapter.EnDrugAdapter;
import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DiagnosisBean;
import doctor.kmwlyy.com.recipe.Model.DrugBean;
import doctor.kmwlyy.com.recipe.Model.DrugBean_Recipe;
import doctor.kmwlyy.com.recipe.R;
import doctor.kmwlyy.com.recipe.View.MyListView;

/**
 * Created by Administrator on 2016/8/20.
 */
public class CNDrugFragment extends Fragment implements View.OnClickListener {
    public MyListView lv_diagnosis,lv_cn_druglist;
    public static List<DiagnosisBean> diag_list;
    public List<DrugBean> cn_drug_list;
    public DiagnosisAdapter diag_adapter;
    public CnDrugAdapter cn_drugAdapter;
    public EditText et_usage;
    public EditText et_jishu;
    public TextView tv_total;

    public Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cn, null);
        mContext = getActivity();
        lv_diagnosis = (MyListView) view.findViewById(R.id.lv_diagnosis);
        lv_cn_druglist = (MyListView) view.findViewById(R.id.lv_cn_druglist);


        //诊断
        diag_list = new ArrayList<>();
        diag_adapter = new DiagnosisAdapter(mContext,diag_list, Constant.ICDType_CN);
        diag_adapter.add();
        lv_diagnosis.setAdapter(diag_adapter);

        //药品信息
        cn_drug_list = new ArrayList<>();
        cn_drugAdapter = new CnDrugAdapter(mContext,CNDrugFragment.this,cn_drug_list, Constant.DrugType_CN);
        cn_drugAdapter.add();
        lv_cn_druglist.setAdapter(cn_drugAdapter);

        //用法
        et_usage = (EditText) view.findViewById(R.id.et_usage);

        //剂数
        et_jishu = (EditText) view.findViewById(R.id.et_jishu);
        et_jishu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setTotalFee();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        //合计
        tv_total = (TextView) view.findViewById(R.id.tv_total);
        return view;
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 检查参数是否都填了
     * @return
     */
    public Boolean checkParms(){
        //检查诊断参数
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

        //检查药品参数
        List<DrugBean> drugList = cn_drugAdapter.getList();
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

        //检查用法
        if(et_usage.getText().toString().length() == 0){
            return false;
        }

        //检查剂数
        if(et_jishu.getText().toString().length() == 0){
            return false;
        }

        return true;
    }

    /**
     * 检查参数是否只填了一部分
     * true 有东西没填，报提示，并且不让保存
     * false 没问题，可以保存
     */
    public Boolean checkFullInput(){
        int inputNum = 0;//计算用，哪个地方填了就加1. 最后如果inputNum >0 表示没有填完整
        //诊断数据
        List<DiagnosisBean> list = diag_adapter.getList();
        if(list.size() == 0){

        }else{
            for(int i=0;i<list.size();i++){
                if(list.get(i).Description.trim().length() > 0 || list.get(i).Detail.DiseaseName.trim().length() > 0){
                    inputNum = inputNum + 1;
                }
            }
        }

        //药品数据
        List<DrugBean> drugList = cn_drugAdapter.getList();
        if(drugList.size() == 0){
            return false;
        }else{
            for(int i=0;i<drugList.size();i++){
                DrugBean drug = drugList.get(i);
                if(drug.Dose.trim().length() > 0 ||  drug.Drug.DrugCode.trim().length() > 0){
                    inputNum = inputNum + 1;
                }
            }
        }

        //用法数据
        if(et_usage.getText().toString().length() > 0){
            inputNum = inputNum + 1;
        }

        //剂数数据
        if(et_jishu.getText().toString().length() > 0){
            inputNum = inputNum + 1;
        }

        if(inputNum > 0){
            ToastUtils.showShort(mContext,mContext.getResources().getString(R.string.string_save_remind2));
            return true;
        }

        return false;
    }

    /**
     * 计算合计
     */
    public void setTotalFee(){
        Double total = 0.0;
        for (int i=0;i<cn_drug_list.size();i++){
            if(cn_drug_list.get(i).SubTotal.length() > 0){
                total = total + Double.parseDouble(cn_drug_list.get(i).SubTotal);
            }
        }
        if(et_jishu.getText().toString().trim().length() != 0){
            tv_total.setText(total * Double.parseDouble(et_jishu.getText().toString()) + "");
        }else{
            tv_total.setText("");
        }

    }

    /**
     * 返回诊断数据，进行保存
     * @return
     */
    public List<DiagnosisBean> getDiagList(){
        return diag_adapter.getList();
    }

    /**
     * 返回诊断数据，进行保存
     * @return
     */
    public List<DrugBean_Recipe> getDrugList(){
        return cn_drugAdapter.getSaveList();
    }

    /**
     * 返回用法数据，进行保存
     * @return
     */
    public String getUsage(){
        return et_usage.getText().toString().trim();
    }

    /**
     * 返回剂数，进行保存
     * @return
     */
    public String getJiShu(){
        return et_jishu.getText().toString().trim();
    }
}
