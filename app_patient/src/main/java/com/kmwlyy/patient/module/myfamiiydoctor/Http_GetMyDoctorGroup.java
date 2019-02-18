package com.kmwlyy.patient.module.myfamiiydoctor;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.module.familydoctorteam.ChangeDoctorTeam;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 更换团队页面
 */

public class Http_GetMyDoctorGroup extends HttpEvent<List<MyFamilyDoctorDean.DataBean>> {

    public Http_GetMyDoctorGroup( HttpListener<List<MyFamilyDoctorDean.DataBean>> mListener) {
        super(mListener);
        mReqAction = "/DoctorGroup/GetMyDoctorGroup";
        mReqMethod = HttpClient.GET;
        mReqParams = new HashMap<>();
//        mJsonData ="{'pageSize':'1','currentPage':'3'}";
        mJsonData = new Gson().toJson(mReqParams);
    }
}
