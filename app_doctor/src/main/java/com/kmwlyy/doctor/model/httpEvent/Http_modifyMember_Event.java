package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

/**
 * Created by TFeng on 2017/7/7.
 */

public class Http_modifyMember_Event extends HttpEvent {
    public Http_modifyMember_Event(String FamilyDoctorID,String MemberID,String Mobile,String Address,String Remark,HttpListener mListener) {
        super(mListener);
        mReqAction="/DoctorFamily/ModifyMember";
        mReqMethod= HttpClient.POST;
        mReqParams = new HashMap<>();
        mReqParams.put("FamilyDoctorID",FamilyDoctorID);
        mReqParams.put("MemberID",MemberID);
        mReqParams.put("Mobile",Mobile);
        mReqParams.put("Address",Address);
        mReqParams.put("Remark",Remark);
    }
}
