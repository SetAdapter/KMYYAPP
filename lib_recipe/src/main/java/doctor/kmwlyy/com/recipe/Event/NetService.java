package doctor.kmwlyy.com.recipe.Event;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpFilter;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.AppToken;
import com.kmwlyy.core.net.event.HttpGetAppTokenEvent;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.LogUtils;

/**
 * Created by Winson on 2016/8/8.
 */
public class NetService {

    private static final String TAG = NetService.class.getSimpleName();
    private static final String TOKEN_EXPIRE = "appToken 过期";

    public static HttpClient createClient(final Context context, final HttpEvent event) {
        HttpClient client = new HttpClient(context, event, new HttpFilter() {
            @Override
            public void onTokenExpire() {
                super.onTokenExpire();
                if (DebugUtils.debug) {
                    Log.d(TAG, "onTokenExpire");
                }

            }

            @Override
            public void onAppTokenExpire() {
                super.onAppTokenExpire();
                if (DebugUtils.debug) {
                    Log.d(TAG, "onAppTokenExpire");
                }
//                Intent mIntent = new Intent(TOKEN_EXPIRE);
//                mIntent.putExtra("info", "TokenExpire");

//                //发送广播
//                context.sendBroadcast(mIntent);

                onAppTokenExpired(mHttpClient);
            }

            @Override
            public void onNoneUserLogin() {
                super.onNoneUserLogin();
                if (DebugUtils.debug) {
                    Log.d(TAG, "onNoneUserLogin");
                }

            }
        });
        return client;
    }


    public static void onAppTokenExpired(HttpClient mHttpClient) {
        if (DebugUtils.debug) {
            Log.d(TAG, "request app token");
        }
        if(BaseApplication.instance.updateAppToken()){
            if (mHttpClient != null) {
                // refresh http client
                mHttpClient.start();
            }
        }
    }
}
