package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.Pay;

import java.util.HashMap;

/**
 * Created by Winson on 2016/8/22.
 */
public interface HttpCashier {

    class AliPay extends HttpEvent {

        public AliPay(String orderNo, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;
            mReqAction = "/Cashier/AliPay";
            mReqParams = new HashMap();
            mReqParams.put("OrderNo", orderNo);

        }

    }

    class WxPay extends HttpEvent<Pay.Wechat> {

        public WxPay(String orderNo, String sellerID, HttpListener<Pay.Wechat> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;
            mReqAction = "/Cashier/WxPay";
            mReqParams = new HashMap();
            mReqParams.put("OrderNo", orderNo);
            mReqParams.put("SellerID", sellerID);

        }

    }

    class KMPay extends HttpEvent<Pay.KM> {

        public KMPay(String orderNo,String returnUrl, HttpListener<Pay.KM> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;
            mReqAction = "/Cashier/KMPay";
            mReqParams = new HashMap();
            mReqParams.put("OrderNo", orderNo);
            mReqParams.put("returnUrl", returnUrl);

        }

    }


}
