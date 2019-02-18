package com.kmwlyy.core.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UserMember;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Winson on 2016/8/10.
 */
public interface HttpUserMember {

    String ACTION = "/UserMembers";

//    class Params {
//
//        public static final int RELATION_SELF = 0;
//        public static final int RELATION_MATE = 1;
//        public static final int RELATION_FATHER = 2;
//        public static final int RELATION_MOTHER = 3;
//        public static final int RELATION_SON = 4;
//        public static final int RELATION_DAUGHTER = 5;
//        public static final int RELATION_OTHER = 6;
//
//        public static final int MARRIAGE_DONE = 0;
//        public static final int MARRIAGE_NONE = 1;
//        public static final int MARRIAGE_UNKONW = 2;
//
//        public static final int GENDER_MAN = 0;
//        public static final int GENDER_WOMEN = 1;
//        public static final int GENDER_UNKONW = 2;
//
//        //        UserID	string
//        //        用户编号
//        public String mUserId;
//
//        //        MemberName
//        public String mMemberName;
//
//        //        Relation	string
//        //        成员关系 （0-自己、1-配偶、2-父亲、3-母亲、4-儿子、5女儿、6-其他）
//        public int mRelation;
//
//        //        Gender	string
//        //        性别（0-男、1-女、2-未知）
//        public int mGender;
//
//        //        Marriage	string
//        //        婚姻状况(0-未婚、1-已婚、2-未知)
//        public int mMarriage;
//
//        //        Birthday	string
//        //        生日
//        public String mBirthday;
//
//        //        Mobile	string
//        //        手机号码
//        public String mMobile;
//
//        //        IDType	string
//        //        证件类型（0-身份证）
//        public int mIDType;
//
//        //        IDNumber	string
//        //        证件号码
//        public String mIDNumber;
//
//        //        Address 可选	string
//        //                地址
//        public String mAddress;
//
//        //        Email 可选	string
//        //                邮箱
//        public String mEmail;
//
//        //        PostCode 可选	string
//        //                邮编
//        public String mPostCode;
//
//        public Params(String memberName, int mRelation, int mGender, int mMarriage, String mBirthday, String mMobile, int mIDType, String mIDNumber) {
//            this.mMemberName = memberName;
//            this.mRelation = mRelation;
//            this.mGender = mGender;
//            this.mMarriage = mMarriage;
//            this.mBirthday = mBirthday;
//            this.mMobile = mMobile;
//            this.mIDType = mIDType;
//            this.mIDNumber = mIDNumber;
//        }
//    }

    class Add extends HttpEvent {

        public Add(UserMember params, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = ACTION;

            if (params != null) {
                mReqParams = new HashMap<>();
//                mReqParams.put("UserID", params.mUserId);
//                "Province" : "省"，
//                "ProvinceRegionID" : "省ID"，
//                "City" : "市"，
//                "CityRegionID" : "市ID"
//                "District" : "区"，
//                "DistrictRegionID" : "区ID"
//                "Town" : "村，街道"，
//                "TownRegionID" : "村，街道ID"
                mReqParams.put("MemberName", params.mMemberName);
                mReqParams.put("Relation", "" + params.mRelation);
                mReqParams.put("Gender", "" + params.mGender);
                mReqParams.put("Marriage", "" + params.mMarriage);
                mReqParams.put("Birthday", params.mBirthday);
                mReqParams.put("Mobile", params.mMobile);
                mReqParams.put("IDType", "" + params.mIDType);
                mReqParams.put("IDNumber", params.mIDNumber);
                mReqParams.put("Email", params.mEmail);
                mReqParams.put("PostCode", params.mPostCode);
                mReqParams.put("Province",params.mProvince);
                mReqParams.put("ProvinceRegionID",params.ProvinceRegionID);
                mReqParams.put("City",params.mCity);
                mReqParams.put("CityRegionID",params.mCityRegionID);
                mReqParams.put("District",params.mDistrict);
                mReqParams.put("DistrictRegionID",params.mDistrictRegionID);
                mReqParams.put("Town",params.mTown);
                mReqParams.put("TownRegionID",params.mTownRegionID);
            }

        }
    }

    class Update extends HttpEvent {

        public Update(UserMember params, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.PUT;

            mReqAction = ACTION;

            if (params != null) {
                mReqParams = new HashMap<>();
                mReqParams.put("UserID", params.mUserId);
                mReqParams.put("MemberID", params.mMemberID);
                mReqParams.put("MemberName", params.mMemberName);
                mReqParams.put("Relation", "" + params.mRelation);
                mReqParams.put("Gender", "" + params.mGender);
                mReqParams.put("Marriage", "" + params.mMarriage);
                mReqParams.put("Birthday", params.mBirthday);
                mReqParams.put("Mobile", params.mMobile);
                mReqParams.put("IDType", "" + params.mIDType);
                mReqParams.put("IDNumber", params.mIDNumber);
                mReqParams.put("Email", params.mEmail);
                mReqParams.put("PostCode", params.mPostCode);
                mReqParams.put("Province",params.mProvince);
                mReqParams.put("ProvinceRegionID",params.ProvinceRegionID);
                mReqParams.put("City",params.mCity);
                mReqParams.put("CityRegionID",params.mCityRegionID);
                mReqParams.put("District",params.mDistrict);
                mReqParams.put("DistrictRegionID",params.mDistrictRegionID);
                mReqParams.put("Town",params.mTown);
                mReqParams.put("TownRegionID",params.mTownRegionID);
                mReqParams.put("Address",params.mAddress);
            }

        }

    }

    class Delete extends HttpEvent {

        public Delete(String memberId, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.DELETE;

            mReqAction = ACTION;
            mUseReqParams = true;
            mReqParams = new HashMap();
            mReqParams.put("ID", memberId);
        }
    }

    class GetList extends HttpEvent<ArrayList<UserMember>> {

        public GetList(int currentPage, int pageSize, HttpListener<ArrayList<UserMember>> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = ACTION;

            mReqParams = new HashMap();
            mReqParams.put("CurrentPage", "" + currentPage);
            mReqParams.put("PageSize", "" + pageSize);

        }

    }

    /**
     * 设置默认成员
     */
    class SetDefault extends HttpEvent {

        public SetDefault(String memberId, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/UserMembers/SetDefault";

            mReqParams = new HashMap();
            mReqParams.put("memberID", memberId);

        }

    }


}
