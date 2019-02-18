package doctor.kmwlyy.com.recipe.Event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

import doctor.kmwlyy.com.recipe.Model.DSRecipeDetailBean;

public class Http_getDSRecipeDetail_Event extends HttpEvent<DSRecipeDetailBean> {

    public Http_getDSRecipeDetail_Event(String id, HttpListener listener) {
        super(listener);
        mReqAction = "/Drugstore/GetDrugstoreRecipe";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("DrugstoreRecipeID", id);


    }
}