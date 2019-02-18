package com.kmwlyy.patient.helper.net.bean;

/**
 * Created by xcj on 2016/12/23.
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
