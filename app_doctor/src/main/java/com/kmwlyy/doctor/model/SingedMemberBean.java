package com.kmwlyy.doctor.model;

import java.io.Serializable;

/**
 * Created by TFeng on 2017/7/6.
 */

public class SingedMemberBean implements Serializable {


    /**
     * FamilyDoctorID : 6c844221f3d547649d762289b5e8bff5
     * DoctorID : CDCEA38AD8C04063AA79DAC2ADF673E7
     * ServicePrice : 0
     * UserID : 6a7eb192c8f04dda904911f5c3f43c0d
     * StartDate : 2017-07-10T00:00:00
     * EndDate : 2017-08-09T00:00:00
     * EnableDays : 29
     * Status : 1
     * VidServiceCount : 10
     * VidConsumeCount : 0
     * PicServiceCount : 60
     * PicConsumeCount : 0
     * ForOrderType : 99
     * UserMember : {"MemberID":"a787eb80bba34d3eaf1f048c47b58a21","MemberName":"王胖子","Relation":0,"Gender":1,"Marriage":0,"Birthday":"1991-09-11","Mobile":"18620059888","IDType":0,"Address":"","Age":25,"IsDefault":false,"Identifier":0,"PhotoUrl":"https://tstore.kmwlyy.com:8027/images/04357ce3ddf834f6ba356fc0e9b99633"}
     */

    private String FamilyDoctorID;
    private String DoctorID;
    private int ServicePrice;
    private String UserID;
    private String StartDate;
    private String EndDate;
    private int EnableDays;
    private int Status;
    private int VidServiceCount;
    private int VidConsumeCount;
    private int PicServiceCount;
    private int PicConsumeCount;
    private int ForOrderType;
    private UserMemberBean UserMember;
    private String IDNumber;

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public String getFamilyDoctorID() {
        return FamilyDoctorID;
    }

    public void setFamilyDoctorID(String FamilyDoctorID) {
        this.FamilyDoctorID = FamilyDoctorID;
    }

    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String DoctorID) {
        this.DoctorID = DoctorID;
    }

    public int getServicePrice() {
        return ServicePrice;
    }

    public void setServicePrice(int ServicePrice) {
        this.ServicePrice = ServicePrice;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

    public int getEnableDays() {
        return EnableDays;
    }

    public void setEnableDays(int EnableDays) {
        this.EnableDays = EnableDays;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getVidServiceCount() {
        return VidServiceCount;
    }

    public void setVidServiceCount(int VidServiceCount) {
        this.VidServiceCount = VidServiceCount;
    }

    public int getVidConsumeCount() {
        return VidConsumeCount;
    }

    public void setVidConsumeCount(int VidConsumeCount) {
        this.VidConsumeCount = VidConsumeCount;
    }

    public int getPicServiceCount() {
        return PicServiceCount;
    }

    public void setPicServiceCount(int PicServiceCount) {
        this.PicServiceCount = PicServiceCount;
    }

    public int getPicConsumeCount() {
        return PicConsumeCount;
    }

    public void setPicConsumeCount(int PicConsumeCount) {
        this.PicConsumeCount = PicConsumeCount;
    }

    public int getForOrderType() {
        return ForOrderType;
    }

    public void setForOrderType(int ForOrderType) {
        this.ForOrderType = ForOrderType;
    }

    public UserMemberBean getUserMember() {
        return UserMember;
    }

    public void setUserMember(UserMemberBean UserMember) {
        this.UserMember = UserMember;
    }

    public static class UserMemberBean implements Serializable{
        /**
         * MemberID : a787eb80bba34d3eaf1f048c47b58a21
         * MemberName : 王胖子
         * Relation : 0
         * Gender : 1
         * Marriage : 0
         * Birthday : 1991-09-11
         * Mobile : 18620059888
         * IDType : 0
         * Address :
         * Age : 25
         * IsDefault : false
         * Identifier : 0
         * PhotoUrl : https://tstore.kmwlyy.com:8027/images/04357ce3ddf834f6ba356fc0e9b99633
         */

        private String MemberID;
        private String MemberName;
        private int Relation;
        private int Gender;
        private int Marriage;
        private String Birthday;
        private String Mobile;
        private int IDType;
        private String Address;
        private int Age;
        private boolean IsDefault;
        private int Identifier;
        private String PhotoUrl;
        private String IDNumber;
        private String Remark;


        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public String getIDNumber() {
            return IDNumber;
        }

        public void setIDNumber(String IDNumber) {
            this.IDNumber = IDNumber;
        }

        public String getMemberID() {
            return MemberID;
        }

        public void setMemberID(String MemberID) {
            this.MemberID = MemberID;
        }

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

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public int getIDType() {
            return IDType;
        }

        public void setIDType(int IDType) {
            this.IDType = IDType;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
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

        public int getIdentifier() {
            return Identifier;
        }

        public void setIdentifier(int Identifier) {
            this.Identifier = Identifier;
        }

        public String getPhotoUrl() {
            return PhotoUrl;
        }

        public void setPhotoUrl(String PhotoUrl) {
            this.PhotoUrl = PhotoUrl;
        }
    }
}
