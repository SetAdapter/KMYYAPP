package com.kmwlyy.doctor.model;

import android.graphics.drawable.Drawable;

/**
 * Created by TFeng on 2017/6/30.
 */

public class SingedPeopleBean {
    String name;
    int age;
    String gender;
    String iconUrl;
//    是否回复
    boolean isReplay;
//    咨询问题
    String question;

//    咨询时间
    String time;

    public SingedPeopleBean() {

    }

    public SingedPeopleBean(String name, int age, String gender, String iconUrl, boolean isReplay, String question, String time) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.iconUrl = iconUrl;
        this.isReplay = isReplay;
        this.question = question;
        this.time = time;
    }

    public SingedPeopleBean(String name, int age, String gender, String iconUrl) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isReplay() {
        return isReplay;
    }

    public void setReplay(boolean replay) {
        isReplay = replay;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
