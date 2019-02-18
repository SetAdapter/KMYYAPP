package com.kmwlyy.patient.module.familydoctorteam;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Stefan on 2017/8/7.
 * 更换团队页面
 */

public class Http_SearchDoctorTeam extends HttpEvent<List<NewChangeDoctorTeam.DataBean>> {

    public Http_SearchDoctorTeam(String CurrentPage, String PageSize, String OrgnazitionID,String DoctorName , HttpListener<List<NewChangeDoctorTeam.DataBean>> mListener) {
        super(mListener);
        mReqAction = "/DoctorGroup/FilteDoctor";
        mReqMethod = HttpClient.GET;
        mReqParams = new HashMap<>();
        mReqParams.put("CurrentPage", CurrentPage);
        mReqParams.put("PageSize", PageSize);
        mReqParams.put("OrgnazitionID", OrgnazitionID);
        mReqParams.put("DoctorName ", DoctorName );
//        mJsonData ="{'pageSize':'1','currentPage':'3'}";
        mJsonData = new Gson().toJson(mReqParams);
    }
}
