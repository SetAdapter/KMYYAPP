package com.kmwlyy.doctor.model.httpEvent;

import com.alibaba.fastjson.JSON;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.Constant;
import com.kmwlyy.doctor.model.ConsultBeanNew;
import com.kmwlyy.doctor.model.MedRecordBean;
import com.kmwlyy.doctor.model.QueryChatListBean;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xcj on 2017/7/5.
 */

/**
 * 获取服务记录
 */
public class Http_getPatientRecord_Event extends HttpEvent<MedRecordBean> {

    public Http_getPatientRecord_Event(String opdRegisterID, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorFamily/GetMedicalRecord";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("opdRegisterID", opdRegisterID);
    }
}
