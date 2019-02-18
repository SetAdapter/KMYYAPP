package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.BankCardBean;

import java.util.HashMap;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/8/22
 */
public class Http_cashBalance_Event  extends HttpEvent<String> {

    public Http_cashBalance_Event(String Amount,BankCardBean bankCardBean,HttpListener listener) {
        super(listener);
        mReqAction = "/userCashe/addUserCash";
        mReqMethod = HttpClient.POST;

        mReqParams = new HashMap<>();
        mReqParams.put("Amount", Amount);
        mReqParams.put("Bank", bankCardBean.Bank);
        mReqParams.put("BankBarnch", bankCardBean.BankBarnch);
        mReqParams.put("AccountName", bankCardBean.AccountName);
        mReqParams.put("CardCode", bankCardBean.CardCode);
    }
}