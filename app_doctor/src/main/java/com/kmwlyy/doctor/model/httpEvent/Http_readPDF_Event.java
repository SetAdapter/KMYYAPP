package com.kmwlyy.doctor.model.httpEvent;

import com.alibaba.fastjson.JSON;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import doctor.kmwlyy.com.recipe.Model.DS_SaveBean;

public class Http_readPDF_Event extends HttpEvent<String> {

    public Http_readPDF_Event(String id, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorRecipeFile/"+id;
        mReqMethod = HttpClient.GET;
//        noParmName = true;

//        mReqParams = new HashMap<>();
//        String str = JSON.toJSONString(id);
//
//        try {
//            mJsonData = new String(str.getBytes(),"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

    }
}