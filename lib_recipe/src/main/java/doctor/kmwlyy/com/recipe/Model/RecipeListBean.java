package doctor.kmwlyy.com.recipe.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/22.
 */
public class RecipeListBean implements Serializable {
    /**
     * RecipeFormulaFileID : 45cd6624967f4be5bd1755b821fb6c2e
     * DoctorID : 4B2DB2F7D37246E9B769B8E454C79F43
     * RecipeFormulaName : 西药处方B
     * RecipeType : 2
     * Scope :
     * Details : [{"RecipeFormulaDetailID":"7fe20ab8c5b442f09833947184b3e151","RecipeFormulaFileID":"45cd6624967f4be5bd1755b821fb6c2e","Dose":5.01,"Quantity":1,"DoseUnit":"片","Unit":"盒","DrugRouteName":"外用加棉签","Frequency":"TID 每日三次","Drug":{"DrugID":"0C58A74787D146179368614F1402C214","DrugCode":"50208106421","DrugName":"红金消结片","Specification":"0.42g*12片*3小盒","DrugExpiryDay":"0","BatchNO":"国药准字Z20080400                                     ","FactoryName":"深圳市泰康制药有限公司","PinYinName":"HJXJP","LicenseNo":"国药准字Z20080400                                     ","TotalDose":0,"DoseUnit":"片","Unit":"盒","UnitPrice":10,"DrugType":2,"PharmacyID":"00040006       ","PharmacyDrugID":"-","Status":0}}]
     * CreateTime : 2016-12-16T10:15:55.6234554
     */
    private String RecipeFormulaFileID;
    private String DoctorID;
    private String RecipeFormulaName;
    private int RecipeType;
    private String Scope;
    private String CreateTime;
    /**
     * RecipeFormulaDetailID : 7fe20ab8c5b442f09833947184b3e151
     * RecipeFormulaFileID : 45cd6624967f4be5bd1755b821fb6c2e
     * Dose : 5.01
     * Quantity : 1
     * DoseUnit : 片
     * Unit : 盒
     * DrugRouteName : 外用加棉签
     * Frequency : TID 每日三次
     * Drug : {"DrugID":"0C58A74787D146179368614F1402C214","DrugCode":"50208106421","DrugName":"红金消结片","Specification":"0.42g*12片*3小盒","DrugExpiryDay":"0","BatchNO":"国药准字Z20080400                                     ","FactoryName":"深圳市泰康制药有限公司","PinYinName":"HJXJP","LicenseNo":"国药准字Z20080400                                     ","TotalDose":0,"DoseUnit":"片","Unit":"盒","UnitPrice":10,"DrugType":2,"PharmacyID":"00040006       ","PharmacyDrugID":"-","Status":0}
     */

    private List<DetailsBean> Details;

    public RecipeListBean() {
    }

    public String getRecipeFormulaFileID() {
        return RecipeFormulaFileID;
    }

    public void setRecipeFormulaFileID(String RecipeFormulaFileID) {
        this.RecipeFormulaFileID = RecipeFormulaFileID;
    }

    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String DoctorID) {
        this.DoctorID = DoctorID;
    }

    public String getRecipeFormulaName() {
        return RecipeFormulaName;
    }

    public void setRecipeFormulaName(String RecipeFormulaName) {
        this.RecipeFormulaName = RecipeFormulaName;
    }

    public int getRecipeType() {
        return RecipeType;
    }

    public void setRecipeType(int RecipeType) {
        this.RecipeType = RecipeType;
    }

    public String getScope() {
        return Scope;
    }

    public void setScope(String Scope) {
        this.Scope = Scope;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public List<DetailsBean> getDetails() {
        return Details;
    }

    public void setDetails(List<DetailsBean> Details) {
        this.Details = Details;
    }

    public static class DetailsBean {
        private String RecipeFormulaDetailID;
        private String RecipeFormulaFileID;
        private double Dose;
        private int Quantity;
        private String DoseUnit;
        private String Unit;
        private String DrugRouteName;
        private String Frequency;
        /**
         * DrugID : 0C58A74787D146179368614F1402C214
         * DrugCode : 50208106421
         * DrugName : 红金消结片
         * Specification : 0.42g*12片*3小盒
         * DrugExpiryDay : 0
         * BatchNO : 国药准字Z20080400
         * FactoryName : 深圳市泰康制药有限公司
         * PinYinName : HJXJP
         * LicenseNo : 国药准字Z20080400
         * TotalDose : 0
         * DoseUnit : 片
         * Unit : 盒
         * UnitPrice : 10
         * DrugType : 2
         * PharmacyID : 00040006
         * PharmacyDrugID : -
         * Status : 0
         */

        private DrugBean Drug;

        public String getRecipeFormulaDetailID() {
            return RecipeFormulaDetailID;
        }

        public void setRecipeFormulaDetailID(String RecipeFormulaDetailID) {
            this.RecipeFormulaDetailID = RecipeFormulaDetailID;
        }

        public String getRecipeFormulaFileID() {
            return RecipeFormulaFileID;
        }

        public void setRecipeFormulaFileID(String RecipeFormulaFileID) {
            this.RecipeFormulaFileID = RecipeFormulaFileID;
        }

        public double getDose() {
            return Dose;
        }

        public void setDose(double Dose) {
            this.Dose = Dose;
        }

        public int getQuantity() {
            return Quantity;
        }

        public void setQuantity(int Quantity) {
            this.Quantity = Quantity;
        }

        public String getDoseUnit() {
            return DoseUnit;
        }

        public void setDoseUnit(String DoseUnit) {
            this.DoseUnit = DoseUnit;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String Unit) {
            this.Unit = Unit;
        }

        public String getDrugRouteName() {
            return DrugRouteName;
        }

        public void setDrugRouteName(String DrugRouteName) {
            this.DrugRouteName = DrugRouteName;
        }

        public String getFrequency() {
            return Frequency;
        }

        public void setFrequency(String Frequency) {
            this.Frequency = Frequency;
        }

        public DrugBean getDrug() {
            return Drug;
        }

        public void setDrug(DrugBean Drug) {
            this.Drug = Drug;
        }

        public static class DrugBean {
            private String DrugID;
            private String DrugCode;
            private String DrugName;
            private String Specification;
            private String DrugExpiryDay;
            private String BatchNO;
            private String FactoryName;
            private String PinYinName;
            private String LicenseNo;
            private int TotalDose;
            private String DoseUnit;
            private String Unit;
            private int UnitPrice;
            private int DrugType;
            private String PharmacyID;
            private String PharmacyDrugID;
            private int Status;

            public String getDrugID() {
                return DrugID;
            }

            public void setDrugID(String DrugID) {
                this.DrugID = DrugID;
            }

            public String getDrugCode() {
                return DrugCode;
            }

            public void setDrugCode(String DrugCode) {
                this.DrugCode = DrugCode;
            }

            public String getDrugName() {
                return DrugName;
            }

            public void setDrugName(String DrugName) {
                this.DrugName = DrugName;
            }

            public String getSpecification() {
                return Specification;
            }

            public void setSpecification(String Specification) {
                this.Specification = Specification;
            }

            public String getDrugExpiryDay() {
                return DrugExpiryDay;
            }

            public void setDrugExpiryDay(String DrugExpiryDay) {
                this.DrugExpiryDay = DrugExpiryDay;
            }

            public String getBatchNO() {
                return BatchNO;
            }

            public void setBatchNO(String BatchNO) {
                this.BatchNO = BatchNO;
            }

            public String getFactoryName() {
                return FactoryName;
            }

            public void setFactoryName(String FactoryName) {
                this.FactoryName = FactoryName;
            }

            public String getPinYinName() {
                return PinYinName;
            }

            public void setPinYinName(String PinYinName) {
                this.PinYinName = PinYinName;
            }

            public String getLicenseNo() {
                return LicenseNo;
            }

            public void setLicenseNo(String LicenseNo) {
                this.LicenseNo = LicenseNo;
            }

            public int getTotalDose() {
                return TotalDose;
            }

            public void setTotalDose(int TotalDose) {
                this.TotalDose = TotalDose;
            }

            public String getDoseUnit() {
                return DoseUnit;
            }

            public void setDoseUnit(String DoseUnit) {
                this.DoseUnit = DoseUnit;
            }

            public String getUnit() {
                return Unit;
            }

            public void setUnit(String Unit) {
                this.Unit = Unit;
            }

            public int getUnitPrice() {
                return UnitPrice;
            }

            public void setUnitPrice(int UnitPrice) {
                this.UnitPrice = UnitPrice;
            }

            public int getDrugType() {
                return DrugType;
            }

            public void setDrugType(int DrugType) {
                this.DrugType = DrugType;
            }

            public String getPharmacyID() {
                return PharmacyID;
            }

            public void setPharmacyID(String PharmacyID) {
                this.PharmacyID = PharmacyID;
            }

            public String getPharmacyDrugID() {
                return PharmacyDrugID;
            }

            public void setPharmacyDrugID(String PharmacyDrugID) {
                this.PharmacyDrugID = PharmacyDrugID;
            }

            public int getStatus() {
                return Status;
            }

            public void setStatus(int Status) {
                this.Status = Status;
            }
        }
    }
}
