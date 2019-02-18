package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xcj on 2016/12/27.
 */

public class CheckUserPackageBean {
    @SerializedName("HasFamilyDoctor")
    public boolean mHasFamilyDoctor;
    @SerializedName("HasUserPackage")
    public boolean mHasUserPackage;
}
