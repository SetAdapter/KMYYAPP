package com.kmwlyy.patient.module.myservice.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/9.
 */

public class BaseService {


    /**
     * Data : {"Details":[{"ConsumeCount":0,"ServiceCount":0,"ServiceItemCode":"1006","ServiceItemContent":"食疗方指导","ServiceItemName":"辨证施膳指导","ServiceType":1006},{"ConsumeCount":0,"ServiceCount":0,"ServiceItemCode":"1005","ServiceItemContent":"中药辨证处方指导","ServiceItemName":"中医辨证论治","ServiceType":1005}],"FitRemark":"初级包（中医药服务包）","PackageID":"750ae5c18a2646c8891a2dc6ccd97a6e","PackageName":"初级包（中医药服务包）","PackageType":1,"Price":0,"Remark":"初级包（中医药服务包）","Remind":"初级包（中医药服务包）","UserRange":1,"UserRangeName":"一般人群服务包"}
     * Msg : 操作成功
     * Result : true
     * Status : 0
     * Total : 0
     */

    private DataBean Data;
    private String Msg;
    private boolean Result;
    private int Status;
    private int Total;

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
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

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public static class DataBean {
        /**
         * Details : [{"ConsumeCount":0,"ServiceCount":0,"ServiceItemCode":"1006","ServiceItemContent":"食疗方指导","ServiceItemName":"辨证施膳指导","ServiceType":1006},{"ConsumeCount":0,"ServiceCount":0,"ServiceItemCode":"1005","ServiceItemContent":"中药辨证处方指导","ServiceItemName":"中医辨证论治","ServiceType":1005}]
         * FitRemark : 初级包（中医药服务包）
         * PackageID : 750ae5c18a2646c8891a2dc6ccd97a6e
         * PackageName : 初级包（中医药服务包）
         * PackageType : 1
         * Price : 0.0
         * Remark : 初级包（中医药服务包）
         * Remind : 初级包（中医药服务包）
         * UserRange : 1
         * UserRangeName : 一般人群服务包
         */

        private String FitRemark;
        private String PackageID;
        private String PackageName;
        private int PackageType;
        private double Price;
        private String Remark;
        private String Remind;
        private int UserRange;
        private String UserRangeName;
        private List<DetailsBean> Details;

        public String getFitRemark() {
            return FitRemark;
        }

        public void setFitRemark(String FitRemark) {
            this.FitRemark = FitRemark;
        }

        public String getPackageID() {
            return PackageID;
        }

        public void setPackageID(String PackageID) {
            this.PackageID = PackageID;
        }

        public String getPackageName() {
            return PackageName;
        }

        public void setPackageName(String PackageName) {
            this.PackageName = PackageName;
        }

        public int getPackageType() {
            return PackageType;
        }

        public void setPackageType(int PackageType) {
            this.PackageType = PackageType;
        }

        public double getPrice() {
            return Price;
        }

        public void setPrice(double Price) {
            this.Price = Price;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }

        public String getRemind() {
            return Remind;
        }

        public void setRemind(String Remind) {
            this.Remind = Remind;
        }

        public int getUserRange() {
            return UserRange;
        }

        public void setUserRange(int UserRange) {
            this.UserRange = UserRange;
        }

        public String getUserRangeName() {
            return UserRangeName;
        }

        public void setUserRangeName(String UserRangeName) {
            this.UserRangeName = UserRangeName;
        }

        public List<DetailsBean> getDetails() {
            return Details;
        }

        public void setDetails(List<DetailsBean> Details) {
            this.Details = Details;
        }

        public static class DetailsBean {
            /**
             * ConsumeCount : 0
             * ServiceCount : 0
             * ServiceItemCode : 1006
             * ServiceItemContent : 食疗方指导
             * ServiceItemName : 辨证施膳指导
             * ServiceType : 1006
             */

            private int ConsumeCount;
            private int ServiceCount;
            private String ServiceItemCode;
            private String ServiceItemContent;
            private String ServiceItemName;
            private int ServiceType;

            public int getConsumeCount() {
                return ConsumeCount;
            }

            public void setConsumeCount(int ConsumeCount) {
                this.ConsumeCount = ConsumeCount;
            }

            public int getServiceCount() {
                return ServiceCount;
            }

            public void setServiceCount(int ServiceCount) {
                this.ServiceCount = ServiceCount;
            }

            public String getServiceItemCode() {
                return ServiceItemCode;
            }

            public void setServiceItemCode(String ServiceItemCode) {
                this.ServiceItemCode = ServiceItemCode;
            }

            public String getServiceItemContent() {
                return ServiceItemContent;
            }

            public void setServiceItemContent(String ServiceItemContent) {
                this.ServiceItemContent = ServiceItemContent;
            }

            public String getServiceItemName() {
                return ServiceItemName;
            }

            public void setServiceItemName(String ServiceItemName) {
                this.ServiceItemName = ServiceItemName;
            }

            public int getServiceType() {
                return ServiceType;
            }

            public void setServiceType(int ServiceType) {
                this.ServiceType = ServiceType;
            }
        }
    }
}
