package com.kmwlyy.core.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Administrator on 2016-8-6.
 */
public class SPUtils {
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "APP_DATA";
    public static final String APP_TOKEN = "APP_TOKEN";
    public static final String USER_DATA = "USER_DATA";//用户信息
    public static final String IM_CONFIG = "IM_CONFIG";//即时通讯
    public static final String REG_AREAID = "REG_AREAID";//挂号区域ID
    public static final String REG_AREANAME = "REG_AREANAME";//挂号区域名称
    public static final String REG_LOC_CITYCODE = "REG_LOC_CITYCODE";//挂号定位到的CityCode
    public static final String DOCTOR_ID = "DOCTOR_ID";//医生id
    public static final String SIGNATURE_DATA = "SIGNATURE_DATA";//签约信息

    public static final String LOGIN_DATA = "LOGIN_DATA";
    public static final String REFRESH_TIME = "REFRESH_TIME";
    public static final String USER_NAME = "USER_NAME";
    public static final String PASSWORD = "PASSWORD";

    public static final String JPUSH_STATE = "JPUSH_STATE";

    public static class LoginInfo {
        public String userName;
        public String pwd;
    }

    public static void logout(Context context) {
        saveLoginInfo(context, null, null);
        saveLoginTime(context, 0);
    }

    public static long getLoginTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(LOGIN_DATA, Context.MODE_PRIVATE);
        return sp.getLong(REFRESH_TIME, 0);
    }

    public static void saveLoginTime(Context context, long time) {
        SharedPreferences sp = context.getSharedPreferences(LOGIN_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        ed.putLong(REFRESH_TIME, time);

        ed.apply();
    }

    public static void saveLoginInfo(Context context, String userName, String pwd) {
        SharedPreferences sp = context.getSharedPreferences(LOGIN_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();

        ed.putString(USER_NAME, userName);
        ed.putString(PASSWORD, pwd);

        ed.apply();
    }

    public static LoginInfo getLoginInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(LOGIN_DATA, Context.MODE_PRIVATE);

        LoginInfo info = new LoginInfo();
        info.userName = sp.getString(USER_NAME, null);
        info.pwd = sp.getString(PASSWORD, null);

        return info;
    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object) {

        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }

        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param context
     * @param key
     */
    public static void remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean contains(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.contains(key);
    }

    /**
     * 返回所有的键值对
     *
     * @param context
     * @return
     */
    public static Map<String, ?> getAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getAll();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
            editor.commit();
        }
    }

}
