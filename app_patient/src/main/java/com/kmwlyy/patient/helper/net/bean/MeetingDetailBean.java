package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @Description描述: 远程会诊详情数据bean
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/24
 */
public class MeetingDetailBean {
    @SerializedName("DoctorNames")
    public String mDoctorNames;
    @SerializedName("ConsultationID")
    public String mConsultationID;
    @SerializedName("DoctorID")
    public String mDoctorID;
    @SerializedName("MemberID")
    public String mMemberID;
    @SerializedName("ConsultationSubject")
    public String mConsultationSubject;//会诊主题
    @SerializedName("ConsultationContent")
    public String mConsultationContent;//会诊内容
    @SerializedName("ConsultationRemind")
    public String mConsultationRemind;//提醒患者的内容
    @SerializedName("ConsultationResult")
    public String mConsultationResult;//会诊结论
    @SerializedName("ConsultationDate")
    public String mConsultationDate;
    @SerializedName("StartTime")
    public String mStartTime;
    @SerializedName("Amount")
    public float mAmount;

    @SerializedName("ConsultationStatus")
    public int mConsultationStatus;//(-1未提交、0-待支付、1-待开始、2-进行中、3-已完成)
    @SerializedName("CreateTime")
    public String mCreateTime;



    @SerializedName("DoctorIDList")
    public ArrayList<String> mDoctorIDList;

    @SerializedName("ConsultationInvites")
    public ArrayList<MeetingConsultBean.Invite> invites;

    @SerializedName("Order")
    public Order mOrder;

    @SerializedName("Member")
    public Member mMember;

    @SerializedName("Doctor")
    public MeetingConsultBean.Doctor mDoctor;

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


}
