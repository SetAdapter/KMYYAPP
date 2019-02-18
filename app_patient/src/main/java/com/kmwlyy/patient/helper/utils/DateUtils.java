package com.kmwlyy.patient.helper.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
	/**
     * 通过年份和月份 得到当月的日子
     * 
     * @param year
     * @param month
     * @return
     */
    public static int getMonthDays(int year, int month) {
		month++;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12: 
		    return 31;
		case 4:
		case 6:
		case 9:
		case 11: 
		    return 30;
		case 2:
			if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)){
				return 29;
			}else{
				return 28;
			}
		default:
			return  -1;
		}
    }
    /**
     * 返回当前月份1号位于周几
     * @param year
     * 		年份
     * @param month
     * 		月份，传入系统获取的，不需要正常的
     * @return
     * 	日：1		一：2		二：3		三：4		四：5		五：6		六：7
     */
    public static int getFirstDayWeek(int year, int month){
    	Calendar calendar = Calendar.getInstance();
    	calendar.set(year, month, 1);
    	Log.d("DateView", "DateView:First:" + calendar.getFirstDayOfWeek());
    	return calendar.get(Calendar.DAY_OF_WEEK);
    }

	/**
	 * 通过出生日期获取 年龄
	 * @param birthday
	 * @return
	 */
	public static int getAgeByBirth(Date birthday) {
		int age = 0;
		try {
			Calendar now = Calendar.getInstance();
			now.setTime(new Date());// 当前时间

			Calendar birth = Calendar.getInstance();
			birth.setTime(birthday);

			if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
				age = 0;
			} else {
				if (now.get(Calendar.YEAR) > birth.get(Calendar.YEAR)){
					age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
					if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
						age += 1;
					}
				}
			}
			return age;
		} catch (Exception e) {//兼容性更强,异常后返回数据
			return 0;
		}
	}

	/**
	 * 将一个时间戳转化成时间字符串，自定义格式
	 *
	 * @param time
	 * @param format
	 *            如 yyyy-MM-dd HH:mm:ss
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
	 * @param timeStr
	 * @param format
	 * @return
	 */
	public static long timeString2long(String timeStr, String format){

		if(null == timeStr || "".equals(timeStr))return -1l;

		Date date;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08"));
			date = simpleDateFormat .parse(timeStr);

			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return -1l;
	}
}
