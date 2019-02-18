package com.kmwlyy.doctor.model;

import android.support.annotation.NonNull;

import com.kmwlyy.doctor.model.httpEvent.OfflineBookingBean;

import java.util.List;

/**
 * Created by TFeng on 2017/7/28.
 */

public class OfflineDayTimeBean implements Comparable<OfflineDayTimeBean>{
    private String dayTime;
    private List<OfflineBookingBean> list;
    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public List<OfflineBookingBean> getList() {
        return list;
    }

    public void setList(List<OfflineBookingBean> list) {
        this.list = list;
    }

    @Override
    public int compareTo(@NonNull OfflineDayTimeBean offlineDayTimeBean) {
        int i = this.getID()-offlineDayTimeBean.getID();
        return i;
    }
}
