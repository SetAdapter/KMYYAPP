package com.kmwlyy.doctor.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Winson on 2017/7/5.
 */

public class BannerItem {

    @SerializedName("BannerName")
    public String bannerName;//":"Banner名称", //banner名称

    @SerializedName("ImgUrl")
    public String imgUrl;//":"images/d2066052c296a7cbb41ff1cbc830b64c", //图片地址

    @SerializedName("UrlPrefix")
    public String urlPrefix;//":"https://store.kmwlyy.com/", //图片服务器地址

    @SerializedName("TargetUrl")
    public String targetUrl;//":"www.kmwlyy.com", //跳转链接

    @SerializedName("SliderTime")
    public long sliderTime;//":1000             //轮播时间(毫秒)

}
