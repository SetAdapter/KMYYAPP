package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.DrugStoreRecipeBean;

import java.util.HashMap;
import java.util.List;

public class Http_getDrugStorelRecipe_Event extends HttpEvent<List<DrugStoreRecipeBean>> {
    final String PageSize = "20";

    public Http_getDrugStorelRecipe_Event(String CurrentPage, HttpListener listener) {
        super(listener);
        mReqAction = "/Drugstore/GetDrugstoreRecipeByDoctor";
        mReqMethod = HttpClient.GET;

        mReqParams = new HashMap<>();
        mReqParams.put("CurrentPage", CurrentPage);
        mReqParams.put("PageSize", PageSize);


    }
}