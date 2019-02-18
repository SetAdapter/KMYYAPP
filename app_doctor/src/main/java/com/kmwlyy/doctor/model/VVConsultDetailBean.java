package com.kmwlyy.doctor.model;

import java.util.List;

public class VVConsultDetailBean {


    /**
     * OPDRegisterID : 915bd4d712aa494bbc1f992238a1e60a
     * UserID : 6a7eb192c8f04dda904911f5c3f43c0d
     * DoctorID : CDCEA38AD8C04063AA79DAC2ADF673E7
     * ScheduleID : 065527f20ade4451b7b858f0a90bf14a
     * RegDate : 2017-01-10T10:38:32.683
     * OPDDate : 2017-01-10T00:00:00
     * OPDType : 3
     * Fee : 0.01
     * MemberID : 4324b931b3c844c6afe7a4dbd65e48c2
     * Order : {"OrderNo":"SP2017011010380001","TradeNo":"","LogisticNo":"","PayType":-1,"OrderType":100,"OrderState":2,"RefundState":0,"LogisticState":0,"CostType":0,"OrderTime":"0001-01-01T00:00:00","OrderExpireTime":"0001-01-01T00:00:00","OriginalPrice":0,"TotalFee":0,"IsEvaluated":false}
     * Member : {"MemberName":"霸王龙","Relation":0,"Gender":0,"Marriage":0,"Birthday":"1990-09-11","IDType":0,"Age":27,"IsDefault":false}
     * Doctor : {"DoctorID":"CDCEA38AD8C04063AA79DAC2ADF673E7","DoctorName":"林东浩","Gender":0,"Marriage":0,"IDType":0,"IsConsultation":false,"IsExpert":false,"IsFreeClinicr":false,"Title":"主任医师","CheckState":0,"Sort":0,"FollowNum":0,"DiagnoseNum":0,"ConsultNum":0,"ConsulServicePrice":0,"DoctorServices":[{"ServiceType":2,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false},{"ServiceType":4,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false},{"ServiceType":1,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false},{"ServiceType":3,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false}],"User":{"UserType":2,"_PhotoUrl":"images/ff80fbb4366a4a5c88ad5c5f1df4b96a","PhotoUrl":"http://121.15.153.63:8027/images/ff80fbb4366a4a5c88ad5c5f1df4b96a","Score":0,"Star":0,"Comment":0,"Good":0,"Fans":0,"Grade":0,"Checked":0,"RegTime":"0001-01-01T00:00:00","CancelTime":"0001-01-01T00:00:00","UserState":0,"UserLevel":0,"LastTime":"0001-01-01T00:00:00","identifier":0},"IsFollowed":false}
     * Room : {"ServiceType":0,"ChannelID":10975,"Secret":"7541b48ef60c4501a2317fc9b0c7ba7e","RoomState":3,"BeginTime":"0001-01-01T00:00:00","EndTime":"0001-01-01T00:00:00","TotalTime":0}
     * ConsultContent : 嘿girl啦老K考虑图啦啦啊fool亏pull了考虑图兔兔
     * RecipeFiles : []
     * UserMedicalRecord : {"UserMedicalRecordID":"771c763d8b8d409fa93948131015e0df","OPDRegisterID":"915bd4d712aa494bbc1f992238a1e60a","UserID":"6a7eb192c8f04dda904911f5c3f43c0d","MemberID":"4324b931b3c844c6afe7a4dbd65e48c2","DoctorID":"CDCEA38AD8C04063AA79DAC2ADF673E7","Sympton":"我是住宿","PastMedicalHistory":"我是寄养","PresentHistoryIllness":"我是馅饼","PreliminaryDiagnosis":"我是初步","AllergicHistory":"我是过敏","Advised":"我是意见","OrgnazitionID":""}
     * RecipeFileUrl : http://121.15.153.63:8027//PDF/Recipe/915bd4d712aa494bbc1f992238a1e60a/
     */

    private String OPDRegisterID;
    private String UserID;
    private String DoctorID;
    private String ScheduleID;
    private String RegDate;
    private String OPDDate;
    private int OPDType;
    private double Fee;
    private String MemberID;
    /**
     * OrderNo : SP2017011010380001
     * TradeNo :
     * LogisticNo :
     * PayType : -1
     * OrderType : 100
     * OrderState : 2
     * RefundState : 0
     * LogisticState : 0
     * CostType : 0
     * OrderTime : 0001-01-01T00:00:00
     * OrderExpireTime : 0001-01-01T00:00:00
     * OriginalPrice : 0.0
     * TotalFee : 0.0
     * IsEvaluated : false
     */

    private OrderBean Order;
    /**
     * MemberName : 霸王龙
     * Relation : 0
     * Gender : 0
     * Marriage : 0
     * Birthday : 1990-09-11
     * IDType : 0
     * Age : 27
     * IsDefault : false
     */

    private MemberBean Member;
    /**
     * DoctorID : CDCEA38AD8C04063AA79DAC2ADF673E7
     * DoctorName : 林东浩
     * Gender : 0
     * Marriage : 0
     * IDType : 0
     * IsConsultation : false
     * IsExpert : false
     * IsFreeClinicr : false
     * Title : 主任医师
     * CheckState : 0
     * Sort : 0
     * FollowNum : 0
     * DiagnoseNum : 0
     * ConsultNum : 0
     * ConsulServicePrice : 0.0
     * DoctorServices : [{"ServiceType":2,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false},{"ServiceType":4,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false},{"ServiceType":1,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false},{"ServiceType":3,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false}]
     * User : {"UserType":2,"_PhotoUrl":"images/ff80fbb4366a4a5c88ad5c5f1df4b96a","PhotoUrl":"http://121.15.153.63:8027/images/ff80fbb4366a4a5c88ad5c5f1df4b96a","Score":0,"Star":0,"Comment":0,"Good":0,"Fans":0,"Grade":0,"Checked":0,"RegTime":"0001-01-01T00:00:00","CancelTime":"0001-01-01T00:00:00","UserState":0,"UserLevel":0,"LastTime":"0001-01-01T00:00:00","identifier":0}
     * IsFollowed : false
     */

    private DoctorBean Doctor;
    /**
     * ServiceType : 0
     * ChannelID : 10975
     * Secret : 7541b48ef60c4501a2317fc9b0c7ba7e
     * RoomState : 3
     * BeginTime : 0001-01-01T00:00:00
     * EndTime : 0001-01-01T00:00:00
     * TotalTime : 0
     */

    private RoomBean Room;
    private String ConsultContent;
    /**
     * UserMedicalRecordID : 771c763d8b8d409fa93948131015e0df
     * OPDRegisterID : 915bd4d712aa494bbc1f992238a1e60a
     * UserID : 6a7eb192c8f04dda904911f5c3f43c0d
     * MemberID : 4324b931b3c844c6afe7a4dbd65e48c2
     * DoctorID : CDCEA38AD8C04063AA79DAC2ADF673E7
     * Sympton : 我是住宿
     * PastMedicalHistory : 我是寄养
     * PresentHistoryIllness : 我是馅饼
     * PreliminaryDiagnosis : 我是初步
     * AllergicHistory : 我是过敏
     * Advised : 我是意见
     * OrgnazitionID :
     */

    private UserMedicalRecordBean UserMedicalRecord;
    private String RecipeFileUrl;
    private List<?> RecipeFiles;

    public String getOPDRegisterID() {
        return OPDRegisterID;
    }

    public void setOPDRegisterID(String OPDRegisterID) {
        this.OPDRegisterID = OPDRegisterID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String DoctorID) {
        this.DoctorID = DoctorID;
    }

    public String getScheduleID() {
        return ScheduleID;
    }

    public void setScheduleID(String ScheduleID) {
        this.ScheduleID = ScheduleID;
    }

    public String getRegDate() {
        return RegDate;
    }

    public void setRegDate(String RegDate) {
        this.RegDate = RegDate;
    }

    public String getOPDDate() {
        return OPDDate;
    }

    public void setOPDDate(String OPDDate) {
        this.OPDDate = OPDDate;
    }

    public int getOPDType() {
        return OPDType;
    }

    public void setOPDType(int OPDType) {
        this.OPDType = OPDType;
    }

    public double getFee() {
        return Fee;
    }

    public void setFee(double Fee) {
        this.Fee = Fee;
    }

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String MemberID) {
        this.MemberID = MemberID;
    }

    public OrderBean getOrder() {
        return Order;
    }

    public void setOrder(OrderBean Order) {
        this.Order = Order;
    }

    public MemberBean getMember() {
        return Member;
    }

    public void setMember(MemberBean Member) {
        this.Member = Member;
    }

    public DoctorBean getDoctor() {
        return Doctor;
    }

    public void setDoctor(DoctorBean Doctor) {
        this.Doctor = Doctor;
    }

    public RoomBean getRoom() {
        return Room;
    }

    public void setRoom(RoomBean Room) {
        this.Room = Room;
    }

    public String getConsultContent() {
        return ConsultContent;
    }

    public void setConsultContent(String ConsultContent) {
        this.ConsultContent = ConsultContent;
    }

    public UserMedicalRecordBean getUserMedicalRecord() {
        return UserMedicalRecord;
    }

    public void setUserMedicalRecord(UserMedicalRecordBean UserMedicalRecord) {
        this.UserMedicalRecord = UserMedicalRecord;
    }

    public String getRecipeFileUrl() {
        return RecipeFileUrl;
    }

    public void setRecipeFileUrl(String RecipeFileUrl) {
        this.RecipeFileUrl = RecipeFileUrl;
    }

    public List<?> getRecipeFiles() {
        return RecipeFiles;
    }

    public void setRecipeFiles(List<?> RecipeFiles) {
        this.RecipeFiles = RecipeFiles;
    }

    public static class OrderBean {
        private String OrderNo;
        private String TradeNo;
        private String LogisticNo;
        private int PayType;
        private int OrderType;
        private int OrderState;
        private int RefundState;
        private int LogisticState;
        private int CostType;
        private String OrderTime;
        private String OrderExpireTime;
        private double OriginalPrice;
        private double TotalFee;
        private boolean IsEvaluated;

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
        }

        public String getTradeNo() {
            return TradeNo;
        }

        public void setTradeNo(String TradeNo) {
            this.TradeNo = TradeNo;
        }

        public String getLogisticNo() {
            return LogisticNo;
        }

        public void setLogisticNo(String LogisticNo) {
            this.LogisticNo = LogisticNo;
        }

        public int getPayType() {
            return PayType;
        }

        public void setPayType(int PayType) {
            this.PayType = PayType;
        }

        public int getOrderType() {
            return OrderType;
        }

        public void setOrderType(int OrderType) {
            this.OrderType = OrderType;
        }

        public int getOrderState() {
            return OrderState;
        }

        public void setOrderState(int OrderState) {
            this.OrderState = OrderState;
        }

        public int getRefundState() {
            return RefundState;
        }

        public void setRefundState(int RefundState) {
            this.RefundState = RefundState;
        }

        public int getLogisticState() {
            return LogisticState;
        }

        public void setLogisticState(int LogisticState) {
            this.LogisticState = LogisticState;
        }

        public int getCostType() {
            return CostType;
        }

        public void setCostType(int CostType) {
            this.CostType = CostType;
        }

        public String getOrderTime() {
            return OrderTime;
        }

        public void setOrderTime(String OrderTime) {
            this.OrderTime = OrderTime;
        }

        public String getOrderExpireTime() {
            return OrderExpireTime;
        }

        public void setOrderExpireTime(String OrderExpireTime) {
            this.OrderExpireTime = OrderExpireTime;
        }

        public double getOriginalPrice() {
            return OriginalPrice;
        }

        public void setOriginalPrice(double OriginalPrice) {
            this.OriginalPrice = OriginalPrice;
        }

        public double getTotalFee() {
            return TotalFee;
        }

        public void setTotalFee(double TotalFee) {
            this.TotalFee = TotalFee;
        }

        public boolean isIsEvaluated() {
            return IsEvaluated;
        }

        public void setIsEvaluated(boolean IsEvaluated) {
            this.IsEvaluated = IsEvaluated;
        }
    }

    public static class MemberBean {
        private String MemberName;
        private int Relation;
        private int Gender;
        private int Marriage;
        private String Birthday;
        private int IDType;
        private int Age;
        private boolean IsDefault;

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
        private String DoctorID;
        private String DoctorName;
        private int Gender;
        private int Marriage;
        private int IDType;
        private boolean IsConsultation;
        private boolean IsExpert;
        private boolean IsFreeClinicr;
        private String Title;
        private int CheckState;
        private int Sort;
        private int FollowNum;
        private int DiagnoseNum;
        private int ConsultNum;
        private double ConsulServicePrice;
        /**
         * UserType : 2
         * _PhotoUrl : images/ff80fbb4366a4a5c88ad5c5f1df4b96a
         * PhotoUrl : http://121.15.153.63:8027/images/ff80fbb4366a4a5c88ad5c5f1df4b96a
         * Score : 0
         * Star : 0
         * Comment : 0
         * Good : 0
         * Fans : 0
         * Grade : 0
         * Checked : 0
         * RegTime : 0001-01-01T00:00:00
         * CancelTime : 0001-01-01T00:00:00
         * UserState : 0
         * UserLevel : 0
         * LastTime : 0001-01-01T00:00:00
         * identifier : 0
         */

        private UserBean User;
        private boolean IsFollowed;
        /**
         * ServiceType : 2
         * ServiceSwitch : 0
         * ServicePrice : 0.0
         * HasSchedule : false
         */

        private List<DoctorServicesBean> DoctorServices;

        public String getDoctorID() {
            return DoctorID;
        }

        public void setDoctorID(String DoctorID) {
            this.DoctorID = DoctorID;
        }

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

        public boolean isIsFreeClinicr() {
            return IsFreeClinicr;
        }

        public void setIsFreeClinicr(boolean IsFreeClinicr) {
            this.IsFreeClinicr = IsFreeClinicr;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
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

        public int getFollowNum() {
            return FollowNum;
        }

        public void setFollowNum(int FollowNum) {
            this.FollowNum = FollowNum;
        }

        public int getDiagnoseNum() {
            return DiagnoseNum;
        }

        public void setDiagnoseNum(int DiagnoseNum) {
            this.DiagnoseNum = DiagnoseNum;
        }

        public int getConsultNum() {
            return ConsultNum;
        }

        public void setConsultNum(int ConsultNum) {
            this.ConsultNum = ConsultNum;
        }

        public double getConsulServicePrice() {
            return ConsulServicePrice;
        }

        public void setConsulServicePrice(double ConsulServicePrice) {
            this.ConsulServicePrice = ConsulServicePrice;
        }

        public UserBean getUser() {
            return User;
        }

        public void setUser(UserBean User) {
            this.User = User;
        }

        public boolean isIsFollowed() {
            return IsFollowed;
        }

        public void setIsFollowed(boolean IsFollowed) {
            this.IsFollowed = IsFollowed;
        }

        public List<DoctorServicesBean> getDoctorServices() {
            return DoctorServices;
        }

        public void setDoctorServices(List<DoctorServicesBean> DoctorServices) {
            this.DoctorServices = DoctorServices;
        }

        public static class UserBean {
            private int UserType;
            private String _PhotoUrl;
            private String PhotoUrl;
            private int Score;
            private int Star;
            private int Comment;
            private int Good;
            private int Fans;
            private int Grade;
            private int Checked;
            private String RegTime;
            private String CancelTime;
            private int UserState;
            private int UserLevel;
            private String LastTime;
            private int identifier;

            public int getUserType() {
                return UserType;
            }

            public void setUserType(int UserType) {
                this.UserType = UserType;
            }

            public String get_PhotoUrl() {
                return _PhotoUrl;
            }

            public void set_PhotoUrl(String _PhotoUrl) {
                this._PhotoUrl = _PhotoUrl;
            }

            public String getPhotoUrl() {
                return PhotoUrl;
            }

            public void setPhotoUrl(String PhotoUrl) {
                this.PhotoUrl = PhotoUrl;
            }

            public int getScore() {
                return Score;
            }

            public void setScore(int Score) {
                this.Score = Score;
            }

            public int getStar() {
                return Star;
            }

            public void setStar(int Star) {
                this.Star = Star;
            }

            public int getComment() {
                return Comment;
            }

            public void setComment(int Comment) {
                this.Comment = Comment;
            }

            public int getGood() {
                return Good;
            }

            public void setGood(int Good) {
                this.Good = Good;
            }

            public int getFans() {
                return Fans;
            }

            public void setFans(int Fans) {
                this.Fans = Fans;
            }

            public int getGrade() {
                return Grade;
            }

            public void setGrade(int Grade) {
                this.Grade = Grade;
            }

            public int getChecked() {
                return Checked;
            }

            public void setChecked(int Checked) {
                this.Checked = Checked;
            }

            public String getRegTime() {
                return RegTime;
            }

            public void setRegTime(String RegTime) {
                this.RegTime = RegTime;
            }

            public String getCancelTime() {
                return CancelTime;
            }

            public void setCancelTime(String CancelTime) {
                this.CancelTime = CancelTime;
            }

            public int getUserState() {
                return UserState;
            }

            public void setUserState(int UserState) {
                this.UserState = UserState;
            }

            public int getUserLevel() {
                return UserLevel;
            }

            public void setUserLevel(int UserLevel) {
                this.UserLevel = UserLevel;
            }

            public String getLastTime() {
                return LastTime;
            }

            public void setLastTime(String LastTime) {
                this.LastTime = LastTime;
            }

            public int getIdentifier() {
                return identifier;
            }

            public void setIdentifier(int identifier) {
                this.identifier = identifier;
            }
        }

        public static class DoctorServicesBean {
            private int ServiceType;
            private int ServiceSwitch;
            private double ServicePrice;
            private boolean HasSchedule;

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

            public double getServicePrice() {
                return ServicePrice;
            }

            public void setServicePrice(double ServicePrice) {
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

    public static class RoomBean {
        private int ServiceType;
        private int ChannelID;
        private String Secret;
        private int RoomState;
        private String BeginTime;
        private String EndTime;
        private int TotalTime;

        public int getServiceType() {
            return ServiceType;
        }

        public void setServiceType(int ServiceType) {
            this.ServiceType = ServiceType;
        }

        public int getChannelID() {
            return ChannelID;
        }

        public void setChannelID(int ChannelID) {
            this.ChannelID = ChannelID;
        }

        public String getSecret() {
            return Secret;
        }

        public void setSecret(String Secret) {
            this.Secret = Secret;
        }

        public int getRoomState() {
            return RoomState;
        }

        public void setRoomState(int RoomState) {
            this.RoomState = RoomState;
        }

        public String getBeginTime() {
            return BeginTime;
        }

        public void setBeginTime(String BeginTime) {
            this.BeginTime = BeginTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public int getTotalTime() {
            return TotalTime;
        }

        public void setTotalTime(int TotalTime) {
            this.TotalTime = TotalTime;
        }
    }

    public static class UserMedicalRecordBean {
        private String UserMedicalRecordID;
        private String OPDRegisterID;
        private String UserID;
        private String MemberID;
        private String DoctorID;
        private String Sympton;
        private String PastMedicalHistory;
        private String PresentHistoryIllness;
        private String PreliminaryDiagnosis;
        private String AllergicHistory;
        private String Advised;
        private String OrgnazitionID;

        public String getUserMedicalRecordID() {
            return UserMedicalRecordID;
        }

        public void setUserMedicalRecordID(String UserMedicalRecordID) {
            this.UserMedicalRecordID = UserMedicalRecordID;
        }

        public String getOPDRegisterID() {
            return OPDRegisterID;
        }

        public void setOPDRegisterID(String OPDRegisterID) {
            this.OPDRegisterID = OPDRegisterID;
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

        public String getDoctorID() {
            return DoctorID;
        }

        public void setDoctorID(String DoctorID) {
            this.DoctorID = DoctorID;
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

        public String getPreliminaryDiagnosis() {
            return PreliminaryDiagnosis;
        }

        public void setPreliminaryDiagnosis(String PreliminaryDiagnosis) {
            this.PreliminaryDiagnosis = PreliminaryDiagnosis;
        }

        public String getAllergicHistory() {
            return AllergicHistory;
        }

        public void setAllergicHistory(String AllergicHistory) {
            this.AllergicHistory = AllergicHistory;
        }

        public String getAdvised() {
            return Advised;
        }

        public void setAdvised(String Advised) {
            this.Advised = Advised;
        }

        public String getOrgnazitionID() {
            return OrgnazitionID;
        }

        public void setOrgnazitionID(String OrgnazitionID) {
            this.OrgnazitionID = OrgnazitionID;
        }
    }
}
