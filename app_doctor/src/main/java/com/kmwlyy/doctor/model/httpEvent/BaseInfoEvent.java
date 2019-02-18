package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

/**
 * Created by TFeng on 2017/7/13.
 */

public interface BaseInfoEvent {


    class ModifyInfo extends HttpEvent{

        public ModifyInfo(String FamilyDoctorID, String MemberID,String MemberName, String IDNumber,String Gender,String Mobile,
                          String Address,String Remark, String Relation,HttpListener mListener) {
            super(mListener);
            mReqMethod = HttpClient.POST;
            mReqAction = "/DoctorFamily/ModifyMember";
            mUserOriginalData = true;
            mReqParams = new HashMap();
            mReqParams.put("FamilyDoctorID",FamilyDoctorID);
            mReqParams.put("IDNumber",IDNumber);
            mReqParams.put("MemberName",MemberName);
            mReqParams.put("Gender",Gender);
            mReqParams.put("MemberID",MemberID);
            mReqParams.put("Mobile",Mobile);
            mReqParams.put("Address",Address);
            mReqParams.put("Remark",Remark);
            mReqParams.put("Relation",Relation);
            mReqParams.put("IDType",0+"");
        }
    }
}
