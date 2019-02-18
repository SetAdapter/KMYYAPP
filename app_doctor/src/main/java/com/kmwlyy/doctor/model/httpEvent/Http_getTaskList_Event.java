package com.kmwlyy.doctor.model.httpEvent;

import com.alibaba.fastjson.JSON;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.doctor.model.Constant;
import com.kmwlyy.doctor.model.ConsultBean;
import com.kmwlyy.doctor.model.ConsultBeanNew;
import com.kmwlyy.doctor.model.QueryChatListBean;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xcj on 2017/7/5.
 */

/**
 * 获取咨询我的记录
 */
public class Http_getTaskList_Event extends HttpEvent<List<ConsultBeanNew>> {

    public Http_getTaskList_Event(String CurrentPage, String PageSize, String type, HttpListener listener) {
        super(listener);
        mReqAction = "/Task/getTaskList";
        mReqMethod = HttpClient.POST;
        noParmName = true;
        mReqParams = new HashMap<>();
        QueryChatListBean bean = new QueryChatListBean();
        bean.CurrentPage = CurrentPage;
        bean.PageSize = PageSize;
        bean.OrderType = 1;

        switch (type){
            case Constant.OPDTYPE_CHAT_SERVICE:
                bean.OPDType = new ArrayList<String>() {{
                    add("1");
                }};
                //0, 1, 2, 4, 6, 7
//                bean.RoomState = new ArrayList<String>() {{
//                    add("0");
//                    add("1");
//                    add("2");
//                    add("4");
//                    add("6");
//                    add("7");
//                }};
                break;
            case Constant.OPDTYPE_CHAT:
                bean.OPDType = new ArrayList<String>() {{
                    add("1");
                }};
                //0, 1, 2, 4, 6, 7
                bean.RoomState = new ArrayList<String>() {{
                    add("0");
                    add("1");
                    add("2");
                    add("4");
                    add("6");
                    add("7");
                }};
                bean.BeginDate = CommonUtils.getCurrentDate("yyyy-MM-dd");
                bean.EndDate = CommonUtils.getFutureDate(CommonUtils.getCurrentDate("yyyy-MM-dd"), "yyyy-MM-dd", 1);
                break;
            case Constant.OPDTYPE_VOICE:
                bean.OPDType = new ArrayList<String>() {{
                    add("2");
                    add("3");
                }};
                bean.RoomState = new ArrayList<String>() {{
                    add("0");
                    add("1");
                    add("2");
                    add("4");
                    add("6");
                    add("7");
                }};
                bean.BeginDate = CommonUtils.getCurrentDate("yyyy-MM-dd");
                bean.EndDate = CommonUtils.getFutureDate(CommonUtils.getCurrentDate("yyyy-MM-dd"), "yyyy-MM-dd", 1);
                break;
            case Constant.OPDTYPE_VOICE_VIDEO:
                bean.OPDType = new ArrayList<String>() {{
                    add("2");
                    add("3");
                }};
                break;
            case Constant.OPDTYPE_FAMILY:
                bean.OPDType = new ArrayList<String>() {{
                    add("1");
                    add("5");
                }};
                break;
            case Constant.OPDTYPE_ORDER_CHAT:
                bean.OPDType = new ArrayList<String>() {{
                    add("1");
                }};
                break;
            case Constant.OPDTYPE_ORDER_VOICE:
                bean.OPDType = new ArrayList<String>() {{
                    add("2");
                    add("3");
                }};
                break;
        }

        String str = JSON.toJSONString(bean);
        try {
            mJsonData = new String(str.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索的时候使用
     */
    public Http_getTaskList_Event(String searchKey, String CurrentPage, String PageSize, String type, HttpListener listener) {
        super(listener);
        mReqAction = "/Task/getTaskList";
        mReqMethod = HttpClient.POST;
        noParmName = true;
        mReqParams = new HashMap<>();
        QueryChatListBean bean = new QueryChatListBean();
        bean.CurrentPage = CurrentPage;
        bean.PageSize = PageSize;
        bean.Keyword = searchKey;
        bean.OrderType = 1;

        switch (type){
            case Constant.OPDTYPE_CHAT:
                bean.OPDType = new ArrayList<String>() {{add("1");}};
                break;
            case Constant.OPDTYPE_VOICE:
                bean.OPDType = new ArrayList<String>() {{add("2");add("3");}};
//                bean.RoomState = new ArrayList<String>() {{add("0");add("1");}}; //需要所有数据，所以注释掉
                bean.BeginDate = CommonUtils.getCurrentDate("yyyy-MM-dd");
                bean.EndDate = CommonUtils.getFutureDate(CommonUtils.getCurrentDate("yyyy-MM-dd"),"yyyy-MM-dd",1);
                break;
            case Constant.OPDTYPE_VOICE_VIDEO:
                bean.OPDType = new ArrayList<String>() {{add("2");}};
                bean.OPDType = new ArrayList<String>() {{add("3");}};
                break;
            case Constant.OPDTYPE_FAMILY:
                bean.OPDType = new ArrayList<String>() {{add("1");}};
                bean.OrderCostType = new ArrayList<String>() {{add("5");}};
                break;
        }
        String str = JSON.toJSONString(bean);
        try {
            mJsonData = new String(str.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 家庭医生 服务记录列表使用
     * @param CurrentPage
     * @param PageSize
     * @param type
     * @param MemberID
     * @param FamilyDoctorID
     * @param listener
     */
    public Http_getTaskList_Event(String CurrentPage, String PageSize, String type,String MemberID, String FamilyDoctorID,HttpListener listener) {
        super(listener);
        mReqAction = "/Task/getTaskList";
        mReqMethod = HttpClient.POST;
        noParmName = true;
        mReqParams = new HashMap<>();
        QueryChatListBean bean = new QueryChatListBean();
        bean.CurrentPage = CurrentPage;
        bean.PageSize = PageSize;
        bean.OrderType = 1;
        if(type.equals(Constant.OPDTYPE_RECORD)){
            bean.OPDType = new ArrayList<String>() {{add("1");add("2");add("3");}};
            bean.OrderCostType = new ArrayList<String>() {{add("5");}};
            bean.MemberID = MemberID;
        }else if(type.equals(Constant.OPDTYPE_FAMILY)){
            bean.OPDType = new ArrayList<String>() {{add("1");}};
            bean.OrderCostType = new ArrayList<String>() {{add("5");}};
        }

        String str = JSON.toJSONString(bean);
        try {
            mJsonData = new String(str.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
