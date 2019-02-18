package com.kmwlyy.doctor.model;

/**
 * Created by Administrator on 2016/8/11.
 */
public class EvaluateTagBean {

    /**
     * ServiceEvaluationTagID : 2
     * Score : 5
     * EvaluatedCount : 很有医德
     */

    private String ServiceEvaluationTagID;
    private int EvaluatedCount;
    private String TagName;

    public String getServiceEvaluationTagID() {
        return ServiceEvaluationTagID;
    }

    public void setServiceEvaluationTagID(String serviceEvaluationTagID) {
        ServiceEvaluationTagID = serviceEvaluationTagID;
    }

    public int getEvaluatedCount() {
        return EvaluatedCount;
    }

    public void setEvaluatedCount(int evaluatedCount) {
        EvaluatedCount = evaluatedCount;
    }

    public String getTagName() {
        return TagName;
    }

    public void setTagName(String tagName) {
        TagName = tagName;
    }
}
