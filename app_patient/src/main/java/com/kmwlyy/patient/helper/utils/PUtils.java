package com.kmwlyy.patient.helper.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.login.LoginActivity;
import com.kmwlyy.patient.MainActivity;
import com.kmwlyy.patient.PApplication;
import com.kmwlyy.patient.R;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Winson on 2016/8/6.
 */
public class PUtils {

    private static final String TAG = PUtils.class.getSimpleName();

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

    public static String convertUrl(String url) {
        if (url == null || url.startsWith("http")) {
            return url;
        }
//        return HttpClient.KMYY_URL + url;
        return "http://www.kmwlyy.com" + url;
    }

    public static int getRandomColor() {
        return Color.rgb((int) (255 * Math.random()), (int) (255 * Math.random()), (int) (255 * Math.random()));
    }

    public static void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    public static void closeDatabase(SQLiteDatabase db) {
        if (db != null) {
            db.close();
        }
    }

    public static void closeIo(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeIo(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeIo(Reader reader) {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeIo(Writer writer) {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void goToMyService(Context context, int serviceTab, int type, boolean isRestart) {
        if (DebugUtils.debug) {
            Log.d(TAG, "goToMyService position : " + serviceTab);
        }
        EventApi.MainTabSelect mainTab = new EventApi.MainTabSelect();
        mainTab.position = EventApi.MainTabSelect.MY_PROFILE;
        mainTab.secondPosition = serviceTab;
        mainTab.type = type;
        EventBus.getDefault().post(mainTab);

//        EventApi.MyServiceTab tab = new EventApi.MyServiceTab();
//        tab.position = serviceTab;
//        EventBus.getDefault().post(tab);
        if (isRestart) {
            CommonUtils.startActivity(context, MainActivity.class);
        }
    }

    public static boolean checkHaveUser(Context context) {
        return checkHaveUser(context, true);
    }

    public static boolean checkHaveUser(Context context, boolean startActivity) {
        if (BaseApplication.getInstance().getUserToken() == null) {
            if (startActivity) {
                CommonUtils.startActivity(context, LoginActivity.class);
            }
            return false;
        }
        return true;
    }

    public static boolean checkHaveSign() {
        if (TextUtils.isEmpty(BaseApplication.getInstance().getUserData().mIDNumber)) {
            return false;
        }
        return true;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 获取医生title
     * 1,//住院医师 2,//主治医师 3,//副主任医师 4,//主任医师
     *
     * @return title
     */
    public static String getDoctorTitle(Context context,String title)
    {
        try {
            switch (Integer.parseInt(title)){
                case 1:
                    return context.getString(R.string.in_hospital_doctor);
                case 2:
                    return context.getString(R.string.primary_doctor);
                case 3:
                    return context.getString(R.string.second_doctor);
                case 4:
                    return context.getString(R.string.director_doctor);
                default:
                    return "";
            }
        }catch (Exception e){
            return title;
        }
    }

    /**
     * convert “yyyy-MM-dd'T'HH:mm:ss.SSS” to “yyyy-MM-dd HH:mm”
     *
     * @return  "yyyy-MM-dd HH:mm"
     */
    public static String convertTimeSSS(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            SimpleDateFormat dateFormatOut2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return dateFormatOut2.format(dateFormat.parse(dateStr));
        }catch (Exception e){
            return dateStr;
        }

    }


    /**
     * convert “yyyy-MM-dd'T'HH:mm:ss” to “yyyy-MM-dd HH:mm”
     *
     * @return  "yyyy-MM-dd HH:mm"
     */
    public static String convertTime(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat dateFormatOut2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return dateFormatOut2.format(dateFormat.parse(dateStr));
        }catch (Exception e){
            return dateStr;
        }

    }

    /**
     * convert “yyyy-MM-dd'T'HH:mm:ss” to “yyyy-MM-dd”
     *
     * @return  "yyyy-MM-dd"
     */
    public static String convertTimeToDay(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat dateFormatOut2 = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormatOut2.format(dateFormat.parse(dateStr));
        }catch (Exception e){
            return dateStr;
        }

    }

    /**
     * 返回评价类型
     */
    public static String getEvaluateType(Context mContext, String state) {
        String str = "";
        switch (state) {
            case "0":
                str = mContext.getResources().getString(R.string.string_evaluate_record0);
                break;
            case "1":
                str = mContext.getResources().getString(R.string.string_evaluate_record1);
                break;
            case "2":
                str = mContext.getResources().getString(R.string.string_evaluate_record2);
                break;
            case "3":
                str = mContext.getResources().getString(R.string.string_evaluate_record3);
                break;
            case "4":
                str = mContext.getResources().getString(R.string.string_evaluate_record4);
                break;
            case "5":
                str = mContext.getResources().getString(R.string.string_evaluate_record5);
                break;
            case "6":
                str = mContext.getResources().getString(R.string.string_evaluate_record6);
                break;
            default:
                str = "";
                break;
        }
        return str;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置状态高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
