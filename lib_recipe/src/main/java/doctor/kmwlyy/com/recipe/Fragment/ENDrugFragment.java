package doctor.kmwlyy.com.recipe.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kmwlyy.core.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

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
public class ENDrugFragment extends Fragment implements View.OnClickListener {
    public MyListView lv_diagnosis,lv_en_druglist;
    public static List<DiagnosisBean> diag_list;
    public List<DrugBean> en_drug_list;
    public DiagnosisAdapter diag_adapter;
    public EnDrugAdapter en_drugAdapter;
    public TextView tv_total;

    public Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_en, null);
        mContext = getActivity();
        lv_diagnosis = (MyListView) view.findViewById(R.id.lv_diagnosis);
        lv_en_druglist = (MyListView) view.findViewById(R.id.lv_en_druglist);


        //诊断
        diag_list = new ArrayList<>();
        diag_adapter = new DiagnosisAdapter(mContext,diag_list, Constant.ICDType_EN);
        diag_adapter.add();
        lv_diagnosis.setAdapter(diag_adapter);

        //药品信息
        en_drug_list = new ArrayList<>();
        en_drugAdapter = new EnDrugAdapter(mContext,ENDrugFragment.this,en_drug_list, Constant.DrugType_EN);
        en_drugAdapter.add();
        lv_en_druglist.setAdapter(en_drugAdapter);

        //合计
        tv_total = (TextView) view.findViewById(R.id.tv_total);
        return view;
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * 计算合计
     */
    public void setTotalFee(){
        Double total = 0.0;
        for (int i=0;i<en_drug_list.size();i++){
            if(en_drug_list.get(i).SubTotal.length() > 0){
                total = total + Double.parseDouble(en_drug_list.get(i).SubTotal);
            }
        }
        tv_total.setText(total + "");
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
        List<DrugBean> drugList = en_drugAdapter.getList();
        if(drugList.size() == 0){
            return false;
        }else{
            for(int i=0;i<drugList.size();i++){
                DrugBean drug = drugList.get(i);
                if( drug.Quantity.trim().length() == 0 || drug.Drug.DrugName.trim().length() == 0){
                    return false;
                }

                if( drug.Frequency.equals("频率")){
                    return false;
                }

                if( drug.DrugRouteName.equals("途径")){
                    return false;
                }
            }
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

        //检查药品参数
        List<DrugBean> drugList = en_drugAdapter.getList();
        if(drugList.size() == 0){
            return false;
        }else{
            for(int i=0;i<drugList.size();i++){
                DrugBean drug = drugList.get(i);
                if( drug.Quantity.trim().length() > 0 || drug.Drug.DrugName.trim().length() > 0){
                    inputNum = inputNum + 1;
                }

                if( !drug.Frequency.equals("频率")){
                    inputNum = inputNum + 1;
                }

                if( !drug.DrugRouteName.equals("途径")){
                    inputNum = inputNum + 1;
                }
            }
        }

        if(inputNum > 0){
            ToastUtils.showShort(mContext,mContext.getResources().getString(R.string.string_save_remind3));
            return true;
        }

        return false;
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
        return en_drugAdapter.getSaveList();
    }
}