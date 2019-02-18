package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/29
 */
public class CreateOrderBean implements Serializable{
    @SerializedName("OrderState")
    public int mOrderState;
    @SerializedName("OrderNo")
    public String mOrderNo;
}
