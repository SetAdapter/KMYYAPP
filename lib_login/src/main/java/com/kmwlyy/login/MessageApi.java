package com.kmwlyy.login;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016-10-27.
 */

public interface MessageApi {

    class getMessageList extends HttpEvent<List<Message>> {

        public getMessageList(String CurrentPage, String PageSize, HttpListener<List<Message>> mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;
            mReqAction = "/UserNotice/GetMyMsg";
            mReqParams = new HashMap<>();
            mReqParams.put("CurrentPage", CurrentPage);
            mReqParams.put("PageSize", PageSize);
        }

        public getMessageList(String ReadStatus, String BeginTime, String EndTime, HttpListener<List<Message>> mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;
            mReqAction = "/UserNotice/GetMyMsg";
            mReqParams = new HashMap<>();
            mReqParams.put("CurrentPage","1");
            mReqParams.put("PageSize", "50");
            mReqParams.put("BeginTime", BeginTime);
            mReqParams.put("EndTime", EndTime);
        }
    }

    class getMsgUnreadCount extends HttpEvent<String> {

        public getMsgUnreadCount(HttpListener<String> mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;
            mReqAction = "/UserNotice/GetMyUnreadCount";
        }
    }

    class setMsgToReaded extends HttpEvent {

        public setMsgToReaded(String userNoticeID, HttpListener mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;
            mReqAction = "/UserNotice/UpdateMsgToReaded";
            mReqParams = new HashMap<>();
            mReqParams.put("messageID", userNoticeID);
        }
    }

    /* 设置所有消息为已读 */
    class setAllMsgReaded extends HttpEvent {

        public setAllMsgReaded(HttpListener mListener) {
            super(mListener);
            mReqMethod = HttpClient.POST;
            mReqAction = "/UserNotice/UpdateAllToReaded";
        }
    }

    class Message implements Serializable{
        /**
         * UserNoticeID :
         * MessageID : a322b2d55324413c8b8dd978303992f1
         * Title : 哈哈哈6
         * Summary : 哈哈哈6~~
         * Content : 哈哈哈6~~~~
         * UserNoticeType : 2
         * NoticeFirstType : 1
         * NoticeSecondType : 1
         * NoticeDate : 2016-10-27T10:14:06.147
         * IsRead : false
         * ReadTerminals :
         */

        private String UserNoticeID;
        private String MessageID;
        private String Title;
        private String Summary;
        private String Content;
        private int UserNoticeType;
        private int NoticeFirstType;
        private int NoticeSecondType;
        private String NoticeDate;
        private boolean IsRead;
        private String ReadTerminals;

        public String getUserNoticeID() {
            return UserNoticeID;
        }

        public void setUserNoticeID(String UserNoticeID) {
            this.UserNoticeID = UserNoticeID;
        }

        public String getMessageID() {
            return MessageID;
        }

        public void setMessageID(String MessageID) {
            this.MessageID = MessageID;
        }

        public String getTitle() {
            return Title;
        }

        public void setTitle(String Title) {
            this.Title = Title;
        }

        public String getSummary() {
            return Summary;
        }

        public void setSummary(String Summary) {
            this.Summary = Summary;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public int getUserNoticeType() {
            return UserNoticeType;
        }

        public void setUserNoticeType(int UserNoticeType) {
            this.UserNoticeType = UserNoticeType;
        }

        public int getNoticeFirstType() {
            return NoticeFirstType;
        }

        public void setNoticeFirstType(int NoticeFirstType) {
            this.NoticeFirstType = NoticeFirstType;
        }

        public int getNoticeSecondType() {
            return NoticeSecondType;
        }

        public void setNoticeSecondType(int NoticeSecondType) {
            this.NoticeSecondType = NoticeSecondType;
        }

        public String getNoticeDate() {
            return NoticeDate;
        }

        public void setNoticeDate(String NoticeDate) {
            this.NoticeDate = NoticeDate;
        }

        public boolean isIsRead() {
            return IsRead;
        }

        public void setIsRead(boolean IsRead) {
            this.IsRead = IsRead;
        }

        public String getReadTerminals() {
            return ReadTerminals;
        }

        public void setReadTerminals(String ReadTerminals) {
            this.ReadTerminals = ReadTerminals;
        }
    }
}
