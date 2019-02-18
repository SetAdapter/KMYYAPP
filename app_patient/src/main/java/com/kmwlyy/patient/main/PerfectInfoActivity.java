package com.kmwlyy.patient.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kmwlyy.address.utils.AssetsUtils;
import com.kmwlyy.core.base.BaseApplication;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.net.bean.SignatureBean;
import com.kmwlyy.core.net.bean.UserData;
import com.kmwlyy.core.net.bean.UserMember;
import com.kmwlyy.core.net.event.GetMySignature;
import com.kmwlyy.core.net.event.HttpUserMember;
import com.kmwlyy.core.util.ToastUtils;
import com.kmwlyy.patient.R;
import com.kmwlyy.patient.helper.base.BaseActivity;
import com.kmwlyy.patient.helper.net.NetService;
import com.kmwlyy.patient.module.InhabitantStart.Http_SignInform;
import com.kmwlyy.patient.module.InhabitantStart.InhabitanStartActivity;
import com.kmwlyy.patient.module.InhabitantStart.RegionsTreeBean;
import com.kmwlyy.patient.weight.pickerview.OptionsPickerView;
import com.kmwlyy.usermember.Validator;
import com.winson.ui.widget.CleanableEditText;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xcj on 2017/8/17.
 */

public class PerfectInfoActivity extends BaseActivity{
    @BindView(R.id.user_name)
    CleanableEditText mUserName;
    @BindView(R.id.tv_center)
    TextView tv_center;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.id_card)
    CleanableEditText id_card;
    @BindView(R.id.tvStrweetName)
    TextView tvStrweetName;
    @BindView(R.id.area_rl)
    RelativeLayout mAreaRl;
    @BindView(R.id.row_rl)
    RelativeLayout mRowRl;
    @BindView(R.id.area_tv)
    TextView mAreaTv;
    @BindView(R.id.familyaddress)
    EditText mFamilyaddress;

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

    //key: 街道名称; value: 街道对象
    private Map<String, RegionsTreeBean.DataBean.RegionBean> strweetMap = new HashMap<>();
    private Map<String, RegionsTreeBean.DataBean> districtMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_info);
        ButterKnife.bind(this);
        tv_center.setText("信息完善");
        getHttp_getRegions();
        mAreaRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPickerView();
                closeKeyboard();
            }
        });
        mRowRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStrweetDialog();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
    }

    private void confirm(){
        String name = mUserName.getText().toString();
        final String idNumber = id_card.getText().toString();
        String detail = mFamilyaddress.getText().toString();

        if (TextUtils.isEmpty(name)){
            ToastUtils.showShort(PerfectInfoActivity.this,"请输入姓名");
            return;
        }

        if (TextUtils.isEmpty(idNumber)) {
            ToastUtils.show(this, "身份证号不能为空", Toast.LENGTH_SHORT);
            return ;
        }
        if (!TextUtils.isEmpty(idNumber)) {
            if (!Validator.checkCardId(idNumber)) {
                ToastUtils.showShort(this, "请输入正确的身份证号");
                return;
            }
        }
        if (TextUtils.isEmpty(mProvinceStr)){
            ToastUtils.show(this, "请选择地址", Toast.LENGTH_SHORT);
            return ;
        }
        if (TextUtils.isEmpty(mTownStr)){
            ToastUtils.show(this, "请选择街道地址", Toast.LENGTH_SHORT);
            return ;
        }
        if (TextUtils.isEmpty(detail)){
            ToastUtils.showShort(PerfectInfoActivity.this,"请输入详细地址");
            return;
        }

        UserMember params = new UserMember();
        params.mUserId = BaseApplication.getInstance().getUserData().mUserId;
        params.mMemberID = BaseApplication.getInstance().getUserData().MemberID;
        params.mAddress = detail;
        params.mMemberName = name;
        params.mIDNumber = idNumber;
        params.mProvince = mProvinceStr;
        params.ProvinceRegionID = mProvinceId;
        params.mCity = mCityStr;
        params.mCityRegionID = mCityId;
        params.mDistrict = mCountyStr;
        params.mDistrictRegionID = mCountyId;
        params.mTown = mTownStr;
        params.mTownRegionID = mTownId;
        params.mRelation = 0;
        params.mMobile = BaseApplication.getInstance().getUserData().mMobile;

        showLoadDialog(R.string.infoTitle);
        HttpUserMember.Update addUserMember = new HttpUserMember.Update(params, new HttpListener() {
            @Override
            public void onError(int code, String msg) {
                dismissLoadDialog();
            }

            @Override
            public void onSuccess(Object o) {
                dismissLoadDialog();
                UserData data = BaseApplication.getInstance().getUserData();
                data.mIDNumber = idNumber;
                BaseApplication.getInstance().setUserData(data);
                finish();
            }
        });

        new HttpClient(this, addUserMember).start();
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
                tvStrweetName.setText(strweetName);
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
            tvStrweetName.setText("");
            Toast.makeText(PerfectInfoActivity.this, "该区(县)暂未收录对应街道!!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
