package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.AccountDetail;
import com.kmwlyy.patient.helper.net.bean.BankBean;
import com.kmwlyy.patient.helper.net.bean.RechargeDetail;
import com.kmwlyy.patient.helper.net.bean.UsedBankBean;
import com.kmwlyy.patient.helper.net.bean.UserTransInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xcj on 2016/12/17.
 */

public interface AccountEvent {
    class GetAccountDetail extends HttpEvent<AccountDetail> {
        public GetAccountDetail(HttpListener<AccountDetail> mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;
            mReqAction = "/userAccount/getUserAccount";
        }
    }

    class Recharge extends HttpEvent<RechargeDetail> {
        public Recharge(String mRechargeAmount, String mRechargeType, HttpListener<RechargeDetail> mListener) {
            super(mListener);
            mReqMethod = HttpClient.POST;
            mReqAction = "/userAccount/UserRecharge";
            mReqParams = new HashMap();
            mReqParams.put("RechargeAmount", mRechargeAmount);
            mReqParams.put("RechargeType", mRechargeType);
        }
    }

    class Withdraw extends HttpEvent {
        public Withdraw(String Amout, String Bank, String BankBarnch, String AccountName, String CardCode, String Remark, HttpListener mListener) {
            super(mListener);
            mReqMethod = HttpClient.POST;
            mReqAction = "/userCashe/addUserCash";
            mReqParams = new HashMap();
            mReqParams.put("Amount", Amout);
            mReqParams.put("Bank", Bank);
            mReqParams.put("BankBarnch", BankBarnch);
            mReqParams.put("AccountName", AccountName);
            mReqParams.put("CardCode", CardCode);
            mReqParams.put("PayPassword", Remark);
        }
    }

    class GetUserTransPagelist extends HttpEvent<ArrayList<UserTransInfo.ListItem>> {

        public GetUserTransPagelist(int transType, int pageIndex, int pageSize, HttpListener<ArrayList<UserTransInfo.ListItem>> mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/userTrans/getUserTransPagelist";
            mReqParams = new HashMap();
            mReqParams.put("transType", "" + transType);
            mReqParams.put("pageSize", "" + pageSize);
            mReqParams.put("CurrentPage", "" + pageIndex);


        }

    }

    class SetPayPassword extends HttpEvent<String>{
        public SetPayPassword(String code, String input, String confirm, HttpListener<String> mListener) {
            super(mListener);
            mUserOriginalData = true;
            mReqMethod = HttpClient.POST;
            mReqAction = "/userAccount/SetPayPassword";
            mReqParams = new HashMap();
            mReqParams.put("CheckCode", code);
            mReqParams.put("PayPassword",input);
            mReqParams.put("PayPasswordConfirm", confirm);
        }
    }

    class GetBankList extends HttpEvent<ArrayList<BankBean>>{
        public GetBankList(HttpListener<ArrayList<BankBean>> mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;
            mReqAction = "/bank/getBankList";
        }
    }

    class GetBankCardList extends HttpEvent<ArrayList<UsedBankBean>>{
        public GetBankCardList(HttpListener<ArrayList<UsedBankBean>> mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;
            mReqAction = "/userBankCard/getBankCardList";
        }
    }

    class BlancePay extends HttpEvent{
        public BlancePay(String order, String password, HttpListener mListener) {
            super(mListener);
            mReqMethod = HttpClient.POST;
            mReqAction = "/Cashier/BlancePay";
            mReqParams = new HashMap();
            mReqParams.put("OrderNo", order);
            mReqParams.put("PayPassword",password);
        }
    }


    class BindMobile extends HttpEvent{
        public BindMobile(String mobile, String code, HttpListener mListener) {
            super(mListener);
            mReqMethod = HttpClient.POST;
            mReqAction = "/userAccount/BindMobile";
            mReqParams = new HashMap();
            mReqParams.put("Mobile", mobile);
            mReqParams.put("CheckCode",code);
        }
    }
}
