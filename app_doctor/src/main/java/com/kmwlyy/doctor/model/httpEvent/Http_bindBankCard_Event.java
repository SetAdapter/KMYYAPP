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
public class Http_bindBankCard_Event extends HttpEvent<String> {

    public Http_bindBankCard_Event(String Bank, String BankBarnch,
                                   String AccountName,String CardCode, HttpListener listener) {
        super(listener);
        mReqAction = "/userBankCard/addBankCard";
        mReqMethod = HttpClient.POST;

        mReqParams = new HashMap<>();
        mReqParams.put("Bank", Bank);
        mReqParams.put("BankBarnch", BankBarnch);
        mReqParams.put("AccountName", AccountName);
        mReqParams.put("CardCode", CardCode);
    }
}

