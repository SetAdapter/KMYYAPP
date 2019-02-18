package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.FamilyDoctor;

import java.util.HashMap;
import java.util.List;

/**
 * 家庭医生 订单列表使用的接口
 */
public class Http_getOrderDoctor_Event extends HttpEvent<List<FamilyDoctor>> {
	public static String pageSize = "20";

	public Http_getOrderDoctor_Event(String CurrentPage,HttpListener listener) {
		super(listener);
		mReqAction = "/UserFamilyDoctor/GetFamilyDoctorRecord";

		mReqMethod = HttpClient.GET;

		mReqParams = new HashMap<>();
		mReqParams.put("CurrentPage", CurrentPage);
		mReqParams.put("PageSize", pageSize);
	}
}
