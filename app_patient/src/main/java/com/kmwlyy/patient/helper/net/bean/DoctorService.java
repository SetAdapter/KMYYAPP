package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Winson on 2016/8/13.
 */
public interface DoctorService {

    class ListItem{
        @SerializedName("ServiceID")
        public String mServiceID;

        @SerializedName("ServiceType")
        public String mServiceType;

        @SerializedName("ServiceTypeName")
        public String mServiceTypeName;

        @SerializedName("ServiceSwitch")
        public String mServiceSwitch;

        @SerializedName("ServicePrice")
        public String mServicePrice;
    }

    class Detail {
        @SerializedName("ServiceID")
        public String mServiceID;

        @SerializedName("ServiceType")
        public int mServiceType;

        @SerializedName("ServiceTypeName")
        public String mServiceTypeName;

        @SerializedName("ServiceSwitch")
        public int mServiceSwitch;

        @SerializedName("ServicePrice")
        public float mServicePrice;
    }

}
