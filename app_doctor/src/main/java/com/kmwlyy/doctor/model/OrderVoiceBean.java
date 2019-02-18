package com.kmwlyy.doctor.model;

/**
 * Created by Administrator on 2016/8/25.
 */
public class OrderVoiceBean {

    /**
     * OPDRegisterID : a97d3894acd84d2fb649fa4094b34b23
     * RegDate : 2016-12-30T13:58:16.097
     * OPDType : 3
     * OPDDate : 2016-12-31T00:00:00
     * Price : 0.01
     * OrderState : 1
     * MemberID : a7d1fa735e1345babf73fdba8f0f7fc2
     * MemberName : 赵先生
     * ConsultContent :
     * Birthday : 1989-01-22
     * Gender : 0
     * GenderText : 男
     * ChannelID : 9477
     * Schedule : {"OPDate":"20161231","StartTime":"06:00","EndTime":"08:00","Disable":false,"Checked":false}
     * Age : 28
     */

    private String OPDRegisterID;
    private String RegDate;
    private int OPDType;
    private String OPDDate;
    private double Price;
    private int OrderState;
    private String MemberID;
    private String MemberName;
    private String ConsultContent;
    private String Birthday;
    private int Gender;
    private String GenderText;
    private int ChannelID;
    /**
     * OPDate : 20161231
     * StartTime : 06:00
     * EndTime : 08:00
     * Disable : false
     * Checked : false
     */

    private ScheduleBean Schedule;
    private int Age;

    public String getOPDRegisterID() {
        return OPDRegisterID;
    }

    public void setOPDRegisterID(String OPDRegisterID) {
        this.OPDRegisterID = OPDRegisterID;
    }

    public String getRegDate() {
        return RegDate;
    }

    public void setRegDate(String RegDate) {
        this.RegDate = RegDate;
    }

    public int getOPDType() {
        return OPDType;
    }

    public void setOPDType(int OPDType) {
        this.OPDType = OPDType;
    }

    public String getOPDDate() {
        return OPDDate;
    }

    public void setOPDDate(String OPDDate) {
        this.OPDDate = OPDDate;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double Price) {
        this.Price = Price;
    }

    public int getOrderState() {
        return OrderState;
    }

    public void setOrderState(int OrderState) {
        this.OrderState = OrderState;
    }

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String MemberID) {
        this.MemberID = MemberID;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String MemberName) {
        this.MemberName = MemberName;
    }

    public String getConsultContent() {
        return ConsultContent;
    }

    public void setConsultContent(String ConsultContent) {
        this.ConsultContent = ConsultContent;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String Birthday) {
        this.Birthday = Birthday;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int Gender) {
        this.Gender = Gender;
    }

    public String getGenderText() {
        return GenderText;
    }

    public void setGenderText(String GenderText) {
        this.GenderText = GenderText;
    }

    public int getChannelID() {
        return ChannelID;
    }

    public void setChannelID(int ChannelID) {
        this.ChannelID = ChannelID;
    }

    public ScheduleBean getSchedule() {
        return Schedule;
    }

    public void setSchedule(ScheduleBean Schedule) {
        this.Schedule = Schedule;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int Age) {
        this.Age = Age;
    }

    public static class ScheduleBean {
        private String OPDate;
        private String StartTime;
        private String EndTime;
        private boolean Disable;
        private boolean Checked;

        public String getOPDate() {
            return OPDate;
        }

        public void setOPDate(String OPDate) {
            this.OPDate = OPDate;
        }

        public String getStartTime() {
            return StartTime;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public boolean isDisable() {
            return Disable;
        }

        public void setDisable(boolean Disable) {
            this.Disable = Disable;
        }

        public boolean isChecked() {
            return Checked;
        }

        public void setChecked(boolean Checked) {
            this.Checked = Checked;
        }
    }
}
