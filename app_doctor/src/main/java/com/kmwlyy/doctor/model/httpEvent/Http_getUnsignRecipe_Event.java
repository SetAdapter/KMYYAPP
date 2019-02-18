package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.UnSignRecipeBean;

import java.util.HashMap;
import java.util.List;

/**
 * 获取待签列表
 */
public class Http_getUnsignRecipe_Event extends HttpEvent<List<UnSignRecipeBean>> {
    final String PageSize = "20";
    final String State = "2"; //处方状态（0：未签名，1：已签名，2：已提交）

    public Http_getUnsignRecipe_Event(String CurrentPage, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorRecipeFiles";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("State", State);
        mReqParams.put("PageIndex", CurrentPage);
        mReqParams.put("PageSize", PageSize);
    }

}