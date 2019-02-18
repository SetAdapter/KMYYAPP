package com.kmwlyy.core.util;

import com.google.gson.Gson;

/**
 * Created by Winson on 2016/8/5.
 */
public class DebugUtils {

    public static final boolean debug = true;

    public static final String ERROR_FORMAT = "%s error , code : %s , msg : %s";
    public static final String SUCCESS_FORMAT = "%s success , result : %s";

    private static Gson mGson;

    static {
        if (DebugUtils.debug) {
            mGson = new Gson();
        }
    }

    public static String toJson(Object obj) {
        if (mGson != null) {
            return mGson.toJson(obj);
        }
        return null;
    }

    public static String errorFormat(String key, int code, String msg) {
        return String.format(ERROR_FORMAT, key, code, msg);
    }

    public static String successFormat(String key, Object result) {
        return String.format(SUCCESS_FORMAT, key, result);
    }

}
