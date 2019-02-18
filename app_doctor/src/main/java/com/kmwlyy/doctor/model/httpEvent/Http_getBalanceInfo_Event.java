package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.AccountBalanceInfoBean;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/8/24
 */
public class Http_getBalanceInfo_Event extends HttpEvent<AccountBalanceInfoBean> {

    public Http_getBalanceInfo_Event(HttpListener listener) {
        super(listener);
        mReqAction = "/userAccount/getUserAccount";
        mReqMethod = HttpClient.GET;
    }

}