package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;
import com.kmwlyy.core.net.bean.UserMember;

/**
 * Created by Winson on 2016/8/18.
 */
public class UserConsult {

    public static final int REPLAYED = 1;
    public static final int NOT_REPLAYED = 2;

    @SerializedName("UserConsultID")
    public String mUserConsultID;

    @SerializedName("UserID")
    public String mUserID;//": "9A4C83966C784DD5BEFA68766591A272",

    @SerializedName("MemberID")
    public String mMemberID;//": "77C5CF07923A4E3D8121F628336527B8",

    @SerializedName("DoctorID")
    public String mDoctorID;//": "89F9E5907FD04DBF96A9867D1FA30396",

    @SerializedName("ConsultContent")
    public String mConsultContent;//": "咨询内容",

    @SerializedName("ConsultTime")
    public String mConsultTime;//": "2016-08-18T00:00:00",

    @SerializedName("FinishTime")
    public String mFinishTime;//": "2016-08-18T00:00:00",

    @SerializedName("ConsultType")
    public int mConsultType; //问诊类型：0-图文咨询，1-报告解读

    @SerializedName("InquiryType")
    public int mInquiryType;//":

    @SerializedName("ConsultState")
    public int mConsultState;//": 0,

    @SerializedName("Deletable")
    public boolean mDeletable;//是否可删除
    @SerializedName("Cancelable")
    public boolean mCancelable;//是否可取消


    @SerializedName("UserMember")
    public UserMember mUserMember;

    @SerializedName("Doctor")
    public Doctor.ListItem mDoctor;

    @SerializedName("User")
    public Doctor.User mUser;

    @SerializedName("Room")
    public Room mRoom;

    @SerializedName("Order")
    public Order mOrder;

//            "UserMember": {
//        "MemberID": "77C5CF07923A4E3D8121F628336527B8",
//                "UserID": "9A4C83966C784DD5BEFA68766591A272",
//                "MemberName": "郭明",
//                "Relation": 1,
//                "Gender": 1,
//                "Marriage": 1,
//                "Birthday": "19890202",
//                "Mobile": "18688941654",
//                "IDType": 1,
//                "IDNumber": "0",
//                "Address": "",
//                "Email": "18688941654@qq.com",
//                "PostCode": "0",
//                "Age": 0,
//                "IsDefault": false
//    },
//            "Doctor": {
//        "DoctorID": "89F9E5907FD04DBF96A9867D1FA30396",
//                "DoctorName": "邱浩强",
//                "Gender": 0,
//                "Marriage": 0,
//                "IDType": 0,
//                "IsConsultation": false,
//                "IsExpert": false,
//                "Specialty": "高血压 糖尿病 恶性肿瘤 其他",
//                "DepartmentID": "A8064D2DAE3542B18CBD64F467828F57",
//                "Title": "",
//                "CheckState": 0,
//                "Sort": 0
//    },
//            "User": {
//        "UserCNName": "18688941654",
//                "UserENName": "20168",
//                "UserType": 1,
//                "PhotoUrl": "http://www.kmwlyy.com// /static/images/unknow.png",
//                "Score": 0,
//                "Star": 0,
//                "Comment": 0,
//                "Good": 0,
//                "Fans": 0,
//                "Grade": 0,
//                "Checked": 0,
//                "RegTime": "0001-01-01T00:00:00",
//                "CancelTime": "0001-01-01T00:00:00",
//                "UserState": 0,
//                "UserLevel": 0,
//                "Terminal": 0,
//                "LastTime": "0001-01-01T00:00:00",
//                "identifier": 0
//    }

    public static class Room {

        @SerializedName("RoomState")
        public int mRoomState;//": 1,//状态 0=未就诊,1=候诊中,2=就诊中,3=已就诊,4=呼叫中,5=离开中

        @SerializedName("ChannelID")
        public String mChannelID;//": 1,

        @SerializedName("Secret")
        public String mSecret;//": ""

    }

    public static class AddResp {
//        {\"UserConsultID\":\"f48bc8e2a3ad493bb50c657476f29c08\",\"ChannelID\":211,\"ActionStatus\":\"Success\",\"ErrorInfo\":\"预约成功\",\"OrderNO\":\"TW2016082509225516041116\"}

        @SerializedName("UserConsultID")
        public String mUserConsultID;

        @SerializedName("ChannelID")
        public String mChannelID;

        @SerializedName("ActionStatus")
        public String mActionStatus;

        @SerializedName("OrderNO")
        public String mOrderNO;

        @SerializedName("ErrorInfo")
        public String mErrorInfo;


    }

}
