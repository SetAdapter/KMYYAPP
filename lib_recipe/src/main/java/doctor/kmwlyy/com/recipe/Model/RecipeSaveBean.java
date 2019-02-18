package doctor.kmwlyy.com.recipe.Model;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */
public class RecipeSaveBean {

    /**
     * DiagnoseList : [{"Detail":{"DiseaseCode":"af91ce2e58844bcc811a6297944787d7"},"Description":"诊断描述"},{"Detail":{"DiseaseName":"自定义的字段"},"Description":"诊断描述"}]
     * Details : [{"Dose":1,"Quantity":1,"DrugRouteName":"口服","Frequence":"","Drug":{"DrugID":"B9C68C1CA15F456C9B155C0E9FC18AAA"},"Frequency":"QD 每日一次"}]
     * Remark : 嘱托
     * FreqTimes : 1
     * FreqDay : 1
     * BoilWay : 1
     * ReplaceDose : 1
     * DecoctNum : 1
     * Times : 2
     * DecoctTargetWater : 500
     * DecoctTotalWater : 300
     * Usage :
     * TCMQuantity : 1
     * RecipeType : 2
     * RecipeName : 西药处方
     * RecipeNo : 处方编号
     * OPDRegisterID : 预约编号
     */

    public String Remark;
    public int FreqTimes;
    public int FreqDay;
    public String BoilWay;
    public int ReplaceDose;
    public int DecoctNum;
    public int Times;
    public int DecoctTargetWater;
    public int DecoctTotalWater;
    public String Usage;
    public int TCMQuantity;
    public int RecipeType;
    public String RecipeName;
    public String RecipeNo;
    public String OPDRegisterID;
    /**
     * Detail : {"DiseaseCode":"af91ce2e58844bcc811a6297944787d7"}
     * Description : 诊断描述
     */

    public List<DiagnosisBean> DiagnoseList;
    /**
     * Dose : 1
     * Quantity : 1
     * DrugRouteName : 口服
     * Frequence :
     * Drug : {"DrugID":"B9C68C1CA15F456C9B155C0E9FC18AAA"}
     * Frequency : QD 每日一次
     */

    public List<DrugBean_Recipe> Details;
}
