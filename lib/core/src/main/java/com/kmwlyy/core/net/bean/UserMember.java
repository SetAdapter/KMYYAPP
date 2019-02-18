package com.kmwlyy.core.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Winson on 2016/8/17.
 */
public class UserMember implements Serializable {

//    {
//            "MemberID": "77C5CF07923A4E3D8121F628336527B8",
//            "UserID": "9A4C83966C784DD5BEFA68766591A272",
//            "MemberName": "郭明",
//            "Relation": 1,
//            "Gender": 1,
//            "Marriage": 1,
//            "Birthday": "19890202",
//            "Mobile": "18688941654",
//            "IDType": 1,
//            "IDNumber": "0",
//            "Address": "",
//            "Email": "18688941654@qq.com",
//            "PostCode": "0",
//            "Age": 0
//    }

    public static final int RELATION_SELF = 0;
    public static final int RELATION_MATE = 1;
    public static final int RELATION_FATHER = 2;
    public static final int RELATION_MOTHER = 3;
    public static final int RELATION_SON = 4;
    public static final int RELATION_DAUGHTER = 5;
    public static final int RELATION_OTHER = 6;

    public static final int MARRIAGE_DONE = 0;
    public static final int MARRIAGE_NONE = 1;
    public static final int MARRIAGE_UNKONW = 2;

    public static final int GENDER_MAN = 0;
    public static final int GENDER_WOMEN = 1;
    public static final int GENDER_UNKONW = 2;

    //        MemberID	string
    //        用户编号
    @SerializedName("MemberID")
    public String mMemberID;

    //        UserID	string
    //        用户编号
    @SerializedName("UserID")
    public String mUserId;

    //        MemberName
    @SerializedName("MemberName")
    public String mMemberName;

    //        Relation	string
    //        成员关系 （0-自己、1-配偶、2-父亲、3-母亲、4-儿子、5女儿、6-其他）
    @SerializedName("Relation")
    public int mRelation;

    //        Gender	string
    //        性别（0-男、1-女、2-未知）
    @SerializedName("Gender")
    public int mGender;

    //        Marriage	string
    //        婚姻状况(0-未婚、1-已婚、2-未知)
    @SerializedName("Marriage")
    public int mMarriage;

    //        Birthday	string
    //        生日
    @SerializedName("Birthday")
    public String mBirthday;

    //        Mobile	string
    //        手机号码
    @SerializedName("Mobile")
    public String mMobile;

    //        IDType	string
    //        证件类型（0-身份证）
    @SerializedName("IDType")
    public int mIDType;

    //        IDNumber	string
    //        证件号码
    @SerializedName("IDNumber")
    public String mIDNumber;

    //        Address 可选	string
    //                地址
    @SerializedName("Address")
    public String mAddress;

    //        Email 可选	string
    //                邮箱
    @SerializedName("Email")
    public String mEmail;

    //        PostCode 可选	string
    //                邮编
    @SerializedName("PostCode")
    public String mPostCode;

    //        Age 可选	string
    //                邮编
    @SerializedName("Age")
    public int mAge;

    // IsDefault 是否是默认
    @SerializedName("IsDefault")
    public boolean mIsDefault;

//    "Province" : "省"，
//            "ProvinceRegionID" : "省ID"，
//            "City" : "市"，
//            "CityRegionID" : "市ID"
//            "District" : "区"，
//            "DistrictRegionID" : "区ID"
//            "Town" : "村，街道"，
//            "TownRegionID" : "村，街道ID"
    @SerializedName("Province")
    public String mProvince;
    @SerializedName("ProvinceRegionID")
    public String ProvinceRegionID;
    @SerializedName("City")
    public String mCity;
    @SerializedName("CityRegionID")
    public String mCityRegionID;
    @SerializedName("District")
    public String mDistrict;
    @SerializedName("DistrictRegionID")
    public String mDistrictRegionID;
    @SerializedName("Town")
    public String mTown;
    @SerializedName("TownRegionID")
    public String mTownRegionID;

    public UserMember() {

    }

    public UserMember(String memberName, int mRelation, int mGender, int mMarriage, String mBirthday, String mMobile, int mIDType, String mIDNumber) {
        this.mMemberName = memberName;
        this.mRelation = mRelation;
        this.mGender = mGender;
        this.mMarriage = mMarriage;
        this.mBirthday = mBirthday;
        this.mMobile = mMobile;
        this.mIDType = mIDType;
        this.mIDNumber = mIDNumber;

    }

}
