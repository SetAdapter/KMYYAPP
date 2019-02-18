package com.kmwlyy.doctor.model.httpEvent;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.PatientGroup;
import com.kmwlyy.doctor.model.PatientList;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Winson on 2017/7/5.
 */

public interface MemberGroup {

    class GetList extends HttpEvent<ArrayList<PatientGroup>> {

        public GetList(String groupName, int currentPage, int pageSize, HttpListener<ArrayList<PatientGroup>> listener) {
            super(listener);

            mReqAction = "/MemberGroup/GetList";
            mReqMethod = HttpClient.GET;
            mReqParams = new HashMap<>();
            mReqParams.put("CurrentPage", "" + currentPage);
            mReqParams.put("PageSize", "" + pageSize);
            mReqParams.put("GroupName", groupName);

        }

    }

    class GetMemberList extends HttpEvent<ArrayList<PatientList>> {

        private String searchKey;

        public GetMemberList(String memberGroupID, String memberName, int currentPage, int pageSize, HttpListener<ArrayList<PatientList>> listener) {
            super(listener);

            mReqAction = "/MemberGroup/GetMemberList";
            mReqMethod = HttpClient.GET;
            mReqParams = new HashMap<>();
            mReqParams.put("CurrentPage", "" + currentPage);
            mReqParams.put("PageSize", "" + pageSize);
            mReqParams.put("MemberGroupID", memberGroupID);
            mReqParams.put("MemberName", memberName);
            searchKey = memberName;

        }

        public String getSearchKey() {
            return searchKey;
        }

        public GetMemberList(String memberGroupID, int currentPage, int pageSize, HttpListener<ArrayList<PatientList>> listener) {
            super(listener);

            mReqAction = "/MemberGroup/GetMemberList";
            mReqMethod = HttpClient.GET;
            mReqParams = new HashMap<>();
            mReqParams.put("CurrentPage", "" + currentPage);
            mReqParams.put("PageSize", "" + pageSize);
            mReqParams.put("MemberGroupID", memberGroupID);

        }

    }

    class Insert extends HttpEvent {

        public Insert(String groupName, int sort, HttpListener listener) {
            super(listener);

            mReqAction = "/MemberGroup/Insert";
            mReqMethod = HttpClient.POST;
            mReqParams = new HashMap<>();
            mReqParams.put("Sort", "" + sort);
            mReqParams.put("GroupName", groupName);

        }

    }

    class Delete extends HttpEvent {

        public Delete(String groupId, HttpListener listener) {
            super(listener);

            mReqAction = "/MemberGroup/Delete";
            mReqMethod = HttpClient.GET;
            mReqParams = new HashMap<>();
            mReqParams.put("ID", groupId);

        }

    }

    class AddMembers extends HttpEvent {

        public AddMembers(String memberGroupID, ArrayList<String> doctorMemberIDs, HttpListener listener) {
            super(listener);

            mReqAction = "/MemberGroup/AddMembers";
            mReqMethod = HttpClient.POST;

            noParmName = true;
            mReqParams = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("MemberGroupID", memberGroupID);
            jsonObject.put("DoctorMemberIDs", doctorMemberIDs);
            try {
                mJsonData = new String(jsonObject.toString().getBytes(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }

    }

    class RemoveMembers extends HttpEvent {

        public RemoveMembers(String memberGroupID, List<String> doctorMemberIDs, HttpListener listener) {
            super(listener);

            mReqAction = "/MemberGroup/RemoveMembers";
            mReqMethod = HttpClient.POST;
            mReqParams = new HashMap<>();
            noParmName = true;

            JSONObject data = new JSONObject();
            data.put("MemberGroupID", memberGroupID);
            data.put("DoctorMemberIDs", doctorMemberIDs);
            mJsonData = data.toJSONString();
            Log.d("RemoveMembers", "json data : " + mJsonData);

        }
    }

}
