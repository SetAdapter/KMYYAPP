package com.kmwlyy.doctor.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Winson on 2017/7/6.
 */

public class ConsultBeanNew {
    public String OPDRegisterID;//\":\"8f8c435e09e24ce2af61d92fa2ff422e\",
    public String UserID;//\":null,
    public String DoctorID;//\":null,
    public String ScheduleID;//\":null,
    public String RegDate;//\":\"0001-01-01 00:00:00\",
    public String OPDDate;//\":\"2016-08-18 00:00:00\",
    public String Sympton;//\":null,
    public String PastMedicalHistory;//\":null,
    public String PresentHistoryIllness;//\":null,
    public int OPDType;//\":3,
    public double Fee;//\":0.0,
    public String MemberID;//\":null,
    public OrderChatBean.OrderBean Order;//\":null,
    public MemberBean Member;
    public DoctorBean Doctor;
    public Schedule Schedule;
    public RoomBean Room;
    public ArrayList<Messages> Messages;
    public String ConsultContent;
    private MedRecordBean record;//用于家庭医生服务记录列表的 子列表

    public MedRecordBean getRecord() {
        if (null == record) {
            return new MedRecordBean("", "", "");
        }
        return record;
    }

    public void setRecord(MedRecordBean record) {
        this.record = record;
    }

    public static class Messages {

        @SerializedName("MessageContent")
        public String messageContent;

    }

    public static class MessageContent {

        @SerializedName("MsgContent")
        public MsgContent msgContent;

        //MessageType : TIMSoundElem 声音，TIMImageElem 图片，TIMFileElem 文件，TIMTextElem 文本，TIMFaceElem 表情，TIMCustomElem 自定义
        @SerializedName("MsgType")
        public String msgType;

    }

    public static class MsgContent {

        @SerializedName("Desc")
        public String desc;

        @SerializedName("Text")
        public String text;

    }
}
