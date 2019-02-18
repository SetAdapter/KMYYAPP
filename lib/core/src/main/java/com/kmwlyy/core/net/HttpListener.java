package com.kmwlyy.core.net;

/**
 * Created by Winson on 2016/8/8.
 */
public interface HttpListener<T> {

    void onError(int code, String msg);

    void onSuccess(T t);

}
