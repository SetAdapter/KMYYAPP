package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xcj on 2016/12/17.
 */

public class UserTrans {
    /*"TransactionID": "",
            "AccountID": "",
            "OrderID": "",
            "TransType": 1,
            "Amount": "",
            "OrderNo": ""*/
    @SerializedName("TransactionID")
    public String mTransactionID;
    @SerializedName("AccountID")
    public String mAccountID;
    @SerializedName("OrderID")
    public String mOrderID;
    @SerializedName("TransType")
    public int mTransType;
    @SerializedName("Amount")
    public String mAmount;
    @SerializedName("OrderNo")
    public String mOrderNo;

}
