package com.kmwlyy.doctor.model.httpEvent;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.doctor.model.SignRecipeBean;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 获取患者列表
 */
public class Http_getSignList_Event extends HttpEvent<List<SignRecipeBean>> {
    final String PageSize = "20";

    /**
     *
     * @param type 已签1   待签2
     */
    public Http_getSignList_Event(String CurrentPage, String type, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorRecipe/GetList";
        mReqMethod = HttpClient.POST;
        noParmName = true;
        mReqParams = new HashMap<>();

        try {
            Gson gson = new Gson();
            mJsonData = gson.toJson(new SignBean(CurrentPage,PageSize,Integer.parseInt(type)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索处方
       */
    public Http_getSignList_Event(String CurrentPage, String type, String memberName, HttpListener listener) {
        super(listener);
        mReqAction = "/DoctorRecipe/GetList";
        mReqMethod = HttpClient.POST;
        noParmName = true;
        mReqParams = new HashMap<>();

        try {
            Gson gson = new Gson();
            mJsonData = gson.toJson(new SignBean(CurrentPage,PageSize,memberName));
            LogUtils.i("testttttt",mJsonData + "   memberName:" + memberName);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            mJsonData = new String(JSON.toJSONString(new SignBean(CurrentPage,PageSize,memberName)).getBytes(),"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
    }

}

class SignBean{
    List<String> States;
    String CurrentPage;
    String PageSize;
    String memberName;

    public SignBean(String currentPage, String pageSize, String memberName) {
        States = new ArrayList<>();
        States.add("1");
        States.add("2");
        CurrentPage = currentPage;
        PageSize = pageSize;
        this.memberName = memberName;
    }

    public SignBean(String currentPage, String pageSize, int type) {
        States = new ArrayList<>();
        States.add(""+ type);
        CurrentPage = currentPage;
        PageSize = pageSize;
    }

    @Override
    public String toString() {
        return "SignBean{" +
                "States=" + States +
                ", CurrentPage='" + CurrentPage + '\'' +
                ", PageSize='" + PageSize + '\'' +
                ", memberName='" + memberName + '\'' +
                '}';
    }
}