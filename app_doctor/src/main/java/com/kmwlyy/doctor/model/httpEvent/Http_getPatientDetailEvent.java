package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.PatientDetailBean;
import com.kmwlyy.doctor.model.PatientListBean;

import java.util.HashMap;
import java.util.List;

/**
 * 获取待签列表
 */
public class Http_getPatientDetailEvent extends HttpEvent<PatientDetailBean> {

    public Http_getPatientDetailEvent(String id, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorMembers";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("DoctorMemberID", id);
    }

}