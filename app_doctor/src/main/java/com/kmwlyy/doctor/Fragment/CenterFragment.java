package com.kmwlyy.doctor.Fragment;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.doctor.Activity.AccountManagementActivity;

import com.kmwlyy.doctor.Activity.ClinicRuleActivity;
import com.kmwlyy.doctor.Activity.DrugStoreRecipeActivity;
import com.kmwlyy.doctor.Activity.EvaluateActivity;
import com.kmwlyy.doctor.Activity.MainActivity;
import com.kmwlyy.doctor.Activity.MyFansActivity;
import com.kmwlyy.doctor.Activity.OrderListActivity;
import com.kmwlyy.doctor.Activity.SettingActivity;

import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.jpush.Doctor_EventApi;
import com.kmwlyy.doctor.model.DoctorInfo;
import com.kmwlyy.doctor.model.DoctorInfoBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getDoctorInfo1_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_getMyInfo_Event;
import com.kmwlyy.personinfo.PersonInfoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.networkbench.agent.impl.NBSAppAgent;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class CenterFragment extends Fragment implements OnClickListener {
	public static final String TAG = "CenterFragment";

	@ViewInject(R.id.lay_drugstore)
	LinearLayout lay_drugstore;
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

	public String doctorId = "";
	public Context mContext;
	public DoctorInfo doctorInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_center, null);
		ViewUtils.inject(this,view); //注入view和事件
		mContext = getActivity();
		doctorInfo = new DoctorInfo();

		view.findViewById(R.id.lay_rule).setOnClickListener(this);
		view.findViewById(R.id.lay_yue).setOnClickListener(this);
		view.findViewById(R.id.lay_cert).setOnClickListener(this);
		view.findViewById(R.id.ll_person_info).setOnClickListener(this);
		view.findViewById(R.id.lay_setting).setOnClickListener(this);
		view.findViewById(R.id.ll_earn).setOnClickListener(this);
		view.findViewById(R.id.ll_query).setOnClickListener(this);
		view.findViewById(R.id.ll_fans).setOnClickListener(this);
		view.findViewById(R.id.ll_evaluate).setOnClickListener(this);

		//如果等于3，才
		if(BaseApplication.getInstance().getUserData().UserLevel == 3){
			lay_drugstore.setVisibility(View.VISIBLE);
			lay_drugstore.setOnClickListener(this);
		}else{
			lay_drugstore.setVisibility(View.GONE);
		}
		getDoctorInfo();
		EventBus.getDefault().register(this);
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	/**
	 * 接收推送过来的医生 问诊、粉丝等信息
	 * @param bean
	 */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void updateDoctorInfo(Doctor_EventApi.updateDoctorInfo bean){
		LogUtils.i(TAG,"获取后台推送过来的医生 问诊、粉丝等信息： " + bean.toString());
		//验证是否是当前登录的医生，如果是才更新界面。否则不更新
//		if(bean.getUserID().equals(BaseApplication.getInstance().getUserData().mUserId) &&
//				bean.getDoctorName().equals(BaseApplication.getInstance().getUserData().mUserCNName)){
//			tv_query_times.setText(""+bean.getDiagnoseTimes());
//			tv_fans.setText(""+bean.getFollowedCount());
//			tv_income.setText(""+bean.getIncome());
//			tv_evaluate_num.setText(""+bean.getEvaluatedCount());
//		}
		//后台推的数据不准，改成获取数据
		getDoctorInfo();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.lay_yue://帐户管理
				NBSAppAgent.onEvent("个人中心-账户管理");
				startActivity(new Intent(mContext,AccountManagementActivity.class));
				break;
			case R.id.lay_rule://诊所规则
				startActivity(new Intent(mContext,ClinicRuleActivity.class));
				break;
			case R.id.lay_cert://证书管理
				NBSAppAgent.onEvent("个人中心-证书管理");
				MainActivity parentActivity = ( MainActivity ) getActivity();
				parentActivity.getCertYWQ();
				break;
			case R.id.lay_drugstore://药店处方
				startActivity(new Intent(mContext,DrugStoreRecipeActivity.class));
				break;
			case R.id.ll_person_info://个人资料
				PersonInfoActivity.startPersonInfoActivity(getActivity(), false,  BaseApplication.getInstance().getUserData().mUserId,"","","");
				break;
			case R.id.lay_setting://设置
				NBSAppAgent.onEvent("个人中心-点击设置");
				startActivity(new Intent(mContext,SettingActivity.class));
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

	/**
	 * 获取医生信息
	 */
	private void getDoctorInfo() {
		Http_getMyInfo_Event event = new Http_getMyInfo_Event(mContext,new HttpListener<DoctorInfo>() {
			@Override
			public void onError(int code, String msg) {
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
		Http_getDoctorInfo1_Event event = new Http_getDoctorInfo1_Event(mContext,new HttpListener<DoctorInfoBean>() {
			@Override
			public void onError(int code, String msg) {
				if (DebugUtils.debug) {
					Log.i(TAG, "request error , code : " + code + " , msg : " + msg);
				}
			}

			@Override
			public void onSuccess(DoctorInfoBean bean) {
				if (DebugUtils.debug) {
					Log.i(TAG, "request success");
				}
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
