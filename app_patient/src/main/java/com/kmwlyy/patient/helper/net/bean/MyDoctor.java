package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Description描述: 我的医生列表数据实体
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/14
 */
public class MyDoctor implements Serializable {
    @SerializedName("OPDRegisterID")
    public String opdRegisterId;
    @SerializedName("DoctorID")
    public String doctorId;
    @SerializedName("DoctorName")
    public String doctorName;
    @SerializedName("HospitalID")
    public String hospitalId;
    @SerializedName("HospitalName")
    public String hospitalName;
    @SerializedName("DepartmentName")
    public String departmentName;
    @SerializedName("DepartmentID")
    public String departmentId;
    @SerializedName("Gender")
    public String gender;
    @SerializedName("Portait")
    public String portrait;
    @SerializedName("Title")
    public String title = "";
    @SerializedName("IsExpert")
    public boolean isExpert;
    @SerializedName("IsFollowed")
    public boolean isFollowed;
    @SerializedName("Specialties")
    public String specialties;

}
