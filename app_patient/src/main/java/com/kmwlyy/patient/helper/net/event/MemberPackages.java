package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.CheckUserPackageBean;
import com.kmwlyy.patient.helper.net.bean.DoctorClinic;
import com.kmwlyy.patient.helper.net.bean.MemberOrder;
import com.kmwlyy.patient.helper.net.bean.UserPackage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/12/9.
 * 获取会员套餐
 */

public interface MemberPackages {
    class GetList extends HttpEvent<ArrayList<UserPackage>> {

        public GetList(HttpListener<ArrayList<UserPackage>> mListener) {
            super(mListener);
            mReqMethod = HttpClient.GET;
            mReqAction = "/UserPackageConsumes/GetUserPackages";
        }

    }

    class CreateOrder extends HttpEvent<MemberOrder>{
        public CreateOrder(String userPackageId,HttpListener<MemberOrder> mListener){
            super(mListener);
            mReqMethod = HttpClient.POST;
            mReqAction = "/UserPackageConsumes/CreateOrder?userPackageId="+userPackageId;
            mReqParams = new HashMap();
            mReqParams.put("userPackageId", userPackageId);
        }
    }


    class GetCurrentPackage extends HttpEvent<UserPackage>{
        public GetCurrentPackage(HttpListener<UserPackage> mListener){
            super(mListener);
            mReqMethod = HttpClient.GET;
            mReqAction = "/UserPackageConsumes/CurrentPackage";
        }
    }

    class CancelOrder extends HttpEvent{
        public CancelOrder(String orderNO,HttpListener mListener){
            super(mListener);
            mReqMethod = HttpClient.POST;
            mReqAction = "/Orders/Cancel?OrderNo="+orderNO;
            mReqParams = new HashMap();
            //mReqParams.put("OrderNo", orderNO);
        }
    }

    class  CheckUserPackage extends HttpEvent<CheckUserPackageBean>{
        public CheckUserPackage(String DoctorID,int OPDType, HttpListener<CheckUserPackageBean> mListener){
            super(mListener);
            mReqMethod = HttpClient.POST;
            mReqAction = "/UserPackageConsumes/CheckUserPackage";
            mReqParams = new HashMap();
            mReqParams.put("DoctorID", DoctorID);
            mReqParams.put("OPDType", OPDType+"");
        }
    }
}
