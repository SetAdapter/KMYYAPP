package com.kmwlyy.doctor.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Winson on 2017/7/5.
 */

public class PatientList implements Serializable{

    @SerializedName("DoctorMemberID")
    public String doctorMemberID;//:"xxxxxxx",

    @SerializedName("DoctorID")
    public String doctorID;//:"xxxxxxx",

    @SerializedName("MemberID")
    public String memberID;//:"xxxxxxx",

    @SerializedName("MemberName")
    public String memberName;//:"张三",

    @SerializedName("Birthday")
    public String birthday;//:"2010-01-01",

    @SerializedName("Gender")
    public int gender;//:1, //0男，1女

    @SerializedName("Mobile")
    public String mobile;//:"13632152124",

    @SerializedName("CreateTime")
    public String createTime;//:"2017-01-01",

    @SerializedName("Age")
    public int age;//:20

}
