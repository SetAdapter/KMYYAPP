package doctor.kmwlyy.com.recipe.Event;

import com.alibaba.fastjson.JSON;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import doctor.kmwlyy.com.recipe.Model.Constant;
import doctor.kmwlyy.com.recipe.Model.RecipeSaveBean;

public class Http_deleteRecipe_Event extends HttpEvent<String> {

    public Http_deleteRecipe_Event(String RecipeNo, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorPatients/DeletePatientRecipe";
        mReqMethod = HttpClient.POST;

//        mReqParams = new HashMap<>();
//        mReqParams.put("RecipeNo",RecipeNo);

        noParmName = true;
        mReqParams = new HashMap<>();

        String str = JSON.toJSONString(new testBean(RecipeNo));

        try {
            mJsonData = new String(str.getBytes(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public class testBean{
        public String RecipeNo;

        public testBean(String recipeNo) {
            RecipeNo = recipeNo;
        }
    }
}