package doctor.kmwlyy.com.recipe.Event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;
import doctor.kmwlyy.com.recipe.Model.RecipeBean;

/**
 * 获取处方的接口
 */
public class Http_getRecipe_Event extends HttpEvent<RecipeBean> {

	public Http_getRecipe_Event(String id, HttpListener listener) {
		super(listener);
		mReqAction = "/DoctorDiagnosis/DiagnoseRecipe";
		mReqMethod = HttpClient.GET;

		mReqParams = new HashMap<>();
		mReqParams.put("OPDRegisterID", id);
	}
}
