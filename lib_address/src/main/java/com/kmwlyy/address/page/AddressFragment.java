package com.kmwlyy.address.page;

import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kmwlyy.address.R;
import com.kmwlyy.address.R2;
import com.kmwlyy.address.net.Address;
import com.kmwlyy.address.net.AddressEvent;
import com.kmwlyy.address.utils.AssetsUtils;
import com.kmwlyy.core.base.BaseFragment;
import com.kmwlyy.core.net.HttpClient;
import com.kmwlyy.core.net.HttpEvent;
import com.kmwlyy.core.net.HttpListener;
import com.kmwlyy.core.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import cn.qqtheme.framework.picker.AddressPicker;

/**
 * Created by Administrator on 2016-8-18.
 */
public class AddressFragment extends BaseFragment {

    public static final String TAG_ADDRESS = "TAG_ADDRESS";
    public static final int ACTION_ADD = 0;
    public static final int ACTION_EDIT = 1;

    // 控件
    @BindView(R2.id.et_username)
    EditText mNameEdit;

    @BindView(R2.id.et_phone)
    EditText mPhoneEdit;

    @BindView(R2.id.tv_area)
    TextView mAreaText;

    @BindView(R2.id.et_addr)
    EditText mAddrEdit;

    @BindView(R2.id.cb_default)
    CheckBox mDefaultBox;

    @BindView(R2.id.layout_default)
    LinearLayout mDefaultLayout;

    private int mAction;
    private Address mAddress;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_address;
    }

    @Override
    protected void afterBindView() {
        mRightText.setText("保存");
        mRightText.setVisibility(View.VISIBLE);

        initAddressPicker();
        mAddress = (Address) getArguments().getSerializable(TAG_ADDRESS);
        if (mAddress != null) {
            mAction = ACTION_EDIT;
            mCenterText.setText("编辑配送地址");
            mNameEdit.setText(mAddress.getUserName());
            mPhoneEdit.setText(mAddress.getMobile());
            mAddrEdit.setText(mAddress.getDetailAddress());
            mAreaText.setText(mAddress.getProvinceName() + mAddress.getCityName() + mAddress.getAreaName());
        } else {
            mAction = ACTION_ADD;
            mCenterText.setText("新增配送地址");
            mAddress = new Address();
        }
        mAreaText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressPicker(mAddress.getProvinceName(), mAddress.getCityName(), mAddress.getAreaName());
            }
        });
        mDefaultLayout.setVisibility(View.GONE);//mAddress.isIsDefault() ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onRightClick(View view) {
        //super.onRightClick(view);
        String name = mNameEdit.getText().toString().trim();
        String phone = mPhoneEdit.getText().toString().trim();
        String area = mAreaText.getText().toString().trim();
        String addr = mAddrEdit.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort(mContext, "收货人不能为空");
            return;
        }
        if (!phone.matches("^[1][0-9]{10}$")) {// 判断手机号码合法性
            Toast.makeText(mContext, "联系电话不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(area)) {
            ToastUtils.showShort(mContext,"所在地区不能为空");
            return;
        }
        if (TextUtils.isEmpty(addr)) {
            ToastUtils.showShort(mContext, "详细地址不能为空");
            return;
        }

        mAddress.setUserName(name);
        mAddress.setMobile(phone);
        mAddress.setDetailAddress(addr);

        commit();
    }

    /*******************************************************/
    private void commit() {
        showDialog("正在保存，请稍等...");
        HttpEvent event;
        if (mAction == ACTION_EDIT) {
            event = new AddressEvent.Update(mAddress);
        } else {
            event = new AddressEvent.Add(mAddress);
        }
        event.setHttpListener(new HttpListener<String>() {
            @Override
            public void onError(int code, String msg) {
                ToastUtils.showShort(mContext, msg);
                hideDialog();
            }

            @Override
            public void onSuccess(String result) {
                hideDialog();
                ToastUtils.showShort(mContext, "保存成功");
                EventBus.getDefault().post(new AddressListFragment.AddressUpdate());
                getActivity().finish();
            }
        });
        new HttpClient(mContext, event).start();
    }

    /*******************************************************/
    private ArrayList<AddressPicker.Province> mPickData = null;

    private void initAddressPicker() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPickData = new ArrayList<>();
                try {
                    String json = AssetsUtils.readText(mContext, "city.json");
                    Type type = new TypeToken<ArrayList<AddressPicker.Province>>() {
                    }.getType();
                    mPickData.addAll((ArrayList<AddressPicker.Province>) new GsonBuilder().create().fromJson(json, type));
                    //JSON.parseArray(json, AddressPicker.Province.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showAddressPicker(String selectedProvince, String selectedCity, String selectedCounty) {
        if (mPickData != null) {
            AddressPicker picker = new AddressPicker((ContainerActivity) mContext, mPickData);
            //picker.setHideCounty(hideCounty);
            picker.setSelectedItem(selectedProvince, selectedCity, selectedCounty);
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(String province, String city, String county) {
                    mAddress.setProvinceName(province);
                    mAddress.setCityName(city);
                    mAddress.setAreaName(county);
                    mAreaText.setText(province + city + county);
//                    if (county==null){
//                        Toast.makeText(AddrAddActivity.this, province + city, Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(AddrAddActivity.this, province + city + county, Toast.LENGTH_LONG).show();
//                    }
                }
            });
            picker.show();
        } else {
            initAddressPicker();
            ToastUtils.showShort(mContext, "正在加载，请稍后再试！");
        }
    }
}
