package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by xcj on 2016/8/22.
 */
public class LogisticInfo {
//    "company": {
//        "logis_company": "康美物流",
//                "logis_num": "",
//                "order_id": "TD16070400010"
//    },
//            "list": [
//    {
//        "id": 900085,
//            "current_lo_time": "2016-07-04 16:09:06",
//            "addr_info": "您的订单已审核！"
//    }
//    ]
    @SerializedName("company")
    public Company mCompany;
    @SerializedName("list")
    public ArrayList<LogisticList> mLogisticList;

    public static class Company {

       /* "logis_company": "康美物流",
                "logis_num": "",
                "order_id": "TD16070400010"*/
        @SerializedName("logis_company")
        public String mLogisCompany;
        @SerializedName("logis_num")
        public String mLogisNum;
        @SerializedName("order_id")
        public String mOrderId;
    }

    public static class LogisticList {
//        "id": 900085,
//                "current_lo_time": "2016-07-04 16:09:06",
//                "addr_info": "您的订单已审核！"
        @SerializedName("current_lo_time")
        public String mTime;
        @SerializedName("addr_info")
        public String mAddrInfo;

    }
}
