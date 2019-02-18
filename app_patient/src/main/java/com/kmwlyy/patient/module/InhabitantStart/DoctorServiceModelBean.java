package com.kmwlyy.patient.module.InhabitantStart;

/**
 * Created by Administrator on 2017/8/11.
 */

public class DoctorServiceModelBean {


    /**
     * msg : 请求成功
     * resultCode : 0
     * resultData : {"contractcontent":"合同内容","contractstatus":1,"contracttime":1502348507000,"currentPage":1,"doctorgroupid":1,"doctormembers":"团队成员","doctorservicemodelid":1,"doctorservicename":"协议名称","doctorteammaster":"团队责任医生","doctorteammobiles":"团队手机","doctorteamname":"团队名称","doctorteamphones":"团队电话","familyaddress":"家庭住址","familyhealthno":"家庭健康档案号","familyphone":"家庭电话","firstparty":"甲方","pageSize":10,"secondparty":"乙方","servicecompany":"指导单位","servicemembers":"指导单位成员","servicephones":"联系电话","status":1}
     */

    private String msg;
    private String resultCode;
    private ResultDataBean resultData;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultData(ResultDataBean resultData) {
        this.resultData = resultData;
    }

    public String getMsg() {
        return msg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public ResultDataBean getResultData() {
        return resultData;
    }

    public static class ResultDataBean {
        /**
         * contractcontent : 合同内容
         * contractstatus : 1
         * contracttime : 1502348507000
         * currentPage : 1
         * doctorgroupid : 1
         * doctormembers : 团队成员
         * doctorservicemodelid : 1
         * doctorservicename : 协议名称
         * doctorteammaster : 团队责任医生
         * doctorteammobiles : 团队手机
         * doctorteamname : 团队名称
         * doctorteamphones : 团队电话
         * familyaddress : 家庭住址
         * familyhealthno : 家庭健康档案号
         * familyphone : 家庭电话
         * firstparty : 甲方
         * pageSize : 10
         * secondparty : 乙方
         * servicecompany : 指导单位
         * servicemembers : 指导单位成员
         * servicephones : 联系电话
         * status : 1
         */

        private String contractcontent;
        private int contractstatus;
        private long contracttime;
        private int currentPage;
        private int doctorgroupid;
        private String doctormembers;
        private int doctorservicemodelid;
        private String doctorservicename;
        private String doctorteammaster;
        private String doctorteammobiles;
        private String doctorteamname;
        private String doctorteamphones;
        private String familyaddress;
        private String familyhealthno;
        private String familyphone;
        private String firstparty;
        private int pageSize;
        private String secondparty;
        private String servicecompany;
        private String servicemembers;
        private String servicephones;
        private int status;

        public void setContractcontent(String contractcontent) {
            this.contractcontent = contractcontent;
        }

        public void setContractstatus(int contractstatus) {
            this.contractstatus = contractstatus;
        }

        public void setContracttime(long contracttime) {
            this.contracttime = contracttime;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public void setDoctorgroupid(int doctorgroupid) {
            this.doctorgroupid = doctorgroupid;
        }

        public void setDoctormembers(String doctormembers) {
            this.doctormembers = doctormembers;
        }

        public void setDoctorservicemodelid(int doctorservicemodelid) {
            this.doctorservicemodelid = doctorservicemodelid;
        }

        public void setDoctorservicename(String doctorservicename) {
            this.doctorservicename = doctorservicename;
        }

        public void setDoctorteammaster(String doctorteammaster) {
            this.doctorteammaster = doctorteammaster;
        }

        public void setDoctorteammobiles(String doctorteammobiles) {
            this.doctorteammobiles = doctorteammobiles;
        }

        public void setDoctorteamname(String doctorteamname) {
            this.doctorteamname = doctorteamname;
        }

        public void setDoctorteamphones(String doctorteamphones) {
            this.doctorteamphones = doctorteamphones;
        }

        public void setFamilyaddress(String familyaddress) {
            this.familyaddress = familyaddress;
        }

        public void setFamilyhealthno(String familyhealthno) {
            this.familyhealthno = familyhealthno;
        }

        public void setFamilyphone(String familyphone) {
            this.familyphone = familyphone;
        }

        public void setFirstparty(String firstparty) {
            this.firstparty = firstparty;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public void setSecondparty(String secondparty) {
            this.secondparty = secondparty;
        }

        public void setServicecompany(String servicecompany) {
            this.servicecompany = servicecompany;
        }

        public void setServicemembers(String servicemembers) {
            this.servicemembers = servicemembers;
        }

        public void setServicephones(String servicephones) {
            this.servicephones = servicephones;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getContractcontent() {
            return contractcontent;
        }

        public int getContractstatus() {
            return contractstatus;
        }

        public long getContracttime() {
            return contracttime;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public int getDoctorgroupid() {
            return doctorgroupid;
        }

        public String getDoctormembers() {
            return doctormembers;
        }

        public int getDoctorservicemodelid() {
            return doctorservicemodelid;
        }

        public String getDoctorservicename() {
            return doctorservicename;
        }

        public String getDoctorteammaster() {
            return doctorteammaster;
        }

        public String getDoctorteammobiles() {
            return doctorteammobiles;
        }

        public String getDoctorteamname() {
            return doctorteamname;
        }

        public String getDoctorteamphones() {
            return doctorteamphones;
        }

        public String getFamilyaddress() {
            return familyaddress;
        }

        public String getFamilyhealthno() {
            return familyhealthno;
        }

        public String getFamilyphone() {
            return familyphone;
        }

        public String getFirstparty() {
            return firstparty;
        }

        public int getPageSize() {
            return pageSize;
        }

        public String getSecondparty() {
            return secondparty;
        }

        public String getServicecompany() {
            return servicecompany;
        }

        public String getServicemembers() {
            return servicemembers;
        }

        public String getServicephones() {
            return servicephones;
        }

        public int getStatus() {
            return status;
        }
    }
}
