package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.UserAttentions;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Winson on 2016/8/13.
 */
public interface HttpUserAttentions {

    class Update extends HttpEvent {

        public Update(String doctorId, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.PUT;

            mReqAction = "/UserAttentions";

            mReqParams = new HashMap();
            mReqParams.put("DoctorID", doctorId);

        }

    }

    class GetList extends HttpEvent<ArrayList<UserAttentions.ListItem>> {

        public GetList(String isExpert, HttpListener<ArrayList<UserAttentions.ListItem>> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/UserAttentions";

            mReqParams = new HashMap();
            mReqParams.put("IsExpert", "" + isExpert);
        }

    }

}
