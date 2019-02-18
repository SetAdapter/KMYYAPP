package com.kmwlyy.doctor.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xcj on 2017/7/6.
 */

public class QueryChatListBean implements Serializable {
    public String CurrentPage;
    public String PageSize;
    public String Keyword;
    public String MemberID;
    public String FamilyDoctorID;
    public String BeginDate;
    public String EndDate;
    public List<String> OPDType;
    public List<String> OrderCostType;
    public List<String> RoomState;
    public int OrderType;
}
