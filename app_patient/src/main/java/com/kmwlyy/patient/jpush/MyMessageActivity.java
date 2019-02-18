package com.kmwlyy.patient.jpush;

import com.kmwlyy.login.MsgListActivity;
import com.kmwlyy.login.MessageApi;
import com.kmwlyy.login.MessageEvent;

import org.greenrobot.eventbus.EventBus;

public class MyMessageActivity extends MsgListActivity {
    /*********************医生*************************
     * 1	通知/公告
     * 2	新图文咨询订单（所有类型的图文咨询）
     * 3	新音视频问诊订单（所有类型的音视频问诊）
     * 4	新家庭医生订单
     *
     * 5	新远程会诊订单
     * 6	音视频问诊患者进入诊室
     * 7	有新药店处方
     * 8	有会诊正在进行
     * *******************患者*************************
     * 9	通知/公告
     * 10	图文咨询医生回复（所有类型的图文咨询）
     * 11	你的看诊处方已生成
     * 12	音视频看诊医生呼叫
     */
    @Override
    public void goToDetail(MessageApi.Message item) {
        switch (item.getNoticeSecondType()){
            case 10:
            case 11:
            case 12:
                EventBus.getDefault().post(new MessageEvent(MessageEvent.TYPE_USER_CONSULT));
                finish();
                break;
        }
    }

}
