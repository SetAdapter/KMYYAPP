package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.CalendarBean;

import java.util.HashMap;

/**
 * 获取医生排班使用的接口
 */
public class Http_getCalendar_Event extends HttpEvent<CalendarBean> {

	public Http_getCalendar_Event(String date,HttpListener listener) {
		super(listener);
		mReqAction = "/doctorSchedule/GetScheduleList";
		mReqMethod = HttpClient.GET;

		mReqParams = new HashMap<>();
		mReqParams.put("BeginDate", date);
		mReqParams.put("Days", "7");
	}
}
