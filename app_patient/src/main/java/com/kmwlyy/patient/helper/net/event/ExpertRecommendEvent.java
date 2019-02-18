package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.Doctor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/16.
 */

public interface ExpertRecommendEvent {
    class GetList extends HttpEvent<ArrayList<Doctor.ListItem>> {

        public GetList(int currentPage, int pageSize,HttpListener<ArrayList<Doctor.ListItem>> mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;

            mReqAction = "/Doctors/GetExpertDoctors";

            mReqParams = new HashMap<>();
            mReqParams.put("pageIndex", "" + currentPage);
            mReqParams.put("PageSize", "" + pageSize);
        }

    }
}
