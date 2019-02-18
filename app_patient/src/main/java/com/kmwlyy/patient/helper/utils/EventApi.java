package com.kmwlyy.patient.helper.utils;

import com.kmwlyy.patient.helper.net.bean.DoctorScheduleItem;
import com.kmwlyy.patient.onlinediagnose.BuyVideoConsultActivity;

/**
 * Created by Winson on 2016/8/6.
 */
public interface EventApi {

    class Login {

    }

    class Logout {
        public boolean active;
    }

    class AppTokenExpired {
        public String mOldAppToken;
    }

    class MainTabSelect {
        public static final int HOME = 0;
        public static final int REGISTER_ORDER = 1;
        public static final int ONLINE_DIAGNOSE = 2;
        public static final int MY_PROFILE = 3;
        public static final int MY_COSULT = 101;
        public static final int MY_DIOAGNOSE= 102;
        public static final int MY_DOCTOR = 103;
        public static final int MY_PLAN = 4;
        public int position;
        public int secondPosition;
        public int type;
    }


    class UserMemberUpdated {

    }

    class Pay {
        public boolean success;
        public boolean repeat;
    }

    class Duty {

    }

    class MyServiceTab {

        public static final int FAMILY_DOCTOR = 0;
        public static final int CONSULT = 1;
        public static final int REGISTER = 2;
        public static final int DIAGNOSE = 3;

        public int position;
    }

    class MainPayPosition {
        public int position;
    }

    class DutyList {

    }

    class SelectTime{
        public DoctorScheduleItem doctorScheduleItem;
    }

    class OpenBuyVV{
        public DoctorScheduleItem doctorScheduleItem;
    }

    class RefreshFollowState{
        public String doctorId;
        public RefreshFollowState(String doctorId) {
            this.doctorId = doctorId;
        }
    }

    class CaseHistory{

    }

    class BuyVVConsultSuc{

    }

    //刷新我的套餐页面
    class RefreshMyPlan{}

    //刷新图文咨询列表
    class RefreshImageConsultList{}
    //刷新语音视频咨询列表
    class RefreshVVConsultList{}
    //刷新会诊咨询列表
    class RefreshMeetingConsultList{}
    //刷新PDF页面的处方信息
    class RefreshPDFRecipeFiles{}

    class SetPayPasswordSuc{}

    class WithdrawSuc{}

    class RechargeSuc{}

}
