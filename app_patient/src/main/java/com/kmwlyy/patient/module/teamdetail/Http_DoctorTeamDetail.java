package com.kmwlyy.patient.module.teamdetail;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.module.familydoctorteam.ChangeDoctorTeam;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/22.
 */

public class Http_DoctorTeamDetail extends HttpEvent<List<DoctorGroupInfo.DataBean.DoctorGroupMembersBean>> {
    public Http_DoctorTeamDetail(String doctorGroupID,String currentPage, String pageSize, HttpListener<List<DoctorGroupInfo.DataBean.DoctorGroupMembersBean>> mListener) {
        super(mListener);
        mReqAction = "/DoctorGroup/GetDoctorGroups";
        mReqMethod = HttpClient.GET;
        mReqParams = new HashMap<>();
        mReqParams.put("doctorGroupID", doctorGroupID);
        mReqParams.put("currentPage", currentPage);
        mReqParams.put("pageSize", pageSize);

//        mJsonData ="{'pageSize':'1','currentPage':'3'}";
        mJsonData = new Gson().toJson(mReqParams);
    }
}
