package com.kmwlyy.patient.module.myservice.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/9.
 */

public class Doorservoce {

    /**
     * Data : [{"BuyUserNum":0,"FitRemark":"初级包","OrderState":-1,"PackageID":"6cd13ed884254579b453753f89206093","PackageName":"初级包","PackageType":1,"Price":0,"Remark":"初级包","Remind":"初级包","UserRange":1},{"BuyUserNum":0,"FitRemark":"初级包（中医药服务包）","OrderState":-1,"PackageID":"750ae5c18a2646c8891a2dc6ccd97a6e","PackageName":"初级包（中医药服务包）","PackageType":1,"Price":0,"Remark":"初级包（中医药服务包）","Remind":"初级包（中医药服务包）","UserRange":1},{"BuyUserNum":2,"FitRemark":"基本公共卫生项目包","OrderState":1,"PackageID":"c04513fa7ff04285b7ecc9e0c292c275","PackageName":"基本公共卫生项目包","PackageType":1,"Price":0,"Remark":"基本公共卫生项目包","Remind":"基本公共卫生项目包","UserRange":0},{"BuyUserNum":0,"FitRemark":"中级包","OrderState":-1,"PackageID":"d5856ea394c94415a3ff57e9ddf8f833","PackageName":"中级包","PackageType":1,"Price":0,"Remark":"中级包","Remind":"中级包","UserRange":1}]
     * Msg : 操作成功
     * Result : false
     * Status : 0
     * Total : 4
     */

    private String Msg;
    private boolean Result;
    private int Status;
    private int Total;
    private List<DataBean> Data;

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

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        /**
         * BuyUserNum : 0
         * FitRemark : 初级包
         * OrderState : -1
         * PackageID : 6cd13ed884254579b453753f89206093
         * PackageName : 初级包
         * PackageType : 1
         * Price : 0.0
         * Remark : 初级包
         * Remind : 初级包
         * UserRange : 1
         */

        private int BuyUserNum;
        private String FitRemark;
        private int OrderState;
        private String PackageID;
        private String PackageName;
        private int PackageType;
        private double Price;
        private String Remark;
        private String Remind;
        private int UserRange;

        public int getBuyUserNum() {
            return BuyUserNum;
        }

        public void setBuyUserNum(int BuyUserNum) {
            this.BuyUserNum = BuyUserNum;
        }

        public String getFitRemark() {
            return FitRemark;
        }

        public void setFitRemark(String FitRemark) {
            this.FitRemark = FitRemark;
        }

        public int getOrderState() {
            return OrderState;
        }

        public void setOrderState(int OrderState) {
            this.OrderState = OrderState;
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
    }
}
