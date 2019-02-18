package com.kmwlyy.doctor.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UploadImageResp;
import com.kmwlyy.core.net.event.HttpIM;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.core.util.Validator;
import com.kmwlyy.doctor.MyApplication;
import com.kmwlyy.doctor.NetService;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.model.VerificationInfoBean;
import com.kmwlyy.doctor.model.httpEvent.Http_getVerificationInfo_Event;
import com.kmwlyy.doctor.model.httpEvent.Http_submitVerificationInfo_Event;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.winson.ui.widget.EmptyViewUtils;

import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by xcj on 2016/10/26.
 */
public class  DoctorCertificationActivity extends BaseActivity {

    @ViewInject(R.id.navigation_icon)
    ImageView mNavigationBtn;
    @ViewInject(R.id.toolbar_title)
    TextView mToolbarTitle;
    @ViewInject(R.id.tv_save)
    TextView mSaveTxt;
    @ViewInject(R.id.et_name)
    EditText mNameEdit;
    @ViewInject(R.id.et_hospital_title)
    EditText et_hospital_title;
    @ViewInject(R.id.et_administrative_office)
    EditText et_administrative_office;
    @ViewInject(R.id.et_job_title)
    EditText et_job_title;
    @ViewInject(R.id.et_id_card_title)
    EditText et_id_card_title;
    @ViewInject(R.id.et_qualification_title)
    EditText et_qualification_title;
    @ViewInject(R.id.rl_specialty)
    LinearLayout rl_specialty;
    @ViewInject(R.id.rl_person_intro)
    LinearLayout rl_person_intro;
    @ViewInject(R.id.rl_example_photo)
    RelativeLayout rl_example_photo;
    @ViewInject(R.id.rl_first_photo)
    RelativeLayout rl_first_photo;
    @ViewInject(R.id.rl_second_photo)
    RelativeLayout rl_second_photo;
    @ViewInject(R.id.rl_third_photo)
    RelativeLayout rl_third_photo;
    @ViewInject(R.id.iv_first_photo)
    ImageView iv_first_photo;
    @ViewInject(R.id.iv_second_photo)
    ImageView iv_second_photo;
    @ViewInject(R.id.iv_third_photo)
    ImageView iv_third_photo;
    @ViewInject(R.id.et_specialty)
    EditText mSpecialtyEditText;
    @ViewInject(R.id.et_person_intro)
    EditText mPersonIntroEditText;
    @ViewInject(R.id.tv_specialty_length)
    TextView mSpecialtyLength;
    @ViewInject(R.id.tv_length)
    TextView mLength;
    @ViewInject(R.id.tv_first_photo)
    TextView mFirstPhotoTxt;
    @ViewInject(R.id.tv_second_photo)
    TextView mSecondPhotoTxt;
    @ViewInject(R.id.tv_third_photo)
    TextView mThirdPhotoTxt;

    private static final int ID_CARD_REQUEST_IMAGE = 1;
    private static final int HANDLE_ID_CARD_REQUEST_IMAGE = 2;
    private static final int PRACTISING_CERTIFICATION_REQUEST_IMAGE = 3;
    private static final String IS_ATTESTATION = "IS_ATTESTATION";
    private ArrayList<String> mSelectFirstPath = new ArrayList<>();
    private ArrayList<String> mSelectSecondPath = new ArrayList<>();
    private ArrayList<String> mSelectThirdPath = new ArrayList<>();
    private HttpClient mGetUserInfoClient;
    private ProgressDialog mLoginDialog;
    private String mFirstPhotoPath = "";
    private String mSecondPhotoPath = "";
    private String mThirdPhotoPath = "";
    private boolean mIsAttestation = false;
    private View root = null;

    public static void startDoctorCertificationActivity(Context context, boolean isAttestation) {
        Intent intent = new Intent(context, DoctorCertificationActivity.class);
        intent.putExtra(IS_ATTESTATION, isAttestation);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_certification);
        root = ((ViewGroup) findViewById(R.id.ll_root));
        ViewUtils.inject(this); //注入view和事件
        mIsAttestation = getIntent().getBooleanExtra(IS_ATTESTATION, false);
        if (mIsAttestation){
            EmptyViewUtils.showLoadingView((ViewGroup) root);
            getAttestationInfo();
        }else {
            EmptyViewUtils.removeAllView((ViewGroup) root);
            mFirstPhotoTxt.setVisibility(View.VISIBLE);
            mSecondPhotoTxt.setVisibility(View.VISIBLE);
            mThirdPhotoTxt.setVisibility(View.VISIBLE);
        }
        mNavigationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MyApplication.getApplication(v.getContext()).logout();
                SPUtils.logout(mContext);
            }
        });
        mToolbarTitle.setText(R.string.real_name_authentication);
        mSaveTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });
        rl_example_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到示例页面
                PhotoExampleActivity.startPhotoExampleActivity(DoctorCertificationActivity.this);
            }
        });
        rl_first_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //第三方选择图片
                modificationPhoto(ID_CARD_REQUEST_IMAGE, mSelectFirstPath);
            }
        });
        rl_second_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //第三方选择图片
                modificationPhoto(HANDLE_ID_CARD_REQUEST_IMAGE, mSelectSecondPath);
            }
        });
        rl_third_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //第三方选择图片
                modificationPhoto(PRACTISING_CERTIFICATION_REQUEST_IMAGE, mSelectThirdPath);
            }
        });
        mPersonIntroEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = s.length();
                mLength.setText(i+ "/300");
            }
        });
        mSpecialtyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int i = s.length();
                mSpecialtyLength.setText(i+ "/50");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ID_CARD_REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectFirstPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                if (mSelectFirstPath.size() == 0) {
                    return;
                }
                savePhoto(1,mSelectFirstPath.get(0));


            }
        }
        if (requestCode == HANDLE_ID_CARD_REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectSecondPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                if (mSelectSecondPath.size() == 0) {
                    return;
                }
                savePhoto(2,mSelectSecondPath.get(0));
            }
        }
        if (requestCode == PRACTISING_CERTIFICATION_REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectThirdPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                if (mSelectThirdPath.size() == 0) {
                    return;
                }
                savePhoto(3,mSelectThirdPath.get(0));
            }
        }
    }

    private void savePhoto(final int  i,String path) {
        showLoginDialog(getResources().getString(R.string.post_photo));
        HttpIM.UploadImage uploadImage = new HttpIM.UploadImage(path, new HttpListener<UploadImageResp>() {
            @Override
            public void onError(int code, String msg) {
                dismissLoginDialog();
                ToastUtils.showLong(DoctorCertificationActivity.this, getResources().getString(R.string.post_photo_fail));
            }

            @Override
            public void onSuccess(UploadImageResp uploadImageResp) {
                saveImageUrl(i, uploadImageResp.mFileName);
            }
        });
        mGetUserInfoClient = new HttpClient(this, uploadImage);
        mGetUserInfoClient.imageStart();
    }

    private void saveImageUrl(int i,String url){
        dismissLoginDialog();
        if (i == 1){
            mFirstPhotoPath = url;
            ImageLoader.getInstance().displayImage("file://" + mSelectFirstPath.get(0),
                    iv_first_photo,
                    getSquareDisplayOptions(R.mipmap.image_id_card_example));
        }else if (i == 2){
            mSecondPhotoPath = url;
            ImageLoader.getInstance().displayImage("file://" + mSelectSecondPath.get(0),
                    iv_second_photo,
                    getSquareDisplayOptions(R.mipmap.image_handheld_id_card_example));
        }else if(i == 3){
            mThirdPhotoPath = url;
            ImageLoader.getInstance().displayImage("file://" + mSelectThirdPath.get(0),
                    iv_third_photo,
                    getSquareDisplayOptions(R.mipmap.image_practising_certification_example));
        }

    }

    private void saveInfo(){
        String mDoctorName = mNameEdit.getText().toString().trim();
        String mHospitalName = et_hospital_title.getText().toString().trim();
        String mDepartmentName = et_administrative_office.getText().toString().trim();
        String mTitle = et_job_title.getText().toString().trim();
        String mIntro = mPersonIntroEditText.getText().toString().trim();
        String mSpecialty = mSpecialtyEditText.getText().toString().trim();
        String mIDNumber = et_id_card_title.getText().toString().trim();
        String mCertificateNo = et_qualification_title.getText().toString().trim();
        String mIDURL = mFirstPhotoPath;
        String mHandheldIDURL = mSecondPhotoPath;
        String mCertificateURL = mThirdPhotoPath;
        if (mDoctorName.length() <= 0){
            ToastUtils.showShort(this, getResources().getString(R.string.plz_input_name));
            return;
        }else if (mHospitalName.length() <= 0){
            ToastUtils.showShort(this, getResources().getString(R.string.plz_input_hospital_name));
            return;
        }else if (mDepartmentName.length() <= 0){
            ToastUtils.showShort(this, getResources().getString(R.string.plz_input_department_name));
            return;
        }else if (mTitle.length() <= 0){
            ToastUtils.showShort(this, getResources().getString(R.string.plz_input_title));
            return;
        }else if (mIDNumber.length() <= 0){
            ToastUtils.showShort(this, getResources().getString(R.string.plz_input_id_number));
            return;
        }else if (mCertificateNo.length() <= 0){
            ToastUtils.showShort(this, getResources().getString(R.string.plz_input_certificate_no));
            return;
        }else if(TextUtils.isEmpty(mSpecialty)){
            ToastUtils.showShort(this, getResources().getString(R.string.plz_input_specialty));
            return;
        }else if(TextUtils.isEmpty(mIntro)){
            ToastUtils.showShort(this, getResources().getString(R.string.plz_input_intro));
            return;
        }else if(TextUtils.isEmpty(mIDURL)){
            ToastUtils.showShort(this, getResources().getString(R.string.plz_input_post_id_url));
            return;
        }else if(TextUtils.isEmpty(mHandheldIDURL)){
            ToastUtils.showShort(this, getResources().getString(R.string.plz_input_post_handle_id_url));
            return;
        }else if(TextUtils.isEmpty(mCertificateURL)){
            ToastUtils.showShort(this, getResources().getString(R.string.plz_input_certificate_url));
            return;
        }else if(!Validator.checkCardId(mIDNumber)){
            ToastUtils.showShort(this, getResources().getString(R.string.plz_input_correct_id_number));
            return;
        }
        certificationInfo(mDoctorName,mHospitalName ,mDepartmentName ,mTitle , mIntro ,mSpecialty ,
        mIDNumber , mCertificateNo , mIDURL , mHandheldIDURL , mCertificateURL);
    }

    //认证信息
    private void certificationInfo(String mDoctorName, String mHospitalName , String mDepartmentName , String mTitle , String mIntro , String mSpecialty ,
                                   String mIDNumber , String mCertificateNo , String mIDURL , String mHandheldIDURL , String mCertificateURL){
        Http_submitVerificationInfo_Event event = new Http_submitVerificationInfo_Event(mDoctorName, mHospitalName, mDepartmentName, mTitle, mIntro, mSpecialty,
                mIDNumber, mCertificateNo, mIDURL, mHandheldIDURL, mCertificateURL, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                CertificationResultActivity.startCertificationResultActivity(DoctorCertificationActivity.this, 2);
            }

            @Override
            public void onSuccess(Object o) {
                CertificationResultActivity.startCertificationResultActivity(DoctorCertificationActivity.this, 4);
            }
        });
        HttpClient httpClient = NetService.createClient(DoctorCertificationActivity.this, event);
        httpClient.start();
    }

    private void modificationPhoto(int requestCode,ArrayList<String> mSelectPath){
        mSelectPath.clear();
        MultiImageSelector.create(DoctorCertificationActivity.this)
                .showCamera(true)
                .count(1)
                .multi()
                .origin(mSelectPath)
                .start(DoctorCertificationActivity.this, requestCode);
    }


    private void showLoginDialog(String content) {
        mLoginDialog = new ProgressDialog(this);
        mLoginDialog.setMessage(content);
        mLoginDialog.setCancelable(false);
        mLoginDialog.show();
    }

    private void dismissLoginDialog() {
        if (mLoginDialog != null) {
            mLoginDialog.dismiss();
        }
    }

    private void getAttestationInfo(){
        final Http_getVerificationInfo_Event getInfo = new Http_getVerificationInfo_Event(new HttpListener<VerificationInfoBean>() {
            @Override
            public void onError(int code, String msg) {
                EmptyViewUtils.showErrorView((ViewGroup) root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getAttestationInfo();
                    }
                });
            }

            @Override
            public void onSuccess(VerificationInfoBean verificationInfoBean) {
                mNameEdit.setText(verificationInfoBean.DoctorName);
                et_hospital_title.setText(verificationInfoBean.HospitalName);
                et_administrative_office.setText(verificationInfoBean.DepartmentName);
                et_job_title.setText(verificationInfoBean.Title);
                mPersonIntroEditText.setText(Html.fromHtml(verificationInfoBean.Intro));
                mSpecialtyLength.setText(verificationInfoBean.Specialty.length()+ "/50");
                mLength.setText(verificationInfoBean.Intro.length()+ "/300");
                mSpecialtyEditText.setText(Html.fromHtml(verificationInfoBean.Specialty));
                et_id_card_title.setText(verificationInfoBean.IDNumber);
                et_qualification_title.setText(verificationInfoBean.CertificateNo);
                ImageLoader.getInstance().displayImage(verificationInfoBean.UrlPrefix + verificationInfoBean.IDURL, iv_first_photo, getSquareDisplayOptions(R.mipmap.image_id_card_example));
                mFirstPhotoPath = verificationInfoBean.IDURL;
                ImageLoader.getInstance().displayImage(verificationInfoBean.UrlPrefix + verificationInfoBean.HandheldIDURL, iv_second_photo, getSquareDisplayOptions(R.mipmap.image_handheld_id_card_example));
                mSecondPhotoPath = verificationInfoBean.HandheldIDURL;
                ImageLoader.getInstance().displayImage(verificationInfoBean.UrlPrefix + verificationInfoBean.CertificateURL, iv_third_photo, getSquareDisplayOptions(R.mipmap.image_practising_certification_example));
                mThirdPhotoPath = verificationInfoBean.CertificateURL;
                mFirstPhotoTxt.setVisibility(View.GONE);
                mSecondPhotoTxt.setVisibility(View.GONE);
                mThirdPhotoTxt.setVisibility(View.GONE);
                EmptyViewUtils.removeAllView((ViewGroup) root);
            }

        });
        HttpClient httpClient = NetService.createClient(DoctorCertificationActivity.this, getInfo);
        httpClient.start();
    }


    public static DisplayImageOptions getSquareDisplayOptions(int path) {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.default_avatar) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(path) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(path) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new SimpleBitmapDisplayer()) // 设置成圆角图片
                .build(); // 构建完成
        return options;
    }
}
