package doctor.kmwlyy.com.recipe.Event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;

import doctor.kmwlyy.com.recipe.Model.RecipeLAddBean;
import doctor.kmwlyy.com.recipe.Model.RecipeLModifyBean;

public class Http_editRecipeList_Event extends HttpEvent<RecipeLModifyBean> {

    public Http_editRecipeList_Event(String id, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorRecipeFormulaFiles";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("ID", id);
    }
}