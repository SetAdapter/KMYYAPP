package com.kmwlyy.doctor.model.httpEvent;

import android.content.Context;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.model.VVConsultDetailBean;

import java.util.HashMap;

/**
 * 获取粉丝数
 */
public class Http_getVoiceDetail_Event extends HttpEvent<VVConsultDetailBean> {

    public Http_getVoiceDetail_Event(String oPDRegisterID, HttpListener listener) {
        super(listener);
        mReqAction = "/UserOPDRegisters";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("OPDRegisterID", oPDRegisterID);
    }
}