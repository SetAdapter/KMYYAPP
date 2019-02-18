package com.kmwlyy.core.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by xcj on 2016/10/23.
 */
public class PatientInfo implements Serializable {
 /*   "UserID": "5eb975d41a65491ea68e75c3d48ffac0",
            "Mobile": "18118775915",
            "UserCNName": null,
            "UserENName": null,
            "UserType": 1,
            "PhotoUrl": "",
            "IDNumber": ""*/

    @SerializedName("UserID")
    public String mUserID;
    @SerializedName("Mobile")
    public String mMobile;
    @SerializedName("UserCNName")
    public String mUserCNName;
    @SerializedName("UserENName")
    public String mUserENName;
    @SerializedName("PhotoUrl")
    public String mPhotoUrl;
    @SerializedName("IDNumber")
    public String mIDNumber;
    @SerializedName("UrlPrefix")
    public String mUrlPrefix;

}
