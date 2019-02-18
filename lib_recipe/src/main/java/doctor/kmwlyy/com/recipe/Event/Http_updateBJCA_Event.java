package doctor.kmwlyy.com.recipe.Event;


import com.alibaba.fastjson.JSON;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import doctor.kmwlyy.com.recipe.Model.RecipeBean;

/**
 * 保存处方后，同步BJCA的接口
 */
public class Http_updateBJCA_Event extends HttpEvent<String> {

	public Http_updateBJCA_Event(String id, HttpListener listener) {
		super(listener);
		mReqAction = "/BJCA/RecipesSynByOPDRegisterID";
		mReqMethod = HttpClient.GET;

		mReqParams = new HashMap<>();
		mReqParams.put("opdRegisterId", id);
		mReqParams.put("signType", 0+"");
	}

}
