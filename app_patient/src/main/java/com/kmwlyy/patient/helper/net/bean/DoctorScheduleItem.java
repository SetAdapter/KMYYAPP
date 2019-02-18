package com.kmwlyy.patient.helper.net.bean;

import java.io.Serializable;

/**
 * Created by xcj on 2016/12/19.
 */

public class DoctorScheduleItem implements Serializable{
    public static final int TYPE_NULL = 0;
    public static final int TYPE_WEEK = 1;
    public static final int TYPE_DURATION = 2;
    public static final int TYPE_ACTION = 3;

    public int mType;

    public String mDateStr;
    public String mWeekStr;

    public String mStartTime;
    public String mEndTime;

    public String mDoctorScheduleID;
    public int mRegSum;
    public int mRegNum;

    public DoctorScheduleItem(int mType, String mDateStr, String mWeekStr, String mStartTime, String mEndTime, String mDoctorScheduleID, int mRegSum, int mRegNum) {
        this.mType = mType;
        this.mDateStr = mDateStr;
        this.mWeekStr = mWeekStr;
        this.mStartTime = mStartTime;
        this.mEndTime = mEndTime;
        this.mDoctorScheduleID = mDoctorScheduleID;
        this.mRegSum = mRegSum;
        this.mRegNum = mRegNum;
    }
}
