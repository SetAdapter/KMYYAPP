package com.kmwlyy.patient.module.teamdetail;

import java.util.List;

/**
 * Created by Administrator on 2017/8/10.
 */

public class DoctorGroupInfo {

    /**
     * Data : [{"DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","GroupName":"123","OrgnazitionID":"582BCD7B74F74A12931045AE6F525FDE","HospitalName":"小谷围新村社区卫生服务中心","Telephone":"111","Remark":"111","LeaderName":"白玉芳","LeaderTitle":"3","LeaderTitleName":"副主任医师","DoctorGroupMembers":[{"GroupMemberID":"10a012ddb98340df9ba2cb4f031ab432","DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","DoctorID":"320402193308070033","DoctorName":"储留招","Mobile":"13961432988","Title":"3","TitleName":"副主任医师","Position":1},{"GroupMemberID":"30f60931e5984c0c99b14a64ffc8b64f","DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","DoctorID":"320402193206111017","DoctorName":"殷益南","Mobile":"15651225963","Title":"3","TitleName":"副主任医师","Position":1},{"GroupMemberID":"8a694a14b0f94e9a84d45c2403c0dd41","DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","DoctorID":"320404195504250228","DoctorName":"白玉芳","Mobile":"13775607335","Title":"3","TitleName":"副主任医师","Position":2},{"GroupMemberID":"8b6bb9919ab64b899949139ef647370e","DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","DoctorID":"32040219320426102X","DoctorName":"吴静芬","Mobile":"15606129712","Title":"3","TitleName":"副主任医师","Position":1},{"GroupMemberID":"ace7dd957ca04047be312b6424cbae09","DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","DoctorID":"320404195507010625","DoctorName":"蔡旦平","Mobile":"18994989861","Title":"3","TitleName":"副主任医师","Position":1}],"GroupRegions":[{"DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","TownRegionID":"5006","TownRegionName":"小谷围街道"},{"DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","TownRegionID":"5006","TownRegionName":"小谷围街道"},{"DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","TownRegionID":"5006","TownRegionName":"小谷围街道"},{"DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","TownRegionID":"5006","TownRegionName":"小谷围街道"},{"DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","TownRegionID":"5006","TownRegionName":"小谷围街道"}]}]
     * Total : 1
     * Status : 0
     * Msg : 操作成功
     * Result : true
     */

    private int Total;
    private int Status;
    private String Msg;
    private boolean Result;
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

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean Result) {
        this.Result = Result;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * DoctorGroupID : b03335288ce54778966aac81f8ec31bc
         * GroupName : 123
         * OrgnazitionID : 582BCD7B74F74A12931045AE6F525FDE
         * HospitalName : 小谷围新村社区卫生服务中心
         * Telephone : 111
         * Remark : 111
         * LeaderName : 白玉芳
         * LeaderTitle : 3
         * LeaderTitleName : 副主任医师
         * DoctorGroupMembers : [{"GroupMemberID":"10a012ddb98340df9ba2cb4f031ab432","DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","DoctorID":"320402193308070033","DoctorName":"储留招","Mobile":"13961432988","Title":"3","TitleName":"副主任医师","Position":1},{"GroupMemberID":"30f60931e5984c0c99b14a64ffc8b64f","DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","DoctorID":"320402193206111017","DoctorName":"殷益南","Mobile":"15651225963","Title":"3","TitleName":"副主任医师","Position":1},{"GroupMemberID":"8a694a14b0f94e9a84d45c2403c0dd41","DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","DoctorID":"320404195504250228","DoctorName":"白玉芳","Mobile":"13775607335","Title":"3","TitleName":"副主任医师","Position":2},{"GroupMemberID":"8b6bb9919ab64b899949139ef647370e","DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","DoctorID":"32040219320426102X","DoctorName":"吴静芬","Mobile":"15606129712","Title":"3","TitleName":"副主任医师","Position":1},{"GroupMemberID":"ace7dd957ca04047be312b6424cbae09","DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","DoctorID":"320404195507010625","DoctorName":"蔡旦平","Mobile":"18994989861","Title":"3","TitleName":"副主任医师","Position":1}]
         * GroupRegions : [{"DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","TownRegionID":"5006","TownRegionName":"小谷围街道"},{"DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","TownRegionID":"5006","TownRegionName":"小谷围街道"},{"DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","TownRegionID":"5006","TownRegionName":"小谷围街道"},{"DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","TownRegionID":"5006","TownRegionName":"小谷围街道"},{"DoctorGroupID":"b03335288ce54778966aac81f8ec31bc","TownRegionID":"5006","TownRegionName":"小谷围街道"}]
         */

        private String DoctorGroupID;
        private String GroupName;
        private String OrgnazitionID;
        private String HospitalName;
        private String Telephone;
        private String Remark;
        private String LeaderName;
        private String LeaderTitle;
        private String LeaderTitleName;
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

        public String getLeaderName() {
            return LeaderName;
        }

        public void setLeaderName(String LeaderName) {
            this.LeaderName = LeaderName;
        }

        public String getLeaderTitle() {
            return LeaderTitle;
        }

        public void setLeaderTitle(String LeaderTitle) {
            this.LeaderTitle = LeaderTitle;
        }

        public String getLeaderTitleName() {
            return LeaderTitleName;
        }

        public void setLeaderTitleName(String LeaderTitleName) {
            this.LeaderTitleName = LeaderTitleName;
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
             * GroupMemberID : 10a012ddb98340df9ba2cb4f031ab432
             * DoctorGroupID : b03335288ce54778966aac81f8ec31bc
             * DoctorID : 320402193308070033
             * DoctorName : 储留招
             * Mobile : 13961432988
             * Title : 3
             * TitleName : 副主任医师
             * Position : 1
             */

            private String GroupMemberID;
            private String DoctorGroupID;
            private String DoctorID;
            private String DoctorName;
            private String Mobile;
            private String Title;
            private String TitleName;
            private int Position;

            public String getGroupMemberID() {
                return GroupMemberID;
            }

            public void setGroupMemberID(String GroupMemberID) {
                this.GroupMemberID = GroupMemberID;
            }

            public String getDoctorGroupID() {
                return DoctorGroupID;
            }

            public void setDoctorGroupID(String DoctorGroupID) {
                this.DoctorGroupID = DoctorGroupID;
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

            public String getMobile() {
                return Mobile;
            }

            public void setMobile(String Mobile) {
                this.Mobile = Mobile;
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

            public int getPosition() {
                return Position;
            }

            public void setPosition(int Position) {
                this.Position = Position;
            }
        }

        public static class GroupRegionsBean {
            /**
             * DoctorGroupID : b03335288ce54778966aac81f8ec31bc
             * TownRegionID : 5006
             * TownRegionName : 小谷围街道
             */

            private String DoctorGroupID;
            private String TownRegionID;
            private String TownRegionName;

            public String getDoctorGroupID() {
                return DoctorGroupID;
            }

            public void setDoctorGroupID(String DoctorGroupID) {
                this.DoctorGroupID = DoctorGroupID;
            }

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
