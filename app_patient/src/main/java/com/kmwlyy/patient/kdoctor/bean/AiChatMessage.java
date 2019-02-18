package com.kmwlyy.patient.kdoctor.bean;

/**
 * @Description描述:
 * @Author作者: hx
 * @Date日期: 2016/11/24
 */
public class AiChatMessage {
    public final static int SEND = 1;
    public final static int RECEIVED = 2;
    public final static int SETTLED = 3;
    public final static int MUSIC = 3;
    public final static int RECEIVE_TITLE = 2;
    public final static int RECEIVE_ITEM = 3;
    public final static int SENDING = 1;
    public final static int SUCCEED = 2;
    public final static int FAILED = 3;

    private int id;

    private int type;//1是发送的，2是接收的 3是固定的形式 4是播放音乐

    private String content;

    private int state;//1是发送中，2是发送成功，3是发送失败，4是固定的

    private SearchBean searchBean;//疾病bean类

    private String diseaseId;//疾病id

    public Boolean startPlayMusic = false;//是否开始播放音乐

    private int webViewHeight;

    private int webViewWidth;

    private int musicId;

    private String time;

    private String musicTime;

    public boolean hasTime = true;//是否需要显示时间

    public AiChatMessage() {
    }

    public AiChatMessage(int type, String content) {
        this.type = type;
        this.content = content;

    }

    public AiChatMessage(int type, int musicId,Boolean startPlayMusic) {
        this.type = type;
        this.musicId = musicId;
        this.startPlayMusic = startPlayMusic;
    }


    public AiChatMessage(int type, String content, int webViewHeight, int webViewWidth) {
        this.type = type;
        this.content = content;
        this.webViewHeight = webViewHeight;
        this.webViewWidth = webViewWidth;
    }

    public AiChatMessage(String diseaseId, int type, String content) {
        this.type = type;
        this.content = content;
        this.diseaseId = diseaseId;
    }

    public AiChatMessage(int type, SearchBean searchBean) {
        this.type = type;
        this.searchBean = searchBean;
    }

    public AiChatMessage(int type, String content, int state) {
        this.type = type;
        this.content = content;
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMusicTime() {
        return musicTime;
    }

    public void setMusicTime(String musicTime) {
        this.musicTime = musicTime;
    }

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(String diseaseId) {
        this.diseaseId = diseaseId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SearchBean getSearchBean() {
        return searchBean;
    }

    public void setSearchBean(SearchBean searchBean) {
        this.searchBean = searchBean;
    }

    public int getWebViewHeight() {
        return webViewHeight;
    }

    public void setWebViewHeight(int webViewHeight) {
        this.webViewHeight = webViewHeight;
    }

    public int getWebViewWidth() {
        return webViewWidth;
    }

    public void setWebViewWidth(int webViewWidth) {
        this.webViewWidth = webViewWidth;
    }
}
