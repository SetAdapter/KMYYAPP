package com.kmwlyy.patient.module.InhabitantStart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UploadImageResp;
import com.kmwlyy.core.net.bean.UserMember;
import com.kmwlyy.core.net.event.HttpIM;
import com.kmwlyy.core.net.event.HttpUser;
import com.kmwlyy.core.net.event.HttpUserMember;
import com.kmwlyy.core.util.CommonUtils;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.module.familydoctorteam.ChangeDoctorTeam;
import com.kmwlyy.patient.module.familydoctorteam.FamilyDoctorTeamActivity;
import com.kmwlyy.patient.module.familydoctorteam.NewChangeDoctorTeam;
import com.kmwlyy.patient.module.signcomfirm.FinishEvent;
import com.kmwlyy.patient.module.signcomfirm.SignComfirmActivity;
import com.kmwlyy.patient.module.teamdetail.TeamDetailActivity;
import com.kmwlyy.patient.weight.NoScrollListView;
import com.kmwlyy.patient.weight.SignDialog;
import com.kmwlyy.patient.weight.pickerview.OptionsPickerView;
import com.kmwlyy.usermember.UserMemberEditActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 签约医生团队
 * Created by Administrator on 2017/7/29.
 */
public class InhabitanStartActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = InhabitanStartActivity.class.getSimpleName();

    @BindView(R.id.iv_tools_left)
    Button mIvToolsLeft;
    @BindView(R.id.iv_tools_right)
    Button mIvToolsRight;
    @BindView(R.id.tv_title_center)
    TextView mTvTitleCenter;
    @BindView(R.id.firstparty)
    TextView mFirstparty;
    @BindView(R.id.doctorteamname)
    TextView mDoctorteamname;
    @BindView(R.id.doctorteammaster)
    TextView mDoctorteammaster;
    @BindView(R.id.text_td_ll)
    LinearLayout mTextTdLl;
    @BindView(R.id.image_gh)
    TextView mImageGh;
    @BindView(R.id.IDNumber_edt)
    EditText mIDNumberEdt;
    @BindView(R.id.familyhealthno)
    EditText mFamilyhealthno;
    @BindView(R.id.relation_phone)
    TextView mRelationPhone;
    @BindView(R.id.identify_code)
    EditText mIdentifyCode;
    @BindView(R.id.get_identify_code)
    Button mGetIdentifyCode;
    @BindView(R.id.familyaddress)
    EditText mFamilyaddress;
    @BindView(R.id.add_list)
    NoScrollListView mAddList;
    @BindView(R.id.confirm_tv)
    Button mConfirmTv;
    @BindView(R.id.confirm_ll)
    LinearLayout mConfirmLl;
    @BindView(R.id.area_rl)
    LinearLayout mAreaRl;
    @BindView(R.id.row_rl)
    LinearLayout mRowRl;
    @BindView(R.id.area_tv)
    TextView mAreaTv;
    @BindView(R.id.signature_username)
    EditText mSignatureUsername;

    @BindView(R.id.tvStrweetName)
    TextView tvStrweetName;

    private MemberAdapter mAdapter;
    private AlertDialog mAlertDialog;
    private boolean mCouldNotRepeatGetIndetifyCode;
    private String mRepeatGetIndentifyCodeStr;
    private CountDownTimer mTimer;

    private String mProvinceStr;
    private String mCityStr;
    private String mCountyStr;
    //家庭成员的列表
    List<MySignInformBean.MembersBean> mUserMemberList = new ArrayList<>();
    private String mOrgnazitionID;
    private String mUrlPrefix;
    private String mDoctorGroupID;
    private Object mHttp_getRegions;
    private String mStrweetName;
    private String orgnazitionID;
    private String doctorGroupID;
    public static final int CHECKCODE_TIMEOUT = 61;// 验证码超时
    private NewChangeDoctorTeam.DataBean dataBean;

    @Override
    protected int getLayoutId() {
        return R.layout.inhabitan_start_activity;
    }

    @Override
    protected void afterBindView() {
        mTvTitleCenter.setText("签约医生团队");
        //家庭成员列表 数据
        getUserMemberList();
        getHttp_getRegions();
        //获取我的默认医生团队
        GetMyHosGroup();
        //获取医疗机构服务区域
        GetOrgRegions();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getUserMemberList();
    }

    //处理添加家庭成员
    public void setData(List<MySignInformBean.MembersBean> data) {
        mAdapter = new MemberAdapter(InhabitanStartActivity.this, data);
        mAddList.setAdapter(mAdapter);
    }

    @OnClick({R.id.iv_tools_left, R.id.text_td_ll, R.id.image_gh, R.id.confirm_tv, R.id.add_new_member,
            R.id.get_identify_code, R.id.area_rl, R.id.row_rl, R.id.firstparty})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tools_left:
                finish();
                break;
            case R.id.text_td_ll:  //团队详情进入口
                if (TextUtils.isEmpty(mDoctorteamname.getText().toString())) {
                    ToastUtils.showShort(InhabitanStartActivity.this, "请选择医生团队");
                }
                if (dataBean != null) {
                    Intent mIntent = new Intent(this, TeamDetailActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("DataBean", dataBean);
                    mIntent.putExtras(mBundle);
                    startActivity(mIntent);
                }
                break;
            case R.id.image_gh: //更换医生团队
                Intent intent = new Intent(InhabitanStartActivity.this, FamilyDoctorTeamActivity.class);
                intent.putExtra("mOrgnazitionID",mOrgnazitionID);
                startActivityForResult(intent, 250);
                break;
            case R.id.confirm_tv: //确定签约
                showSignDialog();
                break;
            case R.id.add_new_member: //添加家庭成员  直接跳入添加家人信息
                startActivity(new Intent(InhabitanStartActivity.this, UserMemberEditActivity.class));
                break;
            case R.id.get_identify_code: //获取验证码
                String phone = mFamilyhealthno.getText().toString();
                if (checkPhone(phone)) {
                    return;
                }
                requestIndentifyCode(phone);
                break;
            case R.id.area_rl: //所在区域  选择 省市县
//                showAddressPicker();
                ShowPickerView();
                break;
            case R.id.row_rl: //街道 乡镇的
                showStrweetDialog();
                break;
            case R.id.firstparty: //更换医疗机构服务区域
                showOrgRegionsDialog();
                break;
        }
    }


    /**
     * 显示签名的dialog
     */
    private void showSignDialog() {
        //验证码
        String mIdentifyCodeStr = mIdentifyCode.getText().toString();
        //家庭医生签约ID,修改时用
        String signatureIDStr = "";
        // 医生团队ID（甲方）
        String mFDGroupIDStr = mDoctorGroupID;
        //"医生团队所属机构ID（甲方）
        String mOrgnazitionIDStr = mOrgnazitionID;
        //签名用户姓名(乙方)
        String mSignatureUserNameStr = mSignatureUsername.getText().toString();
        //签名用户身份证(乙方)
        String mSignatureUserIDNumberStr = mIDNumberEdt.getText().toString();

        //家庭健康档案号(乙方)
        String mFamilyFN = "";
        //手机号(乙方)
        String mMobile = mFamilyhealthno.getText().toString();
        //省份(乙方)mProvinceStr
        //城市(乙方)mCityStr
        //市区(乙)mCountyStr
        //乡镇或者街道(乙方)
//        String mSubdistrict = mStrweetName;
        //地址(乙方)
        String mAddressStr = mFamilyaddress.getText().toString();

        if (TextUtils.isEmpty(mIdentifyCodeStr)) {
            ToastUtils.showShort(InhabitanStartActivity.this, "请输入验证码");
            return;
        }

        if (TextUtils.isEmpty(mSignatureUserNameStr)) {
            ToastUtils.showShort(InhabitanStartActivity.this, "请输入姓名");
            return;
        }

        if (TextUtils.isEmpty(mSignatureUserIDNumberStr)) {
            ToastUtils.showShort(InhabitanStartActivity.this, "请输入身份证");
            return;
        }
        if (TextUtils.isEmpty(mAddressStr)) {
            ToastUtils.showShort(InhabitanStartActivity.this, "请输入详细地址");
            return;
        }
        if (TextUtils.isEmpty(mProvinceStr) || TextUtils.isEmpty(mCityStr) || TextUtils.isEmpty(mCountyStr)) {
            ToastUtils.showShort(InhabitanStartActivity.this, "请输入所在区域");
            return;
        }
        if (TextUtils.isEmpty(mStrweetName)) {
            ToastUtils.showShort(InhabitanStartActivity.this, "请输入所在街道");
            return;
        }

        final SignDialog signDialog = new SignDialog(InhabitanStartActivity.this);
        signDialog.setOnDoodleListener(new SignDialog.OnDoodleListener() {
            @Override
            public void OnConfrim(String path) {
                //做一个请求 将图片上传
                saveSignImage(path);

                signDialog.dismiss();


                showComfirm();
            }


        });
        signDialog.show();
        Window dialogWindow = signDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager windowManager = dialogWindow.getWindowManager();
        Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高用
        lp.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕
        lp.width = d.getWidth(); // 宽度设置为屏幕的
        dialogWindow.setAttributes(lp);
    }

    /**
     * 上传图片
     *
     * @param path
     */
    private void saveSignImage(String path) {
        showDialog(getResources().getString(R.string.submit_wait));
        HttpIM.UploadImage uploadImage = new HttpIM.UploadImage(path, new HttpListener<UploadImageResp>() {
            @Override
            public void onError(int code, String msg) {
                hideDialog();
                ToastUtils.showLong(InhabitanStartActivity.this, getResources().getString(com.kmwlyy.personinfo.R.string.post_fail));
            }

            @Override
            public void onSuccess(UploadImageResp uploadImageResp) {
                hideDialog();
                mUrlPrefix = uploadImageResp.mUrlPrefix + uploadImageResp.mFileName;
//                saveImageUrl(path, uploadImageResp.mFileName);
            }
        });
        HttpClient mGetUserInfoClient = new HttpClient(this, uploadImage);
        mGetUserInfoClient.imageStart();
    }


    /**
     * 检查手机号码
     *
     * @param phone
     * @return
     */
    private boolean checkPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, com.kmwlyy.login.R.string.phone_cannot_be_null, Toast.LENGTH_SHORT);
            return true;
        }

        if (!CommonUtils.checkPhone(phone)) {
            ToastUtils.show(this, com.kmwlyy.login.R.string.please_input_true_phone, Toast.LENGTH_SHORT);
            return true;
        }
        return false;
    }

    /**
     * 提交签约的dialog
     */
    private void showComfirm() {
        new MaterialDialog.Builder(this).title("确定提交签约申请吗?").positiveText(android.R.string.yes).onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                //点击确定 提交请求 成功之后跳转
                requesctorFamilyPlatform();

            }
        }).negativeText(android.R.string.no).onNegative(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        }).show();
    }

    //查询我的签约信息
    private void requesctorFamilyPlatform() {
        MySignInformBean params = getMySignInform();
        showDialog(getResources().getString(R.string.submit_wait));
        if (params != null) {
            Http_SignInform.Http_MySignInfrom event = new Http_SignInform.Http_MySignInfrom(params, new HttpListener<String>() {
                @Override
                public void onError(int code, String msg) {
                    hideDialog();
                    ToastUtils.showShort(InhabitanStartActivity.this, msg);
                    Log.d("msg", msg);
                }

                @Override
                public void onSuccess(String id) {
                    hideDialog();
                    Intent intent = new Intent(InhabitanStartActivity.this, SignComfirmActivity.class);
                    intent.putExtra("mIDNumberEdt", mIDNumberEdt.getText().toString());
                    intent.putExtra("id", id);
                    startActivity(intent);
//                    ToastUtils.showShort(InhabitanStartActivity.this, id);
//                    finish();
                }
            });
            NetService.createClient(this, HttpClient.FAMILY_URL, event).start();
        } else {
            ToastUtils.showShort(InhabitanStartActivity.this, "填写资料不完整");
            return;
        }

    }

    public MySignInformBean getMySignInform() {
        //验证码
        String mIdentifyCodeStr = mIdentifyCode.getText().toString();
        //家庭医生签约ID,修改时用
        String signatureIDStr = "";
        // 医生团队ID（甲方）
        String mFDGroupIDStr = mDoctorGroupID;
        //"医生团队所属机构ID（甲方）
        String mOrgnazitionIDStr = mOrgnazitionID;
        //签名用户姓名(乙方)
        String mSignatureUserNameStr = mSignatureUsername.getText().toString();
        //签名用户身份证(乙方)
        String mSignatureUserIDNumberStr = mIDNumberEdt.getText().toString();
        //签名图片URL
        String imageUrl = mUrlPrefix;
        //家庭健康档案号(乙方)
        String mFamilyFN = "";
        //手机号(乙方)
        String mMobile = mFamilyhealthno.getText().toString();
        //省份(乙方)mProvinceStr
        //城市(乙方)mCityStr
        //市区(乙)mCountyStr
        //乡镇或者街道(乙方)
//        String mSubdistrict = mStrweetName;
        //地址(乙方)
        String mAddressStr = mFamilyaddress.getText().toString();


        MySignInformBean params = new MySignInformBean(mIdentifyCodeStr, signatureIDStr, mFDGroupIDStr,
                mOrgnazitionIDStr, mSignatureUserNameStr, mSignatureUserIDNumberStr, imageUrl, mFamilyFN,
                mMobile, mProvinceStr, mCityStr, mCountyStr, mStrweetName, mAddressStr, mUserMemberList);
        return params;

    }

    private void requestIndentifyCode(String phone) {
        showDialog(getString(R.string.get_code));
        HttpUser.SendSmsCode sendSmsCodeEvent = new HttpUser.SendSmsCode(phone, 7, new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, "requestIndentifyCode onError , code : " + code + " , msg : " + msg);
                }
                hideDialog();
                ToastUtils.show(InhabitanStartActivity.this, com.kmwlyy.login.R.string.get_identify_code_failed, Toast.LENGTH_SHORT);
                ToastUtils.show(InhabitanStartActivity.this, msg, Toast.LENGTH_SHORT);

            }

            @Override
            public void onSuccess(String s) {
                if (DebugUtils.debug) {
                    Log.d(TAG, "requestIndentifyCode onSuccess!");
                }
                hideDialog();
                ToastUtils.show(InhabitanStartActivity.this, com.kmwlyy.login.R.string.get_identify_code_success, Toast.LENGTH_SHORT);
                mGetIdentifyCode.setEnabled(false);
                getTimer(mGetIdentifyCode, CHECKCODE_TIMEOUT * 1000, 1000).start();
            }
        });

        HttpClient mSendSmsClient = new HttpClient(this, sendSmsCodeEvent, BaseApplication.instance.getHttpFilter());
        mSendSmsClient.start();
    }

    /**
     * 获取验证码倒计时计数器
     *
     * @param btn               获取验证码按钮
     * @param millisInFuture    计数时长
     * @param countDownInterval 计数间隔时间
     * @return 计时器
     */
    public static CountDownTimer getTimer(final Button btn, long millisInFuture, long countDownInterval) {
        CountDownTimer timer = new CountDownTimer(millisInFuture, countDownInterval) {

            @Override
            public void onTick(long millisUntilFinished) {
                btn.setText((millisUntilFinished / 1000) + "秒后\n重新获取");
            }

            @Override
            public void onFinish() {
                btn.setText("获取验证码");
                btn.setEnabled(true);
            }
        };
        return timer;
    }


    /**
     * 获取家庭成员列表
     *
     * @param
     */
    private void getUserMemberList() {
        showDialog(getResources().getString(R.string.patient_loading));
        HttpUserMember.GetList getUserMemberList = new HttpUserMember.GetList(1, 5, new HttpListener<ArrayList<UserMember>>() {
            @Override
            public void onError(int code, String msg) {
                LogUtils.d(TAG, "getUserMemberList error, code : " + code + " , msg : " + msg);
                hideDialog();
            }

            @Override
            public void onSuccess(ArrayList<UserMember> userMembers) {
                hideDialog();
                if (DebugUtils.debug) {
                    Log.d(TAG, "getUserMemberList result : " + userMembers);
                }
                mUserMemberList.clear();
                //省份(乙方)mProvinceStr
                //城市(乙方)mCityStr
                //市区(乙)mCountyStr
                for (int i = 0; i < userMembers.size(); i++) {
                    if (userMembers.get(i).mRelation == 0) {
                        //获取自己的身份证
                        String idNumber = userMembers.get(i).mIDNumber;
                        if (!TextUtils.isEmpty(idNumber)) {
                            mIDNumberEdt.setText(idNumber.substring(0, 6) + "********" + idNumber.substring(14,18));
                        }
                        //获取全地址
                        String address = userMembers.get(i).mAddress;
                        if (!TextUtils.isEmpty(address)) {
                            mFamilyaddress.setText(address);
                        }
                        //获取手机号码
                        String Mobile = userMembers.get(i).mMobile;
                        if (!TextUtils.isEmpty(Mobile)) {
                            mFamilyhealthno.setText(Mobile);
                        }
                        //获取家庭成员名字
                        String MemberName = userMembers.get(i).mMemberName;
                        if (!TextUtils.isEmpty(Mobile)) {
                            mSignatureUsername.setText(MemberName);
                        }
                        //省
                        mProvinceStr = userMembers.get(i).mProvince;
                        //市
                        mCityStr = userMembers.get(i).mCity;
                        //区 县
                        mCountyStr = userMembers.get(i).mDistrict;
                        if (!TextUtils.isEmpty(mProvinceStr) && !TextUtils.isEmpty(mCityStr) && !TextUtils.isEmpty(mCountyStr)) {
                            mAreaTv.setText(mProvinceStr + mCityStr + mCountyStr);
                        }
                        //街道  镇
                        mStrweetName = userMembers.get(i).mTown;
                        if (!TextUtils.isEmpty(mStrweetName)) {
                            tvStrweetName.setText(mStrweetName);
                        }

                    } else {
                        mUserMemberList.add(new MySignInformBean.MembersBean(userMembers.get(i).mMemberName,
                                userMembers.get(i).mIDNumber, userMembers.get(i).mRelation));
                    }


                }
                setData(mUserMemberList);

            }
        });

        HttpClient mGetUserMemberListClient = new HttpClient(this, getUserMemberList);
        mGetUserMemberListClient.start();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 250) {
            dataBean = (NewChangeDoctorTeam.DataBean) data.getSerializableExtra("DataBean");
            mOrgnazitionID = data.getStringExtra("orgnazitionID");
            mDoctorGroupID = data.getStringExtra("doctorGroupID");
            String groupName = data.getStringExtra("groupName");//队长名字或者团队名字
            String leaderName = data.getStringExtra("leaderName");//团队名字
            int size = data.getIntExtra("size", 0);//团队成员数量
            String hospitalName = data.getStringExtra("hospitalName");

            if (!TextUtils.isEmpty(leaderName) && !TextUtils.isEmpty(groupName)) {
                mTextTdLl.setVisibility(View.VISIBLE);
                mDoctorteamname.setText(leaderName + "(" + size + ")");
                mDoctorteammaster.setText(groupName);
                mImageGh.setText("更换医生团队");
            }

            if (!TextUtils.isEmpty(hospitalName)) {
                mFirstparty.setVisibility(View.VISIBLE);
                mFirstparty.setText(hospitalName);
            }

        }
    }

    /**
     * 获取 省市区
     */
    public void getHttp_getRegions() {
        Http_SignInform.Http_getRegions event = new Http_SignInform.Http_getRegions(new HttpListener<List<RegionsTreeBean.DataBean>>() {
            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onSuccess(List<RegionsTreeBean.DataBean> dataBeen) {
                if (dataBeen != null) {
//                   mPickData.addAll(dataBeen);
                    initJsonData(dataBeen);

                }

            }


        });
        NetService.createClient(this, HttpClient.FAMILY_URL, event).start();
    }

    private void initJsonData(List<RegionsTreeBean.DataBean> dataBeenss) {

        List<RegionsTreeBean.DataBean> dataBeen = dataBeenss.get(0).getChildRegions();
//        List<RegionsTreeBean.DataBean.ChildRegionsBeanXX.ChildRegionsBeanX> dataBeen = dataBeens.get(0).getChildRegions();
        options1Items = dataBeen;

        for (int i = 0; i < dataBeen.size(); i++) {////遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int j = 0; j < dataBeen.get(i).getChildRegions().size(); j++) {
                String CityName = dataBeen.get(i).getChildRegions().get(j).getRegion().getRegionName();
                CityList.add(CityName);

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
//                    if (dataBeen.get(i).getChildRegions().get(j).getChildRegions() != null
//                            || dataBeen.get(i).getChildRegions().get(j).getChildRegions().size() == 0) {
//                        City_AreaList.add("");
//                    } else {
                for (int k = 0; k < dataBeen.get(i).getChildRegions().get(j).getChildRegions().size(); k++) {
                    String AreaName = dataBeen.get(i).getChildRegions().get(j).getChildRegions().get(k).getRegion().getRegionName();

                    City_AreaList.add(AreaName);//添加该城市所有地区数据

                    districtMap.put(AreaName, dataBeen.get(i).getChildRegions().get(j).getChildRegions().get(k));

                    //获取街道集合
                    List<RegionsTreeBean.DataBean> strweetList = dataBeen.get(i).getChildRegions().get(j).getChildRegions().get(k).getChildRegions();
                    for (RegionsTreeBean.DataBean strweetBean : strweetList) {
                        strweetMap.put(strweetBean.getRegion().getRegionName(), strweetBean.getRegion());
                    }
                }

                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

    }


    private List<RegionsTreeBean.DataBean> options1Items = new ArrayList<>();
    private List<ArrayList<String>> options2Items = new ArrayList<>();
    private List<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private List<String> options4Items = new ArrayList<>();
    //key: 区(县)名称;   value: 区(县)对象
    private Map<String, RegionsTreeBean.DataBean> districtMap = new HashMap<>();
    //key: 街道名称; value: 街道对象
    private Map<String, RegionsTreeBean.DataBean.RegionBean> strweetMap = new HashMap<>();

    /**
     * 省市区 dialog选择器
     */
    private void ShowPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                mProvinceStr = options1Items.get(options1).getPickerViewText();
                mCityStr = options2Items.get(options1).get(options2);
                mCountyStr = options3Items.get(options1).get(options2).get(options3);
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);

//                Toast.makeText(InhabitanStartActivity.this, tx, Toast.LENGTH_SHORT).show();
                mAreaTv.setText(tx);

                //获取 区(县) 下面的街道集合
                RegionsTreeBean.DataBean districtBean = districtMap.get(mCountyStr);
                List<RegionsTreeBean.DataBean> strweetList = districtBean.getChildRegions();
                options4Items.clear();
                for (RegionsTreeBean.DataBean strweetbean : strweetList) {
                    options4Items.add(strweetbean.getRegion().getRegionName());
                }

            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        if (options1Items.size() != 0 && options2Items.size() != 0 && options3Items.size() != 0) {
            pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
            pvOptions.show();
        } else {
            ToastUtils.showShort(InhabitanStartActivity.this, "暂时没有数据");
        }
    }

    /**
     * 街道 弹窗dialog
     */
    private void showStrweetDialog() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                mAreaTv.setText(tx);
                mStrweetName = options4Items.get(options1);
                RegionsTreeBean.DataBean.RegionBean strweetBean = strweetMap.get(mStrweetName);

                tvStrweetName.setText(mStrweetName);
            }
        })
                .setCyclic(false, false, false)//循环与否
                .setLabels("", "", "")//设置选择的三级单位
                .setTitleText("街道选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        if (options4Items.size() != 0) {
            pvOptions.setPicker(options4Items);
            pvOptions.show();
        } else {
            tvStrweetName.setText("");
            Toast.makeText(InhabitanStartActivity.this, "该区(县)暂未收录对应街道!!!", Toast.LENGTH_SHORT).show();
        }
    }

    //获取我的默认医生团队
    private void GetMyHosGroup() {
        Http_GetMyHosGroup getMyHosGroup = new Http_GetMyHosGroup(new HttpListener<GetMyHosGroupBean.DataBean>() {
            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onSuccess(GetMyHosGroupBean.DataBean dataBean) {
                //                "DoctorGroupID": "医生团队ID",
//                        "GroupName": "医生团队名称",
//                        "OrgnazitionID": "所属医院id",
//                        "HospitalName": "所属医院名称",
                Log.d("nihao", dataBean.toString());
                //所属医院id  传入到 更换医生团队界面中  orgnazitionID 是全部团队
                orgnazitionID = dataBean.getOrgnazitionID();
                mOrgnazitionID = dataBean.getOrgnazitionID();
                //医生团队ID  传入到 更换医生团队界面中   doctorGroupID 是一个团队
                doctorGroupID = dataBean.getDoctorGroupID();
                mDoctorGroupID = dataBean.getDoctorGroupID();
                //医生团队名称
                String groupName = dataBean.getGroupName();
                //所属医院名称
                String hospitalName = dataBean.getHospitalName();
                //团队人数
                List<GetMyHosGroupBean.DataBean.DoctorGroupMembersBean> doctorGroupMembers = dataBean.getDoctorGroupMembers();

                mFirstparty.setText(hospitalName);

                mDoctorteamname.setText(groupName + "(" + doctorGroupMembers.size() + ")");

                mDoctorteammaster.setText(doctorGroupMembers.get(0).getDoctorName());

//                mFirstparty.setText(dataBeen.get(0).getHospitalName());
//                mDoctorteamname.setText(dataBeen.get(0).getGroupName() + "(" + dataBeen.get(0).getDoctorGroupMembers().size() + ")");
//                mDoctorteammaster.setText(dataBeen.get(0).getGroupName());
            }


        });

        NetService.createClient(this, HttpClient.FAMILY_URL, getMyHosGroup).start();
    }

    List<String> HospitalNamelist = new ArrayList<>();

    //获取医疗机构服务区域
    private void GetOrgRegions() {
        Http_SignInform.Http_GetOrgRegions event = new Http_SignInform.Http_GetOrgRegions(new HttpListener<List<OrgRegionsBean.DataBean>>() {
            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onSuccess(List<OrgRegionsBean.DataBean> dataBeen) {
                if (dataBeen != null) {
                    for (int i = 0; i < dataBeen.size(); i++) {
                        HospitalNamelist.add(dataBeen.get(i).getHospitalName());

                    }
                }
            }

        });
        NetService.createClient(this, HttpClient.FAMILY_URL, event).start();
    }


    /**
     * 医疗机构服务区域列表
     */
    private void showOrgRegionsDialog() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mFirstparty.setText(HospitalNamelist.get(options1));
            }
        })
                .setCyclic(false, false, false)//循环与否
                .setLabels("", "", "")//设置选择的三级单位
                .setTitleText("选择医疗机构服务区域")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        if (HospitalNamelist.size() != 0) {
            pvOptions.setPicker(HospitalNamelist);
            pvOptions.show();
        } else {
            Toast.makeText(InhabitanStartActivity.this, "没有数据", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FinishEvent event) {
        finish();
    }
}
