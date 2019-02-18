package com.kmwlyy.patient.helper.net.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Winson on 2016/8/23.
 */
public interface Pay {

    class Wechat {
        public String appid;//": "wx7833444576404bcb",
        public String code_url;//": "weixin://wxpay/bizpayurl?pr=V6aPkgg",
        public String partnerId;//": "1263997401",
        public String nonce_str;//": "ECb1tzPtvX1FcKU8",
        public String prepay_id;//": "wx2016082211541365c425a5bc0232230911",
        public String result_code;//": "SUCCESS",
        public String return_code;//": "SUCCESS",
        public String return_msg;//": "OK",
        public String sign;//": "923EDEF1288CD891E95D288DCCB3B5B4",
        public String trade_type;//": "NATIVE"
        public String timeStamp;//": "NATIVE"

        @SerializedName("package")
        public String packageV;//": "NATIVE"
    }

    class KM {

        public String partner;//"partner\":\"160302015080162\"
        public String inputCharset;//UTF-8
        public String clientIp;
        public String timestamp;
        public String sellerEmail;
        public String paymentType;
        public String notifyUrl;
        public String returnUrl;
        public String outTradeNo;
        public String subject;
        public String price;
        public String totalAmount;
        public String quantity;
        public String sign;
        public String signtype;
        public String body;

    }

}
