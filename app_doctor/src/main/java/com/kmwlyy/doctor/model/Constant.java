package com.kmwlyy.doctor.model;


import android.content.Context;



import com.kmwlyy.doctor.R;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/8.
 */
public class Constant implements Serializable{
    public final static String MOBILE_PATTERN = "[1][34578][0-9]{9}";

    public final static String TOKEN_EXPIRE = "appToken 过期";

    public final static String CONSULT_NOT_FREE = "2";
    public final static String CONSULT_FREE = "0";
    public final static String CONSULT_ALL = "-1";

    /**
     * 咨询类别
     */
    public final static String OPDTYPE_CHAT = "1";
    public final static String OPDTYPE_CHAT_SERVICE = "9";
    public final static String OPDTYPE_VOICE = "2";
    public final static String OPDTYPE_VIDEO = "3";
    public final static String OPDTYPE_FAMILY = "4";
    public final static String OPDTYPE_VOICE_VIDEO = "5";
    public final static String OPDTYPE_RECORD = "6";
    public final static String OPDTYPE_ORDER_CHAT = "7";
    public final static String OPDTYPE_ORDER_VOICE = "8";

    public static String[] getBank(Context context){
        String[] Banks = new String[]{ context.getResources().getString(R.string.string_bank1),
                context.getResources().getString(R.string.string_bank2),context.getResources().getString(R.string.string_bank3),context.getResources().getString(R.string.string_bank4),context.getResources().getString(R.string.string_bank5),
                context.getResources().getString(R.string.string_bank6),context.getResources().getString(R.string.string_bank7),context.getResources().getString(R.string.string_bank8),context.getResources().getString(R.string.string_bank9),
                context.getResources().getString(R.string.string_bank10),context.getResources().getString(R.string.string_bank11),context.getResources().getString(R.string.string_bank12),context.getResources().getString(R.string.string_bank13),context.getResources().getString(R.string.string_bank14),
                context.getResources().getString(R.string.string_bank15),context.getResources().getString(R.string.string_bank16),context.getResources().getString(R.string.string_bank17),context.getResources().getString(R.string.string_bank18),context.getResources().getString(R.string.string_bank19),
                context.getResources().getString(R.string.string_bank20),context.getResources().getString(R.string.string_bank21),context.getResources().getString(R.string.string_bank22),context.getResources().getString(R.string.string_bank23),context.getResources().getString(R.string.string_bank24),
                context.getResources().getString(R.string.string_bank25),context.getResources().getString(R.string.string_bank26),context.getResources().getString(R.string.string_bank27),context.getResources().getString(R.string.string_bank28),context.getResources().getString(R.string.string_bank29)  };
        return Banks;
    }
}
