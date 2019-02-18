package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xcj on 2016/12/28.
 */

public class UsedBankBean {
   /* "BankCardID": "",
            "AccountID": "",
            "Bank": "",
            "BankBarnch": "",
            "AccountName": "",
            "CardCode": "",
            "Status": 1*/
   @SerializedName("BankCardID")
   public String mBankCardID;
    @SerializedName("AccountID")
    public String mAccountID;
    @SerializedName("Bank")
    public String mBank;
    @SerializedName("BankBarnch")
    public String mBankBarnch;
    @SerializedName("AccountName")
    public String mAccountName;
    @SerializedName("CardCode")
    public String mCardCode;

}
