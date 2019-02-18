package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.PatientListBean;
import com.kmwlyy.doctor.model.UnSignRecipeBean;

import java.util.HashMap;
import java.util.List;

/**
 * 获取患者列表
 */
public class Http_getPatientList_Event extends HttpEvent<List<PatientListBean>> {
    final String PageSize = "20";

    public Http_getPatientList_Event(String CurrentPage,String Keyword, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorMembers";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("CurrentPage", CurrentPage);
        mReqParams.put("PageSize", PageSize);
        mReqParams.put("Keyword", Keyword );
    }

}