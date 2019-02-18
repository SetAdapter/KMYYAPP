package com.kmwlyy.patient.module.signcomfirm;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xcj on 2017/8/24.
 */

public class SignDetailBean {
//    "OrgnazitionID": "医生团队所属社区编号",
//            "OrgnazitionName": "医生团队所属机构/社区",
//            "FDGroupID":"医生团队编号"
//            "FDGroupName": "医生团队名称",
//            "SignatureID":"签约编号"
//            "SignatureUserName": "家庭代表/签名用户姓名",
//            "FamilyFN": "家庭健康档案号",
//            "FamilyMobile": "手机号码",
//            "FamilyProvince":"省",
//            "FamilyCity":"市",
//            "FamilyDistrict":"区",
//            "FamilySubdistrict":"乡镇街道",
//            "FamilyAddress":"详细地址"
//            "FamilyMemberCount": 4,//成员数量
//            "Status": 0, //签约状态
//            "StatusName": "已签约", //签约状态
//            "CreateTime" :"签约时间",
    @SerializedName("OrgnazitionID")
    public String OrgnazitionID;

    @SerializedName("OrgnazitionName")
    public String OrgnazitionName;

    @SerializedName("FDGroupID")
    public String FDGroupID;

    @SerializedName("FDGroupName")
    public String FDGroupName;

    @SerializedName("SignatureID")
    public String SignatureID;

    @SerializedName("SignatureUserName")
    public String SignatureUserName;

    @SerializedName("FamilyFN")
    public String FamilyFN;

    @SerializedName("FamilyMobile")
    public String FamilyMobile;

    @SerializedName("FamilyProvince")
    public String FamilyProvince;

    @SerializedName("FamilyCity")
    public String FamilyCity;

    @SerializedName("FamilyDistrict")
    public String FamilyDistrict;

    @SerializedName("FamilySubdistrict")
    public String FamilySubdistrict;

    @SerializedName("FamilyAddress")
    public String FamilyAddress;

    @SerializedName("FamilyMemberCount")
    public String FamilyMemberCount;
    @SerializedName("Status")
    public String Status;

    @SerializedName("CreateTime")
    public String CreateTime;
}
