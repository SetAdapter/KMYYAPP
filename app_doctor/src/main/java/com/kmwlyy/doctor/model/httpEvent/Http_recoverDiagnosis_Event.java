package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

/**
 * Created by xcj on 2017/7/10.
 */

public class Http_recoverDiagnosis_Event extends HttpEvent{
    public Http_recoverDiagnosis_Event(HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorService/RecoverDiagnosis";
        mReqMethod = HttpClient.POST;
        mReqParams = new HashMap<>();
    }
}
