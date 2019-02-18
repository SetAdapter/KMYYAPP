package com.kmwlyy.doctor.model.httpEvent;

import android.content.Context;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.model.DoctorInfoBean;
import com.kmwlyy.doctor.model.ServiceNumBean;

import java.util.HashMap;
import java.util.List;

/**
 * 获取粉丝数
 */
public class Http_getDoctorInfo1_Event extends HttpEvent<DoctorInfoBean> {

    public Http_getDoctorInfo1_Event(Context mContext, HttpListener listener) {
        super(listener);
        mReqAction = "/Doctors/GetDoctorStatisticsInfo" + "?UserID=" + BaseApplication.getInstance().getUserData().mUserId;
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
    }

}