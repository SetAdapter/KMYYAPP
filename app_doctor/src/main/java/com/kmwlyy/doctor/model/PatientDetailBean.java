package com.kmwlyy.doctor.model;

/**
 * Created by Administrator on 2016/8/11.
 */
public class PatientDetailBean {

    /**
     * MemberID : 1f36d5177e3049098afa17801852b2a0
     * UserID : 10618a70cf124bc69905c23debd6dd7f
     * MemberName : 牛雨
     * Relation : 0
     * Gender : 0
     * Marriage : 0
     * Birthday : 1973-04-20
     * Mobile : 15013591424
     * IDType : 0
     * IDNumber : 360829197304203537
     * Address :
     * Email :
     * PostCode :
     * GenderName : 男性
     * Age : 44
     * IsDefault : false
     */

    private String MemberID;
    private String UserID;
    private String MemberName;
    private int Relation;
    private int Gender;
    private int Marriage;
    private String Birthday;
    private String Mobile;
    private int IDType;
    private String IDNumber;
    private String Address;
    private String Email;
    private String PostCode;
    private String GenderName;
    private int Age;
    private boolean IsDefault;

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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPostCode() {
        return PostCode;
    }

    public void setPostCode(String PostCode) {
        this.PostCode = PostCode;
    }

    public String getGenderName() {
        return GenderName;
    }

    public void setGenderName(String GenderName) {
        this.GenderName = GenderName;
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