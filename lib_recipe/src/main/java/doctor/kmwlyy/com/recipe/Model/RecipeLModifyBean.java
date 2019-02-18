package doctor.kmwlyy.com.recipe.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public class RecipeLModifyBean implements Serializable{

    /**
     * RecipeFormulaFileID : 29da476b02a144a288b15394136c9f9b
     * DoctorID : 4B2DB2F7D37246E9B769B8E454C79F43
     * RecipeFormulaName : ffggg
     * RecipeType : 2
     * Scope : 
     * Details : [{"RecipeFormulaDetailID":"ffabd66867944705a3d040d133540394","RecipeFormulaFileID":"29da476b02a144a288b15394136c9f9b","Dose":5,"Quantity":0,"DoseUnit":"斤","DrugRouteName":"涂口腔","Frequency":"BIW 每周二次","Drug":{"DrugID":"049F2E3B32FF464B917B319E24CD5EA3","DrugCode":"50208106400","DrugName":"调经丸","Specification":"9g*10丸","DrugExpiryDay":"0","BatchNO":"国药准字Z11020019                                     ","FactoryName":"北京同仁堂股份有限公司同仁堂制药厂","PinYinName":"DJW","LicenseNo":"国药准字Z11020019                                     ","TotalDose":0,"DoseUnit":"斤","Unit":"盒","UnitPrice":24.7,"DrugType":2,"PharmacyID":"00040020       ","PharmacyDrugID":"-","Status":0}}]
     * CreateTime : 2016-12-17T10:33:02.751001
     */

    public String RecipeFormulaFileID;
    public String DoctorID;
    public String RecipeFormulaName;
    public int RecipeType;
    public String Scope;
    public String CreateTime;
    /**
     * RecipeFormulaDetailID : ffabd66867944705a3d040d133540394
     * RecipeFormulaFileID : 29da476b02a144a288b15394136c9f9b
     * Dose : 5
     * Quantity : 0
     * DoseUnit : 斤
     * DrugRouteName : 涂口腔
     * Frequency : BIW 每周二次
     * Drug : {"DrugID":"049F2E3B32FF464B917B319E24CD5EA3","DrugCode":"50208106400","DrugName":"调经丸","Specification":"9g*10丸","DrugExpiryDay":"0","BatchNO":"国药准字Z11020019                                     ","FactoryName":"北京同仁堂股份有限公司同仁堂制药厂","PinYinName":"DJW","LicenseNo":"国药准字Z11020019                                     ","TotalDose":0,"DoseUnit":"斤","Unit":"盒","UnitPrice":24.7,"DrugType":2,"PharmacyID":"00040020       ","PharmacyDrugID":"-","Status":0}
     */

    public List<DetailsBean> Details;
    
    public static class DetailsBean implements Serializable{
        public String RecipeFormulaDetailID;
        public String RecipeFormulaFileID;
        public int Dose;
        public int Quantity;
        public String DoseUnit;
        public String DrugRouteName;
        public String Frequency;

        public DetailsBean() {
            RecipeFormulaDetailID = "";
            RecipeFormulaFileID = "";
            Dose = 0;
            Quantity = 0;
            DoseUnit = "";
            DrugRouteName = "";
            Frequency = "";
            Drug = new DrugBean();
        }

        /**
         * DrugID : 049F2E3B32FF464B917B319E24CD5EA3
         * DrugCode : 50208106400
         * DrugName : 调经丸
         * Specification : 9g*10丸
         * DrugExpiryDay : 0
         * BatchNO : 国药准字Z11020019                                     
         * FactoryName : 北京同仁堂股份有限公司同仁堂制药厂
         * PinYinName : DJW
         * LicenseNo : 国药准字Z11020019                                     
         * TotalDose : 0
         * DoseUnit : 斤
         * Unit : 盒
         * UnitPrice : 24.7
         * DrugType : 2
         * PharmacyID : 00040020       
         * PharmacyDrugID : -
         * Status : 0
         */

        public DrugBean Drug;

        public static class DrugBean implements Serializable{
            public String DrugID;
            public String DrugCode;
            public String DrugName;
            public String Specification;
            public String DrugExpiryDay;
            public String BatchNO;
            public String FactoryName;
            public String PinYinName;
            public String LicenseNo;
            public int TotalDose;
            public String DoseUnit;
            public String Unit;
            public double UnitPrice;
            public int DrugType;
            public String PharmacyID;
            public String PharmacyDrugID;
            public int Status;

            public DrugBean() {
                DrugID = "";
                DrugCode = "";
                DrugName = "";
                Specification = "";
            }
        }
    }
}