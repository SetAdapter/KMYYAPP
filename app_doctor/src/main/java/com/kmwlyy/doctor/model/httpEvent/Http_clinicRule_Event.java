package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/8/24
 */
public class Http_clinicRule_Event  extends HttpEvent<String> {

    public Http_clinicRule_Event(HttpListener listener) {
        super(listener);
        mReqAction = "/clinicRule/getClinicRuleData";
        mReqMethod = HttpClient.GET;
    }

}