package com.kmwlyy.doctor.model;

/**
 * Created by Administrator on 2016/8/17.
 */
public class Content {
    public MsgCon MsgContent;
    public String MsgType;

    public class MsgCon {
        public String Text;
        public String ImageFormat;//图片
        public String FileName;//文件
        public String Data;//表情
        public String Second;//语音

        public String getText() {
            return Text;
        }

        public void setText(String text) {
            Text = text;
        }
    }
}


