package doctor.kmwlyy.com.recipe.Event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.DrugDetail;

/**
 * 获取药品信息的接口
 * 药品类型（2=西药,1=中药）
 */
public class Http_getDoseUnit_Event extends HttpEvent<ArrayList<String>> {

	public Http_getDoseUnit_Event(String type, HttpListener listener) {
		super(listener);
		mReqAction = "/SysDrugs/GetDoseUnit";
		mReqMethod = HttpClient.GET;

		mReqParams = new HashMap<>();
		mReqParams.put("DrugType", type);
	}
}
