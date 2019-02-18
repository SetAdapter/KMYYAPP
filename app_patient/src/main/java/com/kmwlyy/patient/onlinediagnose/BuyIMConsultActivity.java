package com.kmwlyy.patient.onlinediagnose;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UserMember;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.bean.CheckUserPackageBean;
import com.kmwlyy.patient.helper.net.event.MemberPackages;
import com.kmwlyy.patient.myservice.MyConsultActivity;
import com.kmwlyy.patient.pay.PayActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.UploadImageResp;
import com.kmwlyy.patient.helper.net.bean.UserConsult;
import com.kmwlyy.patient.helper.net.event.HttpIM;
import com.kmwlyy.patient.helper.net.event.HttpUserConsults;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.registry.net.NetEvent;
import com.kmwlyy.usermember.UserMemberSelectActivity;
import com.ta.utdid2.android.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by Winson on 2016/8/18.
 */
public class BuyIMConsultActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = BuyIMConsultActivity.class.getSimpleName();

    public static final String DOCTOR_INFO = "DOCTOR_INFO";
    public static final String CONSULT_TYPE = "CONSULT_TYPE";
    public static final String MONEY = "MONEY";
    //    public static final String USER_MEMBER_INFO = "USER_MEMBER_INFO";
//    public static final int SELECT_USER_MEMBER_REQ = 10001;
    private ProgressDialog mOnCreateConsultDialog;
    private static final int REQUEST_IMAGE = 2;



    public static void startBuyIMConsultActivity(Context context, String doctorId, int consultType, String money) {
        Intent intent = new Intent(context, BuyIMConsultActivity.class);
        intent.putExtra(DOCTOR_INFO, doctorId);
        intent.putExtra(CONSULT_TYPE, consultType);
        intent.putExtra(MONEY, money);
        context.startActivity(intent);
    }

    @BindView(R.id.user_member_info)
    TextView mUserMemberInfo;
    @BindView(R.id.select_user)
    LinearLayout mSelectUser;
    @BindView(R.id.submit)
    Button mSubmit;
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.consult_content)
    EditText mConsultContent;
    @BindView(R.id.grid_upload_pictures)
    GridView mGridView;
    @BindView(R.id.ll_root)
    LinearLayout mRoot;
    @BindView(R.id.tv_length)
    TextView mEdtLength;
    @BindView(R.id.iv_has_one)
    ImageView mHasOneIv;
    @BindView(R.id.ll_family_doctor)
    LinearLayout mFamilyDoctorLinearLayout;
    @BindView(R.id.ll_user_package)
    LinearLayout mUserPackageLinearLayout;
    @BindView(R.id.iv_family_doctor_line)
    ImageView mFamilyDoctorLine;
    @BindView(R.id.iv_user_package_line)
    ImageView mUserPackageLine;
    @BindView(R.id.tv_service_money)
    TextView mServiceMoneyTxt;

    private String mDoctorID;
    private UserMember mUserMember;
    private int mConsultType;
    public String mImagePath;
    private String name;
    private String mobile;
    private String idNumber;
    private String id;
    private ArrayList<String> mSelectPath = new ArrayList<>();
    private String mMoneyValue;

    /**
     * 需要上传的图片路径  控制默认图片在最后面需要用LinkedList
     */
    private LinkedList<String> dataList = new LinkedList<String>();
    /**
     * 图片上传Adapter
     */
    private UploadImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_im_consult);
        butterknife.ButterKnife.bind(this);

        tv_center.setText(R.string.im_consult);

        mSubmit.setOnClickListener(this);
        mSelectUser.setOnClickListener(this);
        mFamilyDoctorLinearLayout.setOnClickListener(this);
        mUserPackageLinearLayout.setOnClickListener(this);


        mConsultType = getIntent().getIntExtra(CONSULT_TYPE, HttpUserConsults.PAY);
        mDoctorID = getIntent().getStringExtra(DOCTOR_INFO);
        mMoneyValue = getIntent().getStringExtra(MONEY);
        if (mConsultType == HttpUserConsults.FREE) {
            setBarTitle(R.string.free_consult);
            mServiceMoneyTxt.setText(getString(R.string.duty_service));
        } else if (mConsultType == HttpUserConsults.DUTY) {
            setBarTitle(R.string.duty_consult);
            mServiceMoneyTxt.setText(getString(R.string.duty_service));
        }else{
            getUserPackages();
            mServiceMoneyTxt.setText(getString(R.string.service_account)+mMoneyValue);
        }
        mSubmit.setEnabled(false);
        mSubmit.setBackgroundResource(R.drawable.unable_btn);
        getMemberDefault();
        mHasOneIv.setVisibility(View.GONE);
        mFamilyDoctorLinearLayout.setVisibility(View.GONE);
        mUserPackageLinearLayout.setVisibility(View.GONE);
        mFamilyDoctorLine.setVisibility(View.GONE);
        mUserPackageLine.setVisibility(View.GONE);
        mConsultContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 10) {
                    mSubmit.setEnabled(true);
                    mSubmit.setBackgroundResource(R.drawable.yellow_solid_btn_normal);
                } else {
                    mSubmit.setEnabled(false);
                    mSubmit.setBackgroundResource(R.drawable.unable_btn);
                }
                int i = s.length();
                mEdtLength.setText(i + "/300");
            }
        });

        dataList.addLast(null);// 初始化第一个添加按钮数据
        adapter = new UploadImageAdapter(this, dataList);
        mGridView.setAdapter(adapter);
        mGridView.setOnItemClickListener(mItemClick);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.select_user:
                selectUserMember();
                break;
            case R.id.submit:
                buyConsultService();
                break;
            case R.id.ll_user_package:
                if (mUserPackageLinearLayout.isSelected()){
                    mUserPackageLinearLayout.setSelected(false);
                }else{
                    mUserPackageLinearLayout.setSelected(true);
                }
                mFamilyDoctorLinearLayout.setSelected(false);
                break;
            case R.id.ll_family_doctor:
                if (mFamilyDoctorLinearLayout.isSelected()){
                    mFamilyDoctorLinearLayout.setSelected(false);
                }else{
                    mFamilyDoctorLinearLayout.setSelected(true);
                }
                mUserPackageLinearLayout.setSelected(false);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void duty(EventApi.Duty duty) {
        finish();
    }

    private void buyConsultService() {
        if (StringUtils.isEmpty(id)) {
            ToastUtils.show(this, R.string.please_select_usermember, Toast.LENGTH_SHORT);
            return;
        }
       final int type =  getType();
        showCreateConsultDialog();
        if (!dataList.isEmpty() && dataList.get(0) != null) {
            final ArrayList<HttpUserConsults.ImageFile> imageFileList = new ArrayList<>();
            final int length = getLength();
            final Point point = new Point();
            for (final String path : dataList) {
                if (TextUtils.isEmpty(path)){
                    continue;
                }
                HttpIM.UploadImage uploadImage = new HttpIM.UploadImage(path, new HttpListener<UploadImageResp>() {
                    @Override
                    public void onError(int code, String msg) {
                        LogUtils.d(TAG, "upload image error : " + path);
                        point.x++;
                        if (point.x >= length) {
                            // complete
                            createRecordAndPay(imageFileList,type);
                        }
                    }

                    @Override
                    public void onSuccess(UploadImageResp data) {
                        point.x++;

                        HttpUserConsults.ImageFile imageFile = new HttpUserConsults.ImageFile();
                        imageFile.mFileUrl = data.mFileName;
                        imageFile.mFileType = 0;
                        imageFile.mFileName = data.mFileName;
                        imageFile.mRemark = data.mFileName;
                        imageFileList.add(imageFile);

                        if (point.x >= length) {
                            // complete
                            createRecordAndPay(imageFileList,type);
                        }
                    }
                });

                HttpClient uploadImageClient = NetService.createClient(this, uploadImage);
//                uploadImageClient.start();
                uploadImageClient.imageStart();

            }
        } else {
            createRecordAndPay(null,type);
        }
    }

    private void createRecordAndPay(List<HttpUserConsults.ImageFile> imageFiles, final int type) {

        String consultContent = mConsultContent.getText().toString();
        boolean free = false;
        if (mConsultType == HttpUserConsults.FREE) {
            free = true;
        }
        if (mFamilyDoctorLinearLayout.isSelected()){
            mConsultType = HttpUserConsults.FAMILY_DOCTOR;
        }else if (mUserPackageLinearLayout.isSelected()){
            mConsultType = HttpUserConsults.MEAL;
        }
        HttpUserConsults.Add addUserConsult = new HttpUserConsults.Add(
                id,
                mDoctorID,
                imageFiles,
                consultContent,
                mConsultType, free,
                new HttpListener<UserConsult.AddResp>() {
                    @Override
                    public void onError(int code, String msg) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("addUserConsult", code, msg));
                        }
                        if (mConsultType == HttpUserConsults.FREE) {
                            ToastUtils.show(BuyIMConsultActivity.this, R.string.create_im_consult_free_failed, Toast.LENGTH_SHORT);
                        } else if (mConsultType == HttpUserConsults.DUTY) {
                            ToastUtils.show(BuyIMConsultActivity.this, R.string.create_im_consult_duty_failed, Toast.LENGTH_SHORT);
                        } else {
                            ToastUtils.show(BuyIMConsultActivity.this, R.string.create_im_consult_record_failed, Toast.LENGTH_SHORT);
                        }
                        if (msg != null) {
                            try {
                                JSONObject data = new JSONObject(msg);
                                ToastUtils.show(BuyIMConsultActivity.this, data.getString("ErrorInfo"), Toast.LENGTH_SHORT);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        dismissCreateConsultDialog();
                    }

                    @Override
                    public void onSuccess(UserConsult.AddResp data) {
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("addUserConsult", PUtils.toJson(data)));
                        }
                        if (!"Success".equals(data.mActionStatus)){
                            dismissCreateConsultDialog();
                            ToastUtils.show(BuyIMConsultActivity.this, data.mErrorInfo, Toast.LENGTH_SHORT);
                            return;
                        }
                        if (mConsultType == HttpUserConsults.FREE) {
                            ToastUtils.show(BuyIMConsultActivity.this, R.string.create_im_consult_free_success, Toast.LENGTH_SHORT);
                        } else if (mConsultType == HttpUserConsults.DUTY) {
                            ToastUtils.show(BuyIMConsultActivity.this, R.string.create_im_consult_duty_success, Toast.LENGTH_SHORT);
                        } else {
                            ToastUtils.show(BuyIMConsultActivity.this, R.string.create_im_consult_record_success, Toast.LENGTH_SHORT);
                        }
                        dismissCreateConsultDialog();

                        if (data == null || TextUtils.isEmpty(data.mOrderNO)) {
                            PUtils.goToMyService(BuyIMConsultActivity.this, 0, EventApi.MainTabSelect.MY_COSULT, false);
                            EventBus.getDefault().post(new EventApi.BuyVVConsultSuc());
                            finish();
                        } else {
                            PayActivity.startPayActivity(BuyIMConsultActivity.this, data.mOrderNO,"0", type, PayActivity.BUY_IM, true
                                    , mConsultType != HttpUserConsults.DUTY);
                        }

                    }
                }
        );
        addUserConsult.mUseErrorData = true;
        HttpClient client = NetService.createClient(this, addUserConsult);
        client.start();

    }

    private void showCreateConsultDialog() {
        mOnCreateConsultDialog = new ProgressDialog(this);
        mOnCreateConsultDialog.setMessage(getResources().getString(R.string.on_create_im_consult_record));
        mOnCreateConsultDialog.setCancelable(false);
        mOnCreateConsultDialog.show();
    }

    private void dismissCreateConsultDialog() {
        if (mOnCreateConsultDialog != null) {
            mOnCreateConsultDialog.dismiss();
        }
    }

    private void selectUserMember() {
//        CommonUtils.startActivity(this, SelectUserMemberActivity.class);
        Intent intent = new Intent(this, UserMemberSelectActivity.class);
        startActivityForResult(intent, UserMemberSelectActivity.SELECT_USER_MEMBER_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UserMemberSelectActivity.SELECT_USER_MEMBER_REQ && data != null) {
            mUserMember = (UserMember) data.getSerializableExtra(UserMemberSelectActivity.USER_MEMBER_INFO);
            id = mUserMember.mMemberID;
            name = mUserMember.mMemberName;
            mobile = mUserMember.mMobile;
            idNumber = mUserMember.mIDNumber;
            updateUserMemberInfo();
        }
//        if (requestCode == SELECT_IMAGE_RESULT_CODE && resultCode == RESULT_OK) {
//            String imagePath = "";
//            if (data != null && data.getData() != null) {//有数据返回直接使用返回的图片地址
//                imagePath = ImageUtils.getFilePathByFileUri(this, data.getData());
//            } else {//无数据使用指定的图片路径
//                imagePath = mImagePath;
//            }
//            dataList.add(dataList.size() - 1, imagePath);
//            //dataList.addFirst(imagePath);
//            adapter.update(dataList); // 刷新图片
//        }
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                if (mSelectPath.size() == 0) {
                    return;
                }
                dataList.removeAll(dataList);
                dataList.addLast(null);
                for (String p : mSelectPath) {
                    if (dataList.size() == 4) {
                        continue;
                    }
                    dataList.add(dataList.size() - 1, p);
                }
                adapter.update(dataList); // 刷新图片

            }
        }
    }

    private void updateUserMemberInfo() {
        mUserMemberInfo.setText(mUserMember.mMemberName);
    }

    /**
     * 上传图片GridView Item单击监听
     */
    private AdapterView.OnItemClickListener mItemClick = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (mSelectPath != null || mSelectPath.size() != 0){
                mSelectPath.clear();
                int i = adapter.getCount();
                for(int j=0;j< i;j++){
                    String path =  (String)adapter.getItem(j);
                    if (!TextUtils.isEmpty(path)) {
                        mSelectPath.add(path);
                    }
                }
            }
            MultiImageSelector.create(BuyIMConsultActivity.this)
                    .showCamera(true)
                    .count(3)
                    .multi()
                    .origin(mSelectPath)
                    .start(BuyIMConsultActivity.this, REQUEST_IMAGE);

        }
    };

    private void getMemberDefault() {//获取默认成员
        NetEvent.GetMemberDefault event = new NetEvent.GetMemberDefault();
        event.setHttpListener(new HttpListener<ArrayList<UserMember>>() {
            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onSuccess(ArrayList<UserMember> userMembers) {
                if (userMembers != null) {
                    id = userMembers.get(0).mMemberID;
                    name = userMembers.get(0).mMemberName;
                    mobile = userMembers.get(0).mMobile;
                    idNumber = userMembers.get(0).mIDNumber;

                } else {
                    id = "";
                    name = "";
                    mobile = "";
                    idNumber = "";
                }
                if (!StringUtils.isEmpty(id)) {
                    mUserMemberInfo.setText(name);
                }
            }
        });
        new HttpClient(BuyIMConsultActivity.this, event).start();
    }

    private void getUserPackages(){
        MemberPackages.CheckUserPackage checkUserPackage = new MemberPackages.CheckUserPackage(mDoctorID, 1, new HttpListener<CheckUserPackageBean>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(BuyIMConsultActivity.this, getString(R.string.check_user_package_fail));
            }

            @Override
            public void onSuccess(CheckUserPackageBean checkUserPackageBean) {
                updateUi(checkUserPackageBean.mHasFamilyDoctor, checkUserPackageBean.mHasUserPackage);
            }
        });
        new HttpClient(BuyIMConsultActivity.this, checkUserPackage).start();
    }

    private void updateUi(boolean hasFamily, boolean hasUserPackage){
        if (!hasFamily && !hasUserPackage){
            mHasOneIv.setVisibility(View.GONE);
            mFamilyDoctorLinearLayout.setVisibility(View.GONE);
            mUserPackageLinearLayout.setVisibility(View.GONE);
            mFamilyDoctorLine.setVisibility(View.GONE);
            mUserPackageLine.setVisibility(View.GONE);
        }else if(hasFamily && hasUserPackage){
            mHasOneIv.setVisibility(View.VISIBLE);
            mFamilyDoctorLinearLayout.setVisibility(View.VISIBLE);
            mUserPackageLinearLayout.setVisibility(View.VISIBLE);
            mFamilyDoctorLine.setVisibility(View.VISIBLE);
            mUserPackageLine.setVisibility(View.VISIBLE);
        }else if (hasFamily && !hasUserPackage){
            mHasOneIv.setVisibility(View.VISIBLE);
            mFamilyDoctorLinearLayout.setVisibility(View.VISIBLE);
            mUserPackageLinearLayout.setVisibility(View.GONE);
            mFamilyDoctorLine.setVisibility(View.VISIBLE);
            mUserPackageLine.setVisibility(View.GONE);
        }else{
            mHasOneIv.setVisibility(View.VISIBLE);
            mFamilyDoctorLinearLayout.setVisibility(View.GONE);
            mUserPackageLinearLayout.setVisibility(View.VISIBLE);
            mFamilyDoctorLine.setVisibility(View.GONE);
            mUserPackageLine.setVisibility(View.VISIBLE);
        }
    }

    private int getType(){
        int i = 0;
        if (mFamilyDoctorLinearLayout.isSelected()){
            i = 5;
        }else if (mUserPackageLinearLayout.isSelected()){
            i = 3;
        }
        return  i;
    }

    private int getLength(){
        if (dataList.getLast() == null){
            return  dataList.size()-1;
        }else{
            return dataList.size();
        }
    }
}
