package com.kmwlyy.imchat.model;

public interface Chat_EventApi {
    class updateTime{
        private String timeStr;//时间

        public String getTimeStr() {
            return timeStr;
        }

        public void setTimeStr(String timeStr) {
            this.timeStr = timeStr;
        }
    }

}
