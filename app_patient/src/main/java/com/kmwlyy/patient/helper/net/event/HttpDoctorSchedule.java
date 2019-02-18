package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.DoctorSchedule;

import java.util.HashMap;

/**
 * Created by Winson on 2016/8/13.
 */
public interface HttpDoctorSchedule {

    class GetAvailableTimes extends HttpEvent<DoctorSchedule> {

        public GetAvailableTimes(String doctorId, String beginDate, int days, HttpListener<DoctorSchedule> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/DoctorSchedule/AvailableTimes";

            mReqParams = new HashMap();
            mReqParams.put("doctorId", doctorId);
            mReqParams.put("beginDate", beginDate);
            mReqParams.put("days", "" + days);

        }

    }

    class getDoctorSchedule extends HttpEvent {

        public getDoctorSchedule(String scheduleId, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/DoctorSchedule/getDoctorSchedule";

            mReqParams = new HashMap();
            mReqParams.put("ID", scheduleId);

        }

    }

}
