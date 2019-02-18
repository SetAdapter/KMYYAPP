package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Winson on 2016/8/13.
 */
public class UserOPDRegisters {


    @SerializedName("OPDRegisterID")
    public String mOPDRegisterID;

    @SerializedName("UserID")
    public String mUserID;

    @SerializedName("RegDate")
    public String mRegDate;

    @SerializedName("OPDDate")
    public String mOPDDate;

    @SerializedName("VisitDate")
    public String mVisitDate;

    @SerializedName("OPDType")
    public int mOPDType;

    @SerializedName("BeginTime")
    public String mBeginTime;

    @SerializedName("EndTime")
    public String mEndTime;

    @SerializedName("TotalTime")
    public long mTotalTime;

    @SerializedName("State")
    public int mState;

    @SerializedName("ChannelId")
    public String mChannelId;

    @SerializedName("RandomCode")
    public String mRandomCode;

    @SerializedName("Fee")
    public float mFee;

    @SerializedName("ConsultContent")
    public String mConsultContent;

    @SerializedName("Deletable")
    public boolean mDeletable;//是否可删除
    @SerializedName("Cancelable")
    public boolean mCancelable;//是否可取消


    @SerializedName("RecipeFileUrl")
    public String mRecipeFileUrl;

    @SerializedName("Order")
    public Order mOrder;

    @SerializedName("Member")
    public Member mMember;

    @SerializedName("Doctor")
    public Doctor.ListItem mDoctor;

    @SerializedName("Schedule")
    public Schedule mSchedule;

    @SerializedName("Room")
    public Room mRoom;

    @SerializedName("RecipeFiles")
    public ArrayList<ConsultRecipeBean> mRecipeFiles = new ArrayList<>();



    public static class Room {

        @SerializedName("ServiceType")
        public int mServiceType;//":0,

        @SerializedName("ChannelID")
        public String mChannelID;//":270,

        @SerializedName("RoomState")
        public int mRoomState;//":-1,

        @SerializedName("BeginTime")
        public String mBeginTime;//":"0001-01-01T00:00:00",

        @SerializedName("EndTime")
        public String mEndTime;//":"0001-01-01T00:00:00",

        @SerializedName("TotalTime")
        public long mTotalTime;//":0

    }

    public static class Order {

        @SerializedName("OrderNo")
        public String mOrderNo;

        @SerializedName("TradeNo")
        public String mTradeNo;

        @SerializedName("LogisticNo")
        public String mLogisticNo;

        @SerializedName("PayType")
        public int mPayType;

        @SerializedName("OrderType")
        public int mOrderType;

        @SerializedName("OrderState")
        public int mOrderState;

        @SerializedName("RefundState")
        public int mRefundState;

        @SerializedName("LogisticState")
        public int mLogisticState;

        @SerializedName("OrderTime")
        public String mOrderTime;

        @SerializedName("TotalFee")
        public float mTotalFee;


        @SerializedName("IsEvaluated")
        public boolean mIsEvaluated;

    }


    public static class Member {

        @SerializedName("MemberID")
        public String mMemberID;

        @SerializedName("UserID")
        public String mUserID;

        @SerializedName("MemberName")
        public String mMemberName;

        @SerializedName("Relation")
        public int mRelation;

        @SerializedName("Gender")
        public int mGender;

        @SerializedName("Marriage")
        public int mMarriage;

        @SerializedName("IDType")
        public int mIDType;

        @SerializedName("Age")
        public int mAge;

    }

//    class Doctor {
//
//        public String DoctorID;
//        public String DoctorName;
//        public int Gender;
//        public int Marriage;
//        public int IDType;
//        public boolean IsConsultation;
//        public boolean IsExpert;
//        public String HospitalID;
//        public String HospitalName;
//        public String DepartmentID;
//        public String DepartmentName;
//        public int CheckState;
//        public int Sort;
//
//    }


    public static class Schedule {

        @SerializedName("ScheduleID")
        public String mScheduleID;

        @SerializedName("StartTime")
        public String mStartTime;

        @SerializedName("EndTime")
        public String mEndTime;

        @SerializedName("Disable")
        public boolean mDisable;

        @SerializedName("Checked")
        public boolean mChecked;

    }

    public static class AddResp {
//        OPDRegisterID:"预约编号",
//        OrderNO:"订单编号",
//        ErrorInfo:"错误消息",
//        ActionStatus:"状态" //Repeat/Success/Fail/UnSupport

        @SerializedName("OPDRegisterID")
        public String mOPDRegisterID;

        @SerializedName("OrderNO")
        public String mOrderNO;

        @SerializedName("ErrorInfo")
        public String mErrorInfo;

        @SerializedName("ActionStatus")
        public String mActionStatus;

        @SerializedName("OrderState")
        public int mOrderState;//订单状态

    }


}
