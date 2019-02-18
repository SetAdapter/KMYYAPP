package com.kmwlyy.patient.helper.net.event;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.Consignee;
import com.kmwlyy.patient.helper.net.bean.Order;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Winson on 2016/8/13.
 */
public interface HttpOrders {

    class ParamsDetail {

        @SerializedName("Subject")
        public String mSubject;

        @SerializedName("Body")
        public String mBody;

        @SerializedName("UnitPrice")
        public float mUnitPrice;//单价（元）

        @SerializedName("Quantity")
        public int mQuantity;//数量

        @SerializedName("ProductId")
        public String mProductId;//产品编号

        @SerializedName("ProductType")
        public String mProductType;//产品类型（ 挂号=0,图文咨询=1,电话咨询=2,视频咨询=3,处方支付=4，5：其他）
    }

    class Create extends HttpEvent {

        public Create(int orderType, String orderOutId, ArrayList<ParamsDetail> details, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/Orders/Create";
            mReqParams = new HashMap();
            mReqParams.put("OrderType", "" + orderType);
            mReqParams.put("OrderOutID", orderOutId);
            if (details != null) {
                mReqParams.put("Details", new Gson().toJson(details));
            }
        }
    }

    class Confirm extends HttpEvent<Order> {

        public Confirm(String orderNo, int combo, Consignee consignee, HttpListener<Order> mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/Orders/Confirm";
          /*  mReqParams = new HashMap();
            mReqParams.put("OrderNo", "" + orderNo);
            mReqParams.put("Privilege", "" + combo);
            if (consignee != null){
                mReqParams.put("Consignee",new Gson().toJson(consignee));
            }*/

            noParmName = true;
            HashMap<String, Object> params = new HashMap<>();
            params.put("OrderNo", orderNo);
            params.put("Privilege", "" + combo);
            if (consignee != null&& !TextUtils.isEmpty(consignee.mId) ) {
                params.put("Consignee", consignee);
            }
            mJsonData = new Gson().toJson(params);

        }
    }

    class GetDetail extends HttpEvent<Order> {

        public GetDetail(String orderNo,HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/Orders";
            mReqParams = new HashMap();
            mReqParams.put("OrderNo", "" + orderNo);
        }
    }

    class Cancel extends HttpEvent {

        public Cancel(String orderNo, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/Orders/Cancel";
            mReqParams = new HashMap();
            mReqParams.put("OrderNo", "" + orderNo);
        }
    }

    class Refund extends HttpEvent {

        public Refund(String orderNo, String refundNo, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/Orders/Refund";
            mReqParams = new HashMap();
            mReqParams.put("OrderNo", "" + orderNo);
            mReqParams.put("RefundNo", refundNo);

        }

    }

    class Complete extends HttpEvent {

        public Complete(String orderNo, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/Orders/Complete";
            mReqParams = new HashMap();
            mReqParams.put("OrderNo", "" + orderNo);
        }
    }

}
