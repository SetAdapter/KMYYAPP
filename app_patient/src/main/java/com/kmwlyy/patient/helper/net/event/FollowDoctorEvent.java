package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.MyDoctor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/14.
 */

public interface FollowDoctorEvent {
    class GetList extends HttpEvent<ArrayList<MyDoctor>> {

        public GetList(int currentPage, int pageSize,HttpListener<ArrayList<MyDoctor>> mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;

            mReqAction = "/UserAttentions";

            mReqParams = new HashMap<>();
            mReqParams.put("CurrentPage", "" + currentPage);
            mReqParams.put("PageSize", "" + pageSize);
        }

    }

    class ChangeFollow extends HttpEvent{
        public ChangeFollow(String doctorId , HttpListener mListener) {
            super(mListener);
            mReqMethod = HttpClient.PUT;

            mReqAction = "/UserAttentions";

            mReqParams = new HashMap<>();
            mReqParams.put("DoctorID", "" + doctorId);
        }
    }
}
