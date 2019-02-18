package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

/**
 * Created by xcj on 2017/8/19.
 */

public class CorporeityIdentifyEvent extends HttpEvent<String> {
    public CorporeityIdentifyEvent(String name, String idCardNumber, String phoneNumber, String sex, HttpListener<String> mListener) {
        super(mListener);
        mReqMethod = HttpClient.GET;
        mUserOriginalData = true;
        mReqAction = "/personInfo/" + name + "/" + idCardNumber + "/" + phoneNumber + "/" + sex;
        mReqParams = new HashMap<>();
    }
}
