package com.kmwlyy.doctor.model.httpEvent;

import com.alibaba.fastjson.JSON;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.SignRecipeBean;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

/**
 * 获取患者列表
 */
public class Http_cancelRecipe_Event extends HttpEvent<String> {
    public Http_cancelRecipe_Event(List<String> list, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorRecipe/Retract";
        mReqMethod = HttpClient.POST;
        noParmName = true;
        mReqParams = new HashMap<>();

        try {
            mJsonData = new String(JSON.toJSONString(list).getBytes(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}