package com.kmwlyy.core.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpCode;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.AppToken;

import java.util.HashMap;

/**
 * Created by Winson on 2016/8/8.
 */
public class HttpGetAppTokenEvent extends HttpEvent<AppToken> {

    public HttpGetAppTokenEvent(HttpListener<AppToken> listener) {
        super(listener);

        mReqAction = "/Token/get";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("appId", HttpCode.APP_ID);
        mReqParams.put("appSecret", HttpCode.APP_SECRET);
    }

}
