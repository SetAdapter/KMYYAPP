package com.kmwlyy.core.net.event;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;
import java.util.List;


/**
 * 意见反馈
 * Created by Alois on 2016/12/9.
 */

public interface FeedbackEvent {
    class ImageFile {
        @SerializedName("FileName")
        public String fileName;//:"图片名称",

        @SerializedName("URL")
        public String url;//:"/xxx/xxx.jpg",
    }


    class Feedback extends HttpEvent {
        public Feedback(String content , List<ImageFile> images, String Subject,HttpListener mListener) {
            super(mListener);
            mReqMethod = HttpClient.POST;

            mReqAction = "/UserFeedbacks/Add";

            noParmName = true;

            HashMap<String, Object> params  = new HashMap<>();
            params.put("Subject", Subject);//医生端传  康美医生，  患者端传 康美医疗
            params.put("Content", content);
            params.put("Terminal", "1");//0-Web、1-安卓、2-IOS
            params.put("Attachments",images);

            mJsonData = new Gson().toJson(params);

        }
    }
}
