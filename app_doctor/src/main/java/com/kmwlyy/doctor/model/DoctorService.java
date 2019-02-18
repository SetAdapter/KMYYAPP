package com.kmwlyy.doctor.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Winson on 2017/7/8.
 */

public class DoctorService {

    @SerializedName("PicNoReplyCount")
    public int picNoReplyCount;//:2, //图文数

    @SerializedName("AudCount")
    public int audCount;//:3, //语音数

    @SerializedName("VidCount")
    public int vidCount;//:1 //视频数

}
