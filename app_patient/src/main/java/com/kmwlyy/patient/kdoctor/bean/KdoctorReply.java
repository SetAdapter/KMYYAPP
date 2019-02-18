package com.kmwlyy.patient.kdoctor.bean;

/**
 * @Description描述:
 * @Author作者: hx
 * @Date日期: 2017/8/21
 */

public class KdoctorReply {
    private String resultCode;
    private String msg;
    private ResultDataBean resultData;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultDataBean getResultData() {
        return resultData;
    }

    public void setResultData(ResultDataBean resultData) {
        this.resultData = resultData;
    }

    public static class ResultDataBean {

        private String body;
        private String respId;
        private String answerType;
        private String type;

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getRespId() {
            return respId;
        }

        public void setRespId(String respId) {
            this.respId = respId;
        }

        public String getAnswerType() {
            return answerType;
        }

        public void setAnswerType(String answerType) {
            this.answerType = answerType;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
