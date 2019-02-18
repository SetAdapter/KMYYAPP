package com.kmwlyy.patient.module.myfamiiydoctor;

import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

public class MyFamilyDoctorDean {
    
    /**
     * Data : [{"DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","GroupName":"大龙社区卫生服务中心（一团）","OrgnazitionID":"0E48FFEEEA7446BA8F2AF8DF6B571411","HospitalName":"大龙社区卫生服务中心","Telephone":"18688941654","Remark":"大龙社区卫生服务中心","SignatureCount":0,"LeaderName":"陈彩云","LeaderTitle":"3","LeaderTitleName":"副主任医师","_LeaderPhotoUrl":"images/doctor/default.jpg","LeaderPhotoUrl":"https://tstore.kmwlyy.com:8027/images/doctor/default.jpg","LeaderSpecialty":"","DoctorGroupMembers":[{"GroupMemberID":"4111df15f8c849a491f2edc5ff0936d7","DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","DoctorID":"32040219401113001X","DoctorName":"方敖法","Mobile":"15606125315","Title":"3","TitleName":"副主任医师","PhotoUrl":"https://tstore.kmwlyy.com:8027/images/doctor/default.jpg","Specialty":"","Position":1},{"GroupMemberID":"8606ee76f97843e7acc5aa3b4c345858","DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","DoctorID":"320402194012281020","DoctorName":"陈彩云","Mobile":"18261151651","Title":"3","TitleName":"副主任医师","_PhotoUrl":"images/doctor/default.jpg","PhotoUrl":"https://tstore.kmwlyy.com:8027/images/doctor/default.jpg","Specialty":"","Position":2},{"GroupMemberID":"9735e6b24e014ccd94576bcbd261e10b","DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","DoctorID":"320402194010211029","DoctorName":"黄华华","Mobile":"15695202260","Title":"3","TitleName":"副主任医师","PhotoUrl":"https://tstore.kmwlyy.com:8027/images/doctor/default.jpg","Specialty":"","Position":1},{"GroupMemberID":"9dba5d0198a9480a9e6d2f2857867a5b","DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","DoctorID":"320402194111271039","DoctorName":"黄大龙","Mobile":"13270372293","Title":"3","TitleName":"副主任医师","PhotoUrl":"https://tstore.kmwlyy.com:8027/images/doctor/default.jpg","Specialty":"","Position":1},{"GroupMemberID":"d2a70a425ab14ae4aa439b4992a0657c","DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","DoctorID":"320402194307240613","DoctorName":"李宝祥","Mobile":"18261169909","Title":"3","TitleName":"副主任医师","_PhotoUrl":"images/22657fda351dd313d4a40553f5446af8.png","PhotoUrl":"https://tstore.kmwlyy.com:8027/images/22657fda351dd313d4a40553f5446af8.png","Specialty":"都是可以的、都是可以的","Position":1}],"GroupRegions":[{"DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","TownRegionID":"5010","TownRegionName":"大龙街道"},{"DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","TownRegionID":"5010","TownRegionName":"大龙街道"},{"DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","TownRegionID":"5010","TownRegionName":"大龙街道"},{"DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","TownRegionID":"5010","TownRegionName":"大龙街道"},{"DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","TownRegionID":"5010","TownRegionName":"大龙街道"}]}]
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
         * DoctorGroupID : f84285ad624e42b483fb1ba233db75cb
         * GroupName : 大龙社区卫生服务中心（一团）
         * OrgnazitionID : 0E48FFEEEA7446BA8F2AF8DF6B571411
         * HospitalName : 大龙社区卫生服务中心
         * Telephone : 18688941654
         * Remark : 大龙社区卫生服务中心
         * SignatureCount : 0
         * LeaderName : 陈彩云
         * LeaderTitle : 3
         * LeaderTitleName : 副主任医师
         * _LeaderPhotoUrl : images/doctor/default.jpg
         * LeaderPhotoUrl : https://tstore.kmwlyy.com:8027/images/doctor/default.jpg
         * LeaderSpecialty :
         * DoctorGroupMembers : [{"GroupMemberID":"4111df15f8c849a491f2edc5ff0936d7","DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","DoctorID":"32040219401113001X","DoctorName":"方敖法","Mobile":"15606125315","Title":"3","TitleName":"副主任医师","PhotoUrl":"https://tstore.kmwlyy.com:8027/images/doctor/default.jpg","Specialty":"","Position":1},{"GroupMemberID":"8606ee76f97843e7acc5aa3b4c345858","DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","DoctorID":"320402194012281020","DoctorName":"陈彩云","Mobile":"18261151651","Title":"3","TitleName":"副主任医师","_PhotoUrl":"images/doctor/default.jpg","PhotoUrl":"https://tstore.kmwlyy.com:8027/images/doctor/default.jpg","Specialty":"","Position":2},{"GroupMemberID":"9735e6b24e014ccd94576bcbd261e10b","DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","DoctorID":"320402194010211029","DoctorName":"黄华华","Mobile":"15695202260","Title":"3","TitleName":"副主任医师","PhotoUrl":"https://tstore.kmwlyy.com:8027/images/doctor/default.jpg","Specialty":"","Position":1},{"GroupMemberID":"9dba5d0198a9480a9e6d2f2857867a5b","DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","DoctorID":"320402194111271039","DoctorName":"黄大龙","Mobile":"13270372293","Title":"3","TitleName":"副主任医师","PhotoUrl":"https://tstore.kmwlyy.com:8027/images/doctor/default.jpg","Specialty":"","Position":1},{"GroupMemberID":"d2a70a425ab14ae4aa439b4992a0657c","DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","DoctorID":"320402194307240613","DoctorName":"李宝祥","Mobile":"18261169909","Title":"3","TitleName":"副主任医师","_PhotoUrl":"images/22657fda351dd313d4a40553f5446af8.png","PhotoUrl":"https://tstore.kmwlyy.com:8027/images/22657fda351dd313d4a40553f5446af8.png","Specialty":"都是可以的、都是可以的","Position":1}]
         * GroupRegions : [{"DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","TownRegionID":"5010","TownRegionName":"大龙街道"},{"DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","TownRegionID":"5010","TownRegionName":"大龙街道"},{"DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","TownRegionID":"5010","TownRegionName":"大龙街道"},{"DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","TownRegionID":"5010","TownRegionName":"大龙街道"},{"DoctorGroupID":"f84285ad624e42b483fb1ba233db75cb","TownRegionID":"5010","TownRegionName":"大龙街道"}]
         */

        private String DoctorGroupID;
        private String GroupName;
        private String OrgnazitionID;
        private String HospitalName;
        private String Telephone;
        private String Remark;
        private int SignatureCount;
        private String LeaderName;
        private String LeaderTitle;
        private String LeaderTitleName;
        private String _LeaderPhotoUrl;
        private String LeaderPhotoUrl;
        private String LeaderSpecialty;
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

        public String get_LeaderPhotoUrl() {
            return _LeaderPhotoUrl;
        }

        public void set_LeaderPhotoUrl(String _LeaderPhotoUrl) {
            this._LeaderPhotoUrl = _LeaderPhotoUrl;
        }

        public String getLeaderPhotoUrl() {
            return LeaderPhotoUrl;
        }

        public void setLeaderPhotoUrl(String LeaderPhotoUrl) {
            this.LeaderPhotoUrl = LeaderPhotoUrl;
        }

        public String getLeaderSpecialty() {
            return LeaderSpecialty;
        }

        public void setLeaderSpecialty(String LeaderSpecialty) {
            this.LeaderSpecialty = LeaderSpecialty;
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
             * GroupMemberID : 4111df15f8c849a491f2edc5ff0936d7
             * DoctorGroupID : f84285ad624e42b483fb1ba233db75cb
             * DoctorID : 32040219401113001X
             * DoctorName : 方敖法
             * Mobile : 15606125315
             * Title : 3
             * TitleName : 副主任医师
             * PhotoUrl : https://tstore.kmwlyy.com:8027/images/doctor/default.jpg
             * Specialty :
             * Position : 1
             * _PhotoUrl : images/doctor/default.jpg
             */

            private String GroupMemberID;
            private String DoctorGroupID;
            private String DoctorID;
            private String DoctorName;
            private String Mobile;
            private String Title;
            private String TitleName;
            private String PhotoUrl;
            private String Specialty;
            private int Position;
            private String _PhotoUrl;

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

            public String getPhotoUrl() {
                return PhotoUrl;
            }

            public void setPhotoUrl(String PhotoUrl) {
                this.PhotoUrl = PhotoUrl;
            }

            public String getSpecialty() {
                return Specialty;
            }

            public void setSpecialty(String Specialty) {
                this.Specialty = Specialty;
            }

            public int getPosition() {
                return Position;
            }

            public void setPosition(int Position) {
                this.Position = Position;
            }

            public String get_PhotoUrl() {
                return _PhotoUrl;
            }

            public void set_PhotoUrl(String _PhotoUrl) {
                this._PhotoUrl = _PhotoUrl;
            }
        }

        public static class GroupRegionsBean {
            /**
             * DoctorGroupID : f84285ad624e42b483fb1ba233db75cb
             * TownRegionID : 5010
             * TownRegionName : 大龙街道
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
