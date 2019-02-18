package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

/**
 * Created by xcj on 2017/8/21.
 */

public class CorporeityIdentifyIsTestEvent extends HttpEvent<String> {
    public CorporeityIdentifyIsTestEvent(String idCardNumber, String appid, String timeStamp,String once,String token, HttpListener<String> mListener) {
        super(mListener);
        mReqMethod = HttpClient.GET;
        mUserOriginalData = true;
        mReqAction = "/api/hlthrecord/examresult";
        mReqParams = new HashMap<>();
        mReqParams.put("idno",idCardNumber);
        mReqParams.put("appid",appid);
        mReqParams.put("timeStamp",timeStamp);
        mReqParams.put("once",once);
        mReqParams.put("token",token);
    }
}
