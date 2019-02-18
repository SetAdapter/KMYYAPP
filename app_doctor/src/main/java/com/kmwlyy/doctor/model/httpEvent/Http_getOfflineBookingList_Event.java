package com.kmwlyy.doctor.model.httpEvent;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by TFeng on 2017/7/27.
 */

public class Http_getOfflineBookingList_Event extends HttpEvent<List<OfflineBookingBean>> {
    public Http_getOfflineBookingList_Event(String CurrentPage,String PageSize,HttpListener<List<OfflineBookingBean>> mListener) {
        super(mListener);
        mReqMethod = HttpClient.GET;
        mReqAction = "/DoctorFamily/GetOppointmentList";
        mReqParams = new HashMap();
        mReqParams.put("CurrentPage",CurrentPage);
        mReqParams.put("PageSize",PageSize);
        mReqParams.put("IsThisWeek",true+"");
    }
}
