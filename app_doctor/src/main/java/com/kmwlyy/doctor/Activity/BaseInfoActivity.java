package com.kmwlyy.doctor.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UploadImageResp;
import com.kmwlyy.core.net.event.HttpIM;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.core.util.Validator;
import com.kmwlyy.doctor.R;
import com.kmwlyy.doctor.Utils.AppEventApi;

import com.kmwlyy.doctor.model.SingedMemberBean;
import com.kmwlyy.doctor.model.httpEvent.BaseInfoEvent;
import com.kmwlyy.personinfo.PersonInfoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.SelectPicPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.qqtheme.framework.picker.OptionPicker;
import me.nereo.multi_image_selector.MultiImageSelector;

/**
 * Created by TFeng on 2017/7/4.
 */

public class BaseInfoActivity extends BaseActivity {
    private static final String BASE_INFO_ACTIVITY = "BaseInfoActivity";
    private static final int NEW_FILE = 0;
    private static final int EXITS_FILE = 1;
    private static final int REQUEST_IMAGE = 2;

    private static final int SINGED_MEMBER = 10;
    private static final int HOME_MEMBER = 11;
    private ArrayList<String> mSelectPath = new ArrayList<>();
    private File mPhotoFile;
    private String mPhotoPath;
    public final static int CAMERA_RESULT = 8888;
    private HttpClient mGetUserInfoClient;


    @ViewInject(R.id.tv_left)
    TextView tv_left;
    @ViewInject(R.id.iv_left)
    ImageView iv_left;
    @ViewInject(R.id.tv_right)
    TextView tv_right;
    @ViewInject(R.id.tv_center)
    TextView tv_center;


    @ViewInject(R.id.ll_info_head)
    LinearLayout ll_info_head;
    @ViewInject(R.id.ll_info_name_icon)
    LinearLayout ll_info_name_icon;
    @ViewInject(R.id.iv_info_head_icon)
    ImageView iv_info_head_icon;

    @ViewInject(R.id.et_id_card)
    EditText et_id_card;
    @ViewInject(R.id.tv_id_card_title)
    TextView tv_id_card_title;
    @ViewInject(R.id.et_phone_number)
    EditText et_phone_number;
    @ViewInject(R.id.et_address)
    EditText et_address;

    @ViewInject(R.id.et_info_remark)
    EditText et_info_remark;
    @ViewInject(R.id.tv_info_gender)
    TextView tv_info_gender;
    @ViewInject(R.id.tv_info_age)
    TextView tv_info_age;
    @ViewInject(R.id.tv_gender_title)
    TextView tv_gender_title;
    @ViewInject(R.id.tv_age_title)
    TextView tv_age_title;
    @ViewInject(R.id.et_info_name)
    EditText et_info_name;
    @ViewInject(R.id.tv_info_relation)
    TextView tv_info_relation;

    @ViewInject(R.id.ll_info_relation)
    LinearLayout ll_info_relation;


    private Intent mIntent;
    private int mBaseInfoAcitvity;
    private int mIntExtra;
    private SingedMemberBean mSingedMemberInfo;
    private String mMobile;
    private String mAddress;
    private String mRemark;
    private int mAge;
    private int mGender;
    private ProgressDialog mLoginDialog;
    private SingedMemberBean.UserMemberBean mHomeMemberInfo;
    private int mMember;
    private String mFamilyDoctorID;
    private boolean mCheckCardId;
    private Map<String, Integer> infoMap = new HashMap<>();
    private String mIDnumber;
    private String mMemberName;
    private String mMemberID;
    private int mRelation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_info);
        ViewUtils.inject(this);
        getIntentInfo();
        initView();
        initListener();
    }

    private void getIntentInfo() {
        mIntExtra = getIntent().getIntExtra(BASE_INFO_ACTIVITY, 0);
        mMember = getIntent().getIntExtra("Member", 10);
        mFamilyDoctorID = getIntent().getStringExtra("FamilyDoctorID");
        mSingedMemberInfo = (SingedMemberBean) getIntent().getSerializableExtra("singedMemberInfo");
        mHomeMemberInfo = (SingedMemberBean.UserMemberBean) getIntent().getSerializableExtra("homeMemberInfo");
    }

    private void initView() {
        tv_left.setVisibility(View.GONE);
        iv_left.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(R.string.commit);

        if (mIntExtra == NEW_FILE) {

            tv_center.setText(R.string.health_file);
            setIDnumber();
            et_phone_number.setHint("请输入手机号");
            et_phone_number.setHintTextColor(getResources().getColor(R.color.color_hint_string));
            et_address.setHint("请输入住址");
            et_address.setHintTextColor(getResources().getColor(R.color.color_hint_string));
            et_info_name.setHint("请输入姓名");
            et_info_name.setHintTextColor(getResources().getColor(R.color.color_hint_string));
            ll_info_relation.setVisibility(View.VISIBLE);

        } else {

            if (mMember == SINGED_MEMBER) {
//                修改签约居民信息
                setSingedMember(mSingedMemberInfo);
            } else {
//                修改家庭成员信息
                setHomeMember(mHomeMemberInfo);
            }

        }

    }

    private void setIDnumber() {
        et_id_card.setHint("请输入身份证");
        et_id_card.setHintTextColor(getResources().getColor(R.color.color_hint_string));
        et_id_card.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    mIDnumber = et_id_card.getText().toString().trim();
                    if (Validator.checkCardId(mIDnumber)) {

                        if (mIDnumber.length() == 18) {
                            try {
                                infoMap = Validator.getCarInfo(mIDnumber);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (mIDnumber.length() == 15) {
                            try {
                                infoMap = Validator.getCarInfo15W(mIDnumber);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        mGender = infoMap.get("sex");
                        mAge = infoMap.get("age");
                        tv_info_gender.setText(mGender == 0 ? "男" : "女");
                        tv_info_age.setText(mAge + "");
                    } else {
                        ToastUtils.showShort(mContext, "身份证不合法");
                    }
                }

            }

        });
    }

    private void setHomeMember(SingedMemberBean.UserMemberBean homeMemberInfo) {
        mMobile = homeMemberInfo.getMobile();
        mAddress = homeMemberInfo.getAddress();
        mAge = homeMemberInfo.getAge();
        mGender = homeMemberInfo.getGender();
        mIDnumber = homeMemberInfo.getIDNumber();
        mRemark = homeMemberInfo.getRemark();


        tv_center.setText("基础信息");
        ll_info_head.setVisibility(View.GONE);
        iv_info_head_icon.setEnabled(false);
        et_info_name.setText(homeMemberInfo.getMemberName());
        ImageLoader.getInstance().displayImage(homeMemberInfo.getPhotoUrl(),
                iv_info_head_icon, PersonInfoActivity.getCircleDisplayOptions(R.drawable.default_avatar_patient));
        ll_info_relation.setVisibility(View.VISIBLE);

        switch (homeMemberInfo.getRelation()) {
            case 0:
                tv_info_relation.setText("自己");
                break;
            case 1:
                tv_info_relation.setText("配偶");
                break;
            case 2:
                tv_info_relation.setText("父亲");
                break;
            case 3:
                tv_info_relation.setText("母亲");
                break;
            case 4:
                tv_info_relation.setText("儿子");
                break;
            case 5:
                tv_info_relation.setText("女儿");
                break;
            case 6:
                tv_info_relation.setText("其他");
                break;
        }
        if (homeMemberInfo.getIDNumber() != null && homeMemberInfo.getIDNumber().length() > 0) {
            et_id_card.setEnabled(false);
            et_id_card.setText(homeMemberInfo.getIDNumber());
        } else {
            et_id_card.setEnabled(true);
            setIDnumber();
        }


        tv_id_card_title.setTextColor(getResources().getColor(R.color.color_hint_string));
        tv_info_gender.setText(mGender == 0 ? "男" : "女");
        tv_gender_title.setTextColor(getResources().getColor(R.color.color_hint_string));
        tv_info_gender.setTextColor(getResources().getColor(R.color.color_hint_string));
        tv_info_age.setText(mAge + "");
        tv_info_age.setTextColor(getResources().getColor(R.color.color_hint_string));
        tv_age_title.setTextColor(getResources().getColor(R.color.color_hint_string));

//            mRemark = mSingedMemberInfo.getUserMember().getRemark();

        if (mMobile == null || mMobile.equals("") || mMobile.length() == 0 ) {
            et_phone_number.setHint("请输入手机号码");
            et_phone_number.setHintTextColor(getResources().getColor(R.color.color_hint_string));
        } else {
            et_phone_number.setText(mMobile + "");
        }

        if ( mAddress==null ||mAddress.equals("") || mAddress.length() == 0) {
            et_address.setHint("请输入地址");
            et_address.setHintTextColor(getResources().getColor(R.color.color_hint_string));
        } else {
            et_address.setText(mAddress);
        }
//备注
        if (mRemark == null || mRemark.equals("") || mRemark.length() == 0) {
            et_info_remark.setHint("请输入备注");
            et_info_remark.setHintTextColor(getResources().getColor(R.color.color_hint_string));
        } else {
            et_info_remark.setText(mRemark);
        }


    }

    private void setSingedMember(SingedMemberBean singedMember) {

        mMobile = singedMember.getUserMember().getMobile();
        mAddress = singedMember.getUserMember().getAddress();
        mAge = singedMember.getUserMember().getAge();
        mGender = singedMember.getUserMember().getGender();
        mIDnumber = singedMember.getUserMember().getIDNumber();
        mRemark = singedMember.getUserMember().getRemark();

        tv_center.setText("基础信息");
        ll_info_head.setVisibility(View.GONE);
        iv_info_head_icon.setEnabled(false);
        et_info_name.setText(singedMember.getUserMember().getMemberName());
        ImageLoader.getInstance().displayImage(singedMember.getUserMember().getPhotoUrl(),
                iv_info_head_icon, PersonInfoActivity.getCircleDisplayOptions(R.drawable.default_avatar_patient));
        if (mIDnumber != null && mIDnumber.length() > 0) {
            et_id_card.setEnabled(false);
            et_id_card.setText(mIDnumber);
        } else {
            et_id_card.setEnabled(true);
            setIDnumber();
        }

        tv_id_card_title.setTextColor(getResources().getColor(R.color.color_hint_string));
        tv_info_gender.setText(mGender == 0 ? "男" : "女");
        tv_gender_title.setTextColor(getResources().getColor(R.color.color_hint_string));
        tv_info_gender.setTextColor(getResources().getColor(R.color.color_hint_string));
        tv_info_age.setText(mAge + "");
        tv_info_age.setTextColor(getResources().getColor(R.color.color_hint_string));
        tv_age_title.setTextColor(getResources().getColor(R.color.color_hint_string));

        ll_info_relation.setVisibility(View.VISIBLE);
        tv_info_relation.setEnabled(false);
        switch (singedMember.getUserMember().getRelation()) {
            case 0:
                tv_info_relation.setText("自己");
                break;
            case 1:
                tv_info_relation.setText("配偶");
                break;
            case 2:
                tv_info_relation.setText("父亲");
                break;
            case 3:
                tv_info_relation.setText("母亲");
                break;
            case 4:
                tv_info_relation.setText("儿子");
                break;
            case 5:
                tv_info_relation.setText("女儿");
                break;
            case 6:
                tv_info_relation.setText("其他");
                break;
        }

//            mRemark = mSingedMemberInfo.getUserMember().getRemark();

        if (mMobile == null || mMobile.equals("") || mMobile.length() == 0) {
            et_phone_number.setHint("请输入手机号码");
            et_phone_number.setHintTextColor(getResources().getColor(R.color.color_hint_string));
        } else {
            et_phone_number.setText(mMobile + "");
        }

        if (mAddress == null || mAddress.equals("") || mAddress.length() == 0) {
            et_address.setHint("请输入地址");
            et_address.setHintTextColor(getResources().getColor(R.color.color_hint_string));
        } else {
            et_address.setText(mAddress);
        }
//备注
        if (mRemark == null || mRemark.equals("") || mRemark.length() == 0) {
            et_info_remark.setHint("请输入备注");
            et_info_remark.setHintTextColor(getResources().getColor(R.color.color_hint_string));
        } else {
            et_info_remark.setText(mRemark);
        }
    }


    private void initListener() {
        iv_left.setOnClickListener(this);
        tv_info_relation.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        iv_info_head_icon.setOnClickListener(this);

    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.iv_info_head_icon:
                ToastUtils.showShort(mContext, "添加头像");
                openPictureDialog();

                break;
            case R.id.tv_info_relation:
                showRelationPick();
                break;
            case R.id.tv_right:
                check_upload();

                break;


        }
    }

    private void check_upload() {
        if (TextUtils.isEmpty(et_info_name.getText().toString().trim())) {
            ToastUtils.showShort(mContext, "请输入姓名");
            return;
        } else {
            mMemberName = et_info_name.getText().toString().trim();
        }
        if (TextUtils.isEmpty(et_id_card.getText().toString().trim())) {
            ToastUtils.showShort(mContext, "请输入身份证");
            return;
        } else {
            mIDnumber = et_id_card.getText().toString().trim();
        }
        if (TextUtils.isEmpty(et_phone_number.getText().toString().trim())) {
            ToastUtils.showShort(mContext, "请输入手机号");
            return;
        } else {
            if (!Validator.isMobile(et_phone_number.getText().toString().trim())) {
                ToastUtils.showShort(mContext, "请输入正确的手机号");
                return;
            } else {
                mMobile = et_phone_number.getText().toString().trim();
            }
        }
        String relation = tv_info_relation.getText().toString().trim();

        if (relation.equals("自己")) {
            mRelation = 0;
        } else if (relation.equals("配偶")) {
            mRelation = 1;
        } else if (relation.equals("父亲")) {
            mRelation = 2;
        } else if (relation.equals("母亲")) {
            mRelation = 3;
        } else if (relation.equals("儿子")) {
            mRelation = 4;
        } else if (relation.equals("女儿")) {
            mRelation = 5;
        } else if (relation.equals("其他")){
            mRelation = 6;
        }
        mAddress = et_address.getText().toString().trim();
        mMobile = et_phone_number.getText().toString().trim();
        mRemark = et_info_remark.getText().toString().trim();
        if (mIntExtra == NEW_FILE) {
            mMemberID = "";
        } else {
            String gender = tv_info_gender.getText().toString().trim();
            if (gender.equals("男")) {
                mGender = 0;
            } else {
                mGender = 1;
            }
            if (mMember == SINGED_MEMBER) {
                mMemberID = mSingedMemberInfo.getUserMember().getMemberID();
            } else {
                mMemberID = mHomeMemberInfo.getMemberID();
            }
        }
        showLoadDialog(R.string.string_save);
        BaseInfoEvent.ModifyInfo event = new BaseInfoEvent.ModifyInfo(mFamilyDoctorID, mMemberID, mMemberName, mIDnumber, mGender + "", mMobile, mAddress, mRemark, mRelation + "", new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
                ToastUtils.showShort(mContext,msg);
            }

            @Override
            public void onSuccess(Object o) {
                dismissLoadDialog();
                JSONObject jsonObject = null;
                try{
                    jsonObject = new JSONObject(o.toString());
                    int status = jsonObject.optInt("Status");

                    if(status == 500){
                        ToastUtils.showShort(mContext,"超过最大人数限制，无法添加");
                        return;
                    }else if(status == 520){
                        ToastUtils.showShort(mContext,"拒绝修改，只能有一个关系为“本人”的就诊人");
                        return;
                    } else{
                        String memberID = jsonObject.optString("Data");
                        if(mIntExtra == NEW_FILE){
                            Intent newMember = new Intent();
                            newMember.putExtra("MemberID",memberID);
                            newMember.putExtra("MemberName",mMemberName);
                            newMember.putExtra("FamilyDoctorID",mFamilyDoctorID);
                            newMember.setClass(mContext,UploadFileActivity.class);
                            startActivity(newMember);
                            EventBus.getDefault().post(new AppEventApi.Add());
                            finish();
                        }else{
                            if(mMember == SINGED_MEMBER){
                                modifySingedMember(mSingedMemberInfo);
                                EventBus.getDefault().post(mSingedMemberInfo);
                                ToastUtils.showShort(mContext,"修改成功");

                            }else{
                                modifyHomeMember(mHomeMemberInfo);
                                EventBus.getDefault().post(mHomeMemberInfo);
                                ToastUtils.showShort(mContext,"修改成功");
                            }
                            finish();
                        }

                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }





            }
        });
        new HttpClient(mContext, event).startNewApi();

    }

    private void modifyHomeMember(SingedMemberBean.UserMemberBean homeMemberInfo) {
        homeMemberInfo.setAge(mAge);
        homeMemberInfo.setMobile(mMobile);
        homeMemberInfo.setRemark(mRemark);
        homeMemberInfo.setMemberName(mMemberName);
        homeMemberInfo.setGender(mGender);
        homeMemberInfo.setAddress(mAddress);
        homeMemberInfo.setRelation(mRelation);
    }

    private void modifySingedMember(SingedMemberBean singedMember) {
        singedMember.getUserMember().setMemberName(mMemberName);
        singedMember.getUserMember().setRemark(mRemark);
        singedMember.getUserMember().setAge(mAge);
        singedMember.getUserMember().setGender(mGender);
        singedMember.getUserMember().setMobile(mMobile);
        singedMember.getUserMember().setAddress(mAddress);
        singedMember.getUserMember().setRelation(mRelation);

    }


    private void openPictureDialog() {
        final SelectPicPopupWindow menuWindow = new SelectPicPopupWindow(BaseInfoActivity.this);
        //显示窗口
        menuWindow.showAtLocation(BaseInfoActivity.this.findViewById(R.id.main_view), Gravity.BOTTOM, 0, 0); //设置layout在PopupWindow中显示的位置
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

    private void doTakePhoto() {
        try {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            mPhotoPath = getSDPath() + "/" + getPhotoFileName();
            mPhotoFile = new File(mPhotoPath);
            if (!mPhotoFile.exists()) {
                mPhotoFile.createNewFile();
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
            startActivityForResult(intent, CAMERA_RESULT);
        } catch (Exception e) {
        }
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    private void modificationHeadPortrait() {
        mSelectPath.clear();
        MultiImageSelector.create(BaseInfoActivity.this)
                .showCamera(false)
                .count(1)
                .multi()
                .origin(mSelectPath)
                .start(BaseInfoActivity.this, REQUEST_IMAGE);
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
        if (requestCode == CAMERA_RESULT) {
            if (resultCode == RESULT_OK) {
                saveHeadPortrait(mPhotoPath);
            }
        }
    }

    private void saveHeadPortrait(final String path) {
        showLoginDialog(getResources().getString(com.kmwlyy.personinfo.R.string.save_head_photo));
        HttpIM.UploadImage uploadImage = new HttpIM.UploadImage(path, new HttpListener<UploadImageResp>() {
            @Override
            public void onError(int code, String msg) {
                dismissLoginDialog();
                ToastUtils.showLong(BaseInfoActivity.this, getResources().getString(com.kmwlyy.personinfo.R.string.post_fail));
            }

            @Override
            public void onSuccess(UploadImageResp uploadImageResp) {
                dismissLoginDialog();
                // TODO: 2017/7/8 保存图片地址
                ImageLoader.getInstance().displayImage("file://" + path,
                        iv_info_head_icon, PersonInfoActivity.getCircleDisplayOptions(com.kmwlyy.personinfo.R.drawable.default_avatar_patient));

            }
        });
        mGetUserInfoClient = new HttpClient(this, uploadImage);
        mGetUserInfoClient.imageStart();
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

    private void showRelationPick() {
        String[] option = {"自己", "配偶", "父亲", "母亲", "儿子", "女儿", "其他"};
        OptionPicker picker = new OptionPicker(this, option);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                tv_info_relation.setText(option);
            }
        });
        picker.show();
    }
}
