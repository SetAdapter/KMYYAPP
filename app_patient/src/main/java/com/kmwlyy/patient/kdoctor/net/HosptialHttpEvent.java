package com.kmwlyy.patient.kdoctor.net;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.patient.kdoctor.BatLocationUtils;

import java.util.HashMap;

/**
 * @Description描述:
 * @Author作者: hx
 * @Date日期: 2017/8/25
 */

public class HosptialHttpEvent extends HttpEvent{
    public HosptialHttpEvent() {
        super();
        mUserOriginalData = true;
        mReqParams = new HashMap();
        mReqMethod = HttpClient.POST;
    }

    public void getNearByHosptial(){
        mReqAction = BaseConstants.SEARCH_SERVER_URL + "elasticsearch/searchapp/searchNearHospital";
        mReqParams.put("lat", BatLocationUtils.latitude + "");
        mReqParams.put("lon", BatLocationUtils.longitude + "");
    }
}
