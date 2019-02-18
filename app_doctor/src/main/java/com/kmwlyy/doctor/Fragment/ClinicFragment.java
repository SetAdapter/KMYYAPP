package com.kmwlyy.doctor.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.Activity.AccountManagementActivity;
import com.kmwlyy.doctor.Activity.BaseActivity;
import com.kmwlyy.doctor.Activity.EvaluateActivity;
import com.kmwlyy.doctor.Activity.MyFansActivity;
import com.kmwlyy.doctor.Activity.OrderListActivity;
import com.kmwlyy.doctor.Activity.PatientManActivity;
import com.kmwlyy.doctor.Activity.QuestionListActivity;
import com.kmwlyy.doctor.Activity.ServiceSettingsActivity;
import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.jpush.Doctor_EventApi;
import com.kmwlyy.doctor.model.DoctorInfo;
import com.kmwlyy.doctor.model.DoctorInfoBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getDoctorInfo1_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_getMyInfo_Event;
import com.kmwlyy.personinfo.EventApi;
import com.kmwlyy.personinfo.PersonInfoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import doctor.kmwlyy.com.recipe.DrugListActivity;


/***
 * 我的诊所
 */
public class ClinicFragment extends Fragment implements OnClickListener {
    public static final String TAG = "ClinicFragment";
    @ViewInject(R.id.iv_head)
    ImageView iv_head;
    @ViewInject(R.id.tv_name)
    TextView tv_name;
    @ViewInject(R.id.tv_hospital)
    TextView tv_hospital;
    @ViewInject(R.id.tv_fans)
    TextView tv_fans;
    @ViewInject(R.id.tv_query_times)
    TextView tv_query_times;
    @ViewInject(R.id.tv_income)
    TextView tv_income;
    @ViewInject(R.id.tv_evaluate_num)
    TextView tv_evaluate_num;

    public Context mContext;
    public DoctorInfo doctorInfo;
    public String doctorId = "";
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_clinic, null);
        ViewUtils.inject(this, view); //注入view和事件
		EventBus.getDefault().register(this);
        //初使化
        init(view);
        getDoctorInfo();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(View view) {
        mContext = getActivity();
        view.findViewById(R.id.ll_myorder).setOnClickListener(this);
        view.findViewById(R.id.ll_sign_recipe).setOnClickListener(this);
        view.findViewById(R.id.ll_question).setOnClickListener(this);
        view.findViewById(R.id.ll_patient_manager).setOnClickListener(this);
        view.findViewById(R.id.ll_druglist).setOnClickListener(this);
        view.findViewById(R.id.ll_serviceset).setOnClickListener(this);
        view.findViewById(R.id.ll_earn).setOnClickListener(this);
        view.findViewById(R.id.ll_query).setOnClickListener(this);
        view.findViewById(R.id.ll_fans).setOnClickListener(this);
        view.findViewById(R.id.ll_evaluate).setOnClickListener(this);
    }


    @Override
    public void onStart() {
        super.onStart();


    }

    private void hideInputKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()){
            if(view != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_myorder://查看订单
                getActivity().startActivity(new Intent(getActivity(), OrderListActivity.class));
                break;
            case R.id.ll_sign_recipe://待签处方
                break;
            case R.id.ll_question://问题库
                getActivity().startActivity(new Intent(getActivity(), QuestionListActivity.class));
//                Intent recipeIntent = new Intent(getActivity(), RecipeActivity.class);
//                recipeIntent.putExtra("id", "992781ffe24842949a12b5f3366c6d17");
//                startActivity(recipeIntent);
                break;
            case R.id.ll_patient_manager://患者管理
                getActivity().startActivity(new Intent(getActivity(), PatientManActivity.class));
                break;
            case R.id.ll_druglist://处方集
                getActivity().startActivity(new Intent(getActivity(), DrugListActivity.class));
                break;
            case R.id.ll_serviceset://服务设置
                getActivity().startActivity(new Intent(getActivity(), ServiceSettingsActivity.class));
                break;
            case R.id.ll_earn://收益
                startActivity(new Intent(mContext,AccountManagementActivity.class));
                break;
            case R.id.ll_query://问诊
                getActivity().startActivity(new Intent(getActivity(), OrderListActivity.class));
                break;
            case R.id.ll_fans://粉丝
                getActivity().startActivity(new Intent(getActivity(), MyFansActivity.class));
                break;
            case R.id.ll_evaluate://评价
                Intent intent = new Intent(mContext,EvaluateActivity.class);
                intent.putExtra("id",doctorId);
                getActivity().startActivity(intent);
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
	public void onHeadPhotoEvent(EventApi.HeadPhoto event){
		ImageLoader.getInstance().displayImage(event.photoUrl, iv_head, PersonInfoActivity.getCircleDisplayOptions(R.drawable.default_avatar_patient));
	}

    /**
     * 接收推送过来的医生 问诊、粉丝等信息
     * @param bean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateDoctorInfo(Doctor_EventApi.updateDoctorInfo bean){
        Log.i(TAG,"获取后台推送过来的医生 问诊、粉丝等信息： " + bean.toString());
        //验证是否是当前登录的医生，如果是才更新界面。否则不更新
        if(bean.getUserID().equals(BaseApplication.getInstance().getUserData().mUserId) &&
           bean.getDoctorName().equals(BaseApplication.getInstance().getUserData().mUserCNName)){
            tv_query_times.setText(""+bean.getDiagnoseTimes());
            tv_fans.setText(""+bean.getFollowedCount());
            tv_income.setText(""+bean.getIncome());
            tv_evaluate_num.setText(""+bean.getEvaluatedCount());
        }

    }

    /**
     * 获取医生信息
     */
    private void getDoctorInfo() {
        ((BaseActivity)getActivity()).showLoadDialog(R.string.string_get_info);

        Http_getMyInfo_Event event = new Http_getMyInfo_Event(mContext,new HttpListener<DoctorInfo>() {
            @Override
            public void onError(int code, String msg) {
                ((BaseActivity)getActivity()).dismissLoadDialog();
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }

                ToastUtils.showShort(getActivity(), mContext.getResources().getString(R.string.string_get_info_failed));
                MyApplication.getApplication(getActivity()).logout();
            }

            @Override
            public void onSuccess(DoctorInfo doctor) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                ((BaseActivity)getActivity()).dismissLoadDialog();
                if(null != doctor){
                    doctorInfo = doctor;
                    doctorId = doctor.getDoctorID();
                    SPUtils.put(getActivity(), SPUtils.DOCTOR_ID, doctor.getDoctorID());
                    ImageLoader.getInstance().displayImage(doctor.getUser().getPhotoUrl(), iv_head, PersonInfoActivity.getCircleDisplayOptions(R.drawable.default_avatar_patient));
                }
                getOtherInfo();
            }
        }
        );
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

    /**
     * 获取收益，问诊，粉丝，评价
     */
    private void getOtherInfo() {
//        ((BaseActivity)getActivity()).showLoadDialog(R.string.string_get_data);
        Http_getDoctorInfo1_Event event = new Http_getDoctorInfo1_Event(mContext,new HttpListener<DoctorInfoBean>() {
            @Override
            public void onError(int code, String msg) {
                ((BaseActivity)getActivity()).dismissLoadDialog();
                if (DebugUtils.debug) {
                    Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
                }
            }

            @Override
            public void onSuccess(DoctorInfoBean bean) {
                if (DebugUtils.debug) {
                    Log.i(TAG, "request success");
                }
                ((BaseActivity)getActivity()).dismissLoadDialog();
                tv_query_times.setText(""+bean.getDiagnoseTimes());
                tv_fans.setText(""+bean.getFollowedCount());
                tv_income.setText(""+bean.getIncome());
                tv_evaluate_num.setText(""+bean.getEvaluatedCount());
                tv_hospital.setText(bean.getHospitalName() + " " +bean.getDepartmentName());
                tv_name.setText(""+bean.getDoctorName());
            }
        }
        );
        HttpClient httpClient = NetService.createClient(mContext, event);
        httpClient.start();
    }

}
