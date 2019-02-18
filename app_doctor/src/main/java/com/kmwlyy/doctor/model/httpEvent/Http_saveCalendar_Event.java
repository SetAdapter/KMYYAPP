package com.kmwlyy.doctor.model.httpEvent;


import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.CalendarBean;
import com.kmwlyy.doctor.model.DateDay;
import com.kmwlyy.doctor.model.Schedule;

import java.util.HashMap;
import java.util.List;

/**
 * 保存排班的接口
 */
public class Http_saveCalendar_Event extends HttpEvent<String> {

	public Http_saveCalendar_Event(List<DateDay> list,String date, HttpListener listener) {
		super(listener);
		mReqAction = "/DoctorSchedule/AddDoctorSchduleList";
		mReqMethod = HttpClient.POST;

		mReqParams = new HashMap<>();
		String str = JSON.toJSONString(list);

		str = str.replace("\"true\"","true");
		str = str.replace("\"false\"","false");

		str = str.replace(" true","true");
		str = str.replace(" false","false");
		str = str.replace("\"null\"","null");
		str = str.replace(" null","null");

		mReqParams.put("BeginDate", date);
		mReqParams.put("ScheduleData", ""+str);
	}

}
