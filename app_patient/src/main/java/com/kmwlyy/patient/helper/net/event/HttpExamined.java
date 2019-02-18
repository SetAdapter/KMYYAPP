package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.Examined;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Winson on 2016/8/13.
 */
public interface HttpExamined {

    class GetList extends HttpEvent<ArrayList<Examined.ListItem>> {

        public GetList(String keyword, int currentPage, int pageSize, HttpListener<ArrayList<Examined.ListItem>> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/UserEexaminations/GetExaminedList";

            mReqParams = new HashMap();
            mReqParams.put("Keyword", keyword);
            mReqParams.put("PageSize", "" + pageSize);
            mReqParams.put("CurrentPage", "" + currentPage);
        }

    }

    class GetDetail extends HttpEvent<Examined.Detail> {

        public GetDetail(String examId, String idNo, HttpListener<Examined.Detail> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/UserEexaminations/GetExaminedDetail";

            mReqParams = new HashMap();
            mReqParams.put("examId", examId);
            mReqParams.put("idNo", idNo);
        }

    }

}
