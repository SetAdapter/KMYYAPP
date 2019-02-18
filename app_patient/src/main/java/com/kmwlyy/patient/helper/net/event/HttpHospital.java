package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.Hospital;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Winson on 2016/8/13.
 */
public interface HttpHospital {

    class GetList extends HttpEvent<ArrayList<Hospital.ListItem>> {

        public GetList(String keyword, int currentPage, int pageSize, HttpListener<ArrayList<Hospital.ListItem>> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/Hospitals";

            mReqParams = new HashMap<>();
            mReqParams.put("CurrentPage", "" + currentPage);
            mReqParams.put("PageSize", "" + pageSize);
            mReqParams.put("Keyword", keyword);

        }
    }

    class GetDetail extends HttpEvent<Hospital.Detail> {

        // test id FC79B50A79B241BFAC7F86C4DE3CEC74
        public GetDetail(String id, HttpListener<Hospital.Detail> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/Hospitals";

            mReqParams = new HashMap<>();
            mReqParams.put("ID", id);
        }
    }

}
