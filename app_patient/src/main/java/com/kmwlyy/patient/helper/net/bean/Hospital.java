package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Winson on 2016/8/12.
 */
public interface Hospital {

    class ListItem {
        @SerializedName("HospitalID")
        public String HospitalID;

        @SerializedName("HospitHospitalNamealID")
        public String mHospitHospitalNamealID;

        @SerializedName("Intro")
        public String mIntro;

        @SerializedName("License")
        public String mLicense;

        @SerializedName("LogoUrl")
        public String mLogoUrl;

        @SerializedName("Address")
        public String mAddress;

        @SerializedName("PostCode")
        public String mPostCode;

        @SerializedName("Telephone")
        public String mTelephone;

        @SerializedName("Email")
        public String mEmail;

        @SerializedName("ImageUrl")
        public String mImageUrl;

        @SerializedName("DepartmentCount")
        public int mDepartmentCount;

        @SerializedName("DoctorCount")
        public int mDoctorCount;
    }

    class Detail {
        //    "HospitalID": "42FF1C61132E443F862510FF3BC3B03A",
//            "HospitalName": "康美医院",
//            "Intro": "
//    康美医院是由广东省卫生厅批准设立，中国制药百强企业、广东省百强民营企业、上海证券交易所上市公司康美药业股份有限公司（股票代码600518）投资创办的第一家大型非营利性综合医院。医院位于粤东商贸名城、全国著名中药材集散地、著名侨乡普宁市中心区，周围环境优美，交通便利，人口稠密，客商云集。医院按照三级甲等医院标准规划设计，占地面积100余亩，其中医疗区50多亩，医用建筑面积近12万平方米，一期可开放床位500张。\n\n
//    医院功能完善，学科齐全，批准设立的一级学科有急诊科、内科、外科、妇产科、儿科、眼科、耳鼻喉科、口腔科、皮肤科、中医科、麻醉科、重症医学科、预防保健科等。内科系统计划开设的二级学科有呼吸内科、消化内科、心血管内科、神经内科、内分泌科、血液病科、肿瘤科等。外科系统计划开设的二级学科有普外科、骨科、神经外科、胸外科、泌尿外科、烧伤整形科、肛肠科等。开设的医技科室有影像科、检验科、超声科、功能科、病理科、药剂科、理疗科等。根据潮汕地区疾病谱和社会医疗需求，医院重点发展神经科、心血管科、肿瘤科、创伤外科、妇产科等优势学科。\n\n
//    医院致力于打造粤东地区设备一流、技术一流、服务一流的地市级区域医疗中心，已经配置的医疗设备有西门子128层高配螺旋CT、1.5T高配核磁共振成像系统（MRI）、平板数字化多功能X线诊断系统、移动式数字化X线拍片系统、三维C臂X线成像系统（术中CT）、全数字化平板乳腺X线摄影系统、ACUSON-SC2000彩超、四维彩超，贝克曼高端全自动生化分析仪、全自动化学发光仪等大批高端设备。\n\n
//    医院设有18间层流净化手术室、20张床位的重症监护中心（ICU）、装备32台透析机的血液透析中心，高标准产房和内镜中心等。\n\n
//    医院立足当地，面向全国，引进大批优秀的专业技术人才，与北京、上海、广州、汕头等地著名医院和高等医学院校建立科研、教学、远程会诊、双向转诊等业务合作关系，聘请省内外著名专家、教授定期来院讲学、会诊、手术和技术指导。\n\n
//    康美医院以服务社会、提高周边地区人民群众医疗保健水平为己任，秉承“心怀苍生，大爱无疆”核心价值观，借鉴国内外先进的医院管理模式，引进JCI国际认证体系，坚持以人才和技术为依托，以病人健康需求为中心，以医疗质量和医疗安全为核心的办院理念，注重内涵建设，优化服务流程，实施人性化服务，竭诚为普宁市和周边人民群众提供技术精湛、服务温馨、价格低廉、优质高效的医疗服务，倾情打造业界认可，政府信任，患者满意、诚信仁爱的康美医疗品牌。\n\n
//    ",
//            "License": "YYZZ000001",
//            "LogoUrl": "/Uploads/hospital/201509/151815495260.png",
//            "Address": "广东省普宁市流沙新河西路38号",
//            "PostCode": "515300",
//            "Telephone": "(0663)2229222",
//            "Email": "km@kmlove.com.cn",
//            "ImageUrl": "/Uploads/hospital/201512/1.jpg",
//            "DepartmentCount": 0,
//            "DoctorCount": 0,
//            "Departments": []

        @SerializedName("HospitalID")
        public String mHospitalID;

        @SerializedName("HospitalName")
        public String mHospitalName;

        @SerializedName("Intro")
        public String mIntro;

        @SerializedName("License")
        public String mLicense;

        @SerializedName("LogoUrl")
        public String mLogoUrl;

        @SerializedName("Address")
        public String mAddress;

        @SerializedName("Telephone")
        public String mTelephone;

        @SerializedName("Email")
        public String mEmail;

        @SerializedName("ImageUrl")
        public String mImageUrl;

        @SerializedName("DepartmentCount")
        public int mDepartmentCount;

        @SerializedName("DoctorCount")
        public int mDoctorCount;

//    @SerializedName("Departments")
//    public String mDepartments;
    }


}
