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
import doctor.kmwlyy.com.recipe.Model.RecipeLAddBean;

public class Http_addRecipeList_Event extends HttpEvent<String> {

    public Http_addRecipeList_Event(String name, String type, List<DrugBean> drugList, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorRecipeFormulaFiles";
        mReqMethod = HttpClient.POST;
        noParmName = true;
        mReqParams = new HashMap<>();

        RecipeLAddBean bean = new RecipeLAddBean();
        bean.RecipeFormulaName = name;
        bean.RecipeType = type.equals("中药处方")?"1":"2";//处方类型 1=中药处方,2=西药处方
        bean.Details = parse(drugList);
        String str = JSON.toJSONString(bean);

        try {
            mJsonData = new String(str.getBytes(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public List<RecipeLAddBean.DetailsBean> parse(List<DrugBean> drugList){
        List<RecipeLAddBean.DetailsBean> list = new ArrayList<>();
        if(null!=drugList && drugList.size()>0){
            for(int i=0;i<drugList.size();i++){
                RecipeLAddBean.DetailsBean bean = new RecipeLAddBean.DetailsBean();
                bean.Dose = Integer.parseInt(drugList.get(i).Dose);
                bean.DrugRouteName = drugList.get(i).DrugRouteName;
                bean.Frequency = drugList.get(i).Frequency;
                bean.Drug.DrugID = drugList.get(i).Drug.DrugID;
                bean.Drug.DrugCode = drugList.get(i).Drug.DrugCode;
                bean.Drug.DoseUnit = drugList.get(i).Drug.DoseUnit;
                bean.Drug.DrugName = drugList.get(i).Drug.DrugName;
                list.add(bean);
            }
        }
        return list;
    }
}