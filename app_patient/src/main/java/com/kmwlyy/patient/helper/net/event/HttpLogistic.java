package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.LogisticInfo;

import java.util.HashMap;

/**
 * Created by xcj on 2016/8/22.
 */
public interface HttpLogistic {
    class GetDetail extends HttpEvent<LogisticInfo> {

        public GetDetail(String logistic, HttpListener<LogisticInfo> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/Orders/LogisticInfo";
            mReqParams = new HashMap();
            mReqParams.put("LogisticNo", logistic);

        }



    }
}
