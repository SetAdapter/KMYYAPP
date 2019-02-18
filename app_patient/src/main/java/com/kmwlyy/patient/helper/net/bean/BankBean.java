package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xcj on 2016/12/28.
 */

public class BankBean {
    /*"BankID": "",
            "BankName": "",
            "Sort": 0*/
    @SerializedName("BankID")
    public String mBankID;
    @SerializedName("BankName")
    public String mBankName;

}
