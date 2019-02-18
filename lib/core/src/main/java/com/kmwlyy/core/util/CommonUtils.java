package com.kmwlyy.core.util;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;

import com.kmwlyy.core.R;
import com.kmwlyy.core.net.HttpCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * Created by Winson on 2016/8/6.
 */
public class CommonUtils {

    public static final String PHONE_PATTERN = "(13[0-9]|14[0-9]|15[0-9]|18[0-9]|17[0-9])\\d{8}";

    public static boolean startActivity(Context context, Class activityClass) {
        if (context == null) {
            return false;
        }
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
        return true;
    }

    /**
     * 生成随机数，每次调用接口不能重复，长度10到40的字母或数字组成
     */
    public static String createRandomString() {
        int length = 20; //长度临时设置为20位

        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 生成sign字符串，用于发请求的头部字段
     * (由apptoken=@apptoken&noncestr=@noneStr&usertoken=@userToken&appkey=@ appkey 串MD5加密后转成大写)
     */
    public static String createSignString(String noneStr, String appToken, String userToken) {
        String str = "apptoken=" + appToken + "&noncestr=" + noneStr + (userToken == null || userToken.equals("") ? "" : "&usertoken=" + userToken) + "&appkey=" + HttpCode.APP_KEY;
        return toMD5(str).toUpperCase();
    }

    /**
     * MD5 加密
     */
    public static String toMD5(String data) {

        try {
            // 实例化一个指定摘要算法为MD5的MessageDigest对象
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // 重置摘要以供再次使用
            md5.reset();
            // 使用bytes更新摘要
            md5.update(data.getBytes());
            // 使用指定的byte数组对摘要进行最的更新，然后完成摘要计算
            return toHexString(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    // 将字符串中的每个字符转换为十六进制
    private static String toHexString(byte[] bytes) {

        StringBuilder hexstring = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexstring.append('0');
            }
            hexstring.append(hex);

        }

        return hexstring.toString();
    }

    public static boolean checkPwd(String pwd) {
        if (pwd == null) {
            return false;
        }
        int length = pwd.length();
        if (length < 6 || length > 18) {
            return false;
        }
        return true;
    }

    public static boolean checkUserName(String userName) {
        if (userName == null) {
            return false;
        }
        if (userName.length() < 2 || userName.length() > 30) {
            return false;
        }
        return true;
    }

    public static boolean checkPhone(String phone) {
        if (phone == null) {
            return false;
        }
        return phone.matches(PHONE_PATTERN);
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

    /**
     * 获取系统时间
     * 参数由 yyyy年MM月dd日 HH:mm:ss 组成，可以自定义格式
     */
    public static String getCurrentDate(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 获取系统时间 后面第几天的日期,或往前几天
     * date = 2016-01-01
     * format = yyyy-MM-dd 想要的格式
     * dayAfter = 正数为往后几天，负数为往前几天
     */
    public static String getFutureDate(String dateStr, String format, int dayAfter) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;

        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, dayAfter);//把日期往后增加N天.整数往后推,负数往前移动
        Date futureDate = calendar.getTime();   //这个时间就是日期往后推的结果
        return formatter.format(futureDate);
    }

    /**
     * 修改时间格式
     */
    public static String changeTimeFormat(String dateStr, String format){
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date=sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        return formatter.format(date);
    }

    /**
     * goToMarket
     *
     * @return  success or fail
     */
    public static boolean goToMarket(Context context) {
        try {
            String packageName = context.getPackageName();
            Uri uri = Uri.parse("market://details?id=" + packageName);
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(goToMarket);
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
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
     * convert “yyyy-MM-dd HH:mm:ss” to “yyyy-MM-dd”
     *
     * @return  "刚刚" or "x分钟前" or "x小时前" or "x天前" or "yyyy-MM-dd"
     */
    public static String convertTime(Context context, String dateStr) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat dateFormatOut2 = new SimpleDateFormat("yyyy-MM-dd");
        long time = System.currentTimeMillis();
        long duration = 0;
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
            duration = (time - date.getTime()) / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        if (duration < 60) {
            return context.getResources().getString(R.string.just_now);
        } else {
            duration = duration / 60 ;
            if (duration < 60) {
                return String.format(context.getResources().getString(R.string.minute_ago), duration);
            } else {
                duration = duration / 60;
                if (duration < 24) {
                    return String.format(context.getResources().getString(R.string.hour_ago), duration);
                } else {
                    duration = duration / 24;
                    if (duration < 30) {
                        return String.format(context.getResources().getString(R.string.day_ago), duration);
                    } else {
                        return dateFormatOut2.format(date.getTime());
                    }
                }
            }
        }
    }


    /**
     * 精确到小数点后两位
     *
     * @return  String
     */
    public static String convertTowDecimalStr(float l){
        BigDecimal bd = new BigDecimal(Float.toString(l));
        bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }

    /**
     * 精确到小数点后两位
     *
     * @return  String
     */
    public static String convertTowDecimalStr(double d){
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }



    /**
     * 获取屏幕宽度
     *
     * @return  int
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return dm.widthPixels;

    }
    /**
     * 获取屏幕高度
     *
     * @return  int
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return dm.heightPixels;

    }

    /* 检查定位权限 */
    public static boolean checkReadStoragePermissions(Context context,Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            if (context.checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                fragment.requestPermissions(permissions, 0);
                return false;
            }
            else {
                return true;
            }
        }
        return true;
    }

    //判断两个时间 的差值是否是 day 天内
    public static boolean compareTime(String time1,String time2,int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        long mDay = 0;
        try {
            Date date1 = sdf.parse(time1);
            Date date2 = sdf.parse(time2);
            long l = date2.getTime() - date1.getTime();
            mDay = l / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if( mDay <= 0 || mDay > day){
            return true;
        }
        return false;
    }

    /**毫秒转日期*/
    public static String mm2time(long l) {
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String           date = sdf.format(new Date(l));
        return date;
    }

    /**
     * 获取MD5加密
     *
     * @param pwd
     *            需要加密的字符串
     * @return String字符串 加密后的字符串
     */
    public static String getMD5(String pwd) {
        try {
            // 创建加密对象
            MessageDigest digest = MessageDigest.getInstance("md5");

            // 调用加密对象的方法，加密的动作已经完成
            byte[] bs = digest.digest(pwd.getBytes());
            // 接下来，我们要对加密后的结果，进行优化，按照mysql的优化思路走
            // mysql的优化思路：
            // 第一步，将数据全部转换成正数：
            String hexString = "";
            for (byte b : bs) {
                // 第一步，将数据全部转换成正数：
                // 解释：为什么采用b&255
                /*
                 * b:它本来是一个byte类型的数据(1个字节) 255：是一个int类型的数据(4个字节)
                 * byte类型的数据与int类型的数据进行运算，会自动类型提升为int类型 eg: b: 1001 1100(原始数据)
                 * 运算时： b: 0000 0000 0000 0000 0000 0000 1001 1100 255: 0000
                 * 0000 0000 0000 0000 0000 1111 1111 结果：0000 0000 0000 0000
                 * 0000 0000 1001 1100 此时的temp是一个int类型的整数
                 */
                int temp = b & 255;
                // 第二步，将所有的数据转换成16进制的形式
                // 注意：转换的时候注意if正数>=0&&<16，那么如果使用Integer.toHexString()，可能会造成缺少位数
                // 因此，需要对temp进行判断
                if (temp < 16 && temp >= 0) {
                    // 手动补上一个“0”
                    hexString = hexString + "0" + Integer.toHexString(temp);
                } else {
                    hexString = hexString + Integer.toHexString(temp);
                }
            }
            return hexString;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
}
