package com.kmwlyy.core.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Winson on 2016/8/13.
 */
public class SysICD {

    @SerializedName("ICDID")
    public String mICDID;

    @SerializedName("DiseaseCode")
    public String mDiseaseCode;

    @SerializedName("DiseaseName")
    public String mDiseaseName;

    @SerializedName("DiseaseEnName")
    public String mDiseaseEnName;

    @SerializedName("PinYinCode")
    public String mPinYinCode;

    @SerializedName("CategoryCode")
    public String mCategoryCode;

    @SerializedName("ICDType")
    public int mICDType;

}
