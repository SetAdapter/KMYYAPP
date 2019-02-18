package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.WorkTimeBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by xcj on 2017/7/10.
 */

public class Http_getWorkingTimeBase_Event extends HttpEvent<List<WorkTimeBean>>{
    public Http_getWorkingTimeBase_Event(HttpListener<List<WorkTimeBean>> listener) {
        super(listener);
        mReqAction = "/DoctorService/GetWorkingTimeBase";
        mReqMethod = HttpClient.GET;

    }
}
