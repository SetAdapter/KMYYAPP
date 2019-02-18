package com.kmwlyy.doctor.model;

/**
 * Created by Administrator on 2016/8/26.
 */
public class RoomBean {
    public String RoomState; //状态 0=未就诊,1=候诊中,2=就诊中,3=已就诊,4=呼叫中,5=离开中
    public String ChannelID;
    public String Secret;
    public String ServiceType;
    public String BeginTime;
    public String EndTime;
    public String TotalTime;
    public int RoomType;
    public String ConversationRoomID;//\":null,
    public String ServiceID;//\":null,
}
