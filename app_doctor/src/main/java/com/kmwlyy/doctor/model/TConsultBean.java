package com.kmwlyy.doctor.model;

import java.util.List;

/**
 * Created by TFeng on 2017/7/3.
 */

public class TConsultBean {
    private int type;
    private List<TMssageBean> mTMessageBeanList;
    private String time;

    public TConsultBean(int type, List<TMssageBean> TMessageBeanList,String time) {
        this.type = type;
        mTMessageBeanList = TMessageBeanList;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<TMssageBean> getTMessageBeanList() {
        return mTMessageBeanList;
    }

    public void setTMessageBeanList(List<TMssageBean> TMessageBeanList) {
        mTMessageBeanList = TMessageBeanList;
    }

    public static class TMssageBean{

        private int itemType;
        String mainSult;
        String initDiag;
        String doctorSuggest;
        String content;

        public TMssageBean(int itemType,String content) {
            this.itemType = itemType;
            this.content = content;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getItemType() {
            return itemType;
        }

        public void setItemType(int itemType) {
            this.itemType = itemType;
        }

        public String getMainSult() {
            return mainSult;
        }

        public void setMainSult(String mainSult) {
            this.mainSult = mainSult;
        }

        public String getInitDiag() {
            return initDiag;
        }

        public void setInitDiag(String initDiag) {
            this.initDiag = initDiag;
        }

        public String getDoctorSuggest() {
            return doctorSuggest;
        }

        public void setDoctorSuggest(String doctorSuggest) {
            this.doctorSuggest = doctorSuggest;
        }
    }
}
