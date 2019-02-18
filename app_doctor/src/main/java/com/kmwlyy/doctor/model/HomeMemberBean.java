package com.kmwlyy.doctor.model;

/**
 * Created by TFeng on 2017/7/2.
 */

public class HomeMemberBean {
    String name;
    int age;
    String gender;
    String remark;
    String serviceCount;
    String phone;
    String address;
    String iconUrl;
    String relationship;

    public HomeMemberBean(String name, int age, String gender, String remark, String serviceCount, String phone, String address, String iconUrl, String relationship) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.remark = remark;
        this.serviceCount = serviceCount;
        this.phone = phone;
        this.address = address;
        this.iconUrl = iconUrl;
        this.relationship = relationship;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getServiceCount() {
        return serviceCount;
    }

    public void setServiceCount(String serviceCount) {
        this.serviceCount = serviceCount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }
}
