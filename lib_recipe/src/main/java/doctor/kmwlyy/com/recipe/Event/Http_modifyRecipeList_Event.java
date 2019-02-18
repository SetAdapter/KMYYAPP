package doctor.kmwlyy.com.recipe.Event;

import com.alibaba.fastjson.JSON;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import doctor.kmwlyy.com.recipe.Model.DrugBean;
import doctor.kmwlyy.com.recipe.Model.RecipeLModifyBean;

public class Http_modifyRecipeList_Event extends HttpEvent<String> {

    public Http_modifyRecipeList_Event(String name, List<DrugBean> drugList, RecipeLModifyBean bean, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorRecipeFormulaFiles";
        mReqMethod = HttpClient.PUT;
        noParmName = true;
        mReqParams = new HashMap();

        if(null != bean){
            //修改处方名字
            bean.RecipeFormulaName = name;
            //修改处方药品
            bean.Details = parse(drugList);
        }

        String str = JSON.toJSONString(bean);
        try {
            mJsonData = new String(str.getBytes(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public List<RecipeLModifyBean.DetailsBean> parse(List<DrugBean> drugList){
        List<RecipeLModifyBean.DetailsBean> list = new ArrayList<>();
        if(null!=drugList && drugList.size()>0){
            for(int i=0;i<drugList.size();i++){
                RecipeLModifyBean.DetailsBean bean = new RecipeLModifyBean.DetailsBean();
                bean.RecipeFormulaDetailID = drugList.get(i).RecipeFormulaDetailID;
                bean.Dose = Integer.parseInt(drugList.get(i).Dose);
                bean.Quantity = Integer.parseInt(drugList.get(i).Quantity.equals("")?"0":drugList.get(i).Quantity);
                bean.DoseUnit = drugList.get(i).Drug.DoseUnit;
                bean.DrugRouteName = drugList.get(i).DrugRouteName;
                bean.Frequency = drugList.get(i).Frequency;
                bean.Drug.DrugID = drugList.get(i).Drug.DrugID;
                bean.Drug.DrugCode = drugList.get(i).Drug.DrugCode;
                bean.Drug.DrugName = drugList.get(i).Drug.DrugName;
                bean.Drug.DoseUnit = drugList.get(i).Drug.DoseUnit;
                list.add(bean);
            }
        }
        return list;
    }


//            mReqParams.put("RecipeFormulaFileID", bean.RecipeFormulaFileID);
//            mReqParams.put("DoctorID", bean.DoctorID);
//            mReqParams.put("RecipeFormulaName", name);
//            mReqParams.put("RecipeType", ""+bean.RecipeType);
//            mReqParams.put("Scope", bean.Scope);
//            mReqParams.put("Details", JSON.toJSONString(bean.Details));
}