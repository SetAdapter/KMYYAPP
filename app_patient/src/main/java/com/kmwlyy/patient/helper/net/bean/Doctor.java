package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Winson on 2016/8/12.
 */
public interface Doctor {

    class Detail implements Serializable{
        @SerializedName("DoctorID")
        public String mDoctorID;

        @SerializedName("DoctorName")
        public String mDoctorName;

        @SerializedName("UserID")
        public String mUserID;

        @SerializedName("Gender")
        public String mGender;

        @SerializedName("Marriage")
        public String mMarriage;

        @SerializedName("Birthday")
        public String mBirthday;

        @SerializedName("IDType")
        public String mIDType;

        @SerializedName("IDNumber")
        public String mIDNumber;

        @SerializedName("Address")
        public String mAddress;

        @SerializedName("PostCode")
        public String mPostCode;

        @SerializedName("Intro")
        public String mIntro;

        @SerializedName("IsConsultation")
        public String mIsConsultation;

        @SerializedName("IsExpert")
        public String mIsExpert;

        @SerializedName("Specialty")
        public String mSpecialty;

        @SerializedName("areaCode")
        public String mAreaCode;

        @SerializedName("HospitalID")
        public String mHospitalID;

        @SerializedName("HospitalName")
        public String mHospitalName;

        @SerializedName("Grade")
        public String mGrade;

        @SerializedName("DepartmentID")
        public String mDepartmentID;

        @SerializedName("DepartmentName")
        public String mDepartmentName;

        @SerializedName("Education")
        public String mEducation;

        @SerializedName("Title")
        public String mTitle;

        @SerializedName("Duties")
        public String mDuties;

        @SerializedName("CheckState")
        public String mCheckState;

        @SerializedName("SignatureURL")
        public String mSignatureURL;

        @SerializedName("Sort")
        public String mSort;

        @SerializedName("User")
        public User mUser;

        @SerializedName("DoctorServices")
        public ArrayList<DoctorService> mDoctorServices;

        @SerializedName("DiagnoseNum")
        public int mDiagnoseNum;//问诊量

        @SerializedName("FollowNum")
        public int mFollowNum;//关注量

        @SerializedName("IsFollowed")
        public boolean mIsFollowed;//是否关注

    }


    class ListItem implements Serializable {

//                "DoctorID":"4FDADA2DD7E3450CAEC78E9CA407BF06",
//                "DoctorName":"向金龙",
//                "Gender":0,
//                "Marriage":0,
//                "Birthday":"19850808",
//                "IDType":0,
//                "Address":"深圳市福田区国际创新中心A座8楼",
//                "IsConsultation":false,
//                "IsExpert":false,
//                "areaCode":"",
//                "HospitalID":"42FF1C61132E443F862510FF3BC3B03A",
//                "HospitalName":"康美医院",
//                "DepartmentID":"BCE87580389041A0A70F9465F305BBC2",
//                "DepartmentName":"全科",
//                "Duties":"",
//                "CheckState":0,
//                "Sort":0,
//                "User":{

//                 }

        @SerializedName("Title")
        public String mTitle;

        @SerializedName("DoctorID")
        public String mDoctorID;

        @SerializedName("DoctorName")
        public String mDoctorName;

        @SerializedName("UserID")
        public String mUserID;

        @SerializedName("Gender")
        public String mGender;

        @SerializedName("Marriage")
        public String mMarriage;

        @SerializedName("Birthday")
        public String mBirthday;

        @SerializedName("IDType")
        public String mIDType;

        @SerializedName("Address")
        public String mAddress;

        @SerializedName("IsConsultation")
        public String mIsConsultation;

        @SerializedName("IsExpert")
        public String mIsExpert;

        @SerializedName("areaCode")
        public String mAreaCode;

        @SerializedName("HospitalID")
        public String mHospitalID;

        @SerializedName("HospitalName")
        public String mHospitalName;

        @SerializedName("DepartmentID")
        public String mDepartmentID;

        @SerializedName("DepartmentName")
        public String mDepartmentName;

        @SerializedName("Duties")
        public String mDuties;

        @SerializedName("CheckState")
        public String mCheckState;

        @SerializedName("Sort")
        public int mSort;

        @SerializedName("DiagnoseNum")
        public int mDiagnoseNum;//问诊量

        @SerializedName("FollowNum")
        public int mFollowNum;//关注量

        @SerializedName("User")
        public User mUser;

        @SerializedName("Specialty")
        public String mSpecialty;

        @SerializedName("DoctorServices")
        public ArrayList<DoctorService> mDoctorServices;

        @SerializedName("Intro")
        public String mIntro;

    }

    class DoctorService implements Serializable {
        //    "DoctorServices": [
//    {
//        "ServiceType": 3,//视频咨询
//            "ServiceSwitch": 1,//开启
//            "ServicePrice": 8//价格（单位：元）
//    },
//    {
//        "ServiceType": 2,//语音咨询
//            "ServiceSwitch": 1,
//            "ServicePrice": 5
//    },
//    {
//        "ServiceType": 4,//家庭医生
//            "ServiceSwitch": 1,
//            "ServicePrice": 7
//    },
//    {
//        "ServiceType": 1,//图文咨询
//            "ServiceSwitch": 1,
//            "ServicePrice": 1
//    }
//    ],

        public static final int IMAGE_CONSULT = 1;
        public static final int IM_CONSULT = 2;
        public static final int VIDEO_CONSULT = 3;
        public static final int FAMILY_DOCTOR_CONSULT = 4;

        public static final int SERVICE_ON = 1;
        public static final int SERVICE_OFF = 2;


        @SerializedName("ServiceType")
        public int mServiceType;

        @SerializedName("ServiceSwitch")
        public int mServiceSwitch;

        @SerializedName("ServicePrice")
        public float mServicePrice;

    }


    class User implements Serializable {

//                        "UserID":"5E5E4318744248E99C18A71B8774E2E9",
//                        "UserType":0,
//                        "PhotoUrl":"http://www.kmwlyy.com///Uploads/doctor/xiangjinlong.jpg",
//                        "Score":0,
//                        "Star":0,
//                        "Comment":0,
//                        "Good":0,
//                        "Fans":0,
//                        "Grade":0,
//                        "Checked":0,
//                        "RegTime":"0001-01-01T00:00:00",
//                        "CancelTime":"0001-01-01T00:00:00",
//                        "UserState":0,
//                        "UserLevel":0,
//                        "Terminal":0,
//                        "LastTime":"0001-01-01T00:00:00",
//                        "identifier":0


        @SerializedName("UserID")
        public String mUserID;

        @SerializedName("UserType")
        public int mUserType;

        @SerializedName("PhotoUrl")
        public String mPhotoUrl;

        @SerializedName("Score")
        public int mScore;

        @SerializedName("Star")
        public long mStar;

        @SerializedName("Comment")
        public long mComment;

        @SerializedName("Good")
        public long mGood;

        @SerializedName("Fans")
        public long mFans;

        @SerializedName("Grade")
        public long mGrade;

        @SerializedName("Checked")
        public int mChecked;

        @SerializedName("RegTime")
        public String mRegTime;

        @SerializedName("CancelTime")
        public String mCancelTime;

        @SerializedName("UserState")
        public int mUserState;

        @SerializedName("UserLevel")
        public int mUserLevel;

        @SerializedName("Terminal")
        public int mTerminal;

        @SerializedName("LastTime")
        public String mLastTime;

        @SerializedName("identifier")
        public int mIdentifier;

    }


}
