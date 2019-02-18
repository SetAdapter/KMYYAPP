package com.kmwlyy.doctor.model;

/**
 * Created by TFeng on 2017/7/8.
 */

public class UploadImageBean {
    private String url;
    private String UrlPrefix;
    private boolean isNet;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isNet() {
        return isNet;
    }

    public void setNet(boolean net) {
        isNet = net;
    }

    public String getUrlPrefix() {
        return UrlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        UrlPrefix = urlPrefix;
    }
}
