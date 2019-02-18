package doctor.kmwlyy.com.recipe.Event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

import doctor.kmwlyy.com.recipe.Model.ResponseBean;

public class Http_SendAgain_DSRecipe_Event extends HttpEvent<String> {

    public Http_SendAgain_DSRecipe_Event(String id, HttpListener listener) {
        super(listener);
        mReqAction = "/Drugstore/ReSend";
        mReqMethod = HttpClient.POST;

        mReqParams = new HashMap<>();
        mReqParams.put("DrugstoreRecipeID", id);


    }
}