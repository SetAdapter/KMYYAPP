package com.kmwlyy.doctor.model.httpEvent;

import android.content.Context;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.model.EvaluateListBean;
import com.kmwlyy.doctor.model.FansListBean;

import java.util.HashMap;
import java.util.List;

/**
 * 获取患者列表
 */
public class Http_getEvaluateList_Event extends HttpEvent<List<EvaluateListBean>> {
    final String PageSize = "20";

    public Http_getEvaluateList_Event(String CurrentPage, Context mContext, HttpListener listener) {
        super(listener);
        mReqAction = "/ServiceEvaluations/Query";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("ProviderID", SPUtils.get(mContext, SPUtils.DOCTOR_ID, "").toString());
        mReqParams.put("CurrentPage", CurrentPage);
        mReqParams.put("PageSize", PageSize);
    }

    public Http_getEvaluateList_Event(String CurrentPage, Context mContext, String TagName,HttpListener listener) {
        super(listener);
        mReqAction = "/ServiceEvaluations/Query";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("ProviderID", SPUtils.get(mContext, SPUtils.DOCTOR_ID, "").toString());
        mReqParams.put("CurrentPage", CurrentPage);
        mReqParams.put("PageSize", PageSize);
        mReqParams.put("EvaluationTag", TagName);
    }

}