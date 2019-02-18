package doctor.kmwlyy.com.recipe.Event;

import com.alibaba.fastjson.JSON;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import doctor.kmwlyy.com.recipe.Model.DSRecipeDetailBean;
import doctor.kmwlyy.com.recipe.Model.DS_SaveBean;
import doctor.kmwlyy.com.recipe.Model.ResponseBean;

public class Http_Save_DSRecipe_Event extends HttpEvent<String> {

    public Http_Save_DSRecipe_Event(DS_SaveBean recipe, HttpListener listener) {
        super(listener);
        mReqAction = "/Drugstore/UpdateDrugstoreRecipe";
        mReqMethod = HttpClient.POST;
        noParmName = true;

        mReqParams = new HashMap<>();
        String str = JSON.toJSONString(recipe);

        try {
            mJsonData = new String(str.getBytes(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}