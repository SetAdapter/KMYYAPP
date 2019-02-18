package com.kmwlyy.registry.net;

import android.text.TextUtils;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UserMember;
import com.kmwlyy.registry.bean.Registry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016-8-15.
 */
public interface NetEvent {
    /* 获取热门城市 */
    class GetHotCity extends HttpEvent<String> {
        public GetHotCity() {
            mReqMethod = HttpClient.GET;
            mReqAction = "/Appointment/GetCity";
            mReqParams = new HashMap<>();
            mReqParams.put("isHot",""+true);
        }
    }
    class GetArea extends HttpEvent<List<NetBean.Area>> {
        public GetArea(String parent_id) {
            mReqMethod = HttpClient.GET;
            mReqAction = "/Appointment/GetArea";
            mReqParams = new HashMap<>();
            mReqParams.put("parent_id",parent_id);
        }
    }

    class GetOneArea extends HttpEvent<NetBean.Area> {
        public GetOneArea(String province, String city) {
            mReqMethod = HttpClient.GET;
            mReqAction = "/Appointment/GetArea";
            mReqParams = new HashMap<>();
            if (!TextUtils.isEmpty(province)){
                mReqParams.put("Level1name",province);
            }
            if (!TextUtils.isEmpty(city)){
                mReqParams.put("Level2name",city);
            }
        }
    }



    class GetHospital extends HttpEvent<List<NetBean.Hospital>> {
        public GetHospital(String area_id,String city_id) {
            mReqMethod = HttpClient.GET;
            mReqAction = "/Appointment/GetHospital";
            mReqParams = new HashMap<>();
            mReqParams.put("area_id",area_id);
            mReqParams.put("city_id",city_id);
        }
        public GetHospital(int uid) {
            mReqMethod = HttpClient.GET;
            mReqAction = "/Appointment/GetHospital";
            mReqParams = new HashMap<>();
            mReqParams.put("uid", ""+uid);
        }
    }

    class GetDepartment extends HttpEvent<List<NetBean.Department>> {
        public GetDepartment(String uid,String depid) {
            mReqMethod = HttpClient.GET;
            mReqAction = "/Appointment/GetDepartment";
            mReqParams = new HashMap<>();
            mReqParams.put("uid",uid);
            mReqParams.put("depid",depid);
        }
    }
    class GetHospitalDepartmentTree extends HttpEvent<ArrayList<NetBean.DepartmentTree>> {
        public GetHospitalDepartmentTree(int uid,String class_id) {
            mReqMethod = HttpClient.GET;
            mReqAction = "/Appointment/GetDepClassList";
            mReqParams = new HashMap<>();
            mReqParams.put("uid",""+uid);
            mReqParams.put("class_id",class_id);
        }
    }
    class GetDoctor extends HttpEvent<List<NetBean.Doctor>> {
        public GetDoctor(String uid,String depid) {
            mReqMethod = HttpClient.GET;
            mReqAction = "/Appointment/GetDoctor";
            mReqParams = new HashMap<>();
            mReqParams.put("uid",uid);
            mReqParams.put("depid",depid);
        }
    }
    class GetSchedule extends HttpEvent<List<NetBean.Schedule>> {
        public GetSchedule(String uid,String depid,String docid,String date) {
            mReqMethod = HttpClient.GET;
            mReqAction = "/Appointment/GetSchedule";
            mReqParams = new HashMap<>();
            mReqParams.put("uid",uid);
            mReqParams.put("depid",depid);
            mReqParams.put("docid",docid);
            //mReqParams.put("date",date);
        }
    }
    class GetSchDetl extends HttpEvent<List<NetBean.SchDetl>> {
        public GetSchDetl(String uid,String schid) {
            mReqMethod = HttpClient.GET;
            mReqAction = "/Appointment/GetSchDetl";
            mReqParams = new HashMap<>();
            mReqParams.put("uid",uid);
            mReqParams.put("schid",schid);
        }
    }

    class GetFamilyYuyue extends HttpEvent<List<NetBean.Order>> {
        public GetFamilyYuyue(String userid,String Orderid,int page,int page_size) {
            mReqMethod = HttpClient.GET;
            mReqAction = "/Appointment/GetFamilyYuyue";
            mReqParams = new HashMap<>();
            mReqParams.put("userid",userid);
            //mReqParams.put("Orderid",Orderid);
            mReqParams.put("page",""+page);
            mReqParams.put("page_size",""+page_size);
        }
    }
    class SetYuyue extends HttpEvent<String> {
        public SetYuyue(Registry item, String cardNo, String password, int visit_Status) {
            mReqMethod = HttpClient.POST;
            mReqAction = "/Appointment/SetYuyue";
            mReqParams = new HashMap<>();
            mReqParams.put("Userid",item.getUserid());
            mReqParams.put("Memberid",item.getMemberid());
            mReqParams.put("Uid",item.getUid());
            mReqParams.put("Dep_id",item.getDep_id());
            mReqParams.put("Doctor_id",item.getDoctor_id());
            mReqParams.put("Phone",item.getPhone());
            mReqParams.put("Card",item.getCard());
            mReqParams.put("Card_type",item.getCard_type());
            mReqParams.put("Truename",item.getTruename());
            mReqParams.put("Sex",item.getSex());
            mReqParams.put("Birth",item.getBirth());
            mReqParams.put("Detlid",item.getDetlid());
            if (!TextUtils.isEmpty(cardNo)){
                mReqParams.put("Clinic_No",cardNo);
            }
            if (!TextUtils.isEmpty(password)){
                mReqParams.put("CardPassword",password);
            }
            if (visit_Status == 1 || visit_Status == 2){
                mReqParams.put("Visit_Status",""+visit_Status);
            }
        }
    }

    class CancelRegister extends HttpEvent<String> {
        public CancelRegister(String userID,String orderID,String MemberName) {
            mReqMethod = HttpClient.POST;
            mReqAction = "/Appointment/CancelRegister";
            mReqParams = new HashMap<>();
            mReqParams.put("orderID",orderID);
            mReqParams.put("userID",userID);
            mReqParams.put("MemberName",MemberName);
        }
    }

    class GetMemberDefault extends HttpEvent<ArrayList<UserMember>> {
        public GetMemberDefault() {
            mReqMethod = HttpClient.GET;
            mReqAction = "/UserMembers/GetDefaultMember";
            mReqParams = new HashMap<>();
        }
    }

    /* 获取是否需要输入诊疗卡 */
    class GetNeedDiagnoseCard extends HttpEvent<NetBean.HospitalRegistryConfig> {
        public GetNeedDiagnoseCard(String uid) {
            mReqMethod = HttpClient.GET;
            mReqAction = "/Appointment/GetHospitalCfg";//?uid="+uid;
            mReqParams = new HashMap<>();
            mReqParams.put("uid",uid);
        }
    }
}
