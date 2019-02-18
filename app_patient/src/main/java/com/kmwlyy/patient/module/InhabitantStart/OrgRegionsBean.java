package com.kmwlyy.patient.module.InhabitantStart;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */

public class OrgRegionsBean {


    /**
     * Data : [{"FDOrgRegionID":"92d33f5fa7ed49389d9f7b8be9fdb4ba","HospitalID":"0E48FFEEEA7446BA8F2AF8DF6B571411","HospitalName":"大龙社区卫生服务中心","DistrictRegionID":"2316","DistrictRegionName":"番禺区","TownRegionID":"5010","TownRegionName":"大龙街道","CreateTime":"2017-08-22T19:11:18.5437941"},{"FDOrgRegionID":"c6ad7df9530c4dfaa926511e622e438a","HospitalID":"84384101DC28488F8EA9F565D96CFB4C","HospitalName":"大石街社区卫生服务中心","DistrictRegionID":"2316","DistrictRegionName":"番禺区","TownRegionID":"5009","TownRegionName":"大石街道","CreateTime":"2017-08-22T19:11:26.5542523"},{"FDOrgRegionID":"15dab6cba1824be78d39f63ca9d58fac","HospitalID":"99B39F85310A4E09BEAC15ADD97D79E9","HospitalName":"东环街社区卫生服务中心","DistrictRegionID":"2316","DistrictRegionName":"番禺区","TownRegionID":"5002","TownRegionName":"东环街道","CreateTime":"2017-08-22T19:12:43.773669"},{"FDOrgRegionID":"6d11817827a64012bfffedb8d97c893f","HospitalID":"0E48FFEEEA7446BA8F2AF34F6B571411","HospitalName":"化龙区卫生服务中心","DistrictRegionID":"2316","DistrictRegionName":"番禺区","TownRegionID":"5015","TownRegionName":"化龙镇","CreateTime":"2017-08-22T19:10:42.8617532"},{"FDOrgRegionID":"b538c49f610648dabbf9e28663133e92","HospitalID":"74DD1AEC825F45EC907DBF76C0456D67","HospitalName":"洛浦街社区卫生服务中心","DistrictRegionID":"2316","DistrictRegionName":"番禺区","TownRegionID":"5003","TownRegionName":"洛浦街道","CreateTime":"2017-08-22T19:12:29.1998354"},{"FDOrgRegionID":"971df9df903f4a319b4749b8bf2a8474","HospitalID":"2C4F7A78D735456D8D60541FC72738D1","HospitalName":"南村社区卫生服务中心","DistrictRegionID":"2316","DistrictRegionName":"番禺区","TownRegionID":"5014","TownRegionName":"南村镇","CreateTime":"2017-08-22T19:10:49.851153"},{"FDOrgRegionID":"302db3117bfe4ae0a87e10a53d495975","HospitalID":"A1EB253B5E7A410B8B4FFFD0C95A37E7","HospitalName":"桥南街社区卫生服务中心","DistrictRegionID":"2316","DistrictRegionName":"番禺区","TownRegionID":"5004","TownRegionName":"桥南街道","CreateTime":"2017-08-22T19:12:17.777182"},{"FDOrgRegionID":"7959bfc4d4a5443eb70c5013fb1663fb","HospitalID":"097FE4DC787E4958ADCE072CF588F248","HospitalName":"沙头街社区卫生服务中心","DistrictRegionID":"2316","DistrictRegionName":"番禺区","TownRegionID":"5008","TownRegionName":"沙头街道","CreateTime":"2017-08-22T19:11:37.860899"},{"FDOrgRegionID":"6a6eec92992d47a2825c59a96cc089ce","HospitalID":"4257A067EA1E4AE9B3FDF48B70C24B7E","HospitalName":"沙湾社区卫生服务中心","DistrictRegionID":"2316","DistrictRegionName":"番禺区","TownRegionID":"5011","TownRegionName":"沙湾镇","CreateTime":"2017-08-22T19:11:09.8812986"},{"FDOrgRegionID":"190b465488e245778db34a32cffd2e0f","HospitalID":"10FCC8FC37814513B9F7FA7950CBDFD7","HospitalName":"石壁社区卫生服务中心","DistrictRegionID":"2316","DistrictRegionName":"番禺区","TownRegionID":"5007","TownRegionName":"石壁街道","CreateTime":"2017-08-22T19:11:47.7864667"}]
     * Total : 16
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
         * FDOrgRegionID : 92d33f5fa7ed49389d9f7b8be9fdb4ba
         * HospitalID : 0E48FFEEEA7446BA8F2AF8DF6B571411
         * HospitalName : 大龙社区卫生服务中心
         * DistrictRegionID : 2316
         * DistrictRegionName : 番禺区
         * TownRegionID : 5010
         * TownRegionName : 大龙街道
         * CreateTime : 2017-08-22T19:11:18.5437941
         */

        private String FDOrgRegionID;
        private String HospitalID;
        private String HospitalName;
        private String DistrictRegionID;
        private String DistrictRegionName;
        private String TownRegionID;
        private String TownRegionName;
        private String CreateTime;

        public String getFDOrgRegionID() {
            return FDOrgRegionID;
        }

        public void setFDOrgRegionID(String FDOrgRegionID) {
            this.FDOrgRegionID = FDOrgRegionID;
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

        public String getDistrictRegionID() {
            return DistrictRegionID;
        }

        public void setDistrictRegionID(String DistrictRegionID) {
            this.DistrictRegionID = DistrictRegionID;
        }

        public String getDistrictRegionName() {
            return DistrictRegionName;
        }

        public void setDistrictRegionName(String DistrictRegionName) {
            this.DistrictRegionName = DistrictRegionName;
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

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String CreateTime) {
            this.CreateTime = CreateTime;
        }
    }
}
