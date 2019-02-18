package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.MeetingConsultBean;
import com.kmwlyy.patient.helper.net.bean.MeetingDetailBean;
import com.kmwlyy.patient.helper.net.bean.UserOPDRegisters;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/22.
 */

public interface HttpMeetingConsults {
    class GetList extends HttpEvent<ArrayList<MeetingConsultBean>> {

        public GetList(int currentPage, int pageSize, HttpListener<ArrayList<MeetingConsultBean>> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/DoctorConsultations";

            mReqParams = new HashMap<>();
            mReqParams.put("PageSize", "" + pageSize);
            mReqParams.put("CurrentPage", "" + currentPage);
        }
    }


    class GetDetail extends HttpEvent<MeetingDetailBean> {

        public GetDetail(String id, HttpListener<MeetingDetailBean> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/DoctorConsultations";

            mReqParams = new HashMap<>();
            mReqParams.put("ID", id);
        }
    }
}
