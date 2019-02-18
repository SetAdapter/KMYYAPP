package com.kmwlyy.doctor.model;

/**
 * Created by Administrator on 2016/8/16.
 */
public class FreeCalendarBean {


    /**
     * ClinicMonth : 201701
     * ClinicDates :
     * AcceptCount : 10
     * CurrentCount : 0
     * State : false
     */

    private String ClinicMonth;
    private String ClinicDates;
    private int AcceptCount;
    private int CurrentCount;
    private boolean State;

    public String getClinicMonth() {
        return ClinicMonth;
    }

    public void setClinicMonth(String ClinicMonth) {
        this.ClinicMonth = ClinicMonth;
    }

    public String getClinicDates() {
        return ClinicDates;
    }

    public void setClinicDates(String ClinicDates) {
        this.ClinicDates = ClinicDates;
    }

    public int getAcceptCount() {
        return AcceptCount;
    }

    public void setAcceptCount(int AcceptCount) {
        this.AcceptCount = AcceptCount;
    }

    public int getCurrentCount() {
        return CurrentCount;
    }

    public void setCurrentCount(int CurrentCount) {
        this.CurrentCount = CurrentCount;
    }

    public boolean isState() {
        return State;
    }

    public void setState(boolean State) {
        this.State = State;
    }
}