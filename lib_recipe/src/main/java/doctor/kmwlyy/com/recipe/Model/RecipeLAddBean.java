package doctor.kmwlyy.com.recipe.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/19.
 */
public class RecipeLAddBean implements Serializable{


    /**
     * RecipeFormulaName : 西药处方
     * RecipeType : 2
     * Details : [{"Dose":12,"Quantity":1,"DrugRouteName":"口服","Frequency":"QD 每日一次","Drug":{"DrugID":"6C0B4D6102244083887C71588F4E6168","DrugCode":"10550","DrugName":"阿莫西林克拉维酸钾片","Specification":"0.457g*12片/盒","DrugExpiryDay":"0","BatchNO":"0","FactoryName":"珠海联邦制药厂有限公司中山分厂","PinYinName":"AMXLKLWSJP","LicenseNo":"0","TotalDose":12,"DoseUnit":"片","Unit":"盒","UnitPrice":18.39,"DrugType":2,"PharmacyID":"2","PharmacyDrugID":"10550","Status":0,"CreateTime":"2016-08-05T08:34:50.923","IsDeleted":false,"Quantity":1,"Dose":12}}]
     */

    public String RecipeFormulaName;
    public String RecipeType;
    /**
     * Dose : 12
     * Quantity : 1
     * DrugRouteName : 口服
     * Frequency : QD 每日一次
     * Drug : {"DrugID":"6C0B4D6102244083887C71588F4E6168","DrugCode":"10550","DrugName":"阿莫西林克拉维酸钾片","Specification":"0.457g*12片/盒","DrugExpiryDay":"0","BatchNO":"0","FactoryName":"珠海联邦制药厂有限公司中山分厂","PinYinName":"AMXLKLWSJP","LicenseNo":"0","TotalDose":12,"DoseUnit":"片","Unit":"盒","UnitPrice":18.39,"DrugType":2,"PharmacyID":"2","PharmacyDrugID":"10550","Status":0,"CreateTime":"2016-08-05T08:34:50.923","IsDeleted":false,"Quantity":1,"Dose":12}
     */

    public List<DetailsBean> Details;

    public static class DetailsBean {
        public int Dose;
        public int Quantity;
        public String DrugRouteName;
        public String Frequency;
        /**
         * DrugID : 6C0B4D6102244083887C71588F4E6168
         * DrugCode : 10550
         * DrugName : 阿莫西林克拉维酸钾片
         * Specification : 0.457g*12片/盒
         * DrugExpiryDay : 0
         * BatchNO : 0
         * FactoryName : 珠海联邦制药厂有限公司中山分厂
         * PinYinName : AMXLKLWSJP
         * LicenseNo : 0
         * TotalDose : 12
         * DoseUnit : 片
         * Unit : 盒
         * UnitPrice : 18.39
         * DrugType : 2
         * PharmacyID : 2
         * PharmacyDrugID : 10550
         * Status : 0
         * CreateTime : 2016-08-05T08:34:50.923
         * IsDeleted : false
         * Quantity : 1
         * Dose : 12
         */

        public DrugBean Drug;

        public DetailsBean() {
            Drug = new DrugBean();
        }

        public static class DrugBean {
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
            public String CreateTime;
            public boolean IsDeleted;
            public int Quantity;
            public int Dose;
        }
    }
}
