package com.kmwlyy.patient.helper.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.Department;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Winson on 2016/8/13.
 */
public interface HttpDepartments {

    class GetList extends HttpEvent<ArrayList<Department>> {

        public GetList(int currentPage, int pageSize, HttpListener<ArrayList<Department>> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;
            mReqAction = "/Departments";
            mReqParams = new HashMap();
            mReqParams.put("CurrentPage", "" + currentPage);
            mReqParams.put("PageSize", "" + pageSize);

        }

    }

    class GetDetail extends HttpEvent<Department> {

        public GetDetail(String departmentId, HttpListener<Department> mListener) {
            super(mListener);

            mReqMethod = HttpClient.GET;
            mReqAction = "/Departments";

            mReqParams = new HashMap();
            mReqParams.put("ID", departmentId);
        }

    }

}
