package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.ConsultBean;
import com.kmwlyy.doctor.model.ConsultBeanNew;

import java.util.HashMap;
import java.util.List;

/**
 * 图文咨询 列表使用的接口
 */
public class HttpSearchConsultEvent extends HttpEvent<List<ConsultBeanNew>> {
    final String PageSize = "20";

    /**
     * 用于搜索时用的请求
     */
    public HttpSearchConsultEvent(String CurrentPage, String Keyword, int IncludeRemoved, HttpListener<List<ConsultBeanNew>> listener) {
        super(listener);
        mReqAction = "/UserConsults/ConsultMe";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("CurrentPage", CurrentPage);
        mReqParams.put("PageSize", PageSize);
        mReqParams.put("Keyword", Keyword);
        mReqParams.put("IncludeRemoved", "" + IncludeRemoved);
    }

}
