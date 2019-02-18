package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by xcj on 2016/12/17.
 */

public interface UserMemberEMRsInfo {
    class ListItem {
        @SerializedName("UserMemberEMRID")
        public String mUserMemberEMRID;

        @SerializedName("MemberID")
        public String mMemberID;

        @SerializedName("MemberName")
        public String mMemberName;

        @SerializedName("EMRName")
        public String mEMRName;

        @SerializedName("Date")
        public String mDate;

        @SerializedName("HospitalName")
        public String mHospitalName;

        @SerializedName("Files")
        public ArrayList<File> mFiles;

        @SerializedName("Remark")
        public String mRemark;

        @SerializedName("ModifyTime")
        public String mModifyTime;
    }

    class  File{
        @SerializedName("FileUrl")
        public String mFileUrl;
        @SerializedName("UrlPrefix")
        public String mUrlPrefix;
    }
}
