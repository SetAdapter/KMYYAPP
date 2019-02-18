package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.Doctor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Winson on 2016/8/13.
 */
public interface HttpDoctor {

    class GetList extends HttpEvent<ArrayList<Doctor.ListItem>>{

        public GetList(String keyword, int currentPage, int pageSize, HttpListener<ArrayList<Doctor.ListItem>> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/Doctors";

            mReqParams = new HashMap<>();
            mReqParams.put("Keyword", keyword);
            mReqParams.put("CurrentPage", "" + currentPage);
            mReqParams.put("PageSize", "" + pageSize);
        }

    }

    class GetDetail extends HttpEvent<Doctor.Detail>{

        public GetDetail(String doctorId, HttpListener<Doctor.Detail> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/Doctors";

            mReqParams = new HashMap<>();
            mReqParams.put("ID", doctorId);

        }

    }

}
