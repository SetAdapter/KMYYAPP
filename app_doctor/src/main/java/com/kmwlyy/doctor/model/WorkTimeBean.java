package com.kmwlyy.doctor.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xcj on 2017/7/10.
 */

public class WorkTimeBean {
//    WorkingTimeBaseID":"88C56AA42B86427BAB0DC0846D1C275E","WorkingTimeName":"上午","BeginTime":"06:00","EndTime":"12:00
    @SerializedName("WorkingTimeBaseID")
    public String mWorkingTimeBaseID;
    @SerializedName("WorkingTimeName")
    public String mWorkingTimeName;
    @SerializedName("BeginTime")
    public String mBeginTime;
    @SerializedName("EndTime")
    public String mEndTime;
}
