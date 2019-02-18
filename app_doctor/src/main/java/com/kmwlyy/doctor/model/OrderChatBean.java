package com.kmwlyy.doctor.model;

/**
 * Created by Administrator on 2016/8/25.
 */
public class OrderChatBean {

    /**
     * UserConsultID : a574e81e06b3420ea7fa098ee5fd0b1f
     * MemberID : a7d1fa735e1345babf73fdba8f0f7fc2
     * ConsultContent : 先尝试只反馈文字试试看。
     * ConsultTime : 2016-12-20T16:40:13.107
     * ConsultType : 1
     * ConsultState : 5
     * Room : {"ServiceType":0,"ChannelID":9188,"Secret":"a19d123d71a9453e9bb25d906dfd4bdf","RoomState":3,"BeginTime":"0001-01-01T00:00:00","EndTime":"0001-01-01T00:00:00","TotalTime":0}
     * Order : {"OrderNo":"TW2016122016400001","TradeNo":"","LogisticNo":"-","PayType":-1,"OrderType":100,"OrderState":2,"RefundState":0,"LogisticState":4,"CostType":0,"OrderTime":"2016-12-20T16:40:13.1403076","TotalFee":0,"IsEvaluated":false}
     * Price : 0
     * MemberName : 赵先生
     */

    private String UserConsultID;
    private String MemberID;
    private String ConsultContent;
    private String ConsultTime;
    private int ConsultType;
    private int ConsultState;
    /**
     * ServiceType : 0
     * ChannelID : 9188
     * Secret : a19d123d71a9453e9bb25d906dfd4bdf
     * RoomState : 3
     * BeginTime : 0001-01-01T00:00:00
     * EndTime : 0001-01-01T00:00:00
     * TotalTime : 0
     */

    private RoomBean Room;
    /**
     * OrderNo : TW2016122016400001
     * TradeNo :
     * LogisticNo : -
     * PayType : -1
     * OrderType : 100
     * OrderState : 2
     * RefundState : 0
     * LogisticState : 4
     * CostType : 0
     * OrderTime : 2016-12-20T16:40:13.1403076
     * TotalFee : 0
     * IsEvaluated : false
     */

    private OrderBean Order;
    private int Price;
    private String MemberName;

    public String getUserConsultID() {
        return UserConsultID;
    }

    public void setUserConsultID(String UserConsultID) {
        this.UserConsultID = UserConsultID;
    }

    public String getMemberID() {
        return MemberID;
    }

    public void setMemberID(String MemberID) {
        this.MemberID = MemberID;
    }

    public String getConsultContent() {
        return ConsultContent;
    }

    public void setConsultContent(String ConsultContent) {
        this.ConsultContent = ConsultContent;
    }

    public String getConsultTime() {
        return ConsultTime;
    }

    public void setConsultTime(String ConsultTime) {
        this.ConsultTime = ConsultTime;
    }

    public int getConsultType() {
        return ConsultType;
    }

    public void setConsultType(int ConsultType) {
        this.ConsultType = ConsultType;
    }

    public int getConsultState() {
        return ConsultState;
    }

    public void setConsultState(int ConsultState) {
        this.ConsultState = ConsultState;
    }

    public RoomBean getRoom() {
        return Room;
    }

    public void setRoom(RoomBean Room) {
        this.Room = Room;
    }

    public OrderBean getOrder() {
        return Order;
    }

    public void setOrder(OrderBean Order) {
        this.Order = Order;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int Price) {
        this.Price = Price;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String MemberName) {
        this.MemberName = MemberName;
    }

    public static class RoomBean {
        private int ServiceType;
        private int ChannelID;
        private String Secret;
        private int RoomState;
        private String BeginTime;
        private String EndTime;
        private int TotalTime;

        public int getServiceType() {
            return ServiceType;
        }

        public void setServiceType(int ServiceType) {
            this.ServiceType = ServiceType;
        }

        public int getChannelID() {
            return ChannelID;
        }

        public void setChannelID(int ChannelID) {
            this.ChannelID = ChannelID;
        }

        public String getSecret() {
            return Secret;
        }

        public void setSecret(String Secret) {
            this.Secret = Secret;
        }

        public int getRoomState() {
            return RoomState;
        }

        public void setRoomState(int RoomState) {
            this.RoomState = RoomState;
        }

        public String getBeginTime() {
            return BeginTime;
        }

        public void setBeginTime(String BeginTime) {
            this.BeginTime = BeginTime;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public int getTotalTime() {
            return TotalTime;
        }

        public void setTotalTime(int TotalTime) {
            this.TotalTime = TotalTime;
        }
    }

    public static class OrderBean {
        private String OrderNo;
        private String TradeNo;
        private String LogisticNo;
        private int PayType;
        private int OrderType;
        private int OrderState;
        private int RefundState;
        private int LogisticState;
        private int CostType;
        private String OrderTime;
        private Double TotalFee;
        private boolean IsEvaluated;
        public float OriginalPrice;

        public String getOrderNo() {
            return OrderNo;
        }

        public void setOrderNo(String OrderNo) {
            this.OrderNo = OrderNo;
        }

        public String getTradeNo() {
            return TradeNo;
        }

        public void setTradeNo(String TradeNo) {
            this.TradeNo = TradeNo;
        }

        public String getLogisticNo() {
            return LogisticNo;
        }

        public void setLogisticNo(String LogisticNo) {
            this.LogisticNo = LogisticNo;
        }

        public int getPayType() {
            return PayType;
        }

        public void setPayType(int PayType) {
            this.PayType = PayType;
        }

        public int getOrderType() {
            return OrderType;
        }

        public void setOrderType(int OrderType) {
            this.OrderType = OrderType;
        }

        public int getOrderState() {
            return OrderState;
        }

        public void setOrderState(int OrderState) {
            this.OrderState = OrderState;
        }

        public int getRefundState() {
            return RefundState;
        }

        public void setRefundState(int RefundState) {
            this.RefundState = RefundState;
        }

        public int getLogisticState() {
            return LogisticState;
        }

        public void setLogisticState(int LogisticState) {
            this.LogisticState = LogisticState;
        }

        public int getCostType() {
            return CostType;
        }

        public void setCostType(int CostType) {
            this.CostType = CostType;
        }

        public String getOrderTime() {
            return OrderTime;
        }

        public void setOrderTime(String OrderTime) {
            this.OrderTime = OrderTime;
        }

        public Double getTotalFee() {
            return TotalFee;
        }

        public void setTotalFee(Double TotalFee) {
            this.TotalFee = TotalFee;
        }

        public boolean isIsEvaluated() {
            return IsEvaluated;
        }

        public void setIsEvaluated(boolean IsEvaluated) {
            this.IsEvaluated = IsEvaluated;
        }
    }
}
