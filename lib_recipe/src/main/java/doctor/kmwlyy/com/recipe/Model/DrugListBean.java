package doctor.kmwlyy.com.recipe.Model;

import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
public class DrugListBean {

    /**
     * RecipeFormulaFileID : f01873532c34429db7a2831452f4e9c2
     * DoctorID : 851ED3F4CD9A4248B35D53277570E9E2
     * RecipeFormulaName : hhh
     * RecipeType : 1
     * Scope :
     * Details : [{"RecipeFormulaDetailID":"74f4f04c6b54482086b774b08fac766a","RecipeFormulaFileID":"f01873532c34429db7a2831452f4e9c2","Dose":5,"Quantity":0,"DrugRouteName":"","Frequency":"","Drug":{"DrugID":"0-051010","DrugCode":"051010","DrugName":"胡黄连","Specification":"","TotalDose":0,"DoseUnit":"g","UnitPrice":0,"DrugType":0,"Status":0}}]
     * CreateTime : 2017-01-18T18:17:00.7339941
     * ModifyTime : 2017-01-18T18:17:00.7339941
     */

    private String RecipeFormulaFileID;
    private String DoctorID;
    private String RecipeFormulaName;
    private int RecipeType;
    private String Scope;
    private String CreateTime;
    private String ModifyTime;
    /**
     * RecipeFormulaDetailID : 74f4f04c6b54482086b774b08fac766a
     * RecipeFormulaFileID : f01873532c34429db7a2831452f4e9c2
     * Dose : 5.0
     * Quantity : 0
     * DrugRouteName :
     * Frequency :
     * Drug : {"DrugID":"0-051010","DrugCode":"051010","DrugName":"胡黄连","Specification":"","TotalDose":0,"DoseUnit":"g","UnitPrice":0,"DrugType":0,"Status":0}
     */

    private List<DetailsBean> Details;

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

    public String getModifyTime() {
        return ModifyTime;
    }

    public void setModifyTime(String ModifyTime) {
        this.ModifyTime = ModifyTime;
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
        private String DrugRouteName;
        private String Frequency;
        /**
         * DrugID : 0-051010
         * DrugCode : 051010
         * DrugName : 胡黄连
         * Specification :
         * TotalDose : 0
         * DoseUnit : g
         * UnitPrice : 0.0
         * DrugType : 0
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
            private int TotalDose;
            private String DoseUnit;
            private String Unit;
            private double UnitPrice;
            private int DrugType;
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

            public double getUnitPrice() {
                return UnitPrice;
            }

            public void setUnitPrice(double UnitPrice) {
                this.UnitPrice = UnitPrice;
            }

            public int getDrugType() {
                return DrugType;
            }

            public void setDrugType(int DrugType) {
                this.DrugType = DrugType;
            }

            public int getStatus() {
                return Status;
            }

            public void setStatus(int Status) {
                this.Status = Status;
            }

            public String getUnit() {
                return Unit;
            }

            public void setUnit(String unit) {
                Unit = unit;
            }
        }
    }
}
