package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

/**
 * Created by xcj on 2017/7/10.
 */

public class Http_stopDiagnosis_Event extends HttpEvent{
    public Http_stopDiagnosis_Event(String BeginDate,String BeginWorkingTimeBaseID,String EndDate,String EndWorkingTimeBaseID, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorService/StopDiagnosis";
        mReqMethod = HttpClient.POST;
        mReqParams = new HashMap<>();
        mReqParams.put("BeginDate", BeginDate);
        mReqParams.put("BeginWorkingTimeBaseID", BeginWorkingTimeBaseID);
        mReqParams.put("EndDate", EndDate);
        mReqParams.put("EndWorkingTimeBaseID", EndWorkingTimeBaseID);

    }
}
