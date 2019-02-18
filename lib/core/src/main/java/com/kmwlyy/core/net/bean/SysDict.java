package com.kmwlyy.core.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Winson on 2016/8/13.
 */
public class SysDict {

    @SerializedName("DicID")
    public String mDicID;

    @SerializedName("DictTypeID")
    public String mDictTypeID;

    @SerializedName("DicCode")
    public String mDicCode;

    @SerializedName("CNName")
    public String mCNName;

    @SerializedName("ENName")
    public String mENName;

    @SerializedName("OrderNo")
    public String mOrderNo;

    @SerializedName("Remark")
    public String mRemark;

}
