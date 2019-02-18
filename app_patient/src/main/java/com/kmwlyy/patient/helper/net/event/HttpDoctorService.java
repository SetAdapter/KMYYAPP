package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.patient.helper.net.bean.DoctorService;
import com.kmwlyy.patient.helper.net.bean.EvaluateListBean;
import com.kmwlyy.patient.helper.net.bean.EvaluateTagBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Winson on 2016/8/13.
 */
public interface HttpDoctorService {

    class GetList extends HttpEvent<DoctorService.ListItem> {

        public GetList(HttpListener<DoctorService.ListItem> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/doctorPrice/getDoctorPriceServiceList";

        }
    }

    class GetDetail extends HttpEvent<DoctorService.Detail> {

        public GetDetail(String serviceId, HttpListener<DoctorService.Detail> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/doctorPrice/getDoctorPriceService";

            mReqParams = new HashMap<>();
            mReqParams.put("ID", serviceId);

        }
    }

    class Save extends HttpEvent<DoctorService.ListItem> {

        public Save(DoctorService.Detail detail, HttpListener<DoctorService.ListItem> mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/doctorPrice/addOrEditeDoctorService";

            if (detail != null) {
                mReqParams = new HashMap<>();
                mReqParams.put("ServiceID", detail.mServiceID);
                mReqParams.put("ServiceType", "" + detail.mServiceType);
                mReqParams.put("ServiceTypeName", detail.mServiceTypeName);
                mReqParams.put("ServiceSwitch", "" + detail.mServiceSwitch);
                mReqParams.put("ServicePrice", "" + detail.mServicePrice);
            }

        }
    }

    /**
     * 获取评论数据
     */
    class GetEvaluate extends HttpEvent<ArrayList<EvaluateListBean>> {
        public String PageSize = "20";

        public GetEvaluate(String id, String CurrentPage,HttpListener<ArrayList<EvaluateListBean>> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/ServiceEvaluations/Query";
            mReqParams = new HashMap<>();
            mReqParams.put("ProviderID", id);
            mReqParams.put("CurrentPage", CurrentPage);
            mReqParams.put("PageSize", PageSize);

        }
    }

    /**
     * 获取评论数据(通过tag 过滤)
     */
    class GetTagEvaluate extends HttpEvent<ArrayList<EvaluateListBean>> {
        public String PageSize = "20";

        public GetTagEvaluate(String id, String CurrentPage,String TagName,HttpListener<ArrayList<EvaluateListBean>> mListener) {
            super(mListener);

            mReqAction = "/ServiceEvaluations/Query";
            mReqMethod = HttpClient.GET;

            mReqParams = new HashMap<>();
            mReqParams.put("ProviderID", id);
            mReqParams.put("CurrentPage", CurrentPage);
            mReqParams.put("PageSize", PageSize);
            mReqParams.put("EvaluationTag", TagName);

        }
    }

    /**
     * 获取tag列表数据
     */
    class GetTags extends HttpEvent<ArrayList<EvaluateTagBean>> {

        public GetTags(HttpListener<ArrayList<EvaluateTagBean>> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/ServiceEvaluations/GetAllTags";
            mReqParams = new HashMap<>();

        }
    }

    /**
     * 提交评论数据
     */
    class SubmitEvaluate extends HttpEvent<String> {

        public SubmitEvaluate(String OuterID,String Score,String EvaluationTags,String Content,HttpListener<String> mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/ServiceEvaluations/Add";
            mReqParams = new HashMap<>();
            mReqParams.put("OuterID", OuterID);
            mReqParams.put("Score", Score);
            mReqParams.put("EvaluationTags", EvaluationTags);
            mReqParams.put("Content", Content);

        }
    }

}
