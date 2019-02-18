package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.FreeCalendarBean;

import java.util.HashMap;

/**
 * 获取义诊
 */
public class Http_getFreeCalender_Event extends HttpEvent<FreeCalendarBean> {

    public Http_getFreeCalender_Event(String month,HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorClinics";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("ClinicMonth", month);
    }

}