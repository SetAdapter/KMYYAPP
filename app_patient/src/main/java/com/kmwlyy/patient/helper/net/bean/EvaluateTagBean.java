package com.kmwlyy.patient.helper.net.bean;

/**
 * Created by Administrator on 2016/8/11.
 */
public class EvaluateTagBean {

    /**
     * ServiceEvaluationTagID : 2
     * Score : 5
     * TagName : 很有医德
     */

    private String ServiceEvaluationTagID;
    private int Score;
    private String TagName;

    public String getServiceEvaluationTagID() {
        return ServiceEvaluationTagID;
    }

    public void setServiceEvaluationTagID(String ServiceEvaluationTagID) {
        this.ServiceEvaluationTagID = ServiceEvaluationTagID;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int Score) {
        this.Score = Score;
    }

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String TagName) {
        this.TagName = TagName;
    }
}
