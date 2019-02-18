package com.kmwlyy.core.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.SysICD;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Winson on 2016/8/13.
 */
public class HttpSysICDs extends HttpEvent<ArrayList<SysICD>> {

    public HttpSysICDs(String keyword, String lastUpdateTime, int currentPage, int pageSize, HttpListener<ArrayList<SysICD>> mListener) {
        super(mListener);

        mReqMethod = HttpClient.GET;

        mReqAction = "/SysICDs";

        mReqParams = new HashMap<>();
        mReqParams.put("CurrentPage", "" + currentPage);
        mReqParams.put("PageSize", "" + pageSize);
        mReqParams.put("Keyword", keyword);
        mReqParams.put("LastUpdTime", lastUpdateTime);
    }

}
