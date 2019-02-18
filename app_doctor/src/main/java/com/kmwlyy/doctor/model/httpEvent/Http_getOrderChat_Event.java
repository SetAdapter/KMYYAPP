package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.FamilyDoctor;
import com.kmwlyy.doctor.model.OrderChatBean;

import java.util.HashMap;
import java.util.List;

/**
 * 图文咨询，免费咨询 订单列表使用的接口
 */
public class Http_getOrderChat_Event extends HttpEvent<List<OrderChatBean>> {
	public static String pageSize = "10";

	public Http_getOrderChat_Event(String CurrentPage,String type, HttpListener listener) {
		super(listener);
		mReqAction = "/UserConsults/ConsultMeRecord";

		mReqMethod = HttpClient.GET;

		mReqParams = new HashMap<>();
		mReqParams.put("CurrentPage", CurrentPage);
		mReqParams.put("SelectType", type);
		mReqParams.put("PageSize", pageSize);

	}


}
