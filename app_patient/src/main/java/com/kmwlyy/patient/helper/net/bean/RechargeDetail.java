package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xcj on 2016/12/17.
 */

public class RechargeDetail {
    /*"OrderNo": "",
            "TotalFee": 100,
            "Body": "",
            "Subject": ""*/
    @SerializedName("OrderNo")
    public String mOrderNo;
    @SerializedName("TotalFee")
    public double mTotalFee;
    @SerializedName("Body")
    public String mBody;
    @SerializedName("Subject")
    public String mSubject;

}
