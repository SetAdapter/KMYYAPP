package com.kmwlyy.patient.module.myconsult;

import java.util.List;

/**
 * Created by Administrator on 2017/8/14.
 */

public class MyDoctorBean {


    /**
     * basePaginator : {"currentPage":1,"pageSize":5,"totalCount":2,"totalPage":1}
     * msg : 返回成功
     * resultCode : 0
     * resultData : [{"address":"北京市西城区前门西河沿街202号","doctorid":48,"doctorname":"李建峰","gender":0,"hospitaldeptid":14,"hospitalid":208,"idnumber":"440304198511080074","idtype":1,"imageurl":"暂无图片详细","intro":"<p>全科医生，执业医师，擅长临床常见病多发病的诊疗，老年保健，慢病管理。<\/p>","isconsultation":false,"isexpert":false,"marriage":0,"postcode":"518108","sort":0,"specialty":"全科诊疗 老年保健 慢病管理","status":1,"title":"主任医师"},{"address":"深圳市福田区福强路3012号","doctorid":49,"doctorname":"测试医生417","gender":0,"hospitalid":215,"idnumber":"123456789012345678","idtype":1,"imageurl":"暂无图片详细","intro":"<p>妇科专家-妇幼保健-深圳市妇幼保健院<!--StartFragment-->主任医师，教授，硕士研究生导师。毕业于山东大学医学院，博士研究生。<br />\r\n从事临床工作23年，广东省数字医学会妇产科分会委员。擅长妇科常见性疾病的诊治，特别在妇科肿瘤、妇科内分泌和妇科腔镜及妇科疑难杂症等方面有丰富的临床经验。<!--EndFragment--><\/p>","isconsultation":false,"isexpert":false,"marriage":0,"postcode":"518108","sort":0,"specialty":"高血压 冠心病 其他","status":1,"title":"副主任医师"}]
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
         * totalCount : 2
         * totalPage : 1
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
         * address : 北京市西城区前门西河沿街202号
         * doctorid : 48
         * doctorname : 李建峰
         * gender : 0
         * hospitaldeptid : 14
         * hospitalid : 208
         * idnumber : 440304198511080074
         * idtype : 1
         * imageurl : 暂无图片详细
         * intro : <p>全科医生，执业医师，擅长临床常见病多发病的诊疗，老年保健，慢病管理。</p>
         * isconsultation : false
         * isexpert : false
         * marriage : 0
         * postcode : 518108
         * sort : 0
         * specialty : 全科诊疗 老年保健 慢病管理
         * status : 1
         * title : 主任医师
         */

        private String address;
        private int doctorid;
        private String doctorname;
        private int gender;
        private int hospitaldeptid;
        private int hospitalid;
        private String idnumber;
        private int idtype;
        private String imageurl;
        private String intro;
        private boolean isconsultation;
        private boolean isexpert;
        private int marriage;
        private String postcode;
        private int sort;
        private String specialty;
        private int status;
        private String title;

        public void setAddress(String address) {
            this.address = address;
        }

        public void setDoctorid(int doctorid) {
            this.doctorid = doctorid;
        }

        public void setDoctorname(String doctorname) {
            this.doctorname = doctorname;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public void setHospitaldeptid(int hospitaldeptid) {
            this.hospitaldeptid = hospitaldeptid;
        }

        public void setHospitalid(int hospitalid) {
            this.hospitalid = hospitalid;
        }

        public void setIdnumber(String idnumber) {
            this.idnumber = idnumber;
        }

        public void setIdtype(int idtype) {
            this.idtype = idtype;
        }

        public void setImageurl(String imageurl) {
            this.imageurl = imageurl;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public void setIsconsultation(boolean isconsultation) {
            this.isconsultation = isconsultation;
        }

        public void setIsexpert(boolean isexpert) {
            this.isexpert = isexpert;
        }

        public void setMarriage(int marriage) {
            this.marriage = marriage;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public void setSpecialty(String specialty) {
            this.specialty = specialty;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public int getDoctorid() {
            return doctorid;
        }

        public String getDoctorname() {
            return doctorname;
        }

        public int getGender() {
            return gender;
        }

        public int getHospitaldeptid() {
            return hospitaldeptid;
        }

        public int getHospitalid() {
            return hospitalid;
        }

        public String getIdnumber() {
            return idnumber;
        }

        public int getIdtype() {
            return idtype;
        }

        public String getImageurl() {
            return imageurl;
        }

        public String getIntro() {
            return intro;
        }

        public boolean getIsconsultation() {
            return isconsultation;
        }

        public boolean getIsexpert() {
            return isexpert;
        }

        public int getMarriage() {
            return marriage;
        }

        public String getPostcode() {
            return postcode;
        }

        public int getSort() {
            return sort;
        }

        public String getSpecialty() {
            return specialty;
        }

        public int getStatus() {
            return status;
        }

        public String getTitle() {
            return title;
        }
    }
}
