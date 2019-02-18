package com.kmwlyy.doctor.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/8/16.
 */
public class Schedule {
    public String ScheduleID;
    public String OPDate;
    public String StartTime;
    public String EndTime;
    public String DoctorID;
    public String Disable;
    public String Checked;
    public Appoint AppointmentCounts;

    public static class Appoint {
        @SerializedName("1")
        public int voice;
        @SerializedName("2")
        public int video;
        @SerializedName("3")
        public int consult;

        public int getCount() {
            return voice + video + consult;
        }
    }
}
