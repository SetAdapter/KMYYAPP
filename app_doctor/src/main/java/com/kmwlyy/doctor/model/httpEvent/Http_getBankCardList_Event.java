package com.kmwlyy.doctor.model.httpEvent;

import com.alibaba.fastjson.JSON;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.BankCardBean;

import java.util.HashMap;
import java.util.List;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/8/19
 */
public class Http_getBankCardList_Event extends HttpEvent<List<BankCardBean>> {

    public Http_getBankCardList_Event(HttpListener listener) {
        super(listener);
        mReqAction = "/userBankCard/getBankCardList";
        mReqMethod = HttpClient.GET;
    }

}