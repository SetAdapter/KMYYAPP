package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Winson on 2016/8/13.
 */
public class Department {

    @SerializedName("DepartmentID")
    public String mDepartmentID;

    @SerializedName("HospitalID")
    public String mHospitalID;

    @SerializedName("DepartmentName")
    public String mDepartmentName;

    @SerializedName("Intro")
    public String mIntro;

    @SerializedName("Doctors")
    public ArrayList<Doctor> mDoctors;

    public static class Doctor {

    }

}
