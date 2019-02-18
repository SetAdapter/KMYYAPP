package io.agora.core;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2017/1/6
 */
public class MeetingRoomBean implements Serializable {

    public String mRegisterID;//预约ID

    public String mVendorKey;//厂商KEY
    public String mRoomID;//房间ID

    public int mLocalID;//自己ID
    public int mRemoteID;//对方ID


    public String mUserID;//对方ID
    public String mUserName;//对方名称

    public ArrayList<MeetingDoctor> doctors = new ArrayList<>();//声网uid和医生名的关联结构




}
