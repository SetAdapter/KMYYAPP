package com.kmwlyy.patient.helper.net.event;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.patient.helper.net.bean.UserConsult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Winson on 2016/8/18.
 */
public interface HttpUserConsults {

    class ImageFile {

        @SerializedName("FileName")
        public String mFileName;//:"图片名称",

        @SerializedName("FileUrl")
        public String mFileUrl;//:"/xxx/xxx.jpg",

        @SerializedName("FileType")
        public int mFileType;//:0,//文件类型 0=图片

        @SerializedName("Remark")
        public String mRemark;//:"这里是图片的说明"

    }

    //    0-付费、1-免费、2-义诊、3-套餐、4-会员
    //0-未筛选、1-未领取、2-未回复、4-已回复、5-已完成
    int PAY = 0;
    int FREE = 1;
    int DUTY = 2;
    int MEAL = 3;
    int FAMILY_DOCTOR = 5;

    class Add extends HttpEvent<UserConsult.AddResp> {

        public Add(String MemberID, String DoctorID, List<ImageFile> images, String ConsultContent, int Privilege, boolean free, HttpListener<UserConsult.AddResp> listener) {
            super(listener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/UserConsults";

            noParmName = true;

            HashMap<String, Object> params = new HashMap<>();
            params.put("MemberID", MemberID);
            params.put("DoctorID", DoctorID);
            params.put("ConsultContent", ConsultContent);
            params.put("Privilege", "" + Privilege);
            params.put("Free", free);
//            if (images != null) {
            params.put("Files", images);
//            }

            mJsonData = new Gson().toJson(params);
        }

    }

    class AddGroup extends HttpEvent<UserConsult.AddResp> {

        public AddGroup(String MemberID, String DoctorID, List<ImageFile> images, String ConsultContent, int Privilege, boolean free, HttpListener<UserConsult.AddResp> listener) {
            super(listener);

            mReqMethod = HttpClient.POST;

            mReqAction = "/UserConsults";

            noParmName = true;

            HashMap<String, Object> params = new HashMap<>();
            params.put("MemberID", MemberID);
            params.put("DoctorGroupID", DoctorID);
            params.put("ConsultContent", ConsultContent);
            params.put("Privilege", "" + Privilege);
            params.put("Free", free);
//            if (images != null) {
            params.put("Files", images);
//            }

            mJsonData = new Gson().toJson(params);
        }

    }

    class GetList extends HttpEvent<ArrayList<UserConsult>> {

        public GetList(int currentPage, int pageSize, String keyword, HttpListener<ArrayList<UserConsult>> listener) {
            super(listener);

            mReqMethod = HttpClient.GET;

            mReqAction = "/UserConsults/Consulted";

            mReqParams = new HashMap<>();
            mReqParams.put("CurrentPage", "" + currentPage);
            mReqParams.put("PageSize", "" + pageSize);
            mReqParams.put("Keyword", keyword);

        }

    }

}
