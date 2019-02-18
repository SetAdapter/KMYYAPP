package com.kmwlyy.patient.module.myservice.Bean;

import java.util.List;

/**
 * Created by xcj on 2017/8/14.
 */

public class ServicePackageInfoBean {
    /**
     * Data : {"Details":[{"ConsumeCount":0,"ServiceCount":0,"ServiceItemCode":"130900001","ServiceItemContent":"","ServiceItemName":"健康咨询","ServiceType":1003},{"ConsumeCount":0,"ServiceCount":0,"ServiceItemCode":"130900002","ServiceItemContent":"","ServiceItemName":"疾病健康教育","ServiceType":1004},{"ConsumeCount":0,"ServiceCount":0,"ServiceItemCode":"1002","ServiceItemContent":"","ServiceItemName":"一般诊疗费","ServiceType":1002}],"FitRemark":"初级包","PackageID":"6cd13ed884254579b453753f89206093","PackageName":"初级包","PackageType":1,"Price":0,"Remark":"初级包","Remind":"初级包","UserRange":1,"UserRangeName":"一般人群服务包"}
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
         * Details : [{"ConsumeCount":0,"ServiceCount":0,"ServiceItemCode":"130900001","ServiceItemContent":"","ServiceItemName":"健康咨询","ServiceType":1003},{"ConsumeCount":0,"ServiceCount":0,"ServiceItemCode":"130900002","ServiceItemContent":"","ServiceItemName":"疾病健康教育","ServiceType":1004},{"ConsumeCount":0,"ServiceCount":0,"ServiceItemCode":"1002","ServiceItemContent":"","ServiceItemName":"一般诊疗费","ServiceType":1002}]
         * FitRemark : 初级包
         * PackageID : 6cd13ed884254579b453753f89206093
         * PackageName : 初级包
         * PackageType : 1
         * Price : 0.0
         * Remark : 初级包
         * Remind : 初级包
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
             * ServiceItemCode : 130900001
             * ServiceItemContent :
             * ServiceItemName : 健康咨询
             * ServiceType : 1003
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


//    public String msg;
//    public String resultCode;
//    public Data resultData;
//    public static class Data {
//        @SerializedName("crowddesc")
//        public String crowddesc;
//        @SerializedName("remark")
//        public String remark;
//        @SerializedName("servicedesc")
//        public String servicedesc;
//        @SerializedName("serviceintro")
//        public String serviceintro;
//        @SerializedName("servicename")
//        public String servicename;
//        @SerializedName("servicepkgid")
//        public String servicepkgid;
//        @SerializedName("serviceprice")
//        public String serviceprice;
//        @SerializedName("servicepricehigh")
//        public String servicepricehigh;
//        @SerializedName("servicestatus")
//        public String servicestatus;
//        @SerializedName("totalbuycount")
//        public String totalbuycount;
//        @SerializedName("imageurl")
//        public String imageurl;
//        @SerializedName("servicePkgContentListVO")
//        public ArrayList<ContentInfo> servicePkgContentListVO;
//
//
//    }
//
//    public static class ContentInfo{
//        @SerializedName("listname")
//        public String listname;
//        @SerializedName("servicepkgcontentlistid")
//        public String servicepkgcontentlistid;
//        @SerializedName("qty")
//        public String qty;
//    }
}
