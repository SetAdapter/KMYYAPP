package com.kmwlyy.patient.module.myservice.Bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by xcj on 2017/8/14.
 */

public class CouponInfoBean {
    public String msg;
    public String resultCode;
    public ArrayList<CouponInfo> resultData;
    public static class CouponInfo {
//        "begintime": 1502371101000,
//                "couponid": 1,
//                "couponname": "优惠券1",
//                "coupontype": "1",
//                "currentPage": 1,
//                "endtime": 1502975906000,
//                "pageSize": 10,
//                "price": 30,
//                "status": 1,
//                "uselimit": "不限制",
//                "usestatus": 1
        @SerializedName("begintime")
        public String begintime;
        @SerializedName("couponid")
        public String couponid;
        @SerializedName("couponname")
        public String couponname;
        @SerializedName("endtime")
        public String endtime;
        @SerializedName("uselimit")
        public String uselimit;
    }
}
