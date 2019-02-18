package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.Doctor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/13.
 */

public interface HomeDoctorEvent {
    class GetList extends HttpEvent<ArrayList<Doctor.ListItem>> {

        public GetList(int currentPage, int pageSize,HttpListener<ArrayList<Doctor.ListItem>> mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;

            mReqAction = "/Doctors/GetAllFamilyDoctor";

            mReqParams = new HashMap<>();
            mReqParams.put("CurrentPage", "" + currentPage);
            mReqParams.put("PageSize", "" + pageSize);
        }

    }
}
