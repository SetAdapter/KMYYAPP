package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Winson on 2016/8/13.
 */
public interface UserAttentions {

    class ListItem {

        @SerializedName("DoctorAttentionID")
        public String mDoctorAttentionID;

        @SerializedName("DoctorID")
        public String mDoctorID;

        @SerializedName("DoctorName")
        public String mDoctorName;

        @SerializedName("HospitalID")
        public String mHospitalID;

        @SerializedName("HospitalName")
        public String mHospitalName;

        @SerializedName("DepartmentName")
        public String mDepartmentName;

        @SerializedName("DepartmentID")
        public String mDepartmentID;

        @SerializedName("Gender")
        public int mGender;

        @SerializedName("Portait")
        public String mPortait;

        @SerializedName("Position")
        public int mPosition;

        @SerializedName("IsExpert")
        public boolean mIsExpert;

        @SerializedName("Specialties")
        public String mSpecialties;

    }

}
