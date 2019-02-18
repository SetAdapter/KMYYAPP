package com.kmwlyy.doctor.model;

import com.kmwlyy.doctor.model.httpEvent.HttpUserConsults;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TFeng on 2017/7/11.
 */

public class MedicalHistoryBean implements Serializable {

    @Override
    public String toString() {
        return "MedicalHistoryBean{" +
                "Date='" + Date + '\'' +
                ", EMRName='" + EMRName + '\'' +
                ", HospitalName='" + HospitalName + '\'' +
                ", MemberID='" + MemberID + '\'' +
                ", MemberName='" + MemberName + '\'' +
                ", ModifyTime='" + ModifyTime + '\'' +
                ", Remark='" + Remark + '\'' +
                ", UserMemberEMRID='" + UserMemberEMRID + '\'' +
                ", Files=" + Files +
                '}';
    }

    /**
     * Date : 2017-07-12T00:00:00
     * EMRName : 333
     * Files : [{"FileName":"images/50c0d6e0963f32704142bd0e776e1c19","FileType":0,"FileUrl":"images/50c0d6e0963f32704142bd0e776e1c19","FullFileUrl":"https://tstore.kmwlyy.com:8027/images/50c0d6e0963f32704142bd0e776e1c19","UrlPrefix":"https://tstore.kmwlyy.com:8027/"}]
     * HospitalName :
     * MemberID : 2431C452E1B540F38E7F1A115DC694F8
     * MemberName : 测试账号1234
     * ModifyTime : 2017-07-12T15:20:42.0176536
     * Remark : hhssjsn
     * UserMemberEMRID : 0c5b7aa600924833805aaa67d22144f2
     */

    private String Date;
    private String EMRName;
    private String HospitalName;
    private String MemberID;
    private String MemberName;
    private String ModifyTime;
    private String Remark;
    private String UserMemberEMRID;
    private List<Files> Files;


    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getEMRName() {
        return EMRName;
    }

    public void setEMRName(String EMRName) {
        this.EMRName = EMRName;
    }

    public String getHospitalName() {
        return HospitalName;
    }

    public void setHospitalName(String HospitalName) {
        this.HospitalName = HospitalName;
    }

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String MemberID) {
        this.MemberID = MemberID;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String MemberName) {
        this.MemberName = MemberName;
    }

    public String getModifyTime() {
        return ModifyTime;
    }

    public void setModifyTime(String ModifyTime) {
        this.ModifyTime = ModifyTime;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getUserMemberEMRID() {
        return UserMemberEMRID;
    }

    public void setUserMemberEMRID(String UserMemberEMRID) {
        this.UserMemberEMRID = UserMemberEMRID;
    }

    public List<Files> getFiles() {
        return Files;
    }

    public void setFiles(List<Files> Files) {
        this.Files = Files;
    }
    public class Files implements Serializable{

        /**
         * UrlPrefix : https://tstore.kmwlyy.com:8027/
         * FullFileUrl : https://tstore.kmwlyy.com:8027/images/2b8ff605880583f43f281738aa59a516
         * FileName : images/2b8ff605880583f43f281738aa59a516
         * FileUrl : images/2b8ff605880583f43f281738aa59a516
         * FileType : 0
         */

        private String UrlPrefix;
        private String FullFileUrl;
        private String FileName;
        private String FileUrl;
        private int FileType;

        public String getUrlPrefix() {
            return UrlPrefix;
        }

        public void setUrlPrefix(String UrlPrefix) {
            this.UrlPrefix = UrlPrefix;
        }

        public String getFullFileUrl() {
            return FullFileUrl;
        }

        public void setFullFileUrl(String FullFileUrl) {
            this.FullFileUrl = FullFileUrl;
        }

        public String getFileName() {
            return FileName;
        }

        public void setFileName(String FileName) {
            this.FileName = FileName;
        }

        public String getFileUrl() {
            return FileUrl;
        }

        public void setFileUrl(String FileUrl) {
            this.FileUrl = FileUrl;
        }

        public int getFileType() {
            return FileType;
        }

        public void setFileType(int FileType) {
            this.FileType = FileType;
        }
    }


}
