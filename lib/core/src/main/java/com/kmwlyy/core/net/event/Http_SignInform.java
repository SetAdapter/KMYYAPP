package com.kmwlyy.core.net.event;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.RegionsTreeBean;

import java.util.List;

/**
 * Created by xcj on 2017/8/24.
 */

public interface Http_SignInform {
    /**
     * 获取地域区域 获取全国区域(树结构
     */
    public class Http_getRegions extends HttpEvent<List<RegionsTreeBean.DataBean>> {
        public Http_getRegions(HttpListener<List<RegionsTreeBean.DataBean>> mListener){
            super(mListener);
            mReqAction = "/DoctorGroup/GetRegionsTree";
            mReqMethod = HttpClient.GET;

        }
    }
}
