package com.kmwlyy.doctor.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Winson on 2017/7/5.
 */

public class PatientGroup implements Serializable{

    @SerializedName("MemberGroupID")
    public String memberGroupID;//":"xxxxxxxxxxxxxxxx",  //分组ID

    @SerializedName("GroupName")
    public String groupName;//":"分组名称"  //分组名称

}
