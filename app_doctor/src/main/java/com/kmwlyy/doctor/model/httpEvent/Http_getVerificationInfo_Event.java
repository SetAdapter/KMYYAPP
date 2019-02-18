package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.VerificationInfoBean;

import java.util.HashMap;

/**
 * Created by xcj on 2016/11/1.
 */
public class Http_getVerificationInfo_Event extends HttpEvent<VerificationInfoBean> {
    public Http_getVerificationInfo_Event( HttpListener<VerificationInfoBean> listener) {
        super(listener);
        mReqAction = "/DoctorVerifications/GetVerificationInfo";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
    }
}
