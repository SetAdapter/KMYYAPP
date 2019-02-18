package com.kmwlyy.usermember;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.kmwlyy.core.base.BaseActivity;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpFilter;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.RegionsTreeBean;
import com.kmwlyy.core.net.bean.UserMember;
import com.kmwlyy.core.net.event.HttpUserMember;
import com.kmwlyy.core.net.event.Http_SignInform;
import com.kmwlyy.core.util.DebugUtils;
import com.kmwlyy.core.util.LogUtils;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.core.util.pickerview.OptionsPickerView;
import com.winson.ui.widget.ToastMananger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * Created by Winson on 2016/8/17.
 */
public class UserMemberEditActivity extends BaseActivity {

    private static final String TAG = UserMemberEditActivity.class.getSimpleName();

    static final String IS_EDIT = "IS_EDIT";
    static final String UER_MEMBER = "UER_MEMBER";
    private HttpClient mUpdateUserMemberClient;

    public static void startUserMemberEditActivity(Context context, boolean isEdit, UserMember userMember) {
        Intent intent = new Intent(context, UserMemberEditActivity.class);
        intent.putExtra(IS_EDIT, isEdit);
        intent.putExtra(UER_MEMBER, userMember);
        context.startActivity(intent);
    }

    @BindView(R2.id.tv_left)
    TextView mLeftTxt;
    @BindView(R2.id.tv_center)
    TextView mToolbarTitle;
    @BindView(R2.id.member_name)
    EditText mMemberName;
    @BindView(R2.id.member_relation)
    TextView mMemberRelation;
    @BindView(R2.id.member_gender)
    TextView mMemberGender;
    @BindView(R2.id.member_birth)
    TextView mMemberBirth;
    @BindView(R2.id.member_mobile)
    EditText mMemberMobile;
    @BindView(R2.id.member_idnumber)
    EditText mMemberIdnumber;
    @BindView(R2.id.member_address)
    TextView mMemberAddress;
    @BindView(R2.id.tv_town)
    TextView tv_town;
    @BindView(R2.id.ll_town)
    LinearLayout ll_town;
    @BindView(R2.id.ll_city)
    LinearLayout ll_city;
    @BindView(R2.id.member_email)
    EditText mMemberEmail;
    @BindView(R2.id.confirm)
    Button mMconfirm;
    @BindView(R2.id.ll_gender)
    LinearLayout mGenderLayout;
    @BindView(R2.id.ll_date_of_birth)
    LinearLayout mDateOfBirthLayout;
    @BindView(R2.id.ll_relation)
    LinearLayout mRelationLayout;

    private ProgressDialog mOnAddDialog;
    private ProgressDialog mOnUpdateDialog;
    private HttpClient mAddUserMemberClient;
    private boolean mIsEdit;
    private UserMember mOldUserMember;
    private List<RegionsTreeBean.DataBean> options1Items = new ArrayList<>();
    private List<ArrayList<String>> options2Items = new ArrayList<>();
    private List<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private List<String> options4Items = new ArrayList<>();
    private String mProvinceStr;
    private String mCityStr;
    private String mCountyStr;
    private String mTownStr;

    private String mProvinceId;
    private String mCityId;
    private String mCountyId;
    private String mTownId;
    private Map<String, RegionsTreeBean.DataBean.RegionBean> strweetMap = new HashMap<>();
    private Map<String, RegionsTreeBean.DataBean> districtMap = new HashMap<>();

    private boolean isOpen = true;

    @Override
    protected int getLayoutId() {
        return R.layout.user_member_add;
    }

    @Override
    protected void afterBindView() {
        mIsEdit = getIntent().getBooleanExtra(IS_EDIT, false);
        mOldUserMember = (UserMember) getIntent().getSerializableExtra(UER_MEMBER);
        //getHttp_getRegions();
        if (mIsEdit) {
            mToolbarTitle.setText(R.string.edit_user_member);
            mMemberName.setText(mOldUserMember.mMemberName);
            mMemberMobile.setText(mOldUserMember.mMobile);
            //显示身份证并去敏
            String mIDNumber = mOldUserMember.mIDNumber;
            mMemberIdnumber.setText(mIDNumber.substring(0, 6) + "********" + mIDNumber.substring(14,18));

            if (mOldUserMember.mGender == 0){
                mMemberGender.setText("男");
            }else if(mOldUserMember.mGender == 1){
                mMemberGender.setText("女");
            }else{
                mMemberGender.setText("其他");
            }
            switch (mOldUserMember.mRelation){
                case 0:
                    mMemberRelation.setText("自己");
                    break;
                case 1:
                    mMemberRelation.setText("配偶");
                    break;
                case 2:
                    mMemberRelation.setText("父亲");
                    break;
                case 3:
                    mMemberRelation.setText("母亲");
                    break;
                case 4:
                    mMemberRelation.setText("儿子");
                    break;
                case 5:
                    mMemberRelation.setText("女儿");
                    break;
                case 6:
                    mMemberRelation.setText("其他");
                    break;
            }
            mMemberBirth.setText(mOldUserMember.mBirthday);
            mMemberEmail.setText(mOldUserMember.mEmail);
            if (mOldUserMember.mRelation == 0){
                isOpen = false;
            }

        } else {
            mToolbarTitle.setText(R.string.add_user_member);
        }

        mLeftTxt.setOnClickListener(this);
        mMconfirm.setOnClickListener(this);
        mGenderLayout.setOnClickListener(this);
        mDateOfBirthLayout.setOnClickListener(this);
        ll_city.setOnClickListener(this);
        ll_town.setOnClickListener(this);
        findViewById(R.id.tv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsEdit) {
                    updateUserMember();
                } else {
                    addUserMember();
                }
            }
        });
        findViewById(R.id.ll_gender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGenderPick();
            }
        });
        findViewById(R.id.ll_date_of_birth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDataPick();
            }
        });
        findViewById(R.id.ll_relation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    showRelationPick();
                }else{
                    ToastUtils.showShort(UserMemberEditActivity.this,"关系为本人，不能修改");
                }
            }
        });
        findViewById(R.id.ll_city).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPickerView();
            }
        });
        findViewById(R.id.ll_town).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStrweetDialog();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R2.id.tv_left:
                onBackPressed();
                break;
            case R2.id.confirm:
                if (mIsEdit) {
                    updateUserMember();
                } else {
                    addUserMember();
                }
                break;
            case R2.id.ll_gender:
                showGenderPick();
                break;
            case R2.id.ll_date_of_birth:
                showDataPick();
                break;
            case R2.id.ll_relation:
                showRelationPick();
                break;
            case R2.id.ll_city:
                ShowPickerView();
                break;
            case R2.id.ll_town:
                showStrweetDialog();
                break;

        }
    }

    private void showAddDialog() {
        mOnAddDialog = new ProgressDialog(this);
        mOnAddDialog.setMessage(getResources().getString(R.string.on_add_user_member));
        mOnAddDialog.setCancelable(false);
        mOnAddDialog.show();
    }

    private void dismissAddDialog() {
        if (mOnAddDialog != null) {
            mOnAddDialog.dismiss();
        }
    }

    private void showUpdateDialog() {
        mOnUpdateDialog = new ProgressDialog(this);
        mOnUpdateDialog.setMessage(getResources().getString(R.string.on_update_user_member));
        mOnUpdateDialog.setCancelable(false);
        mOnUpdateDialog.show();
    }

    private void dismissUpdateDialog() {
        if (mOnUpdateDialog != null) {
            mOnUpdateDialog.dismiss();
        }
    }

    private void updateUserMember() {
        UserMember params = getUserMember();
        if (params == null) {
            return;
        }

        params.mUserId = mOldUserMember.mUserId;
        params.mMemberID = mOldUserMember.mMemberID;
        showUpdateDialog();

        HttpUserMember.Update updateUserMember = new HttpUserMember.Update(params, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.errorFormat("updateUserMember", code, msg));
                }
                ToastMananger.showToast(UserMemberEditActivity.this, msg, Toast.LENGTH_SHORT);
                dismissUpdateDialog();
            }

            @Override
            public void onSuccess(Object o) {
                if (DebugUtils.debug) {
                    Log.d(TAG, DebugUtils.successFormat("updateUserMember", o));
                }

                ToastMananger.showToast(UserMemberEditActivity.this, R.string.update_member_success, Toast.LENGTH_SHORT);
                dismissUpdateDialog();
                EventBus.getDefault().post(new Event.UserMemberUpdated());
                finish();
            }
        });

        mUpdateUserMemberClient = new HttpClient(this, updateUserMember);
        mUpdateUserMemberClient.start();

    }

    private void addUserMember() {
        UserMember params = getUserMember();
        if (params == null) {
            return;
        }

        showAddDialog();

        HttpUserMember.Add addUserMember = new HttpUserMember.Add(params, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                LogUtils.d(TAG, "addUserMember error , code : " + code + " , msg : " + msg);
                ToastMananger.showToast(UserMemberEditActivity.this, msg, Toast.LENGTH_SHORT);
                dismissAddDialog();
            }

            @Override
            public void onSuccess(Object o) {
                LogUtils.d(TAG, "addUserMember success");
                ToastMananger.showToast(UserMemberEditActivity.this, R.string.add_member_success, Toast.LENGTH_SHORT);
                dismissAddDialog();
                EventBus.getDefault().post(new Event.UserMemberUpdated());
                finish();
            }
        });

        mAddUserMemberClient = new HttpClient(this, addUserMember);
        mAddUserMemberClient.start();
    }

    @Nullable
    private UserMember getUserMember() {
        String memberName = mMemberName.getText().toString();

        if (TextUtils.isEmpty(memberName)) {
            ToastUtils.show(this, R.string.member_name_cannot_be_null, Toast.LENGTH_SHORT);
            return null;
        }

        String memberMobile = mMemberMobile.getText().toString();

//        if (TextUtils.isEmpty(memberMobile)) {
//            ToastUtils.show(this, R.string.member_mobile_cannot_be_null, Toast.LENGTH_SHORT);
//            return null;
//        }
//        if (!TextUtils.isEmpty(memberMobile)) {
//            if (!Validator.isMobile(memberMobile)) {
//                ToastUtils.showShort(this, "请输入正确的手机号码");
//                return null;
//            }
//        }

        String memberIdnumber = mMemberIdnumber.getText().toString();
        if (TextUtils.isEmpty(memberIdnumber)) {
            ToastUtils.show(this, "身份证号不能为空", Toast.LENGTH_SHORT);
            return null;
        }
        if (!TextUtils.isEmpty(memberIdnumber)) {
            if (!Validator.checkCardId(memberIdnumber)) {
                ToastUtils.showShort(this, "请输入正确的身份证号");
                return null;
            }
        }

        String memberRelation = mMemberRelation.getText().toString();
        String memberGender = mMemberGender.getText().toString();
        String mBirthDay = mMemberBirth.getText().toString();

        String memberEmail = mMemberEmail.getText().toString();
        if (TextUtils.isEmpty(memberGender)){
            ToastUtils.show(this, "请选择性别", Toast.LENGTH_SHORT);
            return null;
        }
        int gender = 0;
        if (memberGender.equals("女")){
            gender = 1;
        }else if(memberGender.equals("未知")){
            gender = 2;
        }
        if (TextUtils.isEmpty(mBirthDay)){
            ToastUtils.show(this, "请选择出生年月", Toast.LENGTH_SHORT);
            return null;
        }
        if (TextUtils.isEmpty(memberRelation)){
            ToastUtils.show(this, "请选择与本人关系", Toast.LENGTH_SHORT);
            return null;
        }
//        if (TextUtils.isEmpty(mProvinceStr)){
//            ToastUtils.show(this, "请选择地址", Toast.LENGTH_SHORT);
//            return null;
//        }
//        if (TextUtils.isEmpty(mTownStr)){
//            ToastUtils.show(this, "请选择街道地址", Toast.LENGTH_SHORT);
//            return null;
//        }

        int relation=6;//成员关系 （0-自己、1-配偶、2-父亲、3-母亲、4-儿子、5女儿、6-其他）
        if (memberRelation.equals("自己")){
            relation = 0;
        }else if(memberRelation.equals("配偶")){
            relation = 1;
        }else if(memberRelation.equals("父亲")){
            relation = 2;
        }else if(memberRelation.equals("母亲")){
            relation = 3;
        }else if (memberRelation.equals("儿子")){
            relation = 4;
        }else if (memberRelation.equals("女儿")){
            relation = 5;
        }
       /* if(mIsEdit){//如果是编辑就用数据传过来的值，如果是新增就用 6其他处理
            relation = mOldUserMember.mRelation;
        }*/

        UserMember params = new UserMember(
                memberName,
                relation,
                gender,
                0,
                mBirthDay,
                memberMobile,
                0,
                memberIdnumber
        );
        params.mEmail = memberEmail;
        params.mProvince = mProvinceStr;
        params.ProvinceRegionID = mProvinceId;
        params.mCity = mCityStr;
        params.mCityRegionID = mCityId;
        params.mDistrict = mCountyStr;
        params.mDistrictRegionID = mCountyId;
        params.mTown = mTownStr;
        params.mTownRegionID = mTownId;
        return params;
    }

    private void showDataPick(){
        String time = mMemberBirth.getText().toString();
        DatePicker picker = new DatePicker(UserMemberEditActivity.this);
        picker.setRange(1900,2030);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                Calendar c = Calendar.getInstance();
                int  systemYear = c.get(Calendar.YEAR);
                int  systemMonth = c.get(Calendar.MONTH);
                int systemDay = c.get(Calendar.DAY_OF_MONTH);
                systemMonth = systemMonth+1;
                if(systemYear < Integer.valueOf(year)){
                    ToastUtils.showShort(UserMemberEditActivity.this,"不能选择大于当前时间的值");
                    showDataPick();
                    return;
                }else if(systemYear == Integer.valueOf(year)){
                    if (systemMonth < Integer.valueOf(month)){
                        ToastUtils.showShort(UserMemberEditActivity.this,"不能选择大于当前时间的值");
                        showDataPick();
                        return;
                    }else if(systemMonth == Integer.valueOf(month)){
                        if (systemDay < Integer.valueOf(day)){
                            ToastUtils.showShort(UserMemberEditActivity.this,"不能选择大于当前时间的值");
                            showDataPick();
                            return;
                        }
                    }
                }
                mMemberBirth.setText(year+"-"+month+"-"+day);
            }
        });
        if (time == null || time.length() <= 0) {
            Calendar calendar=Calendar.getInstance();  //获取当前时间，作为图标的名字
            int year=calendar.get(Calendar.YEAR);
            int month=calendar.get(Calendar.MONTH)+1;
            int day=calendar.get(Calendar.DAY_OF_MONTH);
            picker.setSelectedItem(year,month,day);
        }else{
            String year = time.substring(0,4);
            String month = time.substring(5,7);
            String day = time.substring(8,10);
            picker.setSelectedItem(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
        }
        picker.show();
    }

    private void showGenderPick(){
        String[] option = {"男","女","未知"};
        OptionPicker picker = new OptionPicker(this,option);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                mMemberGender.setText(option);
            }
        });
        picker.show();
    }

    private void showRelationPick(){
        String[] option = {"自己","配偶","父亲","母亲","儿子","女儿","其他"};
        OptionPicker picker = new OptionPicker(this,option);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                mMemberRelation.setText(option);
            }
        });
        picker.show();
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
                    initJsonData(dataBeen);
                }
            }


        });
        new HttpClient(this, event,HttpClient.FAMILY_URL,new HttpFilter()).start();
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
                    for (RegionsTreeBean.DataBean strweetBean : strweetList){

                        strweetMap.put(strweetBean.getRegion().getRegionName(), strweetBean.getRegion());
                        options4Items.add(strweetBean.getRegion().getRegionName());
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
                mProvinceId = options1Items.get(options1).getRegion().getRegionID();
                mCityId = options1Items.get(options1).getChildRegions().get(options2).getRegion().getRegionID();
                mCountyId = options1Items.get(options1).getChildRegions().get(options2).getChildRegions().get(options3).getRegion().getRegionID();

//                Toast.makeText(InhabitanStartActivity.this, tx, Toast.LENGTH_SHORT).show();
                mMemberAddress.setText(tx);
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
        if (options1Items.size()!=0 &&options2Items.size()!=0&&options3Items.size()!=0){
            pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
            pvOptions.show();
        }
    }

    /**
     * 街道 弹窗dialog
     */
    private void showStrweetDialog(){
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
//                mAreaTv.setText(tx);
                String strweetName = options4Items.get(options1);
                RegionsTreeBean.DataBean.RegionBean strweetBean = strweetMap.get(strweetName);
                mTownStr = strweetName;
                mTownId = strweetBean.getRegionID();
                tv_town.setText(strweetName);
            }
        })
                .setCyclic(false, false, false)//循环与否
                .setLabels("", "", "")//设置选择的三级单位
                .setTitleText("街道选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        if (options4Items.size() != 0){
            pvOptions.setPicker(options4Items);
            pvOptions.show();
        } else {
            tv_town.setText("");
            Toast.makeText(UserMemberEditActivity.this, "该区(县)暂未收录对应街道!!!", Toast.LENGTH_SHORT).show();
        }
    }


}
