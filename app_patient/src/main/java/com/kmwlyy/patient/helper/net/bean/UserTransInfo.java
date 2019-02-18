package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Winson on 2016/8/13.
 */
public interface UserTransInfo {

    class ListItem {

        @SerializedName("TransactionID")
        public String mTransactionID;

        @SerializedName("AccountID")
        public String mAccountID;

        @SerializedName("OrderID")
        public String mOrderID;

        @SerializedName("TransType")
        public int mTransType;

        @SerializedName("Amount")
        public float mAmount;

        @SerializedName("OrderNo")
        public String mOrderNo;

        @SerializedName("TradeTime")
        public String mTradeTime;
        @SerializedName("OrderType")
        public int mOrderType;

        @SerializedName("CreateTime")
        public String mCreateTime;
    }

    class Detail {

        @SerializedName("TransactionID")
        public String mTransactionID;

        @SerializedName("AccountID")
        public String mAccountID;

        @SerializedName("OrderID")
        public String mOrderID;

        @SerializedName("TransType")
        public int mTransType;

        @SerializedName("Amount")
        public float mAmount;

        @SerializedName("OrderNo")
        public String mOrderNo;
    }

}
