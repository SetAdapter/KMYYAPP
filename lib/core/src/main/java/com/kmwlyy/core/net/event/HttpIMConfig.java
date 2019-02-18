package com.kmwlyy.core.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.IMConfig;

/**
 * Created by Winson on 2016/8/13.
 */
public class HttpIMConfig extends HttpEvent<IMConfig> {

    public HttpIMConfig(HttpListener<IMConfig> mListener) {
        super(mListener);
        mReqMethod = HttpClient.GET;
        mReqAction = "/IM/Config";
    }

}
