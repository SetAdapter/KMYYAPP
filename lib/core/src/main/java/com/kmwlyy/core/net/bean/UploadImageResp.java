package com.kmwlyy.core.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Winson on 2016/9/1.
 */
public class UploadImageResp {

    @SerializedName("UrlPrefix")
    public String mUrlPrefix;

    @SerializedName("FileName")
    public String mFileName;

    @SerializedName("MD5")
    public String md5 = "";

}
