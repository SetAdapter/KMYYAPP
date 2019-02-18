package doctor.kmwlyy.com.recipe.Event;

import com.alibaba.fastjson.JSON;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import doctor.kmwlyy.com.recipe.Model.DrugBean;
import doctor.kmwlyy.com.recipe.Model.RecipeLAddBean;
import doctor.kmwlyy.com.recipe.Model.RecipeSaveBean;

public class Http_addRecipe_Event extends HttpEvent<String> {

    public Http_addRecipe_Event(RecipeSaveBean bean, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorPatients/SavePatientRecipe";
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