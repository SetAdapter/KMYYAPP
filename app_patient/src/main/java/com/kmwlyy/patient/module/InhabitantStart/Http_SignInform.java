package com.kmwlyy.patient.module.InhabitantStart;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/8/11.
 */

public interface Http_SignInform {
    //查询我的签约信息
    public class Http_MySignInfrom extends HttpEvent {


        public Http_MySignInfrom(MySignInformBean params, HttpListener<String> mListener) {
            super(mListener);
            noParmName = true;
            mReqAction = "/Signature/Save";
            mReqMethod = HttpClient.POST;



//            if (params != null) {
//
//                Map<String, Object> mReqParams = new HashMap<>();
//                mReqParams.put("VerificationCode", params.VerificationCode);
//                mReqParams.put("SignatureID", params.SignatureID);
//                mReqParams.put("FDGroupID", params.FDGroupID);
//                mReqParams.put("OrgnazitionID", "" + params.OrgnazitionID);
//                mReqParams.put("SignatureUserName", "" + params.SignatureUserName);
//                mReqParams.put("SignatureUserIDNumber", "" + params.SignatureUserIDNumber);
//                mReqParams.put("SignatureURL", params.SignatureURL);
//                mReqParams.put("FamilyFN", params.FamilyFN);
//                mReqParams.put("Mobile", "" + params.Mobile);
//                mReqParams.put("Province", params.Province);
//                mReqParams.put("City", params.City);
//                mReqParams.put("District", params.District);
//                mReqParams.put("Subdistrict", params.Subdistrict);
//                mReqParams.put("Address", params.Address);
//
//                List<MySignInformBean.MembersBean> members = params.Members;
//                for (int i = 0; i < members.size(); i++) {
//                    mReqParams.put("MemberName", members.get(i).getMemberName());
//                    mReqParams.put("IDNumber", members.get(i).getIDNumber());
//                    mReqParams.put("Relation", members.get(i).getRelation());
//                }
//                mReqParams.put("Members", members);

                mJsonData = new Gson().toJson(params).toString();
//            }


        }

    }



    /**
     * 获取地域区域 获取全国区域(树结构
     */
    public class Http_getRegions extends HttpEvent<List<RegionsTreeBean.DataBean>>{
        public Http_getRegions(HttpListener<List<RegionsTreeBean.DataBean>> mListener){
            super(mListener);
            mReqAction = "/DoctorGroup/GetRegionsTree";
            mReqMethod = HttpClient.GET;

        }
    }


    /**
     *  F0204/获取医疗机构服务区域
     */
    public class Http_GetOrgRegions extends HttpEvent<List<OrgRegionsBean.DataBean>>{
        public Http_GetOrgRegions(HttpListener<List<OrgRegionsBean.DataBean>> mListener){
            super(mListener);
            mReqAction = "/DoctorGroup/GetOrgRegions";
            mReqMethod = HttpClient.GET;

        }
    }
}
