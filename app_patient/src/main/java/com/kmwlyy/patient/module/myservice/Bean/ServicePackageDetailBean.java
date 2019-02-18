package com.kmwlyy.patient.module.myservice.Bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xcj on 2017/8/11.
 */

public class ServicePackageDetailBean {
//        "crowddesc": "一般人群",
//                "currentPage": 1,
//                "pageSize": 10,
//                "remark": "医生给予患者中药处方，患者自行购买药物",
//                "servicedesc": "服务描述",
//                "serviceintro": "基本服务包",
//                "servicename": "基本公共卫生项目包",
//                "servicepkgid": 1,
//                "serviceprice": 50,
//                "servicepricehigh": 60,
//                "servicestatus": 1,
//                "servicetype": 1,
//                "status": 1,
//                "totalbuycount": 2

        @SerializedName("crowddesc")
        public String crowddesc;
        @SerializedName("remark")
        public String remark;
        @SerializedName("servicedesc")
        public String servicedesc;
        @SerializedName("serviceintro")
        public String serviceintro;
        @SerializedName("servicename")
        public String servicename;
        @SerializedName("servicepkgid")
        public String servicepkgid;
        @SerializedName("serviceprice")
        public String serviceprice;
        @SerializedName("imageurl")
        public String imageurl;
        @SerializedName("totalbuycount")
        public String totalbuycount;
}
