package com.kmwlyy.patient.module.myservice.Bean;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;

import java.util.HashMap;
import java.util.List;

/**
 * Created by xcj on 2017/8/11.
 */

public interface Http_Services_Package {
    class GetServicePackageList extends HttpEvent<List<Doorservoce.DataBean>> {

        public GetServicePackageList(String CurrentPage, String PageSize, String PackageType,String UserRange, HttpListener<List<Doorservoce.DataBean>> mListener) {
            super(mListener);
            mReqAction = "/DoctorPackage/GetList";
            mReqMethod = HttpClient.GET;
            mReqParams = new HashMap<>();
            mReqParams.put("CurrentPage", CurrentPage);
            mReqParams.put("PageSize", PageSize);
            mReqParams.put("PackageType", PackageType);
            mReqParams.put("UserRange", UserRange);

        }

    }

    class GetServicePackageInfo extends HttpEvent<ServicePackageInfoBean.DataBean> {
        public GetServicePackageInfo(String packageID, HttpListener<ServicePackageInfoBean.DataBean> mListener){
            super(mListener);
            mReqMethod = HttpClient.GET;
            mReqAction = "/DoctorPackage/GetDetail";
            mReqParams = new HashMap();
            mReqParams.put("packageID",packageID);
        }
    }

    class GetCouponList extends HttpEvent<CouponInfoBean> {
        public GetCouponList(HttpListener<CouponInfoBean> mListener){
            super(mListener);
            mReqMethod = HttpClient.POST;
            mReqAction = "/webapibuyservice/getCouponList";
        }
    }

    class SubmitBuyService extends HttpEvent {
        public SubmitBuyService(String PackageID, HttpListener mListener){
            super(mListener);
            mReqMethod = HttpClient.POST;
            mReqAction = "/DoctorPackage/Submit";
            mReqParams = new HashMap();
            mReqParams.put("PackageID",PackageID);

        }
    }
}
