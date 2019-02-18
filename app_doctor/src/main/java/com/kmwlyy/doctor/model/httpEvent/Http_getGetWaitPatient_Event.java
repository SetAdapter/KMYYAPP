package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.OPDRegister;

import java.util.HashMap;
import java.util.List;

/**
 * 语音咨询 和 视频咨询列表使用的接口
 */
public class Http_getGetWaitPatient_Event extends HttpEvent<List<OPDRegister>> {
	final String PageSize = "20";
	final String IsToday = "1";  //默认都是今天    是否当天(0:否，1：是)
	final String Status = "0";  //默认都是待诊     候诊状状态(候诊状(0:待诊，1：已诊))

	/**
	 * 用于在线咨询模块 获取列表
	 * OPDType  预约类型（0-挂号、1-图文、2-语音、3-视频,-1-不区分类型）
     */
	public Http_getGetWaitPatient_Event(String CurrentPage, String OPDType ,HttpListener listener) {
		super(listener);
		mReqAction = "/DoctorPatients/GetWaitPatientPageList";
		mReqMethod = HttpClient.POST;

		mReqParams = new HashMap<>();
		mReqParams.put("PageIndex", CurrentPage);
		mReqParams.put("PageSize", PageSize);
		mReqParams.put("OPDType", OPDType);
		mReqParams.put("Status", Status);
		mReqParams.put("IsToday", IsToday);
	}

	/**
	 * 用于在线咨询模块 - 搜索
	 * OPDType  预约类型（0-挂号、1-图文、2-语音、3-视频,-1-不区分类型）
     */
	public Http_getGetWaitPatient_Event(String CurrentPage, String OPDType ,String Keyword , HttpListener listener) {
		super(listener);
		mReqAction = "/DoctorPatients/GetWaitPatientPageList";
		mReqMethod = HttpClient.POST;

		mReqParams = new HashMap<>();
		mReqParams.put("PageIndex", CurrentPage);
		mReqParams.put("PageSize", PageSize);
		mReqParams.put("OPDType", OPDType);
		mReqParams.put("SearchKey", Keyword);
		mReqParams.put("Status", Status);
		mReqParams.put("IsToday", IsToday);
	}
}
