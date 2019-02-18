package com.kmwlyy.personinfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.DoctorInfo;
import com.kmwlyy.core.net.bean.PatientInfo;
import com.kmwlyy.core.net.bean.UploadImageResp;
import com.kmwlyy.core.net.event.HttpIM;
import com.kmwlyy.core.net.event.HttpUserInfo;
import com.kmwlyy.core.util.CircleDisplayer;
import com.kmwlyy.core.util.ToastUtils;
import com.networkbench.agent.impl.NBSAppAgent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.RateLayout;
import com.winson.ui.widget.SelectPicPopupWindow;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import me.nereo.multi_image_selector.MultiImageSelector;


/**
 * Created by xcj on 2016/10/19.
 */
public class PersonInfoActivity extends Activity {
    private static final String TAG = PersonInfoActivity.class.getSimpleName();

    private static final String IS_PATIENT = "IS_PATIENT";
    private static final String USER_ID = "USER_ID";
    private static final String PHOTO_URL = "PHOTO_URL";
    private static final String NICK_NAME = "NICK_NAME";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final int REQUEST_IMAGE = 2;
    private File mPhotoFile;
    private String mPhotoPath;
    public final static int CAMERA_RESULT=8888;

    private HttpClient mGetUserInfoClient;

    @BindView(R2.id.tv_center)
    TextView mToolbarTitle;
    @BindView(R2.id.ll_head_portrait)
    LinearLayout mHeadPortrait;
    @BindView(R2.id.ll_nickname)
    LinearLayout mNickName;
    @BindView(R2.id.ll_phone_number)
    LinearLayout mPhoneNumber;
    @BindView(R2.id.ll_be_good_at_disease)
    LinearLayout mGoodDisease;
    @BindView(R2.id.ll_individual_resume)
    LinearLayout mIndividualResume;
    @BindView(R2.id.doctor_info)
    LinearLayout mDoctorInfo;
    @BindView(R2.id.tv_good_at_disease)
    TextView mGoodAtDisease;
    @BindView(R2.id.tv_individual_resume)
    TextView mIndividualResumeTxt;
    @BindView(R2.id.iv_head_portrait)
    ImageView mHeadPortraitImage;
    @BindView(R2.id.tv_name_title)
    TextView mNameTitle;
    @BindView(R2.id.tv_name)
    TextView mNameTxt;
    @BindView(R2.id.tv_phone_number)
    TextView mPhoneNumberTxt;
    @BindView(R2.id.iv_arrow)
    ImageView mArrowImage;

    private boolean is_patient;
    private String user_id;
    private View root = null;
    private ArrayList<String> mSelectPath = new ArrayList<>();
    private ProgressDialog mLoginDialog;

    public static void startPersonInfoActivity(Context context, boolean isPatient, String userId, String photoUrl, String nickname, String phone) {
        Intent intent = new Intent(context, PersonInfoActivity.class);
        intent.putExtra(IS_PATIENT, isPatient);
        intent.putExtra(USER_ID,userId);
        intent.putExtra(PHOTO_URL, photoUrl);
        intent.putExtra(NICK_NAME, nickname);
        intent.putExtra(PHONE_NUMBER, phone);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_person_data);
        butterknife.ButterKnife.bind(this);
        root = ((ViewGroup) findViewById(R.id.ll_root));
        EmptyViewUtils.showLoadingView((ViewGroup) root);
        is_patient = getIntent().getBooleanExtra(IS_PATIENT, false);
        if (is_patient){
            mDoctorInfo.setVisibility(View.GONE);
            mIndividualResume.setEnabled(false);
            mGoodDisease.setEnabled(false);
            mNickName.setEnabled(false);
            mNameTitle.setText(R.string.nickname);
            mArrowImage.setVisibility(View.INVISIBLE);
            String photoUrl = getIntent().getStringExtra(PHOTO_URL);
            String nickName = getIntent().getStringExtra(NICK_NAME);
            String phoneNumber = getIntent().getStringExtra(PHONE_NUMBER);
            mNameTxt.setText(nickName);
            mPhoneNumberTxt.setText(phoneNumber);
            ImageLoader.getInstance().displayImage(photoUrl, mHeadPortraitImage, getCircleDisplayOptions(R.drawable.default_avatar_patient));
            EmptyViewUtils.removeAllView((ViewGroup) root);
            //getPatientInfo();
        }else{
            mDoctorInfo.setVisibility(View.VISIBLE);
            mIndividualResume.setEnabled(true);
            mGoodDisease.setEnabled(true);
            mNickName.setEnabled(false);
            mNameTitle.setText(R.string.name);
            mArrowImage.setVisibility(View.INVISIBLE);
            user_id = getIntent().getStringExtra(USER_ID);
            getDoctorInfo(user_id);
        }
        mToolbarTitle.setText(R.string.person_data);
        findViewById(R.id.tv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity();
            }
        });
        findViewById(R.id.ll_head_portrait).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NBSAppAgent.onEvent("个人中心-修改头像");
                //修改头像
                openPictureDialog();
            }
        });
        findViewById(R.id.ll_nickname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改昵称
                modificationNickname();

            }
        });
        findViewById(R.id.ll_be_good_at_disease).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NBSAppAgent.onEvent("个人中心-修改擅长疾病");
                //擅长疾病
                goodAtDisease();

            }
        });
        findViewById(R.id.ll_individual_resume).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NBSAppAgent.onEvent("个人中心-修改个人简介");
                //个人简介
                individualResume();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                if (mSelectPath.size() == 0) {
                    return;
                }
                saveHeadPortrait(mSelectPath.get(0));
            }
        }
        if (requestCode==CAMERA_RESULT) {
            if (resultCode == RESULT_OK) {
                saveHeadPortrait(mPhotoPath);
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setNickName(EventApi.NickName nickName) {
        mNameTxt.setText(nickName.nickname);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setmGoodAtDisease(EventApi.GoodAtDisease disease) {
        mGoodAtDisease.setText(disease.goodAtDisease);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setIntro(EventApi.Intro intro) {
        mIndividualResumeTxt.setText(intro.introInfo);
    }

    //修改头像
    private void modificationHeadPortrait(){
        mSelectPath.clear();
        MultiImageSelector.create(PersonInfoActivity.this)
                .showCamera(false)
                .count(1)
                .multi()
                .origin(mSelectPath)
                .start(PersonInfoActivity.this, REQUEST_IMAGE);
    }

    //修改昵称
    private void modificationNickname(){
        ModificationInfoActivity.startModificationInfoActivity(this, 1, mNameTxt.getText().toString());
    }

    //擅长疾病
    private void goodAtDisease(){
        ModificationInfoActivity.startModificationInfoActivity(this, 2, mGoodAtDisease.getText().toString());
    }

    //个人简介
    private void individualResume(){
        ModificationInfoActivity.startModificationInfoActivity(this, 3, mIndividualResumeTxt.getText().toString());
    }

    //获取患者个人资料
    private void getPatientInfo(){
        HttpUserInfo.GetPatientInfo getPatientInfo = new HttpUserInfo.GetPatientInfo(new HttpListener<PatientInfo>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(PersonInfoActivity.this, getResources().getString(R.string.get_patient_info_fail));
                EmptyViewUtils.showErrorView((ViewGroup) root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPatientInfo();
                    }
                });
            }

            @Override
            public void onSuccess(PatientInfo patientInfo) {
                mNameTxt.setText(patientInfo.mUserCNName);
                mPhoneNumberTxt.setText(patientInfo.mMobile);
                ImageLoader.getInstance().displayImage(patientInfo.mPhotoUrl, mHeadPortraitImage, getCircleDisplayOptions(R.drawable.default_avatar_patient));
                EmptyViewUtils.removeAllView((ViewGroup) root);

                //把头像保存本地
                EventApi.HeadPhoto headPhoto = new EventApi.HeadPhoto();
                headPhoto.photoUrl = patientInfo.mPhotoUrl;
                EventBus.getDefault().post(headPhoto);
            }
        });

        mGetUserInfoClient = new HttpClient(this, getPatientInfo);
        mGetUserInfoClient.start();
    }

    //获取医生个人资料
    private void getDoctorInfo(String userId){
        HttpUserInfo.GetDoctorInfo getDoctorInfo = new HttpUserInfo.GetDoctorInfo("UserID",userId, new HttpListener<DoctorInfo>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(PersonInfoActivity.this, getResources().getString(R.string.get_doctor_info_fail));
                EmptyViewUtils.showErrorView((ViewGroup) root, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDoctorInfo(user_id);
                    }
                });
            }

            @Override
            public void onSuccess(DoctorInfo doctorInfo) {
                mNameTxt.setText(doctorInfo.mDoctorName);
                mPhoneNumberTxt.setText(doctorInfo.mUser.mMobile);
                mGoodAtDisease.setText(Html.fromHtml(doctorInfo.mSpecialty));
                mIndividualResumeTxt.setText(Html.fromHtml(doctorInfo.mIntro));
                ImageLoader.getInstance().displayImage(doctorInfo.mUser.mPhotoUrl, mHeadPortraitImage, getCircleDisplayOptions(R.drawable.default_avatar_patient));
                EmptyViewUtils.removeAllView((ViewGroup) root);
            }
        });

        mGetUserInfoClient = new HttpClient(this, getDoctorInfo);
        mGetUserInfoClient.start();
    }

    private void saveHeadPortrait(final String path){
        showLoginDialog(getResources().getString(R.string.save_head_photo));
        HttpIM.UploadImage uploadImage = new HttpIM.UploadImage(path, new HttpListener<UploadImageResp>() {
            @Override
            public void onError(int code, String msg) {
                dismissLoginDialog();
                ToastUtils.showLong(PersonInfoActivity.this,getResources().getString(R.string.post_fail));
            }

            @Override
            public void onSuccess(UploadImageResp uploadImageResp) {
                saveImageUrl(path, uploadImageResp.mFileName);
            }
        });
        mGetUserInfoClient = new HttpClient(this, uploadImage);
        mGetUserInfoClient.imageStart();
    }

    private void saveImageUrl(final String path, String url){
        if (is_patient){
            HttpUserInfo.PostPatientInfo postPatientInfo = new HttpUserInfo.PostPatientInfo("PhotoUrl", url, new HttpListener() {
                @Override
                public void onError(int code, String msg) {
                    dismissLoginDialog();
                    ToastUtils.showShort(PersonInfoActivity.this, getResources().getString(R.string.save_fail));
                }

                @Override
                public void onSuccess(Object o) {
                    dismissLoginDialog();
                    ImageLoader.getInstance().displayImage("file://" + path,
                            mHeadPortraitImage,
                            getCircleDisplayOptions(R.drawable.default_avatar_patient));
                }
            });

            mGetUserInfoClient = new HttpClient(this, postPatientInfo);
            mGetUserInfoClient.start();
        }else{
            HttpUserInfo.PostDoctorInfo postDoctorInfo = new HttpUserInfo.PostDoctorInfo("PhotoUrl", url, new HttpListener() {
                @Override
                public void onError(int code, String msg) {
                    dismissLoginDialog();
                    ToastUtils.showShort(PersonInfoActivity.this, getResources().getString(R.string.save_fail));
                }

                @Override
                public void onSuccess(Object o) {
                    dismissLoginDialog();
                    ImageLoader.getInstance().displayImage("file://" + path,
                            mHeadPortraitImage,
                            getCircleDisplayOptions(R.drawable.default_avatar_patient));
                }
            });

            mGetUserInfoClient = new HttpClient(this, postDoctorInfo);
            mGetUserInfoClient.start();
        }
    }

    public static DisplayImageOptions getCircleDisplayOptions(int defalultRes) {
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defalultRes) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(defalultRes) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(defalultRes) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new CircleDisplayer(10)) // 设置成圆角图片
                .build(); // 构建完成
        return options;
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

    private void openPictureDialog(){
        final SelectPicPopupWindow menuWindow = new SelectPicPopupWindow(PersonInfoActivity.this);
        //显示窗口
        menuWindow.showAtLocation(PersonInfoActivity.this.findViewById(R.id.main_view), Gravity.BOTTOM, 0, 0); //设置layout在PopupWindow中显示的位置
        menuWindow.setSelectTxt(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificationHeadPortrait();
                menuWindow.dismiss();
            }
        });
        menuWindow.setTakeTxt(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTakePhoto();
                menuWindow.dismiss();
            }
        });

    }
    //拍照
    private void doTakePhoto() {
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            mPhotoPath =  getSDPath() + "/" + getPhotoFileName();
            mPhotoFile = new File(mPhotoPath);
            if (!mPhotoFile.exists()) {
                mPhotoFile.createNewFile();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
            startActivityForResult(intent,CAMERA_RESULT);
        } catch (Exception e) {
        }
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    public String getSDPath(){
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            //do something...
            finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //关闭前保存头像图片
    public void finishActivity(){
        //返回
        if (mSelectPath.size() >= 1) {
            EventApi.HeadPhoto headPhoto = new EventApi.HeadPhoto();
            headPhoto.photoUrl = "file://"+ mSelectPath.get(0);
            EventBus.getDefault().post(headPhoto);

        }
      /*  if (is_patient){
            EventApi.NickName nickName = new EventApi.NickName();
            nickName.nickname = mNameTxt.getText().toString();
            EventBus.getDefault().post(nickName);
        }*/
        onBackPressed();
    }

}
