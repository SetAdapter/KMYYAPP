package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Winson on 2016/8/13.
 */
public interface Examined {

    class ListItem {

        @SerializedName("ExamId")
        public String mExamId;

        @SerializedName("PersonId")
        public String mPersonId;

        @SerializedName("ExamDate")
        public String mExamDate;

        @SerializedName("ExamType")
        public String mExamType;

        @SerializedName("Status")
        public int mStatus;

        @SerializedName("Doctor")
        public String mDoctor;

        @SerializedName("ExamNo")
        public String mExamNo;

    }

    class Detail {

        @SerializedName("apiUrl")
        public String mApiUrl;

        @SerializedName("person")
        public Person mPerson;

        @SerializedName("examinedList")
        public ArrayList<ExaminedList> mExaminedList;

    }

    class Person {

        @SerializedName("PersonId")
        public String mPersonId;

        @SerializedName("PersonNo")
        public String mPersonNo;

        @SerializedName("RecordNo")
        public String mRecordNo;

        @SerializedName("Name")
        public String mName;

        @SerializedName("Gender")
        public int mGender;

        @SerializedName("BirthDate")
        public String mBirthDate;

        @SerializedName("Country")
        public String mCountry;

        @SerializedName("Nationality")
        public String mNationality;

        @SerializedName("MarriageStatus")
        public String mMarriageStatus;

        @SerializedName("IDType")
        public String mIDType;

        @SerializedName("IDNumber")
        public String mIDNumber;

        @SerializedName("Phone")
        public String mPhone;

        @SerializedName("ContactName")
        public String mContactName;

        @SerializedName("ContactPhone")
        public String mContactPhone;

        @SerializedName("EmailAddress")
        public String mEmailAddress;

        @SerializedName("CensusAddressName")
        public String mCensusAddressName;

        @SerializedName("CurrentAddressName")
        public String mCurrentAddressName;

        @SerializedName("CurrentPostCode")
        public String mCurrentPostCode;

        @SerializedName("HireDate")
        public String mHireDate;

        @SerializedName("RHType")
        public String mRHType;

        @SerializedName("ArchiveDate")
        public String mArchiveDate;

    }

    class ExaminedList {

        @SerializedName("ItemCode")
        public String mItemCode;

        @SerializedName("ItemId")
        public String mItemId;

        @SerializedName("Name")
        public String mName;

        @SerializedName("Result")
        public String mResult;

        @SerializedName("ResultId")
        public String mResultId;

    }

}
