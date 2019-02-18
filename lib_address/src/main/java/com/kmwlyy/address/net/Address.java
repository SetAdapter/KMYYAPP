package com.kmwlyy.address.net;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-8-18.
 */
public class Address implements Serializable {


    public String getPostcode() {
        return Postcode;
    }

    public void setPostcode(String postcode) {
        Postcode = postcode;
    }

    /**
     * AddressID :
     * UserName :
     * Mobile :
     * ProvinceName :
     * CityName :
     * AreaName :
     * IsDefault : false
     * DetailAddress :
     */

    private String Postcode="518000";
    private String AddressID;
    private String UserName;
    private String Mobile;
    private String ProvinceName = "广东省";
    private String CityName = "深圳市";
    private String AreaName = "福田区";
    private boolean IsDefault;
    private String DetailAddress;


    public String getAddressID() {
        return AddressID;
    }

    public void setAddressID(String AddressID) {
        this.AddressID = AddressID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String ProvinceName) {
        this.ProvinceName = ProvinceName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String CityName) {
        this.CityName = CityName;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String AreaName) {
        this.AreaName = AreaName;
    }

    public boolean isIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(boolean IsDefault) {
        this.IsDefault = IsDefault;
    }

    public String getDetailAddress() {
        return DetailAddress;
    }

    public void setDetailAddress(String DetailAddress) {
        this.DetailAddress = DetailAddress;
    }
}
