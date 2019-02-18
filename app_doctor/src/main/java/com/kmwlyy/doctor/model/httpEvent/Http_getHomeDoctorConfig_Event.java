package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.HomeSettingBean;

/**
 * Created by TFeng on 2017/7/7.
 */

public class Http_getHomeDoctorConfig_Event extends HttpEvent<HomeSettingBean> {
    public Http_getHomeDoctorConfig_Event(HttpListener<HomeSettingBean> mListener) {
        super(mListener);
        mReqAction = "/DoctorFamily/GetContract";
        mReqMethod= HttpClient.GET;
    }
}
