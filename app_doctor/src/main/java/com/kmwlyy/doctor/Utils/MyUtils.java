package com.kmwlyy.doctor.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.Activity.WelcomeActivity;
import com.kmwlyy.doctor.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2016/8/12.
 */
public class MyUtils {

    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    static SimpleDateFormat dateFormatOut1 = new SimpleDateFormat("MM-dd");
    static SimpleDateFormat dateFormatOut2 = new SimpleDateFormat("yyyy-MM-dd");
    /* 语音视频咨询类型 */
    public class VoiceVideo {
        public static final int TYPE_VOICE = 2;//语音资讯
        public static final int TYPE_VIDEO = 3;//视频资讯
    }

    public static String convertTime(Context context, String dateStr) {

        long time = System.currentTimeMillis();
        long duration = 0;
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
            duration = (time - date.getTime()) / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
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
                    } else if (duration < 365) {
                        return dateFormatOut1.format(date.getTime());
                    } else {
                        return dateFormatOut2.format(date.getTime());
                    }
                }
            }
        }
    }


    /**
     * 获取本周 星期一的日期
     * 参数由 yyyy年MM月dd日 HH:mm:ss 组成，可以自定义格式
     */
    public static String getCurrentDate(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (day_of_week == 0)
            day_of_week = 7;
        c.add(Calendar.DATE, -day_of_week + 1);
        return formatter.format(c.getTime());
    }

    /**
     * 返回年月  如 201701
     */
    public static String getCurrentYearMonth(){
        String time = ""+Calendar.getInstance().get(Calendar.YEAR);
        if(((Calendar.getInstance().get(Calendar.MONTH)+1)) < 10){
            time = time + "0" + (Calendar.getInstance().get(Calendar.MONTH)+1);
        }else{
            time = time + (Calendar.getInstance().get(Calendar.MONTH)+1);
        }
        return time;
    }


//    /**
//     * 获取月份
//     * 参数由 yyyy年MM月dd日 HH:mm:ss 组成，可以自定义格式
//     */
//    public void String getMonth(String format) {
//
//    }

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
     * 根据类型返回性别
     */
    public static String getGendar(Context mContext, String gendar) {
        String str = "";
        switch (gendar) {
            case "0":
                str = mContext.getResources().getString(R.string.string_male);
                break;
            case "1":
                str = mContext.getResources().getString(R.string.string_female);
                break;
            case "2":
                str = mContext.getResources().getString(R.string.string_unknow);
                break;
        }
        return str;
    }

    /**
     * 根据类型返回婚姻
     */
    public static String getMarriage(Context mContext, String marriage) {
        String str = "";
        switch (marriage) {
            case "0":
                str = mContext.getResources().getString(R.string.string_single_dog);
                break;
            case "1":
                str = mContext.getResources().getString(R.string.string_married);
                break;
            case "2":
                str = mContext.getResources().getString(R.string.string_unknow);
                break;
        }
        return str;
    }
    /**
     * 根据医生职位
     */
    public static String getTitle(Context mContext, String title) {
        String str = "";
        switch (title) {
            case "1":
                str = mContext.getResources().getString(R.string.string_doc_title1);
                break;
            case "2":
                str = mContext.getResources().getString(R.string.string_doc_title2);
                break;
            case "3":
                str = mContext.getResources().getString(R.string.string_doc_title3);
                break;
            case "4":
                str = mContext.getResources().getString(R.string.string_doc_title4);
                break;
        }
        return str;
    }

    /**
     * 根据类型返回订单状态
     */
    public static String getState(Context mContext, String state,TextView mTextview) {
        String str = "";
        switch (state) {
            case "0":
                str = mContext.getResources().getString(R.string.string_order_type1);
                mTextview.setTextColor(mContext.getResources().getColor(R.color.color_main_yellow));
                break;
            case "1":
                str = mContext.getResources().getString(R.string.string_order_type2);
                mTextview.setTextColor(mContext.getResources().getColor(R.color.app_color_main));
                break;
            case "2":
                str = mContext.getResources().getString(R.string.string_order_type3);
                mTextview.setTextColor(mContext.getResources().getColor(R.color.color_sub_string));
                break;
            case "3":
                str = mContext.getResources().getString(R.string.string_order_type4);
                mTextview.setTextColor(mContext.getResources().getColor(R.color.color_sub_string));
                break;
            case "7":
                str = mContext.getResources().getString(R.string.string_order_type5);
                mTextview.setTextColor(mContext.getResources().getColor(R.color.color_sub_string));
                break;
            default:
                str = "";
                break;
        }
        return str;
    }

    /**
     * 根据类型返回订单状态
     */
    public static String getConsultType(Context mContext, String state, int type) {
        String str = "";
        if (type == 1){
            str = mContext.getResources().getString(R.string.string_consult_inquiryType);
        }else {
            switch (state) {
                case "0":
                    str = mContext.getResources().getString(R.string.string_consult_type1);
                    break;
                case "1":
                    str = mContext.getResources().getString(R.string.string_consult_type2);
                    break;
                case "2":
                    str = mContext.getResources().getString(R.string.string_consult_type3);
                    break;
                case "3":
                    str = mContext.getResources().getString(R.string.string_consult_type4);
                    break;
                case "4":
                    str = mContext.getResources().getString(R.string.string_consult_type5);
                    break;
                case "5":
                    str = mContext.getResources().getString(R.string.string_consult_type6);
                    break;
                default:
                    str = "";
                    break;
            }
        }
        return str;
    }

    /**
     * 根据类型返回订单状态 //状态  0-未筛选、1-未领取、2-已领取、3-未回复、4-已回复、5-已完成
    **/
    public static String getConsultState(Context mContext, String state,TextView mTextview) {
        String str = "";
        switch (state) {
            case "0":
            case "1":
            case "2":
            case "3":
                str = mContext.getResources().getString(R.string.string_consult_state0);
                mTextview.setTextColor(mContext.getResources().getColor(R.color.color_main_yellow));
                break;
            case "4":
                str = mContext.getResources().getString(R.string.string_consult_state1);
                mTextview.setTextColor(mContext.getResources().getColor(R.color.app_color_main));
                break;
            case "5":
                str = mContext.getResources().getString(R.string.string_consult_state2);
                mTextview.setTextColor(mContext.getResources().getColor(R.color.color_sub_string));
                break;
            default:
                str = "";
                break;
        }
        return str;
    }



    /**
     * 根据类型返回订单状态
     */
    public static String getFamilyDoctorState(Context mContext, String state, TextView mTextview) {
        String str = "";
        switch (state) {
            case "0":
                str = mContext.getResources().getString(R.string.string_family_type1);
                mTextview.setTextColor(mContext.getResources().getColor(R.color.yellow));
                break;
            case "1":
                str = mContext.getResources().getString(R.string.string_family_type2);
                mTextview.setTextColor(mContext.getResources().getColor(R.color.app_color_main));
                break;
            case "2":
                str = mContext.getResources().getString(R.string.string_family_type3);
                mTextview.setTextColor(mContext.getResources().getColor(R.color.color_sub_string));
                break;
            default:
                str = "";
                mTextview.setTextColor(mContext.getResources().getColor(R.color.color_sub_string));
                break;
        }
        return str;
    }

    /**
     * 根据类型返回订单状态
     */
    public static String getVoiceTypeState(Context mContext, int state, TextView mTextview) {
        String str = "";
        switch (state) {
            case 0:
                str = mContext.getResources().getString(R.string.string_voice_type1);
                mTextview.setTextColor(mContext.getResources().getColor(R.color.color_main_yellow));
                break;
            case 1:
                str = mContext.getResources().getString(R.string.string_voice_type2);
                mTextview.setTextColor(mContext.getResources().getColor(R.color.app_color_main));
                break;
            case 2:
                str = mContext.getResources().getString(R.string.string_voice_type3);
                mTextview.setTextColor(mContext.getResources().getColor(R.color.color_tag_string));
                break;
            default:
                str = "";
                mTextview.setTextColor(mContext.getResources().getColor(R.color.color_tag_string));
                break;
        }
        return str;
    }

    /**
     * 根据就诊记录类型
     */
    public static String getMedicalRecord(Context mContext, String state) {
        String str = "";
        switch (state) {
            case "0":
                str = mContext.getResources().getString(R.string.string_medical_record0);
                break;
            case "1":
                str = mContext.getResources().getString(R.string.string_medical_record1);
                break;
            case "2":
                str = mContext.getResources().getString(R.string.string_medical_record2);
                break;
            case "3":
                str = mContext.getResources().getString(R.string.string_medical_record3);
                break;
            case "4":
                str = mContext.getResources().getString(R.string.string_medical_record4);
                break;
            case "5":
                str = mContext.getResources().getString(R.string.string_medical_record5);
                break;
            default:
                str = "";
                break;
        }
        return str;
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
     * 跳到登录界面
     */
    public static void reLogin(Context mContext) {
        SPUtils.logout(mContext);
        Intent intent = new Intent(mContext, WelcomeActivity.class);
        intent.putExtra("msg", "reLogin");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }


    public static DisplayImageOptions getSquareDisplayOptions() {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.default_avatar) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.add_square) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.add_square) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new SimpleBitmapDisplayer()) // 设置成圆角图片
                .build(); // 构建完成
        return options;
    }

    /**
     * 返回年月的时间格式 如 201612
     * monthAfter 为当前月之前第N个月的时间。 可以为负数
     * @param monthAfter
     * @return
     */
    public static String getYearMonth(int monthAfter){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, monthAfter);
        SimpleDateFormat format =  new SimpleDateFormat("yyyyMM");
        return format.format(c.getTime());
    }

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
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
    /**
     * 比较年月日
     * return true 是今天或未来的时间    false 是以前的时间
     */
    public static boolean isFuture(int year, int month,int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        try {
            Date CurrentTime = sdf.parse(""+Calendar.getInstance().get(Calendar.YEAR)+
                                          ((Calendar.getInstance().get(Calendar.MONTH)+1)<10?"0"+(Calendar.getInstance().get(Calendar.MONTH)+1):(Calendar.getInstance().get(Calendar.MONTH)+1)) +
                                           (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)<10?"0"+(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)):(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)))                );
            Date InputTime = sdf.parse(""+year+((month+1)<10?"0"+(month+1):(month+1))+(day<10?"0"+day:day));
            if(CurrentTime.compareTo(InputTime)>0){
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean isTwoWeekLate(int year, int month,int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 14);
            Date CurrentTime = sdf.parse(""+calendar.get(Calendar.YEAR)+
                    ((calendar.get(Calendar.MONTH)+1)<10?"0"+(calendar.get(Calendar.MONTH)+1):(calendar.get(Calendar.MONTH)+1)) +
                    (calendar.get(Calendar.DAY_OF_MONTH)<10?"0"+(calendar.get(Calendar.DAY_OF_MONTH)):(calendar.get(Calendar.DAY_OF_MONTH)))                );
            Date InputTime = sdf.parse(""+year+((month+1)<10?"0"+(month+1):(month+1))+(day<10?"0"+day:day));
            if(CurrentTime.compareTo(InputTime)>0){
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }

    /*检测网络是否连接*/
    public static boolean isNetAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null){
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if(networkInfo != null && networkInfo.isConnected()){
                if(networkInfo.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }
/*    比较时间大小*/
    public static boolean beginTOend(Context context,String beginTime,String endTime){

        boolean large = false;

        if(beginTime == null || beginTime.equals("")){
            return large;
        }
        if(endTime == null || endTime.equals("")){
            return large;
        }

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");

        try {
            Date begin = dateFormat.parse(beginTime);
            Date end = dateFormat.parse(endTime);

            if(begin.getTime()<end.getTime()) {
                large = true;
            }else {
                large = false;
            }


        } catch (ParseException e) {
            e.printStackTrace();
        }

        return large;
    }

}
