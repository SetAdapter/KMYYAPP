package com.kmwlyy.doctor.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/18.
 */
public class ConsultBean {
    public String UserConsultID;
    public String UserID;
    public String MemberID;
    public String DoctorID;
    public String ConsultContent;
    public String ConsultTime;
    public String FinishTime;
    public String ConsultType;
    public int InquiryType;//问诊类型：0-图文咨询，1-报告解读
    public String ConsultState;
    public MemberBean UserMember;
    public DoctorBean Doctor;
    public RoomBean Room;
    public UserBean User;
    public ArrayList<Msg> Messages;
    public ArrayList<UserFile> UserFiles;

    public static class UserFile {
        public String UrlPrefix;
        public String FileUrl;
        public String FileType;
    }

}
