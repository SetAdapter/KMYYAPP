package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.MedicalRecordBean;
import com.kmwlyy.doctor.model.PatientListBean;

import java.util.HashMap;
import java.util.List;

/**
 * 获取待签列表
 */
public class Http_getMedicalRecord_Event extends HttpEvent<List<MedicalRecordBean>> {
    final String PageSize = "20";

    public Http_getMedicalRecord_Event(String CurrentPage,String id, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorMember/GetMyMemberVisitList";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("CurrentPage", CurrentPage);
        mReqParams.put("PageSize", PageSize);
        mReqParams.put("DoctorMemberID", id);
    }

}