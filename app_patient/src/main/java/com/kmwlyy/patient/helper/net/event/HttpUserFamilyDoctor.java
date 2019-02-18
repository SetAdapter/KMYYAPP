package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.UserFamilyDoctor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Winson on 2016/8/20.
 */
public interface HttpUserFamilyDoctor {

    class Add extends HttpEvent<UserFamilyDoctor.AddResp> {

        public Add(String userId, String doctorId, String Month, HttpListener<UserFamilyDoctor.AddResp> mListener) {
            super(mListener);
//            1	UserID	string	√
//            DoctorID	string	√
//            StartDate	Date		缺省时,为当天
//            2	EndDate	Date		缺省时，为下个月的今天
            mReqMethod = HttpClient.POST;
            mReqAction = "/UserFamilyDoctor/Sumit";
            mReqParams = new HashMap();
           // mReqParams.put("UserID", userId);
            mReqParams.put("DoctorID", doctorId);
            mReqParams.put("Month", Month);
        }

    }

    class GetList extends HttpEvent<ArrayList<UserFamilyDoctor>> {

        public GetList(int page, int pageSize, int isContainExpire, int isContainEnable, HttpListener<ArrayList<UserFamilyDoctor>> mListener) {
            super(mListener);
//            1	page	integer		缺省时，默认1
//            2	pagesize	string		缺省时，默认20
//            IsContainExpire	integer		是否包含过期的家庭医生 1/0
//            包含/不包含
//            缺省时，默认0
//            IsContainEnable	integer		是否包含未支付的家庭医生 1/0
//            包含/不包含
//            缺省时，默认0
            mReqMethod = HttpClient.GET;
            mReqAction = "/UserFamilyDoctor/GetFamilyDoctor";

            mReqParams = new HashMap();
            mReqParams.put("page", "" + page);
            mReqParams.put("pageSize", "" + pageSize);
            mReqParams.put("IsContainExpire", "" + isContainExpire);
            mReqParams.put("IsContainEnable", "" + isContainEnable);

        }

    }

    class Delete extends HttpEvent {
        public Delete(String userId, String doctorId, String startDate, HttpListener mListener) {
            super(mListener);
//            1	UserID	string	√
//            DoctorID	string	√
//            2	StartDate	Date		不为空，则只删除开始时间与之匹配的记录
            mReqMethod = HttpClient.POST;
            mReqAction = "/UserFamilyDoctor/Delete";

            mReqParams = new HashMap();
            mReqParams.put("UserID", userId);
            mReqParams.put("DoctorID", doctorId);
            mReqParams.put("StartDate", startDate);
        }
    }

    class GetDoctorServiceInfo extends HttpEvent<UserFamilyDoctor.ServiceInfo> {

        public GetDoctorServiceInfo(String doctorId, HttpListener<UserFamilyDoctor.ServiceInfo> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/UserFamilyDoctor/GetDoctorServiceInfoAll";

            mReqParams = new HashMap();
            mReqParams.put("doctorID", doctorId);
        }

    }

}
