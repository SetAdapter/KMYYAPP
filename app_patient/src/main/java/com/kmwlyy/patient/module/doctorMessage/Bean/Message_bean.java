package com.kmwlyy.patient.module.doctorMessage.Bean;

import java.io.Serializable;

/**
 * Created by Gab on 2017/7/29 0029.
 */
public class Message_bean implements Serializable{
    public String isRead;//0代表未读，1代表已读
    public String messageId;
    public String type;
    public String title;
    public String content;
    public String createDate;
    public String msgCount;
    public String contentTypeId;
    public String contentUrl;

    public String getmessageId() {
        return messageId;
    }

    public void setmessageId(String UserNoticeID) {
        this.messageId = UserNoticeID;
    }

    public String gettype() {
        return type;
    }

    public void settype(String MessageID) {
        this.type = MessageID;
    }

    public String gettitle() {
        return title;
    }

    public void settitle(String Title) {
        this.title = Title;
    }

    public String getcontent() {
        return content;
    }

    public void setcontent(String content) {
        this.content = content;
    }

    public String getcreateDate() {
        return createDate;
    }

    public void setcreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String isRead() {
        return isRead;
    }

    public void isRead(String IsRead) {
        this.isRead = IsRead;
    }

    public String getmsgCount() {
        return msgCount;
    }

    public void setmsgCount(String msgCount) {
        this.msgCount = msgCount;
    }

    public String getcontentTypeId() {
        return contentTypeId;
    }

    public void setcontentTypeId(String contentTypeId) {
        this.contentTypeId = contentTypeId;
    }
    public String getcontentUrl() {
        return contentUrl;
    }

    public void setcontentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    @Override
    public String toString() {
        return "MyCard{" +
                "isRead='" + isRead + '\'' +
                ", messageId='" + messageId + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", content='" + createDate + '\'' +
                ", msgCount='" + msgCount + '\'' +
                ", contentUrl='" + contentUrl + '\'' +
                ", contentTypeId='" + contentTypeId + '\'' +
                '}';
    }
}
