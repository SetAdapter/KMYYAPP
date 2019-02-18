package doctor.kmwlyy.com.recipe.Event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;
import java.util.List;

import doctor.kmwlyy.com.recipe.Model.DrugListBean;

public class Http_getRecipeList_Event extends HttpEvent<List<DrugListBean>> {
    final String PageSize = "20";

    public Http_getRecipeList_Event(String CurrentPage, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorRecipeFormulaFiles";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("CurrentPage", CurrentPage);
        mReqParams.put("PageSize", PageSize);
        mReqParams.put("ShowDetails", "true");
    }
}