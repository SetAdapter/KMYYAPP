package com.kmwlyy.doctor.model.httpEvent;

/**
 * Created by TFeng on 2017/7/27.
 */

public class OfflineBookingBean {

    /**
     * OppointmentID : xxxxxxx
     * OppointmentDate : 2017-07-17
     * BeginTime : 08:00
     * EndTime : 12:00
     * WorkingTime : {"WorkingTimeID":"xxxxxxx","WorkingTimeName":"上午","DayOfWeek":4}
     * UserMember : {"MemberName":"张三"}
     */

    private String OppointmentID;
    private String OppointmentDate;
    private String BeginTime;
    private String EndTime;
    private WorkingTimeBean WorkingTime;
    private UserMemberBean UserMember;

    public String getOppointmentID() {
        return OppointmentID;
    }

    public void setOppointmentID(String OppointmentID) {
        this.OppointmentID = OppointmentID;
    }

    public String getOppointmentDate() {
        return OppointmentDate;
    }

    public void setOppointmentDate(String OppointmentDate) {
        this.OppointmentDate = OppointmentDate;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String BeginTime) {
        this.BeginTime = BeginTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String EndTime) {
        this.EndTime = EndTime;
    }

    public WorkingTimeBean getWorkingTime() {
        return WorkingTime;
    }

    public void setWorkingTime(WorkingTimeBean WorkingTime) {
        this.WorkingTime = WorkingTime;
    }

    public UserMemberBean getUserMember() {
        return UserMember;
    }

    public void setUserMember(UserMemberBean UserMember) {
        this.UserMember = UserMember;
    }

    public static class WorkingTimeBean {
        /**
         * WorkingTimeID : xxxxxxx
         * WorkingTimeName : 上午
         * DayOfWeek : 4
         */

        private String WorkingTimeID;
        private String WorkingTimeName;
        private int DayOfWeek;

        public String getWorkingTimeID() {
            return WorkingTimeID;
        }

        public void setWorkingTimeID(String WorkingTimeID) {
            this.WorkingTimeID = WorkingTimeID;
        }

        public String getWorkingTimeName() {
            return WorkingTimeName;
        }

        public void setWorkingTimeName(String WorkingTimeName) {
            this.WorkingTimeName = WorkingTimeName;
        }

        public int getDayOfWeek() {
            return DayOfWeek;
        }

        public void setDayOfWeek(int DayOfWeek) {
            this.DayOfWeek = DayOfWeek;
        }

        @Override
        public String toString() {
            return "WorkingTimeBean{" +
                    "WorkingTimeID='" + WorkingTimeID + '\'' +
                    ", WorkingTimeName='" + WorkingTimeName + '\'' +
                    ", DayOfWeek=" + DayOfWeek +
                    '}';
        }
    }

    public static class UserMemberBean {
        /**
         * MemberName : 张三
         */

        private String MemberName;

        public String getMemberName() {
            return MemberName;
        }

        public void setMemberName(String MemberName) {
            this.MemberName = MemberName;
        }

        @Override
        public String toString() {
            return "UserMemberBean{" +
                    "MemberName='" + MemberName + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "OfflineBookingBean{" +
                "OppointmentID='" + OppointmentID + '\'' +
                ", OppointmentDate='" + OppointmentDate + '\'' +
                ", BeginTime='" + BeginTime + '\'' +
                ", EndTime='" + EndTime + '\'' +
                ", WorkingTime=" + WorkingTime +
                ", UserMember=" + UserMember +
                '}';
    }
}
