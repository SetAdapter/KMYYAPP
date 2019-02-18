package com.kmwlyy.patient.module.myconsult;

import java.util.List;

/**
 * Created by Administrator on 2017/8/11.
 */

public class MyConsultBean {


    /**
     * basePaginator : {"currentPage":1,"pageSize":5,"totalCount":27,"totalPage":6}
     * msg : 返回成功
     * resultCode : 0
     * resultData : [{"askstatus":1,"content":"%C3%A5%E2%80%A2%E2%80%A0%C3%A5%E2%80%9C%C2%81","createtime":1502182400810,"doctorid":23,"doctorname":"测试医生496","imageurl":"/upload/1060b1a7d61d43f4b18a7309ae412fe4.png","residentid":2,"residentname":"dddd","residentquestionid":10,"speaker":"a"},{"askstatus":1,"content":"%C3%A5%E2%80%A2%E2%80%A0%C3%A5%E2%80%9C%C2%81","createtime":1502182547790,"doctorid":23,"doctorname":"测试医生496","imageurl":"/upload/1060b1a7d61d43f4b18a7309ae412fe4.png","residentid":2,"residentname":"dddd","residentquestionid":11,"speaker":"a"},{"askstatus":1,"content":"%C3%A5%E2%80%A2%E2%80%A0%C3%A5%E2%80%9C%C2%81","createtime":1502182585057,"doctorid":23,"doctorname":"测试医生496","imageurl":"/upload/1060b1a7d61d43f4b18a7309ae412fe4.png","residentid":2,"residentname":"dddd","residentquestionid":12,"speaker":"a"},{"askstatus":1,"content":"å\u2022\u2020å\u201c\u0081","createtime":1502182648157,"doctorid":23,"doctorname":"测试医生496","imageurl":"/upload/1060b1a7d61d43f4b18a7309ae412fe4.png","residentid":2,"residentname":"dddd","residentquestionid":13,"speaker":"a"},{"askstatus":1,"content":"%C3%A5%E2%80%A2%E2%80%A0%C3%A5%E2%80%9C%C2%81","createtime":1502182782907,"doctorid":23,"doctorname":"测试医生496","imageurl":"/upload/1060b1a7d61d43f4b18a7309ae412fe4.png","residentid":2,"residentname":"dddd","residentquestionid":14,"speaker":"a"}]
     */

    private BasePaginatorBean basePaginator;
    private String msg;
    private String resultCode;
    private List<ResultDataBean> resultData;

    public void setBasePaginator(BasePaginatorBean basePaginator) {
        this.basePaginator = basePaginator;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultData(List<ResultDataBean> resultData) {
        this.resultData = resultData;
    }

    public BasePaginatorBean getBasePaginator() {
        return basePaginator;
    }

    public String getMsg() {
        return msg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public List<ResultDataBean> getResultData() {
        return resultData;
    }

    public static class BasePaginatorBean {
        /**
         * currentPage : 1
         * pageSize : 5
         * totalCount : 27
         * totalPage : 6
         */

        private int currentPage;
        private int pageSize;
        private int totalCount;
        private int totalPage;

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public int getTotalPage() {
            return totalPage;
        }
    }

    public static class ResultDataBean {
        /**
         * askstatus : 1
         * content : %C3%A5%E2%80%A2%E2%80%A0%C3%A5%E2%80%9C%C2%81
         * createtime : 1502182400810
         * doctorid : 23
         * doctorname : 测试医生496
         * imageurl : /upload/1060b1a7d61d43f4b18a7309ae412fe4.png
         * residentid : 2
         * residentname : dddd
         * residentquestionid : 10
         * speaker : a
         */

        private int askstatus;
        private String content;
        private long createtime;
        private int doctorid;
        private String doctorname;
        private String imageurl;
        private int residentid;
        private String residentname;
        private int residentquestionid;
        private String speaker;

        public void setAskstatus(int askstatus) {
            this.askstatus = askstatus;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public void setDoctorid(int doctorid) {
            this.doctorid = doctorid;
        }

        public void setDoctorname(String doctorname) {
            this.doctorname = doctorname;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public void setResidentid(int residentid) {
            this.residentid = residentid;
        }

        public void setResidentname(String residentname) {
            this.residentname = residentname;
        }

        public void setResidentquestionid(int residentquestionid) {
            this.residentquestionid = residentquestionid;
        }

        public void setSpeaker(String speaker) {
            this.speaker = speaker;
        }

        public int getAskstatus() {
            return askstatus;
        }

        public String getContent() {
            return content;
        }

        public long getCreatetime() {
            return createtime;
        }

        public int getDoctorid() {
            return doctorid;
        }

        public String getDoctorname() {
            return doctorname;
        }

        public String getImageurl() {
            return imageurl;
        }

        public int getResidentid() {
            return residentid;
        }

        public String getResidentname() {
            return residentname;
        }

        public int getResidentquestionid() {
            return residentquestionid;
        }

        public String getSpeaker() {
            return speaker;
        }
    }
}
