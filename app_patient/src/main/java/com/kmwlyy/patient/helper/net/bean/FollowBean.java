package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xcj on 2017/8/24.
 */

public class FollowBean {
//    {\"sessionID\":\"110103199901010996\",\"serviceType\":\"followup\"}"
@SerializedName("sessionID")
public String sessionID;

    @SerializedName("serviceType")
    public String serviceType;
}
