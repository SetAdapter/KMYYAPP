package com.kmwlyy.doctor.model.httpEvent;

import android.content.Context;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.model.Constant;
import com.kmwlyy.doctor.model.DoctorInfo;

import java.util.HashMap;

public class Http_getMyInfo_Event extends HttpEvent<DoctorInfo> {

    public Http_getMyInfo_Event(Context mContext, HttpListener listener) {
        super(listener);
        mReqAction = "/Doctors";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("UserID", BaseApplication.getInstance().getUserData().mUserId);
    }
}