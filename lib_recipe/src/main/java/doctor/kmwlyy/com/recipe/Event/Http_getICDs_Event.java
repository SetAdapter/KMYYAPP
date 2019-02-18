package doctor.kmwlyy.com.recipe.Event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;
import java.util.List;


import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DiagDetailBean;
import doctor.kmwlyy.com.recipe.Model.ICD;

/**
 * 获取诊断信息的接口
 */
public class Http_getICDs_Event extends HttpEvent<List<DiagDetailBean>> {

	public Http_getICDs_Event(String Keyword, String type,HttpListener listener) {
		super(listener);
		mReqAction = "/SysICDs";
		mReqMethod = HttpClient.GET;

		mReqParams = new HashMap<>();
		mReqParams.put("CurrentPage", Constant.CurrentPage);
		mReqParams.put("PageSize", Constant.PageSize);
		mReqParams.put("Keyword", Keyword);
		mReqParams.put("ICDType", type);
	}
}
