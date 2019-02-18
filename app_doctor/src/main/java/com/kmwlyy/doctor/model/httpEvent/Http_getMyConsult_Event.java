package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.ConsultBean;

import java.util.HashMap;
import java.util.List;

/**
 * 图文咨询 列表使用的接口
 */
public class Http_getMyConsult_Event extends HttpEvent<List<ConsultBean>> {
    final String PageSize = "20";

    /**
     * 图文订单调这个，显示所有数据
     * @param CurrentPage
     * @param listener
     */
    public Http_getMyConsult_Event(String CurrentPage,HttpListener listener) {
        super(listener);
        mReqAction = "/UserConsults/ConsultMe";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("CurrentPage", CurrentPage);
        mReqParams.put("PageSize", PageSize);
        mReqParams.put("OrderType",1+"");
    }

    /**
     * 诊室图文订单调这个，显示当天数据
     * @param CurrentPage
     * @param listener
     */
    public Http_getMyConsult_Event(String CurrentPage, int IncludeRemoved,HttpListener listener) {
        super(listener);
        mReqAction = "/UserConsults/ConsultMe";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("CurrentPage", CurrentPage);
        mReqParams.put("PageSize", PageSize);
        mReqParams.put("IncludeRemoved", ""+IncludeRemoved);
    }

    /**
     * 用于搜索时用的请求
     */
    public Http_getMyConsult_Event(String CurrentPage, String Keyword, int IncludeRemoved,HttpListener listener) {
        super(listener);
        mReqAction = "/UserConsults/ConsultMe";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("CurrentPage", CurrentPage);
        mReqParams.put("PageSize", PageSize);
        mReqParams.put("Keyword", Keyword);
        mReqParams.put("IncludeRemoved", ""+IncludeRemoved);
    }


    public Http_getMyConsult_Event(int consultType, String CurrentPage, String Keyword, HttpListener listener) {
        super(listener);
        mReqAction = "/UserConsults/ConsultMe";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("CurrentPage", CurrentPage);
        mReqParams.put("PageSize", PageSize);
        mReqParams.put("Keyword", Keyword);
        mReqParams.put("ConsultType", "1");
        mReqParams.put("IncludeRemoved", "0");
    }

}
