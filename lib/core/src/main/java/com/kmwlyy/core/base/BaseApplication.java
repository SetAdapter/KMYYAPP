package com.kmwlyy.core.base;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpFilter;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.NewHttpFilter;
import com.kmwlyy.core.net.bean.AppToken;
import com.kmwlyy.core.net.bean.IMConfig;
import com.kmwlyy.core.net.bean.SignatureBean;
import com.kmwlyy.core.net.bean.UserData;
import com.kmwlyy.core.net.event.HttpGetAppTokenEvent;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ImageUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.SPUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016-8-6.
 */
public class BaseApplication {

    public static final String TAG = BaseApplication.class.getSimpleName();
    //听云
    public static final String NBS_DOCTOR_BETA = "9bcb2a0ee6754a54a803db562890d290"; //医生测试key
    public static final String NBS_DOCTOR_RELEASE = "6afc83bdba194d889a2a6b47da033452";//医生正式环境key

    public String getDoctorNBSKey(){
        if(DebugUtils.debug){
            return NBS_DOCTOR_BETA;
        }
        return NBS_DOCTOR_RELEASE;
    }
    public interface JPushListener {
        String getJpushRegisterID();
    }

    public interface LogoutListener {
        void onLogout();
    }

    public static BaseApplication instance;
    private boolean isUpdateAppToken;
    private Context context;
    public HttpFilter httpFilter;
    private JPushListener jPushListener;
    private LogoutListener logoutListener;

    public static BaseApplication newInstance(Context context) {
        if (instance == null) {
            synchronized (BaseApplication.class) {
                if (instance == null) {
                    instance = new BaseApplication(context);
                }
            }
        }
        return instance;
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    private BaseApplication(Context context) {
        this.context = context;
    }

    public static Context getContext() {
        return instance.context;
    }

    public void setJPushListener(JPushListener jPushListener) {
        this.jPushListener = jPushListener;
    }

    public void setLogoutListener(LogoutListener logoutListener) {
        this.logoutListener = logoutListener;
    }

    public void onCreate() {
        ImageUtils.initImageLoader(context);

        if (!hasAppToken()) {
            updateAppToken();
        }
    }

    /*
    * 接口方法
    */
    public boolean updateAppToken() { //更新AppToken
        if (isUpdateAppToken) {
            return false;
        }

        HttpGetAppTokenEvent event = new HttpGetAppTokenEvent(new HttpListener<AppToken>() {
            @Override
            public void onError(int code, String msg) {
                LogUtils.d(TAG, "updateAppToken onError!");
                isUpdateAppToken = false;
            }

            @Override
            public void onSuccess(AppToken appToken) {
                LogUtils.d(TAG, "updateAppToken onSuccess : " + appToken.Token);
                setAppToken(appToken.Token);
                isUpdateAppToken = false;
            }
        });
        HttpClient client = new HttpClient(context, event);
        client.start();
        return true;
    }

    public void setHttpFilter(HttpFilter httpFilter) {
        this.httpFilter = httpFilter;
    }

    /*
    * 抽象方法
    * 接口过滤
    */
    public HttpFilter getHttpFilter() {
        return httpFilter;
    }

    public String getJpushRegisterID() {
        return jPushListener == null ? null : jPushListener.getJpushRegisterID();
    }

    /*
    * 基础数据
    */
    private String mAppToken;//应用Token
    private UserData mUserData;//用户数据
    private IMConfig mIMConfig;//即时通讯
    private SignatureBean mSignatureBean; //签约数据

    public void logout() {
        //清空数据
        setUserData(new UserData());
        setIMConfig(new IMConfig());
        if (logoutListener != null) {
            logoutListener.onLogout();
        }
    }

    public String getAppToken() {
        if (mAppToken == null) {
            mAppToken = SPUtils.get(context, SPUtils.APP_TOKEN, "").toString();
        }
        return mAppToken;
    }

    public void setAppToken(String appToken) {
        if (appToken != null) {
            mAppToken = appToken;
            SPUtils.put(context, SPUtils.APP_TOKEN, mAppToken);
        }
    }


    public UserData getUserData() {
        if (mUserData == null) {
            String data = SPUtils.get(context, SPUtils.USER_DATA, "").toString();
            mUserData = new GsonBuilder().create().fromJson(data, UserData.class);
            if (mUserData == null) {
                mUserData = new UserData();
            }
        }
        return mUserData;
    }

    public void setUserData(UserData userData) {
        if (userData != null) {
            mUserData = userData;
            SPUtils.put(context, SPUtils.USER_DATA, new GsonBuilder().create().toJson(mUserData));
        }
    }

    public void setSignatureData(SignatureBean userData) {
        if (userData != null) {
            mSignatureBean = userData;
            SPUtils.put(context, SPUtils.SIGNATURE_DATA, new GsonBuilder().create().toJson(userData));
        }
    }

    public SignatureBean getSignatureData() {
        if (mSignatureBean == null) {
            String data = SPUtils.get(context, SPUtils.SIGNATURE_DATA, "").toString();
            mSignatureBean = new GsonBuilder().create().fromJson(data, SignatureBean.class);
            if (mSignatureBean == null) {
                mSignatureBean = new SignatureBean();
            }
        }
        return mSignatureBean;
    }

    public String getUserToken() {
        return getUserData().mUserToken;
    }


    public IMConfig getIMConfig() {
        if (mIMConfig == null) {
            String data = SPUtils.get(context, SPUtils.IM_CONFIG, "").toString();
            mIMConfig = new GsonBuilder().create().fromJson(data, IMConfig.class);
            if (mIMConfig == null) {
                mIMConfig = new IMConfig();
            }
        }
        return mIMConfig;
    }

    public void setIMConfig(IMConfig config) {
        if (config != null) {
            mIMConfig = config;
            SPUtils.put(context, SPUtils.IM_CONFIG, new GsonBuilder().create().toJson(mIMConfig));
        }
    }


    public boolean hasAppToken() {
        return !TextUtils.isEmpty(getAppToken());
    }

    public boolean hasUserToken() {
        return !TextUtils.isEmpty(getUserToken());
    }

    public NewHttpFilter getNewHttpFilter(){
        return new NewHttpFilter(){

        };
    }

    public String getDeviceID() {
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception e) {
            return "-1";
        }
    }



    public String getMacid() {
        try {
            WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            String WLANMAC = wm.getConnectionInfo().getMacAddress();
            return WLANMAC ;
        }catch (Exception e){
            e.printStackTrace();
            return "-1";
        }
    }

    public String getBluetoothid() {
        try {
            BluetoothAdapter mBlueth= BluetoothAdapter.getDefaultAdapter();
            String mBluethId= mBlueth.getAddress();
            return mBluethId;
        }catch (Exception e){
            e.printStackTrace();
            return "-1";
        }
    }

    //we make this look like a valid IMEI
    public String getUniqueID(){
        String m_szDevIDShort = "35" +
                Build.BOARD.length()%10 +
                Build.BRAND.length()%10 +
                Build.CPU_ABI.length()%10 +
                Build.DEVICE.length()%10 +
                Build.DISPLAY.length()%10 +
                Build.HOST.length()%10 +
                Build.ID.length()%10 +
                Build.MANUFACTURER.length()%10 +
                Build.MODEL.length()%10 +
                Build.PRODUCT.length()%10 +
                Build.TAGS.length()%10 +
                Build.TYPE.length()%10 +
                Build.USER.length()%10 +
                Build.FINGERPRINT.length()%10 +
                Build.HARDWARE.length()%10; //13 digits

        String m_szLongID = getDeviceID() + getMacid() + getBluetoothid() + m_szDevIDShort + getUserData().mUserId;
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(),0,m_szLongID.length());

        // get md5 bytes
        byte p_md5Data[] = m.digest();

        // create a hex string
        String m_szUniqueID = new String();
        for (int i=0;i<p_md5Data.length;i++) {
            int b = (0xFF & p_md5Data[i]);
            if (b <= 0xF) {
                m_szUniqueID+="0";
            }

            // add number to string
            m_szUniqueID+=Integer.toHexString(b);
        }

        // hex string to uppercase
        m_szUniqueID = m_szUniqueID.toUpperCase();
        return m_szUniqueID;
    }
}
