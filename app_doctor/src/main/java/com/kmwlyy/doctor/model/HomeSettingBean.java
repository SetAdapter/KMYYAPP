package com.kmwlyy.doctor.model;

import android.support.annotation.NonNull;

import com.winson.ui.widget.CommonAdapter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by TFeng on 2017/7/10.
 */

public class HomeSettingBean implements Serializable {

    /**
     * ContractTime : 0001-01-01T00:00:00
     * DoctorFamilyPackages : []
     * DoctorPackageBases : [
     *{"DoctorFamilyPackageDetail":[],"FamilyPackageBaseID":"49B593FF428049EDA9E6BDCA671D30AC","FreeCouponCount":2,"MaxServicePrice":100000,"MinServicePrice":0,"MonthCount":1,"MonthCountName":"一个月","Remark":"49B593FF428049EDA9E6BDCA671D30AC","ServicePrice":100},{"DoctorFamilyPackageDetail":[],"FamilyPackageBaseID":"64E442D3D8534A5C9EE78D279545E288","FreeCouponCount":2,"MaxServicePrice":100000,"MinServicePrice":0,"MonthCount":3,"MonthCountName":"三个月","Remark":"64E442D3D8534A5C9EE78D279545E288","ServicePrice":280},{"DoctorFamilyPackageDetail":[],"FamilyPackageBaseID":"B0BFB164E3FF484CAE52A68659A7C177","FreeCouponCount":2,"MaxServicePrice":100000,"MinServicePrice":0,"MonthCount":12,"MonthCountName":"一年","Remark":"B0BFB164E3FF484CAE52A68659A7C177","ServicePrice":1000},{"DoctorFamilyPackageDetail":[],"FamilyPackageBaseID":"FB630ABA375647359FED52375FE730AA","FreeCouponCount":2,"MaxServicePrice":100000,"MinServicePrice":0,"MonthCount":6,"MonthCountName":"六个月","Remark":"FB630ABA375647359FED52375FE730AA","ServicePrice":520}]
     * DoctorWorkingTimes : []
     * Enable : false
     * IsFreeCoupon : false
     * MaxSaleCount : 0
     * WorkingTimeBases : [{"BeginTime":"12:00","EndTime":"18:00","WorkingTimeBaseID":"25AC3F2D9350472A916161ED731B8213","WorkingTimeName":"下午"},{"BeginTime":"06:00","EndTime":"12:00","WorkingTimeBaseID":"88C56AA42B86427BAB0DC0846D1C275E","WorkingTimeName":"上午"},{"BeginTime":"18:00","EndTime":"23:59","WorkingTimeBaseID":"B4989AECC92A455FBD40A492E87E52CD","WorkingTimeName":"晚上"}]
     */

    private String ContractTime;
    private boolean Enable;
    private boolean IsFreeCoupon;
    private int MaxSaleCount;
    private String OfflineAddress;
    private String AutoReplyContent;
    private String MapAddress;
    private List<DoctorPackageBean> DoctorFamilyPackages;
    private List<DoctorPackageBasesBean> DoctorPackageBases;
    private List<WorkingTimeBean> DoctorWorkingTimes;
    private List<WorkingTimeBasesBean> WorkingTimeBases;
    private List<DoctorPackageBean> PackageList;

    public String getMapAddress() {
        return MapAddress;
    }

    public void setMapAddress(String mapAddress) {
        MapAddress = mapAddress;
    }

    public List<DoctorPackageBean> getPackageList() {
        return PackageList;
    }

    public void setPackageList(List<DoctorPackageBean> packageList) {
        PackageList = packageList;
    }

    public String getAutoReplyContent() {
        return AutoReplyContent;
    }

    public void setAutoReplyContent(String autoReplyContent) {
        AutoReplyContent = autoReplyContent;
    }

    public boolean isFreeCoupon() {
        return IsFreeCoupon;
    }

    public void setFreeCoupon(boolean freeCoupon) {
        IsFreeCoupon = freeCoupon;
    }

    public String getOfflineAddress() {
        return OfflineAddress;
    }

    public void setOfflineAddress(String offlineAddress) {
        OfflineAddress = offlineAddress;
    }

    public String getContractTime() {
        return ContractTime;
    }

    public void setContractTime(String ContractTime) {
        this.ContractTime = ContractTime;
    }

    public boolean isEnable() {
        return Enable;
    }

    public void setEnable(boolean Enable) {
        this.Enable = Enable;
    }

    public boolean isIsFreeCoupon() {
        return IsFreeCoupon;
    }

    public void setIsFreeCoupon(boolean IsFreeCoupon) {
        this.IsFreeCoupon = IsFreeCoupon;
    }

    public int getMaxSaleCount() {
        return MaxSaleCount;
    }

    public void setMaxSaleCount(int MaxSaleCount) {
        this.MaxSaleCount = MaxSaleCount;
    }

    public List<DoctorPackageBean> getDoctorFamilyPackages() {
        return DoctorFamilyPackages;
    }

    public void setDoctorFamilyPackages(List<DoctorPackageBean> DoctorFamilyPackages) {
        this.DoctorFamilyPackages = DoctorFamilyPackages;
    }

    public List<DoctorPackageBasesBean> getDoctorPackageBases() {
        return DoctorPackageBases;
    }

    public void setDoctorPackageBases(List<DoctorPackageBasesBean> DoctorPackageBases) {
        this.DoctorPackageBases = DoctorPackageBases;
    }

    public List<WorkingTimeBean> getDoctorWorkingTimes() {
        return DoctorWorkingTimes;
    }

    public void setDoctorWorkingTimes(List<WorkingTimeBean> DoctorWorkingTimes) {
        this.DoctorWorkingTimes = DoctorWorkingTimes;
    }

    public List<WorkingTimeBasesBean> getWorkingTimeBases() {
        return WorkingTimeBases;
    }

    public void setWorkingTimeBases(List<WorkingTimeBasesBean> WorkingTimeBases) {
        this.WorkingTimeBases = WorkingTimeBases;
    }

    public static class DoctorPackageBasesBean implements Serializable{
        /**
         * DoctorFamilyPackageDetail : []
         * FamilyPackageBaseID : 49B593FF428049EDA9E6BDCA671D30AC
         * FreeCouponCount : 2
         * MaxServicePrice : 100000.0
         * MinServicePrice : 0.0
         * MonthCount : 1
         * MonthCountName : 一个月
         * Remark : 49B593FF428049EDA9E6BDCA671D30AC
         * ServicePrice : 100.0
         */

        private String FamilyPackageBaseID;
        private int FreeCouponCount;
        private double MaxServicePrice;
        private double MinServicePrice;
        private int MonthCount;
        private String MonthCountName;
        private String Remark;
        private double ServicePrice;
        private List<DoctorFamilyPackageDetail> DoctorFamilyPackageDetail;

        public String getFamilyPackageBaseID() {
            return FamilyPackageBaseID;
        }

        public void setFamilyPackageBaseID(String FamilyPackageBaseID) {
            this.FamilyPackageBaseID = FamilyPackageBaseID;
        }

        public int getFreeCouponCount() {
            return FreeCouponCount;
        }

        public void setFreeCouponCount(int FreeCouponCount) {
            this.FreeCouponCount = FreeCouponCount;
        }

        public double getMaxServicePrice() {
            return MaxServicePrice;
        }

        public void setMaxServicePrice(double MaxServicePrice) {
            this.MaxServicePrice = MaxServicePrice;
        }

        public double getMinServicePrice() {
            return MinServicePrice;
        }

        public void setMinServicePrice(double MinServicePrice) {
            this.MinServicePrice = MinServicePrice;
        }

        public int getMonthCount() {
            return MonthCount;
        }

        public void setMonthCount(int MonthCount) {
            this.MonthCount = MonthCount;
        }

        public String getMonthCountName() {
            return MonthCountName;
        }

        public void setMonthCountName(String MonthCountName) {
            this.MonthCountName = MonthCountName;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public double getServicePrice() {
            return ServicePrice;
        }

        public void setServicePrice(double ServicePrice) {
            this.ServicePrice = ServicePrice;
        }

        public List<DoctorFamilyPackageDetail> getDoctorFamilyPackageDetail() {
            return DoctorFamilyPackageDetail;
        }

        public void setDoctorFamilyPackageDetail(List<DoctorFamilyPackageDetail> DoctorFamilyPackageDetail) {
            this.DoctorFamilyPackageDetail = DoctorFamilyPackageDetail;
        }
    }

    public static class WorkingTimeBasesBean implements Serializable{
        /**
         * BeginTime : 12:00
         * EndTime : 18:00
         * WorkingTimeBaseID : 25AC3F2D9350472A916161ED731B8213
         * WorkingTimeName : 下午
         */

        private String BeginTime;
        private String EndTime;
        private String WorkingTimeBaseID;
        private String WorkingTimeName;

        public String getBeginTime() {
            return BeginTime;
        }

        public void setBeginTime(String BeginTime) {
            this.BeginTime = BeginTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public String getWorkingTimeBaseID() {
            return WorkingTimeBaseID;
        }

        public void setWorkingTimeBaseID(String WorkingTimeBaseID) {
            this.WorkingTimeBaseID = WorkingTimeBaseID;
        }

        public String getWorkingTimeName() {
            return WorkingTimeName;
        }

        public void setWorkingTimeName(String WorkingTimeName) {
            this.WorkingTimeName = WorkingTimeName;
        }
    }
    public static class WorkingTimeBean implements Serializable,Comparable<WorkingTimeBean>{
        @Override
        public String toString() {
            return "WorkingTimeBean{" +
                    "ID=" + ID +
                    ", WorkingTimeID='" + WorkingTimeID + '\'' +
                    ", WorkingTimeBaseID='" + WorkingTimeBaseID + '\'' +
                    ", DayOfWeek=" + DayOfWeek +
                    ", BeginTime='" + BeginTime + '\'' +
                    ", EndTime='" + EndTime + '\'' +
                    ", WorkingTimeName='" + WorkingTimeName + '\'' +
                    '}';
        }

        int ID;
        String WorkingTimeID;
        String WorkingTimeBaseID;
        int DayOfWeek;
        String BeginTime;
        String EndTime;
        String WorkingTimeName;
        boolean isSelected;
        boolean isBeginChecked;
        boolean isEndChecked;

        public boolean isBeginChecked() {
            return isBeginChecked;
        }

        public void setBeginChecked(boolean beginChecked) {
            isBeginChecked = beginChecked;
        }

        public boolean isEndChecked() {
            return isEndChecked;
        }

        public void setEndChecked(boolean endChecked) {
            isEndChecked = endChecked;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getWorkingTimeName() {
            return WorkingTimeName;
        }

        public void setWorkingTimeName(String workingTimeName) {
            WorkingTimeName = workingTimeName;
        }

        public String getWorkingTimeBaseID() {
            return WorkingTimeBaseID;
        }

        public void setWorkingTimeBaseID(String workingTimeBaseID) {
            WorkingTimeBaseID = workingTimeBaseID;
        }

        public WorkingTimeBean(){

        }

        public WorkingTimeBean(String workingTimeID,String workingTimeBaseID, int dayOfWeek, String beginTime, String endTime) {
            WorkingTimeID = workingTimeID;
            DayOfWeek = dayOfWeek;
            BeginTime = beginTime;
            EndTime = endTime;
            WorkingTimeBaseID = workingTimeBaseID;
        }

        public String getWorkingTimeID() {
            return WorkingTimeID;
        }

        public void setWorkingTimeID(String workingTimeID) {
            WorkingTimeID = workingTimeID;
        }



        public int getDayOfWeek() {
            return DayOfWeek;
        }

        public void setDayOfWeek(int dayOfWeek) {
            DayOfWeek = dayOfWeek;
        }

        public String getBeginTime() {
            return BeginTime;
        }

        public void setBeginTime(String beginTime) {
            BeginTime = beginTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String endTime) {
            EndTime = endTime;
        }

        @Override
        public int compareTo(WorkingTimeBean workingTimeBean) {
            int i = this.getID()-workingTimeBean.getID();
            return i;
        }
    }

    public static class DoctorPackageBean implements Serializable,Comparable<DoctorPackageBean>{
        @Override
        public String toString() {
            return "DoctorPackageBean{" +
                    "Remark='" + Remark + '\'' +
                    ", MonthCountName='" + MonthCountName + '\'' +
                    ", FreeCouponCount='" + FreeCouponCount + '\'' +
                    ", FamilyContractID='" + FamilyContractID + '\'' +
                    ", FamilyPackageID='" + FamilyPackageID + '\'' +
                    ", FamilyPackageBaseID='" + FamilyPackageBaseID + '\'' +
                    ", ServicePrice='" + ServicePrice + '\'' +
                    ", MonthCount=" + MonthCount +
                    ", VidServiceCount='" + VidServiceCount + '\'' +
                    ", DoctorFamilyPackageDetail=" + DoctorFamilyPackageDetail +
                    '}';
        }

        /*
        *
        * {
			Remark = "石家庄市桥西区大河报客户端里发现的时候",
			MonthCountName = "三个月",
			FreeCouponCount = "2",
			VidServiceCount = "100",
			FamilyContractID = "a68f815d57884924ab5d20fe1629b87b",
			ServicePrice = "255",
			MonthCount = "3",
			FamilyPackageID = "12c64a89fb5a4428b44bda86b97fceda"
		}
        *
        * */

        private String Remark;
        private String MonthCountName;
        private String FreeCouponCount;
        private String FamilyContractID;
        private String FamilyPackageID;
        private String FamilyPackageBaseID;
        private String ServicePrice;
        private int MonthCount;
        private String VidServiceCount;
        private List<DoctorFamilyPackageDetail> DoctorFamilyPackageDetail;

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public String getMonthCountName() {
            return MonthCountName;
        }

        public void setMonthCountName(String monthCountName) {
            MonthCountName = monthCountName;
        }

        public String getFreeCouponCount() {
            return FreeCouponCount;
        }

        public void setFreeCouponCount(String freeCouponCount) {
            FreeCouponCount = freeCouponCount;
        }

        public String getFamilyContractID() {
            return FamilyContractID;
        }

        public void setFamilyContractID(String familyContractID) {
            FamilyContractID = familyContractID;
        }

        public String getFamilyPackageID() {
            return FamilyPackageID;
        }

        public void setFamilyPackageID(String familyPackageID) {
            FamilyPackageID = familyPackageID;
        }

        public String getFamilyPackageBaseID() {
            return FamilyPackageBaseID;
        }

        public void setFamilyPackageBaseID(String familyPackageBaseID) {
            FamilyPackageBaseID = familyPackageBaseID;
        }

        public String getServicePrice() {
            return ServicePrice;
        }

        public void setServicePrice(String servicePrice) {
            ServicePrice = servicePrice;
        }

        public int getMonthCount() {
            return MonthCount;
        }

        public void setMonthCount(int monthCount) {
            MonthCount = monthCount;
        }

        public String getVidServiceCount() {
            return VidServiceCount;
        }

        public void setVidServiceCount(String vidServiceCount) {
            VidServiceCount = vidServiceCount;
        }

        public List<HomeSettingBean.DoctorFamilyPackageDetail> getDoctorFamilyPackageDetail() {
            return DoctorFamilyPackageDetail;
        }

        public void setDoctorFamilyPackageDetail(List<HomeSettingBean.DoctorFamilyPackageDetail> doctorFamilyPackageDetail) {
            DoctorFamilyPackageDetail = doctorFamilyPackageDetail;
        }

        @Override
        public int compareTo(@NonNull DoctorPackageBean doctorPackageBean) {
            int i = this.getMonthCount()-doctorPackageBean.getMonthCount();
            return i;
        }
    }

    public class DoctorFamilyPackageDetail implements Serializable{
        private String ServiceType;
        private String ServiceCount;

        public String getServiceType() {
            return ServiceType;
        }

        public void setServiceType(String serviceType) {
            ServiceType = serviceType;
        }
    }
}
