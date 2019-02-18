package com.kmwlyy.doctor.model.httpEvent;

import android.content.Context;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.model.EvaluateTagBean;
import com.kmwlyy.doctor.model.FansListBean;

import java.util.HashMap;
import java.util.List;

/**
 * 获取患者列表
 */
public class Http_getEvaluateTag_Event extends HttpEvent<List<EvaluateTagBean>> {

    public Http_getEvaluateTag_Event(String id,HttpListener listener) {
        super(listener);
        mReqAction = "/ServiceEvaluations/GetServiceProviderEvaluatedTags";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("ProviderID", id);
    }

}