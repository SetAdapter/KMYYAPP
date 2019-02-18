package com.kmwlyy.doctor.model.httpEvent;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.doctor.model.BannerItem;

import java.util.ArrayList;

/**
 * Created by Winson on 2017/7/5.
 */

public class BannerGetList extends HttpEvent<ArrayList<BannerItem>>{

    public BannerGetList(HttpListener<ArrayList<BannerItem>> httpListener){
        super(httpListener);

        mReqAction = "/config/GetBannerList";
        mReqMethod = HttpClient.GET;

    }

}
