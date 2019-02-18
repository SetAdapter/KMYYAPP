package com.kmwlyy.core.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Winson on 2016/8/13.
 */
public class UserAccount {

    @SerializedName("AccountID")
    public String mAccountID;

    @SerializedName("Currency")
    public int mCurrency;

    @SerializedName("Balance")
    public int mBalance;

    @SerializedName("Available")
    public int mAvailable;

    @SerializedName("Freeze")
    public int mFreeze;

    @SerializedName("Status")
    public int mStatus;

}
