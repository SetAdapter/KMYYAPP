package com.kmwlyy.doctor.model;

/**
 * Created by Administrator on 2017/1/9.
 */
public class DoctorInfoBean {

    /**
     * DoctorID : CDCEA38AD8C04063AA79DAC2ADF673E7
     * DoctorName : 林东浩
     * DepartmentID : A8064D2DAE3542B18CBD64F467828F57
     * DepartmentName : 健康体检中心
     * HospitalID : 42FF1C61132E443F862510FF3BC3B03A
     * HospitalName : 康美医院
     * PhotoUrl : http://121.15.153.63:8027/images/ff80fbb4366a4a5c88ad5c5f1df4b96a
     * Income : 0.28
     * ServiceTimes : 39
     * FollowedCount : 2
     * EvaluatedCount : 14
     * DiagnoseTimes : 36
     */

    private String DoctorID;
    private String DoctorName;
    private String DepartmentID;
    private String DepartmentName;
    private String HospitalID;
    private String HospitalName;
    private String PhotoUrl;
    private double Income;
    private int ServiceTimes;
    private int FollowedCount;
    private int EvaluatedCount;
    private int DiagnoseTimes;

    public String getDoctorID() {
        return DoctorID;
    }

    public void setDoctorID(String DoctorID) {
        this.DoctorID = DoctorID;
    }

    public String getDoctorName() {
        return DoctorName;
    }

    public void setDoctorName(String DoctorName) {
        this.DoctorName = DoctorName;
    }

    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String DepartmentID) {
        this.DepartmentID = DepartmentID;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String DepartmentName) {
        this.DepartmentName = DepartmentName;
    }

    public String getHospitalID() {
        return HospitalID;
    }

    public void setHospitalID(String HospitalID) {
        this.HospitalID = HospitalID;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String HospitalName) {
        this.HospitalName = HospitalName;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String PhotoUrl) {
        this.PhotoUrl = PhotoUrl;
    }

    public double getIncome() {
        return Income;
    }

    public void setIncome(double Income) {
        this.Income = Income;
    }

    public int getServiceTimes() {
        return ServiceTimes;
    }

    public void setServiceTimes(int ServiceTimes) {
        this.ServiceTimes = ServiceTimes;
    }

    public int getFollowedCount() {
        return FollowedCount;
    }

    public void setFollowedCount(int FollowedCount) {
        this.FollowedCount = FollowedCount;
    }

    public int getEvaluatedCount() {
        return EvaluatedCount;
    }

    public void setEvaluatedCount(int EvaluatedCount) {
        this.EvaluatedCount = EvaluatedCount;
    }

    public int getDiagnoseTimes() {
        return DiagnoseTimes;
    }

    public void setDiagnoseTimes(int DiagnoseTimes) {
        this.DiagnoseTimes = DiagnoseTimes;
    }
}
