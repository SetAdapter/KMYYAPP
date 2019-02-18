package com.kmwlyy.doctor.jpush;

import android.content.Intent;

import com.kmwlyy.doctor.Activity.DrugStoreRecipeActivity;
import com.kmwlyy.doctor.Activity.OrderListActivity;
import com.kmwlyy.login.MsgDetailActivity;
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
        switch (item.getNoticeFirstType()){
            case 0:
            case 1:
            case 4:
            case 5:
            case 6:
                Intent intent = new Intent(this,MsgDetailActivity.class);
                intent.putExtra("item",item);
                startActivity(intent);
                break;
            case 2:
                Intent intent1 = new Intent(this, OrderListActivity.class);
                intent1.putExtra("item",item);
                startActivity(intent1);
                break;
            case 3:
                EventBus.getDefault().post(new MessageEvent(MessageEvent.TYPE_DOCTOR_CONSULT));
                finish();
                break;
        }
    }
}
