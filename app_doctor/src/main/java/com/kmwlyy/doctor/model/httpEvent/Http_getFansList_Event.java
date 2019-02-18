package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.FansListBean;
import com.kmwlyy.doctor.model.PatientListBean;

import java.util.HashMap;
import java.util.List;

/**
 * 获取患者列表
 */
public class Http_getFansList_Event extends HttpEvent<List<FansListBean>> {
    final String PageSize = "20";

    public Http_getFansList_Event(String CurrentPage, HttpListener listener) {
        super(listener);
        mReqAction = "/Doctors/GetAttentions";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("CurrentPage", CurrentPage);
        mReqParams.put("PageSize", PageSize);
    }

}