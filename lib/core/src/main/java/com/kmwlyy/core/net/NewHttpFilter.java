package com.kmwlyy.core.net;

/**
 * Created by xcj on 2017/8/9.
 */

public class NewHttpFilter {
    protected NewHttpClient mHttpClient;

    public void setHttpClient(NewHttpClient httpClient) {
        this.mHttpClient = httpClient;
    }

    public void onTokenExpire() {

    }

    public void onAppTokenExpire() {

    }

    public void onNoneUserLogin() {

    }
}
