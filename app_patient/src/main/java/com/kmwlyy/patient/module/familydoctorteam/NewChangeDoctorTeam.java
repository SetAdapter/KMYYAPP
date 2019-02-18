package com.kmwlyy.patient.module.familydoctorteam;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Stefan on 2017/8/9.
 */

public class NewChangeDoctorTeam implements Serializable{


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

    public static class DataBean implements Serializable{

        private GroupInfoBean GroupInfo;
        private String DoctorID;
        private String DoctorName;
        private String Title;
        private String TitleName;
        private String HospitalName;
        private String Mobile;
        private boolean GroupIsEmpty;

        public GroupInfoBean getGroupInfo() {
            return GroupInfo;
        }

        public void setGroupInfo(GroupInfoBean GroupInfo) {
            this.GroupInfo = GroupInfo;
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

        public boolean isGroupIsEmpty() {
            return GroupIsEmpty;
        }

        public void setGroupIsEmpty(boolean GroupIsEmpty) {
            this.GroupIsEmpty = GroupIsEmpty;
        }

        public static class GroupInfoBean implements Serializable{

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

            public static class DoctorGroupMembersBean implements Serializable{

                private String GroupMemberID;
                private String DoctorGroupID;
                private String DoctorID;
                private String DoctorName;
                private String DepartmentName;
                private String Mobile;
                private String Title;
                private String TitleName;
                private String PhotoUrl;
                private String Specialty;
                private int Position;
                private String _PhotoUrl;
                private List<DoctorServicesBean> DoctorServices;

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

                public String getDepartmentName() {
                    return DepartmentName;
                }

                public void setDepartmentName(String DepartmentName) {
                    this.DepartmentName = DepartmentName;
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

                public List<DoctorServicesBean> getDoctorServices() {
                    return DoctorServices;
                }

                public void setDoctorServices(List<DoctorServicesBean> DoctorServices) {
                    this.DoctorServices = DoctorServices;
                }

                public static class DoctorServicesBean implements Serializable{

                    private String ServiceID;
                    private int ServiceType;
                    private int ServiceSwitch;
                    private double ServicePrice;
                    private boolean HasSchedule;

                    public String getServiceID() {
                        return ServiceID;
                    }

                    public void setServiceID(String ServiceID) {
                        this.ServiceID = ServiceID;
                    }

                    public int getServiceType() {
                        return ServiceType;
                    }

                    public void setServiceType(int ServiceType) {
                        this.ServiceType = ServiceType;
                    }

                    public int getServiceSwitch() {
                        return ServiceSwitch;
                    }

                    public void setServiceSwitch(int ServiceSwitch) {
                        this.ServiceSwitch = ServiceSwitch;
                    }

                    public double getServicePrice() {
                        return ServicePrice;
                    }

                    public void setServicePrice(double ServicePrice) {
                        this.ServicePrice = ServicePrice;
                    }

                    public boolean isHasSchedule() {
                        return HasSchedule;
                    }

                    public void setHasSchedule(boolean HasSchedule) {
                        this.HasSchedule = HasSchedule;
                    }
                }
            }

            public static class GroupRegionsBean implements Serializable{
                /**
                 * DoctorGroupID : 179c727ece644965ae99e70076d908ad
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
}
