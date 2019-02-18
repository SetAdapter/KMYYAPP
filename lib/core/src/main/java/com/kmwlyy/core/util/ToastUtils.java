package com.kmwlyy.core.util;

/**
 * Created by Administrator on 2016-8-6.
 */

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理类
 */
public class ToastUtils {

    private ToastUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;
    private static long oneTime = 0;
    private static long twoTime = 0;
    private static String oldMsg;
    private static Toast mToast;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, String message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, String message) {
        show(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        show(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, String message, int duration) {
        if (isShow) {
            if (mToast == null) {
                mToast = Toast.makeText(context, message, duration);
                mToast.show();
                oneTime = System.currentTimeMillis();
            } else {
                twoTime = System.currentTimeMillis();
                if (message.equals(oldMsg)) {
                    if (twoTime - oneTime > duration) {
                        mToast.show();
                    }
                } else {
                    mToast.setText(message);
                    oldMsg = message;
                    mToast.show();
                }
            }
            oneTime = twoTime;
        }
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        show(context, context.getString(message), duration);
    }
}
