package com.kmwlyy.doctor.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/24.
 */
public class DoctorInfo implements Serializable {

    /**
     * DoctorID : 851ED3F4CD9A4248B35D53277570E9E2
     * DoctorName : 何坤才
     * UserID : 6E0DB2AE5F7B42549CD0744B34A8BC32
     * Gender : 1
     * Marriage : 0
     * Birthday : 19850808
     * IDType : 1
     * IDNumber :
     * Address :
     * PostCode :
     * Intro : <p>全科医生，执业医师，擅长常见病，多发病，慢性病诊疗，对养生保健、亚健康、老年人保健等调理有经验。</p>
     * IsConsultation : false
     * IsExpert : false
     * Specialty : 全科医生 保健养生 慢性病诊疗 五官科 呼吸内科 消化内科 心血管内科等
     * areaCode :
     * HospitalID : 42FF1C61132E443F862510FF3BC3B03A
     * HospitalName :
     * Grade : 0
     * DepartmentID : BCE87580389041A0A70F9465F305BBC2
     * DepartmentName :
     * Education :
     * Title : 主任医师
     * Duties :
     * CheckState : 0
     * SignatureURL :
     * Sort : 1
     * DoctorServices : [{"ServiceType":2,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false},{"ServiceType":4,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false},{"ServiceType":1,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false},{"ServiceType":3,"ServiceSwitch":0,"ServicePrice":0,"HasSchedule":false}]
     * User : {"UserID":"6E0DB2AE5F7B42549CD0744B34A8BC32","UserAccount":"13682603132","UserCNName":"何坤才","UserENName":"20164","UserType":2,"Mobile":"13682603132","Email":"","Password":"7c4a8d09ca3762af61e59520943dc26494f8941b","PayPassword":"","_PhotoUrl":"images/b18fe49acecb7062a6a790268986d34c","PhotoUrl":"http://10.2.21.216:8011/images/b18fe49acecb7062a6a790268986d34c","Score":0,"Star":0,"Comment":0,"Good":0,"Fans":0,"Grade":0,"Checked":0,"RegTime":"2016-08-01T17:40:07.92","CancelTime":"2016-08-01T17:40:07.92","UserState":0,"UserLevel":3,"Terminal":0,"LastTime":"2016-08-01T17:40:07.92","identifier":68}
     * IsFreeClinicr : false
     */

    private String DoctorID;
    private String DoctorName;
    private String UserID;
    private int Gender;
    private int Marriage;
    private String Birthday;
    private int IDType;
    private String IDNumber;
    private String Address;
    private String PostCode;
    private String Intro;
    private boolean IsConsultation;
    private boolean IsExpert;
    private String Specialty;
    private String areaCode;
    private String HospitalID;
    private String HospitalName;
    private String Grade;
    private String DepartmentID;
    private String DepartmentName;
    private String Education;
    private String Title;
    private String Duties;
    private int CheckState;
    private String SignatureURL;
    private int Sort;
    /**
     * UserID : 6E0DB2AE5F7B42549CD0744B34A8BC32
     * UserAccount : 13682603132
     * UserCNName : 何坤才
     * UserENName : 20164
     * UserType : 2
     * Mobile : 13682603132
     * Email :
     * Password : 7c4a8d09ca3762af61e59520943dc26494f8941b
     * PayPassword :
     * _PhotoUrl : images/b18fe49acecb7062a6a790268986d34c
     * PhotoUrl : http://10.2.21.216:8011/images/b18fe49acecb7062a6a790268986d34c
     * Score : 0
     * Star : 0
     * Comment : 0
     * Good : 0
     * Fans : 0
     * Grade : 0
     * Checked : 0
     * RegTime : 2016-08-01T17:40:07.92
     * CancelTime : 2016-08-01T17:40:07.92
     * UserState : 0
     * UserLevel : 3
     * Terminal : 0
     * LastTime : 2016-08-01T17:40:07.92
     * identifier : 68
     */

    private UserBean User;
    private boolean IsFreeClinicr;
    /**
     * ServiceType : 2
     * ServiceSwitch : 0
     * ServicePrice : 0
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

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
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

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getPostCode() {
        return PostCode;
    }

    public void setPostCode(String PostCode) {
        this.PostCode = PostCode;
    }

    public String getIntro() {
        return Intro;
    }

    public void setIntro(String Intro) {
        this.Intro = Intro;
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

    public String getSpecialty() {
        return Specialty;
    }

    public void setSpecialty(String Specialty) {
        this.Specialty = Specialty;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(String HospitalID) {
        this.HospitalID = HospitalID;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String HospitalName) {
        this.HospitalName = HospitalName;
    }

    public String getGrade() {
        return Grade;
    }

    public void setGrade(String Grade) {
        this.Grade = Grade;
    }

    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String DepartmentID) {
        this.DepartmentID = DepartmentID;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String DepartmentName) {
        this.DepartmentName = DepartmentName;
    }

    public String getEducation() {
        return Education;
    }

    public void setEducation(String Education) {
        this.Education = Education;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getDuties() {
        return Duties;
    }

    public void setDuties(String Duties) {
        this.Duties = Duties;
    }

    public int getCheckState() {
        return CheckState;
    }

    public void setCheckState(int CheckState) {
        this.CheckState = CheckState;
    }

    public String getSignatureURL() {
        return SignatureURL;
    }

    public void setSignatureURL(String SignatureURL) {
        this.SignatureURL = SignatureURL;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int Sort) {
        this.Sort = Sort;
    }

    public UserBean getUser() {
        return User;
    }

    public void setUser(UserBean User) {
        this.User = User;
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

    public static class UserBean {
        private String UserID;
        private String UserAccount;
        private String UserCNName;
        private String UserENName;
        private int UserType;
        private String Mobile;
        private String Email;
        private String Password;
        private String PayPassword;
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
        private String Terminal;
        private String LastTime;
        private int identifier;

        public String getUserID() {
            return UserID;
        }

        public void setUserID(String UserID) {
            this.UserID = UserID;
        }

        public String getUserAccount() {
            return UserAccount;
        }

        public void setUserAccount(String UserAccount) {
            this.UserAccount = UserAccount;
        }

        public String getUserCNName() {
            return UserCNName;
        }

        public void setUserCNName(String UserCNName) {
            this.UserCNName = UserCNName;
        }

        public String getUserENName() {
            return UserENName;
        }

        public void setUserENName(String UserENName) {
            this.UserENName = UserENName;
        }

        public int getUserType() {
            return UserType;
        }

        public void setUserType(int UserType) {
            this.UserType = UserType;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }

        public String getPayPassword() {
            return PayPassword;
        }

        public void setPayPassword(String PayPassword) {
            this.PayPassword = PayPassword;
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

        public String getTerminal() {
            return Terminal;
        }

        public void setTerminal(String Terminal) {
            this.Terminal = Terminal;
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
