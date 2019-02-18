package com.kmwlyy.patient.onlinediagnose;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.evaluate.EvaluateForDoctorActivity;
import com.kmwlyy.patient.evaluate.EvaluateListActivity;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.helper.net.bean.Doctor;
import com.kmwlyy.patient.helper.net.bean.EvaluateListBean;
import com.kmwlyy.patient.helper.net.event.FollowDoctorEvent;
import com.kmwlyy.patient.helper.net.event.HttpDoctor;
import com.kmwlyy.patient.helper.net.event.HttpDoctorService;
import com.kmwlyy.patient.helper.net.event.HttpUserConsults;
import com.kmwlyy.patient.helper.net.event.HttpUserOPDRegisters;
import com.kmwlyy.patient.helper.utils.EventApi;
import com.kmwlyy.patient.helper.utils.LibUtils;
import com.kmwlyy.patient.helper.utils.PUtils;
import com.kmwlyy.personinfo.PersonInfoActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.winson.ui.widget.CommonAdapter;
import com.winson.ui.widget.EmptyViewUtils;
import com.winson.ui.widget.LineCheckTextView;
import com.winson.ui.widget.MeasureListView;
import com.winson.ui.widget.RateLayout;
import com.winson.ui.widget.TagBaseAdapter;
import com.winson.ui.widget.TagCloudLayout;
import com.winson.ui.widget.ToastMananger;
import com.winson.ui.widget.ViewHolder;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import doctor.kmwlyy.com.recipe.Utils.MyUtils;

/**
 * Created by Winson on 2016/8/17.
 */
public class DoctorServiceActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = DoctorServiceActivity.class.getSimpleName();

    private static final String DOCTOR_ITEM = "DOCTOR_ITEM";
    private static final String IGNORE_SERVICE_CHECK = "IGNORE_SERVICE_CHECK";
    private HttpClient mEvaluateClient;
    private List<EvaluateListBean> evaluateList;
    private EvaluateListAdapter evaluateAdapter;

    public static void startDoctorServiceActivity(Context context, Doctor.ListItem doctorItem) {
        Intent intent = new Intent(context, DoctorServiceActivity.class);

        intent.putExtra(DOCTOR_ITEM, doctorItem);

        context.startActivity(intent);
    }

    public static void startDoctorServiceActivity(Context context, Doctor.ListItem doctorItem, boolean ignoreServiceCheck) {
        Intent intent = new Intent(context, DoctorServiceActivity.class);

        intent.putExtra(DOCTOR_ITEM, doctorItem);
        intent.putExtra(IGNORE_SERVICE_CHECK, ignoreServiceCheck);

        context.startActivity(intent);
    }
    @BindView(R.id.doctor_avatar)
    ImageView mDoctorAvatar;
    @BindView(R.id.doctor_name)
    TextView mDoctorName;
    @BindView(R.id.department)
    TextView mDepartment;
    @BindView(R.id.hospital_name)
    TextView mHospitalName;

    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.doctor_title)
    TextView mDoctorTitle;
    @BindView(R.id.ll_buy_image_word_consult)
    LinearLayout mBuyWordConsultLinearLayout;
    @BindView(R.id.ll_buy_home_doctor)
    LinearLayout mBuyHomeDoctorLinearLayout;
    @BindView(R.id.ll_video_consultation)
    LinearLayout mBuyVideoConsultLinearLayout;
    @BindView(R.id.ll_phonetic_consulting)
    LinearLayout mBuyPhoneConsultLinearLayout;
    @BindView(R.id.tv_image_txt_money)
    TextView mImageTxtMoney;
    @BindView(R.id.tv_video_money)
    TextView mVideoMoney;
    @BindView(R.id.tv_voice_money)
    TextView mVoiceMoney;
    @BindView(R.id.tv_doctor_money)
    TextView mDoctorMoney;
    @BindView(R.id.ll_evaluate)
    LinearLayout ll_evaluate;
    @BindView(R.id.describtion)
    LineCheckTextView describtion;
    @BindView(R.id.display)
    TextView display;
  /*  @BindView(R.id.intro_describtion)
    LineCheckTextView intro_describtion;
    @BindView(R.id.intro_display)
    TextView intro_display;*/
    @BindView(R.id.tv_diagnose_and_follow_num)
    TextView mFollowNum;
    @BindView(R.id.tv_follow)
    TextView tv_follow;
    @BindView(R.id.lv_evaluate)
    MeasureListView lv_evaluate;
    @BindView(R.id.intro_describtions)
    TextView mIntroTxt;


    private Doctor.ListItem mDoctorItem;
    private Doctor.Detail mDoctorDetail;
    public String mCurrentDate, mOldDate;

    private boolean mIsFollow = false;


    private int mOPDType = HttpUserOPDRegisters.OPDTYPE_VIDEO;
    private boolean mFamilyDoctorServiceOn;
    private boolean mVideoServiceOn;
    private boolean mIMServiceOn;
    private boolean mImageServiceOn;
    private boolean mIgnoreServiceCheck;
    private ProgressDialog mLoadScheduleDialog;
    private View root = null;
    private String mVideoMoneyValue = "0";
    private String mVioceMoneyValue = "0";
    private String mImageMoneyValue = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_service);
        butterknife.ButterKnife.bind(this);
        root = ((ViewGroup) findViewById(R.id.ll_root));
//        EmptyViewUtils.showLoadingView((ViewGroup) root);
        tv_center.setText(R.string.doctor_service);
        mDoctorItem = (Doctor.ListItem) getIntent().getSerializableExtra(DOCTOR_ITEM);
        mIgnoreServiceCheck = getIntent().getBooleanExtra(IGNORE_SERVICE_CHECK, false);
        tv_left.setOnClickListener(this);
        mBuyWordConsultLinearLayout.setOnClickListener(this);
        mBuyHomeDoctorLinearLayout.setOnClickListener(this);
        mBuyVideoConsultLinearLayout.setOnClickListener(this);
        mBuyPhoneConsultLinearLayout.setOnClickListener(this);
        tv_follow.setOnClickListener(this);
        ll_evaluate.setOnClickListener(this);

        //评论列表
        evaluateList = new ArrayList<>();
        evaluateAdapter = new EvaluateListAdapter(DoctorServiceActivity.this, evaluateList);
        lv_evaluate.setAdapter(evaluateAdapter);
        getDoctorDetailInfo();
        loadEvaluateInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBuySuc(EventApi.BuyVVConsultSuc event){
        finish();
    }

    private void updateDoctorInfo() {
        if (mDoctorDetail.mDoctorServices != null) {
            for (Doctor.DoctorService service : mDoctorDetail.mDoctorServices) {
                switch (service.mServiceType) {
                    case Doctor.DoctorService.IMAGE_CONSULT:
                        mImageServiceOn = service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON;
                        if (service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON) {
                            mImageTxtMoney.setText("¥"+service.mServicePrice+getString(R.string.service_unit));
                            mImageMoneyValue = service.mServicePrice+"";
                        }else{
                            mImageTxtMoney.setText(getString(R.string.service_not_available));
                        }
                        break;
                    case Doctor.DoctorService.IM_CONSULT:
                        mIMServiceOn = service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON;
                        if (service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON) {
                            mVoiceMoney.setText("¥"+service.mServicePrice+getString(R.string.service_unit));
                            mVioceMoneyValue = service.mServicePrice+"";
                        }else{
                            mVoiceMoney.setText(getString(R.string.service_not_available));
                        }
                        break;
                    case Doctor.DoctorService.VIDEO_CONSULT:
                        mVideoServiceOn = service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON;
                        if (service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON) {
                            mVideoMoney.setText("¥"+service.mServicePrice+getString(R.string.service_unit));
                            mVideoMoneyValue = service.mServicePrice+"";
                        }else{
                            mVideoMoney.setText(getString(R.string.service_not_available));
                        }
                        break;
                    case Doctor.DoctorService.FAMILY_DOCTOR_CONSULT:
                        mFamilyDoctorServiceOn = service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON;
                        if (service.mServiceSwitch == Doctor.DoctorService.SERVICE_ON) {
                            mDoctorMoney.setText("¥"+service.mServicePrice+getString(R.string.doctor_service_unit));
                        }else{
                            mDoctorMoney.setText(getString(R.string.service_not_available));
                        }
                        break;
                }
            }
        }

        mHospitalName.setText(mDoctorDetail.mHospitalName);
        mDepartment.setText(mDoctorDetail.mDepartmentName);
        mDoctorName.setText(mDoctorDetail.mDoctorName);
        mDoctorTitle.setText(PUtils.getDoctorTitle(DoctorServiceActivity.this,mDoctorDetail.mTitle));
        mIsFollow = mDoctorDetail.mIsFollowed;
        if (mDoctorDetail.mIsFollowed) {
            tv_follow.setBackgroundResource(R.drawable.btn_unfollow);
            tv_follow.setText(R.string.already_follow);
            tv_follow.setTextColor(getResources().getColor(R.color.unable));
        }
        else{
            tv_follow.setBackgroundResource(R.drawable.btn_follow);
            tv_follow.setText(R.string.follow);
            tv_follow.setTextColor(getResources().getColor(R.color.color_main_green));
        }
        mFollowNum.setText(getString(R.string.seeing_count)+"  "+mDoctorDetail.mDiagnoseNum+" | "+getString(R.string.fans)+"  "+mDoctorDetail.mFollowNum);

        if (mDoctorDetail.mUser != null) {
            ImageLoader.getInstance().displayImage(mDoctorDetail.mUser.mPhotoUrl, mDoctorAvatar, LibUtils.getCircleDisplayOptions(R.drawable.default_avatar_doctor));
        }

        describtion.setText(mDoctorDetail.mSpecialty);
        if(!TextUtils.isEmpty(mDoctorDetail.mIntro)){
            mIntroTxt.setText(Html.fromHtml(mDoctorDetail.mIntro));
        }else{
            mIntroTxt.setText("");
        }


        describtion.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                describtion.getViewTreeObserver().removeOnPreDrawListener(this);

                if (describtion.checkIsOver()) {
                    if (describtion.isShowAll()) {
                        display.setText(R.string.pack_up);
                        Drawable drawable = DoctorServiceActivity.this.getResources().getDrawable(R.drawable.coll);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        display.setCompoundDrawables(null, null, drawable, null);
                    } else {
                        display.setText(R.string.display);
                        Drawable drawable = DoctorServiceActivity.this.getResources().getDrawable(R.drawable.display);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        display.setCompoundDrawables(null, null, drawable, null);
                    }
                    display.setVisibility(View.VISIBLE);
                } else {
                    display.setVisibility(View.GONE);
                }

                return false;
            }
        });

        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (describtion.isShowAll()) {
                    display.setText(R.string.display);
                    Drawable drawable = DoctorServiceActivity.this.getResources().getDrawable(R.drawable.display);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    display.setCompoundDrawables(null, null, drawable, null);
                    describtion.hiddenPart();
                } else {
                    display.setText(R.string.pack_up);
                    Drawable drawable = DoctorServiceActivity.this.getResources().getDrawable(R.drawable.coll);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    display.setCompoundDrawables(null, null, drawable, null);
                    describtion.showAll();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_left:
                onBackPressed();
                break;
            case R.id.ll_buy_image_word_consult:
                //图文咨询
                if (!PUtils.checkHaveUser(this)) {
                    return;
                }
                if (!mIgnoreServiceCheck && !mImageServiceOn) {
                    ToastMananger.showToast(this, R.string.image_service_not_open, Toast.LENGTH_SHORT);
                    return;
                }
                BuyIMConsultActivity.startBuyIMConsultActivity(this, mDoctorDetail.mDoctorID, HttpUserConsults.PAY, mImageMoneyValue);
                break;
            case R.id.ll_buy_home_doctor:
                //家庭医生
                if (!PUtils.checkHaveUser(this)) {
                    return;
                }
                if (!mIgnoreServiceCheck && !mFamilyDoctorServiceOn) {
                    ToastMananger.showToast(this, R.string.family_service_not_open, Toast.LENGTH_SHORT);
                    return;
                }
                BuyFamilyDoctorActivity.startBuyHomeDoctorActivity(this, mDoctorDetail);
                break;
            case R.id.ll_video_consultation:
                if (!PUtils.checkHaveUser(this)) {
                    return;
                }
                if (!mVideoServiceOn) {
                    ToastMananger.showToast(this, R.string.video_service_not_open, Toast.LENGTH_SHORT);
                    return;
                }
                //BuyVVConsultActivity.startBuyVVConsultActivity(this,mDoctorDetail.mDoctorID,1,mVideoMoneyValue,null);
                BuyVideoConsultActivity.startBuyVideoConsultActivity(this,mDoctorDetail.mDoctorID,1,0,mVideoMoneyValue);
                //视频咨询
                break;
            case R.id.ll_phonetic_consulting:
                //语音咨询
                if (!PUtils.checkHaveUser(this)) {
                    return;
                }
                if (!mIMServiceOn) {
                    ToastMananger.showToast(this, R.string.im_service_not_open, Toast.LENGTH_SHORT);
                    return;
                }
                //BuyVVConsultActivity.startBuyVVConsultActivity(this,mDoctorItem.mDoctorID,0,mVioceMoneyValue);
                BuyVideoConsultActivity.startBuyVideoConsultActivity(this,mDoctorDetail.mDoctorID,0,0,mVioceMoneyValue);
                break;
            case R.id.ll_evaluate://评价列表
                if (!PUtils.checkHaveUser(this)) {
                    return;
                }
                EvaluateListActivity.startEvaluateListActivity(this,mDoctorItem.mDoctorID);
//                EvaluateForDoctorActivity.startEvaluateForDoctorActivity(this,"28bd4fda7f374f8fac9988d869aa4494",mDoctorItem);  //测试评论功能注释一下
                break;
            case R.id.tv_follow:
                changeFollow(mDoctorDetail.mDoctorID, mIsFollow);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 请求评价数据
     */
    public void loadEvaluateInfo(){
        HttpDoctorService.GetEvaluate getEvaluate = new HttpDoctorService.GetEvaluate(mDoctorItem.mDoctorID,"1", new HttpListener<ArrayList<EvaluateListBean>>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getDoctorList", code, msg));
                }
                ToastUtils.showShort(DoctorServiceActivity.this,msg);
            }

            @Override
            public void onSuccess(ArrayList<EvaluateListBean> listItems) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("getDoctorList", PUtils.toJson(listItems)));
                }

                if(listItems.size() <= 2){
                    evaluateList.addAll(listItems);
                }else {
                    evaluateList.add(listItems.get(0));
                    evaluateList.add(listItems.get(1));
                }
                evaluateAdapter.notifyDataSetChanged();
            }
        });

        mEvaluateClient = NetService.createClient(this, getEvaluate);
        mEvaluateClient.start();
    }


    public static class EvaluateListAdapter extends CommonAdapter<EvaluateListBean> {

        private Context context;
        private LayoutInflater inflater;
        private List<EvaluateListBean> list;

        public EvaluateListAdapter(Context context, List<EvaluateListBean> list) {
            super(context, R.layout.item_evaluate_list, list);
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.list = list;
        }

        public void setData(List<EvaluateListBean> list){
            this.list.clear();
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        public void addData(List<EvaluateListBean> list){
            this.list.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public EvaluateListBean getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_evaluate_list, null);
                ViewUtils.inject(holder, convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final EvaluateListBean evaluateListBean = list.get(position);

		    holder.tv_name.setText(evaluateListBean.getUserName());
            holder.tv_type.setText(PUtils.getEvaluateType(context,""+evaluateListBean.getServiceType()));
            holder.tv_time.setText(evaluateListBean.getCreateTime().substring(0,10));
            holder.tv_content.setText(evaluateListBean.getContent());
            holder.rb_score.setRating(evaluateListBean.getScore());

            //分数
            if(evaluateListBean.getEvaluationTags() != null){
                holder.tcl_container.setData(new TagBaseAdapter(context,java.util.Arrays.asList(evaluateListBean.getEvaluationTags().split(";")),TagBaseAdapter.GRAY));
            }

            ImageLoader.getInstance().displayImage(evaluateListBean.getUserPhotoUrl(), holder.iv_head, PersonInfoActivity.getCircleDisplayOptions(R.drawable.default_avatar_patient));
            return convertView;
        }

        @Override
        public void convert(com.winson.ui.widget.ViewHolder viewHolder, EvaluateListBean obj, int position) {

        }

        private class ViewHolder {
            @ViewInject(R.id.tv_patient_name)
            private TextView tv_name;

            @ViewInject(R.id.ratingbarId)
            private RatingBar rb_score;

            @ViewInject(R.id.tv_content)
            private TextView tv_content;

            @ViewInject(R.id.tv_type)
            private TextView tv_type;

            @ViewInject(R.id.tv_time)
            private TextView tv_time;

            @ViewInject(R.id.iv_patient_head)
            private ImageView iv_head;

            @ViewInject(R.id.tcl_container)
            private TagCloudLayout tcl_container;
        }
    }

    private void getDoctorDetailInfo() {
        HttpDoctor.GetDetail getDoctorDetail = new HttpDoctor.GetDetail(mDoctorItem.mDoctorID, new HttpListener<Doctor.Detail>() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("getDoctorDetail", code, msg));
                }
//                EmptyViewUtils.showErrorView((ViewGroup) root, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        getDoctorDetailInfo();
//                    }
//                });
                ToastMananger.showToast(DoctorServiceActivity.this, R.string.loading_error, Toast.LENGTH_SHORT );

            }

            @Override
            public void onSuccess(Doctor.Detail detail) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("getDoctorDetail", PUtils.toJson(detail)));
                }
                mDoctorDetail = detail;
                updateDoctorInfo();
//                EmptyViewUtils.removeAllView((ViewGroup) root);
            }
        });

        HttpClient getDoctorDetailClient = NetService.createClient(this, getDoctorDetail);
        getDoctorDetailClient.start();
    }

    private void changeFollow(final String doctorId, final boolean follow){
        showLoadDialog(R.string.setting);
        FollowDoctorEvent.ChangeFollow changeFollow = new FollowDoctorEvent.ChangeFollow(
                doctorId,
                new HttpListener() {
                    @Override
                    public void onError(int code, String msg) {
                        dismissLoadDialog();

                        if (follow) {
                            ToastUtils.showLong(DoctorServiceActivity.this,R.string.follow_fail);
                        }
                        else{
                            ToastUtils.showLong(DoctorServiceActivity.this,R.string.unfollow_fail);
                        }

                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.errorFormat("changeFollow", code, msg));
                        }
                    }
                    @Override
                    public void onSuccess(Object o) {
                        dismissLoadDialog();
                        if (DebugUtils.debug) {
                            Log.d(TAG, DebugUtils.successFormat("changeFollow", ""));
                        }

                        if (follow) {
                            ToastUtils.showLong(DoctorServiceActivity.this,R.string.unfollow_success);
                            tv_follow.setBackgroundResource(R.drawable.btn_follow);
                            tv_follow.setText(R.string.follow);
                            tv_follow.setTextColor(getResources().getColor(R.color.color_main_green));
                            mIsFollow = !follow;
                        }
                        else{
                            ToastUtils.showLong(DoctorServiceActivity.this,R.string.follow_success);
                            tv_follow.setBackgroundResource(R.drawable.btn_unfollow);
                            tv_follow.setText(R.string.already_follow);
                            tv_follow.setTextColor(getResources().getColor(R.color.second_text_color));
                            mIsFollow = !follow;
                        }
                        getDoctorDetailInfo();
                    }
                }
        );

        HttpClient changeFollowClient = NetService.createClient(DoctorServiceActivity.this, changeFollow);
        changeFollowClient.start();
    }

}
