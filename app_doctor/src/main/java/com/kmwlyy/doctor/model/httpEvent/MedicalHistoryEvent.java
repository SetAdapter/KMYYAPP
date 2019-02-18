package com.kmwlyy.doctor.model.httpEvent;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.MedicalHistoryBean;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Objects;

/**
 * Created by TFeng on 2017/7/12.
 */

public interface MedicalHistoryEvent {

     class ModifyInfo extends HttpEvent{
        public ModifyInfo(String UserMemberEMRID, String MemberID,String HospitalName, String EMRName,
                        String Date, String Remark, ArrayList<HttpUserConsults.ImageFile> Files, HttpListener mListener){
            super(mListener);
            mReqMethod = HttpClient.POST;
            mReqAction ="/DoctorFamily/ModifyMemberEMR";
            noParmName = true;
            HashMap<String,Object> params = new HashMap<>();
            params.put("UserMemberEMRID",UserMemberEMRID);
            params.put("MemberID",MemberID);
            params.put("HospitalName",HospitalName);
            params.put("EMRName",EMRName);
            params.put("Date",Date);
            params.put("Remark",Remark);
            params.put("Files",Files);
            mJsonData = new Gson().toJson(params);
        }
    }
    class GetEMRList extends HttpEvent<List<MedicalHistoryBean>>{


        public GetEMRList(String CurrentPage,String PageSize,String FamilyDoctorID,String MemberID,String UserMemberEMRID,HttpListener<List<MedicalHistoryBean>> mListener){
            super(mListener);
            mReqMethod = HttpClient.GET;
            mReqAction = "/DoctorFamily/GetMemberEMRList";
            mReqParams = new HashMap<>();
            mReqParams.put("CurrentPage",CurrentPage);
            mReqParams.put("PageSize",PageSize);
            mReqParams.put("FamilyDoctorID",FamilyDoctorID);
            mReqParams.put("MemberID",MemberID);
            mReqParams.put("UserMemberEMRID",UserMemberEMRID);
        }
    }

    class DeleteMedicalHistory extends HttpEvent{
        public DeleteMedicalHistory(String userMemberEMRID,HttpListener mListener) {
            super(mListener);
            mUserOriginalData = true;
            mReqMethod = HttpClient.GET;
            mReqAction = "/DoctorFamily/DeleteMedicalRecord";
            mReqParams = new HashMap<>();
            mReqParams.put("userMemberEMRID",userMemberEMRID);


        }
    }
}
