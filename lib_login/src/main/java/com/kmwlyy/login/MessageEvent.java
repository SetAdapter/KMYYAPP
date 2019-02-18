package com.kmwlyy.login;

public class MessageEvent {
    public static final int TYPE_DOCTOR_ORDER =1;
    public static final int TYPE_DOCTOR_RECIPE =2;
    public static final int TYPE_DOCTOR_CONSULT =3;
    public static final int TYPE_USER_CONSULT =4;
    public static final int TYPE_DOCTOR_CONSULT_CHAT =5;
    public static final int TYPE_DOCTOR_CONSULT_VOICE =6;
    public int mType;

    public MessageEvent(int type) {
        mType = type;
    }
}
