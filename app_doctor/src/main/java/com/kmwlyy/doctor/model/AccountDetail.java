package com.kmwlyy.doctor.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xcj on 2016/12/17.
 */

public class AccountDetail {
    /*"AccountID": "",
            "Currency": 1,
            "Balance": 0,
            "Available": 0,
            "Freeze": 0,
            "Status": 1*/
    @SerializedName("AccountID")
    public String mAccountID;
    @SerializedName("Currency")
    public double mCurrency;
    @SerializedName("Balance")
    public double mBalance;
    @SerializedName("Available")
    public double mAvailable;
    @SerializedName("Freeze")
    public double mFreeze;
    @SerializedName("Status")
    public int mStatus;
    @SerializedName("HavePayPassword")
    public boolean mHavePayPassword;

}
