package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Winson on 2016/8/13.
 */
public class Order {

    public final static int WAIT_PAY = 0;
    public final static int PAYED = 1;
    public final static int USED = 2;
    public final static int CANCELED = 3;
    public final static int APPLY_REFUNDED = 4;
    public final static int REFUSE_REFUNDED = 5;
    public final static int REFUNDED = 6;

    @SerializedName("OrderNo")
    public String mOrderNo;//订单编号

    @SerializedName("TradeNo")
    public String mTradeNo;//交易（支付）编号

    @SerializedName("OrderOutID")
    public String mOrderOutID;//外部订单编号

    @SerializedName("LogisticNo")
    public String mLogisticNo;//物流编号

    @SerializedName("PayType")
    public int mPayType;//支付类型 -1=免支付,0=康美支付,1=微信支付,2=支付宝,3=中国银联,4=MasterCard,5=PayPal,6=VISA

    @SerializedName("OrderType")
    public int mOrderType;//订单类型 0=挂号,1=图文咨询,2=电话咨询,3=视频咨询,4=处方支付，5=其他

    @SerializedName("OrderState")
    public int mOrderState;//订单状态 0=待支付,1=已支付,2=已完成,3=已取消,7=7
    //订单状态（state：0-待支付、1-已支付、2-已使用、3-已取消、4-申请退款、5-拒绝退款、6-已退款）

    @SerializedName("RefundState")
    public int mRefundState;//退款状态 1=申请退款,2=已退款,3=拒绝退款

    @SerializedName("LogisticState")
    public int mLogisticState;//物流状态 -1=待审核,0=已审核,1=已备货,2=已发货,3=配送中,4=已送达

    @SerializedName("OrderTime")
    public String mOrderTime;//订单时间

    @SerializedName("TradeTime")
    public String mTradeTime;//交易（支付）时间

    @SerializedName("CancelTime")
    public String mCancelTime;//取消订单时间

    @SerializedName("CancelReason")
    public String mCancelReason;//取消原因

    @SerializedName("FinishTime")
    public String mFinishTime;//订单完成时间

    @SerializedName("StoreTime")
    public String mStoreTime;//仓库出库时间

    @SerializedName("ExpressTime")
    public String mExpressTime;//物流发货时间

    @SerializedName("RefundTime")
    public String mRefundTime;//退款时间

    @SerializedName("RefundFee")
    public float mRefundFee;//退款金额

    @SerializedName("TotalFee")
    public float mTotalFee;//交易总金额

    @SerializedName("IsEvaluated")
    public boolean mIsEvaluated;//是否已评价

    @SerializedName("Details")
    public ArrayList<Detail> mDetails;

    @SerializedName("Consignee")
    public Consignee mConsignee;

    public static class Detail {

        @SerializedName("Subject")
        public String mSubject;

        @SerializedName("Body")
        public String mBody;

        @SerializedName("UnitPrice")
        public float mUnitPrice;//单价

        @SerializedName("Quantity")
        public int mQuantity;//数量

        @SerializedName("Fee")
        public float mFee;//费用

        @SerializedName("Discount")
        public float mDiscount;//折扣

        @SerializedName("ProductId")
        public String mProductId;

        @SerializedName("ProductType")
        public int mProductType;//产品类型

    }


}
