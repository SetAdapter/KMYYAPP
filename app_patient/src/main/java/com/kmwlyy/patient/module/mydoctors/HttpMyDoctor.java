package com.kmwlyy.patient.module.mydoctors;


import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;


/**
 * Created by Administrator on 2017/8/11.
 */

public class HttpMyDoctor extends HttpEvent<MyDoctorBean>{
    public HttpMyDoctor(String currentPage, String pageSize, HttpListener<MyDoctorBean> mListener) {
        super(mListener);

        mReqAction = "/webapidoctor/getMyDoctorInfo";
        mReqMethod = HttpClient.POST;
        mReqParams = new HashMap<>();
        noParmName = true;
        mReqParams.put("currentPage", currentPage);
        mReqParams.put("pageSize", pageSize);


        mJsonData = new Gson().toJson(mReqParams).toString();
    }

}
