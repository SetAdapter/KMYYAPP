package com.kmwlyy.patient.helper;

/**
 * @Description描述:
 * @Author作者: zhaoqile
 * @Date日期: 2016/12/21
 */
public class ConsultConstants {

    /* 咨询类型 */
    public static final int PAY = 0;//付费
    public static final int FREE = 1;//免费咨询
    public static final int DUTY = 2;//义诊咨询
    public static final int MEAL = 3;//套餐
    public static final int MEMBER = 4;//会员
    public static final int FAMILY = 5;//家庭医生


    /* 状态 */
    //0-未筛选、1-未领取、2-未回复、4-已回复、5-已完成
    public static final int STATE_FINISH = 5;//已完成
    public static final int STATE_ALREADY_REPLY = 4;//已回复
    public static final int STATE_WAIT_REPLY = 2;//未回复
    public static final int STATE_WAIT_ASSIGNED  = 1;//未领取
    public static final int STATE_WAIT_FILTER = 0;//未筛选

    /* 订单状态 */
    public class OrderState {
        //state：0-待支付、1-已支付、2-已完成、3-已取消
        public static final int ORDER_WAIT_PAY = 0;//待支付
        public static final int ORDER_PAYED = 1;//已支付
        public static final int ORDER_FINISH = 2;//已完成
        public static final int ORDER_CANCELED = 3;//已取消
        public static final int ORDER_APPLY_REFUNDED = 4;//申请退款
        public static final int ORDER_REFUSE_REFUNDED = 5;//拒绝退款
        public static final int ORDER_REFUNDED = 6;//已退款
    }

    /* 会诊的状态 */
    public class MeetingState {
        //会诊状态(-1未提交、0-待支付、1-待开始、2-进行中、3-已完成
        public static final int MEETING_WAIT_SUMBIT = -1;//未提交
        public static final int MEETING_WAIT_PAY = 0;//待支付
        public static final int MEETING_WAIT_BEGIN = 1;//待开始
        public static final int MEETING = 2;//进行中
        public static final int MEETING_FINISH = 3;//已完成
    }

    /* 语音视频咨询类型 */
    public class VoiceVideo {
        public static final int TYPE_VOICE = 2;//语音资讯
        public static final int TYPE_VIDEO = 3;//视频资讯
    }



}
