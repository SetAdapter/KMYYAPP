package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Winson on 2016/8/13.
 */
public class DoctorSchedule {

    @SerializedName("DoctorId")
    public String mDoctorId;

    @SerializedName("DateWeekList")
    public ArrayList<DateWeek> mDateWeekList;

    @SerializedName("ScheduleList")
    public ArrayList<Schedule> mScheduleList;

    public static class DateWeek {

        @SerializedName("DateStr")
        public String mDateStr;

        @SerializedName("WeekStr")
        public String mWeekStr;

    }

    public static class Schedule {

        @SerializedName("StartTime")
        public String mStartTime;

        @SerializedName("EndTime")
        public String mEndTime;

        @SerializedName("RegNumList")
        public ArrayList<RegNum> mRegNumList;

    }

    public static class RegNum {

        @SerializedName("DoctorScheduleID")
        public String mDoctorScheduleID;

        @SerializedName("RegSum")
        public int mRegSum;

        @SerializedName("RegNum")
        public int mRegNum;

    }


}
