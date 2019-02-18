package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.SingedMemberBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by TFeng on 2017/7/6.
 */

public class Htttp_getSingedList_Event extends HttpEvent<List<SingedMemberBean>> {

    public Htttp_getSingedList_Event(String CurrentPage, String PageSize, HttpListener<List<SingedMemberBean>> listener) {
        super(listener);
        mReqAction = "/DoctorFamily/GetUserList";
        mReqMethod = HttpClient.GET;
        mReqParams = new HashMap<>();
        mReqParams.put("CurrentPage",CurrentPage);
        mReqParams.put("PageSize",PageSize);
    }
}
