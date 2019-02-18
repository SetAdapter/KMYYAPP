package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by xcj on 2016/9/1.
 */
public class DoctorClinic implements Serializable {
    @SerializedName("DoctorID")
    public String mDoctorID;

    @SerializedName("DoctorName")
    public String mDoctorName;

    @SerializedName("Specialty")
    public String mSpecialty;

    @SerializedName("HospitalID")
    public String mHospitalID;

    @SerializedName("HospitalName")
    public String mHospitalName;

    @SerializedName("DepartmentID")
    public String mDepartmentID;

    @SerializedName("DepartmentName")
    public String mDepartmentName;

    @SerializedName("Title")
    public String mTitle;

    @SerializedName("Duties")
    public String mDuties;

    @SerializedName("User")
    public User mUser;

    @SerializedName("DoctorClinic")
    public DoctorClinics mDoctorClinic;

    public static class User {
        @SerializedName("UserID")
        public String mUserID;
        @SerializedName("PhotoUrl")
        public String mPhotoUrl;

    }
    public static class DoctorClinics {
        @SerializedName("AcceptCount")
        public int mAcceptCount;
        @SerializedName("CurrentCount")
        public int mCurrentCount;

    }


}
