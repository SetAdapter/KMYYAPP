package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/8/19
 */
public class Http_unbindBankCard_Event  extends HttpEvent<String> {

    public Http_unbindBankCard_Event(String ID, HttpListener listener) {
        super(listener);
        mReqAction = "/userBankCard/deleteBankCard";
        mReqMethod = HttpClient.POST;

        mReqParams = new HashMap<>();
        mReqParams.put("ID", ID);

    }
}