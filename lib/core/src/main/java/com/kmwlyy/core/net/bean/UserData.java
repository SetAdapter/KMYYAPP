package com.kmwlyy.core.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Winson on 2016/8/9.
 */
public class UserData {

    @SerializedName("Redirect")
    public String mRedirect;

    @SerializedName("Url")
    public String mUrl;

    @SerializedName("UserType")
    public int mUserType;

    @SerializedName("UserToken")
    public String mUserToken;

    @SerializedName("UserID")
    public String mUserId;

    @SerializedName("UserCNName")
    public String mUserCNName;

    @SerializedName("UserENName")
    public String mUserENName;

    @SerializedName("Mobile")
    public String mMobile;

    @SerializedName("CheckState")
    public int mCheckState;

    @SerializedName("BJCA_ClientID")
    public String BJCA_ClientID;

    @SerializedName("UserLevel")
    public int UserLevel;

    @SerializedName("PhotoUrl")
    public String PhotoUrl;

    @SerializedName("IDNumber")
    public String mIDNumber;

    @SerializedName("MemberID")
    public String MemberID;

}
