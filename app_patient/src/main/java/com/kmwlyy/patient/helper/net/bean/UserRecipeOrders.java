package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Winson on 2016/8/13.
 */
public class UserRecipeOrders {

    @SerializedName("RecipeOrderID")
    public String mRecipeOrderID;

    @SerializedName("OPDRegisterID")
    public String mOPDRegisterID;

    @SerializedName("RecipeFileID")
    public String mRecipeFileID;

    @SerializedName("Fee")
    public float mFee;

    @SerializedName("OrderTime")
    public String mOrderTime;

    @SerializedName("Order")
    public Order mOrder;

    @SerializedName("RecipeFiles")
    public ArrayList<RecipeFile> mRecipeFiles;

//    public static class Order {
//
//        @SerializedName("OrderNo")
//        public String mOrderNo;
//
//        @SerializedName("TradeNo")
//        public String mTradeNo;
//
//        @SerializedName("OrderOutID")
//        public String mOrderOutID;
//
//        @SerializedName("LogisticNo")
//        public String mLogisticNo;
//
//
//        //支付类型 -1=免支付,0=康美支付,1=微信支付,2=支付宝,3=中国银联,4=MasterCard,5=PayPal,6=VISA
//        @SerializedName("PayType")
//        public String mPayType;
//
//        //订单类型 0=挂号,1=图文咨询,2=电话咨询,3=视频咨询,4=处方支付，5=其他
//        @SerializedName("OrderType")
//        public String mOrderType;
//
//        //订单状态 0=待支付,1=已支付,2=已完成,3=已取消,7=7
//        @SerializedName("OrderState")
//        public String mOrderState;
//
//        //退款状态 1=申请退款,2=已退款,3=拒绝退款
//        @SerializedName("RefundState")
//        public String mRefundState;
//
//        //物流状态 -1=待审核,0=已审核,1=已备货,2=已发货,3=配送中,4=已送达
//        @SerializedName("LogisticState")
//        public String mLogisticState;
//
//        @SerializedName("OrderTime")
//        public String mOrderTime;
//
//        @SerializedName("TradeTime")
//        public String mTradeTime;
//
//        @SerializedName("CancelTime")
//        public String mCancelTime;
//
//        @SerializedName("CancelReason")
//        public String mCancelReason;
//
//        @SerializedName("FinishTime")
//        public String mFinishTime;
//
//        @SerializedName("StoreTime")
//        public String mStoreTime;
//
//        @SerializedName("ExpressTime")
//        public String mExpressTime;
//
//        @SerializedName("RefundTime")
//        public String mRefundTime;
//
//        @SerializedName("RefundFee")
//        public float mRefundFee;
//
//        @SerializedName("TotalFee")
//        public float mTotalFee;
//
//        @SerializedName("Details")
//        public ArrayList<Detail> mDetails;
//
//        @SerializedName("Consignee")
//        public Consignee mConsignee;
//
//    }
//
//    public static class Detail {
//
//        @SerializedName("Subject")
//        public String mSubject;
//
//        @SerializedName("Body")
//        public String mBody;
//
//        @SerializedName("UnitPrice")
//        public float mUnitPrice;//单价
//
//        @SerializedName("Quantity")
//        public int mQuantity;//数量
//
//        @SerializedName("Fee")
//        public float mFee;//费用
//
//        @SerializedName("Discount")
//        public float mDiscount;//折扣
//
//        @SerializedName("ProductId")
//        public String mProductId;
//
//        @SerializedName("ProductType")
//        public int mProductType;//产品类型
//
//    }
//
//    public static class Consignee {
//
//        @SerializedName("Id")
//        public String mId;//收货人编号
//
//        @SerializedName("Address")
//        public String mAddress;//收货地址
//
//        @SerializedName("Name")
//        public String mName;//收货人姓名
//
//        @SerializedName("Tel")
//        public String mTel;//收货人电话
//
//    }

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

}
