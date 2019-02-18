package io.agora.core;

import java.io.Serializable;

/**
 * Created by Administrator on 2016-8-25.
 */
public class ConsultBean implements Serializable{

    public String mRegisterID;//预约ID

    public String mAppID;//官网dashboard里边分配的唯一性的一个license
    public String mVendorKey;//厂商KEY
    public String mRoomID;//房间ID

    public int mLocalID;//自己ID
    public int mRemoteID;//对方ID
    public int mCallType;//呼叫类型
    public int mUserType;//用户类型

    public String mUserID;//对方ID
    public String mUserFace;//对方头像
    public String mUserName;//对方名称
    public String mUserPhone;//对方电话
    public String mUserInfo;//对方信息

}
