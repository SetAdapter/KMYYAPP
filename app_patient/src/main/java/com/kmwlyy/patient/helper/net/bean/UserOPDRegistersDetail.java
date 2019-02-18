package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/23
 */
public class UserOPDRegistersDetail {

    @SerializedName("OPDRegisterID")
    public String mOPDRegisterID;

    @SerializedName("UserID")
    public String mUserID;

    @SerializedName("DoctorID")
    public String mDoctorID;

    @SerializedName("RegDate")
    public String mRegDate;

    @SerializedName("OPDDate")
    public String mOPDDate;

    @SerializedName("ScheduleID")
    public String mScheduleID;

    @SerializedName("OPDType")
    public int mOPDType;

    @SerializedName("MemberID")
    public String mMemberID;


    @SerializedName("Fee")
    public float mFee;

    @SerializedName("ConsultContent")
    public String mConsultContent;
}
