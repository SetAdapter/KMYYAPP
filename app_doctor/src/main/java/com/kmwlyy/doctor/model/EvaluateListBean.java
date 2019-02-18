package com.kmwlyy.doctor.model;

/**
 * Created by Administrator on 2016/12/23.
 */
public class EvaluateListBean {

    /**
     * ServiceEvaluationID : d5b0ae3ef6d04378a2de256d3cfd0e96
     * OuterID : 28bd4fda7f374f8fac9988d869aa4494
     * Score : 5
     * EvaluationTags : 态度很好;治疗效果好
     * Content : 测试一下评论借口
     * ProviderID : CDCEA38AD8C04063AA79DAC2ADF673E7
     * ServiceType : 1
     * UserID : 5280581AAC254730BAB6EA0AAB9F1368
     * CreateTime : 2017-01-03T14:37:27.34
     * UserName : 奥巴马
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
