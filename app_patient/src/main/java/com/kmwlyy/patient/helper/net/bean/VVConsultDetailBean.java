package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @Description描述: 音视频看诊详情
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/26
 */
public class VVConsultDetailBean {

    @SerializedName("OPDRegisterID")
    public String mOPDRegisterID;

    @SerializedName("UserID")
    public String mUserID;

    @SerializedName("DoctorID")
    public String mDoctorID;

    @SerializedName("ScheduleID")
    public String mScheduleID;

    @SerializedName("RegDate")
    public String mRegDate;

    @SerializedName("OPDDate")
    public String mOPDDate;

    @SerializedName("OPDType")
    public int mOPDType;

    @SerializedName("Fee")
    public float mFee;

    @SerializedName("MemberID")
    public String mMemberID;

    @SerializedName("ConsultContent")
    public String mConsultContent;

    @SerializedName("UserMedicalRecord")
    public UserMedicalRecord mUserMedicalRecord;

    @SerializedName("RecipeFiles")
    public ArrayList<ConsultRecipeBean> mRecipeFiles = new ArrayList<>();

    @SerializedName("RecipeFileUrl")
    public String mRecipeFileUrl;

    @SerializedName("Order")
    public UserOPDRegisters.Order mOrder;

    @SerializedName("Member")
    public UserOPDRegisters.Member mMember;

    public class UserMedicalRecord {

        @SerializedName("UserMedicalRecordID")
        public String mUserMedicalRecordID;

        @SerializedName("OPDRegisterID")
        public String mOPDRegisterID;

        @SerializedName("UserID")
        public String mUserID;

        @SerializedName("MemberID")
        public String mMemberID;

        @SerializedName("DoctorID")
        public String mDoctorID;

        @SerializedName("Sympton")
        public String mSympton;//主诉

        @SerializedName("PastMedicalHistory")
        public String mPastMedicalHistory;//既往病史

        @SerializedName("PresentHistoryIllness")
        public String mPresentHistoryIllness;//现病史

        @SerializedName("PreliminaryDiagnosis")
        public String mPreliminaryDiagnosis;//初步诊断

        @SerializedName("AllergicHistory")
        public String mAllergicHistory;//过敏史

        @SerializedName("Advised")
        public String mAdvised;//医嘱

        @SerializedName("OrgnazitionID")
        public String OrgnazitionID;

    }

}
