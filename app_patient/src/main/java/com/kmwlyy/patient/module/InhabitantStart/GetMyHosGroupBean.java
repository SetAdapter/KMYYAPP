package com.kmwlyy.patient.module.InhabitantStart;

import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

public class GetMyHosGroupBean {

    /**
     * Data : [{"DoctorGroupID":"医生团队ID","GroupName":"医生团队名称","OrgnazitionID":"所属医院id","HospitalName":"所属医院名称","DoctorGroupMembers":[{"GroupMemberID":"GroupMemberID","DoctorID":"医生ID","DoctorName":"医生名称","Position":1,"Intro":"医生简介"},{"GroupMemberID":"GroupMemberID","DoctorID":"医生ID","DoctorName":"医生名称","Position":1,"Intro":"医生简介"}],"GroupRegions":[{"TownRegionID":"服务街道区域","TownRegionName":"服务街道名称"}]}]
     * Total : 10
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

    public static class DataBean {
        /**
         * DoctorGroupID : 医生团队ID
         * GroupName : 医生团队名称
         * OrgnazitionID : 所属医院id
         * HospitalName : 所属医院名称
         * DoctorGroupMembers : [{"GroupMemberID":"GroupMemberID","DoctorID":"医生ID","DoctorName":"医生名称","Position":1,"Intro":"医生简介"},{"GroupMemberID":"GroupMemberID","DoctorID":"医生ID","DoctorName":"医生名称","Position":1,"Intro":"医生简介"}]
         * GroupRegions : [{"TownRegionID":"服务街道区域","TownRegionName":"服务街道名称"}]
         */

        private String DoctorGroupID;
        private String GroupName;
        private String OrgnazitionID;
        private String HospitalName;
        private List<DoctorGroupMembersBean> DoctorGroupMembers;
        private List<GroupRegionsBean> GroupRegions;

        public String getDoctorGroupID() {
            return DoctorGroupID;
        }

        public void setDoctorGroupID(String DoctorGroupID) {
            this.DoctorGroupID = DoctorGroupID;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String GroupName) {
            this.GroupName = GroupName;
        }

        public String getOrgnazitionID() {
            return OrgnazitionID;
        }

        public void setOrgnazitionID(String OrgnazitionID) {
            this.OrgnazitionID = OrgnazitionID;
        }

        public String getHospitalName() {
            return HospitalName;
        }

        public void setHospitalName(String HospitalName) {
            this.HospitalName = HospitalName;
        }

        public List<DoctorGroupMembersBean> getDoctorGroupMembers() {
            return DoctorGroupMembers;
        }

        public void setDoctorGroupMembers(List<DoctorGroupMembersBean> DoctorGroupMembers) {
            this.DoctorGroupMembers = DoctorGroupMembers;
        }

        public List<GroupRegionsBean> getGroupRegions() {
            return GroupRegions;
        }

        public void setGroupRegions(List<GroupRegionsBean> GroupRegions) {
            this.GroupRegions = GroupRegions;
        }

        public static class DoctorGroupMembersBean {
            /**
             * GroupMemberID : GroupMemberID
             * DoctorID : 医生ID
             * DoctorName : 医生名称
             * Position : 1
             * Intro : 医生简介
             */

            private String GroupMemberID;
            private String DoctorID;
            private String DoctorName;
            private int Position;
            private String Intro;

            public String getGroupMemberID() {
                return GroupMemberID;
            }

            public void setGroupMemberID(String GroupMemberID) {
                this.GroupMemberID = GroupMemberID;
            }

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

            public int getPosition() {
                return Position;
            }

            public void setPosition(int Position) {
                this.Position = Position;
            }

            public String getIntro() {
                return Intro;
            }

            public void setIntro(String Intro) {
                this.Intro = Intro;
            }
        }

        public static class GroupRegionsBean {
            /**
             * TownRegionID : 服务街道区域
             * TownRegionName : 服务街道名称
             */

            private String TownRegionID;
            private String TownRegionName;

            public String getTownRegionID() {
                return TownRegionID;
            }

            public void setTownRegionID(String TownRegionID) {
                this.TownRegionID = TownRegionID;
            }

            public String getTownRegionName() {
                return TownRegionName;
            }

            public void setTownRegionName(String TownRegionName) {
                this.TownRegionName = TownRegionName;
            }
        }
    }
}
