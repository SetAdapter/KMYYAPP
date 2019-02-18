package com.kmwlyy.patient.module.familydoctorteam;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Stefan on 2017/8/9.
 */

public class ChangeDoctorTeam implements Serializable{


    /**
     * Data : [{"DoctorID":"医生ID","DoctorName":"医生姓名","Title":"职称","TitleName":"职称名称","HospitalName":"医院名称","Mobile":"手机","GroupIsEmpty":"","GroupInfo":{"GroupName":"医生团队名称","DoctorGroupID":"医生团队ID","OrgnazitionID":"机构ID","Telephone":"团队联系电话","Remark":"团队介绍","SignatureCount":0,"LeaderName":"队长名称"}}]
     * Total : 100
     * Status : 0
     * Msg :
     */

    private int Total;
    private int Status;
    private String Msg;
    private List<DataBean> Data;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean implements Serializable{
        /**
         * DoctorID : 医生ID
         * DoctorName : 医生姓名
         * Title : 职称
         * TitleName : 职称名称
         * HospitalName : 医院名称
         * Mobile : 手机
         * GroupIsEmpty :
         * GroupInfo : {"GroupName":"医生团队名称","DoctorGroupID":"医生团队ID","OrgnazitionID":"机构ID","Telephone":"团队联系电话","Remark":"团队介绍","SignatureCount":0,"LeaderName":"队长名称"}
         */

        private String DoctorID;
        private String DoctorName;
        private String Title;
        private String TitleName;
        private String HospitalName;
        private String Mobile;
        private String GroupIsEmpty;
        private GroupInfoBean GroupInfo;

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

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getTitleName() {
            return TitleName;
        }

        public void setTitleName(String TitleName) {
            this.TitleName = TitleName;
        }

        public String getHospitalName() {
            return HospitalName;
        }

        public void setHospitalName(String HospitalName) {
            this.HospitalName = HospitalName;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public String getGroupIsEmpty() {
            return GroupIsEmpty;
        }

        public void setGroupIsEmpty(String GroupIsEmpty) {
            this.GroupIsEmpty = GroupIsEmpty;
        }

        public GroupInfoBean getGroupInfo() {
            return GroupInfo;
        }

        public void setGroupInfo(GroupInfoBean GroupInfo) {
            this.GroupInfo = GroupInfo;
        }

        public static class GroupInfoBean implements Serializable{
            /**
             * GroupName : 医生团队名称
             * DoctorGroupID : 医生团队ID
             * OrgnazitionID : 机构ID
             * Telephone : 团队联系电话
             * Remark : 团队介绍
             * SignatureCount : 0
             * LeaderName : 队长名称
             */

            private String GroupName;
            private String DoctorGroupID;
            private String OrgnazitionID;
            private String Telephone;
            private String Remark;
            private int SignatureCount;
            private String LeaderName;

            public String getGroupName() {
                return GroupName;
            }

            public void setGroupName(String GroupName) {
                this.GroupName = GroupName;
            }

            public String getDoctorGroupID() {
                return DoctorGroupID;
            }

            public void setDoctorGroupID(String DoctorGroupID) {
                this.DoctorGroupID = DoctorGroupID;
            }

            public String getOrgnazitionID() {
                return OrgnazitionID;
            }

            public void setOrgnazitionID(String OrgnazitionID) {
                this.OrgnazitionID = OrgnazitionID;
            }

            public String getTelephone() {
                return Telephone;
            }

            public void setTelephone(String Telephone) {
                this.Telephone = Telephone;
            }

            public String getRemark() {
                return Remark;
            }

            public void setRemark(String Remark) {
                this.Remark = Remark;
            }

            public int getSignatureCount() {
                return SignatureCount;
            }

            public void setSignatureCount(int SignatureCount) {
                this.SignatureCount = SignatureCount;
            }

            public String getLeaderName() {
                return LeaderName;
            }

            public void setLeaderName(String LeaderName) {
                this.LeaderName = LeaderName;
            }
        }
    }
}
