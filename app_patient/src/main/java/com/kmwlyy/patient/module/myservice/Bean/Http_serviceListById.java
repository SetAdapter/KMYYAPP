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

public class Http_serviceListById extends HttpEvent<BaseService.DataBean> {

    public Http_serviceListById(String packageID, HttpListener<BaseService.DataBean> mListener) {
        super(mListener);
        noParmName =true;
        mReqAction = "/DoctorPackage/GetDetail";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("packageID", packageID);

        mJsonData =new Gson().toJson(mReqParams);
    }


}
