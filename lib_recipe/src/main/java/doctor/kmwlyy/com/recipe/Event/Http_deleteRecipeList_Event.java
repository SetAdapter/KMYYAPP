package doctor.kmwlyy.com.recipe.Event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;
import java.util.List;

public class Http_deleteRecipeList_Event extends HttpEvent<String> {

    public Http_deleteRecipeList_Event(String id, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorRecipeFormulaFiles";
        mReqMethod = HttpClient.DELETE;
        mUseReqParams = true;

        mReqParams = new HashMap<>();
        mReqParams.put("ID", id);
    }
}