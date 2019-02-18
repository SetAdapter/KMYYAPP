package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.HealthInformationBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhaoqile on 2016/12/9.
 */

public interface HealthInformationEvent {
    class GetList extends HttpEvent<ArrayList<HealthInformationBean>> {

        public GetList(int currentPage, int pageSize,HttpListener<ArrayList<HealthInformationBean>> mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;

            mReqAction = "/News";
            mReqParams = new HashMap<>();
            mReqParams.put("CurrentPage", "" + currentPage);
            mReqParams.put("PageSize", "" + pageSize);
        }

    }
}
