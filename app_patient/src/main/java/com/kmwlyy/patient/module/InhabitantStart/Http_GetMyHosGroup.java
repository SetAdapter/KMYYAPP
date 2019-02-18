package com.kmwlyy.patient.module.InhabitantStart;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.module.familydoctorteam.ChangeDoctorTeam;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/23.
 */

public class Http_GetMyHosGroup extends HttpEvent<GetMyHosGroupBean.DataBean> {
    public Http_GetMyHosGroup(HttpListener<GetMyHosGroupBean.DataBean> mListener) {
        super(mListener);
        mReqAction = "/DoctorGroup/GetMyHosGroup";
        mReqMethod = HttpClient.GET;
        mReqParams = new HashMap<>();

//        mJsonData ="{'pageSize':'1','currentPage':'3'}";
        mJsonData = new Gson().toJson(mReqParams);
    }
}
