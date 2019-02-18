package com.kmwlyy.patient.helper.net.event;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.ConsultRecipeBean;
import com.kmwlyy.patient.helper.net.bean.ConsultRecipeListBean;
import com.kmwlyy.patient.helper.net.bean.UserOPDRegisters;
import com.kmwlyy.patient.helper.net.bean.VVConsultDetailBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Winson on 2016/8/13.
 */
public interface HttpUserOPDRegisters {

    int OPDTYPE_REG = 0;
    int OPDTYPE_IMAGE = 1;
    int OPDTYPE_IM = 2;
    int OPDTYPE_VIDEO = 3;

    class Add extends HttpEvent<UserOPDRegisters.AddResp> {

        /**
         * @param memberID
         * @param doctorId
         * @param scheduleID 医生排版编号
         * @param oPDDate    预约日期(格式'yyyy-MM-dd')
         * @param oPDType    预约类型 默认值: 1,2,3,4 , 0-挂号、1-图文、2-语音、3-视频
         * @param randomCode 验证码
         * @param mListener
         */
        public Add(String memberID, String doctorId, String scheduleID, String oPDDate, int oPDType, String randomCode, String ConsultContent, List<HttpUserConsults.ImageFile> images,  HttpListener<UserOPDRegisters.AddResp> mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/UserOPDRegisters";
            noParmName = true;

            HashMap<String, Object> params = new HashMap<>();
            params.put("MemberID", memberID);
            params.put("DoctorID", doctorId);
            params.put("ScheduleID", scheduleID);
            params.put("OPDDate", oPDDate);
            params.put("OPDType", "" + oPDType);
            params.put("RandomCode", randomCode);
            params.put("ConsultContent", ConsultContent);
            params.put("Files", images);
            mJsonData = new Gson().toJson(params);
        }

    }

    class GetWaitingCount extends HttpEvent {

        public GetWaitingCount(String doctorId, String oPDRegisterID, HttpListener mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;
            mReqAction = "/UserOPDRegisters/GetWaitingCount";

            mReqParams = new HashMap();
            mReqParams.put("DoctorID", doctorId);
            mReqParams.put("OPDRegisterID", oPDRegisterID);

        }

    }

    class GetList extends HttpEvent<ArrayList<UserOPDRegisters>> {

        public GetList(String keyword, String beginDate, String endDate, int currentPage, int pageSize, HttpListener<ArrayList<UserOPDRegisters>> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/UserOPDRegisters";

            mReqParams = new HashMap<>();
            mReqParams.put("Keyword", keyword);
            mReqParams.put("BeginDate", beginDate);
            mReqParams.put("EndDate", endDate);
            mReqParams.put("PageSize", "" + pageSize);
            mReqParams.put("CurrentPage", "" + currentPage);
        }
    }

    class GetDetail extends HttpEvent<VVConsultDetailBean> {

        public GetDetail(String oPDRegisterID, HttpListener<VVConsultDetailBean> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/UserOPDRegisters";

            mReqParams = new HashMap<>();
            mReqParams.put("OPDRegisterID", oPDRegisterID);
        }
    }

    class GetRecipes extends HttpEvent<ConsultRecipeListBean> {

        public GetRecipes(String oPDRegisterID, HttpListener<ConsultRecipeListBean> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/UserOPDRegisters/GetRecipeFiles";

            mReqParams = new HashMap<>();
            mReqParams.put("OPDRegisterID", oPDRegisterID);
        }
    }

    /* 取消订单 */
    class CancelOrder extends HttpEvent<String> {

        public CancelOrder(String orderNo, HttpListener<String> mListener) {
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/Orders/Cancel?OrderNo="+orderNo;

//            mReqParams = new HashMap<>();
//            mReqParams.put("OrderNo", orderNo);
        }
    }

    /* 删除订单 */
    class DeleteVVOrder extends HttpEvent<String> {

        public DeleteVVOrder(String id, HttpListener<String> mListener) {
            super(mListener);

            mReqMethod = HttpClient.DELETE;

            mReqAction = "/UserOPDRegisters?ID="+id;

//            mReqParams = new HashMap<>();
//            mReqParams.put("ID", id);
        }
    }
    /* 删除订单 */
    class DeleteImageOrder extends HttpEvent<String> {

        public DeleteImageOrder(String id, HttpListener<String> mListener) {
            super(mListener);

            mReqMethod = HttpClient.DELETE;

            mReqAction = "/UserConsults?ID="+id;

//            mReqParams = new HashMap<>();
//            mReqParams.put("ID", id);
        }
    }

}
