package com.kmwlyy.patient.helper.net.event;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.OptionsBean;
import com.kmwlyy.patient.helper.net.bean.PhotoFile;
import com.kmwlyy.patient.helper.net.bean.UserMemberEMRsInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xcj on 2016/12/17.
 */

public interface UserMemberEMRsEvent {
    class Getlist extends HttpEvent<ArrayList<UserMemberEMRsInfo.ListItem>> {

        public Getlist(String MemberID,int currentPage, int pageSize, HttpListener<ArrayList<UserMemberEMRsInfo.ListItem>> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/UserMemberEMRs";
            mReqParams = new HashMap();
            //mReqParams.put("MemberID", MemberID);
            mReqParams.put("CurrentPage", currentPage+"");
            mReqParams.put("PageSize", pageSize+"");


        }
    }

    class SaveInfo extends HttpEvent{
       /* "UserMemberEMRID": "",
                "MemberID": "77C5CF07923A4E3D8121F628336527B8",
                "EMRName": "入院记录",
                "Date": "2016-12-14",
                "HospitalName": "康美医院",
                "Files": [*/
        public SaveInfo(String UserMemberEMRID, String MemberID, String EMRName, String Date, String HospitalName,String Remark, ArrayList<HttpUserConsults.ImageFile> Files, HttpListener mListener){
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/UserMemberEMRs";
            noParmName = true;

            HashMap<String, Object> params = new HashMap<>();
            if (!TextUtils.isEmpty(UserMemberEMRID)){
                params.put("UserMemberEMRID", UserMemberEMRID);
            }
            params.put("MemberID", MemberID);
            params.put("EMRName", EMRName);
            params.put("Date", Date);
            params.put("HospitalName", HospitalName);
            params.put("Remark", Remark);
            params.put("Files", Files);

            mJsonData = new Gson().toJson(params);
        }
    }

    class  DeleteInfo extends HttpEvent{
        public DeleteInfo(String UserMemberEMRID,HttpListener mListener){
            super(mListener);

            mReqMethod = HttpClient.DELETE;

            mReqAction = "/UserMemberEMRs?id="+UserMemberEMRID;
//            mReqParams = new HashMap();
//            mReqParams.put("UserMemberEMRID", UserMemberEMRID);
        }
    }

    class  GetDetail extends HttpEvent<UserMemberEMRsInfo.ListItem>{
        public GetDetail(String UserMemberEMRID,HttpListener<UserMemberEMRsInfo.ListItem> mListener){
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/UserMemberEMRs/GetUserMemberEMR";
            mReqParams = new HashMap();
            mReqParams.put("UserMemberEMRID", UserMemberEMRID);
        }
    }

    class GetOptions extends HttpEvent<ArrayList<OptionsBean>>{
        public GetOptions(String optionName,HttpListener<ArrayList<OptionsBean>> mListener){
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/options";
            mReqParams = new HashMap();
            mReqParams.put("optionName", optionName);
        }
    }
}
