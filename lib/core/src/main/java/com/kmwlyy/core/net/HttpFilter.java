package com.kmwlyy.core.net;

/**
 * Created by Winson on 2016/8/8.
 */
public class HttpFilter {

    protected HttpClient mHttpClient;

    public void setHttpClient(HttpClient httpClient) {
        this.mHttpClient = httpClient;
    }

    public void onTokenExpire() {

    }

    public void onAppTokenExpire() {

    }

    public void onNoneUserLogin() {

    }

}
