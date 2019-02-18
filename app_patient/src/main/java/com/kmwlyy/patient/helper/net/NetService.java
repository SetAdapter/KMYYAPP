package com.kmwlyy.patient.helper.net;

import android.content.Context;
import android.util.Log;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpFilter;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.patient.PApplication;
import com.kmwlyy.patient.helper.utils.EventApi;

/**
 * Created by Winson on 2016/8/8.
 */
public class NetService {

    private static final String TAG = NetService.class.getSimpleName();

    public static HttpClient createClient(Context context, final HttpEvent event) {
        final PApplication application = PApplication.getPApplication(context);
        HttpClient client = new HttpClient(context, event, application.getHttpFilter());
        return client;
    }

    public static HttpClient createClient(Context context, String url, final HttpEvent event) {
        final PApplication application = PApplication.getPApplication(context);
        HttpClient client = new HttpClient(context, event, url, application.getHttpFilter());
        return client;
    }

    public static void closeClient(HttpClient client) {
        if (client != null) {
            client.cancel();
        }
    }

}
