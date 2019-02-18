package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.DoctorClinic;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xcj on 2016/9/1.
 */
public interface HttpDoctorFreeClinic {
    class GetList extends HttpEvent<ArrayList<DoctorClinic>> {

        public GetList(int page, int pageSize, HttpListener<ArrayList<DoctorClinic>> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/Doctors/FreeClinic";

            mReqParams = new HashMap<>();
            mReqParams.put("page", "" + page);
            mReqParams.put("pageSize", "" + pageSize);
        }

    }
}
