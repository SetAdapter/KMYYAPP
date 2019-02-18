package com.kmwlyy.patient.module.InhabitantStart;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/8/10.
 */

public interface Http_MyFamily {

    class Http_FamilyInform extends HttpEvent<FamilyPersonBean> {
        public Http_FamilyInform(String residentid, String name, String mobile, String gender, String birthday,
                                 String relationshipme, String IDNumber, String ownercrowd, HttpListener<FamilyPersonBean> mListener) {
            super(mListener);

            mReqAction = "/webapifamily/addFamilyPerson";
            mReqMethod = HttpClient.POST;
            mReqParams = new HashMap<>();
            noParmName = true;

            mReqParams.put("residentid", residentid);
//            mReqParams.put("address", address);
            mReqParams.put("name", name);
            mReqParams.put("mobile", mobile);
            mReqParams.put("gender", gender);
            mReqParams.put("birthday", birthday);
            mReqParams.put("relationshipme", relationshipme);
            mReqParams.put("IDNumber", IDNumber);
            mReqParams.put("ownercrowd", ownercrowd);

            mJsonData = new Gson().toJson(mReqParams).toString();
        }
    }

    class Http_FamilyList extends HttpEvent<FamilyListBean> {
        public Http_FamilyList(String residentid, HttpListener<FamilyListBean> mListener) {
            super(mListener);
            mReqAction = "/webapifamily/getFamilyList";
            mReqMethod = HttpClient.POST;
            mReqParams = new HashMap<>();
            noParmName = true;

            mReqParams.put("residentid", residentid);

            mJsonData = new Gson().toJson(mReqParams).toString();
        }
    }
}
