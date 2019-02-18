package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/9
 */
public class HealthInformationBean implements Serializable {
    @SerializedName("ID")
    public String id;
    @SerializedName("Title")
    public String title;
    @SerializedName("LastModifiedTime")
    public String lastModifiedTime;
    public String formatDate;
    @SerializedName("MainImage")
    public String imageUrl;
    @SerializedName("ReadingQuantity")
    public String readingQuantity = "0";
    @SerializedName("URL")
    public String url = "";

}
