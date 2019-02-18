package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Winson on 2016/8/20.
 */
public class UserFamilyDoctor {

    public static final int NOT_PAY = 0;
    public static final int PAYED = 1;
    public static final int EXPIRED = 2;

    @SerializedName("FamilyDoctorID")
    public String mFamilyDoctorID;//	家庭医生主键ID

    @SerializedName("DoctorID")
    public String mDoctorID;//	医生ID

    @SerializedName("DoctorName")
    public String mDoctorName;//	医生 姓名

    @SerializedName("DoctorPhotoUrl")
    public String mDoctorPhotoUrl;//	医生 头像

    @SerializedName("UserID")
    public String mUserID;//

    @SerializedName("StartDate")
    public String mStartDate;//	开始时间

    @SerializedName("EndDate")
    public String mEndDate;//	结束时间

    @SerializedName("IsEnable")
    public boolean mIsEnable;// 是否有效

    @SerializedName("Status")
    public int mStatus; // 0-待支付、1-已支付、2-已过期

    @SerializedName("Title")
    public String mTitle;//	职位

    @SerializedName("DepartmentName")
    public String mDepartmentName;//	科室

    @SerializedName("HospitalName")
    public String mHospitalName;//	医院名字

    @SerializedName("OrderNo")
    public String mOrderNo;

    @SerializedName("VidServiceCount")
    public int mVidServiceCount;

    @SerializedName("VidConsumeCount")
    public int mVidConsumeCount;

    @SerializedName("PicServiceCount")
    public int mPicServiceCount;

    @SerializedName("PicConsumeCount")
    public int mPicConsumeCount;


    public static class AddResp {

        @SerializedName("ServicePrice")
        public float mServicePrice;//":0,

        @SerializedName("StartDate")
        public String mStartDate;//":"0001-01-01T00:00:00",

        @SerializedName("EndDate")
        public String mEndDate;//":"0001-01-01T00:00:00",

        @SerializedName("EnableDays")
        public int mEnableDays;//":0,

        @SerializedName("OrderNo")
        public String mOrderNo;//":"KM2016082315303367045215",

        @SerializedName("Status")
        public int mStatus;//":0,

        @SerializedName("ForOrderType")
        public int mForOrderType;//":0}

    }

    public static class ServiceInfo {

        @SerializedName("DoctorID")
        public String mDoctorID;//	医生ID

        @SerializedName("DoctorName")
        public String mDoctorName;//	医生 姓名


        @SerializedName("ServiceContent")
        public String mServiceContent;//	服务内容

        @SerializedName("Details")
        public ArrayList<Detail> mDetails;



    }

    public static class Detail{
        @SerializedName("ServicePrice")
        public String mServicePrice;//	服务价格

        @SerializedName("StartDate")
        public String mStartDate;//	开始时间

        @SerializedName("EndDate")
        public String mEndDate;//	结束时间

        @SerializedName("Month")
        public String mMonth;//	月份
    }


}
