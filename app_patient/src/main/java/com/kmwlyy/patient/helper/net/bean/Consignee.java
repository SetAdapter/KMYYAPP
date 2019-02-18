package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Winson on 2016/8/13.
 */
public class Consignee {

    @SerializedName("Id")
    public String mId;//收货人编号

    @SerializedName("Address")
    public String mAddress;//收货地址

    @SerializedName("Name")
    public String mName;//收货人姓名

    @SerializedName("Tel")
    public String mTel;//收货人电话

}
