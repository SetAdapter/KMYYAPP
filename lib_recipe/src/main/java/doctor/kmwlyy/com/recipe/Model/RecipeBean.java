package doctor.kmwlyy.com.recipe.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/22.
 */
public class RecipeBean implements Serializable{

    /**
     * OPDRegisterID : 82e1b60d8ec74689b36c04c7f6bd2b66
     * MemberID : a787eb80bba34d3eaf1f048c47b58a21
     * UserID : 6a7eb192c8f04dda904911f5c3f43c0d
     * Sympton : 健健康康健康快乐健康快乐离开了
     * PastMedicalHistory : -
     * PresentHistoryIllness : -
     * PhysicalExam : [{"ItemCode":"TC","ItemCNName":"TC","ItemENName":"总胆固醇","Result":"","RefRange":"","Unit":"mmol/L"},{"ItemCode":"Height","ItemCNName":"Height","ItemENName":"身高","Result":"","RefRange":"","Unit":"cm"},{"ItemCode":"GLU","ItemCNName":"GLU","ItemENName":"空腹血糖","Result":"","RefRange":"","Unit":"mmol/L"},{"ItemCode":"Weight","ItemCNName":"Weight","ItemENName":"体重","Result":"","RefRange":"","Unit":"kg"},{"ItemCode":"Temperature","ItemCNName":"Temperature","ItemENName":"体温","Result":"","RefRange":"","Unit":"℃"},{"ItemCode":"HeartRate","ItemCNName":"HeartRate","ItemENName":"心率","Result":"","RefRange":"","Unit":"次/分"},{"ItemCode":"Breathe","ItemCNName":"Breathe","ItemENName":"呼吸","Result":"","RefRange":"","Unit":"次/分"},{"ItemCode":"Waistline","ItemCNName":"Waistline","ItemENName":"腰围","Result":"","RefRange":"","Unit":"cm"},{"ItemCode":"PBG","ItemCNName":"PBG","ItemENName":"餐后血糖","Result":"","RefRange":"","Unit":"mmol/L"},{"ItemCode":"BloodPressure","ItemCNName":"BloodPressure","ItemENName":"血压","Result":"","RefRange":"","Unit":"mmHg"}]
     * Tags : []
     * MedicalRecord : {"Sympton":"健健康康健康快乐健康快乐离开了","PastMedicalHistory":"-","PresentHistoryIllness":"-"}
     * RecipeList : [{"DiagnoseList":[{"Detail":{"DiagnoseID":"273a10c13509492d9e86bc9f965c38c5","DiseaseCode":"-","DiseaseName":"红斑型","DiagnoseType":2,"Description":"离开了","IsPrimary":false},"Description":"离开了","IsPrimary":true}],"RecipeFileID":"CC2017010315440001","DoctorID":"CDCEA38AD8C04063AA79DAC2ADF673E7","MemberID":"a787eb80bba34d3eaf1f048c47b58a21","OPDRegisterID":"82e1b60d8ec74689b36c04c7f6bd2b66","RecipeDate":"2017-01-03","RecipeNo":"CC2017010315440002","RecipeName":"中药处方","FreqDay":0,"RecipeFileStatus":0,"RecipeType":1,"TCMQuantity":5,"Usage":"兔兔兔兔","Amount":446.25,"BoilWay":0,"ReplaceDose":0,"ReplacePrice":0,"DecoctNum":0,"DecoctTargetWater":0,"DecoctTotalWater":0,"FreqTimes":0,"Times":0,"Details":[{"Dose":5,"Quantity":1,"DrugRouteName":"","Frequency":"","Drug":{"DrugID":"0-070204","DrugCode":"070204","DrugName":"红景天（6g/袋）","Specification":"","PinYinName":"HJT","TotalDose":0,"Unit":"袋","UnitPrice":17.85,"DrugType":1,"Status":0}}]}]
     * ConsultationOpinions : []
     */

    private String OPDRegisterID;
    private String MemberID;
    private String UserID;
    private String Sympton;
    private String PastMedicalHistory;
    private String PresentHistoryIllness;
    /**
     * Sympton : 健健康康健康快乐健康快乐离开了
     * PastMedicalHistory : -
     * PresentHistoryIllness : -
     */

    private PatientDiagnoseBean.MedicalRecordBean MedicalRecord;
    /**
     * ItemCode : TC
     * ItemCNName : TC
     * ItemENName : 总胆固醇
     * Result :
     * RefRange :
     * Unit : mmol/L
     */

    private List<PhysicalExamBean> PhysicalExam;
    private List<?> Tags;
    /**
     * DiagnoseList : [{"Detail":{"DiagnoseID":"273a10c13509492d9e86bc9f965c38c5","DiseaseCode":"-","DiseaseName":"红斑型","DiagnoseType":2,"Description":"离开了","IsPrimary":false},"Description":"离开了","IsPrimary":true}]
     * RecipeFileID : CC2017010315440001
     * DoctorID : CDCEA38AD8C04063AA79DAC2ADF673E7
     * MemberID : a787eb80bba34d3eaf1f048c47b58a21
     * OPDRegisterID : 82e1b60d8ec74689b36c04c7f6bd2b66
     * RecipeDate : 2017-01-03
     * RecipeNo : CC2017010315440002
     * RecipeName : 中药处方
     * FreqDay : 0
     * RecipeFileStatus : 0
     * RecipeType : 1
     * TCMQuantity : 5
     * Usage : 兔兔兔兔
     * Amount : 446.25
     * BoilWay : 0
     * ReplaceDose : 0
     * ReplacePrice : 0
     * DecoctNum : 0
     * DecoctTargetWater : 0
     * DecoctTotalWater : 0
     * FreqTimes : 0
     * Times : 0
     * Details : [{"Dose":5,"Quantity":1,"DrugRouteName":"","Frequency":"","Drug":{"DrugID":"0-070204","DrugCode":"070204","DrugName":"红景天（6g/袋）","Specification":"","PinYinName":"HJT","TotalDose":0,"Unit":"袋","UnitPrice":17.85,"DrugType":1,"Status":0}}]
     */

    private List<RecipeListBean> RecipeList;
    private List<?> ConsultationOpinions;

    public String getOPDRegisterID() {
        return OPDRegisterID;
    }

    public void setOPDRegisterID(String OPDRegisterID) {
        this.OPDRegisterID = OPDRegisterID;
    }

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String MemberID) {
        this.MemberID = MemberID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getSympton() {
        return Sympton;
    }

    public void setSympton(String Sympton) {
        this.Sympton = Sympton;
    }

    public String getPastMedicalHistory() {
        return PastMedicalHistory;
    }

    public void setPastMedicalHistory(String PastMedicalHistory) {
        this.PastMedicalHistory = PastMedicalHistory;
    }

    public String getPresentHistoryIllness() {
        return PresentHistoryIllness;
    }

    public void setPresentHistoryIllness(String PresentHistoryIllness) {
        this.PresentHistoryIllness = PresentHistoryIllness;
    }

    public PatientDiagnoseBean.MedicalRecordBean getMedicalRecord() {
        return MedicalRecord;
    }

    public void setMedicalRecord(PatientDiagnoseBean.MedicalRecordBean MedicalRecord) {
        this.MedicalRecord = MedicalRecord;
    }

    public List<PhysicalExamBean> getPhysicalExam() {
        return PhysicalExam;
    }

    public void setPhysicalExam(List<PhysicalExamBean> PhysicalExam) {
        this.PhysicalExam = PhysicalExam;
    }

    public List<?> getTags() {
        return Tags;
    }

    public void setTags(List<?> Tags) {
        this.Tags = Tags;
    }

    public List<RecipeListBean> getRecipeList() {
        return RecipeList;
    }

    public void setRecipeList(List<RecipeListBean> RecipeList) {
        this.RecipeList = RecipeList;
    }

    public List<?> getConsultationOpinions() {
        return ConsultationOpinions;
    }

    public void setConsultationOpinions(List<?> ConsultationOpinions) {
        this.ConsultationOpinions = ConsultationOpinions;
    }

    public static class MedicalRecordBean implements Serializable{
        private String Sympton;
        private String PastMedicalHistory;
        private String PresentHistoryIllness;

        public String getSympton() {
            return Sympton;
        }

        public void setSympton(String Sympton) {
            this.Sympton = Sympton;
        }

        public String getPastMedicalHistory() {
            return PastMedicalHistory;
        }

        public void setPastMedicalHistory(String PastMedicalHistory) {
            this.PastMedicalHistory = PastMedicalHistory;
        }

        public String getPresentHistoryIllness() {
            return PresentHistoryIllness;
        }

        public void setPresentHistoryIllness(String PresentHistoryIllness) {
            this.PresentHistoryIllness = PresentHistoryIllness;
        }
    }

    public static class PhysicalExamBean implements Serializable{
        private String ItemCode;
        private String ItemCNName;
        private String ItemENName;
        private String Result;
        private String RefRange;
        private String Unit;

        public String getItemCode() {
            return ItemCode;
        }

        public void setItemCode(String ItemCode) {
            this.ItemCode = ItemCode;
        }

        public String getItemCNName() {
            return ItemCNName;
        }

        public void setItemCNName(String ItemCNName) {
            this.ItemCNName = ItemCNName;
        }

        public String getItemENName() {
            return ItemENName;
        }

        public void setItemENName(String ItemENName) {
            this.ItemENName = ItemENName;
        }

        public String getResult() {
            return Result;
        }

        public void setResult(String Result) {
            this.Result = Result;
        }

        public String getRefRange() {
            return RefRange;
        }

        public void setRefRange(String RefRange) {
            this.RefRange = RefRange;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String Unit) {
            this.Unit = Unit;
        }
    }

    public static class RecipeListBean implements Serializable {
        public RecipeListBean() {
            DiagnoseList = new ArrayList<>();
            Details = new ArrayList<>();
            Usage = "";
            BoilWay = 0;
        }

        private String RecipeFileID;
        private String DoctorID;
        private String MemberID;
        private String OPDRegisterID;
        private String RecipeDate;
        private String RecipeNo;
        private String RecipeName;
        private int FreqDay;
        private int RecipeFileStatus;
        private int RecipeType;
        private int TCMQuantity;
        private String Usage;
        private String Remark;
        private double Amount;
        private int BoilWay;
        private int ReplaceDose;
        private double ReplacePrice;
        private int DecoctNum;
        private int DecoctTargetWater;
        private int DecoctTotalWater;
        private int FreqTimes;
        private int Times;
        /**
         * Detail : {"DiagnoseID":"273a10c13509492d9e86bc9f965c38c5","DiseaseCode":"-","DiseaseName":"红斑型","DiagnoseType":2,"Description":"离开了","IsPrimary":false}
         * Description : 离开了
         * IsPrimary : true
         */

        private List<DiagnoseListBean> DiagnoseList;
        /**
         * Dose : 5
         * Quantity : 1
         * DrugRouteName :
         * Frequency :
         * Drug : {"DrugID":"0-070204","DrugCode":"070204","DrugName":"红景天（6g/袋）","Specification":"","PinYinName":"HJT","TotalDose":0,"Unit":"袋","UnitPrice":17.85,"DrugType":1,"Status":0}
         */

        private List<DetailsBean> Details;

        public String getRecipeFileID() {
            return RecipeFileID;
        }

        public void setRecipeFileID(String RecipeFileID) {
            this.RecipeFileID = RecipeFileID;
        }

        public String getDoctorID() {
            return DoctorID;
        }

        public void setDoctorID(String DoctorID) {
            this.DoctorID = DoctorID;
        }

        public String getMemberID() {
            return MemberID;
        }

        public void setMemberID(String MemberID) {
            this.MemberID = MemberID;
        }

        public String getOPDRegisterID() {
            return OPDRegisterID;
        }

        public void setOPDRegisterID(String OPDRegisterID) {
            this.OPDRegisterID = OPDRegisterID;
        }

        public String getRecipeDate() {
            return RecipeDate;
        }

        public void setRecipeDate(String RecipeDate) {
            this.RecipeDate = RecipeDate;
        }

        public String getRecipeNo() {
            return RecipeNo;
        }

        public void setRecipeNo(String RecipeNo) {
            this.RecipeNo = RecipeNo;
        }

        public String getRecipeName() {
            return RecipeName;
        }

        public void setRecipeName(String RecipeName) {
            this.RecipeName = RecipeName;
        }

        public int getFreqDay() {
            return FreqDay;
        }

        public void setFreqDay(int FreqDay) {
            this.FreqDay = FreqDay;
        }

        public int getRecipeFileStatus() {
            return RecipeFileStatus;
        }

        public void setRecipeFileStatus(int RecipeFileStatus) {
            this.RecipeFileStatus = RecipeFileStatus;
        }

        public int getRecipeType() {
            return RecipeType;
        }

        public void setRecipeType(int RecipeType) {
            this.RecipeType = RecipeType;
        }

        public int getTCMQuantity() {
            return TCMQuantity;
        }

        public void setTCMQuantity(int TCMQuantity) {
            this.TCMQuantity = TCMQuantity;
        }

        public String getUsage() {
            return Usage;
        }

        public void setUsage(String Usage) {
            this.Usage = Usage;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public double getAmount() {
            return Amount;
        }

        public void setAmount(double Amount) {
            this.Amount = Amount;
        }

        public int getBoilWay() {
            return BoilWay;
        }

        public void setBoilWay(int BoilWay) {
            this.BoilWay = BoilWay;
        }

        public int getReplaceDose() {
            return ReplaceDose;
        }

        public void setReplaceDose(int ReplaceDose) {
            this.ReplaceDose = ReplaceDose;
        }

        public double getReplacePrice() {
            return ReplacePrice;
        }

        public void setReplacePrice(double ReplacePrice) {
            this.ReplacePrice = ReplacePrice;
        }

        public int getDecoctNum() {
            return DecoctNum;
        }

        public void setDecoctNum(int DecoctNum) {
            this.DecoctNum = DecoctNum;
        }

        public int getDecoctTargetWater() {
            return DecoctTargetWater;
        }

        public void setDecoctTargetWater(int DecoctTargetWater) {
            this.DecoctTargetWater = DecoctTargetWater;
        }

        public int getDecoctTotalWater() {
            return DecoctTotalWater;
        }

        public void setDecoctTotalWater(int DecoctTotalWater) {
            this.DecoctTotalWater = DecoctTotalWater;
        }

        public int getFreqTimes() {
            return FreqTimes;
        }

        public void setFreqTimes(int FreqTimes) {
            this.FreqTimes = FreqTimes;
        }

        public int getTimes() {
            return Times;
        }

        public void setTimes(int Times) {
            this.Times = Times;
        }

        public List<DiagnoseListBean> getDiagnoseList() {
            return DiagnoseList;
        }

        public void setDiagnoseList(List<DiagnoseListBean> DiagnoseList) {
            this.DiagnoseList = DiagnoseList;
        }

        public List<DetailsBean> getDetails() {
            return Details;
        }

        public void setDetails(List<DetailsBean> Details) {
            this.Details = Details;
        }

        public static class DiagnoseListBean implements Serializable{
            /**
             * DiagnoseID : 273a10c13509492d9e86bc9f965c38c5
             * DiseaseCode : -
             * DiseaseName : 红斑型
             * DiagnoseType : 2
             * Description : 离开了
             * IsPrimary : false
             */

            private DetailBean Detail;
            private String Description;
            private boolean IsPrimary;

            public DetailBean getDetail() {
                return Detail;
            }

            public void setDetail(DetailBean Detail) {
                this.Detail = Detail;
            }

            public String getDescription() {
                return Description;
            }

            public void setDescription(String Description) {
                this.Description = Description;
            }

            public boolean isIsPrimary() {
                return IsPrimary;
            }

            public void setIsPrimary(boolean IsPrimary) {
                this.IsPrimary = IsPrimary;
            }

            public static class DetailBean implements Serializable{
                private String DiagnoseID;
                private String DiseaseCode;
                private String DiseaseName;
                private int DiagnoseType;
                private String Description;
                private boolean IsPrimary;

                public String getDiagnoseID() {
                    return DiagnoseID;
                }

                public void setDiagnoseID(String DiagnoseID) {
                    this.DiagnoseID = DiagnoseID;
                }

                public String getDiseaseCode() {
                    return DiseaseCode;
                }

                public void setDiseaseCode(String DiseaseCode) {
                    this.DiseaseCode = DiseaseCode;
                }

                public String getDiseaseName() {
                    return DiseaseName;
                }

                public void setDiseaseName(String DiseaseName) {
                    this.DiseaseName = DiseaseName;
                }

                public int getDiagnoseType() {
                    return DiagnoseType;
                }

                public void setDiagnoseType(int DiagnoseType) {
                    this.DiagnoseType = DiagnoseType;
                }

                public String getDescription() {
                    return Description;
                }

                public void setDescription(String Description) {
                    this.Description = Description;
                }

                public boolean isIsPrimary() {
                    return IsPrimary;
                }

                public void setIsPrimary(boolean IsPrimary) {
                    this.IsPrimary = IsPrimary;
                }
            }
        }

        public static class DetailsBean implements Serializable{
            public DetailsBean() {
                Dose = 0.0;
                Quantity = 0;
                DrugRouteName = "";
                Frequency = "";
                Drug = new DrugBean();
            }

            private Double Dose;
            private int Quantity;
            private String DrugRouteName;
            private String Frequency;
            /**
             * DrugID : 0-070204
             * DrugCode : 070204
             * DrugName : 红景天（6g/袋）
             * Specification :
             * PinYinName : HJT
             * TotalDose : 0
             * Unit : 袋
             * UnitPrice : 17.85
             * DrugType : 1
             * Status : 0
             */

            private DrugBean Drug;

            public Double getDose() {
                return Dose;
            }

            public void setDose(Double Dose) {
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

            public static class DrugBean implements Serializable{
                private String DrugID;
                private String DrugCode;
                private String DrugName;
                private String Specification;
                private String PinYinName;
                private String DoseUnit;
                private int TotalDose;
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

                public String getPinYinName() {
                    return PinYinName;
                }

                public void setPinYinName(String PinYinName) {
                    this.PinYinName = PinYinName;
                }

                public String getDoseUnit() {
                    return DoseUnit;
                }

                public void setDoseUnit(String doseUnit) {
                    DoseUnit = doseUnit;
                }

                public int getTotalDose() {
                    return TotalDose;
                }

                public void setTotalDose(int TotalDose) {
                    this.TotalDose = TotalDose;
                }

                public String getUnit() {
                    return Unit;
                }

                public void setUnit(String Unit) {
                    this.Unit = Unit;
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
            }
        }
    }
}
