package com.kmwlyy.patient.module.InhabitantStart;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/11.
 */

public class MySignInformBean implements Serializable{
    public MySignInformBean() {
    }

    /**
     * VerificationCode : 验证码
     * SignatureID : 家庭医生签约ID,修改时用
     * FDGroupID : 医生团队ID（甲方）
     * OrgnazitionID : 医生团队所属机构ID（甲方）
     * SignatureUserName : 签名用户姓名(乙方)
     * SignatureUserIDNumber : 签名用户身份证(乙方)
     * SignatureURL : 签名图片URL
     * FamilyFN : 家庭健康档案号(乙方)
     * Mobile : 手机号(乙方)
     * Province : 省份(乙方)
     * City : 城市(乙方)
     * District : 市区(乙方)
     * Subdistrict : 乡镇或者街道(乙方)
     * Address : 地址(乙方)
     * Members : [{"MemberName":"成员姓名","IDNumber":"身份证号码","Relation":6}]
     */
    @SerializedName("VerificationCode")
    public String VerificationCode;
    @SerializedName("SignatureID")
    public String SignatureID;
    @SerializedName("FDGroupID")
    public String FDGroupID;
    @SerializedName("OrgnazitionID")
    public String OrgnazitionID;
    @SerializedName("SignatureUserName")
    public String SignatureUserName;
    @SerializedName("SignatureUserIDNumber")
    public String SignatureUserIDNumber;
    @SerializedName("SignatureURL")
    public String SignatureURL;
    @SerializedName("FamilyFN")
    public String FamilyFN;
    @SerializedName("Mobile")
    public String Mobile;
    @SerializedName("Province")
    public String Province;
    @SerializedName("City")
    public String City;
    @SerializedName("District")
    public String District;
    @SerializedName("Subdistrict")
    public String Subdistrict;
    @SerializedName("Address")
    public String Address;
    @SerializedName("Members")
    public List<MembersBean> Members;

    public void setVerificationCode(String VerificationCode) {
        this.VerificationCode = VerificationCode;
    }

    public void setSignatureID(String SignatureID) {
        this.SignatureID = SignatureID;
    }

    public void setFDGroupID(String FDGroupID) {
        this.FDGroupID = FDGroupID;
    }

    public void setOrgnazitionID(String OrgnazitionID) {
        this.OrgnazitionID = OrgnazitionID;
    }

    public void setSignatureUserName(String SignatureUserName) {
        this.SignatureUserName = SignatureUserName;
    }

    public void setSignatureUserIDNumber(String SignatureUserIDNumber) {
        this.SignatureUserIDNumber = SignatureUserIDNumber;
    }

    public void setSignatureURL(String SignatureURL) {
        this.SignatureURL = SignatureURL;
    }

    public void setFamilyFN(String FamilyFN) {
        this.FamilyFN = FamilyFN;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public void setProvince(String Province) {
        this.Province = Province;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public void setDistrict(String District) {
        this.District = District;
    }

    public void setSubdistrict(String Subdistrict) {
        this.Subdistrict = Subdistrict;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setMembers(List<MembersBean> Members) {
        this.Members = Members;
    }

    public String getVerificationCode() {
        return VerificationCode;
    }

    public String getSignatureID() {
        return SignatureID;
    }

    public String getFDGroupID() {
        return FDGroupID;
    }

    public String getOrgnazitionID() {
        return OrgnazitionID;
    }

    public String getSignatureUserName() {
        return SignatureUserName;
    }

    public String getSignatureUserIDNumber() {
        return SignatureUserIDNumber;
    }

    public String getSignatureURL() {
        return SignatureURL;
    }

    public String getFamilyFN() {
        return FamilyFN;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getProvince() {
        return Province;
    }

    public String getCity() {
        return City;
    }

    public String getDistrict() {
        return District;
    }

    public String getSubdistrict() {
        return Subdistrict;
    }

    public String getAddress() {
        return Address;
    }

    public List<MembersBean> getMembers() {
        return Members;
    }

    public static class MembersBean {
        /**
         * MemberName : 成员姓名
         * IDNumber : 身份证号码
         * Relation : 6
         */

        public String MemberName;
        public String IDNumber;
        public int Relation;

        public MembersBean(String memberName, String IDNumber, int relation) {
            MemberName = memberName;
            this.IDNumber = IDNumber;
            Relation = relation;
        }

        public void setMemberName(String MemberName) {
            this.MemberName = MemberName;
        }

        public void setIDNumber(String IDNumber) {
            this.IDNumber = IDNumber;
        }

        public void setRelation(int Relation) {
            this.Relation = Relation;
        }

        public String getMemberName() {
            return MemberName;
        }

        public String getIDNumber() {
            return IDNumber;
        }

        public int getRelation() {
            return Relation;
        }
    }

    public MySignInformBean(String verificationCode, String signatureID, String FDGroupID, String orgnazitionID, String signatureUserName, String signatureUserIDNumber, String signatureURL, String familyFN, String mobile, String province, String city, String district, String subdistrict, String address, List<MembersBean> members) {
        VerificationCode = verificationCode;
        SignatureID = signatureID;
        this.FDGroupID = FDGroupID;
        OrgnazitionID = orgnazitionID;
        SignatureUserName = signatureUserName;
        SignatureUserIDNumber = signatureUserIDNumber;
        SignatureURL = signatureURL;
        FamilyFN = familyFN;
        Mobile = mobile;
        Province = province;
        City = city;
        District = district;
        Subdistrict = subdistrict;
        Address = address;
        Members = members;
    }
}
