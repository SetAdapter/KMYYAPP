package com.kmwlyy.core.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.DoctorInfo;
import com.kmwlyy.core.net.bean.PatientInfo;

import java.util.HashMap;

/**
 * Created by xcj on 2016/10/23.
 */
public interface HttpUserInfo {
    //获得患者个人资料
    class GetPatientInfo extends HttpEvent<PatientInfo> {

        public GetPatientInfo(HttpListener<PatientInfo> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/users/GetUserInfo";

            mReqParams = new HashMap();

        }

    }

    //获得医生个人资料
    class GetDoctorInfo extends HttpEvent<DoctorInfo> {

        public GetDoctorInfo(String type, String id , HttpListener<DoctorInfo> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/Doctors";

            mReqParams = new HashMap();

            mReqParams.put(type, id);

        }

    }

    //更新患者个人资料
    class PostPatientInfo extends HttpEvent {

        public PostPatientInfo(String type, String content , HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/Users/UpdateUserInfo";

            mReqParams = new HashMap();

            mReqParams.put(type, content);

        }

    }

    //更新医生个人资料
    class PostDoctorInfo extends HttpEvent {

        public PostDoctorInfo(String type, String content , HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/Doctors/UpdateDoctorInfo";

            mReqParams = new HashMap();

            mReqParams.put(type, content);

        }

    }
}
