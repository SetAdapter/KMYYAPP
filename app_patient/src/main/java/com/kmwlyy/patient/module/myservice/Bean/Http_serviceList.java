package com.kmwlyy.patient.module.myservice.Bean;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */

public class Http_serviceList extends HttpEvent<List<Doorservoce.DataBean>> {

    public Http_serviceList(String CurrentPage, String PageSize, String PackageType,  HttpListener<List<Doorservoce.DataBean>> mListener) {
        super(mListener);

        noParmName = true;
        mReqAction = "/DoctorPackage/GetList";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("CurrentPage", CurrentPage);
        mReqParams.put("PageSize", PageSize);
        mReqParams.put("PackageType", PackageType);
        mJsonData = new Gson().toJson(mReqParams);
    }
}
