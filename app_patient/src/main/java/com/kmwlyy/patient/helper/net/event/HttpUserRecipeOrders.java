package com.kmwlyy.patient.helper.net.event;

import com.google.gson.Gson;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.CreateOrderBean;
import com.kmwlyy.patient.helper.net.bean.DiagnoseDetails;
import com.kmwlyy.patient.helper.net.bean.PrescriptionOrders;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Winson on 2016/8/13.
 */
public interface HttpUserRecipeOrders {

    class GetList extends HttpEvent<ArrayList<PrescriptionOrders.ListItem>> {

        public GetList(String keyword, String beginDate, String endDate, int currentPage, int pageSize, HttpListener<ArrayList<PrescriptionOrders.ListItem>> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/UserRecipeOrders";

            mReqParams = new HashMap();
            mReqParams.put("CurrentPage", "" + currentPage);
            mReqParams.put("PageSize", "" + pageSize);
            mReqParams.put("Keyword", keyword);
            mReqParams.put("BeginDate", beginDate);
            mReqParams.put("EndDate", endDate);

        }

    }

    class GetDetail extends HttpEvent<DiagnoseDetails> {

        public GetDetail(String orderNO, HttpListener<DiagnoseDetails> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/UserRecipeOrders";

            mReqParams = new HashMap();
            mReqParams.put("OrderNO", orderNO);

        }
    }

    class CreateOrder extends HttpEvent<CreateOrderBean> {

        public CreateOrder(String mOPDRegisterID,ArrayList<String> mRecipeNos, HttpListener<CreateOrderBean> mListener){
            super(mListener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/UserRecipeOrders";

            noParmName = true;

            HashMap<String, Object> params = new HashMap<>();
            params.put("OPDRegisterID", mOPDRegisterID);
            params.put("RecipeNos", mRecipeNos);

            mJsonData = new Gson().toJson(params);
        }
    }

    /* 删除处方订单 */
    class DeleteRecipeFileOrder extends HttpEvent<String> {

        public DeleteRecipeFileOrder(String id, HttpListener<String> mListener) {
            super(mListener);

            mReqMethod = HttpClient.DELETE;

            mReqAction = "/UserRecipeOrders?ID="+id;

//            mReqParams = new HashMap<>();
//            mReqParams.put("ID", id);
        }
    }
}
