package doctor.kmwlyy.com.recipe.Model;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public class DS_SaveBean {

    /**
     * DoctorID : 89F9E5907FD04DBF96A9867D1FA30396
     * MemberID : 26e802e1cdad49648c1c4070d6b50771
     * DrugstoreRecipeID : 8c5c17b6469249ddadbf1a9b07a2dcbf
     * DrugstoreRecipeStatus : 2
     * RecipeFileDTOs : [{"RecipeFileID":"C2016101914150099502354","DoctorID":"89F9E5907FD04DBF96A9867D1FA30396","MemberID":"26e802e1cdad49648c1c4070d6b50771","RecipeDate":"2016-10-19","RecipeName":"中药处方","RecipeFileStatus":0,"RecipeType":1,"RecipeTypeName":"中药处方","TCMQuantity":1,"Usage":"","Amount":10,"Details":[{"Dose":1,"Quantity":1,"DrugRouteName":"","Frequency":"","Drug":{"DrugID":"E4B87731CA2A40E6BF33FEF46986BD16","DrugCode":"50101115198","DrugName":"合欢花","Specification":"10g*100","PinYinName":"HHH","TotalDose":0,"DoseUnit":"g","Unit":"kg","UnitPrice":10,"DrugType":1}}],"Diagnoses":[{"Detail":{"DiagnoseID":"6b2d4c03d73c44a1b4def2d14886680c","DiseaseCode":"-","DiseaseName":"bbbbb","DiagnoseType":2,"Description":"","IsPrimary":false},"Description":"","IsPrimary":true}]},{"RecipeFileID":"E2016101914150077980831","DoctorID":"89F9E5907FD04DBF96A9867D1FA30396","MemberID":"26e802e1cdad49648c1c4070d6b50771","RecipeDate":"2016-10-19","RecipeName":"西药处方","RecipeFileStatus":0,"RecipeType":2,"RecipeTypeName":"西药处方","TCMQuantity":1,"Usage":"","Amount":10,"Details":[{"Dose":1,"Quantity":1,"DrugRouteName":"煎服","Frequency":"QD","Drug":{"DrugID":"E75283CED31D4EF494AD825972CC2DC3","DrugCode":"B0805503","DrugName":"山东东阿阿胶","Specification":"250g","PinYinName":"SDDAAJ","TotalDose":0,"DoseUnit":"g","Unit":"盒","UnitPrice":10,"DrugType":2,"Status":0}}],"Diagnoses":[{"Detail":{"DiagnoseID":"f3094d55def7467eabcdf8d2d2eca2ee","DiseaseCode":"-","DiseaseName":"aaaaa","DiagnoseType":1,"Description":"","IsPrimary":false},"Description":"","IsPrimary":true}]}]
     */

    public String DoctorID;
    public String MemberID;
    public String DrugstoreRecipeID;
    public String DrugSellDate;
    public int DrugstoreRecipeStatus;
    /**
     * RecipeFileID : C2016101914150099502354
     * DoctorID : 89F9E5907FD04DBF96A9867D1FA30396
     * MemberID : 26e802e1cdad49648c1c4070d6b50771
     * RecipeDate : 2016-10-19
     * RecipeName : 中药处方
     * RecipeFileStatus : 0
     * RecipeType : 1
     * RecipeTypeName : 中药处方
     * TCMQuantity : 1
     * Usage :
     * Amount : 10
     * Details : [{"Dose":1,"Quantity":1,"DrugRouteName":"","Frequency":"","Drug":{"DrugID":"E4B87731CA2A40E6BF33FEF46986BD16","DrugCode":"50101115198","DrugName":"合欢花","Specification":"10g*100","PinYinName":"HHH","TotalDose":0,"DoseUnit":"g","Unit":"kg","UnitPrice":10,"DrugType":1}}]
     * Diagnoses : [{"Detail":{"DiagnoseID":"6b2d4c03d73c44a1b4def2d14886680c","DiseaseCode":"-","DiseaseName":"bbbbb","DiagnoseType":2,"Description":"","IsPrimary":false},"Description":"","IsPrimary":true}]
     */

    public List<DSRecipeDetailBean.RecipeListBean> RecipeFileDTOs;




}