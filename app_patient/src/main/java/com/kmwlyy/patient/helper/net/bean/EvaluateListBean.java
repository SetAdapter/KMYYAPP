package com.kmwlyy.patient.helper.net.bean;

/**
 * Created by Administrator on 2016/12/23.
 */
public class EvaluateListBean {
    /**
     * ServiceEvaluationID : 270ca4bdf9844099999874e0dbb56502
     * OuterID : a574e81e06b3420ea7fa098ee5fd0b1f
     * Score : 5
     * EvaluationTags : 态度很好
     * Content : 国家局vv唱歌哈哈哈哈
     * ProviderID : CDCEA38AD8C04063AA79DAC2ADF673E7
     * ServiceType : 1
     * UserID : 23629D68451149B3B46B520D4F3F2388
     * CreateTime : 2017-01-06T16:23:46.51
     * UserName : 赵先生
     * UserPhotoUrl : http://121.15.153.63:8027/images/doctor/unknow.png
     */

    private String ServiceEvaluationID;
    private String OuterID;
    private int Score;
    private String EvaluationTags;
    private String Content;
    private String ProviderID;
    private int ServiceType;
    private String UserID;
    private String CreateTime;
    private String UserName;
    private String UserPhotoUrl;

    public String getServiceEvaluationID() {
        return ServiceEvaluationID;
    }

    public void setServiceEvaluationID(String ServiceEvaluationID) {
        this.ServiceEvaluationID = ServiceEvaluationID;
    }

    public String getOuterID() {
        return OuterID;
    }

    public void setOuterID(String OuterID) {
        this.OuterID = OuterID;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int Score) {
        this.Score = Score;
    }

    public String getEvaluationTags() {
        return EvaluationTags;
    }

    public void setEvaluationTags(String EvaluationTags) {
        this.EvaluationTags = EvaluationTags;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getProviderID() {
        return ProviderID;
    }

    public void setProviderID(String ProviderID) {
        this.ProviderID = ProviderID;
    }

    public int getServiceType() {
        return ServiceType;
    }

    public void setServiceType(int ServiceType) {
        this.ServiceType = ServiceType;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String UserID) {
        this.UserID = UserID;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserPhotoUrl() {
        return UserPhotoUrl;
    }

    public void setUserPhotoUrl(String UserPhotoUrl) {
        this.UserPhotoUrl = UserPhotoUrl;
    }
}
