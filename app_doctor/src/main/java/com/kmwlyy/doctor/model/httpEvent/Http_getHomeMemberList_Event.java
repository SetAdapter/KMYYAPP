package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.SingedMemberBean;

import java.util.HashMap;
import java.util.List;

/**
 * Created by TFeng on 2017/7/8.
 */

public class Http_getHomeMemberList_Event extends HttpEvent<List<SingedMemberBean.UserMemberBean>> {
    public Http_getHomeMemberList_Event(String FamilyDoctorID,HttpListener<List<SingedMemberBean.UserMemberBean>> mListener) {
        super(mListener);
        mReqMethod = HttpClient.GET;
        mReqAction = "/DoctorFamily/GetMemberList";
        mReqParams = new HashMap<>();
        mReqParams.put("FamilyDoctorID",FamilyDoctorID);
    }
}
