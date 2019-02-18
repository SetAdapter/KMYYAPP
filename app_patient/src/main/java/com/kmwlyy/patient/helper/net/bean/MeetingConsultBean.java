package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Description描述: 远程会诊列表数据bean
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/22
 */
public class MeetingConsultBean {
    @SerializedName("ConsultationStatusName")
    public String mConsultationStatusName;
    @SerializedName("ConsultationID")
    public String mConsultationID;
    @SerializedName("DoctorID")
    public String mDoctorID;
    @SerializedName("MemberID")
    public String mMemberID;
    @SerializedName("ConsultationSubject")
    public String mSubject;
    @SerializedName("ConsultationDate")
    public String mConsultationDate;
    @SerializedName("Amount")
    public float mAmount;
    @SerializedName("ConsultationStatus")
    public int mConsultationStatus;//(-1未提交、0-待支付、1-待开始、2-进行中、3-已完成)
    @SerializedName("StartTime")
    public String mStartTime;
    @SerializedName("RecipeFileUrl")
    public String mRecipeFileUrl;//处方链接

    @SerializedName("RecipeFiles")
    public ArrayList<ConsultRecipeBean> mRecipeFiles = new ArrayList<>();


    @SerializedName("ConsultationInvites")
    public ArrayList<Invite> invites;

    @SerializedName("Order")
    public Order mOrder;

    @SerializedName("Member")
    public Member mMember;

    @SerializedName("Doctor")
    public Doctor mDoctor;

    @SerializedName("Room")
    public Room mRoom;


    public static class Room {

        @SerializedName("ConversationRoomID")
        public String mRoomID;

        @SerializedName("ServiceID")
        public String mServiceID;

        @SerializedName("ServiceType")
        public int mServiceType;

        @SerializedName("ChannelID")
        public String mChannelID;

        @SerializedName("RoomState")
        public int mRoomState;

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

        @SerializedName("IsDefault")
        public boolean mIsDefault;

    }

    public static class Invite {
        @SerializedName("CreateTime")
        public String mCreateTime;
        @SerializedName("IsConsult")
        public boolean mIsConsult;
        @SerializedName("ConsultationID")
        public String mConsultationID;
        @SerializedName("Amount")
        public float mAmount;
        @SerializedName("Doctor")
        public Doctor mDoctor;
    }

    public static class Doctor {
        @SerializedName("User")
        public User mUser;

        @SerializedName("DoctorID")
        public String mDoctorID;

        @SerializedName("DoctorName")
        public String mDoctorName;
    }

    public static class User {
        @SerializedName("identifier")
        public int agoraUid;
    }


}
