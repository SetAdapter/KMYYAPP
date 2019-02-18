package com.kmwlyy.doctor.model;

/**
 * Created by Administrator on 2017/7/12.
 */

public class MedRecordBean {

    /**
     * Sympton : jdjej
     * PastMedicalHistory : hdhfh
     * PresentHistoryIllness : jdjdj
     * PreliminaryDiagnosis : hdhdn
     * AllergicHistory : jdjdh
     * Advised : dbdbdn
     */

    private String Sympton;
    private String PastMedicalHistory;
    private String PresentHistoryIllness;
    private String PreliminaryDiagnosis; //初步诊断
    private String AllergicHistory;
    private String Advised; //医生建议
    private String ConsultationSubject; //主诉

    public MedRecordBean(String preliminaryDiagnosis, String advised, String consultationSubject) {
        PreliminaryDiagnosis = preliminaryDiagnosis;
        Advised = advised;
        ConsultationSubject = consultationSubject;
    }

    public String getConsultationSubject() {
        return ConsultationSubject == null?"":ConsultationSubject;
    }

    public void setConsultationSubject(String consultationSubject) {
        ConsultationSubject = consultationSubject;
    }

    public String getSympton() {
        return Sympton;
    }

    public void setSympton(String Sympton) {
        this.Sympton = Sympton;
    }

    public String getPastMedicalHistory() {
        return PastMedicalHistory;
    }

    public void setPastMedicalHistory(String PastMedicalHistory) {
        this.PastMedicalHistory = PastMedicalHistory;
    }

    public String getPresentHistoryIllness() {
        return PresentHistoryIllness;
    }

    public void setPresentHistoryIllness(String PresentHistoryIllness) {
        this.PresentHistoryIllness = PresentHistoryIllness;
    }

    public String getPreliminaryDiagnosis() {
        return PreliminaryDiagnosis == null?"":PreliminaryDiagnosis;
    }

    public void setPreliminaryDiagnosis(String PreliminaryDiagnosis) {
        this.PreliminaryDiagnosis = PreliminaryDiagnosis;
    }

    public String getAllergicHistory() {
        return AllergicHistory;
    }

    public void setAllergicHistory(String AllergicHistory) {
        this.AllergicHistory = AllergicHistory;
    }

    public String getAdvised() {
        return Advised == null?"":Advised;
    }

    public void setAdvised(String Advised) {
        this.Advised = Advised;
    }

    @Override
    public String toString() {
        return "MedRecordBean{" +
                "PreliminaryDiagnosis='" + PreliminaryDiagnosis + '\'' +
                ", Advised='" + Advised + '\'' +
                ", ConsultationSubject='" + ConsultationSubject + '\'' +
                '}';
    }
}
