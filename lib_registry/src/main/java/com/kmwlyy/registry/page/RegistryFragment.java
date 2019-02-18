package com.kmwlyy.registry.page;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.base.BaseFragment;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.UserMember;
import com.kmwlyy.core.util.SPUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.registry.R;
import com.kmwlyy.registry.R2;
import com.kmwlyy.registry.bean.Contants;
import com.kmwlyy.registry.bean.IdCard;
import com.kmwlyy.registry.bean.Registry;
import com.kmwlyy.registry.net.NetBean;
import com.kmwlyy.registry.net.NetEvent;
import com.kmwlyy.usermember.UserMemberSelectActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016-8-15.
 */
public class RegistryFragment extends BaseFragment {

    public static final String TAG_DOCTOR = "TAG_DOCTOR";
    public static final String TAG_SCHEDULEL = "TAG_SCHEDULEL";
    private int[] Items = {R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item5};

    @BindView(R2.id.tv_name)
    TextView mNameText;
    @BindView(R2.id.et_code)
    EditText mCodeEdit;
    @BindView(R2.id.et_phone)
    EditText mPhoneEdit;
    @BindView(R2.id.tv_time)
    TextView mTimeText;
    @BindView(R2.id.btn_commit)
    Button mCommitBtn;
    @BindView(R2.id.registry_d_card_root)
    LinearLayout registry_d_card_root;
    @BindView(R2.id.registry_first)
    RadioButton registry_first;
    @BindView(R2.id.registry_return)
    RadioButton registry_return;
    @BindView(R2.id.et_cardNumber)
    EditText et_cardNumber;
    @BindView(R2.id.et_Password)
    EditText et_Password;
    @BindView(R2.id.radioGroup)
    RadioGroup radioGroup;

    @BindView(R2.id.tv_cardNumber)
    TextView tv_cardNumber;
    @BindView(R2.id.ll_registry_first)
    LinearLayout ll_registry_first;
    @BindView(R2.id.ll_registry_card)
    LinearLayout ll_registry_card;
    @BindView(R2.id.ll_registry_password)
    LinearLayout ll_registry_password;
    @BindView(R2.id.line_registry_first)
    View line_registry_first;
    @BindView(R2.id.line_registry_card)
    View line_registry_card;
    @BindView(R2.id.line_registry_password)
    View line_registry_password;

    private UserMember mMember;
    private NetBean.Doctor mDoctor;
    private NetBean.Schedule mSchedule;
    private NetBean.SchDetl mSelectSchDetl;
    private List<NetBean.SchDetl> mSchDetlList;
    private String name;
    private String mobile;
    private String idNumber;
    private String id;
    private String userId;
    private String cardNo = null;//诊疗卡卡号
    private String password = null;//诊疗卡密码
    private int visit_Status = -1;// 1初诊,2复诊 默认 1

    private int uid = 0;

    private NetBean.HospitalRegistryConfig registryConfig;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_registry;
    }

    @Override
    protected void afterBindView() {

        mDoctor = (NetBean.Doctor) getArguments().getSerializable(TAG_DOCTOR);
        mSchedule = (NetBean.Schedule) getArguments().getSerializable(TAG_SCHEDULEL);
        uid = mDoctor.getUNIT_ID();
        getMemberDefault();
        getScheduleDetail();
        getNeedDiagnoseCard();//联网获取是否需要诊疗卡
        mCenterText.setText(getResources().getString(R.string.confirm_register));
        for (int i = 0; i < Items.length; i++) setTextData(i);

        mNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectUserMember();
            }
        });
        mTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSchDetl();
            }
        });
        mCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commit();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectMember(UserMember member) {
        if (member != null) {
            mMember = member;
            userId = member.mUserId;
            id = member.mMemberID;
            name = member.mMemberName;
            mobile = member.mMobile;
            idNumber = member.mIDNumber;
            mNameText.setText(name!=null?name:"");
           //mPhoneEdit.setText(member.mMobile!=null?member.mMobile:"");
            //mCodeEdit.setText(member.mIDNumber!=null?member.mIDNumber:"");
        }
    }

    @Override
    public boolean isRegisterEventBus() {
        return true;
    }

    /*******************************************************/
    private void setTextData(int index) {
        View v = getView().findViewById(Items[index]);
        TextView key = (TextView) v.findViewById(R.id.tv_key);
        TextView value = (TextView) v.findViewById(R.id.tv_value);
        switch (index) {
            case 0:
                key.setText(getResources().getString(R.string.visit_hospital));
                value.setText(mDoctor.getUNIT_NAME());
                break;
            case 1:
                key.setText(getResources().getString(R.string.visit_department));
                value.setText(mDoctor.getDEP_NAME());
                break;
            case 2:
                key.setText(getResources().getString(R.string.visit_doctor));
                value.setText(mDoctor.getDOCTOR_NAME());
                break;
            case 3:
                key.setText(getResources().getString(R.string.doctor_title));
                value.setText(Contants.getDoctorZC(getActivity(), mDoctor.getZCID()));
                break;
            case 4:
                key.setText(getResources().getString(R.string.expenses));
                value.setText(mSchedule.getGUAHAO_AMT() + getResources().getString(R.string.money));
                break;
        }
    }

    private void selectUserMember() {
        Intent intent = new Intent(mContext, UserMemberSelectActivity.class);
        startActivityForResult(intent, UserMemberSelectActivity.SELECT_USER_MEMBER_REQ);
    }

    /* 联网获取是否需要诊疗卡 */
    private void getNeedDiagnoseCard(){
//        ((BaseActivity)getActivity()).showDialog(getString(R.string.r_get_net_data));
        NetEvent.GetNeedDiagnoseCard event = new NetEvent.GetNeedDiagnoseCard(String.valueOf(uid));
        event.setHttpListener(new HttpListener<NetBean.HospitalRegistryConfig>() {
            @Override
            public void onError(int code, String msg) {
//                ((BaseActivity)getActivity()).hideDialog();
                ToastUtils.showLong(getActivity(),msg);
                getActivity().finish();

            }

            @Override
            public void onSuccess(NetBean.HospitalRegistryConfig config) {
//                ((BaseActivity)getActivity()).hideDialog();
                registryConfig = config;
                updateDiagnoseCardLayout();

            }
        });
        new HttpClient(mContext, event).start();
    }

    /* 更新是否需要诊疗卡的页面 */
    private void updateDiagnoseCardLayout(){
        try {
            if (!TextUtils.isEmpty(registryConfig.clinc_no_config.clinic_no_name)) {
                tv_cardNumber.setText(registryConfig.clinc_no_config.clinic_no_name);
            }


            //需要就诊卡密码
            if (registryConfig.clinc_no_config.need_clinic_no_pass == 1) {
                ll_registry_password.setVisibility(View.VISIBLE);
                line_registry_password.setVisibility(View.VISIBLE);
            }else{
                ll_registry_password.setVisibility(View.GONE);
                line_registry_password.setVisibility(View.GONE);
            }
            //是否需要填初次就诊
            if (registryConfig.clinc_no_config.is_first_return_visit == 1) {
                ll_registry_first.setVisibility(View.VISIBLE);
                line_registry_first.setVisibility(View.VISIBLE);
            }else{
                ll_registry_first.setVisibility(View.GONE);
                line_registry_first.setVisibility(View.GONE);
            }
            //是否需要填就诊卡号
            if (registryConfig.clinc_no_config.need_clinc_no == 1) {
                ll_registry_card.setVisibility(View.VISIBLE);
                line_registry_card.setVisibility(View.VISIBLE);
            }else{
                ll_registry_card.setVisibility(View.GONE);
                line_registry_card.setVisibility(View.GONE);
            }

            if (registryConfig.clinc_no_config.need_clinic_no_pass == 1
                    || registryConfig.clinc_no_config.is_first_return_visit == 1
                    || registryConfig.clinc_no_config.need_clinc_no == 1){
                registry_d_card_root.setVisibility(View.VISIBLE);
            }else{
                registry_d_card_root.setVisibility(View.GONE);
            }
        }catch (Exception e){}

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (registry_first.isChecked()){
                    //初诊不需要诊疗卡
                    ll_registry_card.setVisibility(View.GONE);
                    line_registry_card.setVisibility(View.GONE);
                } else{//复诊需要诊疗卡
                    ll_registry_card.setVisibility(View.VISIBLE);
                    line_registry_card.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void selectSchDetl() {
        if (mSchDetlList != null) {
            String[] datas = new String[mSchDetlList.size()];
            for (int i = 0; i < mSchDetlList.size(); i++) {
                NetBean.SchDetl item = mSchDetlList.get(i);
                datas[i] = String.format(getResources().getString(R.string.leave_signal_resource), item.getBEGIN_TIME(), item.getEND_TIME(), item.getLEFT_NUM());
            }
            new AlertDialog.Builder(mContext).setItems(datas, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    NetBean.SchDetl item = mSchDetlList.get(which);
                    if (item.getLEFT_NUM() == 0) {
                        ToastUtils.showShort(mContext, getResources().getString(R.string.have_no_signal));
                    } else {
                        mSelectSchDetl = item;
                        if (TextUtils.isEmpty(mSchedule.getTO_DATE())) {
                            mTimeText.setText(String.format(getResources().getString(R.string.duration_format), item.getBEGIN_TIME(), item.getEND_TIME()));
                        }
                        else{
                            mTimeText.setText(mSchedule.getTO_DATE()+" "+String.format(getResources().getString(R.string.duration_format), item.getBEGIN_TIME(), item.getEND_TIME()));
                        }
                    }
                }
            }).show();
        }
    }

    private void commit() {//提交数据
        if (id == null|| id == "") {
            ToastUtils.showShort(mContext,getResources().getString(R.string.select_visit_member));
            return;
        }

        cardNo = et_cardNumber.getText().toString();
        password = et_Password.getText().toString();
        //是否需要填诊疗卡号
        if (registryConfig.clinc_no_config.need_clinc_no == 1 && TextUtils.isEmpty(cardNo) ) {
            ToastUtils.showShort(mContext,getResources().getString(R.string.r_input_diagnose_card));
            return;
        }

        //需要就诊卡密码
        if (registryConfig.clinc_no_config.need_clinic_no_pass == 1 && TextUtils.isEmpty(password) ) {
            ToastUtils.showShort(mContext,getResources().getString(R.string.r_input_diagnose_password));
            return;
        }

        //初诊复诊
        if (registryConfig.clinc_no_config.is_first_return_visit == 1)
        {
            if (registry_return.isChecked()){
                visit_Status = 2;
            }else{
                visit_Status = 1;
            }
        }




    /*    String code = mCodeEdit.getText().toString().trim();
        if (!Validator.isIDCard(code)) {
            ToastUtils.showShort(mContext, "请输入正确的身份证号");
            return;
        }
        String phone = mPhoneEdit.getText().toString().trim();
        if (!Validator.isMobile(phone)) {
            ToastUtils.showShort(mContext, "请输入正确的手机号码");
            return;
        }*/
        if (mSelectSchDetl == null) {
            ToastUtils.showShort(mContext, getResources().getString(R.string.select_visit_duration));
            return;
        }
        Registry item = new Registry();
        item.setUserid(userId);//BaseApplication.instance.getUserData().mUserId);//"6621F39A9CD14B17BE795D5B3C4DE321");
        item.setMemberid(id);//"15303FD934B748A6B38AB35B0E9FCF02");
        item.setPhone(mobile);//"18003758931");
        item.setCard(idNumber);//"410402196304120027");
        item.setCard_type("01");
        item.setTruename(name);//"患者");
        item.setSex(IdCard.parseGender(idNumber));//"1");
        item.setBirth(IdCard.parseBirthday(idNumber));//"1963-04-12");

        item.setDetlid(mSelectSchDetl.getDETL_ID());
        item.setUid(mDoctor.getUNIT_ID() + "");
        item.setDep_id(mDoctor.getDEP_ID() + "");
        item.setDoctor_id(mDoctor.getDOCTOR_ID() + "");
        setYuyue(item);
    }

    /*******************************************************/
    private void getScheduleDetail() {//获取排班详情
        NetEvent.GetSchDetl event = new NetEvent.GetSchDetl(mDoctor.getUNIT_ID() + "", mSchedule.getSCHEDULE_ID() + "");
        event.setHttpListener(new HttpListener<List<NetBean.SchDetl>>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(mContext, msg);
            }

            @Override
            public void onSuccess(List<NetBean.SchDetl> schDetls) {
                mSchDetlList = schDetls;
            }
        });
        new HttpClient(mContext, event).start();
    }

    private void setYuyue(Registry item) {//提交
        showDialog(getResources().getString(R.string.on_submit));
        NetEvent.SetYuyue event;

        event = new NetEvent.SetYuyue(item,cardNo,password,visit_Status);
        event.setHttpListener(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(mContext, msg);
                hideDialog();
            }

            @Override
            public void onSuccess(String s) {
                ToastUtils.showShort(mContext, getResources().getString(R.string.register_success));
                hideDialog();
                Intent intent = new Intent(mContext, ContainerActivity.class);
                intent.putExtra(ContainerActivity.TAG_FRAGMENT, ContainerActivity.REGISTRYLIST);
                startActivity(intent);
                getActivity().finish();
            }
        });
        new HttpClient(mContext, event).start();
    }

    private void getMemberDefault() {//获取默认成员
        NetEvent.GetMemberDefault event = new NetEvent.GetMemberDefault();
        event.setHttpListener(new HttpListener<ArrayList<UserMember>>() {
            @Override
            public void onError(int code, String msg) {

            }

            @Override
            public void onSuccess(ArrayList<UserMember> userMembers) {
                if (userMembers != null){
                    userId = userMembers.get(0).mUserId;
                    id = userMembers.get(0).mMemberID;
                    name = userMembers.get(0).mMemberName;
                    mobile = userMembers.get(0).mMobile;
                    idNumber = userMembers.get(0).mIDNumber;
                    mNameText.setText(name!=null?name:"");
                }else{
                    userId = "";
                    id = "";
                    name = "";
                    mobile = "";
                    idNumber = "";
                    mNameText.setText(name!=null?name:"");
                }
            }
        });
        new HttpClient(mContext, event).start();
    }
}