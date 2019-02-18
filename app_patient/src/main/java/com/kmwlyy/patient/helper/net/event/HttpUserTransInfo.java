package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.UserTransInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Winson on 2016/8/13.
 */
public interface HttpUserTransInfo {

    int TRANSTYPE_IN = 1;
    int TRANSTYPE_OUT = 2;

    class Add extends HttpEvent {

        public Add(int transType, float amount, String morderNo, String orderId, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;
            mReqAction = "/userTrans/addUserTrans";

            mReqParams = new HashMap();

            //交易类型(1:收入,2:支出)
            mReqParams.put("TransType", "" + transType);

            //金额
            mReqParams.put("Amount", "" + amount);

            //订单编号(此字段为非必字段)
            mReqParams.put("OrderNo", morderNo);

            //订单ID(此字段为非必字段)
            mReqParams.put("OrderID", orderId);
        }
    }

    class GetList extends HttpEvent<ArrayList<UserTransInfo.ListItem>> {

        public GetList(int transType, int pageIndex, int pageSize, HttpListener<ArrayList<UserTransInfo.ListItem>> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/userTrans/getUserTransPagelist";
            mReqParams = new HashMap();
            mReqParams.put("transType", "" + transType);
            mReqParams.put("pageSize", "" + pageSize);
            mReqParams.put("pageIndex", "" + pageIndex);


        }

    }

    class GetDetail extends HttpEvent<UserTransInfo.Detail> {

        public GetDetail(String id, HttpListener<UserTransInfo.Detail> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/userTrans/getUserTransInfo";
            mReqParams = new HashMap();
            mReqParams.put("ID", id);

        }

    }

}
