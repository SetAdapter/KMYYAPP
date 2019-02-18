package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by xcj on 2016/12/20.
 */

public class UserPackage {
        @SerializedName("ConsumeID")
        public String mConsumeID;
        @SerializedName("UserPackageID")
        public String mUserPackageID;
        @SerializedName("OrderNo")
        public String mOrderNo;
        @SerializedName("Amount")
        public String mAmount;
        @SerializedName("Period")
        public String mPeriod;
        @SerializedName("PeriodUnit")
        public String mPeriodUnit;
        @SerializedName("Status")
        public int mStatus;
        @SerializedName("UserPackageName")
        public String mUserPackageName;
        @SerializedName("StatusText")
        public String mStatusText;
        @SerializedName("Details")
        public ArrayList<Detail> mDetails;
        @SerializedName("PeriodUnitName")
        public String mPeriodUnitName;
        @SerializedName("StartTime")
        public String mStartTime;
        @SerializedName("EndTime")
        public String mEndTime;

    public static class Detail {
        @SerializedName("ConsumeDetailID")
        public String mConsumeDetailID;
        @SerializedName("ServiceGoodsCount")
        public int mServiceGoodsCount;
        @SerializedName("ConsumeCount")
        public int mConsumeCount;
        @SerializedName("ServiceGoodsType")
        public int mServiceGoodsType;
        @SerializedName("ServiceGoodsName")
        public String mServiceGoodsName;
    }
}
