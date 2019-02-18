package com.kmwlyy.patient.module.homepage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpFilter;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.SignatureBean;
import com.kmwlyy.core.net.event.GetMySignature;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.login.EventMessageClear;
import com.kmwlyy.login.MessageApi;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.patient.jpush.MyMessageActivity;
import com.kmwlyy.patient.kdoctor.activity.TcmConstitutionActivity;
import com.kmwlyy.patient.kdoctor.activity.UserMemberHealthActivity;
import com.kmwlyy.patient.main.PerfectInfoActivity;
import com.kmwlyy.patient.module.myfamiiydoctor.FamilyDoctorActivity;
import com.kmwlyy.patient.module.myservice.MyServiceActivity;
import com.kmwlyy.patient.module.sign.SignActivity;
import com.kmwlyy.patient.symptomGuide.AiBodyActivity;
import com.kmwlyy.patient.weight.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 首页
 * Created by Stefan on 2017/8/10 17:46.
 */

public class NewFamilyDoctorFragment extends BaseFragment implements View.OnClickListener {

    //    @BindView(R.id.health_record)
//    TextView health_record;
    @BindView(R.id.zytz)
    RelativeLayout zytz;
    @BindView(R.id.health_report)
    RelativeLayout health_report;
    @BindView(R.id.myService)
    RelativeLayout myService;
    @BindView(R.id.zzzc)
    RelativeLayout zzzc;
    @BindView(R.id.wywz)
    RelativeLayout wywz;
    @BindView(R.id.doctor_tz)
    ImageView doctor_tz;
    public ProgressDialog mLoadingDialog;
    @BindView(R.id.tabNewMsg)
    TextView tv_right;

    @Override
    protected int getContentLayout() {
        return R.layout.new_fragment_family_doctor;
    }

    @Override
    protected void baseInitView() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick({R.id.health_report, R.id.myService, R.id.zzzc, R.id.wywz, R.id.iv_ll, R.id.zytz, R.id.doctor_tz})
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.health_report:
                getStateJump(R.id.health_report);
                break;
            case R.id.myService:
                getStateJump(R.id.myService);
                break;
            case R.id.zzzc:
                getStateJump(R.id.zzzc);
                break;
            case R.id.wywz:
                getStateJump(R.id.wywz);
                break;
            case R.id.doctor_tz:
                getStateJump(R.id.doctor_tz);
                break;
            case R.id.zytz:
                getStateJump(R.id.zytz);
                break;
        }
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        getState();
//    }

    /**
     * 获取屏幕宽度，单位像素
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    /* 获取屏幕dp */
    public static int getScreenWidthDp(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度（像素）
        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
//        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
        //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        return (int) (width / density);//屏幕宽度(dp)
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private boolean jump() {
        if (PUtils.checkHaveUser(getActivity())) {
            if (!TextUtils.isEmpty(BaseApplication.getInstance().getUserData().mIDNumber)) {
                if (!TextUtils.isEmpty(BaseApplication.getInstance().getSignatureData().mSignatureID)) {
                    return true;
                } else {
                    //跳转到签约页面
                    startActivity(new Intent(getActivity(), SignActivity.class));
                    return false;
                }
            } else {
                //跳转到信息完善页面PerfectInfoActivity
                startActivity(new Intent(getActivity(), PerfectInfoActivity.class));
                return false;
            }
        }
        return false;
    }


    private void getStateJump(final int id) {
        if (PUtils.checkHaveUser(getActivity())) {
            showLoadDialog("正在获取签约状态...");
            GetMySignature getMySignature = new GetMySignature(new HttpListener<SignatureBean>() {
                @Override
                public void onError(int code, String msg) {
                    // ToastUtils.showShort();
                    dismissLoadDialog();
                }

                @Override
                public void onSuccess(SignatureBean signatureBean) {
                    dismissLoadDialog();
                    if (signatureBean != null) {
                        if (!TextUtils.isEmpty(signatureBean.mFDGroupID)) {
                            BaseApplication.getInstance().setSignatureData(signatureBean);
                        } else {
                            BaseApplication.getInstance().setSignatureData(new SignatureBean());
                        }
                    } else {
                        BaseApplication.getInstance().setSignatureData(new SignatureBean());
                    }
                    if (jump()) {
                        switch (id) {
                            case R.id.health_report:
                                startActivity(new Intent(getActivity(), UserMemberHealthActivity.class));
                                break;
                            case R.id.myService:
                                startActivity(new Intent(getActivity(), MyServiceActivity.class));
                                break;
                            case R.id.zzzc:
                                startActivity(new Intent(getActivity(), AiBodyActivity.class));
                                break;
                            case R.id.wywz:
                                startActivity(new Intent(getActivity(), FamilyDoctorActivity.class));
                                break;
                            case R.id.doctor_tz:
                                if (BaseApplication.instance.hasUserToken()) {
                                    startActivity(new Intent(getActivity(), MyMessageActivity.class));
                                } else {
                                    BaseApplication.instance.logout();
                                }
                                break;
                            case R.id.zytz:
                                startActivity(new Intent(getActivity(), TcmConstitutionActivity.class));
                                break;
                        }
                    }
                }
            });
            new HttpClient(getActivity(), getMySignature, HttpClient.FAMILY_URL, BaseApplication.getInstance().getHttpFilter()).start();
        }
    }

    /**
     * 显示 Dialog
     */
    public void showLoadDialog(String msg) {
        mLoadingDialog = new ProgressDialog(getActivity());
        mLoadingDialog.setMessage(msg);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.show();
    }

    /**
     * 取消 Dialog
     */
    public void dismissLoadDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clearUnreadCount(EventMessageClear arg) {
        tv_right.setText("");
        tv_right.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsgUnreadCount(MessageApi.Message message) {
        if (!BaseApplication.instance.hasUserToken()) {
            tv_right.setVisibility(View.GONE);
            return;
        }
        MessageApi.getMsgUnreadCount event = new MessageApi.getMsgUnreadCount(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(getActivity(), msg);
            }

            @Override
            public void onSuccess(String count) {
                if (count.equals("0")) {
                    tv_right.setVisibility(View.GONE);
                } else {
                    if (count.length() > 2) {
                        tv_right.setText("99+");
                    } else {
                        tv_right.setText(count);
                    }
                    tv_right.setVisibility(View.VISIBLE);
                }
            }
        });
        new HttpClient(getActivity(), event).start();
    }
}
