package doctor.kmwlyy.com.recipe.Model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/18.
 */
public class DSRecipeDetailBean implements Serializable {


    /**
     * DrugstoreRecipeID : b8a16b2d13da433fb92ccbff8d93c2f7
     * ApplyDate : 2016-10-26
     * DrugSellDate : 2016-10-26
     * DrugstoreRecipeStatus : 3
     * Amount : 10
     * UserID : D18A287B422D4174B535874EFE89FA21
     * MemberID : 5d20b517beb94b29a143a268471cc419
     * UserMember : {"MemberName":"大理石","Relation":0,"Gender":1,"Marriage":0,"Birthday":"1988-05-01","IDType":0,"Age":29,"IsDefault":false}
     * DoctorID : 89F9E5907FD04DBF96A9867D1FA30396
     * Doctor : {"DoctorName":"邱浩强","Gender":0,"Marriage":0,"IDType":0,"IsConsultation":false,"IsExpert":false,"CheckState":0,"Sort":0,"DoctorServices":[{"ServiceType":2,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false},{"ServiceType":4,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false},{"ServiceType":1,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false},{"ServiceType":3,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false}],"IsFreeClinicr":false}
     * Details : [{"DrugstoreRecipeDetailID":"7a376833c00b4e9abb8230052b56c409","Amount":10}]
     * RecipeList : [{"RecipeFileID":"C2016102616302823745785","DoctorID":"89F9E5907FD04DBF96A9867D1FA30396","MemberID":"5d20b517beb94b29a143a268471cc419","RecipeDate":"2016-10-26","RecipeName":"中药处方","RecipeFileStatus":2,"RecipeType":1,"RecipeTypeName":"中药处方","TCMQuantity":1,"Usage":"","Amount":10,"Details":[{"Dose":1,"Quantity":1,"DrugRouteName":"","Frequency":"","Drug":{"DrugID":"8EA12C9B9B3748C0A3F5029B13B06AE2","DrugCode":"50101110126","DrugName":"白扁豆","Specification":"10g*100","PinYinName":"BBD","TotalDose":0,"DoseUnit":"g","Unit":"kg","UnitPrice":10,"DrugType":1,"Status":0}}],"Diagnoses":[{"Detail":{"DiagnoseID":"0340bd1b6ed94a16b3ebcd397815540e","DiseaseCode":"ZZGF11","DiseaseName":"肝风内动\u2014\u2014风窜络脉证","DiagnoseType":2,"Description":"","IsPrimary":false},"Description":"","IsPrimary":true}]}]
     * RecipeHis : [{"DrugstoreRecipeHisID":"8ef0d93a089b4c018c951852980ea113","DrugstoreRecipeID":"b8a16b2d13da433fb92ccbff8d93c2f7","OperationTimeDate":"2016-10-26T16:30:29.013","OperationTime":"2016-10-26 16:30","OpType":0,"OpUserID":"D18A287B422D4174B535874EFE89FA21","UserName":"测试药店","Remark":""},{"DrugstoreRecipeHisID":"305c64eef87044129fb3452a68c7219e","DrugstoreRecipeID":"b8a16b2d13da433fb92ccbff8d93c2f7","OperationTimeDate":"2016-10-26T16:30:29.013","OperationTime":"2016-10-26 16:30","OpType":2,"OpUserID":"D18A287B422D4174B535874EFE89FA21","UserName":"测试药店","Remark":""},{"DrugstoreRecipeHisID":"ddf29ad114ae41b3ad585b47f405bf08","DrugstoreRecipeID":"b8a16b2d13da433fb92ccbff8d93c2f7","OperationTimeDate":"2016-10-26T16:34:01.053","OperationTime":"2016-10-26 16:34","OpType":3,"OpUserID":"B04D4AE28F994AE2AACBB456D7E0647B","UserName":"邱浩强","Remark":""}]
     */

    public String DrugstoreRecipeID;
    public String ApplyDate;
    public String DrugSellDate;
    public int DrugstoreRecipeStatus;
    public Double Amount;
    public String UserID;
    public String MemberID;
    /**
     * MemberName : 大理石
     * Relation : 0
     * Gender : 1
     * Marriage : 0
     * Birthday : 1988-05-01
     * IDType : 0
     * Age : 29
     * IsDefault : false
     */

    public UserMemberBean UserMember;
    public String DoctorID;
    /**
     * DoctorName : 邱浩强
     * Gender : 0
     * Marriage : 0
     * IDType : 0
     * IsConsultation : false
     * IsExpert : false
     * CheckState : 0
     * Sort : 0
     * DoctorServices : [{"ServiceType":2,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false},{"ServiceType":4,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false},{"ServiceType":1,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false},{"ServiceType":3,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false}]
     * IsFreeClinicr : false
     */

    public DoctorBean Doctor;
    /**
     * DrugstoreRecipeDetailID : 7a376833c00b4e9abb8230052b56c409
     * Amount : 10
     */

    public List<DetailsBean> Details;
    /**
     * RecipeFileID : C2016102616302823745785
     * DoctorID : 89F9E5907FD04DBF96A9867D1FA30396
     * MemberID : 5d20b517beb94b29a143a268471cc419
     * RecipeDate : 2016-10-26
     * RecipeName : 中药处方
     * RecipeFileStatus : 2
     * RecipeType : 1
     * RecipeTypeName : 中药处方
     * TCMQuantity : 1
     * Usage :
     * Amount : 10
     * Details : [{"Dose":1,"Quantity":1,"DrugRouteName":"","Frequency":"","Drug":{"DrugID":"8EA12C9B9B3748C0A3F5029B13B06AE2","DrugCode":"50101110126","DrugName":"白扁豆","Specification":"10g*100","PinYinName":"BBD","TotalDose":0,"DoseUnit":"g","Unit":"kg","UnitPrice":10,"DrugType":1,"Status":0}}]
     * Diagnoses : [{"Detail":{"DiagnoseID":"0340bd1b6ed94a16b3ebcd397815540e","DiseaseCode":"ZZGF11","DiseaseName":"肝风内动\u2014\u2014风窜络脉证","DiagnoseType":2,"Description":"","IsPrimary":false},"Description":"","IsPrimary":true}]
     */

    public List<RecipeListBean> RecipeList;
    /**
     * DrugstoreRecipeHisID : 8ef0d93a089b4c018c951852980ea113
     * DrugstoreRecipeID : b8a16b2d13da433fb92ccbff8d93c2f7
     * OperationTimeDate : 2016-10-26T16:30:29.013
     * OperationTime : 2016-10-26 16:30
     * OpType : 0
     * OpUserID : D18A287B422D4174B535874EFE89FA21
     * UserName : 测试药店
     * Remark :
     */

    public List<RecipeHisBean> RecipeHis;

    public String getDrugstoreRecipeID() {
        return DrugstoreRecipeID;
    }

    public void setDrugstoreRecipeID(String DrugstoreRecipeID) {
        this.DrugstoreRecipeID = DrugstoreRecipeID;
    }

    public String getApplyDate() {
        return ApplyDate;
    }

    public void setApplyDate(String ApplyDate) {
        this.ApplyDate = ApplyDate;
    }

    public String getDrugSellDate() {
        return DrugSellDate;
    }

    public void setDrugSellDate(String DrugSellDate) {
        this.DrugSellDate = DrugSellDate;
    }

    public int getDrugstoreRecipeStatus() {
        return DrugstoreRecipeStatus;
    }

    public void setDrugstoreRecipeStatus(int DrugstoreRecipeStatus) {
        this.DrugstoreRecipeStatus = DrugstoreRecipeStatus;
    }

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double Amount) {
        this.Amount = Amount;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String MemberID) {
        this.MemberID = MemberID;
    }

    public UserMemberBean getUserMember() {
        return UserMember;
    }

    public void setUserMember(UserMemberBean UserMember) {
        this.UserMember = UserMember;
    }

    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String DoctorID) {
        this.DoctorID = DoctorID;
    }

    public DoctorBean getDoctor() {
        return Doctor;
    }

    public void setDoctor(DoctorBean Doctor) {
        this.Doctor = Doctor;
    }

    public List<DetailsBean> getDetails() {
        return Details;
    }

    public void setDetails(List<DetailsBean> Details) {
        this.Details = Details;
    }

    public List<RecipeListBean> getRecipeList() {
        return RecipeList;
    }

    public void setRecipeList(List<RecipeListBean> RecipeList) {
        this.RecipeList = RecipeList;
    }

    public List<RecipeHisBean> getRecipeHis() {
        return RecipeHis;
    }

    public void setRecipeHis(List<RecipeHisBean> RecipeHis) {
        this.RecipeHis = RecipeHis;
    }

    public static class UserMemberBean {
        public String MemberName;
        public int Relation;
        public int Gender;
        public int Marriage;
        public String Birthday;
        public int IDType;
        public int Age;
        public boolean IsDefault;

        public String getMemberName() {
            return MemberName;
        }

        public void setMemberName(String MemberName) {
            this.MemberName = MemberName;
        }

        public int getRelation() {
            return Relation;
        }

        public void setRelation(int Relation) {
            this.Relation = Relation;
        }

        public int getGender() {
            return Gender;
        }

        public void setGender(int Gender) {
            this.Gender = Gender;
        }

        public int getMarriage() {
            return Marriage;
        }

        public void setMarriage(int Marriage) {
            this.Marriage = Marriage;
        }

        public String getBirthday() {
            return Birthday;
        }

        public void setBirthday(String Birthday) {
            this.Birthday = Birthday;
        }

        public int getIDType() {
            return IDType;
        }

        public void setIDType(int IDType) {
            this.IDType = IDType;
        }

        public int getAge() {
            return Age;
        }

        public void setAge(int Age) {
            this.Age = Age;
        }

        public boolean isIsDefault() {
            return IsDefault;
        }

        public void setIsDefault(boolean IsDefault) {
            this.IsDefault = IsDefault;
        }
    }

    public static class DoctorBean {
        public String DoctorName;
        public int Gender;
        public int Marriage;
        public int IDType;
        public boolean IsConsultation;
        public boolean IsExpert;
        public int CheckState;
        public int Sort;
        public boolean IsFreeClinicr;
        /**
         * ServiceType : 2
         * ServiceSwitch : 0
         * ServicePrice : 0
         * HasSchedule : false
         */

        public List<DoctorServicesBean> DoctorServices;

        public String getDoctorName() {
            return DoctorName;
        }

        public void setDoctorName(String DoctorName) {
            this.DoctorName = DoctorName;
        }

        public int getGender() {
            return Gender;
        }

        public void setGender(int Gender) {
            this.Gender = Gender;
        }

        public int getMarriage() {
            return Marriage;
        }

        public void setMarriage(int Marriage) {
            this.Marriage = Marriage;
        }

        public int getIDType() {
            return IDType;
        }

        public void setIDType(int IDType) {
            this.IDType = IDType;
        }

        public boolean isIsConsultation() {
            return IsConsultation;
        }

        public void setIsConsultation(boolean IsConsultation) {
            this.IsConsultation = IsConsultation;
        }

        public boolean isIsExpert() {
            return IsExpert;
        }

        public void setIsExpert(boolean IsExpert) {
            this.IsExpert = IsExpert;
        }

        public int getCheckState() {
            return CheckState;
        }

        public void setCheckState(int CheckState) {
            this.CheckState = CheckState;
        }

        public int getSort() {
            return Sort;
        }

        public void setSort(int Sort) {
            this.Sort = Sort;
        }

        public boolean isIsFreeClinicr() {
            return IsFreeClinicr;
        }

        public void setIsFreeClinicr(boolean IsFreeClinicr) {
            this.IsFreeClinicr = IsFreeClinicr;
        }

        public List<DoctorServicesBean> getDoctorServices() {
            return DoctorServices;
        }

        public void setDoctorServices(List<DoctorServicesBean> DoctorServices) {
            this.DoctorServices = DoctorServices;
        }

        public static class DoctorServicesBean {
            public int ServiceType;
            public int ServiceSwitch;
            public int ServicePrice;
            public boolean HasSchedule;

            public int getServiceType() {
                return ServiceType;
            }

            public void setServiceType(int ServiceType) {
                this.ServiceType = ServiceType;
            }

            public int getServiceSwitch() {
                return ServiceSwitch;
            }

            public void setServiceSwitch(int ServiceSwitch) {
                this.ServiceSwitch = ServiceSwitch;
            }

            public int getServicePrice() {
                return ServicePrice;
            }

            public void setServicePrice(int ServicePrice) {
                this.ServicePrice = ServicePrice;
            }

            public boolean isHasSchedule() {
                return HasSchedule;
            }

            public void setHasSchedule(boolean HasSchedule) {
                this.HasSchedule = HasSchedule;
            }
        }
    }

    public static class DetailsBean {
        public String DrugstoreRecipeDetailID;
        public Double Amount;

        public String getDrugstoreRecipeDetailID() {
            return DrugstoreRecipeDetailID;
        }

        public void setDrugstoreRecipeDetailID(String DrugstoreRecipeDetailID) {
            this.DrugstoreRecipeDetailID = DrugstoreRecipeDetailID;
        }

        public Double getAmount() {
            return Amount;
        }

        public void setAmount(Double Amount) {
            this.Amount = Amount;
        }
    }

    public static class RecipeListBean {
        public String RecipeFileID;
        public String DoctorID;
        public String MemberID;
        public String RecipeDate;
        public String RecipeName;
        public int RecipeFileStatus;
        public int RecipeType;
        public String RecipeTypeName;
        public int TCMQuantity;
        public String Usage;
        public Double Amount;
        /**
         * Dose : 1
         * Quantity : 1
         * DrugRouteName :
         * Frequency :
         * Drug : {"DrugID":"8EA12C9B9B3748C0A3F5029B13B06AE2","DrugCode":"50101110126","DrugName":"白扁豆","Specification":"10g*100","PinYinName":"BBD","TotalDose":0,"DoseUnit":"g","Unit":"kg","UnitPrice":10,"DrugType":1,"Status":0}
         */

        public List<DetailsBean> Details;
        /**
         * Detail : {"DiagnoseID":"0340bd1b6ed94a16b3ebcd397815540e","DiseaseCode":"ZZGF11","DiseaseName":"肝风内动\u2014\u2014风窜络脉证","DiagnoseType":2,"Description":"","IsPrimary":false}
         * Description :
         * IsPrimary : true
         */

        public List<DiagnosesBean> Diagnoses;


        public static class DetailsBean {
            public Double Dose;
            public int Quantity;
            public String DrugRouteName;
            public String Frequency;

            public DetailsBean() {
                Dose = 0.0;
                Quantity = 0;
                DrugRouteName = "";
                Frequency = "";
                Drug = new DrugBean();
            }

            /**
             * DrugID : 8EA12C9B9B3748C0A3F5029B13B06AE2
             * DrugCode : 50101110126
             * DrugName : 白扁豆
             * Specification : 10g*100
             * PinYinName : BBD
             * TotalDose : 0
             * DoseUnit : g
             * Unit : kg
             * UnitPrice : 10
             * DrugType : 1
             * Status : 0
             */



            public DrugBean Drug;

            public static class DrugBean {
                public String DrugID;
                public String DrugCode;
                public String DrugName;
                public String Specification;
                public String PinYinName;
                public int TotalDose;
                public String DoseUnit;
                public String Unit;
                public Double UnitPrice;
                public int DrugType;
                public int Status;

                public DrugBean() {
                    DrugID = "";
                    DrugCode = "";
                    DrugName = "";
                    DoseUnit = "";
                    Unit = "";
                    UnitPrice = 0.0;
                    Specification = "";
                }
            }
        }

        public static class DiagnosesBean {
            /**
             * DiagnoseID : 0340bd1b6ed94a16b3ebcd397815540e
             * DiseaseCode : ZZGF11
             * DiseaseName : 肝风内动——风窜络脉证
             * DiagnoseType : 2
             * Description :
             * IsPrimary : false
             */

            public DetailBean Detail;
            public String Description;
            public boolean IsPrimary;

            public DiagnosesBean() {
                Detail = new DetailBean();
                Description = "";
                IsPrimary = false;
            }

            public static class DetailBean {
                public String DiagnoseID;
                public String DiseaseCode;
                public String DiseaseName;
                public int DiagnoseType;
                public String Description;
                public boolean IsPrimary;

                public DetailBean() {
                    DiseaseCode = "";
                    DiseaseName = "";
                    Description = "";
                }
            }
        }
    }

    public static class RecipeHisBean {
        public String DrugstoreRecipeHisID;
        public String DrugstoreRecipeID;
        public String OperationTimeDate;
        public String OperationTime;
        public int OpType;
        public String OpUserID;
        public String UserName;
        public String Remark;

        public String getDrugstoreRecipeHisID() {
            return DrugstoreRecipeHisID;
        }

        public void setDrugstoreRecipeHisID(String DrugstoreRecipeHisID) {
            this.DrugstoreRecipeHisID = DrugstoreRecipeHisID;
        }

        public String getDrugstoreRecipeID() {
            return DrugstoreRecipeID;
        }

        public void setDrugstoreRecipeID(String DrugstoreRecipeID) {
            this.DrugstoreRecipeID = DrugstoreRecipeID;
        }

        public String getOperationTimeDate() {
            return OperationTimeDate;
        }

        public void setOperationTimeDate(String OperationTimeDate) {
            this.OperationTimeDate = OperationTimeDate;
        }

        public String getOperationTime() {
            return OperationTime;
        }

        public void setOperationTime(String OperationTime) {
            this.OperationTime = OperationTime;
        }

        public int getOpType() {
            return OpType;
        }

        public void setOpType(int OpType) {
            this.OpType = OpType;
        }

        public String getOpUserID() {
            return OpUserID;
        }

        public void setOpUserID(String OpUserID) {
            this.OpUserID = OpUserID;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }
    }
}