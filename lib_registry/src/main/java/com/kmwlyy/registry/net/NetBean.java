package com.kmwlyy.registry.net;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-8-15.
 */
public interface NetBean {
    //区域
    class Area {

        /**
         * AREA_ID : 3317
         * AREA_Name : 福田区
         * PARENT_ID : 5
         * CITY_ID : 5
         * AREA_LEVEL : 4
         */

        private int AREA_ID;
        private String AREA_Name;
        private int PARENT_ID;
        private int CITY_ID;
        private int AREA_LEVEL;

        public int getAREA_ID() {
            return AREA_ID;
        }

        public void setAREA_ID(int AREA_ID) {
            this.AREA_ID = AREA_ID;
        }

        public String getAREA_Name() {
            return AREA_Name;
        }

        public void setAREA_Name(String AREA_Name) {
            this.AREA_Name = AREA_Name;
        }

        public int getPARENT_ID() {
            return PARENT_ID;
        }

        public void setPARENT_ID(int PARENT_ID) {
            this.PARENT_ID = PARENT_ID;
        }

        public int getCITY_ID() {
            return CITY_ID;
        }

        public void setCITY_ID(int CITY_ID) {
            this.CITY_ID = CITY_ID;
        }

        public int getAREA_LEVEL() {
            return AREA_LEVEL;
        }

        public void setAREA_LEVEL(int AREA_LEVEL) {
            this.AREA_LEVEL = AREA_LEVEL;
        }
    }

    class  BranchHospital implements Serializable{
        public String ADDR;//"总院深圳市笋岗西路3002号"
        public String SORT;//1
        public String UNIT_ID;//22
        public String CITY_ID;//5
        public String PHONE;//"0755-83366388"
        public String CLASS_ID;//"100018293"
        public String NAME;//"深圳市第二人民医院"
        public String STATE;//1
    }

    //医院
    class Hospital implements Serializable{

        /**
         * UNIT_ID : 21
         * UNIT_NAME : 北京大学深圳医院
         * AREA_ID : 3317
         * UNIT_SPELL : BJDXSZYY
         * ADDRESS : 广东省深圳市福田莲花路1120号
         * PHONE : 0755-83923333
         * URL : http://www.pkuszh.com/
         * IMAGE : http://images.91160.com/upload/5/unit/2/unit_21.jpg
         * UNIT_ALIAS : 深圳市中心医院,北京大学深圳临床医学院
         * UNIT_LEVEL : B
         * PAY_METHODS : 2
         * LEFT_NUM : 15465
         * PAYMENT : 1
         * CITY_ID : 5
         * UNIT_CLASS : 100138289
         */

        private int UNIT_ID;
        private String UNIT_NAME;
        private int AREA_ID;
        private String UNIT_SPELL;
        private String ADDRESS;
        private String PHONE;
        private String URL;
        private String IMAGE;
        private String UNIT_ALIAS;
        private String UNIT_LEVEL;
        private String PAY_METHODS;
        private String LEFT_NUM;
        private int PAYMENT;
        private int CITY_ID;
        private String UNIT_CLASS;
        private List<BranchHospital> UNIT_SON = new ArrayList<>();

        public int getUNIT_ID() {
            return UNIT_ID;
        }

        public void setUNIT_ID(int UNIT_ID) {
            this.UNIT_ID = UNIT_ID;
        }

        public String getUNIT_NAME() {
            return UNIT_NAME;
        }

        public void setUNIT_NAME(String UNIT_NAME) {
            this.UNIT_NAME = UNIT_NAME;
        }

        public int getAREA_ID() {
            return AREA_ID;
        }

        public void setAREA_ID(int AREA_ID) {
            this.AREA_ID = AREA_ID;
        }

        public String getUNIT_SPELL() {
            return UNIT_SPELL;
        }

        public void setUNIT_SPELL(String UNIT_SPELL) {
            this.UNIT_SPELL = UNIT_SPELL;
        }

        public String getADDRESS() {
            return ADDRESS;
        }

        public void setADDRESS(String ADDRESS) {
            this.ADDRESS = ADDRESS;
        }

        public String getPHONE() {
            return PHONE;
        }

        public void setPHONE(String PHONE) {
            this.PHONE = PHONE;
        }

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }

        public String getIMAGE() {
            return IMAGE;
        }

        public void setIMAGE(String IMAGE) {
            this.IMAGE = IMAGE;
        }

        public String getUNIT_ALIAS() {
            return UNIT_ALIAS;
        }

        public void setUNIT_ALIAS(String UNIT_ALIAS) {
            this.UNIT_ALIAS = UNIT_ALIAS;
        }

        public String getUNIT_LEVEL() {
            return UNIT_LEVEL;
        }

        public void setUNIT_LEVEL(String UNIT_LEVEL) {
            this.UNIT_LEVEL = UNIT_LEVEL;
        }

        public String getPAY_METHODS() {
            return PAY_METHODS;
        }

        public void setPAY_METHODS(String PAY_METHODS) {
            this.PAY_METHODS = PAY_METHODS;
        }

        public String getLEFT_NUM() {
            return LEFT_NUM;
        }

        public void setLEFT_NUM(String LEFT_NUM) {
            this.LEFT_NUM = LEFT_NUM;
        }

        public int getPAYMENT() {
            return PAYMENT;
        }

        public void setPAYMENT(int PAYMENT) {
            this.PAYMENT = PAYMENT;
        }

        public int getCITY_ID() {
            return CITY_ID;
        }

        public void setCITY_ID(int CITY_ID) {
            this.CITY_ID = CITY_ID;
        }

        public String getUNIT_CLASS() {
            return UNIT_CLASS;
        }

        public void setUNIT_CLASS(String UNIT_CLASS) {
            this.UNIT_CLASS = UNIT_CLASS;
        }

        public List<BranchHospital> getUNIT_SON() {
            return UNIT_SON;
        }
    }

    /* 两级的科室信息 */
    class DepartmentTree{
        /**
         * CLASS_ID  200018652
         * PARENT_ID  100000140
         * NAME  其他
         */

        private String CLASS_ID;
        private String PARENT_ID;
        private String NAME;

        private ArrayList<Department> DEP = new ArrayList<>();

        public String getCLASS_ID() {
            return CLASS_ID;
        }

        public String getPARENT_ID() {
            return PARENT_ID;
        }

        public String getNAME() {
            return NAME;
        }

        public void setCLASS_ID(String CLASS_ID) {
            this.CLASS_ID = CLASS_ID;
        }

        public void setPARENT_ID(String PARENT_ID) {
            this.PARENT_ID = PARENT_ID;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public ArrayList<Department> getDepartments() {
            return DEP;
        }
    }

    //科室
    class Department {

        /**
         * DEP_ID : 4249
         * UNIT_ID : 21
         * DEP_NAME : 产前诊断
         * DEP_SPELL : D06
         * CAT_NO : D06
         * LEFT_NUM : 23
         * DEP_INTRO :     北京大学产前诊断中心建立于2008年，并于2009年通过广东省卫生厅评估检查。产前诊断中心是一个新兴的边缘性科学，因目前我国较高的出生缺陷应运而生的 。其工作主要包括针对孕期涉及对胎儿发育有可能影响的某些因素接触病史，进行相关的咨询及和孕妇、家属进行共同商讨。对可疑胎儿有染色体异常进行绒毛、羊水、脐带血染色体检查。对地中海贫血胎儿的基因检查。并将开展基因芯片检测技术用于由于某些多基因突变导致的胎儿异常。对于涉及伦理的疑难问题提供医学伦理讨论意见等。将来逐步开展某些异常胎儿的宫内治疗。
         */

        private int DEP_ID;
        private int UNIT_ID;
        private String DEP_NAME;
        private String DEP_SPELL;
        private String CAT_NO;
        private int LEFT_NUM;
        private String DEP_INTRO;

        public int getDEP_ID() {
            return DEP_ID;
        }

        public void setDEP_ID(int DEP_ID) {
            this.DEP_ID = DEP_ID;
        }

        public int getUNIT_ID() {
            return UNIT_ID;
        }

        public void setUNIT_ID(int UNIT_ID) {
            this.UNIT_ID = UNIT_ID;
        }

        public String getDEP_NAME() {
            return DEP_NAME;
        }

        public void setDEP_NAME(String DEP_NAME) {
            this.DEP_NAME = DEP_NAME;
        }

        public String getDEP_SPELL() {
            return DEP_SPELL;
        }

        public void setDEP_SPELL(String DEP_SPELL) {
            this.DEP_SPELL = DEP_SPELL;
        }

        public String getCAT_NO() {
            return CAT_NO;
        }

        public void setCAT_NO(String CAT_NO) {
            this.CAT_NO = CAT_NO;
        }

        public int getLEFT_NUM() {
            return LEFT_NUM;
        }

        public void setLEFT_NUM(int LEFT_NUM) {
            this.LEFT_NUM = LEFT_NUM;
        }

        public String getDEP_INTRO() {
            return DEP_INTRO;
        }

        public void setDEP_INTRO(String DEP_INTRO) {
            this.DEP_INTRO = DEP_INTRO;
        }
    }

    //医生
    class Doctor implements Serializable{

        /**
         * DOCTOR_ID : 2083
         * DOCTOR_NAME : 邓玉清
         * DOC_SPELL : DYQ
         * SEX : 1
         * EXPERT : 擅长病理妊娠、高危孕产妇的诊疗管理及产前诊断工作
         * ZCID : 1
         * IMAGE : http://images.91160.com/upload/5/201208/13449327511806.jpg
         * DETAIL : 邓玉清，产前诊断中心主任，主任医师，硕士生导师。从事妇产科临床、教学、科研工作24年，广东省健康管理学会妇产科专业委员会委员，广东省妇幼保健协会产前诊断专家库专家，深圳市医学会妇产科分会母胎医学组副组长，深圳市急危重症孕产妇抢救专家组成员。曾赴瑞典Karolinska 大学附属医院和香港中文大学威尔斯亲王医院研修围产医学和产前诊断。擅长病理妊娠、高危孕产妇的诊疗管理及产前诊断工作。主持和参与市级科研项目5个，发表专业论著30余篇，参编医学著作1部。
         * <p/>
         * DEP_ID : 4249
         * DEP_NAME : 产前诊断
         * UNIT_ID : 21
         * UNIT_NAME : 北京大学深圳医院
         * PAY_METHOD : 1
         * PAY_PASS_TIME : 0
         * LEFT_NUM : 10  剩余可预约数量
         */

        private int DOCTOR_ID;
        private String DOCTOR_NAME;
        private String DOC_SPELL;
        private String SEX;
        private String EXPERT;
        private String ZCID;
        private String IMAGE;
        private String DETAIL;
        private int DEP_ID;
        private String DEP_NAME;
        private int UNIT_ID;
        private String UNIT_NAME;
        private int PAY_METHOD;
        private int LEFT_NUM;
        private String PAY_PASS_TIME;

        public int getDOCTOR_ID() {
            return DOCTOR_ID;
        }

        public void setDOCTOR_ID(int DOCTOR_ID) {
            this.DOCTOR_ID = DOCTOR_ID;
        }

        public String getDOCTOR_NAME() {
            return DOCTOR_NAME;
        }

        public void setDOCTOR_NAME(String DOCTOR_NAME) {
            this.DOCTOR_NAME = DOCTOR_NAME;
        }

        public String getDOC_SPELL() {
            return DOC_SPELL;
        }

        public void setDOC_SPELL(String DOC_SPELL) {
            this.DOC_SPELL = DOC_SPELL;
        }

        public String getSEX() {
            return SEX;
        }

        public void setSEX(String SEX) {
            this.SEX = SEX;
        }

        public String getEXPERT() {
            return EXPERT;
        }

        public void setEXPERT(String EXPERT) {
            this.EXPERT = EXPERT;
        }

        public String getZCID() {
            return ZCID;
        }

        public void setZCID(String ZCID) {
            this.ZCID = ZCID;
        }

        public String getIMAGE() {
            return IMAGE;
        }

        public void setIMAGE(String IMAGE) {
            this.IMAGE = IMAGE;
        }

        public String getDETAIL() {
            return DETAIL;
        }

        public void setDETAIL(String DETAIL) {
            this.DETAIL = DETAIL;
        }

        public int getDEP_ID() {
            return DEP_ID;
        }

        public void setDEP_ID(int DEP_ID) {
            this.DEP_ID = DEP_ID;
        }

        public String getDEP_NAME() {
            return DEP_NAME;
        }

        public void setDEP_NAME(String DEP_NAME) {
            this.DEP_NAME = DEP_NAME;
        }

        public int getUNIT_ID() {
            return UNIT_ID;
        }

        public void setUNIT_ID(int UNIT_ID) {
            this.UNIT_ID = UNIT_ID;
        }

        public String getUNIT_NAME() {
            return UNIT_NAME;
        }

        public void setUNIT_NAME(String UNIT_NAME) {
            this.UNIT_NAME = UNIT_NAME;
        }

        public int getPAY_METHOD() {
            return PAY_METHOD;
        }

        public void setPAY_METHOD(int PAY_METHOD) {
            this.PAY_METHOD = PAY_METHOD;
        }

        public String getPAY_PASS_TIME() {
            return PAY_PASS_TIME;
        }

        public void setPAY_PASS_TIME(String PAY_PASS_TIME) {
            this.PAY_PASS_TIME = PAY_PASS_TIME;
        }

        public int getLEFT_NUM() {
            return LEFT_NUM;
        }

        public void setLEFT_NUM(int LEFT_NUM) {
            this.LEFT_NUM = LEFT_NUM;
        }
    }

    //排班
    class Schedule implements Serializable{

        /**
         * SCHEDULE_ID : 114150626
         * DOCTOR_ID : 2083
         * DOCTOR_NAME : 邓玉清
         * TO_DATE : 2016-08-18
         * TIME_TYPE_DESC : 下午
         * LEVEL_NAME : 正高
         * YUYUE_MAX : 10
         * YUYUE_NUM : 10
         * TIME_TYPE : pm
         * GUAHAO_AMT : 23.00
         * LEFT_NUM : 0
         */

        private String SCHEDULE_ID;
        private int DOCTOR_ID;
        private String DOCTOR_NAME;
        private String TO_DATE;
        private String TIME_TYPE_DESC;
        private String LEVEL_NAME;
        private String YUYUE_MAX;
        private String YUYUE_NUM;
        private String TIME_TYPE;
        private String GUAHAO_AMT;
        private String LEFT_NUM;

        public String getSCHEDULE_ID() {
            return SCHEDULE_ID;
        }

        public void setSCHEDULE_ID(String SCHEDULE_ID) {
            this.SCHEDULE_ID = SCHEDULE_ID;
        }

        public int getDOCTOR_ID() {
            return DOCTOR_ID;
        }

        public void setDOCTOR_ID(int DOCTOR_ID) {
            this.DOCTOR_ID = DOCTOR_ID;
        }

        public String getDOCTOR_NAME() {
            return DOCTOR_NAME;
        }

        public void setDOCTOR_NAME(String DOCTOR_NAME) {
            this.DOCTOR_NAME = DOCTOR_NAME;
        }

        public String getTO_DATE() {
            return TO_DATE;
        }

        public void setTO_DATE(String TO_DATE) {
            this.TO_DATE = TO_DATE;
        }

        public String getTIME_TYPE_DESC() {
            return TIME_TYPE_DESC;
        }

        public void setTIME_TYPE_DESC(String TIME_TYPE_DESC) {
            this.TIME_TYPE_DESC = TIME_TYPE_DESC;
        }

        public String getLEVEL_NAME() {
            return LEVEL_NAME;
        }

        public void setLEVEL_NAME(String LEVEL_NAME) {
            this.LEVEL_NAME = LEVEL_NAME;
        }

        public String getYUYUE_MAX() {
            return YUYUE_MAX;
        }

        public void setYUYUE_MAX(String YUYUE_MAX) {
            this.YUYUE_MAX = YUYUE_MAX;
        }

        public String getYUYUE_NUM() {
            return YUYUE_NUM;
        }

        public void setYUYUE_NUM(String YUYUE_NUM) {
            this.YUYUE_NUM = YUYUE_NUM;
        }

        public String getTIME_TYPE() {
            return TIME_TYPE;
        }

        public void setTIME_TYPE(String TIME_TYPE) {
            this.TIME_TYPE = TIME_TYPE;
        }

        public String getGUAHAO_AMT() {
            return GUAHAO_AMT;
        }

        public void setGUAHAO_AMT(String GUAHAO_AMT) {
            this.GUAHAO_AMT = GUAHAO_AMT;
        }

        public String getLEFT_NUM() {
            return LEFT_NUM;
        }

        public void setLEFT_NUM(String LEFT_NUM) {
            this.LEFT_NUM = LEFT_NUM;
        }
    }

    //排班详情
    class SchDetl {

        /**
         * DETL_ID : src:21:4249:2083:2016-08-18:pm:14:00:14:30
         * SCHEDULE_ID : 114150626
         * BEGIN_TIME : 14:00
         * END_TIME : 14:30
         * LEFT_NUM : 0
         */

        private String DETL_ID;
        private String SCHEDULE_ID;
        private String BEGIN_TIME;
        private String END_TIME;
        private int LEFT_NUM;

        public String getDETL_ID() {
            return DETL_ID;
        }

        public void setDETL_ID(String DETL_ID) {
            this.DETL_ID = DETL_ID;
        }

        public String getSCHEDULE_ID() {
            return SCHEDULE_ID;
        }

        public void setSCHEDULE_ID(String SCHEDULE_ID) {
            this.SCHEDULE_ID = SCHEDULE_ID;
        }

        public String getBEGIN_TIME() {
            return BEGIN_TIME;
        }

        public void setBEGIN_TIME(String BEGIN_TIME) {
            this.BEGIN_TIME = BEGIN_TIME;
        }

        public String getEND_TIME() {
            return END_TIME;
        }

        public void setEND_TIME(String END_TIME) {
            this.END_TIME = END_TIME;
        }

        public int getLEFT_NUM() {
            return LEFT_NUM;
        }

        public void setLEFT_NUM(int LEFT_NUM) {
            this.LEFT_NUM = LEFT_NUM;
        }
    }

    //挂号单
    class Order{

        /**
         * OrderNo : 50276102
         * OrderID : 136421728
         * UName : 深圳市罗湖区妇幼保健院
         * DepName : 儿童保健科
         * DoctorName : 夏莺（高危儿门诊）
         * MemberID : 15303FD934B748A6B38AB35B0E9FCF02
         * MemberName : 闫丽萍
         * ToDate : 2016-08-12
         * BeginTime : 16:30
         * EndTime : 17:00
         * OrderState : -1
         * HisTakeNo :
         * GuoHaoAMT : 18
         * Level_Name : 副主任医师
         * Doctor_Image :
         */

        private String OrderNo;
        private String OrderID;
        private String UName;
        private String DepName;
        private String DoctorName;
        private String MemberID;
        private String MemberName;
        private String ToDate;
        private String BeginTime;
        private String EndTime;
        private int OrderState;
        private String HisTakeNo;
        private String GuoHaoAMT;
        private String Level_Name;
        private String Doctor_Image;

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
        }

        public String getOrderID() {
            return OrderID;
        }

        public void setOrderID(String OrderID) {
            this.OrderID = OrderID;
        }

        public String getUName() {
            return UName;
        }

        public void setUName(String UName) {
            this.UName = UName;
        }

        public String getDepName() {
            return DepName;
        }

        public void setDepName(String DepName) {
            this.DepName = DepName;
        }

        public String getDoctorName() {
            return DoctorName;
        }

        public void setDoctorName(String DoctorName) {
            this.DoctorName = DoctorName;
        }

        public String getMemberID() {
            return MemberID;
        }

        public void setMemberID(String MemberID) {
            this.MemberID = MemberID;
        }

        public String getMemberName() {
            return MemberName;
        }

        public void setMemberName(String MemberName) {
            this.MemberName = MemberName;
        }

        public String getToDate() {
            return ToDate;
        }

        public void setToDate(String ToDate) {
            this.ToDate = ToDate;
        }

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

        public int getOrderState() {
            return OrderState;
        }

        public void setOrderState(int OrderState) {
            this.OrderState = OrderState;
        }

        public String getHisTakeNo() {
            return HisTakeNo;
        }

        public void setHisTakeNo(String HisTakeNo) {
            this.HisTakeNo = HisTakeNo;
        }

        public String getGuoHaoAMT() {
            return GuoHaoAMT;
        }

        public void setGuoHaoAMT(String GuoHaoAMT) {
            this.GuoHaoAMT = GuoHaoAMT;
        }

        public String getLevel_Name() {
            return Level_Name;
        }

        public void setLevel_Name(String Level_Name) {
            this.Level_Name = Level_Name;
        }

        public String getDoctor_Image() {
            return Doctor_Image;
        }

        public void setDoctor_Image(String Doctor_Image) {
            this.Doctor_Image = Doctor_Image;
        }
    }

    class HospitalRegistryConfig{
        public Clinc_no_config clinc_no_config;

        public class Clinc_no_config{
            public int only_idcard;//必须要身份证预约 0-否 1是
            public int need_clinc_no;//必须要就诊卡 0-否 1是
            public String clinic_no_name;//就诊卡名称
            public int clinic_no_check;//是否强制 0-否 1是
            public int need_clinic_no_pass;//是否需要就诊卡密码 0-否 1是
            public int is_first_return_visit;//是否要填写初诊，复诊　0-否 1是
            public int can_empty;//0-否 1是

        }

    }
}
