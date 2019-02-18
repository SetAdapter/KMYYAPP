package com.kmwlyy.patient.module.InhabitantStart;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/8/10.
 */

public class FamilyListBean implements Serializable{


    /**
     * msg : 查询成
     * resultCode : 0
     * resultData : [{"address":"30","area":"112","areacode":"23","birthday":1502121600000,"bloodtype":"B","city":"4124","currentPage":1,"education":"4124","familyhealthno":"33CF5AA3-C96E-4E36-BFF9-82D36956ABFC","gender":1,"idnumber":"151","idtype":55,"imageurl":"25","latitude":33,"longitude":334,"mainresidentid":2,"marriage":1,"mobile":"44","name":"居民2","pageSize":10,"pcacode":123,"postcode":"55","province":"123","residentid":3,"sort":0,"status":1,"terminal":1,"userid":13},{"address":"江苏省常州市天宁区清凉东村17幢丙单元102室","birthday":-770025600000,"createtime":1502187006240,"currentPage":1,"edittime":1502187006240,"familyhealthno":"532A1FA5-F558-4BE4-8E1E-C276BCEF11BD","gender":0,"idnumber":"320303193502231323","idtype":1,"mainresidentid":2,"mobile":"88253551","name":"王琴玉","pageSize":10,"pcacode":320402,"residentid":4,"sort":0,"status":1}]
     */

    private String msg;
    private String resultCode;
    private List<ResultDataBean> resultData;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public void setResultData(List<ResultDataBean> resultData) {
        this.resultData = resultData;
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

    public static class ResultDataBean implements Serializable{
        /**
         * address : 30
         * area : 112
         * areacode : 23
         * birthday : 1502121600000
         * bloodtype : B
         * city : 4124
         * currentPage : 1
         * education : 4124
         * familyhealthno : 33CF5AA3-C96E-4E36-BFF9-82D36956ABFC
         * gender : 1
         * idnumber : 151
         * idtype : 55
         * imageurl : 25
         * latitude : 33.0
         * longitude : 334.0
         * mainresidentid : 2
         * marriage : 1
         * mobile : 44
         * name : 居民2
         * pageSize : 10
         * pcacode : 123
         * postcode : 55
         * province : 123
         * residentid : 3
         * sort : 0
         * status : 1
         * terminal : 1
         * userid : 13
         */

        private String address;
        private String area;
        private String areacode;
        private long birthday;
        private String bloodtype;
        private String city;
        private int currentPage;
        private String education;
        private String familyhealthno;
        private int gender;
        private String idnumber;
        private int idtype;
        private String imageurl;
        private double latitude;
        private double longitude;
        private int mainresidentid;
        private int marriage;
        private String mobile;
        private String name;
        private int pageSize;
        private int pcacode;
        private String postcode;
        private String province;
        private int residentid;
        private int sort;
        private int status;
        private int terminal;
        private int userid;
        private String ownercrowd;
        private String relationshipme;


        public String getOwnercrowd() {
            return ownercrowd;
        }

        public void setOwnercrowd(String ownercrowd) {
            this.ownercrowd = ownercrowd;
        }

        public String getRelationshipme() {
            return relationshipme;
        }

        public void setRelationshipme(String relationshipme) {
            this.relationshipme = relationshipme;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public void setAreacode(String areacode) {
            this.areacode = areacode;
        }

        public void setBirthday(long birthday) {
            this.birthday = birthday;
        }

        public void setBloodtype(String bloodtype) {
            this.bloodtype = bloodtype;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public void setFamilyhealthno(String familyhealthno) {
            this.familyhealthno = familyhealthno;
        }

        public void setGender(int gender) {
            this.gender = gender;
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

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public void setMainresidentid(int mainresidentid) {
            this.mainresidentid = mainresidentid;
        }

        public void setMarriage(int marriage) {
            this.marriage = marriage;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public void setPcacode(int pcacode) {
            this.pcacode = pcacode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setResidentid(int residentid) {
            this.residentid = residentid;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setTerminal(int terminal) {
            this.terminal = terminal;
        }

        public void setUserid(int userid) {
            this.userid = userid;
        }

        public String getAddress() {
            return address;
        }

        public String getArea() {
            return area;
        }

        public String getAreacode() {
            return areacode;
        }

        public long getBirthday() {
            return birthday;
        }

        public String getBloodtype() {
            return bloodtype;
        }

        public String getCity() {
            return city;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public String getEducation() {
            return education;
        }

        public String getFamilyhealthno() {
            return familyhealthno;
        }

        public int getGender() {
            return gender;
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

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public int getMainresidentid() {
            return mainresidentid;
        }

        public int getMarriage() {
            return marriage;
        }

        public String getMobile() {
            return mobile;
        }

        public String getName() {
            return name;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getPcacode() {
            return pcacode;
        }

        public String getPostcode() {
            return postcode;
        }

        public String getProvince() {
            return province;
        }

        public int getResidentid() {
            return residentid;
        }

        public int getSort() {
            return sort;
        }

        public int getStatus() {
            return status;
        }

        public int getTerminal() {
            return terminal;
        }

        public int getUserid() {
            return userid;
        }
    }
}
