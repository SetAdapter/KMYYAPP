package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.DoctorService;

import java.util.HashMap;

/**
 * Created by Winson on 2017/7/8.
 */

public interface HttpDoctorService {

    class GetTodayApptCount extends HttpEvent<DoctorService>{

        public GetTodayApptCount(HttpListener<DoctorService> listener) {
            super(listener);

            mReqAction = "/DoctorService/GetTodayApptCount";
            mReqMethod = HttpClient.GET;
            mReqParams = new HashMap<>();

        }

    }

}
