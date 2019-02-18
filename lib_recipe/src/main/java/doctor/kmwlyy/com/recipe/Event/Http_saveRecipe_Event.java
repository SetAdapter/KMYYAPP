package doctor.kmwlyy.com.recipe.Event;


import com.alibaba.fastjson.JSON;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import doctor.kmwlyy.com.recipe.Model.PatientDiagnoseBean;
import doctor.kmwlyy.com.recipe.Model.RecipeBean;

/**
 * 保存处方的接口
 */
public class Http_saveRecipe_Event extends HttpEvent<String> {

	public Http_saveRecipe_Event(PatientDiagnoseBean bean, HttpListener listener) {
		super(listener);
		mReqAction = "/DoctorPatients/SavePatientDiagnose";
		mReqMethod = HttpClient.POST;
		noParmName = true;

		mReqParams = new HashMap<>();
		String str = JSON.toJSONString(bean);

		try {
			mJsonData = new String(str.getBytes(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
