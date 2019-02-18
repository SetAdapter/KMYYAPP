package com.kmwlyy.patient.module.signcomfirm;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.module.InhabitantStart.FamilyListBean;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/24.
 */

public class Http_GetDetail extends HttpEvent<SignDetailBean> {
    public  Http_GetDetail(String FDGroupName,HttpListener<SignDetailBean> mListener){
        super(mListener);
        mReqAction = "/Signature/GetDetail";
        mReqMethod = HttpClient.GET;
        mReqParams = new HashMap<>();

        mReqParams.put("ID", FDGroupName);

        mJsonData = new Gson().toJson(mReqParams).toString();

    }
}
