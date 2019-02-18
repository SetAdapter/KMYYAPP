package com.kmwlyy.doctor.model;

/**
 * Created by Administrator on 2016/8/11.
 */
public class PatientListBean {


    /**
     * DoctorMemberID : 1b0be3fb8ede4863a95b6b2653c6d2b3
     * DoctorID : CDCEA38AD8C04063AA79DAC2ADF673E7
     * MemberID : 1ac435ee8b9a4852a217417ff7a26d02
     * MemberName : test11
     * Gender : 0
     * GenderName : 男性
     * Birthday :
     * Mobile : 13537885242
     * Age : 0
     */

    private String DoctorMemberID;
    private String DoctorID;
    private String MemberID;
    private String MemberName;
    private int Gender;
    private String GenderName;
    private String Birthday;
    private String Mobile;
    private int Age;

    public String getDoctorMemberID() {
        return DoctorMemberID;
    }

    public void setDoctorMemberID(String DoctorMemberID) {
        this.DoctorMemberID = DoctorMemberID;
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

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String MemberName) {
        this.MemberName = MemberName;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int Gender) {
        this.Gender = Gender;
    }

    public String getGenderName() {
        return GenderName;
    }

    public void setGenderName(String GenderName) {
        this.GenderName = GenderName;
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

    public int getAge() {
        return Age;
    }

    public void setAge(int Age) {
        this.Age = Age;
    }
}
