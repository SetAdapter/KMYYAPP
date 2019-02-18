package com.kmwlyy.patient.module.addfamilymember;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.NewHttpClient;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.module.InhabitantStart.FamilyPersonBean;
import com.kmwlyy.patient.module.InhabitantStart.Http_MyFamily;
import com.kmwlyy.patient.weight.DatePickDialogUtil;
import com.kmwlyy.patient.weight.RelationshpmeDialog;
import com.kmwlyy.patient.weight.TitleChose;
import com.kmwlyy.patient.weight.TitleEditText;

import butterknife.BindView;

/**
 * Created by ljf on 2017/7/31.
 * 添加家人
 */

public class AddFamilyMemberActivity extends BaseActivity {

    TextView title;
    Button saveBt;
    @BindView(R.id.iv_tools_left)
    Button mIvToolsLeft;

    @BindView(R.id.tv_title_center)
    TextView mTvTitleCenter;
    @BindView(R.id.name_edt)
    TitleEditText mNameEdt;
    @BindView(R.id.phone_edt)
    TitleEditText mPhoneEdt;
    //    @BindView(R.id.code)
//    TextView mCode;
//    @BindView(R.id.get_code)
//    ImageView mGetCode;
//    @BindView(R.id.address_tv)
//    TitleChose mAddressTv;


    @BindView(R.id.health_insurance_number)
    TitleEditText mHealthInsuranceNumber;

    @BindView(R.id.relationshpme_tv)
    TitleChose mRelationshpmeTv;
    @BindView(R.id.IDNumber_edt)
    TitleEditText mIDNumberEdt;
    @BindView(R.id.iv_tools_right)
    Button mIvToolsRight;

    @BindView(R.id.confirm_btn)
    Button mConfirmBtn;
    @BindView(R.id.textView10)
    TextView mTextView10;
    @BindView(R.id.text_data)
    TextView mTextData;
    @BindView(R.id.set_calendar)
    LinearLayout mSetCalendar;
    @BindView(R.id.gender_tv)
    TitleChose mGenderTv;
    private String BirthDay;
    private String mTemp_gender;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_addfamily_member;
    }

    @Override
    protected void afterBindView() {
        initView();

    }

    private void initData(String residentid, String nameStr, String phoneStr, String genderStr, String BirthDay,
                          String relationStr, String IDNumberStr) {
        Http_MyFamily.Http_FamilyInform event = new Http_MyFamily.Http_FamilyInform("2", nameStr, phoneStr, mTemp_gender,
                BirthDay, "舅叔",
                IDNumberStr, "普通用户", new HttpListener<FamilyPersonBean>() {
            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onSuccess(FamilyPersonBean personBean) {
                if (null != personBean) {
                    finish();
                }
            }
        });
        NewHttpClient newHttpClient = new NewHttpClient(AddFamilyMemberActivity.this, event);
        newHttpClient.start();
    }

    private void initView() {
        mTvTitleCenter.setText("添加信息");

        mIvToolsLeft.setOnClickListener(this);

        mGenderTv.setOnClickListener(this);
        mRelationshpmeTv.setOnClickListener(this);
        mConfirmBtn.setOnClickListener(this);
        mSetCalendar.setOnClickListener(this);
        BirthDay = mTextData.getText().toString();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_tools_left: //返回
                finish();
                break;
            case R.id.confirm_btn: //保存
                String nameStr = mNameEdt.getContent();
                String phoneStr = mPhoneEdt.getContent();
//                String genderStr = (String) mGenderTv.getTag();
                String genderStr = mGenderTv.getHint();
                switch (genderStr) {
                    case "男":
                        mTemp_gender = "1";
                        break;
                    case "女":
                        mTemp_gender = "2";
                        break;
                    default:
                        mTemp_gender = "未知";
                        break;
                }
                //BirthDay
                String relationStr = mRelationshpmeTv.getHint();
                String IDNumberStr = mIDNumberEdt.getContent();
                initData("2", nameStr, phoneStr, mTemp_gender, BirthDay, relationStr, IDNumberStr);
                break;
//            case R.id.address_tv: //居民地址
//                ToastUtils.showShort(mContext,"居民地址");
//                final AddressEdtDialog dialog =new AddressEdtDialog(AddFamilyMemberActivity.this);
//                dialog.setOnYesOrNoListener(new OnYesOrNoListener() {
//                    @Override
//                    public void onCancel() {
//                        dialog.dismiss();
//                    }
//
//                    @Override
//                    public void onConfirm(String edtStr) {
//                        mAddressTv.setHint(edtStr);
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//                break;
            case R.id.gender_tv: //性别
                new MaterialDialog.Builder(this).title("请选择性别").positiveColor(Color.BLACK).positiveText("男").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mGenderTv.setHint("男");
                        dialog.dismiss();
                    }
                }).negativeColor(Color.BLACK).negativeText("女").onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        mGenderTv.setHint("女");
                        dialog.dismiss();
                    }
                }).show();

//                final GenderDialog genderDialog = new GenderDialog();
//                genderDialog.setOnYesOrNoListener(new OnYesOrNoListener() {
//                    @Override
//                    public void onCancel() {
//                        genderDialog.dismiss();
//                    }
//
//                    @Override
//                    public void onConfirm(String edtStr) {
//                        mGenderTv.setHint(edtStr);
//
//                        genderDialog.dismiss();
//                    }
//                });
//                genderDialog.show(getFragmentManager(), "GenderDialog");
                break;
            case R.id.set_calendar: //出生日期
                DatePickDialogUtil dateTimePicKDialog = new DatePickDialogUtil(this, BirthDay);
                dateTimePicKDialog.dateTimePickDialog(this, mTextData, null);
                break;
            case R.id.relationshpme_tv: //与我的关系
                RelationshpmeDialog relationshpmeDialog = new RelationshpmeDialog(AddFamilyMemberActivity.this);
                relationshpmeDialog.setBackTypeNameListenre(new RelationshpmeDialog.BackTypeNameListenre() {
                    @Override
                    public void refreshBackTypeNameUI(String string) {
                        mRelationshpmeTv.setHint(string);

                    }
                });
                relationshpmeDialog.show();
                break;
        }
    }


}
