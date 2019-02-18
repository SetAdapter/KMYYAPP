package com.kmwlyy.doctor.model;

import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */
public class ServiceSetBean {

    /**
     * AcceptCount : 1
     * State : false
     * ClinicMonth : 201703
     */

    private FreeClinicSettingBean FreeClinicSetting;
    /**
     * ServiceID : 0b8e32a220134d3db0cc53e927bd8881
     * ServiceType : 0
     * ServiceTypeName : 医院挂号
     * ServiceSwitch : 1
     * ServicePrice : 0.1
     * ServicePriceDown : 0
     * ServicePriceUp : 1000
     */

    private List<DataBean> Data;

    public FreeClinicSettingBean getFreeClinicSetting() {
        return FreeClinicSetting;
    }

    public StopDiagnosisBean StopDiagnosis;

    public void setFreeClinicSetting(FreeClinicSettingBean FreeClinicSetting) {
        this.FreeClinicSetting = FreeClinicSetting;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class FreeClinicSettingBean {
        private int AcceptCount;
        private boolean State;
        private String ClinicMonth;

        public int getAcceptCount() {
            return AcceptCount;
        }

        public void setAcceptCount(int AcceptCount) {
            this.AcceptCount = AcceptCount;
        }

        public boolean isState() {
            return State;
        }

        public void setState(boolean State) {
            this.State = State;
        }

        public String getClinicMonth() {
            return ClinicMonth;
        }

        public void setClinicMonth(String ClinicMonth) {
            this.ClinicMonth = ClinicMonth;
        }
    }

    public static class DataBean {
        private String ServiceID;
        private int ServiceType;
        private String ServiceTypeName;
        private int ServiceSwitch;
        private double ServicePrice;
        private int ServicePriceDown;
        private int ServicePriceUp;

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

        public String getServiceTypeName() {
            return ServiceTypeName;
        }

        public void setServiceTypeName(String ServiceTypeName) {
            this.ServiceTypeName = ServiceTypeName;
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

        public int getServicePriceDown() {
            return ServicePriceDown;
        }

        public void setServicePriceDown(int ServicePriceDown) {
            this.ServicePriceDown = ServicePriceDown;
        }

        public int getServicePriceUp() {
            return ServicePriceUp;
        }

        public void setServicePriceUp(int ServicePriceUp) {
            this.ServicePriceUp = ServicePriceUp;
        }
    }

    public static class StopDiagnosisBean{
        //BeginDate":"2017-07-13","EndDate":"2017-07-14","BeginTimeName":"上午","EndTimeName":"晚上"
        public String BeginDate;
        public String EndDate;
        public String BeginTimeName;
        public String EndTimeName;
    }
}
