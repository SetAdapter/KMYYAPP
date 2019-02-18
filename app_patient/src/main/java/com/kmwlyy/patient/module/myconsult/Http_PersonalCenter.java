package com.kmwlyy.patient.module.myconsult;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/11.
 */

public interface Http_PersonalCenter {
    /**
     * 个人中心  我的咨询
     */
    class Http_MyConsult extends HttpEvent<MyConsultBean> {
        public Http_MyConsult(String residentid,String currentPage, String pageSize, HttpListener<MyConsultBean> mListener) {
            super(mListener);
            mReqAction = "/webapifamily/getResidentQuestion";
            mReqMethod = HttpClient.POST;
            mReqParams = new HashMap<>();
            noParmName = true;
            mReqParams.put("residentid", residentid);
            mReqParams.put("currentPage", currentPage);
            mReqParams.put("pageSize", pageSize);


            mJsonData = new Gson().toJson(mReqParams).toString();

        }
    }

    /**
     * 我的医生
     */
    class Http_MyDoctor extends HttpEvent<MyDoctorBean> {
        public Http_MyDoctor(String currentPage, String pageSize, HttpListener<MyDoctorBean> mListener) {
            super(mListener);

            mReqAction = "/webapidoctor/getMyDoctorInfo";
            mReqMethod = HttpClient.POST;
            mReqParams = new HashMap<>();
            noParmName = true;
            mReqParams.put("currentPage", currentPage);
            mReqParams.put("pageSize", pageSize);


            mJsonData = new Gson().toJson(mReqParams).toString();
        }

    }
}
