package doctor.kmwlyy.com.recipe.Event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;
import java.util.List;

import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DrugDetail;
import doctor.kmwlyy.com.recipe.Model.ICD;

/**
 * 获取药品信息的接口
 */
public class Http_getDrugs_Event extends HttpEvent<List<DrugDetail>> {

	public Http_getDrugs_Event(String Keyword, String type, HttpListener listener) {
		super(listener);
		mReqAction = "/SysDrugs";
		mReqMethod = HttpClient.GET;

		mReqParams = new HashMap<>();
		mReqParams.put("CurrentPage", Constant.CurrentPage);
		mReqParams.put("PageSize", Constant.PageSize);
		mReqParams.put("Keyword", Keyword);
		mReqParams.put("DrugType", type);
	}
}
