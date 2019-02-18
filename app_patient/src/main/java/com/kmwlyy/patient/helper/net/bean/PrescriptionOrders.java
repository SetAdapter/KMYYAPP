package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by xcj on 2016/8/20.
 */
public interface PrescriptionOrders {
    class ListItem {

        @SerializedName("RecipeOrderID")
        public String mRecipeOrderID;
        @SerializedName("Deletable")
        public boolean mDeletable;
        @SerializedName("Cancelable")
        public boolean mCancelable;


        @SerializedName("Order")
        public Orders mOrder;

        @SerializedName("Doctor")
        public Doctors mDoctor;

        @SerializedName("Member")
        public Member mMember;

        @SerializedName("RecipeFiles")
        public ArrayList<RecipeFile> mRecipeFiles;

    }
    public static class RecipeFile {

        @SerializedName("RecipeDate")
        public String mRecipeDate;//处方日期

        @SerializedName("RecipeName")
        public String mRecipeName;//处方名称

        @SerializedName("RecipeType")
        public String mRecipeType;//处方类型

        @SerializedName("TCMQuantity")
        public int mTCMQuantity;//剂数

        @SerializedName("Usage")
        public String mUsage;//用法

        @SerializedName("Amount")
        public String mAmount;//金额

    }

    public static class Orders {
        @SerializedName("OrderNo")
        public String mOrderNo;//订单号

        @SerializedName("TradeNo")
        public String mTradeNo;//支付编号

        @SerializedName("LogisticNo")
        public String mLogisticNo;//物流编号

        @SerializedName("PayType")
        public int mPayType;//支付类型 -1=免支付,0=康美支付,1=微信支付,2=支付宝,3=中国银联,4=MasterCard,5=PayPal,6=VISA

        @SerializedName("OrderType")
        public int mOrderType;//订单类型 0=挂号,1=图文咨询,2=电话咨询,3=视频咨询,4=处方支付，5=其他

        @SerializedName("OrderState")
        public int mOrderState;//订单状态 0=待支付,1=已支付,2=已完成,3=已取消

        @SerializedName("RefundState")
        public int mRefundState;//退款状态 1=申请退款,2=已退款,3=拒绝退款

        @SerializedName("LogisticState")
        public int mLogisticState;//物流状态 -1=待审核,0=已审核,1=已备货,2=已发货,3=配送中,4=已送达

        @SerializedName("OrderTime")
        public String mOrderTime;//订单时间

        @SerializedName("TotalFee")
        public double mTotalFee;//交易总金额

        @SerializedName("TradeTime")
        public double mTradeTime;//支付时间

    }

    public static class Doctors {
//        "DoctorID": "89F9E5907FD04DBF96A9867D1FA30396",
//                "DoctorName": "邱浩强",
//                "Gender": 0,
//                "Marriage": 0,
//                "IDType": 0,
//                "IsConsultation": false,
//                "IsExpert": false,
//                "HospitalID": "42FF1C61132E443F862510FF3BC3B03A",
//                "HospitalName": "康美医院",
//                "DepartmentID": "A8064D2DAE3542B18CBD64F467828F57",
//                "DepartmentName": "健康体检中心",
//                "CheckState": 0,
//                "Sort": 0

        @SerializedName("DoctorID")
        public String mDoctorID;

        @SerializedName("DoctorName")
        public String mDoctorName;

        @SerializedName("Gender")
        public int mGender;

        @SerializedName("Marriage")
        public int mMarriage;

        @SerializedName("IDType")
        public int mIDType;

        @SerializedName("IsConsultation")
        public boolean mIsConsultation;

        @SerializedName("IsExpert")
        public boolean mIsExpert;

        @SerializedName("HospitalName")
        public String mHospitalName;

        @SerializedName("DepartmentName")
        public String mDepartmentName;

        @SerializedName("CheckState")
        public int mCheckState;

        @SerializedName("Sort")
        public int mSort;

    }

    public static class Member {
//        "MemberID": "77C5CF07923A4E3D8121F628336527B8",
//                "MemberName": "郭明",
//                "Relation": 0,
//                "Gender": 0,
//                "Marriage": 0,
//                "IDType": 0,
//                "Age": 0,
//                "IsDefault": false


        @SerializedName("MemberName")
        public String mMemberName;

        @SerializedName("Age")
        public int mAge;

        @SerializedName("Mobile")
        public String mMobile;//手机号码

        @SerializedName("Gender")
        public int mGender;//性别

    }
}
