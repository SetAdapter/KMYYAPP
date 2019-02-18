package com.kmwlyy.patient.weight;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间操作工具类
 * <p>
 * Created by fangs on 2017/3/22.
 */
public class TimeUtils {

    private TimeUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 将一个时间戳转化成时间字符串，自定义格式
     *
     * @param time
     * @param format 如 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String Long2DataString(long time, String format) {
        if (time == 0) {
            return "";
        }

        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        return sdf.format(date);
    }

    /**
     * 将一个时间字符串转换为时间戳，自定义格式
     *
     * @param timeStr
     * @param format
     * @return
     */
    public static long timeString2long(String timeStr, String format) {

        if (null == timeStr || "".equals(timeStr)) return -1L;

        Date date;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
            date = simpleDateFormat.parse(timeStr);

            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1L;
    }

    /**
     * 已知道年和月份 来获取当月有多少天
     */
    public static int getDayOfMonth(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 0); //输入类型为int类型
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth;
    }

    /**
     * 获取当前的年月日
     *
     * @return
     */
    public static String getCurrentDate() {
        //当前年的月日
        SimpleDateFormat adf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDay = adf.format(new Date());
        return currentDay;
    }


    /**
     * 获取当前时间 所在的 本月的第一天 格式都是yyyy-MM-dd
     * @return
     */
    public static String getCurrentMonthDefaultStartDay() {
        String currentDate = getCurrentDate();
        String temp_years = currentDate.substring(0, 4);
        String temp_months = currentDate.substring(5, 7);
//        Log.i("nihao", temp_months + "");
        String defaultStartDay = temp_years + "-" + temp_months + "-" + "01";
        return defaultStartDay;
    }
}
