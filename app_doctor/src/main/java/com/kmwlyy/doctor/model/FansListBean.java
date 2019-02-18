package com.kmwlyy.doctor.model;

/**
 * Created by Administrator on 2016/8/11.
 */
public class FansListBean {

    /**
     * UserID : 9A4C83966C784DD5BEFA68766591A272
     * UserCNName : 喔喔
     * UserENName : 20168
     * PhotoUrl : http://121.15.153.63:8027/images/55cee3461816e9838caf6c5edb3402ab
     * Mobile :
     * Gender : 1
     * Birthday : 2016-08-29
     * Age : 1
     */

    private String UserID;
    private String UserCNName;
    private String UserENName;
    private String PhotoUrl;
    private String Mobile;
    private int Gender;
    private String Birthday;
    private int Age;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getUserCNName() {
        return UserCNName;
    }

    public void setUserCNName(String UserCNName) {
        this.UserCNName = UserCNName;
    }

    public String getUserENName() {
        return UserENName;
    }

    public void setUserENName(String UserENName) {
        this.UserENName = UserENName;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String PhotoUrl) {
        this.PhotoUrl = PhotoUrl;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int Gender) {
        this.Gender = Gender;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String Birthday) {
        this.Birthday = Birthday;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int Age) {
        this.Age = Age;
    }
}