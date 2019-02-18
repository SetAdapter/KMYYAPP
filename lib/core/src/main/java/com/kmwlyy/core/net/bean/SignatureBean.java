package com.kmwlyy.core.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xcj on 2017/8/23.
 */

public class SignatureBean {
//    "SignatureID":"签约编号",
//            "OrgnazitionID": "医生团队所属社区编号",
//            "OrgnazitionName": "医生团队所属机构/社区",
//            "FDGroupID":"医生团队编号",
//            "FDGroupName": "医生团队名称",
//            "Status": 0, //签约状态
//            "StatusName": "已签约" //签约状态
    @SerializedName("SignatureID")
    public String mSignatureID;
    @SerializedName("OrgnazitionID")
    public String mOrgnazitionID;
    @SerializedName("FDGroupID")
    public String mFDGroupID;
    @SerializedName("FDGroupName")
    public String mFDGroupName;
    @SerializedName("Status")
    public int mStatus;
    @SerializedName("StatusName")
    public String mStatusName;
    @SerializedName("OrgnazitionName")
    public String mOrgnazitionName;

}
