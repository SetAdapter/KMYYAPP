package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.MyDoctor;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Description描述: 已就诊过的医生
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/14
 */
public interface DiagnoseDoctorEvent {

    class GetList extends HttpEvent<ArrayList<MyDoctor>> {

        public GetList(int currentPage, int pageSize,HttpListener<ArrayList<MyDoctor>> mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;

            mReqAction = "/Doctors/GetMyVisitDoctors";

            mReqParams = new HashMap<>();
            mReqParams.put("CurrentPage", "" + currentPage);
            mReqParams.put("PageSize", "" + pageSize);
        }

    }
}
